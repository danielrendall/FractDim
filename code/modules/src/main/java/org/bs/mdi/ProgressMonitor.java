/* 
 * ProgressMonitor.java - A means of indicating progress to the user
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
 * Gives feedback about currently running tasks.
 */
public interface ProgressMonitor {
	
	/**
	 * Registers the given Task.
	 * After this method got called, the given Task
	 * is periodically queried for its progress. This progress
	 * is displayed to the user.
	 * @param task
	 */
	public void add(Task task);
	
	/**
	 * Unregisters the given Task.
	 * The task is no longer queried for its progress after this
	 * method got called.
	 * @param task
	 */
	public void remove(Task task);

}
