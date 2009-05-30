/* 
 * Preferences.java - Manages the user's individual settings and preferences
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
import java.io.*;
import java.util.*;

/**
 * Manages application-specific per-user preferences and settings. 
 */
public class Preferences {
	
	Hashtable preferences;
	String filename;
	protected static String newline = System.getProperty("line.separator");

	/**
	 * Creates a new Preferences object. Uses the application's name
	 * (see {@link Application#getName}) as filename to store and retrieve
	 * user settings. The preferences file is usually created in the user's home
	 * directory and prefixed with a dot character to make it invisible on
	 * UNIX machines.
	 */
	public Preferences() {
		String userDir = System.getProperty("user.home");
		File f = new File(userDir, "." + Application.getInstance().getName() + ".properties");
		preferences = new Hashtable();
		filename = f.getPath();
		load();
	}
	
	/**
	 * Saves the preferences to disk.
	 */
	public void save() {
		try {
			File f = new File(filename);
			FileOutputStream fos = new FileOutputStream(f);
			Enumeration elements = preferences.keys();
			while (elements.hasMoreElements()) {
				String key = (String)elements.nextElement();
				String value = toString(preferences.get(key));
				
				// Aaargh! Backslashes in Windoze filenames can screw everything up!
				// Windoze should really be forbidden...
				StringWriter sw = new StringWriter();
				for (int i=0; i<value.length(); i++) {
					char c = value.charAt(i);
					if (c == '\\') sw.write(c);
					sw.write(c);
				}
				value = sw.toString();
				
				fos.write((key + "=" + value + newline).getBytes());
			}		
			fos.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	/**
	 * Loads the preferences from disk.
	 */
	public void load() {		
		preferences.clear();
		File f = new File(filename);
		if (!f.exists()) return;
		try {
			FileInputStream fis = new FileInputStream(f);
			PropertyResourceBundle prop = new PropertyResourceBundle(fis);
			Enumeration keys = prop.getKeys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				Object value = toObject(prop.getString(key));
				preferences.put(key, value);
			}		
			fis.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * Reloads the preferences from disk
	 */
	public void reload() {
		save();
		load();
	}
		
	/**
	 * Returns the string specified by the given key from the preferences file.
	 * @param key	the key
	 * @return	the string or null in case of error
	 */
	public String getString(String key) {
		return (String)preferences.get(key);
	}
	
	/**
	 * Returns the int value specified by the given key from the preferences file.
	 * @param key	the key
	 * @param def	the default value which will be returned in case of error
	 * @return	the int value
	 */
	public int getInt(String key, int def) {
		Integer i = (Integer)preferences.get(key);
		return (i != null) ? i.intValue() : def;
	}
	
	/**
	 * Returns the long value specified by the given key from the preferences file.
	 * @param key	the key
	 * @param def	the default value which will be returned in case of error
	 * @return	the long value
	 */	
	public long getLong(String key, long def) {
		Long l = (Long)preferences.get(key);
		return (l != null) ? l.longValue() : def;
	}
	
	/**
	 * Returns the double value specified by the given key from the preferences file.
	 * @param key	the key
	 * @param def	the default value which will be returned in case of error
	 * @return	the double value
	 */	
	public double getDouble(String key, double def) {
		Double d = (Double)preferences.get(key);
		return (d != null) ? d.doubleValue() : def;
	}
	
	/**
	 * Returns the point specified by the given key from the preferences file.
	 * @param key	the key
	 * @return	the point, or null in case of error
	 */	
	public Point getPoint(String key) {
		return (Point)preferences.get(key);
	}

	/**
	 * Returns the rectangle specified by the given key from the preferences file.
	 * @param key	the key
	 * @return	the rectangle, or null in case of error
	 */		
	public Rectangle getRectangle(String key) {
		return (Rectangle)preferences.get(key);
	}

	/**
	 * Returns the boolean value specified by the given key from the preferences file.
	 * @param key	the key
	 * @param def	the default value which will be returned in case of error
	 * @return	the boolean value
	 */	
	public boolean getBoolean(String key, boolean def) {
		Boolean b = (Boolean)preferences.get(key);
		return (b != null) ? b.booleanValue() : def;
	}

	/**
	 * Writes the specified key-value pair into the preferences file.
	 * @param key	the key
	 * @param value the value
	 */	
	public void setString(String key, String value) {
		if (key == null || value == null) return;
		preferences.put(key, value);
	}

	/**
	 * Writes the specified key-value pair into the preferences file.
	 * @param key	the key
	 * @param value the value
	 */	
	public void setInt(String key, int value) {
		if (key == null) return;
		preferences.put(key, new Integer(value));
	}
	
	/**
	 * Writes the specified key-value pair into the preferences file.
	 * @param key	the key
	 * @param value the value
	 */		
	public void setLong(String key, long value) {
		if (key == null) return;
		preferences.put(key, new Long(value));
	}

	/**
	 * Writes the specified key-value pair into the preferences file.
	 * @param key	the key
	 * @param value the value
	 */		
	public void setDouble(String key, double value) {
		if (key == null) return;
		preferences.put(key, new Double(value));
	}

	/**
	 * Writes the specified key-value pair into the preferences file.
	 * @param key	the key
	 * @param value the value
	 */		
	public void setPoint(String key, Point value) {
		if (key == null || value == null) return;
		preferences.put(key, value);
	}

	/**
	 * Writes the specified key-value pair into the preferences file.
	 * @param key	the key
	 * @param value the value
	 */		
	public void setRectangle(String key, Rectangle value) {
		if (key == null || value == null) return;
		preferences.put(key, value);
	}
	
	protected String pointToString(Point p) {
		return "(" + p.x + ", " + p.y + ")";
	}
	
	protected String rectangleToString(Rectangle rect) {
		return "(" + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height + ")";
	}
	
	protected Point stringToPoint(String s) {
		try {
			int i, x, y;
			if (s.charAt(0) != '(') return null;
			i = s.indexOf(", ", 1);
			x = new Integer(s.substring(1, i)).intValue();
			y = new Integer(s.substring(i+2, s.indexOf(")", i+2))).intValue();
			return new Point(x, y);
		} catch (NumberFormatException e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	protected Rectangle stringToRectangle(String s) {
		try {
			int i, j, x, y, w, h;
			if (s.charAt(0) != '(') return null;
			i = 1;
			j = s.indexOf(", ", i);
			x = new Integer(s.substring(i, j)).intValue();
			i = j+2;
			j = s.indexOf(", ", i);
			y = new Integer(s.substring(i, j)).intValue();
			i = j+2;
			j = s.indexOf(", ", i);
			w = new Integer(s.substring(i, j)).intValue();
			i = j+2;
			j = s.indexOf(")", i);
			h = new Integer(s.substring(i, j)).intValue();
			return new Rectangle(x, y, w, h);
		} catch (NumberFormatException e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	protected Boolean stringToBoolean(String s) {
		if (s.equals("true")) return new Boolean(true);
		if (s.equals("false")) return new Boolean(false);
		throw new RuntimeException("Could not parse boolean value!");
	}
	
	protected String toString(Object o) {
		if (o instanceof Point) return pointToString((Point)o);
		if (o instanceof Rectangle) return rectangleToString((Rectangle)o);
		return o.toString();
	}
	
	protected Object toObject(String s) {
		if (s.startsWith("(") && s.endsWith(")")) {
			// Point or Rectangle?
			if (s.indexOf(", ") == s.lastIndexOf(", ")) {
				return stringToPoint(s);
			} else {
				return stringToRectangle(s);
			}
		}		
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {}
		try {
			return Long.valueOf(s);
		} catch (NumberFormatException e) {}
		try {
			return Double.valueOf(s);
		} catch (NumberFormatException e) {}
		try {
			return stringToBoolean(s);	
		} catch (RuntimeException e) {}	
		return s;
	}
	
}
