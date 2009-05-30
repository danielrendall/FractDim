/* 
 * View.java - View interface
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2004-2005 Bernhard Stiftner
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
 * Displays a piece of the document's information. 
 */
public interface View extends ActionObserver, ActionObservable, ActionProcessor {
	
	/**
	 * Returns the document which this view is assigned to.
	 * This is just a convenience function which is roughly equivalent to
	 * <code>getData().getDocument</code>.
	 * @return	the document
	 */
	public Document getDocument();
	
	/**
	 * Returns the Data object associated with this view.
	 * @return	the corresponding Data object
	 */
	public Data getData();
	
	/**
	 * Associates this View with the given Data object.
	 * This method also makes this view observe the data as well as the
	 * data observe the view.
	 * @param data	the Data object
	 */
	public void setData(Data data);
	
	/**
	 * Gets the parent View object, or null if there is no parent object
	 * (i.e. this is probably a {@link RootView} object).
	 * @return	the parent object
	 */
	public View getParentView();
	
	/**
	 * Adds a child element.
	 * @param child	the child element
	 */
	public void addChild(View child);
	
	/**
	 * Removes a child element.
	 * @param child	the child element
	 */
	public void removeChild(View child);
	
	/**
	 * Counts all child elements.
	 * @return	the number of child elements
	 */
	public int countChildren();
	
	/**
	 * Gets the child element at the specified index.
	 * @param index	the index
	 * @return	the child element at the specified position
	 */
	public View getChild(int index);
	
	/**
	 * Perform a "copy to clipboard" operation.
	 * Converts the currently selected information to an action object
	 * and returns it.
	 * @return	the action object containing the copied information
	 */
	public Action copy();
	
	/**
	 * Perform a "cut to clipboard" operation.
	 * Converts the currently selected information to an action object
	 * and returns it.
	 * @return	the action object containing the cutted information
	 */
	public Action cut();
	
	/**
	 * Perform a "paste from clipboard" operation.
	 * The information to be pasted is delivered via the "action" parameter.
	 * @param action	the action object containing the information to be pasted
	 */
	public void paste(Action action);
	
	/**
	 * Perform a delete operation.
	 */
	public void delete();
	
	/**
	 * Returns true if content can be copied to the clipboard.
	 * @return	true if a copy operation is possible
	 */
	public boolean isCopyPossible();
	
	/**
	 * Enables or disables copying to the clipboard.
	 * @param possible	true if copying is possible, false otherwise
	 */
	public void setCopyPossible(boolean possible);
	
	/**
	 * Returns true if content can be cutted to the clipboard.
	 * @return	true if a cut operation is possible
	 */
	public boolean isCutPossible();
	
	/**
	 * Enables or disables cutting to the clipboard.
	 * @param possible	true if cutting is possible, false otherwise
	 */
	public void setCutPossible(boolean possible);
	
	/**
	 * Returns true if content can be pasted from the clipboard.
	 * @return	true if a paste operation is possible
	 */
	public boolean isPastePossible();
	
	/**
	 * Enables or disables pasting from the clipboard.
	 * @param possible	true if pasting is possible, false otherwise
	 */
	public void setPastePossible(boolean possible);
	
	/**
	 * Returns true if content can be deleted.
	 * @return	true if a delete operation is possible
	 */
	public boolean isDeletePossible();
	
	/**
	 * Enables or disables deleting operations.
	 * @param possible	true if deleting is possible, false otherwise
	 */
	public void setDeletePossible(boolean possible);
	
	/**
	 * Called from the Data object to indicate that the documents data has changed.<p>
	 * The view update or redraw its contents the reflect that changes.
	 */
	public void syncWithData();

}

