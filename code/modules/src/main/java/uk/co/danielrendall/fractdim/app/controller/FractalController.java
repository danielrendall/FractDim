package uk.co.danielrendall.fractdim.app.controller;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.SettingsPanel;
import uk.co.danielrendall.fractdim.app.model.*;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.UnmodifiableBoundedRangeModel;
import uk.co.danielrendall.fractdim.app.workers.CalculateStatisticsWorker;
import uk.co.danielrendall.fractdim.calculation.FractalMetadataUtil;
import uk.co.danielrendall.fractdim.calculation.Statistics;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:02:09
 * To change this template use File | Settings | File Templates.
 */
public class FractalController implements ParameterChangeListener {

    private final static int DOC_LOADED = 1;
    private final static int STATS_CALCULATED = 2;

    private final static String CALC_STATS = "CalcStats";
    private final static String MIN_GRID = "MinGrid";
    private final static String MAX_GRID = "MaxGrid";
    private final static String BOUNDING_BOX = "BoundingBox";

    public final static Parameter SQUARE_SIZES = new Parameter("CALC_SETTINGS", "SQUARE_SIZES", "Square sizes", "The range of square sizes to be used for counting");
    public final static Parameter NUMBER_RESOLUTIONS = new Parameter("CALC_SETTINGS", "NUMBER_RESOLUTIONS", "Number of resolutions", "The number of different square sizes between the minimum and maximum (inclusive)");
    public final static Parameter NUMBER_ANGLES = new Parameter("CALC_SETTINGS", "NUMBER_ANGLES", "Number of angles", "The number of different grid angles to be tried for each resolution");
    public final static Parameter NUMBER_DISPLACEMENTS = new Parameter("CALC_SETTINGS", "NUMBER_DISPLACEMENTS", "Number of displacements", "The number of different offsets within each square to be tried for each angle");

    private final Map<Parameter, UnmodifiableBoundedRangeModel> calculationSettings;

    private final FractalDocument document;
    private final FractalPanel panel;

    private final Action actionCalculateStats = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            actionCalculateStats();
        }
    };

    private final Action actionCloseFile = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            actionCloseFile();
        }
    };

    private int status = 0;

    public static FractalController fromFile(File file) throws IOException {
        SAXSVGDocumentFactory factory = Utilities.getDocumentFactory();
        SVGDocument doc = factory.createSVGDocument(file.toURI().toString());
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(Utilities.cloneSVGDocument(doc));
        FractalDocument document = new FractalDocument(doc, metadata);
        document.setName(file.getName());
        return new FractalController(document);
    }

    public static FractalController fromInputStream(InputStream inputStream) throws IOException {
        SAXSVGDocumentFactory factory = Utilities.getDocumentFactory();
        SVGDocument doc = factory.createSVGDocument("SomeURI", inputStream);
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(Utilities.cloneSVGDocument(doc));
        FractalDocument document = new FractalDocument(doc, metadata);
        document.setName("Inputstream " + new Date().getTime());
        return new FractalController(document);
    }

    public static FractalController fromDocument(SVGDocument doc) {
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(Utilities.cloneSVGDocument(doc));
        FractalDocument document = new FractalDocument(doc, metadata);
        document.setName("Document " + new Date().getTime());
        return new FractalController(document);
    }

    private FractalController(FractalDocument document) {
        this.document = document;
        panel = new FractalPanel();

        SettingsPanel settingsPanel = panel.getSettingsPanel();
        BoundingBox box = document.getMetadata().getBoundingBox();
        double biggestDimension = Math.max(box.getWidth(), box.getHeight());
        double smallestDimension = Math.min(box.getWidth(), box.getHeight());
        double min = smallestDimension / 50.0d;

        Map<Parameter, UnmodifiableBoundedRangeModel> _tempSettings = new HashMap<Parameter, UnmodifiableBoundedRangeModel>();

        double range = biggestDimension - min;
        // start with a slider range just a little inside the real range.
        double rangeMin = min + (range * 0.1d);
        double rangeExtent = range * 0.8d;

        addToPanel(settingsPanel, _tempSettings, SQUARE_SIZES, new UnmodifiableBoundedRangeModel(rangeMin, rangeExtent, min, biggestDimension));
        addToPanel(settingsPanel, _tempSettings, NUMBER_RESOLUTIONS, new UnmodifiableBoundedRangeModel(2, 2, 20));
        addToPanel(settingsPanel, _tempSettings, NUMBER_ANGLES, new UnmodifiableBoundedRangeModel(1, 1, 10));
        addToPanel(settingsPanel, _tempSettings, NUMBER_DISPLACEMENTS, new UnmodifiableBoundedRangeModel(1, 1, 3));

        this.calculationSettings = Collections.unmodifiableMap(_tempSettings);
        panel.updateDocument(document);
        status = DOC_LOADED;
        CalculateStatisticsWorker csw = new CalculateStatisticsWorker(this, CALC_STATS);
        csw.execute();
    }

    private void addToPanel(SettingsPanel panel, Map<Parameter, UnmodifiableBoundedRangeModel> tempSettings, Parameter param, UnmodifiableBoundedRangeModel brm) {
        brm.addChangeListener(new SimpleChangeListener(this, param));
        tempSettings.put(param, brm);
        panel.setDataModel(param, brm);
    }

    public FractalDocument getDocument() {
        return document;
    }

    public FractalDocument getClonedDocument() {
        return document.clone();
    }

    public FractalPanel getPanel() {
        return panel;
    }

    // called when our panel becomes active
    public void enableMenuItems() {
        ActionMap actionMap = panel.getActionMap();
        ActionRepository repository = ActionRepository.instance();
        repository.getFileClose().setDelegate(actionCloseFile);
        repository.getDiagramZoomIn().setDelegate(actionMap.get(JSVGCanvas.ZOOM_IN_ACTION));
        repository.getDiagramZoomOut().setDelegate(actionMap.get(JSVGCanvas.ZOOM_OUT_ACTION));
        if (status < STATS_CALCULATED) {
            repository.getFileCalculate().removeDelegate();
        } else {
            repository.getFileCalculate().setDelegate(actionCalculateStats);
        }
    }

    public void actionCloseFile() {
        // TODO - check we're in a fit state to close
        FractDim.instance().remove(this);
    }

    public void actionCalculateStats() {
        panel.removeOverlay(MIN_GRID);
        panel.removeOverlay(MAX_GRID);
    }
    
    public void updateProgress(String taskId, int progress) {
        Log.calc.debug("Task " + taskId + " progress " + progress);
    }

    public void setStatistics(Statistics statistics) {
        if (status == DOC_LOADED) {
            // todo - some nicer way of selecting the algorithm for this...

            updateGrids();

            status = STATS_CALCULATED;
            FractDim.instance().updateGlobal(this);
        } else {
            Log.app.warn("Wasn't expecting statistics when status was " + status);
        }
    }

    private void updateGrids() {
        final Grid minGrid = new Grid(calculationSettings.get(SQUARE_SIZES).getValue());
        final Grid maxGrid = new Grid(calculationSettings.get(SQUARE_SIZES).getUpperValue());

        final BoundingBox boundingBox = document.getMetadata().getBoundingBox();

        Log.gui.info("Bounding box is " + boundingBox);

        panel.updateOverlay(BOUNDING_BOX, new SVGContentGenerator() {
            public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                Element path = creator.createPath("#ff9999");
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

    public void valueChanged(Parameter param, int value) {
        if (param.equals(SQUARE_SIZES)) {
            updateGrids();
        } else {
            Log.gui.debug("Ignoring change to param " + param.getId() + " to " + value);
        }
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
}
