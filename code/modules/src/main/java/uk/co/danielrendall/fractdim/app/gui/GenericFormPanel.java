package uk.co.danielrendall.fractdim.app.gui;

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

    private final int padding = 5;

    public GenericFormPanel(ComponentPreparer labelPreparer, ComponentPreparer componentPreparer) {
        this.labelPreparer = labelPreparer;
        this.componentPreparer = componentPreparer;
        setLayout(new GridLayout(0, 2, padding, padding));
    }

    public void add(String labelText, JComponent component) {

        JLabel label = new JLabel(labelText);

        labelPreparer.prepare(label);
        componentPreparer.prepare(component);

        add(label);
        add(component);

    }

    public static interface ComponentPreparer {
        void prepare(JComponent component);
    }
}
