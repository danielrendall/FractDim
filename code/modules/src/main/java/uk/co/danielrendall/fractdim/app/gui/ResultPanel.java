package uk.co.danielrendall.fractdim.app.gui;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

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
    final JXTreeTable resultTable = new JXTreeTable();

    private SquareCountingResult result;
    public ResultPanel() {
        super (new BorderLayout());

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

    public void update(SquareCountingResult result) {
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

    public void addResultPanelListener(ResultPanelListener listener) {
        listenerList.add(ResultPanelListener.class, listener);
    }

    public void removeResultPanelListener(ResultPanelListener listener) {
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

