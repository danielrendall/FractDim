/* 
 * Clipboardmanager.java - manages clipboard items & operations
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

import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;

/**
 * The ClipboardManager provides clipboard access and manages {@link ActionConverter} objects.
 * Usually, an application has only one ClipboardManager, which can be accessed
 * by calling the static method <code>Application.getClipboardManager()</code>.
 */
public class ClipboardManager implements ClipboardOwner {
	
	ArrayList converters;
	Clipboard clipboard;
	
	/**
	 * Creates a new ClipboardManager, which uses the system's default clipboard
	 * for data exchange.
	 * Note that you shouldn't create more than one ClipboardManager.
	 * The Application's default ClipboardManager can be accessed by calling
	 * the static method <code>Application.getClipboardManager()</code>.
	 */
	public ClipboardManager() {
		converters = new ArrayList();
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	/**
	 * Registers an ActionConverter to be used with this ClipboardManager.
	 * @param converter	the converter to be registered
	 */
	public synchronized void registerConverter(ActionConverter converter) {
		if (converter == null) return;
		converters.add(converter);
	}
	
	/**
	 * Registers all converts in the given array.
	 * @param converters	the converts to be registered.
	 */
	public synchronized void registerAllConverters(ActionConverter converters[]) {
		if (converters == null) return;
		for (int i=0; i<converters.length; i++) {
			this.converters.add(converters[i]);
		}
	}
	
	/**
	 * Sets the content of the clipboard.
	 * The given Action object is automatically converted to a
	 * Transferable object.
	 * @param content	the content to be set.
	 */
	public void setContent(Action content) throws ClipboardConversionException {
		if (content == null) return;
		Transferable t = toTransferable(content);
		if (t != null) {
			clipboard.setContents(t, this);
		} else {
			throw new ClipboardConversionException();
		}
	}
	
	/**
	 * Gets the content of the clipboard.
	 * The Transferable object is automatically converted to an Action object.
	 * @return	the clipboard's content in form of an Action object
	 */
	public Action getContent() throws ClipboardConversionException {
		Action action;
		Transferable t = clipboard.getContents(this);
		action = toAction(t);
		if (action != null) {
			return action;
		} else {
			throw new ClipboardConversionException();
		}
	}
	
	/**
	 * Returns true if the clipboard is empty.
	 * @return	true if the clipboard is empty
	 */
	public boolean isClipboardEmpty() {
		return (clipboard.getContents(this) == null);
	}
	
	protected Transferable toTransferable(Action action) {
		CompositeTransferable t = new CompositeTransferable();
		for (int i=0; i<converters.size(); i++) {
			ActionConverter c = (ActionConverter)converters.get(i);
			if (c.canHandle(action)) {
				t.add(c.toTransferable(action));
			}
		}
		if (t.count() == 0) return null; else return t;
	}
	
	protected Action toAction(Transferable transferable) {
		for (int i=0; i<converters.size(); i++) {
			ActionConverter c = (ActionConverter)converters.get(i);
			if (c.canHandle(transferable)) {
				return c.toAction(transferable);
			}
		}
		return null;
	}
	
	/**
	 * Returns true if this ClipboardManager is "ready".
	 * A ClipboardManager is considered ready if at least one {@link ActionConverter} has been registered.
	 * @return	true if at least one ActionConverter has been registered
	 */
	public boolean isReady() {
		return (converters.size() != 0);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard clipboard, Transferable contents) { 
	}
	
	
	private class CompositeTransferable implements Transferable {
		
		ArrayList elements;
		
		public CompositeTransferable() {
			elements = new ArrayList();
		}
		
		public void add(Transferable t) {
			elements.add(t);
		}
		
		public void remove(Transferable t) {
			elements.remove(t);
		}
		
		public int count() {
			return elements.size();
		}
		
		public Object getTransferData(DataFlavor flavor) {
			for (int i=0; i<elements.size(); i++) {
				Transferable t = (Transferable)elements.get(i);
				try {
					if (t.isDataFlavorSupported(flavor)) {
						Object data = t.getTransferData(flavor);
						if (data != null) return data;
					}
				} catch (Exception e) {};
			}
			return null;
		}
		
		public DataFlavor[] getTransferDataFlavors() {
			ArrayList flavors = new ArrayList();
			for (int i=0; i<elements.size(); i++) {
				Transferable t = (Transferable)elements.get(i);
				DataFlavor[] f = t.getTransferDataFlavors();
				for (int j=0; j<f.length; j++) {
					flavors.add(f[j]);
				}
			}
			return (DataFlavor[])flavors.toArray(new DataFlavor[0]);
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			for (int i=0; i<elements.size(); i++) {
				Transferable t = (Transferable)elements.get(i);
				if (t.isDataFlavorSupported(flavor) == true) return true;
			}
			return false;	 
		}
	}
	
}
