package uk.co.danielrendall.fractdim.app.controller;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.XMLReader;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
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
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

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
    private int status = 0;

    public static FractalController fromFile(File file) throws IOException {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        SVGDocument doc = factory.createSVGDocument(file.toURI().toString());
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(doc);
        FractalDocument document = new FractalDocument(doc, metadata);
        document.setName(file.getName());
        return new FractalController(document);
    }

    public static FractalController fromInputStream(InputStream inputStream) throws IOException {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        SVGDocument doc = factory.createSVGDocument("SomeURI", inputStream);
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(doc);
        FractalDocument document = new FractalDocument(doc, metadata);
        document.setName("Inputstream " + new Date().getTime());
        return new FractalController(document);
    }

    public static FractalController fromDocument(SVGDocument doc) {
        FractalDocumentMetadata metadata = FractalMetadataUtil.getMetadata(doc);
        FractalDocument document = new FractalDocument(doc, metadata);
        document.setName("Document " + new Date().getTime());
        return new FractalController(document);
    }

    private static SVGDocument createSVGDocument() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        return (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
    }

    private FractalController(FractalDocument document) {
        this.document = document;
        panel = new FractalPanel();
        panel.updateSvgDocument(document.getSvgDoc());
        status = DOC_LOADED;
        CalculateStatisticsWorker csw = new CalculateStatisticsWorker(this, CALC_STATS);
        csw.execute();
    }

    public FractalDocument getDocument() {
        return document;
    }

    public FractalPanel getPanel() {
        return panel;
    }

    // called when our panel becomes active
    public void enableMenuItems() {
        ActionRepository repository = ActionRepository.instance();
        repository.getFileClose().setEnabled(true);
        repository.getDiagramZoomIn().setEnabled(true);
        repository.getDiagramZoomOut().setEnabled(true);
        if (status < STATS_CALCULATED) {
            repository.getFileCalculate().setEnabled(false);
        } else {
            repository.getFileCalculate().setEnabled(true);
        }
    }

    public void closeFile(FractDim fractDim) {
        // TODO - check we're in a fit state to close
        fractDim.remove(this);
    }

    public void calculate(FractDim fractDim) {
        //To change body of created methods use File | Settings | File Templates.
    }
    
    public void zoomIn(FractDim fractDim) {
        panel.zoomIn();
    }

    public void zoomOut(FractDim fractDim) {
        panel.zoomOut();
    }

    public void updateProgress(String taskId, int progress) {
        Log.calc.debug("Task " + taskId + " progress " + progress);
    }

    public void setStatistics(Statistics statistics) {
        if (status == DOC_LOADED) {
            // todo - some nicer way of selecting the algorithm for this...
            final CalculationSettings settings = new CalculationSettings(statistics);
            panel.getStatisticsPanel().update(statistics);
            panel.getSettingsPanel().update(settings);

            SVGDocument minDoc = createSVGDocument();
            SVGDocument maxDoc = createSVGDocument();
            SVGDocument bounding = createSVGDocument();

            Grid minGrid = new Grid(settings.getMinimumSquareSize());
            Grid maxGrid = new Grid(settings.getMaximumSquareSize());

            String pathStyle = "fill:#ff0000;fill-rule:evenodd;stroke:#ff0000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;fill-opacity:0.1";

            BoundingBox boundingBox = document.getMetadata().getBoundingBox();

            Element root = bounding.getRootElement();
            root.setAttributeNS(null, "width", "" + boundingBox.getMaxX());
            root.setAttributeNS(null, "height", "" + boundingBox.getMaxY());
            Element path = bounding.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "path");
            path.setAttributeNS(null, "id", "boundingBox");
            path.setAttributeNS(null, "style", pathStyle);
            path.setAttributeNS(null, "d", String.format("M %s,%s L %s,%s L %s,%s L %s,%s z",
                    boundingBox.getMinX(), boundingBox.getMinY(),
                    boundingBox.getMaxX(), boundingBox.getMinY(),
                    boundingBox.getMaxX(), boundingBox.getMaxY(),
                    boundingBox.getMinX(), boundingBox.getMaxY()));
            root.appendChild(path);

            minGrid.writeToSVG(minDoc, boundingBox);
//            prettyPrint("Generated min document", minDoc);
            maxGrid.writeToSVG(maxDoc, boundingBox);
//            prettyPrint("Generated max document", maxDoc);

            panel.addOverlay(BOUNDING_BOX, bounding);
            panel.addOverlay(MIN_GRID, minDoc);
            panel.addOverlay(MAX_GRID, maxDoc);

            panel.repaint();

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
