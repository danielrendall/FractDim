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
        super(new BorderLayout());

        GenericFormPanel myForm = new GenericFormPanel(new GenericFormPanel.ComponentPreparer() {
            public void prepare(JComponent component) {
                ((JLabel) component).setHorizontalAlignment(JLabel.RIGHT);
            }
        }, new GenericFormPanel.ComponentPreparer() {
            public void prepare(JComponent component) {
                component.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        });

        myForm.add("Smallest square size", txtSmallestSquareSize);
        model.bind("minimumSquareSize", txtSmallestSquareSize);

        myForm.add("Largest square size", txtLargestSquareSize);
        model.bind("maximumSquareSize", txtLargestSquareSize);

        myForm.add("Number of square sizes", txtNumberOfSizes);
        model.bind("numberOfResolutions", txtNumberOfSizes);

        spnNumberOfAngles.setModel(new SpinnerNumberModel(1, 1, 18, 1));
        myForm.add("Number of angles", spnNumberOfAngles);
        model.bind("numberOfAngles", spnNumberOfAngles);

        spnNumberOfDisplacements.setModel(new SpinnerNumberModel(1, 1, 3, 1));
        myForm.add("Number of sub-points", spnNumberOfDisplacements);
        model.bind("numberOfDisplacementPoints", spnNumberOfDisplacements);

        add(myForm, BorderLayout.CENTER);

        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        final JButton btnOk = new JButton("OK");
        btnOk.setEnabled(false);
        buttonBox.add(btnOk);

        final JButton btnCancel = new JButton("Cancel");
        buttonBox.add(btnCancel);

        add(buttonBox, BorderLayout.SOUTH);

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
