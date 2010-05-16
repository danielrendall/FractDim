package uk.co.danielrendall.fractdim.app.gui;

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

    private final JComboBox resolutionList;

    SettingsPanel() {

        minimumSquareSizeSlider = new JSlider();
        maximumSquareSizeSlider = new JSlider();
        angleSlider = new JSlider();
        resolutionSlider = new JSlider();
        displacementSlider = new JSlider();
        resolutionList = new JComboBox();
        
        setLayout(new BorderLayout());
        Box outerBox = Box.createVerticalBox();
        Box topBox = Box.createVerticalBox();
        Box lowerBox = Box.createHorizontalBox();

        setBorder(BorderFactory.createTitledBorder("Settings"));

        JPanel anglePanel = new JPanel(new BorderLayout());
        anglePanel.setBorder(BorderFactory.createTitledBorder("Angles"));
        angleSlider.setLabelTable(angleSlider.createStandardLabels(5));
        angleSlider.setPaintLabels(true);
        angleSlider.setMinorTickSpacing(1);
        angleSlider.setPaintTicks(true);
        angleSlider.setSnapToTicks(true);
        anglePanel.add(angleSlider, BorderLayout.CENTER);
        topBox.add(anglePanel);

        JPanel resolutionPanel = new JPanel(new BorderLayout());
        resolutionPanel.setBorder(BorderFactory.createTitledBorder("Resolutions"));
        resolutionSlider.setLabelTable(resolutionSlider.createStandardLabels(5));
        resolutionSlider.setPaintLabels(true);
        resolutionSlider.setMinorTickSpacing(1);
        resolutionSlider.setPaintTicks(true);
        resolutionSlider.setSnapToTicks(true);
        resolutionPanel.add(resolutionSlider, BorderLayout.CENTER);
        resolutionPanel.add(resolutionList, BorderLayout.SOUTH);
        topBox.add(resolutionPanel);

        JPanel displacementPanel = new JPanel(new BorderLayout());
        displacementPanel.setBorder(BorderFactory.createTitledBorder("Displacements"));
        displacementSlider.setLabelTable(angleSlider.createStandardLabels(1));
        displacementSlider.setPaintLabels(true);
        displacementSlider.setMinorTickSpacing(1);
        displacementSlider.setPaintTicks(true);
        displacementSlider.setSnapToTicks(true);
        displacementPanel.add(displacementSlider, BorderLayout.CENTER);
        topBox.add(displacementPanel);

        lowerBox.setBorder(BorderFactory.createTitledBorder("Square sizes"));
        minimumSquareSizeSlider.setOrientation(JSlider.VERTICAL);
        minimumSquareSizeSlider.setBorder(BorderFactory.createTitledBorder("Min"));
        lowerBox.add(minimumSquareSizeSlider);

        maximumSquareSizeSlider.setOrientation(JSlider.VERTICAL);
        maximumSquareSizeSlider.setBorder(BorderFactory.createTitledBorder("Max"));
        lowerBox.add(maximumSquareSizeSlider);

        outerBox.add(topBox);
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(lowerBox, BorderLayout.CENTER);
        outerBox.add(lowerPanel);

        this.add(outerBox, BorderLayout.CENTER);
                
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
        resolutionList.setEnabled(state);
        displacementSlider.setEnabled(state);
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

    JComboBox getResolutionList() {
        return resolutionList;
    }
}
