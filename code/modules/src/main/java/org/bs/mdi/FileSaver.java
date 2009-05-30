/* 
 * FileLoader.java - File Loader Interface
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
 * A file I/O module which can save data to disk.
 */
public interface FileSaver extends FileIOModule {

	/**
	 * Saves the document data to disk.
	 * @param data	the data
	 * @param filename	the filename to be written to
	 * @throws FileIOException
	 */
	public void save (RootData data, String filename) throws FileIOException;

}


