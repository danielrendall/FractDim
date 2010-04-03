package uk.co.danielrendall.fractdim.app.gui.actions;

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 19:21:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class FractDimAction extends AbstractAction {

    protected FractDimAction() {
    }


    protected void setIcons(String name) {
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(FractDimAction.class.getResource("/icons/22/" + name + ".png")));
        putValue(Action.SMALL_ICON, new ImageIcon(FractDimAction.class.getResource("/icons/16/" + name + ".png")));
    }


}
