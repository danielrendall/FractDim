package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 20:13:52
 * To change this template use File | Settings | File Templates.
 */
public class FileClose extends FractDimAction {

    protected FileClose() {
        putValue(Action.NAME, "Close...");
        setEnabled(false);
        setIcons("fileclose");
    }

    public void actionPerformed(ActionEvent e) {
        FractDim.instance().closeFile(e);
    }
}
