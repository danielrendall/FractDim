package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Task;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 06-Jun-2009
 * Time: 09:20:14
 * To change this template use File | Settings | File Templates.
 */
public interface FDTask extends Task, Runnable {

    public void stop();
}
