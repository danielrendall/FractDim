/* 
 * ResourceManager.java - Manages multiple resources
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2005 Bernhard Stiftner
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

/**
 * Manages multiple {@link Resources}.
 */
public class CompositeResources extends Resources {
	
	LinkedList resources = new LinkedList();
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
		if (resources == null) return;
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			((Resources)iter.next()).setLocale(locale);
		}		
	}
	
	/**
	 * Adds/Registers the specified resources.
	 * @param res	the resources object to be added
	 */
	public void addResource(Resources res) {
		if (res == null) return;
		res.setLocale(locale);
		resources.addFirst(res);
	}
	
	/**
	 * Removes the specified resources.
	 * @param res	the resources object to be removed
	 */
	public void removeResource(Resources res) {
		if (res == null) return;
		resources.remove(res);
	}
	
	/**
	 * Counts the registered resources.
	 * @return	the number of registered resources.
	 */
	public int countResources() {
		return resources.size();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#loadResources()
	 */
	public void loadResources() {
		if (resources == null) return;
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			((Resources)iter.next()).loadResources();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#getString(java.lang.String)
	 */
	public String getString(String key) {
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			Resources res = (Resources)iter.next();
			String s = res.getString(key);
			if (s != null) return s;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#getIcon(java.lang.String, int)
	 */
	public javax.swing.Icon getIcon(String key, int size) {
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			Resources res = (Resources)iter.next();
			javax.swing.Icon icon = res.getIcon(key, size);
			if (icon != null) return icon;
		}
		return null;
	}

}
