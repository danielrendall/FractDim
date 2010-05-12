package uk.co.danielrendall.fractdim.svg;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.FractDim;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 24-Apr-2010
 * Time: 15:22:37
 * To change this template use File | Settings | File Templates.
 */
public class SVGElementCreator {

    private final SVGDocument doc;
    private final static String DEFAULT_PATH_STYLE = "fill-rule:evenodd;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1";

    public SVGElementCreator(SVGDocument doc) {
        this.doc = doc;
    }

    public Element createGroup() {
        Element group = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "g");
        group.setAttributeNS(null, "id", "g" + FractDim.newId());
        return group;
    }

    public Element createPath() {
        return createPath("#000000");
    }

    public Element createPath(String colour) {
        Element path = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "path");
        path.setAttributeNS(null, "id", "path" + FractDim.newId());
        path.setAttributeNS(null, "style", String.format("%s;stroke:%s;fill:none", DEFAULT_PATH_STYLE, colour));
        return path;
    }

    public Element createFilledPath(String strokeColour, String fillColour) {
        Element path = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "path");
        path.setAttributeNS(null, "id", "path" + FractDim.newId());
        path.setAttributeNS(null, "style", String.format("%s;stroke:%s;fill:%s;fill-opacity:0.3", DEFAULT_PATH_STYLE, strokeColour, fillColour));
        return path;
    }
}
