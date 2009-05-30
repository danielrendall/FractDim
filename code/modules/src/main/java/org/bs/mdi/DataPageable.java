/* 
 * DataPageable.java - Simplifies access to Java's Pageable interface
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

import java.awt.*;
import java.awt.print.*;

/**
 * A {@link Pageable} implementation receives its printing data from
 * a {@link RootData} object.
 */
public class DataPageable implements Pageable {
	
	protected RootData data;
	protected PageFormat format = PrinterJob.getPrinterJob().defaultPage();
	
	/**
	 * Creates a new DataPageable with no RootData associated with it.
	 */
	public DataPageable() {
	}
	
	/**
	 * Returns the RootData which is associated with this DataPageable.
	 * @return	the data
	 */
	public RootData getData() {
		return data;
	}
	
	/**
	 * Associates the given RootData with this DataPageable.
	 * The printing data will be provided by the RootData's {@link Printer}
	 * (see also: {@link RootData#getPrinter}). 
	 * @param data	the data
	 * @throws PrintException
	 */
	public void setData(RootData data) throws PrintException {
		if (data.getPrinter() == null) {
			throw new PrintException("This RootData object doesn't support printing!");
		}
		this.data = data;
	}
	
	/**
	 * Returns the page format.
	 * @return	the page format.
	 */
	public PageFormat getPageFormat() {
		return format;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.print.Pageable#getPageFormat(int)
	 */
	public PageFormat getPageFormat(int pageIndex) {
		return format;
	}
	
	/**
	 * Sets the page format used for the print job.
	 * @param format	the format
	 */
	public void setPageFormat(PageFormat format) {
		this.format = format;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.print.Pageable#getNumberOfPages()
	 */
	public int getNumberOfPages() {
		return data.getPrinter().getNumPages(format);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.print.Pageable#getPrintable(int)
	 */
	public Printable getPrintable(int pageIndex) {
		return new DataPrintable(data, format, pageIndex);
	}
	
	
	public class DataPrintable implements Printable {		
		RootData data;
		PageFormat format;
		int page;
		
		public DataPrintable(RootData data, PageFormat format, int page) {
			this.data = data;
			this.format = format;
			this.page = page;
		}
		
		public int print(Graphics g, PageFormat pageFormat, int pageIndex) {			
			boolean b = data.getPrinter().print(g, format, page);
			return (b ? Printable.PAGE_EXISTS : Printable.NO_SUCH_PAGE);
		}
	}
	
	public class PrintException extends Exception {
		public PrintException(String message) {
			super(message);
		}
	}

}
