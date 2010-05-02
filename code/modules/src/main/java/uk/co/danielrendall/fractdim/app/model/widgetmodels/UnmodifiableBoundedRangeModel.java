package uk.co.danielrendall.fractdim.app.model.widgetmodels;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Apr-2010
 * Time: 22:03:58
 * To change this template use File | Settings | File Templates.
 */
public class UnmodifiableBoundedRangeModel extends DefaultBoundedRangeModel {


    public UnmodifiableBoundedRangeModel(double value, double extent, double min, double max) {
        super((int) value,(int) extent, (int) min, (int) max);
    }

    public UnmodifiableBoundedRangeModel(double value, double min, double max) {
        this(value, 0.0d, min, max);
    }


    @Override
    public void setMinimum(int n) {
        throw new UnsupportedOperationException("Can't set minimum");
    }

    @Override
    public void setMaximum(int n) {
        throw new UnsupportedOperationException("Can't set maximum");
    }

    public double getDoubleValue() {
        return (double) getValue();
    }

    public int getUpperValue() {
        return  getValue() + getExtent();
    }

    public double getDoubleUpperValue() {
        return (double) getUpperValue();
    }

}
