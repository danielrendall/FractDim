package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.gvt.JGVTComponentListener;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherListener;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Apr-2010
 * Time: 10:21:49
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class CanvasStack extends JPanel {

    private final JSVGCanvas rootCanvas;
    private final List<JSVGCanvas> overlays;
    // We may want to set this on our children, and we'll need to
    // ensure any new children added to us have it set.
    private boolean recenterOnResize = false;

    public CanvasStack() {
        this.rootCanvas = new JSVGCanvas();
        rootCanvas.setRecenterOnResize(false);
        overlays = new ArrayList<JSVGCanvas>();
        setLayout(new StackLayout());
        super.add(rootCanvas);
    }

    

    private void addCanvas(JSVGCanvas canvas) {
        Log.gui.debug("Adding a canvas");
        canvas.setRecenterOnResize(false);
        canvas.setBackground(new Color(0, 0, 0, 0));
        canvas.setRenderingTransform(rootCanvas.getRenderingTransform());
        canvas.setPaintingTransform(rootCanvas.getPaintingTransform());
        canvas.setMinimumSize(rootCanvas.getMinimumSize());
        canvas.setMaximumSize(rootCanvas.getMaximumSize());
        canvas.setPreferredSize(rootCanvas.getPreferredSize());
        overlays.add(canvas);
        super.add(canvas);
    }

    private void removeCanvas(JSVGCanvas canvas) {
        if (canvas != rootCanvas) {
            if (overlays.remove(canvas)) {
                super.remove(canvas);
            } else {
                Log.gui.warn("Asked to remove canvas which wasn't in the stack");
            }
        } else {
            Log.gui.warn("Asked to remove root canvas");
        }
    }

    //TODO -repaint? Something sensible?
    public void setRootSVGDoc(SVGDocument doc) {
        rootCanvas.setSVGDocument(doc);
    }

//    public JSVGCanvas getRootCanvas() {
//        return rootCanvas;
//    }

    @Override
    public Component getComponent(int i) {
        return super.getComponent(getComponentCount() - 1 - i);
    }

    void setRecenterOnResize(boolean recenterOnResize) {
        this.recenterOnResize = recenterOnResize;
        rootCanvas.setRecenterOnResize(recenterOnResize);
        for (JSVGCanvas canvas : overlays) {
            canvas.setRecenterOnResize(false);
        }
    }

    // Delegate
    public void addSVGDocumentLoaderListener(SVGDocumentLoaderListener loadListener) {
        rootCanvas.addSVGDocumentLoaderListener(loadListener);
    }

    // Delegate
    public void removeSVGDocumentLoaderListener(SVGDocumentLoaderListener l) {
        rootCanvas.removeSVGDocumentLoaderListener(l);
    }

    // Delegate
    public void addGVTTreeBuilderListener(GVTTreeBuilderListener l) {
        rootCanvas.addGVTTreeBuilderListener(l);
    }

    // Delegate
    public void removeGVTTreeBuilderListener(GVTTreeBuilderListener l) {
        rootCanvas.removeGVTTreeBuilderListener(l);
    }

    // Delegate
    public void addGVTTreeRendererListener(GVTTreeRendererListener l) {
        rootCanvas.addGVTTreeRendererListener(l);
    }

    // Delegate
    public void removeGVTTreeRendererListener(GVTTreeRendererListener l) {
        rootCanvas.removeGVTTreeRendererListener(l);
    }

    // Delegate
    public void addSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener l) {
        rootCanvas.addSVGLoadEventDispatcherListener(l);
    }

    // Delegate
    public void removeSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener l) {
        rootCanvas.removeSVGLoadEventDispatcherListener(l);
    }

    // Delegate
    public void addJGVTComponentListener(JGVTComponentListener listener) {
        rootCanvas.addJGVTComponentListener(listener);
    }

    // Delegate
    public void removeJGVTComponentListener(JGVTComponentListener listener) {
        rootCanvas.removeJGVTComponentListener(listener);
    }

    // Delegate
    public void addUpdateManagerListener(UpdateManagerListener listener) {
        rootCanvas.addUpdateManagerListener(listener);
    }

    // Delegate
    public void removeUpdateManagerListener(UpdateManagerListener listener) {
        rootCanvas.removeUpdateManagerListener(listener);
    }

    public AffineTransform getRenderingTransform() {
        return rootCanvas.getRenderingTransform();
    }

    public void setRenderingTransform(AffineTransform at) {
        rootCanvas.setRenderingTransform(at);
        for(JSVGCanvas canvas : overlays) {
            canvas.setRenderingTransform(at);
        }
    }

    public AffineTransform getViewBoxTransform() {
        return rootCanvas.getViewBoxTransform();
    }

    public void setPaintingTransform(AffineTransform at) {
        rootCanvas.setPaintingTransform(at);
        for(JSVGCanvas canvas : overlays) {
            canvas.setPaintingTransform(at);
        }
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return rootCanvas.getSize(rv);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        rootCanvas.setPreferredSize(preferredSize);
        for(JSVGCanvas canvas : overlays) {
            canvas.setPreferredSize(preferredSize);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return rootCanvas.getPreferredSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        rootCanvas.setMaximumSize(maximumSize);
        for(JSVGCanvas canvas : overlays) {
            canvas.setMaximumSize(maximumSize);
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return rootCanvas.getMaximumSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        rootCanvas.setMinimumSize(minimumSize);
        for(JSVGCanvas canvas : overlays) {
            canvas.setMinimumSize(minimumSize);
        }
    }

    public Dimension getMinimumSize() {
        return rootCanvas.getMinimumSize();
    }

    @Override
    public boolean getVerifyInputWhenFocusTarget() {
        return super.getVerifyInputWhenFocusTarget();    //To change body of overridden methods use File | Settings | File Templates.
    }

    // probably better handled by the scrollpane
    @Deprecated
    void zoomIn(double factor) {
        AffineTransform currentTransform = (AffineTransform) rootCanvas.getRenderingTransform().clone();
        Log.gui.debug(String.format("Before: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
        currentTransform.scale(factor, factor);
        rootCanvas.setRenderingTransform(currentTransform);
        for (JSVGCanvas svgCanvas : overlays) {
            svgCanvas.setRenderingTransform(currentTransform);
        }
        Log.gui.debug(String.format("ZoomIn: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
    }

    // probably better handled by the scrollpane
    @Deprecated
    void zoomOut(double factor) {
        double iFactor = 1.0d / factor;
        AffineTransform currentTransform = (AffineTransform) rootCanvas.getRenderingTransform().clone();
        Log.gui.debug(String.format("Before: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
        currentTransform.scale(iFactor, iFactor);
        rootCanvas.setRenderingTransform(currentTransform);
        for (JSVGCanvas svgCanvas : overlays) {
            svgCanvas.setRenderingTransform(currentTransform);
        }
        Log.gui.debug(String.format("ZoomOut: translate (%s, %s) Scale (%s, %s)", currentTransform.getTranslateX(), currentTransform.getTranslateY(), currentTransform.getScaleX(), currentTransform.getScaleY()));
    }

    // these two are to make tests compile... may not be needed
    public void setRootURI(String uri) {
        rootCanvas.setURI(uri);
    }

    public void addFromURI(String uri) {
        JSVGCanvas canvas = new JSVGCanvas();
        canvas.setURI(uri);
        addCanvas(canvas);
    }
}
