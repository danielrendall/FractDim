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

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import uk.co.danielrendall.fractdim.calculation.grids.Grid;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Jul-2009
 * Time: 11:39:41
 * To change this template use File | Settings | File Templates.
 */
public class ResultPanel extends JPanel {

    private final JXTreeTable resultTable;
    private SquareCountingResult result;

    ResultPanel() {

        super (new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Results"));
        resultTable = new JXTreeTable();

        setPreferredSize(new Dimension(300, 300));

        Log.gui.debug("Tree cell renderer is " + resultTable.getTreeCellRenderer().getClass().getName());

        resultTable.setEnabled(false);
        resultTable.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                Object selectedNode = e.getPath().getLastPathComponent();
                if (selectedNode instanceof SquareCountingModelRoot.DisplacementModelNode) {
                    Log.gui.debug("Displacement node selected - firing event");
                    SquareCountingModelRoot.DisplacementModelNode theNode = (SquareCountingModelRoot.DisplacementModelNode) selectedNode;
                    fireGridSelected(theNode.getGrid());
                }

            }
        });

        add(new JScrollPane(resultTable), BorderLayout.CENTER);

    }

    void update(SquareCountingResult result) {
        Log.gui.info("Result panel setting SquareCountingResult");
        this.result = result;
        SquareCountingModelRoot root = new SquareCountingModelRoot(result.getAngleGridCollection());
        resultTable.setTreeTableModel(new SquareCountingTreeTableModel(root));
        resultTable.setTreeCellRenderer(new SquareCountingModelTreeCellRenderer());
        resultTable.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        resultTable.setShowGrid(true);
        resultTable.setRootVisible(true);
        resultTable.setEnabled(true);
    }

    private static class SquareCountingTreeTableModel extends AbstractTreeTableModel {
        private SquareCountingTreeTableModel(SquareCountingModelRoot root) {
            super(root);
        }
        
        public int getColumnCount() {
            return 3;
        }
        
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Grid";
                case 1:
                    return "Fractal Dimension";
                case 2:
                    return "Square count";

            }
            return "Unknown";
        }

        public Object getValueAt(Object node, int column) {
            return ((SquareCountingModelTreeNode) node).getValueAt(column);
        }

        public Object getChild(Object parent, int index) {
            return ((SquareCountingModelTreeNode) parent).getChild(index);
        }

        public int getChildCount(Object parent) {
            return ((SquareCountingModelTreeNode) parent).getChildCount();
        }

        public int getIndexOfChild(Object parent, Object child) {
            return ((SquareCountingModelTreeNode) parent).getIndexOfChild(child);
        }
    }

    void addResultPanelListener(ResultPanelListener listener) {
        listenerList.add(ResultPanelListener.class, listener);
    }

    void removeResultPanelListener(ResultPanelListener listener) {
        listenerList.remove(ResultPanelListener.class, listener);
    }

    protected void fireGridSelected(Grid theGrid) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ResultPanelListener.class) {
                GridSelectedEvent gse = new GridSelectedEvent(theGrid);
                ((ResultPanelListener) listeners[i + 1]).gridSelected(gse);
            }
        }
    }

}

