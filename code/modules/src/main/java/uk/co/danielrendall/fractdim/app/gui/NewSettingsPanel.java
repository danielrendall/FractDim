package uk.co.danielrendall.fractdim.app.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 25-Apr-2010
 * Time: 11:36:08
 * To change this template use File | Settings | File Templates.
 */
public class NewSettingsPanel {
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;
    private JSlider slider5;

    public NewSettingsPanel() {
        slider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public void setData(NewSettingsBean data) {
    }

    public void getData(NewSettingsBean data) {
    }

    public boolean isModified(NewSettingsBean data) {
        return false;
    }
}
