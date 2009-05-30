/* 
 * Resources.java - Interface for localized application resources
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
 
/**
 * Manages program resources such as localized strings or icons.
 */
public abstract class Resources {
	
	public static final int CUSTOM_ICON = 0;
	public static final int MENU_ICON = 16;
	public static final int BUTTON_ICON = 16;
	public static final int TOOLBAR_ICON = 22;
	public static final int MESSAGEBOX_ICON = 32; 
	
	Locale locale;
	
	/**
	 * Resources is an abstract class and therefore cannot be instantiated.
	 * The default implementation sets the system-default locale and calls
	 * {@link #loadResources}.
	 */
	public Resources() {
		setLocale(Locale.getDefault());
	}
	
	/**
	 * Sets the locale used for getting translated/localized resources.
	 * @param locale	the locale to be used
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
		loadResources();
	}
	
	/**
	 * Returns the currently used locale.
	 * @return	the current locale
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * Re-loads the resources from storage. This method should take
	 * the currently selected locale (see {@link #getLocale}) into account.
	 */
	public abstract void loadResources();
	
	/**
	 * Returns a localized string for the given key.
	 * @param key	the key
	 * @return	the localized string, or null in case of error
	 */
	public abstract String getString(String key);
	
	/**
	 * Returns an icon for the specified key and in the given size
	 * @param key	the key
	 * @param size	the size of the icon: pre-defined values are: 
	 * 	<code>CUSTOM_ICON</code>, <code>MENU_ICON</code>, 
	 * 	<code>BUTTON_ICON</code>, <code>TOOLBAR_ICON</code>, 
	 * 	<code>MESSAGEBOX_ICON</code>.
	 * @return	the icon, or null in case of error
	 */
	public abstract javax.swing.Icon getIcon(String key, int size);
	
	/**
	 * Returns a localized string for the given key, stripping ampersand
	 * characters which are meant for marking mnemonics.
	 * For a detailed description of the mnemonic feature, see
	 * {@link #getMnemonic}. 
	 * @param key	the key
	 * @return	the localized string, or key in case of error
	 */
	public String i18n(String key) {		
		int prefixLen = 0;		
		int suffixLen = 0;
		String out;
		
		
		
		// Try to find resource for whole key
		out = getString(key);
		if (out != null) {
			return stripMnemonic(out);
		}		
		
		// Trim key and try again
		while (prefixLen < key.length() && !Character.isLetterOrDigit(key.charAt(prefixLen))) {
			prefixLen++;
		}
		while (suffixLen < (key.length() - prefixLen) && !Character.isLetterOrDigit(key.charAt(key.length() - suffixLen - 1))) {
			suffixLen++;
		}
		out = getString(key.substring(prefixLen, key.length()-suffixLen));
		if (out == null) return key;
		out = stripMnemonic(out);
		return key.substring(0, prefixLen) + out + key.substring(key.length()-suffixLen, key.length());
	}
	
	
	protected String stripMnemonic(String s) {
		if (s == null) return null;
		int i = s.indexOf("&");
		while(i > -1) {
			s = s.substring(0,i)+s.substring(i+1,s.length());
			if (s.charAt(i+1) == '&') i++;
			i = s.indexOf("&",i);
		}
		return s;
	}

	/**
	 * Returns the Mnemonic for the given key.
	 * A mnemonic is a key shortcut for accessing GUI elements such as
	 * menu items, buttons, or labels.<p>
	 * Example: For accessing the file menu, the keystroke "F" may be used 
	 * as the mnemonic of this menu. The letter "F" then usually appears
	 * underligned, and the user can press a key combination such as
	 * STRG-F or ALT-F for activating the menu.<p>
	 * Using the jmdiframework resource system, mnemonics are usually
	 * marked using the prefix "&amp;" (ampersand). In the above example, 
	 * the file menu label would be saved in the resource files as "&amp;File".<p>
	 * This method first looks for a translation of the given key using the
	 * {@link #getString} method, then scans the result for the appearance 
	 * of an ampersand character, and returns the following character as the 
	 * mnemonic.
	 * @param key	the key
	 * @return	the mnemonic, or 0 if no mnemonic is set
	 */
	public int getMnemonic(String key) {
		int prefixLen = 0;
		int suffixLen = 0;
		String out;
		out = getString(key);
		if (out == null) {
			while (prefixLen < key.length() && !Character.isLetterOrDigit(key.charAt(prefixLen))) {
				prefixLen++;
			}
			while (suffixLen < (key.length() - prefixLen) && !Character.isLetterOrDigit(key.charAt(key.length() - suffixLen - 1))) {
				suffixLen++;
			}
			out = getString(key.substring(prefixLen, key.length()-suffixLen));
			if (out == null) return 0;
		}		
		int index = out.indexOf("&");
		if(index >= 0 && out.length() > index)
			return out.charAt(index+1);
		return 0;
	}
	
}
