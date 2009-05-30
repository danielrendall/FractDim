/* 
 * SwingCommandTrigger.java - enhanced CommandTrigger
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

import java.awt.*;
import org.bs.mdi.*;

/**
 * Enhanced CommandTrigger with additional GUI functionality
 */
public abstract class SwingCommandTrigger implements CommandTrigger {
	
	SwingCommand command;
	
	/**
	 * Creates a new command trigger.
	 * Note that this is an abstract class which cannot be instantiated
	 * directly.
	 * @param command	the command to be associated with the trigger
	 */
	public SwingCommandTrigger(SwingCommand command) {
		this.command = command;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#getCommand()
	 */
	public Command getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#setEnabled(boolean)
	 */
	public abstract void setEnabled(boolean enabled);

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#execute()
	 */
	public void execute() {
		command.execute(this);
	}
	
	/**
	 * Removes this trigger from the sepecified container.
	 * @param container	the container which is supposed to contain this trigger
	 * @return	true if the trigger has been removed, false otherwise
	 */
	public abstract boolean removeFrom(Container container);

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#commandExecuted(org.bs.mdi.CommandTrigger)
	 */
	public void commandExecuted(CommandTrigger trigger) {
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#commandUpdated()
	 */
	public void commandUpdated() {
	}

}
