/* 
 * RecentFiles.java - Remembers recently opened filenames
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
 * Remembers recently opened filenames.
 */
public class RecentFiles {

	class RecentFileEntry {
		String filename;
		long time;
		
		public RecentFileEntry(String filename, long time) {
			this.filename = filename;
			this.time = time;
		}
	}
	
	class RecentFileEntryComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			long t1 = ((RecentFileEntry)o1).time;
			long t2 = ((RecentFileEntry)o2).time;
			return (int)(t2-t1);
		}
	}
	
	long expiryTime = 7 * 24 * 60 * 60 * 1000;	// store recent files for 1 week
	int maxEntries = 12;
	ArrayList entries;
	RecentFileEntryComparator entryComparator;
	
	/**
	 * Creates a new RecentFiles object.
	 */
	public RecentFiles() {
		entries = new ArrayList();
		entryComparator = new RecentFileEntryComparator();
		load();
	}

	/**
	 * Adds the given filename to the list of remembered filenames.
	 * @param filename	the filename to be remembered
	 */
	public void add(String filename) {
		RecentFileEntry entry;
		entry = findEntry(filename);
		if (entry != null) {
			entry.time = System.currentTimeMillis();
			sort();
			return;
		}
		entry = new RecentFileEntry(filename, System.currentTimeMillis());
		entries.add(entry);
		sort();
	}
	
	/**
	 * Returns the list of recently opened filenames.
	 * @return	the list of recent filenames
	 */
	public String[] getList() {
		String filenames[] = new String[entries.size()];
		for (int i=0; i<Math.min(entries.size(), maxEntries); i++) {
			RecentFileEntry e = (RecentFileEntry)entries.get(i);
			filenames[i] = e.filename;
		}
		return filenames;
	}
	
	/**
	 * Returns the number of items which are currently saved in the list
	 * of recently opened filenames.
	 * @return	the number of filenames
	 */
	public int count() {
		return entries.size();
	}
	
	/**
	 * Removes the given filename from the list of recently opened filenames.
	 * @param filename	the filename to be removed
	 */
	public void remove(String filename) {
		for (int i=0; i<entries.size(); i++) {
			RecentFileEntry entry = (RecentFileEntry)entries.get(i);
			if (entry.filename.equals(filename)) {
				entries.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Returns the expiry time of all entries, in milliseconds.
	 * If a filename has not been re-opened during its expiry time, 
	 * it is automatically removed from the list of remembered filenames.
	 * @return	the expiry time
	 */
	public long getExpiryTime() {
		return expiryTime;
	}
	
	/**
	 * Sets the "time to live" of an recent filename entry.
	 * If a filename has not been re-opened during its expiry time, 
	 * it is automatically removed from the list of remembered filenames.
	 * @param time	the expiry time in milliseconds
	 */
	public void setExpiryTime(long time) {
		expiryTime = time;
	}
	
	/**
	 * Returns the maximum number of filenames returned by {@link #getList}.
	 * @return	the maximum number of filenames returned by {@link #getList}.
	 */
	public int getMaxEntries() {
		return maxEntries;
	}
	
	/**
	 * Sets the maximum number of filenames returned by {@link #getList}.
	 * @param maxEntries	the new maximum number of filenames
	 */
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}
	
	protected void sort() {
		Collections.sort(entries, entryComparator);
	}
	
	protected void load() {
		Preferences prefs = Application.getPreferences();
		int numEntries = prefs.getInt("mdi.recentfiles.count", 0);
		entries.clear();
		for (int i=0; i<numEntries; i++) {
			String filename = prefs.getString("mdi.recentfiles.filename" + i);
			long time = prefs.getLong("mdi.recentfiles.time" + i, 0);
			if (filename == null || time == 0) continue;
			entries.add(new RecentFileEntry(filename, time));
		}
		sort();
	}
	
	protected void save() {
		expire();
		Preferences prefs = Application.getPreferences();
		for (int i=0; i<entries.size(); i++) {
			RecentFileEntry entry = (RecentFileEntry)entries.get(i);
			prefs.setString("mdi.recentfiles.filename" + i, entry.filename);
			prefs.setLong("mdi.recentfiles.time" + i, entry.time);
		}
		prefs.setInt("mdi.recentfiles.count", entries.size());
	}
	
	protected void expire() {
		long currentTime = System.currentTimeMillis();
		for (int i=0; i<entries.size(); i++) {
			RecentFileEntry entry = (RecentFileEntry)entries.get(i);
			if (entry.time + expiryTime < currentTime) {
				entries.remove(i);
				i--;
			}
		}
	}
	
	protected RecentFileEntry findEntry(String filename) {
		for (int i=0; i<entries.size(); i++) {
			RecentFileEntry entry = (RecentFileEntry)entries.get(i);
			if (entry.filename.equals(filename)) return entry;
		}
		return null;
	}
	
}
