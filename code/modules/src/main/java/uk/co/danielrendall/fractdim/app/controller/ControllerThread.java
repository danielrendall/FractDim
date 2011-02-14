/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.fractdim.app.controller;

import uk.co.danielrendall.fractdim.logging.Log;

import java.util.LinkedList;
import java.util.Queue;

public class ControllerThread extends Thread {
    private final FractalController fractalController;
    private final Queue<TypedRunnable> jobs;
    private volatile boolean shouldQuit = false;

    public ControllerThread(FractalController fractalController) {
        jobs = new LinkedList<TypedRunnable>();
        this.fractalController = fractalController;
        setDaemon(true);
    }

    // main lifecycle of controller. Does things, updates status.
    public void run() {
        while (!shouldQuit) {
            for (TypedRunnable r = getFromQueue(); r != null; r = getFromQueue()) {
                r.getRunnable().run();
            }

            try {
                Log.thread.info("Sleeping...");
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // ignore
            }
            Log.thread.info("Awoken");
        }
    }

    synchronized void addToQueue(String type, Runnable r) {
        // if it's another job like the pending job, get rid of the pending job and replace with a new one
        TypedRunnable currentNextJob = jobs.peek();
        if (currentNextJob == null || type.equals(currentNextJob.getType())) {
            jobs.poll();
        }
        jobs.add(new TypedRunnable(type, r));

        interrupt();
    }

    synchronized TypedRunnable getFromQueue() {
        return jobs.poll();
    }

    void checkControllerThread() {
        if (Thread.currentThread() != this)
            throw new IllegalStateException("Should be called in controller thread");
    }

    public void quit() {
        shouldQuit = true;
        interrupt();
        try {
            join();
            Log.thread.info("Joined controller thread");
        } catch (InterruptedException e) {
            Log.thread.warn("Couldn't join controller thread - " + e.getMessage());
        }

    }

    private static class TypedRunnable {
        private final String type;
        private final Runnable runnable;

        private TypedRunnable(String type, Runnable r) {
            this.type = type;
            this.runnable = r;
        }

        public String getType() {
            return type;
        }

        public Runnable getRunnable() {
            return runnable;
        }
    }
}