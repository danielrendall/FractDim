package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

import se.datadosen.component.RiverLayout;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Jul-2009
 * Time: 11:39:41
 * To change this template use File | Settings | File Templates.
 */
public class ResultPanel extends JPanel {
    final JTable resultTable = new JTable();
    final JTable anglesTable = new JTable();
    final JTable squaresTable = new JTable();


    private SquareCountingResult result;

    public ResultPanel() {
        super(new RiverLayout());

        resultTable.setEnabled(false);
//        resultTable.setPreferredScrollableViewportSize(new Dimension(300, resultTable.getRowHeight() * 3));

        anglesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        anglesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                squaresTable.setModel(new ResolutionAndSquaresTableModel(anglesTable.getSelectedRow()));
                Dimension preferredSize = squaresTable.getPreferredSize();
//                int height = preferredSize.height > 300 ? 300 : preferredSize.height;
//                squaresTable.setPreferredScrollableViewportSize(new Dimension(300, height));
                squaresTable.invalidate();
            }
        });
//        anglesTable.setPreferredScrollableViewportSize(new Dimension(300, 40));

        squaresTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        squaresTable.setPreferredScrollableViewportSize(new Dimension(300, 40));

        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Results"));
        add("p left", resultScrollPane);

        JScrollPane angleScrollPane = new JScrollPane(anglesTable);
        angleScrollPane.setBorder(BorderFactory.createTitledBorder("Angles calculated"));
        add("br", angleScrollPane);

        JScrollPane squaresScrollPane = new JScrollPane(squaresTable);
        squaresScrollPane.setBorder(BorderFactory.createTitledBorder("Square counts for selected angle"));
        add("br", squaresScrollPane);

    }

    public void update(SquareCountingResult result) {
        this.result = result;
        resultTable.setModel(new ResultTableModel());
        anglesTable.setModel(new AnglesAndDimensionsTableModel());
        Dimension preferredSize = anglesTable.getPreferredSize();
//        int height = preferredSize.height > 300 ? 300 : preferredSize.height;
//        anglesTable.setPreferredScrollableViewportSize(new Dimension(300, height));
        anglesTable.invalidate();
    }

    private class ResultTableModel extends AbstractTableModel {

        private final double fractalDimension;
        private final double variance;
        private final double sd;

        public ResultTableModel() {
            List<Double> availableAngles = result.getAvailableAngles();
            int numRows = availableAngles.size();
            List<Double> fractalDimensions = new ArrayList<Double>(numRows);

            double totalFractalDimension = 0.0d;
            double totalFractalDimensionSquared = 0.0d;

            for (Double angle : availableAngles) {
                double fractalDimension = result.getResultsForAngle(angle).getFractalDimension();
                totalFractalDimension += fractalDimension;
                totalFractalDimensionSquared += (fractalDimension * fractalDimension);
            }
            fractalDimension = totalFractalDimension / ((double) numRows);
            variance = totalFractalDimensionSquared / ((double) numRows) - (fractalDimension * fractalDimension);
            sd = Math.sqrt(variance);
            
        }

        public int getRowCount() {
            return 3;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    switch(rowIndex) {
                        case 0:
                            return "Mean";
                        case 1:
                            return "Variance";
                        case 2:
                            return "Standard Deviation";
                        default:
                            return "Error";

                    }
                default:
                    switch(rowIndex) {
                        case 0:
                            return String.format("%9.3f", fractalDimension);
                        case 1:
                            return String.format("%9.3f", variance);
                        case 2:
                            return String.format("%9.3f", sd);
                        default:
                            return "Error";

                    }
            }
        }

    }

    private class ResolutionAndSquaresTableModel extends AbstractTableModel {

        private final List<Double> resolutionData;
        private final List<Double> squaresData;

        private final int numRows;

        private final String[] columnNames = new String[] {
                "Resolution", "Square Count"
        };

        public ResolutionAndSquaresTableModel(int angleSelection) {
            Log.gui.debug("Selected angle: " + angleSelection);
            double angle = result.getAvailableAngles().get(angleSelection);
            SquareCountingResult.FixedAngleSquareCountingResult resultForAngle = result.getResultsForAngle(angle);

            resolutionData = resultForAngle.getAvailableResolutions();
            numRows = resolutionData.size();
            squaresData = new ArrayList<Double>(resolutionData.size());

            for(Double resolution : resolutionData) {
                squaresData.add(resultForAngle.getCountsForResolution(resolution));
            }
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
                    return String.format("%9.3f", resolutionData.get(rowIndex));
                case 1:
                    return String.format("%9.3f", squaresData.get(rowIndex));
                default:
                    return "FAIL";
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

    }

    private class AnglesAndDimensionsTableModel extends AbstractTableModel {

        private final List<Double> availableAngles;
        private final List<Double> fractalDimensions;

        private final int numRows;

        private final String[] columnNames = new String[] {
                "Angle", "Fractal Dimension"
        };

        public AnglesAndDimensionsTableModel() {
            availableAngles = result.getAvailableAngles();
            numRows = availableAngles.size();
            fractalDimensions = new ArrayList<Double>(numRows);
            for (Double angle : availableAngles) {
                fractalDimensions.add(result.getResultsForAngle(angle).getFractalDimension());
            }
        }

        public int getSize() {
            return availableAngles.size();
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
                    return String.format("%9.2f", availableAngles.get(rowIndex) * 180d / Math.PI);
                case 1:
                    return String.format("%9.3f", fractalDimensions.get(rowIndex));
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
