package uk.co.danielrendall.fractdim.app.controller;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.GenericFormPanel;
import uk.co.danielrendall.fractdim.app.model.CalculationSettings;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.app.model.FractalDocumentMetadata;
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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:02:09
 * To change this template use File | Settings | File Templates.
 */
public class FractalController {

    private final static int DOC_LOADED = 1;
    private final static int STATS_CALCULATED = 2;

    private final static String CALC_STATS = "CalcStats";
    private final static String MIN_GRID = "MinGrid";
    private final static String MAX_GRID = "MaxGrid";
    private final static String BOUNDING_BOX = "BoundingBox";


    private final FractalDocument document;
    private final FractalPanel panel;

    private final CalculationSettings settings;

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
        settings = CalculationSettings.createCalculationSettings(document.getMetadata());
        GenericFormPanel settingsPanel = panel.getSettingsPanel();
        settingsPanel.setDataModel(CalculationSettings.MINIMUM_SQUARES, settings.getMinimumSquareSize());
        settingsPanel.setDataModel(CalculationSettings.MAXIMUM_SQUARES, settings.getMaximumSquareSize());
        settingsPanel.setDataModel(CalculationSettings.NUMBER_RESOLUTIONS, settings.getNumberOfResolutions());
        settingsPanel.setDataModel(CalculationSettings.NUMBER_ANGLES, settings.getNumberOfAngles());
        settingsPanel.setDataModel(CalculationSettings.NUMBER_DISPLACEMENTS, settings.getNumberOfDisplacements());
        panel.updateDocument(document);
        status = DOC_LOADED;
        CalculateStatisticsWorker csw = new CalculateStatisticsWorker(this, CALC_STATS);
        csw.execute();
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

            final Grid minGrid = new Grid(settings.getMinimumSquareSize().getValue());
            final Grid maxGrid = new Grid(settings.getMaximumSquareSize().getValue());

            final BoundingBox boundingBox = document.getMetadata().getBoundingBox();

            Log.gui.info("Bounding box is " + boundingBox);

            panel.addOverlay(BOUNDING_BOX, new SVGContentGenerator() {
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

            panel.addOverlay(MIN_GRID, new SVGContentGenerator() {
                public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                    return minGrid.writeToSVG(rootElement, creator, boundingBox, "#99ff99");
                }
            });

            panel.addOverlay(MAX_GRID, new SVGContentGenerator() {
                public BoundingBox generateContent(Element rootElement, SVGElementCreator creator) {
                    return maxGrid.writeToSVG(rootElement, creator, boundingBox, "#9999ff");
                }
            });

            status = STATS_CALCULATED;
            FractDim.instance().updateGlobal(this);
        } else {
            Log.app.warn("Wasn't expecting statistics when status was " + status);
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
