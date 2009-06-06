package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.swing.SwingDefaultCommands;
import org.bs.mdi.swing.SwingCommand;
import org.bs.mdi.swing.SwingCommandAdapter;
import org.bs.mdi.Application;

import javax.swing.*;

/**
 * @author Daniel Rendall
 * @created 30-May-2009 12:24:57
 */
public class FDCommands extends SwingDefaultCommands {

    SwingCommand fileGenerateCmd = new FileGenerateCmd();

    public SwingCommand getFileGenerateCommand() {
        return fileGenerateCmd;
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


}
