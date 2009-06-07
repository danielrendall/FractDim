package uk.co.danielrendall.fractdim.app;

import uk.co.danielrendall.fractdim.logging.Log;


/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 06-Jun-2009
 * Time: 09:23:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class FDTaskSupport implements FDTask {

    private boolean shouldStop = false;
    private volatile boolean isActive = true;

    public void stop() {
        Log.thread.info("Task " + getName() + " asked to stop");
        if (shouldStop) {
            Log.thread.warn("Task " + getName() + " asked to stop more than once");
        }
        shouldStop = true;
    }

    protected final boolean shouldStop() {
        return shouldStop;
    }

    public final void run() {
        try {
            doRun();
        } finally {
            isActive = false;
            Log.thread.info("Task " + getName() + " exiting run method");
        }
    }

    public boolean isActive() {
        return isActive;
    }

    protected abstract void doRun();
}
