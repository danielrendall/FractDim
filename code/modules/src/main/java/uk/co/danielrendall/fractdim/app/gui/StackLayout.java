package uk.co.danielrendall.fractdim.app.gui;

import java.awt.*;

/**
* Created by IntelliJ IDEA.
* User: daniel
* Date: 05-Apr-2010
* Time: 10:15:54
* To change this template use File | Settings | File Templates.
*/
public class StackLayout implements LayoutManager {

    public void addLayoutComponent(String name, Component comp) {
        // nothing to do
    }

    public void layoutContainer(Container parent) {
        Dimension dim = parent.getSize();
        for (int i=0; i < parent.getComponentCount(); ++i) {
            parent.getComponent(i).setBounds(0, 0, dim.width, dim.height);
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension minSize = new Dimension(0, 0);
        for (int i=0; i < parent.getComponentCount(); ++i) {
            Dimension dim = parent.getComponent(i).getMinimumSize();
            minSize.width = Math.max(minSize.width, dim.width);
            minSize.height = Math.max(minSize.height, dim.height);
        }
        return minSize;
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension size = new Dimension(0, 0);
        for (int i=0; i < parent.getComponentCount(); ++i) {
            Dimension dim = parent.getComponent(i).getPreferredSize();
            size.width = Math.max(size.width, dim.width);
            size.height = Math.max(size.height, dim.height);
        }
        return size;
    }

    public void removeLayoutComponent(Component comp) {
        // nothing to do
    }
}
