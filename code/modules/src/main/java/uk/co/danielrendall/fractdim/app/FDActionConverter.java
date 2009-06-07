package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Action;
import org.bs.mdi.ActionConverter;

import java.awt.datatransfer.Transferable;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:38:19
 */
public class FDActionConverter implements ActionConverter {
    public boolean canHandle(Action action) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean canHandle(Transferable transferable) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Transferable toTransferable(Action action) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Action toAction(Transferable transferable) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
