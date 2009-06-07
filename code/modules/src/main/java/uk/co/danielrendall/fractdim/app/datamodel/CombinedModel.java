package uk.co.danielrendall.fractdim.app.datamodel;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bs.mdi.Application;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculator;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Vector;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 20:01:44
 */
public class CombinedModel {

    private final SVGDocument svgDoc;
    private Statistics stats;
    private CalculationSettings settings;
    private SquareCountingResult result;

    public CombinedModel(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
        final StatisticsCalculator statsCalculator = new StatisticsCalculator(svgDoc, Math.PI / 90.0);
        Application.getMainWindow().getProgressMonitor().add(statsCalculator);
        new Thread(new Runnable() {
            public void run() {
                stats = statsCalculator.process();
                Log.app.debug(ToStringBuilder.reflectionToString(stats));
                Application.getMainWindow().getProgressMonitor().remove(statsCalculator);
            }
        }).start();


    }

    public SVGDocument getSvgDoc() {
        return svgDoc;
    }

    public TableModel getResultTableModel() {
        return new CalculationResultTableModel();
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
