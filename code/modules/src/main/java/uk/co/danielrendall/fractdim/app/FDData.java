package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Printer;
import org.bs.mdi.RootData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;

import java.awt.*;
import java.awt.print.PageFormat;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:43:19
 */
public class FDData extends RootData {

    private final Printer printer = new FractDimPrinter();

    // 4 separate aspects to the data

    private SVGDocument svgDoc;
    private boolean svgDocDirty = false;
    private Statistics statistics;
    private boolean statisticsDirty = false;
    private CalculationSettings settings;
    private boolean settingsDirty = false;
    private SquareCountingResult result;
    private boolean resultDirty = false;


    public void setSvgDoc(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
        svgDocDirty = true;
    }

    public SVGDocument getSvgDoc() {
        return getSvgDoc(false);
    }

    public SVGDocument getSvgDoc(boolean resetDirty) {
        if (resetDirty) svgDocDirty = false;
        return svgDoc;
    }

    // annoyingly, Batik has threading issues if you try to traverse the same SVGdoc simultaneously
    // in two threads
    public SVGDocument getSvgDocForCalculation() {
        return (SVGDocument) svgDoc.cloneNode(true);
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
        statisticsDirty = true;
    }

    public Statistics getStatistics() {
        return getStatistics(false);
    }

    public Statistics getStatistics(boolean resetDirty) {
        if (resetDirty) statisticsDirty = false;
        return statistics;
    }

    public void setSettings(CalculationSettings settings) {
        this.settings = settings;
        settingsDirty = true;
    }

    public CalculationSettings getSettings() {
        return getSettings(false);
    }

    public CalculationSettings getSettings(boolean resetDirty) {
        if (resetDirty) settingsDirty = false;
        return settings;
    }

    public void setResult(SquareCountingResult result) {
        this.result = result;
        resultDirty = true;
    }

    public SquareCountingResult getResult() {
        return getResult(false);
    }

    public SquareCountingResult getResult(boolean resetDirty) {
        if (resetDirty) resultDirty = false;
        return result;
    }

    public boolean isSvgDocDirty() {
        return svgDocDirty;
    }

    public boolean isStatisticsDirty() {
        return statisticsDirty;
    }

    public boolean isSettingsDirty() {
        return settingsDirty;
    }

    public boolean isResultDirty() {
        return resultDirty;
    }

    public Printer getPrinter() {
        return printer;
    }


    public void handleStatistics(Statistics stats) {
        setStatistics(stats);
        getDocument().syncViewsWithData();
    }

    private void updateFractalDimension() {
        // todo - consider running this in a separate thread...
//        result = squareCounter.process(svgDoc);
    }

    private void prettyPrint(Node aNode, int depth) {
        Log.gui.debug("                            ".substring(0, depth) + aNode.getClass().getName());
        NodeList children = aNode.getChildNodes();
        for (int i=0; i< children.getLength(); i++) {
            prettyPrint(children.item(i), depth + 1);
        }
    }

    class FractDimPrinter implements Printer {

        public int getNumPages(PageFormat format) {
            return 1;
        }

        public boolean print(Graphics g, PageFormat format, int pageindex) {
            return true;
        }
    }

}
