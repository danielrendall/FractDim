package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07-Jun-2009
 * Time: 17:42:43
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsPanel extends GenericFormPanel {
    
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
        super(new ComponentPreparer() {
            public void prepare(JComponent component) {
                ((JLabel) component).setHorizontalAlignment(JLabel.RIGHT);
            }
        }, new ComponentPreparer() {
            public void prepare(JComponent component) {
                ((JTextField) component).setEditable(false);
                ((JTextField) component).setText("");
                component.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        });
        setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        addLabelAndComponent("Total number of lines", txtTotalCurveCount);
        addLabelAndComponent("Total number of non-straight lines", txtFragmentedCurveCount);
        addLabelAndComponent("Shortest line or fragment", txtShortestLine);
        addLabelAndComponent("Longest line or fragment", txtLongestLine);
        addLabelAndComponent("Mean line or fragment length", txtMeanLineLength);
        addLabelAndComponent("Variance of line or fragment length", txtVarianceLineLength);
        addLabelAndComponent("Mean length of approximating fragments", txtMeanFragmentOnlyLength);
        addLabelAndComponent("Variance of approximating fragments", txtVarianceFragmentOnlyLength);
        addLabelAndComponent("Mean number of fragments per line", txtMeanFragmentsPerCurve);
        addLabelAndComponent("Variance of fragments per line", txtVarianceFragmentsPerCurve);

        Log.gui.debug("Added components");
    }

    public void update(Statistics stats) {
        txtTotalCurveCount.setText(String.format("%9d", stats.getTotalCurveCount()));
        txtFragmentedCurveCount.setText(String.format("%9d", stats.getFragmentedCurveCount()));
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
