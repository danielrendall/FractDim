/* 
 * TraditionalDocumentWindow.java - a document view using Swing's JInternalFrames
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
 * An implementation of a {@link DocumentWindow} using Swing's JInternalFrames.
 */
public class TraditionalDocumentWindow extends JInternalFrame implements SwingDocumentWindow, VetoableChangeListener {

	protected static int transparencyAlpha = 196;
	protected Color desktopColor = UIManager.getColor("Desktop.background");
	protected RootView view;
	protected SwingWindowCommand windowCommand;
	boolean transparencyEffects = true;
	
	/**
	 * Creates a new document window.
	 * Note that the window is initially invisible. Although the window
	 * manager should automatically take care of this window and may make
	 * it visible, it is still not wrong to call <code>setVisible(true)</code>
	 * on the created window.
	 */
	public TraditionalDocumentWindow() {
		super("", true, true, true, true);
		setVisible(false);
		SwingMainWindow mainWindow = (SwingMainWindow) Application
				.getMainWindow();
		windowCommand = new SwingWindowCommand(this);
		addInternalFrameListener(new MyInternalFrameListener());
		addVetoableChangeListener(this);
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
		getContentPane().removeAll();
		getContentPane().add((Component)view);
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
		try {
			setClosed(true);
		} catch (PropertyVetoException e) {
			return false;
		}
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		super.setTitle(title);
		windowCommand.titleChanged(title);
	}
	
	/**
	 * Returns the preferred dimensions of this window.
	 * The window manager may use this value to set the initial size
	 * of this window.
	 * @return	the preferred dimensions, or null to let the window manager
	 * decide
	 */
	public Dimension getPreferredDimension() {
		return null; // let the window manager decide
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#setSelected(boolean)
	 */
	public void setSelected(boolean selected) throws PropertyVetoException {
		if (getDocument() == null) return;
		if (selected && transparencyEffects && getDocument().countViews() > 1) {
			for (int i=0; i<getDocument().countViews(); i++) {
				DocumentWindow w = getDocument().getView(i).getWindow();
				if (w != this) 
					((TraditionalDocumentWindow)w).toFront(false);
			}
		}
		if (selected) 
			toFront(false);
		super.setSelected(selected);
	}
	
	protected void toFront(boolean select) {
		if (select) {
			try { 
				setSelected(true);
			} catch (Exception e) {
			}
		}
		super.toFront();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.DocumentWindow#select()
	 */
	public void toFront() {
		toFront(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if (isSelected() || !transparencyEffects || 
			(Application.getCurrentDocument() == getDocument())) return;		
		g.setColor(
			new Color(
				desktopColor.getRed(),
				desktopColor.getGreen(),
				desktopColor.getBlue(),
				transparencyAlpha));
		g.fillRect(0, 0, getSize().width, getSize().height);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#update(java.awt.Graphics)
	 */
	public void update(Graphics g) {
		paint(g);
	}
	
	/**
	 * Set the transparent window effect on or off.
	 * @param enable	true if the graphical effect should be enabled, false otherwise
	 */
	public void setTransparencyEffects(boolean enable) {
		transparencyEffects = enable;
	}
	
	/**
	 * Gets the alpha value used to draw transparent windows.
	 * @return	the alpha value for transparency effects
	 */
	public static int getTransparency() {
		return transparencyAlpha;
	}
	
	/**
	 * Sets the alpha value used to draw transparent windows.
	 * @param alpha	the alpha value for transparency effects
	 */
	public static void setTransparency(int alpha) {
		transparencyAlpha = alpha;
	}
	
	/**
	 * Gets the command which can be used to activate this window.
	 * This command is usually accessible using the window menu.
	 * @return	the window command
	 */
	public SwingWindowCommand getWindowCommand() {
		return windowCommand;
	}

	/* (non-Javadoc)
	 * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
	 */
	public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
		if (getDocument() == null) return;
		if (pce.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
			boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
			toFront();
			// notify user if this is the last remaining window of a changed document
			if (changed && getDocument().isDirty() && getDocument().countViews() == 1) {
				String buttons[] = { "Yes", "No"};
				int confirm =
					Application.getMainWindow().showDialog(MainWindow.WARNING,
						this,
						Application.tr("Close document?"),
						buttons, 1);
				if (confirm == 1)
					throw new PropertyVetoException("Cancelled", null);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isFocusTraversable()
	 */
	public boolean isFocusTraversable() {
		// FIXME: I don't think that a window itself should be
		// able to get the input focus, but this seems to be
		// necessary to process key events properly.
		// It doesn't seem to break something, it's just not very
		// elegant. Also note that the isFocusTraversable() method
		// is deprecated, but I'm still using it for compatibility
		// with Java versions < 1.4.
		return true;
	}
	
	private TraditionalDocumentWindow getWindowInstance() {
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
