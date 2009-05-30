/* 
 * MessageProcessor.java - Interface message receivers
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2004-2005 Bernhard Stiftner
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
 * An interface for objects which can receive simple messages.
 */
public interface MessageProcessor {
	
	/**
	 * Called from the {@link MessageDispatcher} to indicate that an event
	 * has occurred which this object may be interested in.
	 * @param source	the source where this message originated from
	 * @param type	the message type
	 * @param argument	an optional argument
	 */
	public void processMessage(Object source, int type, Object argument);

}
