package uk.co.danielrendall.fractdim.app.gui.actions;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-May-2010
 * Time: 16:48:57
 * To change this template use File | Settings | File Templates.
 */
public class FileExport extends FractDimDelegatedAction {

    protected FileExport() {
        putValue(Action.NAME, "Export");
        setEnabled(false);
        setIcons("fileexport");
    }
}
