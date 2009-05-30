/* 
 * FileFormat.java - Information about a file format
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
 * Represents a file format.
 */
public class FileFormat {
 	
 	String[] extensions;
 	String description;
 	
 	/**
 	 * Creates a new file format instance with the given extensions and description.
	 * @param extensions	an array of extensions which this FileFormat should be associated with
	 * @param description	a description of the file format, for example "Text files"
	 */
	public FileFormat(String[] extensions, String description) {
 		this.extensions = extensions;
 		this.description = description;
 	}
 	
 	/**
 	 * Returns true if the filename matches this file format.
	 * @param filename	the filename
	 * @return	true if the filename matches this format, false otherwise
	 */
	public boolean accept(String filename) {
 		if (filename == null) return false;
 		for (int i=0; i<extensions.length; i++) {
 			if (filename.endsWith(extensions[i])) return true;
 		}
 		return false;
 	}
 	
 	/**
 	 * Returns the extensions which this file format is associated with.
	 * @return	the extensions
	 */
	public String[] getExtensions() {
 		return extensions;
 	}
 	
 	/**
 	 * Returns the description of this file format.
	 * @return	the description
	 */
	public String getDescription() {
 		return description;
 	}
 	
 }