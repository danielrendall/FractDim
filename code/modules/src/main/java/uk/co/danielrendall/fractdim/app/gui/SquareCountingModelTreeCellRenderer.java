/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

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
