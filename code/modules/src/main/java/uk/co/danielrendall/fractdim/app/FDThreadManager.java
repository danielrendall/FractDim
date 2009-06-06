package uk.co.danielrendall.fractdim.app;

import uk.co.danielrendall.fractdim.logging.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 06-Jun-2009
 * Time: 09:16:50
 * To change this template use File | Settings | File Templates.
 */
public class FDThreadManager extends Thread {

    private final List<FDThread> threads;
    private boolean okToAddTasks = true;
    private boolean shouldStop = false;
    private final long waitTime;

    public FDThreadManager(int pollingFrequency) {
        threads = new ArrayList<FDThread>();
        waitTime = (long) 1000 / pollingFrequency;
    }

    public synchronized void signalStop() {
        okToAddTasks = false;
        for(FDThread thread: threads) {
            thread.signalStop();
        }
        shouldStop = true;
    }

    public void run() {
        while (!shouldStop) {
            synchronized (this) {
                int threadsBefore = threads.size();
                for (Iterator<FDThread> it = threads.iterator(); it.hasNext();) {
                    FDThread thread = it.next();
                    // TODO - can I use isAlive for this?
                    if (thread.isFinished()) {
                        try {
                            thread.join(1000L);
                            it.remove();
                        } catch (InterruptedException e) {
                            Log.thread.warn("Interrupted while joining a thread");
                        }
                    }
                }
                int threadsAfter = threads.size();
                if (threadsAfter != threadsBefore) {
                    Log.thread.debug("Thread count was " + threadsBefore + " and is now " + threadsAfter);
                }
                try {
//                    Log.thread.debug("Waiting");
                    wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void addTask(FDTask task) {
        if (okToAddTasks) {
            FDThread newThread = new FDThread(task);
            threads.add(newThread);
            Log.thread.debug("Starting thread at index " + threads.indexOf(newThread) + " for " + task.getName());
            newThread.start();
        }
    }



    private class FDThread extends Thread {

        private final FDTask task;

        FDThread(FDTask task) {
            super(task);
            // not sure if I can get a handle to the Runnable...
            this.task = task;
        }

        public void run() {
            super.run();
        }

        void signalStop() {
            task.stop();
        }

        boolean isFinished() {
            return task.isActive();
        }
    }
}
