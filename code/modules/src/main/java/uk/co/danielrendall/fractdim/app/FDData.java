package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Printer;
import org.bs.mdi.RootData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.datamodel.CombinedModel;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.print.PageFormat;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:43:19
 */
public class FDData extends RootData {

    private final Printer printer = new FractDimPrinter();

    private CombinedModel model;


    public Printer getPrinter() {
        return printer;
    }

    void setSvgDoc(SVGDocument svgDoc) {
        model = new CombinedModel(svgDoc);
    }

    Document getSvgDoc() {
        return model.getSvgDoc();
    }

    private void updateFractalDimension() {
        // todo - consider running this in a separate thread...
//        result = squareCounter.process(svgDoc);
    }

    private void prettyPrint(Node aNode, int depth) {
        Log.gui.debug("                            ".substring(0, depth) + aNode.getClass().getName());
        NodeList children = aNode.getChildNodes();
        for (int i=0; i< children.getLength(); i++) {
            prettyPrint(children.item(i), depth + 1);
        }
    }

    public TableModel getTableModel() {
        return model.getResultTableModel();
    }

    class FractDimPrinter implements Printer {

        public int getNumPages(PageFormat format) {
            return 1;
        }

        public boolean print(Graphics g, PageFormat format, int pageindex) {
            return true;
        }
    }

  }
