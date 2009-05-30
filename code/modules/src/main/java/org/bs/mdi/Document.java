/* 
 * Document.java - document object
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
 * The Document class manages data and views. 
 * A document consists of two major components: 
 * its data and one or more views, which are represented by the 
 * {@link RootData} and {@link RootView} classes.
 * The combination of data and views is called a <code>Document</code>. 
 */
public class Document {
	
	Application application;
	String filename;
	String baseFilename;
	RootData data;
	ArrayList rootViews;
	UndoManager undoManager;
	static int untitledDocumentCounter = 0;
	boolean dirty = false;
	boolean fileExists = false;
	boolean opened;

	protected Document(Application app) {
		application = app;
		rootViews = new ArrayList();
		opened = true;
		undoManager = new UndoManager(this);
	}

	/**
	 * Returns if this document is "dirty", i.e. contains unsaved data.
	 * @return	true if this document is "dirty"
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Returns true if the document's filename already exists.
	 * @return	true if the document's filename already exists
	 */
	public boolean isFileExistent() {
		return fileExists;
	}

	/**
	 * Returns the document's filename.<p>
	 * Note that every document has a filename, even if the document has not yet
	 * been saved. The filename of a newly created document could be "Untitled-1", 
	 * for example.
	 * @return	the document's filename.
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Returns the basename of the document's filename.
	 * This is just the last part of the path string. For example, the basename
	 * of "/bin/bash" would be just "bash".
	 * @return	the basename of the filename
	 */
	public String getBaseFilename() {
		return baseFilename;
	}

	/**
	 * Returns the data associated with this document.
	 * @return	 the data
	 */
	public RootData getData() {
		return data;
	}

	/**
	 * Gets the specified view. 
	 * @param index	the index of the view
	 * @return	the view
	 */
	public RootView getView(int index) {
		return (RootView)rootViews.get(index);
	}
	
	/**
	 * Gets an array containing all root views.
	 * @return	all RootView's
	 */
	public RootView[] getViews() {
		return (RootView[])rootViews.toArray(new RootView[0]);
	}
	
	/**
	 * Gets an iterator for all views associated with this document.
	 * @return	an iterator
	 */
	public Iterator getViewIterator() {
		return rootViews.iterator();
	}
	
	/**
	 * Returns the number of views associated with this document.
	 * @return	the number of views
	 */
	public int countViews() {
		return rootViews.size();
	}
	
	/**
	 * Associates the specified view with this document. 
	 * @param view	the view to be associated with this document
	 */
	public void addView(RootView view) {
		rootViews.add(view);
		data.addObserver(view);
		view.addObserver(data);
	}
	
	/**
	 * Removes the specified view from this document.
	 * @param view	the view to be removed
	 */
	public void removeView(RootView view) {
		rootViews.remove(view);
	}
	
	/**
	 * Synchronize all root views with the data.
	 * Calls {@link View#syncWithData} for every associated root view.
	 */
	public void syncViewsWithData() {		
		for (int i=0; i<countViews(); i++) {
			try {
				getView(i).syncWithData();
			} catch (RuntimeException e) {}
		}
	}
	
	/**
	 * Gets the UndoManager of this Document.
	 * @return	the UndoManager
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}
	
	/**
	 * Convenience method for getting the RootData's printer.
	 * Equivalent for calling <code>getData().getPrinter()</code>.
	 * @return	the document's printer, or null if doesn't support printing 
	 */
	public Printer getPrinter() {
		return data.getPrinter();
	}
	
