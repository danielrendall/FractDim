/* 
 * SwingCommand.java - Command with icon support
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
import java.util.*;
import javax.swing.*;
import org.bs.mdi.*;

/**
 * SwingCommand is basically a Command with icon support.
 */
public abstract class SwingCommand extends Command {
	
	String name;
	String description;
	
	/**
	 * Creates a new SwingCommand.
	 */
	public SwingCommand() {
		super();
		setAvailable(false);
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Command#doExecute()
	 */
	protected abstract void doExecute();

	/* (non-Javadoc)
	 * @see org.bs.mdi.MessageProcessor#processMessage(org.bs.mdi.Message)
	 */
	public abstract void processMessage(Object source, int type, Object argument);

	/* (non-Javadoc)
	 * @see org.bs.mdi.Command#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Command#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the name of this command and updates all associated triggers.
	 * @param name	the new name of this command
	 */
	public void setName(String name) {
		this.name = name;
		updateTriggers();
	}
	
	/**
	 * Sets the description of this command and updates all associated triggers.
	 * @param description	the new description of this command
	 */
	public void setDescription(String description) {
		this.description = description;
		updateTriggers();
	}
	
	/**
	 * Gets an icon representing this command.
	 * @param size	the requested size of the icon
	 * @return	the icon, or null if no icon is available
	 */
	public abstract Icon getIcon(int size);
	
	/**
	 * Gets the "accelerator" for this action.
	 * This is the key combination which is supposed to trigger the event.
	 * @return	the accelerator
	 */
	public abstract KeyStroke getAccelerator();
	
	/**
	 * Removes all triggers of this command from the specified container.
	 * This is the preferred way of doing thing like removing commands
	 * from menus ans the like.
	 * @param container	the container which is supposed to contain menu items
	 * associated with this command
	 */
	public void removeFrom(Container container) {
		Iterator iter = getTriggers().iterator();
		while (iter.hasNext()) {
			try {
				SwingCommandTrigger sct = (SwingCommandTrigger)iter.next();
				if (sct.removeFrom(container))
					iter.remove();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	}

}
