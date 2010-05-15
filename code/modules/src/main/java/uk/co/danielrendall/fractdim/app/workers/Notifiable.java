package uk.co.danielrendall.fractdim.app.workers;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 12-Jun-2009
 * Time: 22:14:03
 * To change this template use File | Settings | File Templates.
 */
public interface Notifiable<T extends SwingWorker> {

    void notifyComplete(T worker);

    void updateProgress(int progress);
}
