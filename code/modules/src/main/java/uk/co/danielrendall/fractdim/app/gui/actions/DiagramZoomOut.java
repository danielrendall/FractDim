package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 22:24:26
 * To change this template use File | Settings | File Templates.
 */
public class DiagramZoomOut extends FractDimAction {

    protected DiagramZoomOut() {
        putValue(Action.NAME, "Zoom out");
        setEnabled(false);
        setIcons("zoomout");
    }

    public void actionPerformed(ActionEvent e) {
        FractDim.instance().zoomOut(e);
    }
}