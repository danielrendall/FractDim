package uk.co.danielrendall.fractdim.app.model;

import uk.co.danielrendall.fractdim.app.model.widgetmodels.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Apr-2010
 * Time: 18:41:50
 * To change this template use File | Settings | File Templates.
 */
public interface ParameterChangeListener {

    public void valueChanged(Parameter param, int value);
}
