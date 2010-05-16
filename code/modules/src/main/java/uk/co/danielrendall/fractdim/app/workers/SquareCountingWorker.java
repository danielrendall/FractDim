package uk.co.danielrendall.fractdim.app.workers;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.calculation.SquareCounter;
import uk.co.danielrendall.fractdim.calculation.SquareCounterBuilder;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.iterators.*;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Jun-2009
 * Time: 17:41:23
 * To change this template use File | Settings | File Templates.
 */
public class SquareCountingWorker extends SwingWorker<SquareCountingResult, Integer> implements ProgressListener {

    private boolean useful = true;
    private final FractalDocument document;
    private final AngleIterator angleIterator;
    private final ResolutionIterator resolutionIterator;
    private final DisplacementIterator displacementIterator;
    private final Notifiable<SquareCountingWorker> notifiable;

    public SquareCountingWorker(FractalDocument document, AngleIterator angleIterator, ResolutionIterator resolutionIterator, DisplacementIterator displacementIterator, Notifiable<SquareCountingWorker> notifiable) {
        this.document = document;
        this.angleIterator = angleIterator;
        this.resolutionIterator = resolutionIterator;
        this.displacementIterator = displacementIterator;
        this.notifiable = notifiable;
    }

    protected SquareCountingResult doInBackground() throws Exception {

        SquareCounterBuilder builder = new SquareCounterBuilder();
        builder.maxDepth(30).
                angleIterator(angleIterator).
                resolutionIterator(resolutionIterator).
                displacementIterator(displacementIterator).
                fractalDocument(document);


        SquareCounter sc = builder.build();
        sc.addProgressListener(this);
        try {
            return sc.process();
        } catch (OperationAbortedException e) {
            useful = false;
            Log.thread.debug("Operation aborted - caught exception");
            return null;
        }
    }

    public void updateProgress(int minProgress, int progress, int maxProgress) {
        publish((int) (100 * ((double) (progress - minProgress) / (double) (maxProgress - minProgress))));
    }

    @Override
    protected void process(List<Integer> chunks) {
        if (useful && !Thread.currentThread().isInterrupted()) {
            try {
                int last = chunks.get(chunks.size() - 1);
                notifiable.updateProgress(last);
            } catch (Exception e) {
                Log.thread.warn("Problem getting hold of view - " + e.getMessage());
            }
        } else {
            useful = false;
        }

    }

    @Override
    protected void done() {
        try {
            if (useful && !Thread.currentThread().isInterrupted()) {
                notifiable.notifyComplete(this);
            } else {
                useful = false;
            }
        } catch (Exception e) {
            Log.thread.warn("Problem getting hold of view - " + e.getMessage());
        }
    }
}
