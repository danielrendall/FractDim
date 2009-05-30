/* 
 * FileFormatFilter.java - a FileFormat based filter for use with 
 * the javax.swing.JFileChooser class
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
 
package org.bs.mdi.swing;

import java.io.*;
import org.bs.mdi.*;

/**
 * A {@link FileFormat}-based filter for use with the {@link JFileChooser} class.
 */
class FileFormatFilter
	extends javax.swing.filechooser.FileFilter
	implements java.io.FileFilter {

	FileFormat format;

	/**
	 * Creates a new FileFormatFilter which filters the given file format.
	 * @param format	the file format
	 */
	public FileFormatFilter(FileFormat format) {
		this.format = format;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		String name = f.getName();
		if (f.isDirectory()) {
			return true;
		}
		return format.accept(name);
	}

	/**
	 * Returns the file format.
	 * @return	the file format
	 */
	public FileFormat getFormat() {
		return format;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		String[] extensions = format.getExtensions();
		StringBuffer sb = new StringBuffer(format.getDescription());
		sb.append(" (");
		for (int i=0; i<extensions.length; i++) {
			sb.append(extensions[i]);
			if (i < extensions.length-1) sb.append(", ");
		}
		sb.append(")");
		return sb.toString();
	}

}