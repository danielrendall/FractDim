package uk.co.danielrendall.fractdim.app;

import org.apache.batik.swing.JSVGCanvas;
import org.bs.mdi.Action;
import org.bs.mdi.swing.SwingRootView;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;

import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
import uk.co.danielrendall.fractdim.app.gui.StatisticsPanel;
import uk.co.danielrendall.fractdim.app.gui.SettingsPanel;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:45:40
 */
public class FDView extends SwingRootView {

    JTabbedPane tabbedPane = new JTabbedPane();

    JSVGCanvas svgCanvas = new JSVGCanvas();

    StatisticsPanel statsPanel = new StatisticsPanel();

    SettingsPanel settingsPanel = new SettingsPanel();

    JTable resultTable = new JTable();

    JProgressBar progressBar = new JProgressBar();


    public FDView() {
        super();
        setLayout(new BorderLayout());

        tabbedPane.addTab("SVG image", new JScrollPane(svgCanvas));

        add(tabbedPane, BorderLayout.CENTER);

        progressBar.setMaximum(100);
        progressBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        add(progressBar, BorderLayout.SOUTH);

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
        if (theData.isSvgDocDirty()) {
            updateSvgDoc(theData.getSvgDoc(true));
        }
        if (theData.isStatisticsDirty()) {
            updateStatistics(theData.getStatistics(true));
        }
        if (theData.isSettingsDirty()) {
            updateSettings(theData.getSettings(true));
        }
        if (theData.isResultDirty()) {
            updateResult(theData.getResult(true));
        }
//        resultTable.setModel(theData.getTableModel());
    }


    private void updateSvgDoc(SVGDocument svgDoc) {
        svgCanvas.setDocument(svgDoc);
    }

    private void updateStatistics(Statistics statistics) {
        Log.gui.debug("Updating statistics");
        if (tabbedPane.getTabCount() == 1) {
            Log.gui.debug("Adding new pane to tabbed pane");
            Box vertBox = Box.createVerticalBox();
            vertBox.add(statsPanel);
            vertBox.add(Box.createVerticalStrut(5));
            vertBox.add(settingsPanel);
            tabbedPane.addTab("Settings", vertBox);
            tabbedPane.repaint();
        }
        Log.gui.debug("Just updating statistics");
        statsPanel.update(statistics);
    }

    private void updateSettings(CalculationSettings settings) {
    }

    private void updateResult(SquareCountingResult result) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void updateProgressBar(int i) {
        progressBar.setValue(i);
    }
}
