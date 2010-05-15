package uk.co.danielrendall.fractdim.app.controller;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.GridSelectedEvent;
import uk.co.danielrendall.fractdim.app.gui.ResultPanelListener;
import uk.co.danielrendall.fractdim.app.gui.SettingsPanel;
import uk.co.danielrendall.fractdim.app.model.*;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.DoubleRangeModel;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;
import uk.co.danielrendall.fractdim.app.workers.SquareCountingWorker;
import uk.co.danielrendall.fractdim.calculation.FractalMetadataUtil;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.grids.Grid;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.logging.PrettyPrinter;
import uk.co.danielrendall.fractdim.svg.SVGContentGenerator;
import uk.co.danielrendall.fractdim.svg.SVGElementCreator;
import uk.co.danielrendall.fractdim.svg.Utilities;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:02:09
 * To change this template use File | Settings | File Templates.
 */
public class FractalController implements ParameterChangeListener, ResultPanelListener, Runnable {

    private enum Status {NEW, DOC_LOADED, READY_FOR_COUNT, COUNTING_SQUARES, SQUARES_COUNTED};

    private final static String CALC_STATS = "CalcStats";
    private final static String COUNT_SQUARES = "CountSquares";
    private final static String RESULT_GRID = "ResultGrid";
    private final static String MIN_GRID = "MinGrid";
    private final static String MAX_GRID = "MaxGrid";
    private final static String BOUNDING_BOX = "BoundingBox";

    public final static Parameter SQUARE_SIZES = new Parameter("CALC_SETTINGS", "SQUARE_SIZES", "Square sizes", "The range of square sizes to be used for counting");
    public final static Parameter NUMBER_RESOLUTIONS = new Parameter("CALC_SETTINGS", "NUMBER_RESOLUTIONS", "Number of resolutions", "The number of different square sizes between the minimum and maximum (inclusive)");
    public final static Parameter NUMBER_ANGLES = new Parameter("CALC_SETTINGS", "NUMBER_ANGLES", "Number of angles", "The number of different grid angles to be tried for each resolution");
    public final static Parameter NUMBER_DISPLACEMENTS = new Parameter("CALC_SETTINGS", "NUMBER_DISPLACEMENTS", "Number of displacements", "The number of different offsets within each square to be tried for each angle");

    private final FractalDocument document;
    private final FractalPanel panel;

    private DoubleRangeModel squareSizeModel;
    private BoundedRangeModel resolutionModel;
    private BoundedRangeModel angleModel;
    private BoundedRangeModel displacementModel;

    private final Thread controllerThread;

    private final Queue<Runnable> jobs;

    private volatile boolean shouldQuit = false;


