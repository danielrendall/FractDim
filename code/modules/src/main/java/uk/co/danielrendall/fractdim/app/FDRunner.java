package uk.co.danielrendall.fractdim.app;

import org.apache.log4j.Logger;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderException;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.danielrendall.fractdim.calculation.FDTranscoder;
import uk.co.danielrendall.fractdim.calculation.Calculator;
import uk.co.danielrendall.fractdim.calculation.CalculationResult;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 23:01:04
 */
public class FDRunner {
    private static final Logger log = Logger.getLogger(FDRunner.class);

    private static final String filename = "C:\\SVG\\closed_curves.svg";

    public static void main(String[] args) {
        try {
            File file = new File(filename);

            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

            SVGDocument doc = (SVGDocument) f.createDocument(file.toURI().toString());

            Calculator calc = new Calculator();

            CalculationResult result = calc.process(doc);

            // cheat - only one angle at the moment
            double angle = result.getAvailableAngles().iterator().next();

            for (Double resolution : result.getAvailableResolutions(angle)) {
                CalculationResult.Statistics stats = result.getStatistics(angle, resolution);
                log.debug("Resolution: " + resolution + " Number of squares: " + stats.getNumberOfSquares());
            }


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void prettyPrint(Node aNode, int depth) {
        log.debug("                            ".substring(0, depth) + aNode.getClass().getName());
        NodeList children = aNode.getChildNodes();
        for (int i=0; i< children.getLength(); i++) {
            prettyPrint(children.item(i), depth + 1);
        }
    }


}
