package uk.co.danielrendall.fractdim.app.workers;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculator;
import uk.co.danielrendall.fractdim.calculation.Statistics;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculatorBuilder;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.util.concurrent.ExecutionException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07-Jun-2009
 * Time: 10:56:13
 * To change this template use File | Settings | File Templates.
 */
public class CalculateStatisticsWorker extends SwingWorker<Statistics, Integer> implements ProgressListener {

    private boolean useful = true;
    private final FractalDocument document;
    private final Notifiable<CalculateStatisticsWorker> notifiable;

    public CalculateStatisticsWorker(FractalDocument document, Notifiable<CalculateStatisticsWorker> notifiable) {
        this.document = document;
        this.notifiable = notifiable;
    }

    protected Statistics doInBackground() throws Exception {
        StatisticsCalculator sc = new StatisticsCalculatorBuilder().
            minAngle(StatisticsCalculator.TWO_DEGREES).
            fractalDocument(document).
                build();
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

//    @Override
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
