package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.datamodel.CompoundDataModel;
import uk.co.danielrendall.fractdim.app.datamodel.GenerateSettings;

import javax.swing.*;

import java.awt.*;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 11:29:59
 */
public class GenerateDialog extends GenericFormPanelBuilder {

    private final JTextField txtX = new JTextField();
    private final JTextField txtY = new JTextField();
    private final JList lstFractalType = new JList();

    private final JSpinner spnDepth = new JSpinner();


    public GenerateDialog() {
//        super(new GenericFormPanelBuilder.ComponentPreparer() {
//            public void prepare(JComponent component) {
//                ((JLabel) component).setHorizontalAlignment(JLabel.RIGHT);
//            }
//        }, new GenericFormPanelBuilder.ComponentPreparer() {
//            public void prepare(JComponent component) {
//                component.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
//            }
//        });
//
//        setBorder(BorderFactory.createTitledBorder("Settings"));
//
//        lstFractalType.setModel(new AbstractListModel() {
//
//            public int getSize() {
//                return 3;
//            }
//
//            public Object getElementAt(int index) {
//                switch (index) {
//                    case 0:
//                        return "KochCurve";
//                    case 1:
//                        return "Messy";
//                    case 2:
//                        return "SquareKoch";
//                    default:
//                        return "Error";
//                }
//            }
//        });
//        addLabelAndComponent("Fractal type", lstFractalType);
//        addLabelAndComponent("End point x", txtX);
//        addLabelAndComponent("End point y", txtY);
//
//        spnDepth.setModel(new SpinnerNumberModel(1, 1, 6, 1));
//        addLabelAndComponent("Depth", spnDepth);


    }

    public void bindToModel(CompoundDataModel model) {
        model.bind("fractalType", lstFractalType);
        model.bind("endX", txtX);
        model.bind("endY", txtY);
        model.bind("depth", spnDepth);
    }

    public void update(GenerateSettings settings) {
        lstFractalType.setSelectedValue(settings.getFractalType(), true);
        txtX.setText("" + settings.getEndX());
        txtY.setText("" + settings.getEndY());
        spnDepth.setValue(settings.getDepth());
    }

    public void setModel(GenerateSettings settings) {
        update(settings);
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
