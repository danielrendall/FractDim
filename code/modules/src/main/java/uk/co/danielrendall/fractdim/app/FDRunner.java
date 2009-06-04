package uk.co.danielrendall.fractdim.app;

import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.danielrendall.fractdim.calculation.SquareCounter;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.logging.Log;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 23:01:04
 */
public class FDRunner {

    private static final String filename = "C:\\SVG\\closed_curves.svg";

    public static void main(String[] args) {
        try {
            File file = new File(filename);

            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

            SVGDocument doc = (SVGDocument) f.createDocument(file.toURI().toString());

            SquareCounter calc = new SquareCounter(doc);

            SquareCountingResult result = calc.process();

            // cheat - only one angle at the moment
            double angle = result.getAvailableAngles().iterator().next();

            for (Double resolution : result.getAvailableResolutions(angle)) {
                SquareCountingResult.Statistics stats = result.getStatistics(angle, resolution);
                Log.gui.debug("Resolution: " + resolution + " Number of squares: " + stats.getNumberOfSquares());
            }


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void prettyPrint(Node aNode, int depth) {
        Log.gui.debug("                            ".substring(0, depth) + aNode.getClass().getName());
        NodeList children = aNode.getChildNodes();
        for (int i=0; i< children.getLength(); i++) {
            prettyPrint(children.item(i), depth + 1);
        }
    }


}
