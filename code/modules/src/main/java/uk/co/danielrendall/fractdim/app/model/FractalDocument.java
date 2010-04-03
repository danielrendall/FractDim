package uk.co.danielrendall.fractdim.app.model;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 20:30:49
 * To change this template use File | Settings | File Templates.
 */
public class FractalDocument {

    private final SVGDocument svgDoc;
    private String name;

    public FractalDocument(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
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
}
