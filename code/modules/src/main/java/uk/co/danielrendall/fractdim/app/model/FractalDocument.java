package uk.co.danielrendall.fractdim.app.model;

import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.svg.Utilities;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 20:30:49
 * To change this template use File | Settings | File Templates.
 */
public class FractalDocument {

    private final SVGDocument svgDoc;
    private final FractalDocumentMetadata metadata;
    private String name;
    private SquareCountingResult squareCountingResult;

    public FractalDocument(SVGDocument svgDoc, FractalDocumentMetadata metadata) {
        this.svgDoc = svgDoc;
        this.metadata = metadata;
        squareCountingResult = null;
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public SVGDocument getSvgDoc() {
        return svgDoc;
    }

    public String getName() {
        return name;
    }

    public FractalDocumentMetadata getMetadata() {
        return metadata;
    }

    public FractalDocument clone() {
        FractalDocument clone = new FractalDocument(Utilities.cloneSVGDocument(svgDoc), metadata);
        clone.setName(name);
        return clone;
    }

    public synchronized SquareCountingResult getSquareCountingResult() {
        return squareCountingResult;
    }

    public synchronized void setSquareCountingResult(SquareCountingResult squareCountingResult) {
        this.squareCountingResult = squareCountingResult;
    }
}
