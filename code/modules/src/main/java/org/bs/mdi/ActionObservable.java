/* 
 * ActionObservable.java
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
 * An interface for objects that can be observed by an {@link ActionObserver}.
 */
public interface ActionObservable {
	
	/**
	 * Registers this observer so that it will receive notification messages.
	 * @param observer	the observer to be registered
	 */
	public void addObserver(ActionObserver observer);
	
	/**
	 * Un-registers this observer so that it will not receive notifications any longer.
	 * @param observer	the observer to be un-registered
	 */
	public void removeObserver(ActionObserver observer);
	
	/**
	 * Counts all registered observers.
	 * @return	the number of registered observers.
	 */
	public int countObservers();
	
	/**
	 * Determines if the given observer is currently observing this object.
	 * @param observer	the observer
	 * @return	true if the observer is currently observing this object, false otherwise
	 */
	public boolean isObserver(ActionObserver observer);
	
	/**
	 * Notifies the observers about an action which has been recently performed.
	 * Note that the implementation may use threading to deliver the notification
	 * messages.
	 * @param action	the action which has been performed
	 * @param undo	true if the action has been undone, false otherwise
	 */
	public void notifyObservers(Action action, boolean undo);

}
