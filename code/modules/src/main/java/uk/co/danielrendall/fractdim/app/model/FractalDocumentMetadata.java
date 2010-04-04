package uk.co.danielrendall.fractdim.app.model;

import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 04-Apr-2010
 * Time: 15:32:07
 * To change this template use File | Settings | File Templates.
 */
public class FractalDocumentMetadata {
    private final int curveCount;
    private final BoundingBox boundingBox;

    public FractalDocumentMetadata(int curveCount, BoundingBox boundingBox) {
        this.curveCount = curveCount;
        this.boundingBox = boundingBox;
    }

    public int getCurveCount() {
        return curveCount;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
