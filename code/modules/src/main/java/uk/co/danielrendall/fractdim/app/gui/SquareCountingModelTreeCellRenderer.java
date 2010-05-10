package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 09-May-2010
 * Time: 17:58:15
 * To change this template use File | Settings | File Templates.
 */
public class SquareCountingModelTreeCellRenderer extends DefaultTreeCellRenderer {

    private final static Icon angleIcon = new ImageIcon(SquareCountingModelTreeCellRenderer.class.getResource("/icons/22/angle.png"));
    private final static Icon resolutionIcon = new ImageIcon(SquareCountingModelTreeCellRenderer.class.getResource("/icons/22/resolution.png"));
    private final static Icon displacementIcon = new ImageIcon(SquareCountingModelTreeCellRenderer.class.getResource("/icons/22/displacement.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//        Log.gui.debug("Have to render a " + value.getClass().getName());

        if (value instanceof SquareCountingModelRoot.AngleModelNode) {
            setIcon(angleIcon);
        } else if (value instanceof SquareCountingModelRoot.ResolutionModelNode) {
            setIcon(resolutionIcon);
        } else if (value instanceof SquareCountingModelRoot.DisplacementModelNode) {
            setIcon(displacementIcon);
        }

        return this;
    }
}
