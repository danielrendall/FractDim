/* 
 * DefaultSwingCommands.java - default set of commands
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

package org.bs.mdi.swing;

import javax.swing.*;
import org.bs.mdi.*;

/**
 * SwingDefaultCommands - a default set of commands
 */
public class SwingDefaultCommands {
	
	SwingCommand showFileMenuCmd = new ShowFileMenuCmd();
	SwingCommand showEditMenuCmd = new ShowEditMenuCmd();
	SwingCommand showWindowMenuCmd = new ShowWindowMenuCmd();
	SwingCommand fileNewCmd = new FileNewCmd();
	SwingCommand fileOpenCmd = new FileOpenCmd();
	SwingCommand showFileRecentMenuCmd = new ShowFileRecentMenuCmd();
	SwingCommand fileSaveCmd = new FileSaveCmd();
	SwingCommand fileSaveAsCmd = new FileSaveAsCmd();
	SwingCommand showFileExportMenuCmd = new ShowFileExportMenuCmd();
	SwingCommand filePrintSetupCmd = new FilePrintSetupCmd();
	SwingCommand filePrintPreviewCmd = new FilePrintPreviewCmd();
	SwingCommand filePrintCmd = new FilePrintCmd();
	SwingCommand fileCloseCmd = new FileCloseCmd();
	SwingCommand fileQuitCmd = new FileQuitCmd();
	SwingCommand editUndoCmd = new EditUndoCmd();
	SwingCommand editRedoCmd = new EditRedoCmd();
	SwingCommand editCopyCmd = new EditCopyCmd();
	SwingCommand editCutCmd = new EditCutCmd();
	SwingCommand editPasteCmd = new EditPasteCmd();
	SwingCommand editDeleteCmd = new EditDeleteCmd();


	public SwingCommand getShowFileMenuCommand() {
		return showFileMenuCmd;
	}

	public SwingCommand getShowEditMenuCommand() {
		return showEditMenuCmd;
	}

	public SwingCommand getShowWindowMenuCommand() {
		return showWindowMenuCmd;
	}

	public SwingCommand getFileNewCommand() {
		return fileNewCmd;
	}

	public SwingCommand getFileOpenCommand() {
		return fileOpenCmd;
	}

	public SwingCommand getShowFileRecentMenuCommand() {
		return showFileRecentMenuCmd;
	}

	public SwingCommand getFileSaveCommand() {
		return fileSaveCmd;
	}

	public SwingCommand getFileSaveAsCommand() {
		return fileSaveAsCmd;
	}

	public SwingCommand getShowFileExportMenuCommand() {
		return showFileExportMenuCmd;
	}

	public SwingCommand getFilePrintSetupCommand() {
		return filePrintSetupCmd;
	}

	public SwingCommand getFilePrintPreviewCommand() {
		return filePrintPreviewCmd;
	}

	public SwingCommand getFilePrintCommand() {
		return filePrintCmd;
	}

	public SwingCommand getFileCloseCommand() {
		return fileCloseCmd;
	}

	public SwingCommand getFileQuitCommand() {
		return fileQuitCmd;
	}

	public SwingCommand getEditUndoCommand() {
		return editUndoCmd;
	}

	public SwingCommand getEditRedoCommand() {
		return editRedoCmd;
	}

	public SwingCommand getEditCopyCommand() {
		return editCopyCmd;
	}

	public SwingCommand getEditCutCommand() {
		return editCutCmd;
	}

	public SwingCommand getEditPasteCommand() {
		return editPasteCmd;
	}

	public SwingCommand getEditDeleteCommand() {
		return editDeleteCmd;
	}


	class ShowFileMenuCmd extends SwingCommandAdapter {
		public ShowFileMenuCmd() {
			super("File", null);
			setAvailable(true);
		}
	}

	class ShowEditMenuCmd extends SwingCommandAdapter {
		public ShowEditMenuCmd() {
			super("Edit", null);
			setAvailable(true);
		}
	}

	class ShowWindowMenuCmd extends SwingCommandAdapter {
		int windowCount = 0;

		public ShowWindowMenuCmd() {
			super("Windows", null);
		}

