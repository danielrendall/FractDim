/* 
 * Printer.java - printer abstraction interface
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

import java.awt.print.*;

/**
 * An interface for objects providing printing functionality.
 */
public interface Printer {
	
	/**
	 * Returns the number of pages which would be printed using the
	 * specified page format.
	 * @param format	the page format
	 * @return	the number of printed pages
	 */
	public int getNumPages(PageFormat format);
	
	/**
	 * Draws the specified page on the printer's graphics context.
	 * @param g	the printer's graphics context
	 * @param format	the page format to be used
	 * @param pageindex	the page number to be printed
	 * @return	true if everything went well, false otherwise. If a non-existing page
	 * was requested, false is returned.
	 */
	public boolean print(java.awt.Graphics g, PageFormat format, int pageindex);

}
