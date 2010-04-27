package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-Jun-2009
 * Time: 21:23:30
 * To change this template use File | Settings | File Templates.
 */
public class GenericFormPanelBuilder {

    private final Map<Parameter, JSlider> sliders;


    public GenericFormPanelBuilder() {
        sliders = new LinkedHashMap<Parameter, JSlider>();
    }

    public GenericFormPanelBuilder addSlider(Parameter parameter) {
        JSlider slider = new JSlider(new DefaultBoundedRangeModel(50, 0, 0, 100));
        sliders.put(parameter, slider);
        return this;
    }

    public GenericFormPanel build() {
        return new GenericFormPanel(sliders);
    }

}
