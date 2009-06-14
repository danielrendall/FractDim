/* 
 * Application.java - general application interface
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
import java.util.*;

/**
 * The base class for all MDI applications.
 */
public abstract class Application implements MessageProcessor {
	
	protected static List documents;
	protected static DocumentWindow currentWindow;
	protected static MainWindow mainWindow;
	protected static ClipboardManager clipboardManager;
	protected static CompositeResources resources;
	protected static FileIOManager fileIOManager;
	protected static Application application;
	protected static Preferences preferences;
	protected static RecentFiles recentFiles;
	protected static MessageDispatcher messageDispatcher;
	protected static boolean running = false;
	
	/**
	 * Creates a new Application.
	 * Due to static variable initialization in this constructor, there should
	 * be only one Application instance. Anyway, there is no reason for creating 
	 * more than one Application instance. Don't try that, it will not work!
	 * You can always get the current Application instance by calling the static 
	 * method {@link #getInstance}.
	 */
	public Application() {
		if (application != null) {
			// There may be only one Application instance! 
			System.err.println("MDI Framework: ERROR: Attempting to re-instantiante Application");
			System.err.println("HINT: You shuld try to get the Application instance via Application.getInstance()");
			return;
		}
		application = this;
		preferences = new Preferences();
		resources = new CompositeResources();
		resources.addResource(new DefaultResources());
		resources.addResource(createResources());
		recentFiles = new RecentFiles();
		messageDispatcher = new MessageDispatcher();
		documents = Collections.synchronizedList(new ArrayList());
		fileIOManager = new FileIOManager();
		fileIOManager.registerAllModules(createFileIOModules());
		clipboardManager = new ClipboardManager();
		clipboardManager.registerAllConverters(createActionConverters());
		getMessageDispatcher().registerProcessor(this);
		mainWindow = createMainWindow();
		setStatus(tr("Ready"));
	}

	/**
	 * Gets the current instance of the <code>Application</code> class.
	 * @return	the only valid instance of Application
	 */
	public static Application getInstance() {
		return application;
	}
	
	/**
	 * Gets the application's preferences.
	 * The {@link Preferences} object manages per-user preferences and settings for a particular application.
	 * @return	the <code>Preferences</code> object
	 */
	public static Preferences getPreferences() {
		return preferences;
	}
	
	/**
	 * Gets the {@link RecentFiles} object which automatically maintains a list of recently opened filenames. 
	 * @return	the RecentFiles object
	 */
	public static RecentFiles getRecentFiles() {
		return recentFiles;
	}
	
