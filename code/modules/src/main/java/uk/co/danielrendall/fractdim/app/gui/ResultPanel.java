package uk.co.danielrendall.fractdim.app.gui;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;

import javax.swing.*;
import java.awt.*;

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

        resultTable.setEnabled(false);

        add(new JScrollPane(resultTable), BorderLayout.CENTER);

    }

    public void update(SquareCountingResult result) {
        Log.gui.info("Result panel setting SquareCountingResult");
        this.result = result;
        SquareCountingModelRoot root = new SquareCountingModelRoot(result.getAngleGridCollection());
        this.resultTable.setTreeTableModel(new SquareCountingTreeTableModel(root));
        this.resultTable.setShowGrid(true);
        this.resultTable.setRootVisible(true);
        this.resultTable.setEnabled(true);
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
}
