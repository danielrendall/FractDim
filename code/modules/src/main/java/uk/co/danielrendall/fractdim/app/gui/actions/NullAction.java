package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 25-Apr-2010
 * Time: 10:20:17
 * To change this template use File | Settings | File Templates.
 */
public class NullAction extends AbstractAction {

    public final static NullAction INSTANCE = new NullAction();

    public void actionPerformed(ActionEvent e) {
       Log.gui.debug("Null action triggered");
    }
}
