package uk.co.danielrendall.fractdim.app.workers;

import uk.co.danielrendall.fractdim.calculation.StatisticsCalculator;
import uk.co.danielrendall.fractdim.calculation.SquareCounts;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.SquareCounter;
import uk.co.danielrendall.fractdim.FDDocument;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.app.FDData;
import uk.co.danielrendall.fractdim.app.FDView;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationSettings;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Jun-2009
 * Time: 17:41:23
 * To change this template use File | Settings | File Templates.
 */
public class SquareCountingWorker extends NotifyingWorker<SquareCountingResult, Integer> implements ProgressListener {

    private boolean useful = true;
    private final FDDocument document;

    public SquareCountingWorker(FDDocument document) {
        this.document = document;
    }

    protected SquareCountingResult doInBackground() throws Exception {
        FDData data = ((FDData) document.getData());
        CalculationSettings settings = data.getSettings(false);
        SquareCounter sc = SquareCounter.createSquareCounter(30,
                settings.getMinimumSquareSize(),
                settings.getMaximumSquareSize(),
                settings.getNumberOfResolutions(),
                settings.getNumberOfAngles(),
                settings.getNumberOfDisplacementPoints());
        sc.addProgressListener(this);
        try {
            return sc.process(data.getSvgDocForCalculation());
        } catch (OperationAbortedException e) {
            useful = false;
            Log.thread.debug("Operation aborted - caught exception");
            return null;
        }
    }

    public void notifyProgress(int minProgress, int progress, int maxProgress) {
        publish((int) (100 * ((double) (progress - minProgress) / (double) (maxProgress - minProgress))));
    }

    @Override
    protected void process(List<Integer> chunks) {
        if (useful && !Thread.currentThread().isInterrupted()) {
            try {
                int last = chunks.get(chunks.size() - 1);
                FDView view = (FDView) document.getView(0);
                view.updateProgressBar(last);
            } catch (Exception e) {
                Log.thread.warn("Problem getting hold of view - " + e.getMessage());
            }
        } else {
            useful = false;
        }

    }

    @Override
    protected void doDone() {
        try {
            if (useful && !Thread.currentThread().isInterrupted()) {
                document.setSquareCountingResult(get());
            } else {
                useful = false;
            }
        } catch (InterruptedException e) {
            Log.thread.debug("Operation aborted");
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Log.thread.warn("Problem getting hold of view - " + e.getMessage());
        }
    }
}
