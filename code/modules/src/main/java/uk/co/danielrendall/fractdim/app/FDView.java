package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.swing.SwingRootView;
import org.bs.mdi.Action;
import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:45:40
 */
public class FDView extends SwingRootView {

    JSVGCanvas svgCanvas = new JSVGCanvas();
    JTabbedPane tabbedPane = new JTabbedPane();
    JTable resultTable = new JTable();

    public FDView() {
        super();
        setLayout(new BorderLayout());

        tabbedPane.addTab("SVG", new JScrollPane(svgCanvas));
        tabbedPane.addTab("Results", new JScrollPane(resultTable));
        tabbedPane.setSelectedIndex(0);

        add(tabbedPane, BorderLayout.CENTER);
        setPastePossible(false);
    }

    public Action copy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Action cut() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void paste(Action action) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void delete() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void syncWithData() {
        FDData theData = ((FDData) getDocument().getData());
        svgCanvas.setDocument(theData.getSvgDoc());
        resultTable.setModel(theData.getTableModel());
    }
}
