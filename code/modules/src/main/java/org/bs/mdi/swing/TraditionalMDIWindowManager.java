/* 
 * TraditionalMDIWindowManager.java - provides MDI Look&Feel using 
 * JDesktopPane and JInternalFrame 
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
 * Traditional MDI window manager using JDesktopPane and JInternalFrames 
 */
public class TraditionalMDIWindowManager implements MDIWindowManager {

	int numAddedWindows = 0;
	final int windowOverlapOffset = 20;
	java.util.List windows = new ArrayList();
	DocumentWindow selectedWindow = null;
	final Dimension defaultWindowSize = new Dimension(400, 300);
	final Point initialWindowPos = new Point(40, 40);
	SwingCommand commands[] = { new WindowTileCmd(), new WindowCascadeCmd() };
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#createDesktopComponent()
	 */
	public JComponent createDesktopComponent() {
		ScrollableDesktopPane desktopPane = new ScrollableDesktopPane();
		DesktopScroller scroller = new DesktopScroller(desktopPane);
	    return scroller;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#createDocumentWindow()
	 */
	public DocumentWindow createDocumentWindow() {
		return new TraditionalDocumentWindow();
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#addDocumentWindow(javax.swing.JComponent, org.bs.mdi.DocumentWindow)
	 */
	public void addWindow(JComponent desktop, DocumentWindow window) {
		JInternalFrame win = (JInternalFrame)window;
		JDesktopPane desk = ((DesktopScroller)desktop).getDesktop();
		desk.add(win);		
		
		// Manage window locations
		Dimension dim = null;
		try {
			dim = ((TraditionalDocumentWindow) win).getPreferredDimension();
		} catch (RuntimeException e) {
		}
		if (dim == null) {
			dim = defaultWindowSize;
		}
		
		Point loc = new Point(initialWindowPos.x, initialWindowPos.y);
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
		
		windows.add(window);

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
		if (selectedWindow != null && selectedWindow.getDocument() != null) {
			Document doc = selectedWindow.getDocument();
			for (int i=0; i<doc.countViews(); i++) {
				doc.getView(i).getWindow().repaint();
			}
		}
		selectedWindow = window;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#getWindows()
	 */
	public java.util.List getWindows() {
		return windows;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.MDIWindowManager#getSpecialCommands()
	 */
	public SwingCommand[] getSpecialCommands() {
		return commands;
	}
	
	class WindowTileCmd extends SwingCommandAdapter {
		int windowCount = 0;

		public WindowTileCmd() {
			super("Tile", "Aligns windows one next to each other");
		}

		protected void doExecute() {
			SwingMainWindow swm = (SwingMainWindow)Application.getMainWindow();
			ScrollableDesktopPane sdp = ((DesktopScroller)swm.getDesktop()).getDesktop();
			sdp.tileFrames();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.WINDOW_OPENED:
				windowCount++;
				break;
			case MessageDispatcher.WINDOW_CLOSED:
				windowCount--;
				break;
			}
			setAvailable(windowCount != 0);
		}
	}
	
	class WindowCascadeCmd extends SwingCommandAdapter {
		int windowCount = 0;

		public WindowCascadeCmd() {
			super("Cascade", "Aligns windows so that the topmost window overlaps all other windows");
		}

		protected void doExecute() {
			SwingMainWindow swm = (SwingMainWindow)Application.getMainWindow();
			ScrollableDesktopPane sdp = ((DesktopScroller)swm.getDesktop()).getDesktop();
			sdp.cascadeFrames();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.WINDOW_OPENED:
				windowCount++;
				break;
			case MessageDispatcher.WINDOW_CLOSED:
				windowCount--;
				break;
			}
			setAvailable(windowCount != 0);
		}
	}
	
	class DesktopScroller extends JScrollPane {
		ScrollableDesktopPane desktop;
		
		public DesktopScroller(ScrollableDesktopPane desktop) {
			super(desktop);
			this.desktop = desktop;
		}
		
		public ScrollableDesktopPane getDesktop() {
			return desktop;
		}
	}

}
