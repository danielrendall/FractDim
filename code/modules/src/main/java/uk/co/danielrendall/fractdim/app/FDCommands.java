package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Application;
import org.bs.mdi.Document;
import org.bs.mdi.swing.SwingCommand;
import org.bs.mdi.swing.SwingCommandAdapter;
import org.bs.mdi.swing.SwingDefaultCommands;

import javax.swing.*;

import uk.co.danielrendall.fractdim.app.workers.SquareCountingWorker;
import uk.co.danielrendall.fractdim.FDDocument;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 12:24:57
 */
public class FDCommands extends SwingDefaultCommands {

    SwingCommand fileGenerateCmd = new FileGenerateCmd();
    SwingCommand fileCalculateCmd = new FileCalculateCmd();

    public SwingCommand getFileGenerateCommand() {
        return fileGenerateCmd;
    }

    public SwingCommand getFileCalculateCommand() {
        return fileCalculateCmd;
    }

    class FileGenerateCmd extends SwingCommandAdapter {
        public FileGenerateCmd() {
            super("Generate", "Generates a new fractal");
            setAvailable(true);
        }

        protected void doExecute() {
            ((FractDim)Application.getInstance()).generateNewFractal();
        }

        public KeyStroke getAccelerator() {
            return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G,
                    java.awt.event.InputEvent.CTRL_MASK);
        }
    }

    class FileCalculateCmd extends SwingCommandAdapter {
        public FileCalculateCmd() {
            super("Calculate", "Calculates fractal dimension");
            setAvailable(false);
        }

        protected void doExecute() {
            Document currentDoc = Application.getCurrentDocument();
            currentDoc.addWorker(new SquareCountingWorker((FDDocument) currentDoc));
        }
    }

}
