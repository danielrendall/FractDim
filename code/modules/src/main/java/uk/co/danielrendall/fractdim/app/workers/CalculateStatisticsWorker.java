package uk.co.danielrendall.fractdim.app.workers;

import uk.co.danielrendall.fractdim.app.FDData;
import uk.co.danielrendall.fractdim.app.FDView;
import uk.co.danielrendall.fractdim.calculation.StatisticsCalculator;
import uk.co.danielrendall.fractdim.calculation.Statistics;

import javax.swing.*;

import org.bs.mdi.Document;

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

    private final Document doc;

    public CalculateStatisticsWorker(Document doc) {
        this.doc = doc;

    }

    protected Statistics doInBackground() throws Exception {
        FDData data = ((FDData) doc.getData());
        StatisticsCalculator sc = new StatisticsCalculator(data.getSvgDoc(true), Math.PI / 90.0d);
        sc.addProgressListener(this);
        return sc.process();
    }

    public void notifyProgress(int minProgress, int progress, int maxProgress) {
        publish((int) (100 * ((double) (progress - minProgress) / (double) (maxProgress - minProgress))));
    }

    @Override
    protected void process(List<Integer> chunks) {
        int last = chunks.get(chunks.size() - 1);
        FDView view = (FDView) doc.getView(0);
        view.updateProgressBar(last);

    }

    @Override
    protected void done() {
        try {
            FDData data = ((FDData) doc.getData());
            data.setStatistics(get());

            FDView view = (FDView) doc.getView(0);
            view.syncWithData();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
