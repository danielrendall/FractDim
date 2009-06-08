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
import uk.co.danielrendall.fractdim.app.dialog.StatisticsPanel;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:45:40
 */
public class FDView extends SwingRootView {

    JSVGCanvas svgCanvas = new JSVGCanvas();
    JTabbedPane tabbedPane = new JTabbedPane();
    JTable resultTable = new JTable();
    StatisticsPanel statsPanel = new StatisticsPanel();
    JButton btnShowSettings = new JButton("Settings");
    JButton btnCalculate = new JButton("Calculate");
    JProgressBar progressBar = new JProgressBar();


    public FDView() {
        super();
        setLayout(new BorderLayout());

        Box buttonBox = Box.createHorizontalBox();
        btnShowSettings.setEnabled(false);
        buttonBox.add(btnShowSettings);
        btnCalculate.setEnabled(false);
        buttonBox.add(btnCalculate);
        buttonBox.add(Box.createHorizontalGlue());

        add(statsPanel, BorderLayout.NORTH);

        add(new JScrollPane(svgCanvas), BorderLayout.CENTER);

        Box box = Box.createVerticalBox();
        box.add(buttonBox);
        progressBar.setMaximum(100);
        box.add(progressBar);

        add(box, BorderLayout.SOUTH);

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
        statsPanel.update(statistics);
    }

    private void updateSettings(CalculationSettings settings) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void updateResult(SquareCountingResult result) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void updateProgressBar(int i) {
        progressBar.setValue(i);
    }
}
