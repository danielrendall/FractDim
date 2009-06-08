package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Printer;
import org.bs.mdi.RootData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;

import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.print.PageFormat;
import java.util.Vector;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:43:19
 */
public class FDData extends RootData {

    private final Printer printer = new FractDimPrinter();

    // 4 separate aspects to the data

    private SVGDocument svgDoc;
    private boolean svgDocDirty = false;
    private Statistics statistics;
    private boolean statisticsDirty = false;
    private CalculationSettings settings;
    private boolean settingsDirty = false;
    private SquareCountingResult result;
    private boolean resultDirty = false;


    public void setSvgDoc(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
        svgDocDirty = true;
    }

    public SVGDocument getSvgDoc() {
        return getSvgDoc(false);
    }

    public SVGDocument getSvgDoc(boolean resetDirty) {
        if (resetDirty) svgDocDirty = false;
        return svgDoc;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
        statisticsDirty = true;
    }

    public Statistics getStatistics() {
        return getStatistics(false);
    }

    public Statistics getStatistics(boolean resetDirty) {
        if (resetDirty) statisticsDirty = false;
        return statistics;
    }

    public void setSettings(CalculationSettings settings) {
        this.settings = settings;
        settingsDirty = true;
    }

    public CalculationSettings getSettings() {
        return getSettings(false);
    }

    public CalculationSettings getSettings(boolean resetDirty) {
        if (resetDirty) settingsDirty = false;
        return settings;
    }

    public void setResult(SquareCountingResult result) {
        this.result = result;
        resultDirty = true;
    }

    public SquareCountingResult getResult() {
        return getResult(false);
    }

    public SquareCountingResult getResult(boolean resetDirty) {
        if (resetDirty) resultDirty = false;
        return result;
    }

    public boolean isSvgDocDirty() {
        return svgDocDirty;
    }

    public boolean isStatisticsDirty() {
        return statisticsDirty;
    }

    public boolean isSettingsDirty() {
        return settingsDirty;
    }

    public boolean isResultDirty() {
        return resultDirty;
    }

    public TableModel getResultTableModel() {
        return new CalculationResultTableModel();
    }


    public Printer getPrinter() {
        return printer;
    }


    public void handleStatistics(Statistics stats) {
        setStatistics(stats);
        getDocument().syncViewsWithData();
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
        private final Vector<Double> squaresData;

        private final int numRows;

        private final String[] columnNames = new String[] {
                "Angle", "Resolution", "Square Count"
        };

        public CalculationResultTableModel() {
            angleData = new Vector<Double>();
            resolutionData = new Vector<Double>();
            squaresData = new Vector<Double>();

            for (Double angle : result.getAvailableAngles()) {

                for(Double resolution : result.getAvailableResolutions(angle)) {
                    angleData.add(angle);
                    resolutionData.add(resolution);
                    squaresData.add(result.getStatistics(angle, resolution).getNumberOfSquares());
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
