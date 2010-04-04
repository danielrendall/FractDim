package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:08:57
 * To change this template use File | Settings | File Templates.
 */
public class FractalPanel extends JPanel {

    private final SettingsPanel settingsPanel;
    private final StatisticsPanel statisticsPanel;
    private final JSVGCanvas svgCanvas;
    private final ResultPanel resultPanel;

    public FractalPanel() {
        settingsPanel = new SettingsPanel();
        statisticsPanel = new StatisticsPanel();
        svgCanvas = new JSVGCanvas();
        resultPanel = new ResultPanel();

        Box leftColumn = Box.createVerticalBox();
        leftColumn.add(settingsPanel);
        leftColumn.add(statisticsPanel);

        JSplitPane rightComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JSVGScrollPane(svgCanvas), resultPanel);
        rightComponent.setResizeWeight(1.0d);
        JSplitPane mainComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftColumn, rightComponent);
        this.setLayout(new BorderLayout());
        this.add(mainComponent, BorderLayout.CENTER);
    }

    public void updateSvgDocument(SVGDocument doc) {
        svgCanvas.setSVGDocument(doc);
    }


    public void zoomIn() {
        AffineTransform currentTransform = (AffineTransform) svgCanvas.getRenderingTransform().clone();
        Log.gui.debug(String.format("Before: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
        currentTransform.scale(1.5d, 1.5d);
        svgCanvas.setRenderingTransform(currentTransform);
        Log.gui.debug(String.format("ZoomIn: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
    }

    public void zoomOut() {
        AffineTransform currentTransform = (AffineTransform) svgCanvas.getRenderingTransform().clone();
        Log.gui.debug(String.format("Before: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
        currentTransform.scale(1.0d / 1.5d, 1.0d / 1.5d);
        svgCanvas.setRenderingTransform(currentTransform);
        Log.gui.debug(String.format("ZoomOut: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
    }

    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public ResultPanel getResultPanel() {
        return resultPanel;
    }
}
