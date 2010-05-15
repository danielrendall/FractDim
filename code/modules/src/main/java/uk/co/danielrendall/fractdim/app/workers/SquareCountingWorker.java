package uk.co.danielrendall.fractdim.app.workers;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.calculation.SquareCounter;
import uk.co.danielrendall.fractdim.calculation.SquareCounterBuilder;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.iterators.LogarithmicResolutionIterator;
import uk.co.danielrendall.fractdim.calculation.iterators.UniformAngleIterator;
import uk.co.danielrendall.fractdim.calculation.iterators.UniformDisplacementIterator;
import uk.co.danielrendall.fractdim.calculation.iterators.UniformResolutionIterator;
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
    private final double minimumSquareSize;
    private final double maximumSquareSize;
    private final int numberOfResolutions;
    private final int numberOfAngles;
    private final int numberOfDisplacements;
    private final Notifiable<SquareCountingWorker> notifiable;

    public SquareCountingWorker(FractalDocument document, double minimumSquareSize, double maximumSquareSize, int numberOfResolutions, int numberOfAngles, int numberOfDisplacements, Notifiable<SquareCountingWorker> notifiable) {
        this.document = document;
        this.minimumSquareSize = minimumSquareSize;
        this.maximumSquareSize = maximumSquareSize;
        this.numberOfResolutions = numberOfResolutions;
        this.numberOfAngles = numberOfAngles;
        this.numberOfDisplacements = numberOfDisplacements;
        this.notifiable = notifiable;
    }

    protected SquareCountingResult doInBackground() throws Exception {

        SquareCounterBuilder builder = new SquareCounterBuilder();
        builder.maxDepth(30).
                angleIterator(new UniformAngleIterator(numberOfAngles)).
                resolutionIterator(new LogarithmicResolutionIterator(minimumSquareSize, maximumSquareSize, numberOfResolutions)).
                displacementIterator(new UniformDisplacementIterator(numberOfDisplacements)).
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
