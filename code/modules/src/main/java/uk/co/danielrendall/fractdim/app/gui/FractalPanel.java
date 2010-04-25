package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svg.SVGContentGenerator;
import uk.co.danielrendall.fractdim.svg.SVGElementCreator;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;

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
    private final Map<String, BoundingBox> overlayBoundingBoxes;

    private BoundingBox rootBoundingBox;
    private BoundingBox currentBoundingBox;

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
        overlayBoundingBoxes = new HashMap<String, BoundingBox>();

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

    public void updateDocument(FractalDocument doc) {
        canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                synchronized(lock) {
                    updateManagerIsReady = true;
                    for (Iterator<Runnable> it = temporaryRunnableQueue.iterator(); it.hasNext();) {
                        Runnable next = it.next();
                        canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(next);
                        it.remove();
                    }

                }
            }
        });
        rootBoundingBox = doc.getMetadata().getBoundingBox();
        currentBoundingBox = rootBoundingBox;
        canvas.setSVGDocument(doc.getSvgDoc());

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
        runNowOrLater(overlayId, new Runnable() {
            public void run() {
                addOverlayInUpdateManager(overlayId, generator);
            }
        });
    }


    public void removeOverlay(final String overlayId) {
        runNowOrLater(overlayId, new Runnable() {
            public void run() {
                removeOverlayInUpdateManager(overlayId);

            }
        });
    }

    public void updateOverlay(final String overlayId, final SVGContentGenerator generator) {
        runNowOrLater(overlayId, new Runnable() {
            public void run() {
                removeOverlayInUpdateManager(overlayId);
                addOverlayInUpdateManager(overlayId, generator);
            }
        });
    }


    private void runNowOrLater(String overlayId, Runnable updater) {
        synchronized(lock) {
            if (updateManagerIsReady) {
                Log.gui.debug("Running operation on overlay " + overlayId);
                canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(updater);
            } else {
                Log.gui.debug("Queuing operation on overlay " + overlayId);
                temporaryRunnableQueue.add(updater);
            }
        }
    }

    /**
     * This should only ever be called in the Update Manager thread
     * @param overlayId
     * @param generator
     */
    private void addOverlayInUpdateManager(String overlayId, SVGContentGenerator generator) {
        Log.gui.debug("Adding overlay with ID " + overlayId);
        SVGDocument myDoc = canvas.getSVGDocument();
        SVGElementCreator creator = new SVGElementCreator(myDoc);
        SVGSVGElement root = myDoc.getRootElement();

        Element group = creator.createGroup();
        BoundingBox box = generator.generateContent(group, creator);


        overlayIdMap.put(overlayId, group.getAttributeNS(null, "id"));
        overlayBoundingBoxes.put(overlayId, box);
        currentBoundingBox = currentBoundingBox.expandToInclude(box);
        root.appendChild(group);
        String viewBox = currentBoundingBox.forSvg();
        Log.gui.debug("Current bounding box is " + root.getAttributeNS(null, "viewBox"));
        Log.gui.debug("Setting bounding box to " + viewBox);
        root.setAttributeNS(null, "viewBox", viewBox);
        root.setAttributeNS(null, "overflow", "visible");
    }

    /**
     * This should only ever be called in the Update Manager thread
     * @param overlayId
     */
    private void removeOverlayInUpdateManager(String overlayId) {
        Log.gui.debug("Removing overlay with ID " + overlayId);

        String overlayGroupId = overlayIdMap.remove(overlayId);
        if (overlayGroupId != null) {
            SVGDocument myDoc = canvas.getSVGDocument();
            SVGSVGElement root = myDoc.getRootElement();
            Element el = root.getElementById(overlayGroupId);
            if (el != null) {
                root.removeChild(el);
            }
            overlayBoundingBoxes.remove(overlayGroupId);
            BoundingBox newBoundingBox = rootBoundingBox;
            for (BoundingBox box : overlayBoundingBoxes.values()) {
                newBoundingBox = newBoundingBox.expandToInclude(box);
            }
            currentBoundingBox = newBoundingBox;
            String viewBox = currentBoundingBox.forSvg();
            Log.gui.debug("Setting bounding box to " + viewBox);
            root.setAttributeNS(null, "viewBox", viewBox);
        }
    }
}
