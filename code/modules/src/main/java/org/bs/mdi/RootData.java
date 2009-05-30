/* 
 * RootData.java - representation of the document's content
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
 * Represents the information contained in a document.
 */
public abstract class RootData extends Data {
	
	/**
	 * Gets the Printer interface for this document.
	 * If this method returns a working implementation of the <code>Printer</code>
	 * interface, printing functionality is enabled and the user may perform
	 * operations such as showing a print preview or starting a print job. 
	 * @return	the printer interface, or null if this document cannot be printed
	 */
	public abstract Printer getPrinter();
	
}
