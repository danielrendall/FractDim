package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 20:44:44
 * To change this template use File | Settings | File Templates.
 */
public class FileExit extends FractDimAction {

    protected FileExit() {
        putValue(Action.NAME, "Quit");
        setIcons("fileexit");
    }

    public void actionPerformed(ActionEvent e) {
        FractDim.instance().quit(e);
    }
}
