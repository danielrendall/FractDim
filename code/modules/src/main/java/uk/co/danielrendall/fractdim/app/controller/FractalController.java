package uk.co.danielrendall.fractdim.app.controller;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.app.model.FractalDocumentMetadata;
import uk.co.danielrendall.fractdim.calculation.FractalMetadataUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:02:09
 * To change this template use File | Settings | File Templates.
 */
public class FractalController {
    private final FractalDocument document;
    private final FractalPanel panel;

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


    public FractalController(FractalDocument document) {
        this.document = document;
        panel = new FractalPanel();
        panel.updateSvgDocument(document.getSvgDoc());
    }

    public FractalDocument getDocument() {
        return document;
    }

    public FractalPanel getPanel() {
        return panel;
    }


    public void closeFile(FractDim fractDim) {
        // check we're in a fit state to close
        fractDim.remove(this);
    }

    public void zoomIn(FractDim fractDim) {
        panel.zoomIn();
    }

    public void zoomOut(FractDim fractDim) {
        panel.zoomOut();
    }
}
