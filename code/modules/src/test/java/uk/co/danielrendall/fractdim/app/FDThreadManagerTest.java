package uk.co.danielrendall.fractdim.app;

import org.junit.Test;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 06-Jun-2009
 * Time: 10:20:12
 * To change this template use File | Settings | File Templates.
 */
public class FDThreadManagerTest {


    @Test
    public void testThreadManager() {
        FDThreadManager manager = new FDThreadManager(1000);
        manager.start();
        for (int i = 0; i < 10; i++) {
            FDTask task = new CountingTask(i, 1000 * i);
            manager.addTask(task);
        }
        try {
            Thread.sleep(10000);
            manager.signalStop();
            manager.join(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    private class CountingTask extends FDTaskSupport {

        private final int taskNum;
        private final int countTo;

        private int currentNum = 0;

        CountingTask(int taskNum, int countTo) {
            this.taskNum = taskNum;
            this.countTo = countTo;
        }

        public String getName() {
            return "Counting task " + taskNum + " counting to " + countTo;
        }

        public boolean isActive() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int getMinimumProgress() {
            return 0;
        }

        public int getMaximumProgress() {
            return countTo;
        }

        public int getProgress() {
            return currentNum;
        }

        public void doRun() {
            while (currentNum < countTo) {
                currentNum++;
                try {
                    Thread.sleep(11 - taskNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
//                Log.test.debug(getName() + " at " + currentNum);
            }
            Log.test.debug("Finished " + getName());
        }
    }

}
