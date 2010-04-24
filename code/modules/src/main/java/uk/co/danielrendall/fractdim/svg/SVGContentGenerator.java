package uk.co.danielrendall.fractdim.svg;

import org.w3c.dom.Element;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 24-Apr-2010
 * Time: 15:32:54
 * To change this template use File | Settings | File Templates.
 */
public interface SVGContentGenerator {

    public BoundingBox generateContent(Element rootElement, SVGElementCreator creator);
}
