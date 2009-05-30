/* 
 * MDIWindowManager.java - an interface for window managers
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2004 Bernhard Stiftner
 * License: GNU GPL
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA. 
 */

package org.bs.mdi.swing;

import java.util.*;
import javax.swing.*;
import org.bs.mdi.*;

/**
 * An interface for window managers.
 * A window manager is an object which provides a desktop area and
 * subwindows. It also knows how to create a new subwindow and how
 * to embed in into the desktop area.
 */
public interface MDIWindowManager {
	
	/**
	 * Creates the desktop area.
	 * @return	the widget which will contain the document subwindows
	 */
	public JComponent createDesktopComponent();
	
	/**
	 * Creates a new document subwindow.
	 * Note that the subwindow is not automatically embedded into the
	 * desktop area. This has to be done separately using the
	 * {@link #addWindow(JComponent, DocumentWindow)} method.
	 * @return	the document subwindow
	 */
	public DocumentWindow createDocumentWindow();
	
	/**
	 * Adds the specified window to the given desktop area.
	 * @param desktop	the desktop area
	 * @param window	the document subwindow
	 */
	public void addWindow(JComponent desktop, DocumentWindow window);
	
	/**
	 * Called from within the framework to indicate that a document
	 * subwindow has been closed.
	 * @param desktop	the desktop area
	 * @param window	the document window which has just been closed
	 */
	public void windowRemoved(JComponent desktop, DocumentWindow window);
	
	/**
	 * Called from within the framework to indicate that another document 
	 * window has been selected.
	 * @param window	the selected window
	 */
	public void windowSelected(JComponent desktop, DocumentWindow window);
	
	/**
	 * Gets a list of all document subwindows currently managed by this
	 * windowmanager.
	 * @return	a list of document windows
	 */
	public List getWindows();
	
	/**
	 * Gets a list of special commands provided by this window manager.
	 * These special commands should be made accessible via the GUI by
	 * the MainWindow object.
	 * @return	a list of special commands, or null if there are no special 
	 * commands available. 
	 */
	public SwingCommand[] getSpecialCommands();

}
