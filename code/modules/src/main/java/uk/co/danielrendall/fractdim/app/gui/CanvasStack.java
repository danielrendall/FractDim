package uk.co.danielrendall.fractdim.app.gui;

import org.apache.batik.swing.JSVGCanvas;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
public class CanvasStack extends JPanel {

    private final JSVGCanvas rootCanvas;
    private final List<JSVGCanvas> overlays;

    public CanvasStack(JSVGCanvas rootCanvas) {
        this.rootCanvas = rootCanvas;
        rootCanvas.setRecenterOnResize(false);
        overlays = new ArrayList<JSVGCanvas>();
        setLayout(new StackLayout());
        super.add(rootCanvas);
    }

    public void addCanvas(JSVGCanvas canvas) {
        Log.gui.debug("Adding a canvas");
        canvas.setRecenterOnResize(false);
        canvas.setBackground(new Color(0, 0, 0, 0));
        overlays.add(canvas);
        super.add(canvas);
    }

    public void removeCanvas(JSVGCanvas canvas) {
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

    public JSVGCanvas getRootCanvas() {
        return rootCanvas;
    }

    @Override
    public Component getComponent(int i) {
        return super.getComponent(getComponentCount() - 1 - i);
    }

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
}
