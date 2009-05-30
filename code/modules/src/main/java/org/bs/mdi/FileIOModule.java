/* 
 * FileIOModule.java - abstract data exchange module
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
 * An interface for modules performing file I/O.
 */
public interface FileIOModule {

	/**
	 * Returns a list of file formats which are supported by this module.
	 * @return	the file format list
	 */
	public FileFormat[] getSupportedFormats();

	/**
	 * Returns true if the given filename can be used for I/O by this module.
	 * @param filename	the filename
	 * @return	true if this module can perform I/O in this filename, false otherwise
	 */
	public boolean canHandle(String filename);
	
	/**
	 * Returns a textual description of this file I/O module.
	 * This can be, for example, "HTML file exporter".
	 * @return	the module description
	 */
	public String getDescription();

}

