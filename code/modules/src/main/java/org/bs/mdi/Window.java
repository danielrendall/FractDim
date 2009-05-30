/* 
 * Window.java - general window interface
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
 * An interface for all objects implementing windowing funcationality.
 */
public interface Window {
	
	/**
	 * Sets the title of the window.
	 * @param title	the new title
	 */
	public void setTitle(String title);
	
	/**
	 * Returns the title of the window.
	 * @return	the window title
	 */
	public String getTitle();
	
	/**
	 * Makes this window visible.
	 */
	public void show();
	
	/**
	 * Makes this window invisible.
	 */
	public void hide();
	
	/**
	 * Tries to close the window.<p>
	 * Note that calling this method does not guarantee that the window
	 * is actually closed. The implementations of this interface can intercept
	 * close requests and decide if they are really carried out or not.
	 * For example, if you try to close a changed document,  
	 * @return	true if the window was successfully closed, false otherwise
	 */
	public boolean close();
	
	/**
	 * Requests this window to be repainted as soon as possible.
	 */
	public void repaint();

}
