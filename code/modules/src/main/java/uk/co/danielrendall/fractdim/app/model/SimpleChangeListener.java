package uk.co.danielrendall.fractdim.app.model;

import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Apr-2010
 * Time: 18:43:37
 * To change this template use File | Settings | File Templates.
 */
public class SimpleChangeListener implements ChangeListener {

    private final ParameterChangeListener pcl;
    private final Parameter param;

    public SimpleChangeListener(ParameterChangeListener pcl, Parameter param) {
        this.pcl = pcl;
        this.param = param;
    }

    public void stateChanged(ChangeEvent e) {
        BoundedRangeModel brm = (BoundedRangeModel) e.getSource();
        if (!brm.getValueIsAdjusting()) {
            pcl.valueChanged(param, brm.getValue());
        }
    }
}
