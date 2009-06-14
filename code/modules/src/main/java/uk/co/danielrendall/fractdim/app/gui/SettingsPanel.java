package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
import uk.co.danielrendall.fractdim.app.datamodel.CompoundDataModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 14-Jun-2009
 * Time: 11:56:55
 * To change this template use File | Settings | File Templates.
 */
public class SettingsPanel extends JPanel {

    private final JTextField txtSmallestSquareSize = new JTextField();
    private final JTextField txtLargestSquareSize = new JTextField();
    private final JTextField txtNumberOfSizes = new JTextField();

    private final JSpinner spnNumberOfAngles = new JSpinner();
    private final JSpinner spnNumberOfDisplacements = new JSpinner();

    private CompoundDataModel model = new CompoundDataModel(CalculationSettings.class);


    public SettingsPanel() {
        GridLayout myLayout = new GridLayout(6, 2);
        this.setLayout(myLayout);

//        private double minimumSquareSize;
//        private double maximumSquareSize;
//        private int numberOfResolutions;
//        private int numberOfAngles;
//        private int numberOfDisplacementPoints;
        addTextField(txtSmallestSquareSize, "Smallest square size");
        model.bind("minimumSquareSize", txtSmallestSquareSize);

        addTextField(txtLargestSquareSize, "Largest square size");
        model.bind("maximumSquareSize", txtLargestSquareSize);

        addTextField(txtNumberOfSizes, "Number of square sizes");
        model.bind("numberOfResolutions", txtNumberOfSizes);

        spnNumberOfAngles.setModel(new SpinnerNumberModel(1, 1, 18, 1));
        addField(spnNumberOfAngles, "Number of angles");
        model.bind("numberOfAngles", spnNumberOfAngles);

        spnNumberOfDisplacements.setModel(new SpinnerNumberModel(1, 1, 3, 1));
        addField(spnNumberOfDisplacements, "Number of sub-points");
        model.bind("numberOfDisplacementPoints", spnNumberOfDisplacements);
        

        final JButton btnOk = new JButton("OK");
        btnOk.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        btnOk.setEnabled(false);
        add(btnOk);

        final JButton btnCancel = new JButton("Cancel");
        btnCancel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        add(btnCancel);

        model.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (CompoundDataModel.MODEL_VALIDITY.equals(evt.getPropertyName())) {
                    if ((Boolean) evt.getNewValue()) {
                        btnOk.setEnabled(true);
                    } else {
                        btnOk.setEnabled(false);
                    }
                }
            }
        });
    }

    private void addTextField(JTextField textField, String labelText) {
        textField.setText("0");
        addField(textField, labelText);
    }

    private void addField(JComponent c, String labelText) {
        c.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        add(label);
        add(c);
    }

    public void update(CalculationSettings settings) {
        txtSmallestSquareSize.setText(String.format("%9.2f", settings.getMinimumSquareSize()));

        txtLargestSquareSize.setText(String.format("%9.2f", settings.getMaximumSquareSize()));
        txtNumberOfSizes.setText(String.format("%d", settings.getNumberOfResolutions()));

        spnNumberOfAngles.setValue(settings.getNumberOfAngles());
        spnNumberOfDisplacements.setValue(settings.getNumberOfDisplacementPoints());
    }

    public CalculationSettings getModel() {
        return (CalculationSettings) model.getNewModel();
    }

    public void setModel(CalculationSettings settings) {
        update(settings);
    }

}
