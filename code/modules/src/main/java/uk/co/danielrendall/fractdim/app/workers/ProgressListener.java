package uk.co.danielrendall.fractdim.app.workers;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 08-Jun-2009
 * Time: 23:35:24
 * To change this template use File | Settings | File Templates.
 */
public interface ProgressListener {

    void updateProgress(int minProgress, int progress, int maxProgress);
}
