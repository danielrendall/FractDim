/* 
 * DocumentWindow.java - MDI subwindow for a document
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
 
package org.bs.mdi;

/**
 * An interface for MDI subwindows which display the document's view to the user.
 */
public interface DocumentWindow extends Window{
	
	/**
	 * Brings this window to the front and tries to make it the selected
	 * (=active) window.
	 */
	public void toFront();

	/**
	 * Sets the enabled status of this window.
	 * If a window is not enabled, it cannot process user input.
	 * @param enabled	the new enabled status
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * Sets the visible status of this view.
	 * @param visible	true if this window should be visible, false otherwise
	 */
	public void setVisible(boolean visible);

	/**
	 * Try to close the window.
	 * This function is also responsible for displaying warning messages asking
	 * the user if he really wants to close this window and lose the associated data.
	 * This means that this method does not necessarily have to succeed; this is
	 * the case if the user changes his mind and chooses not to close the window.  
	 * @return true if the window was successfully closed, false otherwise
	 */
	public boolean close();
	
	/**
	 * Returns the document which this view is associated with.
	 * This is usually just a convenience method for calling
	 * <code>getView().getDocument</code>.
	 * @return	the document
	 */
	public Document getDocument();
	
	/**
	 * Returns the window which this view is assigned to.
	 * @return	the window
	 */
	public RootView getView();
	
	/**
	 * Assigns the given view to this window.
	 * The view will be displayed in this window.
	 * @param view	the view
	 */
	public void setView(RootView view);

}