	/**
	 * Set's the "dirty" status of a document.<p>
	 * A document is dirty if it contains unsaved modifications.
	 * @param dirty	true if the document should be considered dirty, false otherwise
	 */
	public void setDirty(boolean dirty) {
		if (this.dirty == dirty) return;
		this.dirty = dirty;
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.DOCUMENT_DIRTY, this);
	}
	
	/**
	 * Creates a new document.
	 * @return	the new document
	 */
	public static Document createNew() {
		RootView view;
		DocumentWindow window;
		Application app = Application.getInstance();
		Document doc = new Document(app);
		
		untitledDocumentCounter++;
		doc.setFilename(Application.tr("Untitled")+"-"+untitledDocumentCounter);
		doc.data = Application.getInstance().createRootData();	
		view = Application.getInstance().createRootView();
		window = Application.getMainWindow().createDocumentWindow();
		doc.data.setDocument(doc);
		view.setData(doc.data);
		window.setView(view);
		window.setTitle(doc.filename);
		window.setVisible(true);
		window.toFront();
		doc.addView(view);
		doc.syncViewsWithData();
		doc.getUndoManager().flushLogs();
		doc.dirty = false;		
		return doc;
	}

	/**
	 * Opens an existing document.
	 * @param filename	the document's filename
	 * @return	the opened Document
	 * @throws FileIOException
	 */
	public static Document open(String filename) throws FileIOException {
		RootView view;
		DocumentWindow window;
		
		Application app = Application.getInstance();
		Document doc = new Document(app);
		doc.setFilename(filename);
		doc.data = Application.getFileIOManager().load(filename);				
		view = Application.getInstance().createRootView();
		window = Application.getMainWindow().createDocumentWindow();
		doc.data.setDocument(doc);
		view.setData(doc.data);
		window.setView(view);
		window.setTitle(doc.filename);
		window.setVisible(true);
		window.toFront();
		doc.addView(view);
		doc.syncViewsWithData();
		doc.fileExists = true;
		doc.getUndoManager().flushLogs();
		doc.dirty = false;
		return doc;
	}

	/**
	 * Saves this document to a file.
	 * @param filename	the filename to be used
	 * @throws FileIOException
	 */
	public void save(String filename) throws FileIOException {
		Application.getFileIOManager().save(data, filename);
		setFilename(filename);
		for (int i=0; i<rootViews.size(); i++) {
			DocumentWindow w = getView(i).getWindow();
			if (w == null) continue;
			w.setTitle(filename);
		}		
		setDirty(false);
		fileExists = true;
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.DOCUMENT_SAVED, this);
	}
	
	
	/**
	 * Tells if this document is still opened.
	 * @return	true if the document has not yet been closed, false otherwise
	 */
	public boolean isOpened() {
		return opened;
	}
	

	/**
	 * Tries to close this document.
	 * Note that this operation does not necessarily have to succeed. If the
	 * document contains unsaved modifications and the user decides to close
	 * it, he may be asked to confirm this operation. He has the option of
	 * not letting the <code>close()</code> method succeed.
	 * @return	true if closing the document succeeded, false otherwise
	 */
	public boolean close() {
		for (int i=0; i<rootViews.size(); i++) {
			DocumentWindow w = getView(i).getWindow();
			if (w == null) continue;
			if (!w.close()) return false;
		}
		data = null;				
		opened = false;
		return true;
	}
	
	/**
	 * Convenience function for calling getUndoManager().undo();
	 * @return	the undone Action or null in case of error
	 */
	public Action undo() {
		try {
			return getUndoManager().undo();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Convenience function for calling getUndoManager().redo();
	 * @return	the redone Action or null in case or error
	 */
	public Action redo() {
		try {
			return getUndoManager().redo();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Convenience function for calling getUndoManager().isUndoPossible();
	 * @return	true if an action can be undone, false otherwise
	 */
	public boolean isUndoPossible() {
		return getUndoManager().isUndoPossible();
	}
	
	/**
	 * Convenience function for calling getUndoManager().isRedoPossible();
	 * @return	true if an action can be redone, false otherwise
	 */
	public boolean isRedoPossible() {
		return getUndoManager().isRedoPossible();
	}
	
	/**
	 * Sets the filename and the baseFilename.
	 * @param filename	the new filename
	 */
	protected void setFilename(String filename) {
		this.filename = filename;
		baseFilename = new java.io.File(filename).getName();
	}

}
