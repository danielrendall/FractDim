package uk.co.danielrendall.fractdim.app.workers;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 11-Jun-2009
 * Time: 21:24:19
 * To change this template use File | Settings | File Templates.
 */
public class OperationAbortedException extends RuntimeException {

    public OperationAbortedException() {
    }

    public OperationAbortedException(String message) {
        super(message);
    }

    public OperationAbortedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationAbortedException(Throwable cause) {
        super(cause);
    }
}
