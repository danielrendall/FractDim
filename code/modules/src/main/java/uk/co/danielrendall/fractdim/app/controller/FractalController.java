package uk.co.danielrendall.fractdim.app.controller;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.GridSelectedEvent;
import uk.co.danielrendall.fractdim.app.gui.ResultPanelListener;
import uk.co.danielrendall.fractdim.app.model.*;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.app.workers.ExcelExportWorker;
import uk.co.danielrendall.fractdim.app.workers.Notifiable;
import uk.co.danielrendall.fractdim.app.workers.SquareCountingWorker;
import uk.co.danielrendall.fractdim.calculation.FractalMetadataUtil;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.grids.Grid;
import uk.co.danielrendall.fractdim.calculation.iterators.*;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.logging.PrettyPrinter;
import uk.co.danielrendall.fractdim.svg.SVGContentGenerator;
import uk.co.danielrendall.fractdim.svg.SVGElementCreator;
import uk.co.danielrendall.fractdim.svg.Utilities;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:02:09
 * To change this template use File | Settings | File Templates.
 */
public class FractalController {
    private final ControllerThread controllerThread = new ControllerThread(this);


    private enum Status {NEW, DOC_LOADED, READY_FOR_COUNT, COUNTING_SQUARES, SQUARES_COUNTED, EXPORTING};

    private final static String CALC_STATS = "CalcStats";
    private final static String GENERATE_METADATA = "GenerateMetadata";
    private final static String COUNT_SQUARES = "CountSquares";
    private final static String UPDATE_RESOLUTION_LIST = "UpdateResolutionList";

    private final static String RESULT_GRID = "ResultGrid";
    private final static int RESULT_GRID_Z_INDEX = -1;

    private final static String MIN_MAX_GRIDS = "MinMaxGrids";
    private final static String INDICATIVE_RESOLUTION_GRID = "IndicativeResolutionGrids";
    private final static int INDICATIVE_RESOLUTION_GRID_Z_INDEX = -12;

    private final static String MIN_GRID = "MinGrid";
    private final static int MIN_GRID_Z_INDEX = -15;

    private final static String MAX_GRID = "MaxGrid";
    private final static int MAX_GRID_Z_INDEX = -10;

    private final static String BOUNDING_BOX = "BoundingBox";
    private final static int BOUNDING_BOX_Z_INDEX = -5;

    private final FractalDocument document;
    private final FractalPanel panel;

    private final BoundedRangeModel minimumSquareSizeModel;
    private final BoundedRangeModel maximumSquareSizeModel;
    private final BoundedRangeModel resolutionModel;
    private final BoundedRangeModel angleModel;
    private final BoundedRangeModel displacementModel;

    private boolean indicativeResolutionGridIsDisplayed = false;


    private SquareCountingResult result = null;


    private AtomicReference<ResolutionIteratorFactory> resolutionIteratorFactory = new AtomicReference<ResolutionIteratorFactory>(ResolutionIteratorFactory.factories[0]);