    private final Action actionCalculateFractalDimension = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            actionCalculateFractalDimension();
        }
    };

    private final Action actionCloseFile = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            actionCloseFile();
        }
    };

    private volatile Status status = Status.NEW;

    public static FractalController fromFile(File file) throws IOException {
        SAXSVGDocumentFactory factory = Utilities.getDocumentFactory();
        SVGDocument doc = factory.createSVGDocument(file.toURI().toString());
        return fromDocument(doc, file.getName());
    }

    public static FractalController fromInputStream(InputStream inputStream) throws IOException {
        SAXSVGDocumentFactory factory = Utilities.getDocumentFactory();
        SVGDocument doc = factory.createSVGDocument("SomeURI", inputStream);
        return fromDocument(doc, "Inputstream " + new Date().getTime());
    }

    public static FractalController fromDocument(SVGDocument doc, String name) {
        FractalDocument document = new FractalDocument(doc, name);
        FractalController controller = new FractalController(document);
        return controller;
    }

    private FractalController(FractalDocument document) {
        this.document = document;
        this.panel = new FractalPanel();
        this.panel.getResultPanel().addResultPanelListener(this);
        this.jobs = new LinkedList<Runnable>();

        this.controllerThread = new Thread(this);
        this.controllerThread.setDaemon(true);

    }

    public void notifyAdded() {
        String threadName = "Controller: " + document.getName();
        controllerThread.setName(threadName);
        controllerThread.start();
        panel.updateProgressBar(0);
        panel.showProgressBar();
        addToQueue(new Runnable() {
            public void run() {
                generateMetaData();
            }
        });
    }

    public void notifyRemoving() {
        // nothing to do
    }

    public void notifyRemoved() {
        shouldQuit = true;
        controllerThread.interrupt();
        try {
            controllerThread.join();
            Log.thread.info("Joined controller thread");
        } catch (InterruptedException e) {
            Log.thread.warn("Couldn't join controller thread - " + e.getMessage());
        }
    }

    private void generateMetaData() {
        checkControllerThread();
        Log.thread.debug("Generating metadata");
        panel.updateProgressBar(33);
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(document.getSvgDoc());
        document.setMetadata(metadata);
        BoundingBox box = metadata.getBoundingBox();
        double maximumBoxSize = Math.min(box.getWidth(), box.getHeight());
        double minimumBoxSize = maximumBoxSize / 50.0d;

        double range = maximumBoxSize - minimumBoxSize;
        // start with a slider range just a little inside the real range.
        double rangeMin = minimumBoxSize + (range * 0.1d);
        double rangeExtent = range * 0.8d;
        this.squareSizeModel = new DoubleRangeModel(rangeMin, rangeExtent, minimumBoxSize, maximumBoxSize);
        this.resolutionModel = new DefaultBoundedRangeModel(2, 0, 2, 20);
        this.angleModel = new DefaultBoundedRangeModel(1, 0, 1, 10);
        this.displacementModel = new DefaultBoundedRangeModel(1, 0, 1, 3);
        this.status = Status.DOC_LOADED;

        Log.thread.debug("Populating settings panel");
        SettingsPanel settingsPanel = panel.getSettingsPanel();
        squareSizeModel.addChangeListener(new SimpleChangeListener(FractalController.this, SQUARE_SIZES));
        settingsPanel.setDataModelForParameter(SQUARE_SIZES, squareSizeModel, 0);

        resolutionModel.addChangeListener(new SimpleChangeListener(FractalController.this, NUMBER_RESOLUTIONS));
        settingsPanel.setDataModelForParameter(NUMBER_RESOLUTIONS, resolutionModel, 1);

        angleModel.addChangeListener(new SimpleChangeListener(FractalController.this, NUMBER_ANGLES));
        settingsPanel.setDataModelForParameter(NUMBER_ANGLES, angleModel, 1);

        displacementModel.addChangeListener(new SimpleChangeListener(FractalController.this, NUMBER_DISPLACEMENTS));
        settingsPanel.setDataModelForParameter(NUMBER_DISPLACEMENTS, displacementModel, 1);

        panel.updateProgressBar(66);
        panel.updateDocument(document);
        updateGrids();
        panel.updateProgressBar(100);
        panel.hideProgressBar();
        setStatus(Status.READY_FOR_COUNT);
        // All the setting up of the panel etc. will be done in the controller thread.
    }


    private synchronized void addToQueue(Runnable r) {
        jobs.add(r);
        controllerThread.interrupt();
    }

    private synchronized Runnable getFromQueue() {
        return jobs.poll();
    }


    // main lifecycle of controller. Does things, updates status.
    public void run() {
        while (!shouldQuit) {
            for (Runnable r = getFromQueue(); r != null; r = getFromQueue()) {
                Log.thread.info("Found a runnable in the queue");
                r.run();
            }

            try {
                Log.thread.info("Sleeping...");
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // ignore
            }
            Log.thread.info("Awoken");
        }
    }

