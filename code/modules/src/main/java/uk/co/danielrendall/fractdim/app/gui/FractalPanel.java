package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:08:57
 * To change this template use File | Settings | File Templates.
 */
public class FractalPanel extends JPanel {

    private final SettingsPanel settings;
    private final StatisticsPanel statistics;
    private final JSVGCanvas svgCanvas;
    private final ResultPanel result;

    public FractalPanel() {
        settings = new SettingsPanel();
        statistics = new StatisticsPanel();
        svgCanvas = new JSVGCanvas();
        result = new ResultPanel();

        Box leftColumn = Box.createVerticalBox();
        leftColumn.add(settings);
        leftColumn.add(statistics);


        JSplitPane rightComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(svgCanvas), result);
        rightComponent.setResizeWeight(1.0d);
        JSplitPane mainComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftColumn, rightComponent);
        this.setLayout(new BorderLayout());
        this.add(mainComponent, BorderLayout.CENTER);
    }

    public void updateSvgDocument(SVGDocument doc) {
        svgCanvas.setSVGDocument(doc);
    }
    
}
