package uk.co.danielrendall.fractdim.app.workers;

import org.bs.mdi.Document;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 12-Jun-2009
 * Time: 21:46:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class NotifyingWorker<T, V> extends SwingWorker<T,V> {

    protected final Notifiable notifiable;

    public NotifyingWorker(Notifiable notifiable) {
        this.notifiable = notifiable;
    }


    @Override
    final protected void done() {
        doDone();
        notifiable.notifyComplete(this);
    }
    
    abstract protected void doDone();
}