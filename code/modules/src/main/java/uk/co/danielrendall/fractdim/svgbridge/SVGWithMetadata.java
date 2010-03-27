package uk.co.danielrendall.fractdim.svgbridge;

import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.*;
import org.w3c.dom.events.Event;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-Jan-2010
 * Time: 21:27:29
 * To change this template use File | Settings | File Templates.
 */
public class SVGWithMetadata implements Cloneable {

    private final SVGDocument delegate;
    private final int curveCount;
    private final BoundingBox boundingBox;

    SVGWithMetadata(SVGDocument delegate, int curveCount, BoundingBox boundingBox) {
        this.delegate = delegate;
        this.curveCount = curveCount;
        this.boundingBox = boundingBox;
    }

    @Override
    public SVGWithMetadata clone() throws CloneNotSupportedException {
        return new SVGWithMetadata((SVGDocument) delegate.cloneNode(true), curveCount, boundingBox);
    }

    public SVGDocument getSVGDocument() {
        return delegate;
    }

    public int getCurveCount() {
        return curveCount;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
