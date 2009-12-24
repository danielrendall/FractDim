package uk.co.danielrendall.fractdim.app.gui;

import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-Jun-2009
 * Time: 21:23:30
 * To change this template use File | Settings | File Templates.
 */
public class GenericFormPanel extends JPanel {
    private final ComponentPreparer labelPreparer, componentPreparer;

    private boolean isFirst = true;

    public GenericFormPanel(ComponentPreparer labelPreparer, ComponentPreparer componentPreparer) {
        this.labelPreparer = labelPreparer;
        this.componentPreparer = componentPreparer;
//        setLayout(new GridLayout(0, 2, padding, padding));
        setLayout(new RiverLayout());
    }

    public void addLabelAndComponent(String labelText, JComponent component) {

        JLabel label = new JLabel(labelText);

        labelPreparer.prepare(label);
        componentPreparer.prepare(component);

        add(isFirst ? "p left" : "br", label);
        add("tab hfill", component);
        isFirst = false;

    }

    public static interface ComponentPreparer {
        void prepare(JComponent component);
    }
}
