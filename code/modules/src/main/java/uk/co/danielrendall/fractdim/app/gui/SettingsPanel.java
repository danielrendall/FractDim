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

    public SettingsPanel(BoundedRangeModel minimumSquareSizeModel, BoundedRangeModel maximumSquareSizeModel, BoundedRangeModel angleModel, BoundedRangeModel resolutionModel, BoundedRangeModel displacementModel) {
        minimumSquareSizeSlider = new JSlider(minimumSquareSizeModel);
        maximumSquareSizeSlider = new JSlider(maximumSquareSizeModel);
        angleSlider = new JSlider(angleModel);
        resolutionSlider = new JSlider(resolutionModel);
        displacementSlider = new JSlider(displacementModel);

        setLayout(new BorderLayout());
        Box outerBox = Box.createVerticalBox();
        Box topBox = Box.createVerticalBox();
        Box lowerBox = Box.createHorizontalBox();

        setBorder(BorderFactory.createTitledBorder("Settings"));

        angleSlider.setBorder(BorderFactory.createTitledBorder("Angles"));
        angleSlider.setLabelTable(angleSlider.createStandardLabels(5));
        angleSlider.setPaintLabels(true);
        angleSlider.setMinorTickSpacing(1);
        angleSlider.setPaintTicks(true);
        angleSlider.setSnapToTicks(true);
        topBox.add(angleSlider);

        resolutionSlider.setBorder(BorderFactory.createTitledBorder("Resolutions"));
        resolutionSlider.setLabelTable(resolutionSlider.createStandardLabels(5));
        resolutionSlider.setPaintLabels(true);
        resolutionSlider.setMinorTickSpacing(1);
        resolutionSlider.setPaintTicks(true);
        resolutionSlider.setSnapToTicks(true);
        topBox.add(resolutionSlider);


        displacementSlider.setBorder(BorderFactory.createTitledBorder("Displacements"));
        displacementSlider.setLabelTable(angleSlider.createStandardLabels(1));
        displacementSlider.setPaintLabels(true);
        displacementSlider.setMinorTickSpacing(1);
        displacementSlider.setPaintTicks(true);
        displacementSlider.setSnapToTicks(true);
        topBox.add(displacementSlider);

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

    public void disableAllControls() {
        setAllEnabled(false);
    }

    public void enableAllControls() {
        setAllEnabled(true);
    }

    public void setAllEnabled(boolean state) {
        setMinimumSquaresEnabled(state);
        setMaximumSquaresEnabled(state);
        setAngleEnabled(state);
        setResolutionEnabled(state);
        setDisplacementEnabled(state);
    }

    public void setMinimumSquaresEnabled(boolean state) {
        minimumSquareSizeSlider.setEnabled(state);
    }

    public void setMaximumSquaresEnabled(boolean state) {
        maximumSquareSizeSlider.setEnabled(state);
    }

    public void setAngleEnabled(boolean state) {
        angleSlider.setEnabled(state);
    }

    public void setResolutionEnabled(boolean state) {
        resolutionSlider.setEnabled(state);
    }

    public void setDisplacementEnabled(boolean state) {
        displacementSlider.setEnabled(state);
    }

}
