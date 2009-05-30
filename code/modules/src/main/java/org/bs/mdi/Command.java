/* 
 * Command.java - describes commands accessible via the GUI
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

import java.util.*;

/**
 * Base class for all commands which can be triggered by users
 */
public abstract class Command implements MessageProcessor {
	
	private List triggers = new LinkedList();
	private boolean available = false;
	
	
	/**
	 * Creates a new command.
	 */
	protected Command() {
		Application.getMessageDispatcher().registerProcessor(this);
	}
	
	/**
	 * Executes this command and notifies all CommandTriggerGroups.
	 */
	public void execute(CommandTrigger source) {
		doExecute();
		Iterator iter = triggers.iterator();
		while (iter.hasNext()) {
			CommandTrigger ct = (CommandTrigger)iter.next();
			ct.commandExecuted(source);
		}
	}
	
	/**
	 * Executes this command.
	 */
	protected abstract void doExecute();
	
	/**
	 * Sets the available status of this command.
	 * Available means that this command can be executed and that it makes
	 * sense to execute it.
	 * @param available	true if this command should be available, false otherwise
	 */
	public void setAvailable(boolean available) {
		if (this.available == available) return;
		this.available = available;
		Iterator iter = triggers.iterator();
		while (iter.hasNext()) {
			((CommandTrigger)iter.next()).setEnabled(available);
		}
	}
	
	/**
	 * Indicates whether this command is available or not.
	 * Available means that this command can be executed and that it makes
	 * sense to execute it.
	 * @return true if the command is available, false otherwise
	 */
	public boolean isAvailable() {
		return available;
	}
	
	/**
	 * Processes messages.
	 * Using these messages, this method should try to determine if the command
	 * should be available or not and set the available status using
	 * {@link #setAvailable(boolean)}.
	 * See: {@link MessageProcessor#processMessage(Object, int, Object)}
	 */
	public abstract void processMessage(Object source, int type, Object argument);
	
	/**
	 * Gets the name of this command, e.g. "Undo".
	 * @return	the command's name
	 */
	public abstract String getName();
	
	/**
	 * Gets the localized name of this command.
	 * @return	the command's localized name
	 */
	public String getLocalizedName() {
		return Application.tr(getName());
	}
	
	/**
	 * Gets the description of this command, e.g. "undoes the last modification".
	 * @return	the command's description
	 */
	public abstract String getDescription();
	
	/**
	 * Gets the localized description of this command.
	 * @return	the command's localized description
	 */
	public String getLocalizedDescription() {
		return Application.tr(getDescription());
	}
	
	/**
	 * Associates the given command trigger with this command.
	 * The command trigger will be notified when the available status of this command
	 * changes.
	 * @param trigger	the command trigger
	 */
	public void addTrigger(CommandTrigger trigger) {
		triggers.add(trigger);
	}
	
	/**
	 * Removes the association between the given command trigger and this command.
	 * @param trigger	the command trigger
	 */
	public void removeTrigger(CommandTrigger trigger) {
		triggers.remove(trigger);
	}
	
	/**
	 * Removes all associations between command triggers and this command.
	 */
	public void removeAllTriggers() {
		triggers.clear();
	}
	
	/**
	 * Counts the number of associated command triggers.
	 * @return	the number of command triggers
	 */
	public int countTriggers() {
		return triggers.size();
	}
	
	/**
	 * Gets a list of all triggers associated with this command.
	 * @return	the list of triggers
	 */
	protected List getTriggers() {
		return triggers;
	}
	
	/**
	 * Calls {@link CommandTrigger#commandUpdated()} on every associated trigger.
	 */
	public void updateTriggers() {
		Iterator iter = triggers.iterator();
		while (iter.hasNext()) {
			((CommandTrigger)iter.next()).commandUpdated();
		}
	}

}
