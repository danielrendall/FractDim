package uk.co.danielrendall.fractdim.app.workers;

import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-May-2010
 * Time: 17:33:58
 * To change this template use File | Settings | File Templates.
 */
public class ExcelExportWorker extends SwingWorker<String, Integer>  {
    
    private boolean useful = true;
    private final SquareCountingResult result;
    private final File xlsFile;
    private final Notifiable<ExcelExportWorker> notifiable;

    public ExcelExportWorker(SquareCountingResult result, File xlsFile, Notifiable<ExcelExportWorker> notifiable) {
        this.result = result;
        this.xlsFile = xlsFile;
        this.notifiable = notifiable;
    }

    @Override
    protected String doInBackground() throws Exception {
        return "Finished";
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
