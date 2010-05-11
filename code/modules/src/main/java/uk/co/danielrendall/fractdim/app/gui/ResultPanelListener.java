package uk.co.danielrendall.fractdim.app.gui;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 11-May-2010
 * Time: 08:21:49
 * To change this template use File | Settings | File Templates.
 */
public interface ResultPanelListener extends EventListener {

    public void gridSelected(GridSelectedEvent e);
}
