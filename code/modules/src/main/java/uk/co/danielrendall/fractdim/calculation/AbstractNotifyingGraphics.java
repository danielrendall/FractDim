package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.app.workers.ProgressListener;
import uk.co.danielrendall.fractdim.app.workers.OperationAbortedException;
import uk.co.danielrendall.mathlib.geom2d.ParametricCurve;

import java.util.List;
import java.util.LinkedList;

import org.w3c.dom.svg.SVGDocument;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Jun-2009
 * Time: 18:25:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNotifyingGraphics extends FDGraphics2D {

    private final List<ProgressListener> listeners = new LinkedList<ProgressListener>();
    private int curves = 0;
    protected final FractalDocument fractalDocument;
    protected final int numberOfCurves;

    protected AbstractNotifyingGraphics(FractalDocument fractalDocument) {
        this.fractalDocument = fractalDocument;
        if (fractalDocument != null) { // for testing - a bit nasty...
            this.numberOfCurves = fractalDocument.getMetadata().getCurveCount();
        } else {
            this.numberOfCurves = 0;
        }
    }

    public final void handleCurve(ParametricCurve curve) {
        doHandleCurve(curve);
        curves++;
        for (ProgressListener listener : listeners) {
            listener.notifyProgress(0, curves, numberOfCurves);
        }
        if (Thread.currentThread().isInterrupted()) {
            throw new OperationAbortedException();
        }
    }

    abstract void doHandleCurve(ParametricCurve curve);

    public synchronized void addProgressListener(ProgressListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeProgressListener(ProgressListener listener) {
        listeners.remove(listener);
    }

}
