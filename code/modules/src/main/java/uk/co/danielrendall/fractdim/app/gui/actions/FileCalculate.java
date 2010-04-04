package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 04-Apr-2010
 * Time: 21:15:08
 * To change this template use File | Settings | File Templates.
 */
public class FileCalculate extends FractDimAction {

    protected FileCalculate() {
        putValue(Action.NAME, "Calculate");
        setEnabled(false);
        setIcons("calculate");
    }

    public void actionPerformed(ActionEvent e) {
        FractDim.instance().calculate(e);
    }
}
