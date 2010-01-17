package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.svgbridge.SVGWithMetadata;
import uk.co.danielrendall.fractdim.app.workers.ProgressListener;
import uk.co.danielrendall.fractdim.app.workers.OperationAbortedException;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;

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
    protected final SVGWithMetadata svgWithMetadata;
    protected final int numberOfCurves;

    protected AbstractNotifyingGraphics(SVGWithMetadata svgWithMetadata) {
        this.svgWithMetadata = svgWithMetadata;
        this.numberOfCurves = svgWithMetadata != null ? svgWithMetadata.getCurveCount() : 1;
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