	/**
	 * Gets the {@link MessageDispatcher} object which is responsible for communication
	 * between various parts of the framework.
	 * @return	the framework's message dispatcher
	 */
	public static MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}
	
	/**
	 * Get the name of the application.
	 * Subclasses have to overwrite this method to set the own application name.
	 * Note that this application name is used by the {@link Preferences} object to create
	 * the user-specific settings filename, so your application name should be unique
	 * in order to avoid filename collisions.
	 * @return	the name of the application
	 */
	public abstract String getName();
	
	/**
	 * Returns the running status of this application.
	 * @return true if the application is currently running, false otherwise
	 */
	public static boolean isRunning() {
		return running;
	}
	
	/**
	 * Returns a localized string for the specified key.
	 * This method searches the applications resources (as created by
	 * {@link #createResources}) for the specified key and returns its value.
	 * This is the preferred way of providing internationalization support for
	 * MDI applications.
	 * @param key	the key which should be looked up in the resource bundle
	 * @return	the localized string for the given key
	 */
	public static String tr(String key) {		
		try {
			String s = resources.i18n(key);
			if (s != null) return s;
		} catch (RuntimeException e) {}
		System.err.println("MDI framework: Warning: No translation available for resource key \"" + key + "\"");
		return key;
	}

	/**
	 * Creates a new document and makes its view (window) visible.
	 * @return the created document
	 */
	public Document newDocument() {
		Document doc = Document.createNew();
		documents.add(doc);
		return doc;
	}
	
	/**
	 * Displays a "Open File"-Dialog and tries to open the selected file.
	 * Displays an error message in case of an error.
	 * @return the opened document
	 */
	public Document openDocument() {
		FileFormat formats[] = fileIOManager.getSupportedFormats(FileIOManager.OPEN);
		String filename = mainWindow.showFileOpenDialog(formats);
		if (filename == null || filename.equals("")) return null;
		return openDocument(filename);
	}
	
	/**
	 * Tries to open the given file.
	 * Displays an error message in case of an error.
	 * @param filename	the file to be opened
	 * @return the opened document
	 */
	public Document openDocument(String filename) {
		Document doc;
		for (int i=0; i<documents.size(); i++) {
			Document d = (Document)documents.get(i);
			if (d.getFilename().equals(filename)){
				try {
					selectWindow(d.getView(0).getWindow());
				} catch (RuntimeException ignored) {}
				return null;
			}
		}
		setStatus(tr("Loading..."));
		setBusy(true);
		try {			
			doc = Document.open(filename);			
			documents.add(doc);
			recentFiles.add(filename);
		} catch (FileIOException e) {
			mainWindow.showMessage(MainWindow.ERROR, mainWindow, 
				e.getLocalizedMessage());
			recentFiles.remove(filename);
			doc = null;
		}
		setStatus(tr("Ready"));
		setBusy(false);
		return doc;
	}
	
	/**
	 * Tries to save the current document.
	 * Displays an error message in case of an error.
	 * If no valid filename was given to the document before, it displays a
	 * "Save File"-Dialog to choose one (i.e. it behaves like saveDocumentAs() in this case).
	 */
	public void saveDocument() {
		if (currentWindow == null) return;
		if (!getCurrentDocument().isFileExistent()) {
			saveDocumentAs();
			return;
		}
		setStatus(tr("Saving..."));
		setBusy(true);
		try {
			getCurrentDocument().save(getCurrentDocument().getFilename());
			recentFiles.add(getCurrentDocument().getFilename());
		} catch (FileIOException e) {			
			mainWindow.showMessage(MainWindow.ERROR, mainWindow, 
				e.getLocalizedMessage());
		}
		setStatus(tr("Ready"));
		setBusy(false);
	}
	
	/**
	 * Displays a "Save File"-Dialog and tries to save the current document using the 
	 * selected filename.
	 * Displays an error message in case of an error.
	 */
	public void saveDocumentAs() {
		if (currentWindow == null) return;
		FileFormat formats[] = fileIOManager.getSupportedFormats(FileIOManager.SAVE);
		String filename = mainWindow.showFileSaveDialog(formats, getCurrentDocument().getFilename());
		if (filename == null || filename.equals("")) return;
		if (fileIOManager.fileExists(filename)) {
			String buttons[] = {"Yes", "No"};
			int confirm = mainWindow.showDialog(MainWindow.WARNING, mainWindow, tr("Overwrite?"), buttons, 1);
			if (confirm == 1) return;
		}
		setStatus(tr("Saving..."));
		setBusy(true);
		try {
			getCurrentDocument().save(filename);
			recentFiles.add(filename);
		} catch (FileIOException e) {			
			mainWindow.showMessage(MainWindow.ERROR, mainWindow, 
				e.getLocalizedMessage());
		}		
		setStatus(tr("Ready"));
		setBusy(false);
	}
	
	/**
	 * Tries to export the current document.
	 * Displays an error message in case of an error.
	 * @param exporter	the FileExporter to use.
	 */
	public void exportDocument(FileExporter exporter) {
		if (getCurrentDocument() == null) return;
		setStatus(tr("Exporting..."));
		try {
			fileIOManager.export(getCurrentDocument().getData(), exporter);
		} catch (FileIOException e) {			
			mainWindow.showMessage(MainWindow.ERROR, mainWindow, 
				e.getLocalizedMessage());
		}
		setStatus(tr("Ready"));
	}
	
	/**
	 * Shows a dialog allowing the user to setup printing options such as
	 * paper size and orientation.
	 */
	public void showPrintSetup() {
		mainWindow.showPrintSetup();
	}
	
	/**
	 * Shows a print preview dialog for the current document.
	 * Does nothing if there's no current document.
	 */
	public void showPrintPreview() {
		mainWindow.showPrintPreview();
	}
	
	/**
	 * Prints the current document.
	 * The main window is responsible for showing a print dialog.
	 * Does nothing if there's no current document.
	 */
	public void printDocument() {
		mainWindow.printDocument();
	}

	/**
	 * Tries to close the current document and all of its views and windows.
	 * Note that the close operation may be aborted by the document
	 * (for example, the window may display a dialog to ask the user
	 * if he really wants to close the document).
	 */
	public void closeDocument() {
		Document doc = getCurrentDocument();
		if (doc == null) return;
		doc.close();
	}
	
	/**
	 * Tries to close the current window.
	 * Note that the close operation may be aborted by the document
	 * (for example, the window may display a dialog to ask the user
	 * if he really wants to close the document).
	 */
	public void closeWindow() {
		if (currentWindow == null) return;
		currentWindow.close();
	}
	
	/**
	 * Sets the text to be displayed in the application's status bar.
	 * @param status	The status text to be displayed
	 */
	public static void setStatus(String status) {
		mainWindow.setStatus(status);
	}
	
	/**
	 * Gives the user the indication that the application is busy now.
	 * @param busy	true if the busy indication should be enabled, false otherwise
	 */
	public static void setBusy(boolean busy) {
		mainWindow.setBusy(busy);
	}
	
	/**
	 * Exits the application by closing its main window.
	 * This is the preferred way to exit the application. The user is asked to save modified documents 
	 * and once all data is saved, the application terminates. However, this process may be interrupted
	 * by the user. Therefore this method can not guarantee a successful termination.
	 * @return	true if the application was successfully terminated, false otherwise
	 */
	public boolean close() {
		return mainWindow.close();
	}
	
	/**
	 * Closes the application the brute-force way.
	 * This closes the application by terminating the Java virtual machine. This does not remind the user
	 * of unsaved data; it's just quick and brutal. Consider using close() for a proper and safe method
	 * of closing the application.
	 */
	public void exit() {
		System.out.println(tr("Please wait while shutting down..."));		
		recentFiles.save();
		preferences.save();
		mainWindow.hide();		
		System.exit(0);
	}
	
	/**
	 * Perform an undo operation on the current document.
	 *  Does nothing if there is no current document or an undo is not possible.
	 */
	public void undo() {
		if (getCurrentDocument() == null) return;
		setStatus(tr("Undo in progress..."));
		setBusy(true);
		getCurrentDocument().undo();
		setStatus(tr("Ready"));
		setBusy(false);
	}
	
	/**
	 * Perform a redo operation on the current document.
	 *  Does nothing if there is no current document or a redo is not possible.
	 */
	public void redo() {
		if (getCurrentDocument() == null) return;
		setStatus(tr("Redo in progress..."));
		setBusy(true);
		getCurrentDocument().redo();
		setStatus(tr("Ready"));
		setBusy(false);
	}
	
	/**
	 * Perform a cut operation on the current document.
	 * Does nothing if there is no current document or a cut is not possible.
	 */
	public void cut() {
		if (getCurrentView() == null) return;
		try {
			getClipboardManager().setContent(getCurrentView().cut());
		} catch (ClipboardConversionException e) {
			mainWindow.showMessage(MainWindow.ERROR, currentWindow, 
					tr("Could not perform a cut operation."));
		}
	}
	
	/**
	 * Perform a copy operation on the current document.
	 * Does nothing if there is no current document or a copy is not possible.
	 */
	public void copy() {
		if (getCurrentView() == null) return;
		try {
			getClipboardManager().setContent(getCurrentView().copy());
		} catch (ClipboardConversionException e) {
			mainWindow.showMessage(MainWindow.ERROR, currentWindow, 
					tr("Could not perform a copy operation."));
		}
	}
	
	/**
	 * Perform a paste operation on the current document.
	 * Does nothing if there is no current document or a paste is not possible.
	 */
	public void paste() {
		if (getCurrentView() == null) return;
		try {
			getCurrentView().paste(getClipboardManager().getContent());
		} catch (ClipboardConversionException e) {
			mainWindow.showMessage(MainWindow.ERROR, currentWindow, 
					tr("Incompatible clipboard content"));
		}
	}
	
	/**
	 * Perform a delete operation on the current document.
	 * Does nothing if there is no current document or a delete is not possible.
	 */
	public void delete() {
		if (getCurrentView() == null) return;
		getCurrentView().delete();
	}
	
	/**
	 * Returns true if the last modification of the current document can be undone.
	 * @return	true if an undo operation is possible at the moment
	 */
	public boolean isUndoPossible() {
		if (getCurrentDocument() == null) return false;
		return getCurrentDocument().isUndoPossible();
	}
	
	/**
	 * Returns true if the last undo operation of the current document can be redone.
	 * @return	true if a redo operation is possible at the moment
	 */
	public boolean isRedoPossible() {
		if (getCurrentDocument() == null) return false;
		return getCurrentDocument().isRedoPossible();
	}
	
	/**
	 * Returns true if information can be cutted to the clipboard from the current document window.
	 * @return	true if a cut operation is possible at the moment
	 */
	public boolean isCutPossible() {
		return Application.getClipboardManager().isReady() &&
				getCurrentView() != null && 
				getCurrentView().isCutPossible();
	}

	/**
	 * Returns true if information can be copied to the clipboard from the current document window.
	 * @return	true if a copy operation is possible at the moment
	 */
	public boolean isCopyPossible() {
		return Application.getClipboardManager().isReady() &&
				getCurrentView() != null && 
				getCurrentView().isCopyPossible();
	}

	/**
	 * Returns true if information can be pasted from the clipboard to the current document window.
	 * @return	true if a paste operation is possible at the moment
	 */
	public boolean isPastePossible() {
		return Application.getClipboardManager().isReady() &&
				getCurrentView() != null &&  
				!getClipboardManager().isClipboardEmpty() && 
				getCurrentView().isPastePossible();
	}

	/**
	 * Returns true if information can be deleted from the current document window.
	 * @return	true if a delete operation is possible at the moment
	 */
	public boolean isDeletePossible() {
		return Application.getClipboardManager().isReady() &&
				getCurrentView() != null &&  
				getCurrentView().isDeletePossible();
	}
	
	/**
	 * Returns the {@link Resources} object which handles internationalization 
	 * and icon loading.
	 * @return	the application's resources
	 */
	public static Resources getResources() {
		return resources;
	}
	
	/**
	 * Returns the {@link FileIOManager} which manages modules for performing 
	 * file I/O (loading, saving, exporting).
	 * @return	the application's FileIOManager
	 */
	public static FileIOManager getFileIOManager() { 
		return fileIOManager; 
	}
	
	/**
	 * Returns the {@link ClipboardManager} which provides clipboard access 
	 * and data transfer/conversion functions.
	 * @return	the application's ClipboardManager
	 */
	public static ClipboardManager getClipboardManager() {
		return clipboardManager;
	}
	
	/**
	 * Returns the application's main window.
	 * @return	the application's main window
	 */
	public static MainWindow getMainWindow() { 
		return mainWindow; 
	}
	
	/**
	 * Returns an array of currently opened documents.
	 * A document is considered open if it has at least one open window. 
	 * @return	an array of all currently opened Documents
	 */
	public static Document[] getDocuments() {
		return (Document[])documents.toArray(new Document[0]);
	}
	
	/**
	 * Returns the number of currently opened documents.
	 * @return	the number of currently opened documents
	 */
	public static int countDocuments() {
		return documents.size();
	}
	
	/**
	 * Gets the document at the specified index.
	 * @param index
	 * @return	the document at the specified position
	 */
	public static Document getDocument(int index) {
		return (Document)documents.get(index);
	}
	
	/**
	 * Gets an iterator for all currently opened documents.
	 * @return	an iterator
	 */
	public static Iterator getDocumentIterator() {
		return documents.iterator();
	}

	/**
	 * Returns the currently active window.
	 * @return	the currently active Window, or null if there is no active window
	 */
	public static DocumentWindow getCurrentWindow() { 
		return currentWindow; 
	}
	
	/**
	 * Returns the document which belongs to the current window.
	 * Calling this method is equivalent for calling
	 * <code>getCurrentWindow().getDocument()</code> 
	 * (except for NullPointerException handling).
	 * @return	the currently active document, or null if there is no active document
	 */
	public static Document getCurrentDocument() {
		try {
			return currentWindow.getDocument();
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * Returns the view which belongs to the current window.
	 * Calling this method is equivalent for calling
	 * <code>getCurrentWindow().getView()</code>
	 * (except for NullPointerException handling). 
	 * @return	the currently active view, or null if there is no active view
	 */
	public static RootView getCurrentView() {
		try {
			return currentWindow.getView();
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * Returns the data which belongs to the current window.
	 * Calling this method is equivalent for calling
	 * <code>getCurrentWindow().getDocument().getData()</code>
	 * (except for NullPointerException handling). 
	 * @return	the currently active data, or null if there is no active data object
	 */	
	public static RootData getCurrentData() {
		try {
			return currentWindow.getDocument().getData();
		} catch (RuntimeException e) {
			return null;
		}
	}	
	
	/**
	 * Activates a window programmatically.
	 * @param window	the window to be activated
	 */
	public static void selectWindow(DocumentWindow window) {
		if (window == null) return;
		window.toFront();
	}
	
	/**
	 * Prevents other documents from becoming active.
	 * Using this method, you can ensure that only the currently
	 * selected document stays active. The user will be unable to
	 * activate other document windows than the current one.
	 * This is done by calling setEnabled on every document window
	 * which does not belong to the currently selected document.
	 * Note that this method does not necessarily have to succeed. 
	 * If the return value is false, this method failed and had no effect.
	 * @param locked	true to activate the document lock, false to deactivate
	 * @return	true on success, false on error
	 */
	public static boolean setCurrentDocumentLocked(boolean locked) {
		Document doc = getCurrentDocument();
		Document d;
		if (getCurrentDocument() == null) return false;
		
		for (int i=0; i<countDocuments(); i++) {
			d = getDocument(i);
			if (d == doc) continue;
			for (int j=0; j<d.countViews(); j++) {
				try {
					d.getView(j).getWindow().setEnabled(!locked);
				} catch (RuntimeException e) {}
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.MDIMessageReceiver#handleMDIMessage(org.bs.mdi.MDIMessage)
	 */
	public void processMessage(Object source, int type, Object argument) {
		DocumentWindow win;
		Document doc;
				
		switch (type) {
			case MessageDispatcher.WINDOW_SELECTED:
				win = (DocumentWindow)argument;
				currentWindowChanged(win);
				break;
			case MessageDispatcher.WINDOW_CLOSED:
				win = (DocumentWindow)argument;
				doc = win.getDocument();
				doc.removeView(win.getView());
				if (doc.countViews() == 0)
					documents.remove(doc);
				break;
		}
	}
	
	/**
	 * Starts and runs the application.
	 * This method is designed to be called by the <code>main</code> method of the actual application.
	 * It brings up the main window and loads the documents specified by the filenames
	 * given as command line arguments.	 
	 * @param args	the command line arguments passed to the application
	 */
	public void run(String args[]) {
		running = true;
		mainWindow.setTitle(getName());
		mainWindow.show();
		for (int i=0; i<args.length; i++) {
			try {
				openDocument(new java.io.File(args[i]).getAbsolutePath());
			} catch (Exception e) {};
		}		
	}

	/**
	 * Called when another document window is activated (receives the focus).
	 * @param newWindow	the DocumentWindow that is now active
	 */
	protected void currentWindowChanged(DocumentWindow newWindow) {
		currentWindow = newWindow;
		if (getCurrentDocument() != null) {
			mainWindow.setTitle(getCurrentDocument().getBaseFilename()
					+ " - " + getName());
		} else {
			mainWindow.setTitle(getName());
		}
		getMessageDispatcher().dispatch(
				this, MessageDispatcher.DOCUMENT_SELECTED, getCurrentDocument());
	}
	
	
	/**
	 * Creates and returns all available File I/O extension modules (see {@link FileIOModule})..
	 * FileIOModules are required for opening, saving and exporting documents.
	 * The framework is responsible for making these modules available to
	 * the {@link FileIOManager}.
	 * This is a factory method which is to be implemented by subclasses of 
	 * Application.
	 * @return	an array of all available FileIOModules
	 */
	protected abstract FileIOModule[] createFileIOModules();
	
	/**
	 * Creates and returns all available Action converters (see {@link ActionConverter}).
	 * ActionConverters are required for clipboard functionality (cut, copy, paste).
	 * The framework is responsible for making these converters available to the
	 * {@link ClipboardManager}.
	 * This is a factory method which is to be implemented by subclasses of 
	 * Application.
	 * @return	an array of all available ActionConverters
	 */
	protected abstract ActionConverter[] createActionConverters();
	
	/**
	 * Creates and return an application-specific main window (see {@link MainWindow}).
	 * This is a factory method which is to be implemented by subclasses of 
	 * Application.
	 * @return	the main window
	 */
	protected abstract MainWindow createMainWindow();
	
	/**
	 * Creates and returns the applications resources.
	 * Resources provide internationalization functionality to MDI applications.
	 * Since the MDI framework itself relies on this functionality, is is necessary
	 * to create and return a proper Resources object here.
	 * The framework is responsible for making the resource available to the
	 * {@link CompositeResources}.
	 * This is a factory method which is to be implemented by subclasses of 
	 * Application.
	 * @return	the applications resources
	 */
	protected abstract Resources createResources();
	
	/**
	 * Creates and returns a new RootData instance.
	 * This is a factory method which is to be implemented by subclasses of 
	 * Application.
	 * @return	a new RootData object
	 */
	public abstract RootData createRootData();
	
	/**
	 * Creates and returns a new RootView instance.
	 * This is a factory method which is to be implemented by subclasses of 
	 * Application.
	 * @return	a new RootView object
	 */
	public abstract RootView createRootView();
	
}
