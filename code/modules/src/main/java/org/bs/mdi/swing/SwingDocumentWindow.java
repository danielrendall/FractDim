/* 
 * SwingDocumentWindow.java - superclass for all swing document
 * windows
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

import org.bs.mdi.*;

/**
 * Interface for all document window implementations in org.bs.mdi.swing.
 */
public interface SwingDocumentWindow extends DocumentWindow {
	
	/**
	 * Gets the command which can be used to activate this window.
	 * This command is usually accessible using the window menu.
	 * @return	the window command
	 */
	public SwingWindowCommand getWindowCommand();

}