		protected void doExecute() {
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.WINDOW_OPENED:
				windowCount++;
				break;
			case MessageDispatcher.WINDOW_CLOSED:
				windowCount--;
				break;
			}
			setAvailable(windowCount != 0);
		}
	}

	class FileNewCmd extends SwingCommandAdapter {
		public FileNewCmd() {
			super("New", "Creates a new document");
			setAvailable(true);
		}

		protected void doExecute() {
			Application.getInstance().newDocument();
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}

	class FileOpenCmd extends SwingCommandAdapter {
		public FileOpenCmd() {
			super("Open...", "Opens a document");
			setAvailable(true);
		}

		protected void doExecute() {
			Application.getInstance().openDocument();
		}
		
		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.APP_INIT:
				setAvailable(Application.getFileIOManager().canLoad());
				break;
			}
		}

		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}

	class ShowFileRecentMenuCmd extends SwingCommandAdapter {
		public ShowFileRecentMenuCmd() {
			super("Open Recent", null);
			setAvailable(true);
		}
		
		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.APP_INIT:
				setAvailable(Application.getFileIOManager().canLoad());
				break;
			}
		}
	}

	class FileSaveCmd extends SwingCommandAdapter {
		boolean docReady;
		boolean docDirty;
		
		public FileSaveCmd() {
			super("Save", "Saves the current document");
		}

		protected void doExecute() {
			Application.getInstance().saveDocument();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
				docReady = (Application.getCurrentDocument() != null);
				// fall through (no break!)
			case MessageDispatcher.DOCUMENT_DIRTY:
				docDirty = docReady && Application.getCurrentDocument().isDirty();
				break;
			}
			setAvailable(Application.getFileIOManager().canSave() &&
					docReady && docDirty);
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class FileSaveAsCmd extends SwingCommandAdapter {
		public FileSaveAsCmd() {
			super("Save As...", "Saves the current document under another name");
		}

		protected void doExecute() {
			Application.getInstance().saveDocumentAs();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
				setAvailable(Application.getFileIOManager().canSave() && 
						Application.getCurrentDocument() != null);
			}
		}
	}
	
	class ShowFileExportMenuCmd extends SwingCommandAdapter {
		int numExporters = 0;
		
		public ShowFileExportMenuCmd() {
			super("Export", null);
		}

		protected void doExecute() {
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.APP_INIT:
				numExporters = Application.getFileIOManager().countExporters();
				break;
			case MessageDispatcher.DOCUMENT_SELECTED:
				setAvailable(numExporters != 0 && Application.getCurrentDocument() != null);
			}
		}
	}
	
	class FilePrintSetupCmd extends SwingCommandAdapter {
		public FilePrintSetupCmd() {
			super("Page Setup", "Configures printing parameters");
		}

		protected void doExecute() {
			Application.getInstance().showPrintSetup();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
				Document doc = Application.getCurrentDocument();
				setAvailable(doc != null && doc.getPrinter() != null);
			}
		}
	}
	
	class FilePrintPreviewCmd extends SwingCommandAdapter {
		public FilePrintPreviewCmd() {
			super("Print Preview", "Shows a preview of the printer output");
		}

		protected void doExecute() {
			Application.getInstance().showPrintPreview();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
				Document doc = Application.getCurrentDocument();
				setAvailable(doc != null && doc.getPrinter() != null);
			}
		}
	}
	
	class FilePrintCmd extends SwingCommandAdapter {
		public FilePrintCmd() {
			super("Print...", "Prints the current document");
		}

		protected void doExecute() {
			Application.getInstance().printDocument();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
				Document doc = Application.getCurrentDocument();
				setAvailable(doc != null && doc.getPrinter() != null);
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class FileCloseCmd extends SwingCommandAdapter {
		public FileCloseCmd() {
			super("Close", "Closes the current document");
		}

		protected void doExecute() {
			Application.getInstance().closeWindow();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
				setAvailable(Application.getCurrentDocument() != null);
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class FileQuitCmd extends SwingCommandAdapter {
		public FileQuitCmd() {
			super("Quit", "Quits this application");
			setAvailable(true);
		}

		protected void doExecute() {
			Application.getInstance().close();
		}

		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class EditUndoCmd extends SwingCommandAdapter {
		
		boolean documentSelected = false;
		boolean undoReady = false;
		
		public EditUndoCmd() {
			super("Undo", "Undoes the last modifications on this document");
		}

		protected void doExecute() {
			Application.getInstance().undo();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
			case MessageDispatcher.ACTION_OCCURRED:
			case MessageDispatcher.ACTION_REDONE:
			case MessageDispatcher.ACTION_UNDONE:
				setAvailable(Application.getInstance().isUndoPossible());
				break;
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class EditRedoCmd extends SwingCommandAdapter {
		
		boolean documentSelected = false;
		boolean redoReady = false;
		
		public EditRedoCmd() {
			super("Redo", "Redoes the last undone modifications");
		}

		protected void doExecute() {
			Application.getInstance().redo();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
			case MessageDispatcher.ACTION_OCCURRED:
			case MessageDispatcher.ACTION_REDONE:
			case MessageDispatcher.ACTION_UNDONE:
				setAvailable(Application.getInstance().isRedoPossible());
				break;
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, 
					java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK);
		}
	}
	
	class EditCopyCmd extends SwingCommandAdapter {
		
		boolean documentSelected = false;
		boolean selectionReady = false;
		
		public EditCopyCmd() {
			super("Copy", "Copy the current selection to the clipboard");
		}

		protected void doExecute() {
			Application.getInstance().copy();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
			case MessageDispatcher.SELECTION_CHANGED:
				setAvailable(Application.getInstance().isCopyPossible());
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class EditCutCmd extends SwingCommandAdapter {
		
		boolean documentSelected = false;
		boolean selectionReady = false;
		
		public EditCutCmd() {
			super("Cut", "Cut the current selection to the clipboard");
		}

		protected void doExecute() {
			Application.getInstance().cut();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
			case MessageDispatcher.SELECTION_CHANGED:
				setAvailable(Application.getInstance().isCutPossible());
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class EditPasteCmd extends SwingCommandAdapter {
		
		boolean documentSelected = false;
		boolean selectionReady = false;
		
		public EditPasteCmd() {
			super("Paste", "Paste the current contents of the clipboard into the document");
		}

		protected void doExecute() {
			Application.getInstance().paste();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
			case MessageDispatcher.SELECTION_CHANGED:
				setAvailable(Application.getInstance().isPastePossible());
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, 
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	
	class EditDeleteCmd extends SwingCommandAdapter {
		
		boolean documentSelected = false;
		boolean selectionReady = false;
		
		public EditDeleteCmd() {
			super("Delete", "Delete the current selection");
		}

		protected void doExecute() {
			Application.getInstance().delete();
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.DOCUMENT_SELECTED:
			case MessageDispatcher.SELECTION_CHANGED:
				setAvailable(Application.getInstance().isDeletePossible());
			}
		}
		
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 
					0);
		}
	}

	
}
