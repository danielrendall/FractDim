/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.logging.PrettyPrinter;
import uk.co.danielrendall.fractdim.svg.SVGContentGenerator;
import uk.co.danielrendall.fractdim.svg.SVGElementCreator;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:08:57
 * To change this template use File | Settings | File Templates.
 */
public class FractalPanel extends JLayeredPane {

    private final JXMultiSplitPane splitPane;
    private final SettingsPanel settingsPanel;
    private final JSVGCanvas canvas;
    private final ResultPanel resultPanel;

    private final JProgressBar progressBar;
    private final JPanel progressPanel;

    // Maps our identifiers for overlays (e.g. "MAX_GRID") to the actual id of the group element representing that overlay
    private final Map<String, String> overlayIdMap;

    // Maps the ID of a group element to its desired z-index (note - these should all be negative)
    private final Map<String, Integer> zIndexMap;

    private final Map<String, BoundingBox> overlayBoundingBoxes;

    private BoundingBox rootBoundingBox;
    private BoundingBox currentBoundingBox;

    private final Queue<Runnable> temporaryRunnableQueue;

    private final Object lock = new Object();

    // need to know when Batik's update manager is ready - then we can start adding things to the document
    private transient boolean updateManagerIsReady = false;

    public FractalPanel() {

        this.settingsPanel = new SettingsPanel();

        canvas = new JSVGCanvas();
        canvas.setDocumentState(AbstractJSVGComponent.ALWAYS_DYNAMIC);
        getActionMap().setParent(canvas.getActionMap());
        JSVGScrollPane canvasScrollPane = new JSVGScrollPane(canvas);

        resultPanel = new ResultPanel();

        overlayIdMap = new HashMap<String, String>();
        zIndexMap = new HashMap<String, Integer>();

        overlayBoundingBoxes = new HashMap<String, BoundingBox>();

        temporaryRunnableQueue = new LinkedList<Runnable>();

        MultiSplitLayout.Split modelRoot = new MultiSplitLayout.Split();
        List<MultiSplitLayout.Node> children =
                Arrays.asList(new MultiSplitLayout.Leaf("settings"),
                        new MultiSplitLayout.Divider(),
                        new MultiSplitLayout.Leaf("svg"),
                        new MultiSplitLayout.Divider(),
                        new MultiSplitLayout.Leaf("results"));
        modelRoot.setChildren(children);

        MultiSplitLayout msl = new MultiSplitLayout(modelRoot);
        msl.setFloatingDividers(true);

        splitPane = new JXMultiSplitPane(msl);

        splitPane.add(settingsPanel, "settings");
        splitPane.add(canvasScrollPane, "svg");
        splitPane.add(new JScrollPane(resultPanel), "results");
        add(splitPane, JLayeredPane.DEFAULT_LAYER);

        progressPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300, 28));
        progressBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, SystemColor.controlHighlight, SystemColor.controlShadow));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.setVisible(false);
        add(progressPanel, JLayeredPane.MODAL_LAYER);

    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        splitPane.setBounds(0, 0, width, height);
        Point center = new Point (x + width / 2, y + height / 2);
        Dimension preferredSize = progressPanel.getPreferredSize();
        Point location = center.displace(new Vec(preferredSize.width, preferredSize.height).scale(-0.5d));

        progressPanel.setBounds((int)location.x(), (int)location.y(), preferredSize.width, preferredSize.height);
        super.setBounds(x, y, width, height);
    }

    public void updateDocument(FractalDocument doc) {
        canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                synchronized (lock) {
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
        // put any existing group elements inside a known root group with ZIndex Integer.MAX_VALUE
        SVGDocument document = doc.getSvgDoc();
        SVGElementCreator creator = new SVGElementCreator(document);
        SVGSVGElement root = document.getRootElement();

        Element group = creator.createGroup();

        NodeList children = root.getChildNodes();
        int childCount = children.getLength();
        List<Node> nodesToAppendToGroup = new ArrayList<Node>();


        for (int i=0; i< childCount; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childEl = (Element) child;
                if ("g".equals(childEl.getTagName())) {
                    nodesToAppendToGroup.add(childEl);
                }
            }
        }
        for (Node node:nodesToAppendToGroup) {
            group.appendChild(root.removeChild(node));
        }
        root.appendChild(group);
        zIndexMap.put(group.getAttributeNS(null, "id"), Integer.MAX_VALUE);
        
        canvas.setSVGDocument(document);
    }


    public void addOverlay(final String overlayId, final int zIndex, final SVGContentGenerator generator) {
        runNowOrLater(overlayId, new Runnable() {
            public void run() {
                addOverlayInUpdateManager(overlayId, zIndex, generator);
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

    public void updateOverlay(final String overlayId, final int zIndex, final SVGContentGenerator generator) {
        runNowOrLater(overlayId, new Runnable() {
            public void run() {
                removeOverlayInUpdateManager(overlayId);
                addOverlayInUpdateManager(overlayId, zIndex, generator);
            }
        });
    }


    private void runNowOrLater(String overlayId, Runnable updater) {
        synchronized (lock) {
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
     *
     * @param overlayId
     * @param generator
     */
    private void addOverlayInUpdateManager(String overlayId, int zIndex, SVGContentGenerator generator) {
        SVGDocument myDoc = canvas.getSVGDocument();
//        StringWriter swBefore = new StringWriter();
//        PrettyPrinter pp = new PrettyPrinter(myDoc.getRootElement());
//        pp.prettyPrint(swBefore);
//        try {
            SVGElementCreator creator = new SVGElementCreator(myDoc);
            SVGSVGElement root = myDoc.getRootElement();

            Element group = creator.createGroup();
            BoundingBox box = generator.generateContent(group, creator);

            final String overlayGroupId = group.getAttributeNS(null, "id");

            overlayIdMap.put(overlayId, overlayGroupId);
            zIndexMap.put(overlayGroupId, zIndex);
            Log.gui.debug(String.format("Overlay name %s groupId %s zIndex %d", overlayId, overlayGroupId, zIndex));

            overlayBoundingBoxes.put(overlayId, box);
            currentBoundingBox = currentBoundingBox.expandToInclude(box);
            NodeList children = root.getChildNodes();
            int childCount = children.getLength();
            Log.gui.debug("Root has " + childCount + " children");
            Node childToInsertBefore = null;
            for (int i=0; i< childCount; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element childEl = (Element) child;
                    if ("g".equals(childEl.getTagName())) {
                        childToInsertBefore = childEl;
                        String id = childEl.getAttributeNS(null, "id");
                        Log.gui.debug("Child "+ i + " has Id " + id);
                        int zIndexOfChild = zIndexMap.get(id);
                        if (zIndexOfChild > zIndex) {
                            break;
                        }
                    }
                }
            }
            if (childToInsertBefore != null) {
                root.insertBefore(group, childToInsertBefore);
            } else {
                root.appendChild(group);
            }
            String viewBox = currentBoundingBox.forSvg();
            root.setAttributeNS(null, "viewBox", viewBox);
            root.setAttributeNS(null, "overflow", "visible");
//            StringWriter swAfter = new StringWriter();
//            PrettyPrinter pp1 = new PrettyPrinter(myDoc.getRootElement());
//            pp1.prettyPrint(swAfter);
//            Log.gui.debug("Before: " + swBefore.toString());
//            Log.gui.debug("After: " + swAfter.toString());
//        } catch (Exception e) {
//            Log.gui.warn(e);
//            Log.gui.debug("Before: " + swBefore.toString());
//        }
    }

    /**
     * This should only ever be called in the Update Manager thread
     *
     * @param overlayId
     */
    private void removeOverlayInUpdateManager(String overlayId) {

        String overlayGroupId = overlayIdMap.remove(overlayId);
        if (overlayGroupId != null) {
            zIndexMap.remove(overlayGroupId);
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
            root.setAttributeNS(null, "viewBox", viewBox);
        } else {
            Log.gui.debug("Overlay hasn't been set");
        }
    }

    public void showProgressBar() {
        progressPanel.setVisible(true);
    }

    public void updateProgressBar(int value) {
        progressBar.setValue(value);
    }

    public void hideProgressBar() {
        progressPanel.setVisible(false);
    }

    // Delegate methods for Settings panel
    public JSlider getMinimumSquareSizeSlider() {
        return settingsPanel.getMinimumSquareSizeSlider();
    }

    public JSlider getMaximumSquareSizeSlider() {
        return settingsPanel.getMaximumSquareSizeSlider();
    }

    public JSlider getAngleSlider() {
        return settingsPanel.getAngleSlider();
    }

    public JSlider getResolutionSlider() {
        return settingsPanel.getResolutionSlider();
    }

    public JSlider getDisplacementSlider() {
        return settingsPanel.getDisplacementSlider();
    }

    public JComboBox getResolutionIteratorList() {
        return settingsPanel.getResolutionIteratorList();
    }

    public JList getResolutionList() {
        return settingsPanel.getResolutionList();
    }

    public void update(SquareCountingResult result) {
        resultPanel.update(result);
    }

    public void addResultPanelListener(ResultPanelListener listener) {
        resultPanel.addResultPanelListener(listener);
    }

    public void removeResultPanelListener(ResultPanelListener listener) {
        resultPanel.removeResultPanelListener(listener);
    }

    public void enableAllControls() {
        settingsPanel.enableAllControls();
    }

    public void disableAllControls() {
        settingsPanel.disableAllControls();
    }
 }
