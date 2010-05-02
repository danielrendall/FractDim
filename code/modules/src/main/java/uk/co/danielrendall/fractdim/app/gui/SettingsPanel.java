package uk.co.danielrendall.fractdim.app.gui;

import com.jidesoft.swing.RangeSlider;
import org.apache.log4j.Logger;
import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.ParameterChangeListener;
import uk.co.danielrendall.fractdim.app.model.SimpleChangeListener;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;
import uk.co.danielrendall.fractdim.app.model.widgetmodels.UnmodifiableBoundedRangeModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Apr-2010
 * Time: 08:13:52
 * To change this template use File | Settings | File Templates.
 */
public class SettingsPanel extends JPanel {
    private final static Logger log = Logger.getLogger(SettingsPanel.class);

    private final Map<Parameter, JComponent> components;

    public static SettingsPanel create() {

        Map<Parameter, JComponent> components = new LinkedHashMap<Parameter, JComponent>();

        RangeSlider rs = new RangeSlider();
        rs.setModel(new DefaultBoundedRangeModel(25, 50, 0, 100));
        components.put(FractalController.SQUARE_SIZES, rs);
        components.put(FractalController.NUMBER_RESOLUTIONS, new JSlider(new DefaultBoundedRangeModel(50, 0, 0, 100)));
        components.put(FractalController.NUMBER_ANGLES, new JSlider(new DefaultBoundedRangeModel(50, 0, 0, 100)));
        components.put(FractalController.NUMBER_DISPLACEMENTS, new JSlider(new DefaultBoundedRangeModel(50, 0, 0, 100)));

        return new SettingsPanel(components);

    }

    private SettingsPanel(Map<Parameter, JComponent> components) {
        setLayout(new BorderLayout());
        Box myBox = Box.createVerticalBox();
        this.components = Collections.unmodifiableMap(components);
        setBorder(BorderFactory.createTitledBorder("Settings"));

        int i=0;
        for (Parameter param : components.keySet()) {
            JComponent component = components.get(param);
            component.setBorder(BorderFactory.createTitledBorder(param.getName()));
            component.setToolTipText(param.getDescription());
            myBox.add(component);
        }
        this.add(myBox, BorderLayout.CENTER);
    }

    public void setDataModelForParameter(Parameter parameter, BoundedRangeModel model, ParameterChangeListener pcl) {
        model.addChangeListener(new SimpleChangeListener(pcl, parameter));
        JSlider slider = ((JSlider)this.components.get(parameter));
        slider.setModel(model);
        int increment = (model.getMaximum() - model.getMinimum());
        slider.setLabelTable(slider.createStandardLabels(increment));
        slider.setMajorTickSpacing(increment);
        slider.setPaintLabels(true);
        slider.setPaintTicks(false);
        log.debug(slider.getLabelTable());
    }

    public BoundedRangeModel getModel(Parameter parameter) {
        return ((JSlider)this.components.get(parameter)).getModel();
    }
}
