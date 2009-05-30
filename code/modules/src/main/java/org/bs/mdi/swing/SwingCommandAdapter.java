/* 
 * SwingCommandAdapter.java - simplifies subclassing from SwingCommand
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

import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.bs.mdi.Application;

/**
 * Adapter for SwingCommand to simplify subclassing
 */
public class SwingCommandAdapter extends SwingCommand {

	/**
	 * Creates a new command.
	 * @param name	the name of the command 
	 * @param description	the command's description
	 */
	public SwingCommandAdapter(String name, String description) {
		setName(name);
		setDescription(description);
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Command#doExecute()
	 */
	protected void doExecute() {
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.SwingCommand#getIcon(int)
	 */
	public Icon getIcon(int size) {
		return Application.getResources().getIcon(name, size);
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.MessageProcessor#processMessage(java.lang.Object, int, java.lang.Object)
	 */
	public void processMessage(Object source, int type, Object argument) {
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.SwingCommand#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}
}
