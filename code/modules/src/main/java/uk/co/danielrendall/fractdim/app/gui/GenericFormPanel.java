package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Apr-2010
 * Time: 22:59:50
 * To change this template use File | Settings | File Templates.
 */
public class GenericFormPanel extends JPanel {
    private final Map<Parameter, JSlider> sliders;

    public GenericFormPanel(Map<Parameter, JSlider> sliders) {
        super(new GridBagLayout());
        this.sliders = sliders;
        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1.0d ,1.0d, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        for(Map.Entry<Parameter, JSlider> element : sliders.entrySet()) {
            constraints.gridx = 0;
            add(new JLabel(element.getKey().getName()), constraints);
            constraints.gridx = 1;
            element.getValue().setToolTipText(element.getKey().getDescription());
            add(element.getValue(), constraints);
            constraints.gridy = constraints.gridy + 1;
        }
    }

    public void setDataModel(Parameter parameter, BoundedRangeModel model) {
        sliders.get(parameter).setModel(model);
    }
}