//    public void setStatistics(Statistics statistics) {
//        if (status == Status.DOC_LOADED) {
//            // todo - some nicer way of selecting the algorithm for this...
//
//            status = Status.STATS_CALCULATED;
//            FractDim.instance().updateGlobal(this);
//        } else {
//            Log.app.warn("Wasn't expecting statistics when status was " + status);
//        }
//    }


    public FractalDocument getDocument() {
        return document;
    }

    public FractalPanel getPanel() {
        return panel;
    }

    // called when our panel becomes active
    public void enableMenuItems() {
        Log.gui.info("enableMenuItems called");
        ActionMap actionMap = panel.getActionMap();
        ActionRepository repository = ActionRepository.instance();
        repository.getFileClose().setDelegate(actionCloseFile);
        repository.getDiagramZoomIn().setDelegate(actionMap.get(JSVGCanvas.ZOOM_IN_ACTION));
        repository.getDiagramZoomOut().setDelegate(actionMap.get(JSVGCanvas.ZOOM_OUT_ACTION));
        if (isCapableOfCalculation()) {
            repository.getFileCalculate().setDelegate(actionCalculateFractalDimension);
        } else {
            repository.getFileCalculate().removeDelegate();
        }
    }

    private boolean isCapableOfCalculation() {
        return (status == Status.READY_FOR_COUNT || status == Status.SQUARES_COUNTED);
    }

    private void setStatus(Status newStatus) {
        status = newStatus;
        FractDim.instance().updateMeIfCurrent(this);
    }

    public void actionCloseFile() {
        // TODO - check we're in a fit state to close
        FractDim.instance().remove(this);
    }

    public void actionCalculateFractalDimension() {
        panel.getSettingsPanel().disableAllControls();
        panel.updateProgressBar(0);
        panel.showProgressBar();
        SquareCountingWorker scw = new SquareCountingWorker(this, squareSizeModel.getLowerValue(), squareSizeModel.getMaximum(), resolutionModel.getValue(), angleModel.getValue(), displacementModel.getValue(), COUNT_SQUARES);
        scw.execute();
        setStatus(Status.COUNTING_SQUARES);
        Log.calc.info("Started square counting worker");
    }
    

    public void setSquareCountingResult(SquareCountingResult squareCountingResult) {
        Log.calc.info("Square counting worker reported");
        panel.getSettingsPanel().enableAllControls();
        panel.getResultPanel().update(squareCountingResult);
        panel.hideProgressBar();
        setStatus(Status.SQUARES_COUNTED);
    }

    public void updateProgress(String taskId, int progress) {
//        Log.calc.debug("Task " + taskId + " progress " + progress);
        panel.updateProgressBar(progress);
    }


    private void updateGrids() {
        checkControllerThread();
        final Grid minGrid = new Grid(squareSizeModel.getValue());
        final Grid maxGrid = new Grid(squareSizeModel.getUpperValue());

        final BoundingBox boundingBox = document.getMetadata().getBoundingBox();

        Log.gui.info("Bounding box is " + boundingBox);

        panel.updateOverlay(BOUNDING_BOX, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                Element path = creator.createPath("#999999");
                path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s L %s,%s L %s,%s z",
                        boundingBox.getMinX(), boundingBox.getMinY(),
                        boundingBox.getMaxX(), boundingBox.getMinY(),
                        boundingBox.getMaxX(), boundingBox.getMaxY(),
                        boundingBox.getMinX(), boundingBox.getMaxY()));
                rootElement.appendChild(path);
                return boundingBox;
            }
        });

        panel.updateOverlay(MIN_GRID, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return minGrid.writeToSVG(rootElement, creator, boundingBox, "#99ff99");
            }
        });

        panel.updateOverlay(MAX_GRID, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return maxGrid.writeToSVG(rootElement, creator, boundingBox, "#9999ff");
            }
        });
    }

    private void updateResultGrid(final Grid theGrid) {
        checkControllerThread();
        final BoundingBox boundingBox = document.getMetadata().getBoundingBox();
        panel.updateOverlay(RESULT_GRID, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return theGrid.writeFilledToSVG(rootElement, creator, boundingBox, "#ff9999");
            }
        });
    }

    public void valueChanged(Parameter param, int value) {
        if (param.equals(SQUARE_SIZES)) {
            addToQueue(new Runnable() {
                public void run() {
                    Log.thread.debug("Updating grids in response to parameter change");
                    updateGrids();
                }
            });
        } else {
            Log.gui.debug("Ignoring change to param " + param.getId() + " to " + value);
        }
    }

    public void gridSelected(final GridSelectedEvent e) {
        addToQueue(new Runnable() {
            public void run() {
                Log.thread.debug("Grid selected: " + e.getGrid().toString());
                updateResultGrid(e.getGrid());
            }
        });
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void prettyPrint(String message, SVGDocument doc) {
        if (Log.gui.isDebugEnabled()) {
            Log.gui.debug(message);

            StringWriter sw = new StringWriter();
            PrettyPrinter pp = new PrettyPrinter(doc);
            pp.prettyPrint(sw);
            Log.gui.debug(sw.toString());
        }
    }

    private void checkControllerThread() {
        if (Thread.currentThread() != controllerThread) throw new IllegalStateException("Should be called in controller thread");
    }

}
