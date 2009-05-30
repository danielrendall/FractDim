/* 
 * SwingWindowCommand.java - The command for activating a window
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2005 Bernhard Stiftner
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

import javax.swing.*;
import org.bs.mdi.*;

/**
 * Command subclass which activates a particular {@link TraditionalDocumentWindow}
 */
public class SwingWindowCommand extends SwingCommandAdapter {
	
	DocumentWindow window;
	
	/**
	 * Creates a new SwingWindowCommand which can activate the given window.
	 * @param window	the window to be activated by this command
	 */
	public SwingWindowCommand(DocumentWindow window) {
		super("", "Activate this window");
		this.window = window;
		setAvailable(true);
	}
	
	/**
	 * Invoked from within the framework to indicate that the document's name
	 * has changed. 
	 * @param newTitle	the new title to be displayed
	 */
	public void titleChanged(String newTitle) {
		setName(newTitle);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Command#doExecute()
	 */
	protected void doExecute() {
		window.toFront();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.SwingCommand#getIcon(int)
	 */
	public Icon getIcon(int size) {
		return Application.getResources().getIcon("Window", size);
	}

}
