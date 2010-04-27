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


    public UnmodifiableBoundedRangeModel(int value, int min, int max) {
        super(value, 0, min, max);
    }

    public UnmodifiableBoundedRangeModel(double value, double min, double max) {
        this((int) value, (int) min, (int) max);
    }

    @Override
    public void setExtent(int n) {
        throw new UnsupportedOperationException("Can't set extent");
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

}
