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
    private final String name;
    private FractalDocumentMetadata metadata;
    private SquareCountingResult squareCountingResult;

    public FractalDocument(SVGDocument svgDoc, String name) {
        this.svgDoc = svgDoc;
        this.name = name;
        squareCountingResult = null;
        
    }

    public void setMetadata(FractalDocumentMetadata metadata) {
        this.metadata = metadata;
    }

    public SVGDocument getSvgDoc() {
        // always hand out a copy of this - no more tedious threading problems
        return Utilities.cloneSVGDocument(svgDoc);
    }

    public String getName() {
        return name;
    }

    public FractalDocumentMetadata getMetadata() {
        return metadata;
    }

    public synchronized SquareCountingResult getSquareCountingResult() {
        return squareCountingResult;
    }

    public synchronized void setSquareCountingResult(SquareCountingResult squareCountingResult) {
        this.squareCountingResult = squareCountingResult;
    }
}
