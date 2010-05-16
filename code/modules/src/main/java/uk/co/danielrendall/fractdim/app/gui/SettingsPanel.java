package uk.co.danielrendall.fractdim.app.gui;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Apr-2010
 * Time: 08:13:52
 * To change this template use File | Settings | File Templates.
 */
public class SettingsPanel extends JPanel {

    private final JSlider minimumSquareSizeSlider;
    private final JSlider maximumSquareSizeSlider;
    private final JSlider angleSlider;
    private final JSlider resolutionSlider;
    private final JSlider displacementSlider;

    private final JComboBox resolutionIteratorList;
    private final JList resolutionList;

    SettingsPanel() {

        this.setLayout(new MigLayout("fill", "[pref][pref][pref, grow, fill]", "[min][min][min][grow, fill]"));

        minimumSquareSizeSlider = new JSlider();
        maximumSquareSizeSlider = new JSlider();
        angleSlider = new JSlider();
        resolutionSlider = new JSlider();
        displacementSlider = new JSlider();
        resolutionIteratorList = new JComboBox();
        resolutionList = new JList();

        JPanel anglePanel = new JPanel(new BorderLayout());
        anglePanel.setBorder(BorderFactory.createTitledBorder("Angles"));
        angleSlider.setLabelTable(angleSlider.createStandardLabels(5));
        angleSlider.setPaintLabels(true);
        angleSlider.setMinorTickSpacing(1);
        angleSlider.setPaintTicks(true);
        angleSlider.setSnapToTicks(true);
        anglePanel.add(angleSlider, BorderLayout.CENTER);
        add(anglePanel, "cell 0 0 3 1, growx");

        JPanel resolutionPanel = new JPanel(new BorderLayout());
        resolutionPanel.setBorder(BorderFactory.createTitledBorder("Resolutions"));
        resolutionSlider.setLabelTable(resolutionSlider.createStandardLabels(5));
        resolutionSlider.setPaintLabels(true);
        resolutionSlider.setMinorTickSpacing(1);
        resolutionSlider.setPaintTicks(true);
        resolutionSlider.setSnapToTicks(true);
        resolutionPanel.add(resolutionSlider, BorderLayout.CENTER);
        resolutionPanel.add(resolutionIteratorList, BorderLayout.SOUTH);

        add(resolutionPanel, "cell 0 1 3 1, growx");

        JPanel displacementPanel = new JPanel(new BorderLayout());
        displacementPanel.setBorder(BorderFactory.createTitledBorder("Displacements"));
        displacementSlider.setLabelTable(angleSlider.createStandardLabels(1));
        displacementSlider.setPaintLabels(true);
        displacementSlider.setMinorTickSpacing(1);
        displacementSlider.setPaintTicks(true);
        displacementSlider.setSnapToTicks(true);
        displacementPanel.add(displacementSlider, BorderLayout.CENTER);

        add(displacementPanel, "cell 0 2 3 1, growx");

        minimumSquareSizeSlider.setOrientation(JSlider.VERTICAL);
        minimumSquareSizeSlider.setBorder(BorderFactory.createTitledBorder("Min"));

        add(minimumSquareSizeSlider, "cell 0 3 1 1");

        maximumSquareSizeSlider.setOrientation(JSlider.VERTICAL);
        maximumSquareSizeSlider.setBorder(BorderFactory.createTitledBorder("Max"));

        add(maximumSquareSizeSlider, "cell 1 3 1 1");

        add(resolutionList, "cell 2 3 1 1");
                        
    }


    void disableAllControls() {
        setAllEnabled(false);
    }


    void enableAllControls() {
        setAllEnabled(true);
    }


    public void setAllEnabled(boolean state) {
        minimumSquareSizeSlider.setEnabled(state);
        maximumSquareSizeSlider.setEnabled(state);
        angleSlider.setEnabled(state);
        resolutionSlider.setEnabled(state);
        resolutionIteratorList.setEnabled(state);
        displacementSlider.setEnabled(state);
        resolutionList.setEnabled(state);
    }

    JSlider getMinimumSquareSizeSlider() {
        return minimumSquareSizeSlider;
    }

    JSlider getMaximumSquareSizeSlider() {
        return maximumSquareSizeSlider;
    }

    JSlider getAngleSlider() {
        return angleSlider;
    }

    JSlider getResolutionSlider() {
        return resolutionSlider;
    }

    JSlider getDisplacementSlider() {
        return displacementSlider;
    }

    JComboBox getResolutionIteratorList() {
        return resolutionIteratorList;
    }

    JList getResolutionList() {
        return resolutionList;
    }
}
