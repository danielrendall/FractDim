package uk.co.danielrendall.fractdim.app.datamodel;

import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
* User: daniel
* Date: 05-Jul-2009
* Time: 12:24:13
* To change this template use File | Settings | File Templates.
*/
public class CalculationResultTableModel extends AbstractTableModel {

    private final Vector<Double> angleData;
    private final Vector<Double> resolutionData;
    private final Vector<Double> squaresData;

    private final int numRows;

    private final String[] columnNames = new String[] {
            "Angle", "Resolution", "Square Count"
    };

    public CalculationResultTableModel(SquareCountingResult result) {
        angleData = new Vector<Double>();
        resolutionData = new Vector<Double>();
        squaresData = new Vector<Double>();

        for (Double angle : result.getAvailableAngles()) {
            SquareCountingResult.FixedAngleSquareCountingResult resultForAngle = result.getResultsForAngle(angle);
            for(Double resolution : resultForAngle.getAvailableResolutions()) {
                angleData.add(angle);
                resolutionData.add(resolution);
                squaresData.add(resultForAngle.getCountsForResolution(resolution).getNumberOfSquares());
            }
        }
        numRows = angleData.size();
        Log.gui.debug(String.format("Table has %d rows", numRows));
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
