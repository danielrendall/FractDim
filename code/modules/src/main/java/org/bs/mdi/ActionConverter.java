/* 
 * ActionConverter.java - converts Actions into Transferables for clipboard data exchange
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

import java.awt.datatransfer.*;

/**
 * Provides an interface for mechanisms that convert {@link Action} to
 * {@link Transferable} objects.
 * This conversion is needed in order for the clipboard
 * data transfer functionality to work.
 */
public interface ActionConverter {
	
	/**
	 * Indicates that this ActionConverter can convert the given {@link Action}.
	 * @param action	the Action which could be converted later by this ActionConverter
	 * @return	true if the Action can be converted, false otherwise
	 */
	public boolean canHandle(Action action);
	
	/**
	 * Indicates that this ActionConverter can convert the given {@link Transferable}.
	 * @param transferable	the Transferable which could be converted by this ActionConverter
	 * @return	true if the Transferable can be converted, false otherwise
	 */
	public boolean canHandle(Transferable transferable);

	/**
	 * Converts an {@link Action} to a {@link Transferable}.
	 * @param action	the Action to be converted
	 * @return	the Transferable object created by the conversion process, or null in case of an error
	 */
	public Transferable toTransferable(Action action);
	
	/**
	 * Converts a {@link Transferable} to an {@link Action}.
	 * @param transferable	the Transferable to be converted
	 * @return	the Action object created by the conversion process, or null in case of an error
	 */
	public Action toAction(Transferable transferable);

}
