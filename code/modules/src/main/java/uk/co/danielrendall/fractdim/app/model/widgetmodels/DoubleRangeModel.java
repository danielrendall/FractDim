package uk.co.danielrendall.fractdim.app.model.widgetmodels;

import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 02-May-2010
 * Time: 12:09:12
 * To change this template use File | Settings | File Templates.
 */
public class DoubleRangeModel implements BoundedRangeModel {

    private final BoundedRangeModel delegate;

    public DoubleRangeModel(BoundedRangeModel delegate) {
        this.delegate = delegate;
    }

    public DoubleRangeModel(double rangeMin, double rangeExtent, double minimumBoxSize, double maximumBoxSize) {
        this(new DefaultBoundedRangeModel((int)rangeMin, (int) rangeExtent, (int) minimumBoxSize, (int) maximumBoxSize));
    }

    public double getLowerValue() {
        return (double) delegate.getValue();
    }


    public double getUpperValue() {
        return (double) (delegate.getValue() + delegate.getExtent());
    }

    public int getMinimum() {
        return delegate.getMinimum();
    }

    public void setMinimum(int newMinimum) {
        delegate.setMinimum(newMinimum);
    }

    public int getMaximum() {
        return delegate.getMaximum();
    }

    public void setMaximum(int newMaximum) {
        delegate.setMaximum(newMaximum);
    }

    public int getValue() {
        return delegate.getValue();
    }

    public void setValue(int newValue) {
        delegate.setValue(newValue);
    }

    public void setValueIsAdjusting(boolean b) {
        delegate.setValueIsAdjusting(b);
    }

    public boolean getValueIsAdjusting() {
        return delegate.getValueIsAdjusting();
    }

    public int getExtent() {
        return delegate.getExtent();
    }

    public void setExtent(int newExtent) {
        delegate.setExtent(newExtent);
    }

    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
        delegate.setRangeProperties(value, extent, min, max, adjusting);
    }

    public void addChangeListener(ChangeListener x) {
        delegate.addChangeListener(x);
    }

    public void removeChangeListener(ChangeListener x) {
        delegate.removeChangeListener(x);
    }
}
