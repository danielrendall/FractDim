/* 
 * TabMDIWindowManager.java - Tab Window Manager 
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2004-2005 Bernhard Stiftner
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

import java.awt.*;
import java.util.*;
import java.beans.PropertyVetoException;
import javax.swing.*;

import org.bs.mdi.*;


/**
 * Alternative MDI window manager using tabs
 */
public class TabMDIWindowManager implements MDIWindowManager {

	protected static int numAddedWindows = 0;
	public static final int windowOverlapOffset = 20;
	java.util.List windows = new ArrayList();
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#createDesktopComponent()
	 */
	public JComponent createDesktopComponent() {
		ScrollableDesktopPane desktopPane = new ScrollableDesktopPane();
		JScrollPane scrollPane = new JScrollPane();
	    scrollPane.getViewport().add(desktopPane);
	    return desktopPane;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#createDocumentWindow()
	 */
	public DocumentWindow createDocumentWindow() {
		return new TabDocumentWindow();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#getWindows()
	 */
	public java.util.List getWindows() {
		return windows;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#addDocumentWindow(javax.swing.JComponent, org.bs.mdi.DocumentWindow)
	 */
	public void addWindow(JComponent desktop, DocumentWindow window) {
		JInternalFrame win = (JInternalFrame)window;
		JDesktopPane desk = (JDesktopPane)desktop;
		desk.add(win);		
		
		// Manage window locations
		Dimension dim;
		try {
			dim = ((TraditionalDocumentWindow) win).getPreferredDimension();
		} catch (RuntimeException e1) {
			dim = new Dimension(320, 200);
		}
		
		Point loc = new Point(40, 40);
		loc.x += numAddedWindows * windowOverlapOffset;
		loc.x %= desk.getSize().width - dim.width; 
		loc.y += numAddedWindows * windowOverlapOffset;
		loc.y %= desk.getSize().height - dim.height;
		win.setLocation(loc);
		win.setSize(dim);
		numAddedWindows++;
		
		// Maximize the internal frames by default.
		// This is a more SDI-like look, but I've read that SDI is nowadays
		// preferred over MDI due to usability reasons.
		try { win.setMaximum(true); } catch (PropertyVetoException e) {}
		
		windows.add(win);

	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#removeDocument(javax.swing.JComponent, org.bs.mdi.DocumentWindow)
	 */
	public void windowRemoved(JComponent desktop, DocumentWindow window) {
		windows.remove(window);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#windowSelected(javax.swing.JComponent, org.bs.mdi.DocumentWindow)
	 */
	public void windowSelected(JComponent desktop, DocumentWindow window) {
		// nothing to be done here
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#getSpecialCommands()
	 */
	public SwingCommand[] getSpecialCommands() {
		return null;
	}
	
	public void closeWindow(TabDocumentWindow window) {
		// TODO: close the associated tab!
	}

}
