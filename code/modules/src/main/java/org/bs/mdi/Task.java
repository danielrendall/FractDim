/* 
 * Task.java - Provides status information of progressing tasks
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
 * A Task is a time-consuming operation which supports returning
 * status information. 
 */
public interface Task {
	
	/**
	 * Should be returned by {@link #getProgress} if the current
	 * status cannot be determined.
	 */
	public static final int PROGRESS_UNAVAILABLE = -1;
	
	/**
	 * Gets the name of this task which may be displayed to the user.
	 * @return this task's name
	 */
	public String getName();
	
	/**
	 * Tells if the task is currently running (active) or not.
	 * @return true if the task is active, false otherwise
	 */
	public boolean isActive();
	
	/**
	 * Gets the minimum progress of this task, i.e. the progress
	 * value when this task hasn't done anything yet.
	 * It is assumed that minimum and maximum progress values are
	 * non-negative and that the minimum value is small than the
	 * maximum value.
	 * @return the task's minimum progress value
	 */
	public int getMinimumProgress();
	
	/**
	 * Gets the maximum progress of this task, i.e. the progress
	 * value when this task has just terminated.
	 * It is assumed that minimum and maximum progress values are
	 * non-negative and that the minimum value is small than the
	 * maximum value.
	 * @return the task's maximum progress value
	 */
	public int getMaximumProgress();
	
	/**
	 * Gets the current progress value of this task.
	 * It is assumed that <code>getMinimumProgress() &lt;= 
	 * getProgress() &lt;= getMaximumProgress() </code>.
	 * If the progress cannot be determined, this method
	 * should return {@link #PROGRESS_UNAVAILABLE}.
	 * @return the current progress value
	 */
	public int getProgress();

}
