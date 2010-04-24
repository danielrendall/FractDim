package uk.co.danielrendall.fractdim.svg;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.logging.PrettyPrinter;

import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 24-Apr-2010
 * Time: 18:02:27
 * To change this template use File | Settings | File Templates.
 */
public class Utilities {

    public final static DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
    private final static SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());

    public static SAXSVGDocumentFactory getDocumentFactory() {
        return factory;
    }

    public static SVGDocument cloneSVGDocument(SVGDocument document) {
        SVGDocument newDoc = (SVGDocument) document.cloneNode(true);
        return newDoc;

    }

    public static String prettyPrinted(Node doc) {
        StringWriter sw = new StringWriter();
        new PrettyPrinter(doc).prettyPrint(sw);
        return sw.toString();
    }
}
