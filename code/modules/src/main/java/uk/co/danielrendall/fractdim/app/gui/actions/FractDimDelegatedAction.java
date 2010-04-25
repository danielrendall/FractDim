package uk.co.danielrendall.fractdim.app.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 25-Apr-2010
 * Time: 10:19:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class FractDimDelegatedAction extends FractDimAction {

    protected Action delegate = NullAction.INSTANCE;

    public void setDelegate(Action action) {
        delegate = action;
        setEnabled(true);
    }

    public void removeDelegate() {
        setEnabled(false);
        delegate = NullAction.INSTANCE;
    }

    public final void actionPerformed(ActionEvent e) {
        delegate.actionPerformed(e);
    }
}
