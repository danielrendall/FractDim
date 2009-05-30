package org.bs.mdi;

import java.util.*;
import javax.swing.*;

/**
 * Provides a default set of translation and icon resources.
 */
public class DefaultResources extends Resources {
	
	Hashtable icons;
	ResourceBundle strings;

    // Use the classloader used to load the application to load resources
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#loadResources()
	 */
	public void loadResources() {
		strings = ResourceBundle.getBundle("translations.jmdi", getLocale());
		icons = new Hashtable();
		putMdiIcon("Copy", "editcopy.png", MENU_ICON);
		putMdiIcon("Copy", "editcopy.png", TOOLBAR_ICON);
		putMdiIcon("Cut", "editcut.png", MENU_ICON);
		putMdiIcon("Cut", "editcut.png", TOOLBAR_ICON);
		putMdiIcon("Paste", "editpaste.png", MENU_ICON);
		putMdiIcon("Paste", "editpaste.png", TOOLBAR_ICON);
		putMdiIcon("Delete", "editdelete.png", MENU_ICON);
		putMdiIcon("Delete", "editdelete.png", TOOLBAR_ICON);
		putMdiIcon("Redo", "editredo.png", MENU_ICON);
		putMdiIcon("Redo", "editredo.png", TOOLBAR_ICON);
		putMdiIcon("Undo", "editundo.png", MENU_ICON);
		putMdiIcon("Undo", "editundo.png", TOOLBAR_ICON);
		putMdiIcon("Close", "fileclose.png", MENU_ICON);
		putMdiIcon("Close", "fileclose.png", TOOLBAR_ICON);
		putMdiIcon("Quit", "fileexit.png", MENU_ICON);
		putMdiIcon("Quit", "fileexit.png", TOOLBAR_ICON);
		putMdiIcon("New", "filenew.png", MENU_ICON);
		putMdiIcon("New", "filenew.png", TOOLBAR_ICON);
		putMdiIcon("Open...", "fileopen.png", MENU_ICON);
		putMdiIcon("Open...", "fileopen.png", TOOLBAR_ICON);
		putMdiIcon("Save", "filesave.png", MENU_ICON);
		putMdiIcon("Save", "filesave.png", TOOLBAR_ICON);
		putMdiIcon("Save As...", "filesaveas.png", MENU_ICON);
		putMdiIcon("Save As...", "filesaveas.png", TOOLBAR_ICON);
		putMdiIcon("Print...", "fileprint.png", MENU_ICON);
		putMdiIcon("Print...", "fileprint.png", TOOLBAR_ICON);
		putMdiIcon("Window", "window.png", MENU_ICON);
		putMdiIcon("Previous Page", "previous.png", MENU_ICON);
		putMdiIcon("Next Page", "next.png", MENU_ICON);
		putMdiIcon("First Page", "first.png", MENU_ICON);
		putMdiIcon("Last Page", "last.png", MENU_ICON);
		putMdiIcon("Back", "back.png", BUTTON_ICON);
		putMdiIcon("Next", "next.png", BUTTON_ICON);
		putMdiIcon("Zoom In", "zoomin.png", MENU_ICON);
		putMdiIcon("Zoom Out", "zoomout.png", MENU_ICON);
		putMdiIcon("OK", "ok.png", BUTTON_ICON);
		putMdiIcon("Cancel", "cancel.png", BUTTON_ICON);
		putMdiIcon("Yes", "yes.png", BUTTON_ICON);
		putMdiIcon("No", "no.png", BUTTON_ICON);
		putMdiIcon("Information", "messagebox_info.png", MESSAGEBOX_ICON);
		putMdiIcon("Warning", "messagebox_warning.png", MESSAGEBOX_ICON);
		putMdiIcon("Error", "messagebox_error.png", MESSAGEBOX_ICON);
		putMdiIcon("Question", "messagebox_question.png", MESSAGEBOX_ICON);
	}
	
	protected void putIcon(String key, Icon icon, int size) {
		icons.put(key + size, icon);
	}
	
	protected void putMdiIcon(String key, String filename, int size) {
        // Not ClassLoader.getSystemResource - doesn't work with one-jar
		try {
			putIcon(key, new ImageIcon(
					classLoader.getResource("icons/"+size+"/"+filename)), size);
		} catch (Exception ignored) {}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#getString(java.lang.String)
	 */
	public String getString(String key) {
		try {
			return strings.getString(key);
		} catch (RuntimeException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#getIcon(java.lang.String, int)
	 */
	public Icon getIcon(String key, int size) {
		try {
			return (Icon)icons.get(key + size);
		} catch (RuntimeException e) {
			return null;
		}		
	}

}
