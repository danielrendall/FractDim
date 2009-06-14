package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.fractdim.generate.fractals.Messy;
import uk.co.danielrendall.fractdim.generate.fractals.SquareKoch;

import javax.swing.*;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 11:29:59
 */
public class GenerateDialog {

    private final JDialog delegate;

    private final JComboBox typeCombo;
    private final JTextField depthField;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        GenerateDialog gd = new GenerateDialog(frame);
    }

    public GenerateDialog(JFrame parent) {
        delegate = new JDialog(parent, true);
        JPanel myPanel = new JPanel();


        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        typeCombo = new JComboBox(new FractalTypeComboBoxModel());
        depthField = new JTextField();
        myPanel.add(typeCombo);
        myPanel.add(depthField);

        delegate.add(myPanel);
        delegate.pack();
        delegate.setVisible(true);

    }

    static class FractalTypeComboBoxModel extends DefaultComboBoxModel {
        FractalTypeComboBoxModel() {
            super();
            addElement(new FractalType(KochCurve.class, "Koch curve"));
            addElement(new FractalType(Messy.class, "Messy"));
            addElement(new FractalType(SquareKoch.class, "Square Koch"));
        }
    }

    static class FractalType {
        private final Class clazz;
        private final String name;

        FractalType(Class clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

    }
}
