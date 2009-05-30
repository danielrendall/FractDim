/* 
 * ActionProcessor.java
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
 * An interface for objects that can apply (redo) or undo an {@link Action}.
 * ActionProcessors are usually {@link Data} and {@link View}
 * objects or their subclasses. 
 */
public interface ActionProcessor {
	
	/**
	 * Applies an action or perform a redo.
	 * @param action	the action to be applied or redone
	 */
	public void applyAction(Action action);
	
	/**
	 * Perform an undo.
	 * @param action	the action to be undone
	 */
	public void undoAction(Action action);
	
}
