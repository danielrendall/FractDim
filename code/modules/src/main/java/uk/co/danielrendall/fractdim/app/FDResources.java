package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.Resources;
import org.bs.mdi.DefaultResources;

import javax.swing.*;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:42:02
 */
public class FDResources extends DefaultResources {
    
    public void loadResources() {
        putMdiIcon("Generate", "generate.png", MENU_ICON);
        putMdiIcon("Generate", "generate.png", TOOLBAR_ICON);
    }

}
