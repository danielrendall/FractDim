/* 
 * FileExporter.java - File Exporter Interface
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
 * A file I/O module which can export documents.
 * Exporting means writing data to disk which cannot be opened again.
 */
public interface FileExporter extends FileIOModule {

	/**
	 * Exports the data to a non-loadable format.
	 * The exporter has to do all the required user interaction (such as showing
	 * file selection dialogs) on its own.
	 * @param data	the RootData to be exported
	 */
	public void export(RootData data) throws FileIOException;

}

