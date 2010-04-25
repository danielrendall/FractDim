package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 22:21:49
 * To change this template use File | Settings | File Templates.
 */
public class DiagramZoomIn extends FractDimDelegatedAction {

    protected DiagramZoomIn() {
        putValue(Action.NAME, "Zoom in");
        setEnabled(false);
        setIcons("zoomin");
    }

}