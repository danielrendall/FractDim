/* 
 * RootView.java - document GUI window interface
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

package org.bs.mdi;

/**
 * An interface for the graphical representation of a document's information.
 */
public interface RootView extends View {
	
	/**
	 * Returns the window which is displaying this view.
	 * @return	the window
	 */
	public DocumentWindow getWindow();
	
	/**
	 * Called from within the framework to indicate that the view's window has
	 * changed. This method gives the view the opportunity to react upon
	 * this event.
	 * @param window	the new window
	 */
	public void windowChanged(DocumentWindow window);
	
}

