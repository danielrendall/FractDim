package uk.co.danielrendall.fractdim.app.dialog;

import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.Statistics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07-Jun-2009
 * Time: 17:42:43
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsPanel extends JPanel {
    private final JTextField txtTotalCurveCount = new JTextField();
    private final JTextField txtFragmentedCurveCount = new JTextField();
    private final JTextField txtShortestLine = new JTextField();
    private final JTextField txtLongestLine = new JTextField();
    private final JTextField txtMeanLineLength = new JTextField();
    private final JTextField txtVarianceLineLength = new JTextField();
    private final JTextField txtMeanFragmentOnlyLength = new JTextField();
    private final JTextField txtVarianceFragmentOnlyLength = new JTextField();
    private final JTextField txtMeanFragmentsPerCurve = new JTextField();
    private final JTextField txtVarianceFragmentsPerCurve = new JTextField();

    public StatisticsPanel() {
        GridLayout myLayout = new GridLayout(10, 2);
        this.setLayout(myLayout);
        addInformationField(txtTotalCurveCount, "Total number of lines");
        addInformationField(txtFragmentedCurveCount, "Total number of non-straight lines");
        addInformationField(txtShortestLine, "Shortest line or fragment");
        addInformationField(txtLongestLine, "Longest line or fragment");
        addInformationField(txtMeanLineLength, "Mean line or fragment length");
        addInformationField(txtVarianceLineLength, "Variance of line or fragment length");
        addInformationField(txtMeanFragmentOnlyLength, "Mean length of approximating fragments");
        addInformationField(txtVarianceFragmentOnlyLength, "Variance of approximating fragments");
        addInformationField(txtMeanFragmentsPerCurve, "Mean number of fragments per line");
        addInformationField(txtVarianceFragmentsPerCurve, "Variance of fragments per line");

    }

    private void addInformationField(JTextField textField, String label) {
        textField.setEditable(false);
        textField.setEnabled(false);
        textField.setText("");
        add(new JLabel(label));
        add(textField);
    }

    public void update(Statistics stats) {
        txtTotalCurveCount.setText(String.format("%d", stats.getTotalCurveCount()));
        txtFragmentedCurveCount.setText(String.format("%d", stats.getFragmentedCurveCount()));
        txtShortestLine.setText(String.format("%9.2f", stats.getShortestLine()));
        txtLongestLine.setText(String.format("%9.2f", stats.getLongestLine()));
        txtMeanLineLength.setText(String.format("%9.2f", stats.getMeanLineLength()));
        txtVarianceLineLength.setText(String.format("%9.2f", stats.getVarianceLineLength()));
        txtMeanFragmentOnlyLength.setText(String.format("%9.2f", stats.getMeanFragmentOnlyLength()));
        txtVarianceFragmentOnlyLength.setText(String.format("%9.2f", stats.getVarianceFragmentOnlyLength()));
        txtMeanFragmentsPerCurve.setText(String.format("%9.2f", stats.getMeanFragmentsPerCurve()));
        txtVarianceFragmentsPerCurve.setText(String.format("%9.2f", stats.getVarianceFragmentsPerCurve()));
    }
}
