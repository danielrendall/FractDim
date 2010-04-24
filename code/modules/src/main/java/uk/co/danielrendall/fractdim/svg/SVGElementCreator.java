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
    private final static String DEFAULT_PATH_STYLE = "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1";

    public SVGElementCreator(SVGDocument doc) {
        this.doc = doc;
    }

    public Element createGroup() {
        Element group = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "g");
        group.setAttributeNS(null, "id", "g" + FractDim.newId());
        return group;
    }

    public Element createPath() {
        Element path = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "path");
        path.setAttributeNS(null, "id", "path" + FractDim.newId());
        path.setAttributeNS(null, "style", DEFAULT_PATH_STYLE);
        return path;
    }
}
