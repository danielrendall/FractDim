package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.app.workers.ProgressListener;
import uk.co.danielrendall.fractdim.app.workers.OperationAbortedException;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.logging.Log;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

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
    protected int numberOfCurves = 0;

    protected void initCurveCount(SVGDocument svgDoc) {
        numberOfCurves = new CurveCounter(svgDoc).getCurveCount();
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
