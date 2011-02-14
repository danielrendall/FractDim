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

package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.app.workers.ProgressListener;
import uk.co.danielrendall.fractdim.app.workers.OperationAbortedException;
import uk.co.danielrendall.mathlib.geom2d.ParametricCurve;

import java.util.List;
import java.util.LinkedList;

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
            listener.updateProgress(0, curves, numberOfCurves);
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
