/* 
 * CommandTrigger.java - Interface for GUI elements which trigger commands
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

package org.bs.mdi;

/**
 * Interface for GUI elements which trigger commands.
 */
public interface CommandTrigger {
	
	/**
	 * Gets the command which is associated with this command trigger.
	 * @return	the associated command
	 */
	public Command getCommand();
	
	/**
	 * Enables or disables this command trigger.
	 * @param enabled	true if this trigger should be enabled, false otherwise
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * Shortcut for <code>getCommand().execute()</code>.
	 */
	public void execute();
	
	/**
	 * Informs this trigger that the associated command has been executed.
	 * Note that the command may have been fired by either this trigger or another
	 * trigger which has been associated with the command.
	 * @param trigger	the trigger which made the command execute
	 */
	public void commandExecuted(CommandTrigger trigger);
	
	/**
	 * Informs this trigger that the associated command has changed.
	 * This typically includes name or description changes.
	 * The trigger should update itself to reflect that changes.
	 */
	public void commandUpdated();

}
