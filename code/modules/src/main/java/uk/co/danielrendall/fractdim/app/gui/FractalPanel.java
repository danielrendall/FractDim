package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.svg2svg.PrettyPrinter;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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
    private final CanvasStack canvasStack;
    private final ResultPanel resultPanel;

    private final Map<String, JSVGCanvas> overlays;

    public FractalPanel() {
        settingsPanel = new SettingsPanel();
        statisticsPanel = new StatisticsPanel();
        canvasStack = new CanvasStack(new JSVGCanvas());
        resultPanel = new ResultPanel();
        overlays = new HashMap<String, JSVGCanvas>();

        Box leftColumn = Box.createVerticalBox();
        leftColumn.add(settingsPanel);
        leftColumn.add(statisticsPanel);

        // Todo - write scrollpane implementation based on JSVGScrollPane optimised for stack
        JSplitPane rightComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(canvasStack), resultPanel);
        rightComponent.setResizeWeight(1.0d);
        JSplitPane mainComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftColumn, rightComponent);
        this.setLayout(new BorderLayout());
        this.add(mainComponent, BorderLayout.CENTER);
    }

    public void updateSvgDocument(SVGDocument doc) {
        JSVGCanvas svgCanvas = canvasStack.getRootCanvas();
        svgCanvas.setSVGDocument(doc);
    }


    public void zoomIn() {
        canvasStack.zoomIn(1.5d);
    }

    public void zoomOut() {
        canvasStack.zoomOut(1.5d);
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

    public JSVGCanvas getSVGCanvas() {
        return canvasStack.getRootCanvas();
    }

    public void addOverlay(String overlayId, SVGDocument doc) {
        Log.gui.debug("Adding overlay with ID " + overlayId);
        JSVGCanvas current = overlays.get(overlayId);
        if (current != null) {
            canvasStack.removeCanvas(current);
        }
        JSVGCanvas newCanvas = new JSVGCanvas();
        canvasStack.addCanvas(newCanvas);
        newCanvas.setSVGDocument(doc);
        overlays.put(overlayId, newCanvas);
    }

    public void removeOverlay(String overlayId) {
        JSVGCanvas current = overlays.remove(overlayId);
        if (current != null) {
            canvasStack.removeCanvas(current);
        } else {
            Log.gui.warn("Asked to remove canvas with ID " + overlayId);
        }
    }
}
