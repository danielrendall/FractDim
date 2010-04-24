package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svg.SVGContentGenerator;
import uk.co.danielrendall.fractdim.svg.SVGElementCreator;

import javax.swing.*;
import java.awt.*;
import java.util.*;

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
    private final JSVGCanvas canvas;
    private final ResultPanel resultPanel;

    private final Map<String, String> overlayIdMap;

    private final Queue<Runnable> temporaryRunnableQueue;

    private final Object lock = new Object();

    // need to know when Batik's update manager is ready - then we can start adding things to the document
    private transient boolean updateManagerIsReady = false;

    public FractalPanel() {
        settingsPanel = new SettingsPanel();
        statisticsPanel = new StatisticsPanel();
        canvas = new JSVGCanvas();

        canvas.setDocumentState(AbstractJSVGComponent.ALWAYS_DYNAMIC);

        resultPanel = new ResultPanel();

        overlayIdMap = new HashMap<String, String>();
        temporaryRunnableQueue = new LinkedList<Runnable>();

        Box leftColumn = Box.createVerticalBox();
        leftColumn.add(settingsPanel);
        leftColumn.add(statisticsPanel);

        // Todo - write scrollpane implementation based on JSVGScrollPane optimised for stack
        JSplitPane rightComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JSVGScrollPane(canvas), resultPanel);
        rightComponent.setResizeWeight(1.0d);
        JSplitPane mainComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftColumn, rightComponent);
        this.setLayout(new BorderLayout());
        this.add(mainComponent, BorderLayout.CENTER);
    }

    public void updateSvgDocument(SVGDocument doc) {
        canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
//                synchronized(lock) {
                    updateManagerIsReady = true;
                    for (Iterator<Runnable> it = temporaryRunnableQueue.iterator(); it.hasNext();) {
                        Runnable next = it.next();
                        canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(next);
                        it.remove();
                    }

//                }
            }
        });
        canvas.setSVGDocument(doc);
    }


    public void zoomIn() {
//        canvas.zoomIn(1.5d);
    }

    public void zoomOut() {
//        canvasStack.zoomOut(1.5d);
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
        return canvas;
    }

    public void addOverlay(final String overlayId, final SVGContentGenerator generator) {
        Runnable updater = new Runnable() {
            public void run() {
                Log.gui.debug("Adding overlay with ID " + overlayId);
                SVGDocument myDoc = canvas.getSVGDocument();
                SVGElementCreator creator = new SVGElementCreator(myDoc);
                SVGSVGElement root = myDoc.getRootElement();

                Element group = creator.createGroup();
                generator.generateContent(group, creator);
                overlayIdMap.put(overlayId, group.getAttributeNS(null, "id"));
                root.appendChild(group);
            }
        };
        Log.gui.info("Created runnable, now to try to run it");
//        synchronized(lock) {
            if (updateManagerIsReady) {
                Log.gui.debug("Adding overlay with ID " + overlayId);
                canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(updater);
            } else {
                Log.gui.warn("Update manager wasn't ready... - queuing");
                temporaryRunnableQueue.add(updater);
            }
//        }
    }

    public void removeOverlay(String overlayId) {
//        JSVGCanvas current = overlays.remove(overlayId);
//        if (current != null) {
//            canvasStack.removeCanvas(current);
//        } else {
//            Log.gui.warn("Asked to remove canvas with ID " + overlayId);
//        }
    }
}
