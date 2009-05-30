package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.RootData;
import org.bs.mdi.Printer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import org.apache.log4j.Logger;

import java.awt.print.PageFormat;
import java.awt.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.Vector;

import uk.co.danielrendall.fractdim.calculation.CalculationResult;
import uk.co.danielrendall.fractdim.calculation.Calculator;

import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:43:19
 */
public class FDData extends RootData {
    private static final Logger log = Logger.getLogger(FDData.class);

    private final Printer printer = new FractDimPrinter();
    private final Calculator calculator = new Calculator();

    private SVGDocument svgDoc;
    private CalculationResult result;


    public Printer getPrinter() {
        return printer;
    }

    void setSvgDoc(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
        updateFractalDimension();
    }

    Document getSvgDoc() {
        return svgDoc;
    }

    private void updateFractalDimension() {
        // todo - consider running this in a separate thread...
        result = calculator.process(svgDoc);
    }

    private void prettyPrint(Node aNode, int depth) {
        log.debug("                            ".substring(0, depth) + aNode.getClass().getName());
        NodeList children = aNode.getChildNodes();
        for (int i=0; i< children.getLength(); i++) {
            prettyPrint(children.item(i), depth + 1);
        }
    }

    String getFractalDimension() {
        return result.getFractalDimension();
    }

    public TableModel getTableModel() {
        return new CalculationResultTableModel();
    }

    class FractDimPrinter implements Printer {

        public int getNumPages(PageFormat format) {
            return 1;
        }

        public boolean print(Graphics g, PageFormat format, int pageindex) {
            return true;
        }
    }

    class CalculationResultTableModel extends AbstractTableModel {

        private final Vector<Double> angleData;
        private final Vector<Double> resolutionData;
        private final Vector<Integer> squaresData;

        private final int numRows;

        private final String[] columnNames = new String[] {
                "Angle", "Resolution", "Square Count"
        };

        public CalculationResultTableModel() {
            angleData = new Vector<Double>();
            resolutionData = new Vector<Double>();
            squaresData = new Vector<Integer>();

            for (Double angle : result.getAvailableAngles()) {
                SortedMap<Double, CalculationResult.Statistics> map = result.getMapForAngle(angle);
                for(Map.Entry<Double, CalculationResult.Statistics> entry : map.entrySet()) {
                    angleData.add(angle);
                    resolutionData.add(entry.getKey());
                    squaresData.add(entry.getValue().getNumberOfSquares());
                }
            }
            numRows = angleData.size();
        }

        public int getRowCount() {
            return numRows;
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return angleData.get(rowIndex);
                case 1:
                    return resolutionData.get(rowIndex);
                case 2:
                    return squaresData.get(rowIndex);
                default:
                    return "FAIL";
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

    }
  }