    private final Action actionCalculateFractalDimension = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            actionCalculateFractalDimension();
        }
    };

    private final Action actionFileExport = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            actionFileExport();
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
        return new FractalController(document);
    }

    private FractalController(FractalDocument document) {
        this.document = document;
        panel = new FractalPanel();

        minimumSquareSizeModel = new DefaultBoundedRangeModel(10, 0, 1, 1000);
        panel.getMinimumSquareSizeSlider().setModel(minimumSquareSizeModel);

        maximumSquareSizeModel = new DefaultBoundedRangeModel(990, 0, 1, 1000);
        panel.getMaximumSquareSizeSlider().setModel(maximumSquareSizeModel);

        angleModel = new DefaultBoundedRangeModel(5,0,1,10);
        panel.getAngleSlider().setModel(angleModel);

        resolutionModel = new DefaultBoundedRangeModel(10, 0, 2, 20);
        panel.getResolutionSlider().setModel(resolutionModel);
        panel.getResolutionSlider().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                controllerThread.addToQueue(UPDATE_RESOLUTION_LIST, new Runnable() {
                    public void run() {
                        removeIndicativeResolutionGrid();
                        updateResolutionList();
                    }
                });
            }
        });

        displacementModel = new DefaultBoundedRangeModel(2,0,1,5);
        panel.getDisplacementSlider().setModel(displacementModel);

        panel.getResolutionIteratorList().setModel(new DefaultComboBoxModel(ResolutionIteratorFactory.factories));

        panel.getResolutionIteratorList().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    final ResolutionIteratorFactory chosenFactory = (ResolutionIteratorFactory) e.getItem();
                    resolutionIteratorFactory.set(chosenFactory);
                    if (chosenFactory == ResolutionIteratorFactory.factories[2]) {
                        FractalController.this.panel.getResolutionSlider().setEnabled(false);
                    } else {
                        FractalController.this.panel.getResolutionSlider().setEnabled(true);
                    }
                    controllerThread.addToQueue(UPDATE_RESOLUTION_LIST, new Runnable() {
                        public void run() {
                            removeIndicativeResolutionGrid();
                            updateResolutionList();
                        }
                    });
                }
            }
        });



        panel.addResultPanelListener(new ResultPanelListener() {
            public void gridSelected(final GridSelectedEvent e) {
                controllerThread.addToQueue(RESULT_GRID, new Runnable() {
                    public void run() {
                        Log.thread.debug("Grid selected: " + e.getGrid().toString());
                        updateResultGrid(e.getGrid());
                    }
                });
            }
        });

        final JList jList = panel.getResolutionList();
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                int selectedIndex = e.getFirstIndex();
                    final Double selectedResolution = (Double) jList.getModel().getElementAt(selectedIndex);
                    controllerThread.addToQueue(INDICATIVE_RESOLUTION_GRID, new Runnable() {
                        public void run() {
                            Log.thread.debug("Updating indicative resolution grid");
                            updateIndicativeResolutionGrid(selectedResolution);
                        }
                    });
                }
            }
        });
    }

    public void notifyAdded() {
        String threadName = "Controller: " + document.getName();
        controllerThread.setName(threadName);
        controllerThread.start();
        panel.updateProgressBar(0);
        panel.showProgressBar();
        controllerThread.addToQueue(GENERATE_METADATA, new Runnable() {
            public void run() {
                generateMetaData();
            }
        });
    }

    public void notifyRemoving() {
        // nothing to do
    }

    public void notifyRemoved() {
        controllerThread.quit();
    }

    private void generateMetaData() {
        controllerThread.checkControllerThread();
        Log.thread.debug("Generating metadata");
        panel.updateProgressBar(33);
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(document.getSvgDoc());
        document.setMetadata(metadata);
        BoundingBox box = metadata.getBoundingBox();
        double maximumBoxSize = Math.min(box.getWidth(), box.getHeight());
        this.status = Status.DOC_LOADED;

        Log.thread.debug("Populating settings panel");

        maximumSquareSizeModel.setValue((int)maximumBoxSize + 1);

        minimumSquareSizeModel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int minValue = minimumSquareSizeModel.getValue();
                final int maxValue = maximumSquareSizeModel.getValue();
                if (!minimumSquareSizeModel.getValueIsAdjusting()) {
                    controllerThread.addToQueue(MIN_MAX_GRIDS, new Runnable() {
                        public void run() {
                            Log.thread.debug("Updating grids in response to minimum square size change");
                            updateMinimumAndMaximumGrids();
                        }
                    });
                    if (minValue > maxValue) {
                        maximumSquareSizeModel.setValue(minValue);
                    }
                }
                controllerThread.addToQueue(UPDATE_RESOLUTION_LIST, new Runnable() {
                    public void run() {
                        removeIndicativeResolutionGrid();
                        updateResolutionList();
                    }
                });
            }
        });

        maximumSquareSizeModel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int minValue = minimumSquareSizeModel.getValue();
                final int maxValue = maximumSquareSizeModel.getValue();
                if (!maximumSquareSizeModel.getValueIsAdjusting()) {
                    controllerThread.addToQueue(MIN_MAX_GRIDS, new Runnable() {
                        public void run() {
                            Log.thread.debug("Updating grids in response to maximum square size change");
                            updateMinimumAndMaximumGrids();
                        }
                    });
                    if (maxValue < minValue) {
                        minimumSquareSizeModel.setValue(maxValue);
                    }
                }
                controllerThread.addToQueue(UPDATE_RESOLUTION_LIST, new Runnable() {
                    public void run() {
                        removeIndicativeResolutionGrid();
                        updateResolutionList();
                    }
                });
            }
        });

        panel.updateProgressBar(66);
        panel.updateDocument(document);
        updateMinimumAndMaximumGrids();
        updateResolutionList();
        panel.updateProgressBar(100);
        panel.hideProgressBar();
        setStatus(Status.READY_FOR_COUNT);
        // All the setting up of the panel etc. will be done in the controller thread.
    }

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
        if (isCapableOfExport()) {
            repository.getFileExport().setDelegate(actionFileExport);
        } else {
            repository.getFileExport().removeDelegate();
        }
    }

    private boolean isCapableOfCalculation() {
        return (status == Status.READY_FOR_COUNT || status == Status.SQUARES_COUNTED);
    }

    private boolean isCapableOfExport() {
        return (status == Status.SQUARES_COUNTED);
    }

    private void setStatus(Status newStatus) {
        status = newStatus;
        FractDim.instance().updateMeIfCurrent(this);
    }

    public void actionFileExport() {
        if (result == null) {
            Log.app.warn("Shouldn't be able to export if the result is null");
        }
        File exportFile = FractDim.instance().getExportFile(document.getName());
        if (exportFile != null) {
            panel.disableAllControls();
            panel.updateProgressBar(0);
            panel.showProgressBar();
            Log.app.debug("Exporting to " + exportFile.getAbsolutePath());
            ExcelExportWorker eew = new ExcelExportWorker(document.getName(), result, exportFile, new Notifiable<ExcelExportWorker>() {
                public void notifyComplete(ExcelExportWorker worker) {
                    panel.enableAllControls();
                    panel.hideProgressBar();
                    setStatus(Status.SQUARES_COUNTED);
                }

                public void updateProgress(int progress) {
                    panel.updateProgressBar(progress);
                }
            });
            setStatus(Status.EXPORTING);
            eew.execute();
            Log.app.info("Started excel export worker");
        }

    }

    public void actionCloseFile() {
        // TODO - check we're in a fit state to close
        FractDim.instance().remove(this);
    }

    public void actionCalculateFractalDimension() {
        panel.disableAllControls();
        panel.updateProgressBar(0);
        panel.showProgressBar();

        AngleIterator angleIterator = new UniformAngleIterator(angleModel.getValue());
        ResolutionIterator resolutionIterator = resolutionIteratorFactory.get().create(minimumSquareSizeModel.getValue(), maximumSquareSizeModel.getValue(), resolutionModel.getValue());
        DisplacementIterator displacementIterator = new UniformDisplacementIterator(displacementModel.getValue());

        SquareCountingWorker scw = new SquareCountingWorker(document, angleIterator, resolutionIterator, displacementIterator, new Notifiable<SquareCountingWorker>() {
            public void notifyComplete(SquareCountingWorker worker) {
                Log.calc.info("Square counting worker reported");
                try {
                    result = worker.get();
                    panel.enableAllControls();
                    panel.update(result);
                    panel.hideProgressBar();
                    setStatus(Status.SQUARES_COUNTED);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ExecutionException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            public void updateProgress(int progress) {
                panel.updateProgressBar(progress);
            }
        });
        setStatus(Status.COUNTING_SQUARES);
        scw.execute();
        Log.calc.info("Started square counting worker");
    }


    private void updateResolutionList() {
        controllerThread.checkControllerThread();
        final ResolutionIteratorFactory iteratorFactory = resolutionIteratorFactory.get();
        if (iteratorFactory != null) {
            JList resolutionList = this.panel.getResolutionList();
            Vector<Double> resolutions = new Vector<Double>();
            for (ResolutionIterator it = iteratorFactory.create(minimumSquareSizeModel.getValue(), maximumSquareSizeModel.getValue(), resolutionModel.getValue()); it.hasNext();) {
                resolutions.add(it.next());
            }
            resolutionList.setListData(resolutions);
        }
    }

    private void updateMinimumAndMaximumGrids() {
        controllerThread.checkControllerThread();

        final Grid minGrid = new Grid(minimumSquareSizeModel.getValue());
        final Grid maxGrid = new Grid(maximumSquareSizeModel.getValue());

        final BoundingBox boundingBox = document.getMetadata().getBoundingBox();

        Log.gui.info("Bounding box is " + boundingBox);

        panel.updateOverlay(BOUNDING_BOX, BOUNDING_BOX_Z_INDEX, new SVGContentGenerator() {
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

        panel.updateOverlay(MIN_GRID, MIN_GRID_Z_INDEX, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return minGrid.writeToSVG(rootElement, creator, boundingBox, "#99ff99");
            }
        });

        panel.updateOverlay(MAX_GRID, MAX_GRID_Z_INDEX, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return maxGrid.writeToSVG(rootElement, creator, boundingBox, "#9999ff");
            }
        });
    }

    private void updateIndicativeResolutionGrid(double resolution) {
        controllerThread.checkControllerThread();

        final Grid grid = new Grid(resolution);

        final BoundingBox boundingBox = document.getMetadata().getBoundingBox();

        panel.updateOverlay(INDICATIVE_RESOLUTION_GRID, INDICATIVE_RESOLUTION_GRID_Z_INDEX, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return grid.writeToSVG(rootElement, creator, boundingBox, "#cccc00");
            }
        });
        indicativeResolutionGridIsDisplayed = true;
    }

    private void removeIndicativeResolutionGrid() {
        controllerThread.checkControllerThread();

        if (indicativeResolutionGridIsDisplayed) {
            final BoundingBox boundingBox = document.getMetadata().getBoundingBox();

            panel.removeOverlay(INDICATIVE_RESOLUTION_GRID);
        }
        indicativeResolutionGridIsDisplayed = false;
    }

    private void updateResultGrid(final Grid theGrid) {
        controllerThread.checkControllerThread();
        final BoundingBox boundingBox = document.getMetadata().getBoundingBox();
        panel.updateOverlay(RESULT_GRID, RESULT_GRID_Z_INDEX, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                return theGrid.writeFilledToSVG(rootElement, creator, boundingBox, "#ff9999");
            }
        });
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
        controllerThread.checkControllerThread();
    }

}
