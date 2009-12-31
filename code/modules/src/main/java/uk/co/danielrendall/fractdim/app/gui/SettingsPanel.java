package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
import uk.co.danielrendall.fractdim.app.datamodel.CompoundDataModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 14-Jun-2009
 * Time: 11:56:55
 * To change this template use File | Settings | File Templates.
 */
public class SettingsPanel  extends GenericFormPanel {

    private final JTextField txtSmallestSquareSize = new JTextField();
    private final JTextField txtLargestSquareSize = new JTextField();
    private final JTextField txtNumberOfSizes = new JTextField();

    private final JSpinner spnNumberOfAngles = new JSpinner();
    private final JSpinner spnNumberOfDisplacements = new JSpinner();


    public SettingsPanel() {
        super(new GenericFormPanel.ComponentPreparer() {
            public void prepare(JComponent component) {
                ((JLabel) component).setHorizontalAlignment(JLabel.RIGHT);
            }
        }, new GenericFormPanel.ComponentPreparer() {
            public void prepare(JComponent component) {
                component.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        });

        setBorder(BorderFactory.createTitledBorder("Settings"));

        addLabelAndComponent("Smallest square size", txtSmallestSquareSize);
        addLabelAndComponent("Largest square size", txtLargestSquareSize);
        addLabelAndComponent("Number of square sizes", txtNumberOfSizes);

        spnNumberOfAngles.setModel(new SpinnerNumberModel(1, 1, 18, 1));
        addLabelAndComponent("Number of angles", spnNumberOfAngles);

        spnNumberOfDisplacements.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        addLabelAndComponent("Number of sub-points", spnNumberOfDisplacements);

    }

    public void bindToModel(CompoundDataModel model) {
        model.bind("minimumSquareSize", txtSmallestSquareSize);
        model.bind("maximumSquareSize", txtLargestSquareSize);
        model.bind("numberOfResolutions", txtNumberOfSizes);
        model.bind("numberOfAngles", spnNumberOfAngles);
        model.bind("numberOfDisplacementPoints", spnNumberOfDisplacements);
    }

    public void update(CalculationSettings settings) {
        txtSmallestSquareSize.setText(String.format("%9.2f", settings.getMinimumSquareSize()));

        txtLargestSquareSize.setText(String.format("%9.2f", settings.getMaximumSquareSize()));
        txtNumberOfSizes.setText(String.format("%3d", settings.getNumberOfResolutions()));

        spnNumberOfAngles.setValue(settings.getNumberOfAngles());
        spnNumberOfDisplacements.setValue(settings.getNumberOfDisplacementPoints());
    }

    public void setModel(CalculationSettings settings) {
        update(settings);
    }

}