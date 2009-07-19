package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.List;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Jul-2009
 * Time: 11:39:41
 * To change this template use File | Settings | File Templates.
 */
public class ResultPanel extends JPanel {
    final JTable resultTable = new JTable();

    final JList list = new JList();

    private SquareCountingResult result;

    public ResultPanel() {
        super();
        Box vertBox = Box.createVerticalBox();

        JPanel listPanel = new JPanel();
        listPanel.setBorder(BorderFactory.createTitledBorder("Available angles"));
        listPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        vertBox.add(listPanel);

        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Square counts for selected angle"));
        tablePanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        vertBox.add(tablePanel);

        vertBox.add(new JLabel("Final result goes here"));

        add(vertBox, BorderLayout.CENTER);

    }

    public void update(SquareCountingResult result) {
        this.result = result;
//        resultTable.setModel(new ResolutionAndSquaresTableModel(result));
        list.setModel(new CalculationResultListModel());
        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                resultTable.setModel(new ResolutionAndSquaresTableModel(list.getSelectedIndex()));
            }
        });
    }

    private class ResolutionAndSquaresTableModel extends AbstractTableModel {

        private final Vector<Double> resolutionData;
        private final Vector<Double> squaresData;

        private final int numRows;

        private final String[] columnNames = new String[] {
                "Resolution", "Square Count"
        };

        public ResolutionAndSquaresTableModel(int angleSelection) {
            Log.gui.debug("Selected angle: " + angleSelection);
            resolutionData = new Vector<Double>();
            squaresData = new Vector<Double>();

            double angle = result.getAvailableAngles().get(angleSelection);

            SquareCountingResult.FixedAngleSquareCountingResult resultForAngle = result.getResultsForAngle(angle);
            for(Double resolution : resultForAngle.getAvailableResolutions()) {
                resolutionData.add(resolution);
                squaresData.add(resultForAngle.getCountsForResolution(resolution).getNumberOfSquares());
            }

            numRows = resolutionData.size();
            Log.gui.debug(String.format("Table has %d rows", numRows));
        }

        public int getRowCount() {
            return numRows;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.format("%9.2f", resolutionData.get(rowIndex));
                case 1:
                    return String.format("%9.2f", squaresData.get(rowIndex));
                default:
                    return "FAIL";
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

    }

    private class CalculationResultListModel extends AbstractListModel {

        private final List<Double> availableAngles;
        public CalculationResultListModel() {
            availableAngles = result.getAvailableAngles();
        }

        public int getSize() {
            return availableAngles.size();
        }

        public Object getElementAt(int index) {
            return String.format("%9.2f", availableAngles.get(index) * 180d / Math.PI);
        }
    }
}
