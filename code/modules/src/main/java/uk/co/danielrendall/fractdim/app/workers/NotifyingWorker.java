package uk.co.danielrendall.fractdim.app.workers;

import org.bs.mdi.Document;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 12-Jun-2009
 * Time: 21:46:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class NotifyingWorker<T, V> extends SwingWorker<T,V> {

    protected final Set<Notifiable> notifiables;

    public NotifyingWorker() {
        notifiables = new HashSet<Notifiable>();
    }

    public void addNotifiable(Notifiable notifiable) {
        notifiables.add(notifiable);
    }


    @Override
    final protected void done() {
        doDone();
        for(Notifiable notifiable : notifiables) {
            notifiable.notifyComplete(this);
        }
    }
    
    abstract protected void doDone();
}