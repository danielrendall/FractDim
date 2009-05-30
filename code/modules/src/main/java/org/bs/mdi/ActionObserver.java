/* 
 * ActionObserver.java
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
 * An interface for objects that can observe an {@link ActionObservable}.
 */
public interface ActionObserver {
	
	/**
	 * Called from the {@link ActionObservable} when an action has been performed or redone.
	 * @param source	the observable which this notification originated from
	 * @param action	the action which has been performed
	 */
	public void actionPerformed(ActionObservable source, Action action);
	
	/**
	 * Called from the {@link ActionObservable} when an action has been undone.
	 * @param source	the observable which this notification originated from
	 * @param action	the action which has been undone
	 */
	public void actionUndone(ActionObservable source, Action action);
	
}
