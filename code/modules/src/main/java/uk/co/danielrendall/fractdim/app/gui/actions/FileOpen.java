package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 19:19:52
 * To change this template use File | Settings | File Templates.
 */
public class FileOpen extends FractDimAction {

    FileOpen() {
        putValue(Action.NAME, "Open...");
        setIcons("fileopen");

    }

    public void actionPerformed(ActionEvent e) {
        FractDim.instance().openFile(e);
    }
}
