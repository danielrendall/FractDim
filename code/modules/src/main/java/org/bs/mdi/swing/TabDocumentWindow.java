/* 
 * TabDocumentWindow.java - a document view using tabs
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

import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import org.bs.mdi.*;

/**
 * An implementation of a {@link DocumentWindow} using Tabs.
 */
public class TabDocumentWindow extends JPanel implements SwingDocumentWindow {

	protected RootView view;
	protected SwingWindowCommand windowCommand;
	
	/**
	 * Creates a new document window.
	 */
	public TabDocumentWindow() {
		super();
		setVisible(false);
		windowCommand = new SwingWindowCommand(this);
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.WINDOW_CREATED, this);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.DocumentWindow#getDocument()
	 */
	public Document getDocument() {
		if (view == null) return null;
		return view.getDocument();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.DocumentWindow#setView(org.bs.mdi.RootView)
	 */
	public void setView(RootView view) {
		if (view == null) {
			this.view.windowChanged(null);
		}
		this.view = view;
		removeAll();
		add((Component)view);
		if (view != null) {
			view.windowChanged(this);
		}
		
		// this is a quick hack to ensure that the enabled status of
		// cut/copy/paste commands is updated when a new window is created.
		// The first DOCUMENT_SELECTED message is dispatched before the view
		// is connected to this window. At this point however, the connection
		// has just been established. The rootView is found and can be asked
		// if cut/copy/paste actions should be enabled or not.
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.DOCUMENT_SELECTED, 
				Application.getCurrentDocument());
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.DocumentWindow#getView()
	 */
	public RootView getView() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Window#close()
	 */
	public boolean close() {
		// notify user if this is the last remaining window of a changed document
		if (getDocument().isDirty() && getDocument().countViews() == 1) {
			toFront();
			String buttons[] = { "Yes", "No"};
			int confirm =
				Application.getMainWindow().showDialog(MainWindow.WARNING,
					this,
					Application.tr("Close document?"),
					buttons, 1);
			if (confirm == 1)
				return false;
		}
		SwingMainWindow win = (SwingMainWindow)Application.getMainWindow();
		TabMDIWindowManager manager = (TabMDIWindowManager)win.getWindowManager();
		manager.closeWindow(this);
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		// TODO: how can we change the text on the tab?
		windowCommand.titleChanged(title);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Window#getTitle()
	 */
	public String getTitle() {
		// TODO: implement!
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#setSelected(boolean)
	 */
	public void setSelected(boolean selected) throws PropertyVetoException {
		// TODO: implement!
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.DocumentWindow#select()
	 */
	public void toFront() {
		// TODO: implement!
	}
	
	/**
	 * Gets the command which can be used to activate this window.
	 * This command is usually accessible using the window menu.
	 * @return	the window command
	 */
	public SwingWindowCommand getWindowCommand() {
		return windowCommand;
	}
	
	private TabDocumentWindow getWindowInstance() {
		return this;
	}

	class MyInternalFrameListener extends InternalFrameAdapter {
		public void internalFrameActivated(InternalFrameEvent e) {
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.WINDOW_SELECTED, getWindowInstance());
		}
		public void internalFrameDeactivated(InternalFrameEvent e) {
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.WINDOW_SELECTED, null);
		}
		public void internalFrameClosed(InternalFrameEvent e) {
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.WINDOW_CLOSED, getWindowInstance());
		}
		public void internalFrameOpened(InternalFrameEvent e) {
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.WINDOW_OPENED, getWindowInstance());
		}
	}
	
}
