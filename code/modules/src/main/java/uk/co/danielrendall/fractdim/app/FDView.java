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
import uk.co.danielrendall.fractdim.app.datamodel.CompoundDataModel;
import uk.co.danielrendall.fractdim.app.gui.StatisticsPanel;
import uk.co.danielrendall.fractdim.app.gui.SettingsPanel;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:45:40
 */
public class FDView extends SwingRootView {

    final JTabbedPane tabbedPane = new JTabbedPane();

    final JSVGCanvas svgCanvas = new JSVGCanvas();

    final JPanel settingsTab = new JPanel();

    final StatisticsPanel statsPanel = new StatisticsPanel();

    final SettingsPanel settingsPanel = new SettingsPanel();

    final JTable resultTable = new JTable();

    final JProgressBar progressBar = new JProgressBar();

    public FDView() {
        super();
        setLayout(new BorderLayout());

        tabbedPane.addTab("SVG image", new JScrollPane(svgCanvas));

        Box theBox = Box.createVerticalBox();
        theBox.add(statsPanel);
        theBox.add(Box.createVerticalStrut(10));
        theBox.add(settingsPanel);

        settingsTab.setLayout(new FlowLayout());
        settingsTab.add(theBox);

        tabbedPane.addTab("Settings", settingsTab);
        
        add(tabbedPane, BorderLayout.CENTER);

        progressBar.setMaximum(100);
        progressBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        add(progressBar, BorderLayout.SOUTH);

        setPastePossible(false);
    }

    public void setSettingsModel(CompoundDataModel settingsModel) {
        settingsPanel.bindToModel(settingsModel);
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
        statsPanel.update(statistics);
    }

    private void updateSettings(CalculationSettings settings) {
        Log.gui.debug("Updating settings");
        settingsPanel.update(settings);
    }

    private void updateResult(SquareCountingResult result) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void updateProgressBar(int i) {
        progressBar.setValue(i);
    }

}
