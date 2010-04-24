package uk.co.danielrendall.fractdim.logging;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Apr-2010
 * Time: 11:36:05
 * To change this template use File | Settings | File Templates.
 */
public class PrettyPrinter {

    private final SVGDocument doc;
    private static final String spaces = "                                                ";

    public PrettyPrinter(SVGDocument doc) {
        this.doc = doc;
    }

    public void prettyPrint(Writer w) {
        BufferedWriter bw = new BufferedWriter(w);
        try {
            prettyPrint(bw, doc.getRootElement(), 0);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void prettyPrint(BufferedWriter bw, Node node, int indent) throws IOException {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            spaces(bw, indent);
            bw.write("<" + node.getNodeName() + prettyPrint(node.getAttributes()) + ">");
            bw.newLine();
            NodeList children = node.getChildNodes();
            for(int i=0; i<children.getLength(); i++) {
                prettyPrint(bw, children.item(i), indent + 2);
            }
            spaces(bw, indent);
            bw.write("</" + node.getNodeName() + ">");
            bw.newLine();
        } else {
            spaces(bw, indent);
            bw.write(node.getTextContent());
            bw.newLine();
        }
    }

    private String prettyPrint(NamedNodeMap attributes) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<attributes.getLength(); i++) {
            Node node = attributes.item(i);
            sb.append(" ").append(node.getNodeName()).append("=\"").append(node.getNodeValue()).append("\"");
        }
        return sb.toString();
    }


    private void spaces(BufferedWriter bw, int indent) throws IOException {
        bw.write(spaces.substring(0, indent));
    }
}
