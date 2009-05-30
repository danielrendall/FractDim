/* 
 * FileIOException - Exceptions occurring during loading / saving / exporting
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

import java.text.*;

/**
 * FileIOExceptions can happen during loading, saving or exporting files.
 */
public class FileIOException extends Exception {

	public static final int ERR_UNSUPPORTEDFORMAT = 1;
	public static final int ERR_NOACCESS = 2;
	public static final int ERR_NOSUCHFILE = 3;
	public static final int ERR_UNKNOWN = 4;
		
	static final String messageIDs[] = {"", 
			"Unsupported format", 
			"Access denied",
			"No such file or directory", 
			"Unknown failure"};
				
	int reason;
	String filename;
		
	/**
	 * Creates a new FileException.
	 * @param reason	the reason for the failure
	 * @param filename	the filename which was attempted to be loaded, saved or exported
	 */
	public FileIOException(int reason, String filename) {			
		this.reason = reason;
		this.filename = filename;
	}
		
	/**
	 * Returns the filename which this exception concerns.
	 * @return	the filename
	 */
	public String getFilename() { return filename; }
		
	/**
	 * Returns the reason why this exception was thrown.
	 * @return	the reason
	 */
	public int getReason() { return reason; }
		
	/**
	 * Returns a localized error message.
	 * @return	the localized error message
	 */
	public String getLocalizedMessage() {
		Object[] args = {filename};
		return MessageFormat.format(Application.tr(messageIDs[reason]), args);
	}
		
}