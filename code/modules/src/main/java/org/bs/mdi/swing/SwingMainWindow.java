/* 
 * SwingMainWindow.java - a MainWindow implementation for Swing
 * 
 * This file is part the Abstract MDI Framework.
 * Copyright (c) 2004-2005 Bernhard Stiftner
 * Parts Copyright (c) 2005 Eric Price
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

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.bs.mdi.*;
import org.bs.mdi.DataPageable.PrintException;
import org.jk.printpreview.PreviewDialog;
import org.apache.log4j.Logger;

// DR - refactored the menu creation to make it more convenient for subclasses to
// pick and choose what menus they want

/**
 * An implementation of a {@link MainWindow} using Swing technology.
 */
public class SwingMainWindow extends JFrame implements MainWindow, MessageProcessor {

    private static final Logger log = Logger.getLogger(SwingMainWindow.class);

    // keys for sensible default menus
    public final static String FILE_MENU = "FILE";
    public final static String EDIT_MENU = "EDIT";

	protected Application application;	
	protected PrinterJob printerJob = PrinterJob.getPrinterJob();
	protected DataPageable docPrinter = new DataPageable();
	protected SwingDefaultCommands commands;

	protected JMenu windowMenu;
	protected SwingStatusBar statusBar;
	protected SwingProgressMonitor progressMonitor;
	protected MDIWindowManager windowManager;
	protected JComponent desktop;

    // See code at the bottom of the file
    private int numSpecialCommands = 0;


    protected final Map<String, JMenu> menus = new HashMap<String, JMenu>();

	/**
	 * Creates a new main window and adds a menu and a toolbar to it.
	 * This constructor is equivalent to <code>SwingMainWindow(true, true)</code>.
	 */
	public SwingMainWindow() {
		this(true, true, true);
	}
	
	
	/**
	 * Creates a new main window.
	 * @param addMenu	true if the main menu should be added to this window
	 * @param addStatusBar	true if a status bar should be added to the window
     * @param addToolBar true if a tool bar should be added to the window
	 */
	public SwingMainWindow(boolean addMenu, boolean addStatusBar, boolean addToolBar) {
		application = Application.getInstance();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new MyWindowListener());
		getContentPane().setLayout(new BorderLayout());
		Application.getMessageDispatcher().registerProcessor(this);
		
		// create menus
		if (addMenu) {
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			commands = createCommands();
            addDefaultMenus(menuBar);
			windowMenu = SwingCommandMenu.createMenu(commands.getShowWindowMenuCommand());
			windowMenu.addSeparator();
			menuBar.add(windowMenu);
		}
		
		// create status bar	
		if (addStatusBar) {
			setStatusBar(new SwingStatusBar());
		}
		
		// create toolbar
		if (addToolBar) {
			JToolBar toolBar = new JToolBar();
			addToolBarItems(toolBar);
			getContentPane().add(toolBar, BorderLayout.NORTH);
		}
		
		setWindowManager(new TraditionalMDIWindowManager()); //TODO: replace!
		
		// set last saved bounds
		Rectangle rect = Application.getPreferences().getRectangle("mdi.mainwindow.bounds");
		if (rect != null) {
			setBounds(rect);
		} else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension defaultSize = getDefaultSize();
            setBounds(new Rectangle((screenSize.width - defaultSize.width) / 2,
                                    (screenSize.height - defaultSize.height) / 2,
                                    defaultSize.width,
                                    defaultSize.height));
		}
		// TODO: restore maximized status (see below in class MyWIndowListener)
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Window#close()
	 */
	public boolean close() {
		WindowEvent evt = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		processWindowEvent(evt);
		return true;
	}
	
	/* Set the text to be displayed in a status bar (or another widget).
	 * This implementation displays the given text on the window's
	 * default status bar. If there is no status bar, this method
	 * does exactly nothing. 
	 * @see org.bs.mdi.MainWindow#setStatus(java.lang.String)
	 */
	public void setStatus(String status) {
		if (statusBar != null) {
			statusBar.getStatusLabel().setText(status);
			statusBar.getStatusLabel().paintImmediately(0, 0, 
					statusBar.getStatusLabel().getSize().width, 
					statusBar.getStatusLabel().getSize().height);
		}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#createDocumentWindow(org.bs.mdi.Document)
	 */
	public DocumentWindow createDocumentWindow() {
		return windowManager.createDocumentWindow();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#getDocumentWindows()
	 */
	public java.util.List getDocumentWindows() {
		return windowManager.getWindows();
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#showFileOpenDialog(org.bs.mdi.FileFormat[])
	 */
	public String showFileOpenDialog(FileFormat[] formats) {
		JFileChooser chooser = new JFileChooser();
		if (formats != null && formats.length != 0) {
			FileFormatFilter firstFilter = null;
			for (int i=0; i<formats.length; i++) {
				FileFormatFilter filter = new FileFormatFilter(formats[i]);
				if (i==0) firstFilter = filter;
				chooser.addChoosableFileFilter(filter);
			}		
			chooser.setFileFilter(firstFilter);	
		}		
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getPath();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#showFileSaveDialog(org.bs.mdi.FileFormat[], java.lang.String)
	 */
	public String showFileSaveDialog(
		FileFormat[] formats,
		String initialName) {
		JFileChooser chooser = new JFileChooser();
		if (formats != null && formats.length != 0) {
			FileFormatFilter firstFilter = null;
			boolean unsupportedExtension = true;
			for (int i = 0; i < formats.length; i++) {
				FileFormatFilter filter = new FileFormatFilter(formats[i]);
				chooser.addChoosableFileFilter(filter);
				if (i == 0) firstFilter = filter;					
				if (initialName != null && filter.accept(new File(initialName))) {
					unsupportedExtension = false;
				}
			}
			chooser.setFileFilter(firstFilter);
			if (unsupportedExtension && initialName != null) {
				// try to add missing file suffixes to the initial filename
				String extension = firstFilter.getFormat().getExtensions()[0];
				if (extension.startsWith(".")) extension = extension.substring(1);
				initialName = initialName + "." + extension;
			}			
		}
		if (initialName != null)
			chooser.setSelectedFile(new File(initialName));
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getPath();
			try {
				// try to add missing file suffixes to the chosen filename
				boolean supportedExtension = false;
				FileFormat selectedFormat = ((FileFormatFilter)chooser.getFileFilter()).getFormat();
				for (int i=0; i<selectedFormat.getExtensions().length; i++) {
					if (path.endsWith(selectedFormat.getExtensions()[i]))
						supportedExtension = true;
				}			
				if (!supportedExtension) {
					String extension = selectedFormat.getExtensions()[0];
					if (extension.startsWith(".")) extension = extension.substring(1);
					path = path + "." + extension;
				}
			} catch (RuntimeException e) {}
			return path;
		}
		return null;
	}


    /**
     * Override this to add a different set of menus to the menubar, or override the convenience
     * methods to change the contents of the menus.
     * @param menuBar Menubar to which menus should be added
     */
    protected void addDefaultMenus(JMenuBar menuBar) {
        addFileMenu(menuBar);
        addEditMenu(menuBar);
    }

    /**
     * Convenience method to add a default file menu. To change the items that are added, override
     * #addFileMenuItems
     * @param menuBar Menubar to which the menu is added
     */
    protected final void addFileMenu(JMenuBar menuBar) {
        JMenu fileMenu = SwingCommandMenu.createMenu(commands.getShowFileMenuCommand());
        addFileMenuItems(fileMenu);
        menus.put(FILE_MENU, fileMenu);
        menuBar.add(fileMenu);
    }

    /**
     * Convenience method to add a default edit menu. To change the items that are added, override
     * #addEditMenuItems
     * @param menuBar Menubar to which the menu is added
     */
    protected final void addEditMenu(JMenuBar menuBar) {
        JMenu editMenu = SwingCommandMenu.createMenu(commands.getShowEditMenuCommand());
        addEditMenuItems(editMenu);
        menus.put(EDIT_MENU, editMenu);
        menuBar.add(editMenu);
    }



	protected Icon getMessageIcon(int type) {
		Icon icon;
		switch (type) {
			case MainWindow.QUESTION:
				icon = Application.getResources().getIcon("Question", Resources.MESSAGEBOX_ICON);
				return (icon != null) ? icon : UIManager.getIcon("OptionPane.questionIcon");
			case MainWindow.INFO:
				icon = Application.getResources().getIcon("Information", Resources.MESSAGEBOX_ICON);
				return (icon != null) ? icon : UIManager.getIcon("OptionPane.informationIcon");
			case MainWindow.WARNING:
				icon = Application.getResources().getIcon("Warning", Resources.MESSAGEBOX_ICON);
				return (icon != null) ? icon : UIManager.getIcon("OptionPane.warningIcon");
			case MainWindow.ERROR:
				icon = Application.getResources().getIcon("Error", Resources.MESSAGEBOX_ICON);
				return (icon != null) ? icon : UIManager.getIcon("OptionPane.errorIcon");
		}
		return null;
	}

    /**
     * Override this to set the preferred initial width and height for the application.
     * @return
     */
    protected Dimension getDefaultSize() {
        return new Dimension(320, 240);
    }

    private class HintedButton extends JButton {
		public JOptionPane pane;
		public HintedButton(String s) { super(s); };
	}
	
	private class DialogActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			HintedButton b = (HintedButton)e.getSource();
			b.pane.setValue(b);
		}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#showMessage(int, org.bs.mdi.DocumentWindow, java.lang.String)
	 */
	public void showMessage(int type, org.bs.mdi.Window window, String message) {
		Component c = ((window != null) ? (Component)window : null);
		String title = ((window != null) ? window.getTitle() : Application.getInstance().getName());
		String prefix = "";
		switch (type) {
			case MainWindow.WARNING:
				prefix = Application.tr("Warning!") + "\n"; break;
			case MainWindow.ERROR:
				prefix = Application.tr("Error:") + "\n"; break;
		}
		Icon icon = getMessageIcon(type);
		JOptionPane pane = new JOptionPane(prefix + message);
		HintedButton okButton = new HintedButton(Application.getResources().i18n("OK"));
		okButton.setMnemonic(Application.getResources().getMnemonic("OK"));
		okButton.setIcon(Application.getResources().getIcon("OK", Resources.BUTTON_ICON));
		okButton.pane = pane;
		okButton.addActionListener(new DialogActionListener());
		HintedButton buttons[] = { okButton };

		pane.setIcon(icon);
		pane.setOptions(buttons);
		pane.setInitialValue(okButton);
		JDialog dialog = pane.createDialog(c, title);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.show();
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#showDialog(int, org.bs.mdi.DocumentWindow, java.lang.String, java.lang.String[], int)
	 */
	public int showDialog(int type, org.bs.mdi.Window window, String message, String[] choices, int defaultChoice) {
		DialogActionListener btnListener = new DialogActionListener();
		Component c = ((window != null) ? (Component)window : null);
		String title = ((window != null) ? window.getTitle() : Application.getInstance().getName());
		Icon icon = getMessageIcon(type);
		JOptionPane pane = new JOptionPane(message);
		HintedButton buttons[] = new HintedButton[choices.length];
		for (int i=0; i<choices.length; i++) {
			buttons[i] = new HintedButton(Application.getResources().i18n(choices[i]));
			buttons[i].setMnemonic(Application.getResources().getMnemonic(choices[i]));
			buttons[i].pane = pane;
			buttons[i].setIcon(Application.getResources().getIcon(choices[i], Resources.BUTTON_ICON));
			buttons[i].addActionListener(btnListener);
		}

		pane.setIcon(icon);
		pane.setOptions(buttons);
		pane.setInitialValue(buttons[defaultChoice]);
		JDialog dialog = pane.createDialog(c, title);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.show();
		Object selectedValue = pane.getValue();
		if(selectedValue == null)
		  return -1;
		//If there is not an array of option buttons:
		if(choices == null) {
		  if(selectedValue instanceof Integer)
			 return ((Integer)selectedValue).intValue();
		  return -1;
		}
		//If there is an array of option buttons:
		for(int counter = 0, maxCounter = buttons.length;
		   counter < maxCounter; counter++) {
		   if(buttons[counter].equals(selectedValue))
		   return counter;
		}
		return -1;				
	}
	
	/**
	 * Shows a print setup dialog. Using this dialog, the user
	 * can set the page format, orientation and margins to be
	 * used by further print jobs.
	 */
	public void showPrintSetup() {
		PrinterJob job = PrinterJob.getPrinterJob();
		docPrinter.setPageFormat(job.pageDialog(docPrinter.getPageFormat()));
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#showPrintPreview()
	 */
	public void showPrintPreview() {
		try {
			docPrinter.setData(Application.getCurrentDocument().getData());
			PreviewDialog dialog = new PreviewDialog(Application.getResources().i18n("Print Preview"), this, docPrinter);
			dialog.setBounds(getBounds());
			dialog.show();
		} catch (PrintException e) {
			showMessage(MainWindow.ERROR, this, 
					Application.tr("The print preview could not be created.")+"\n"+
					Application.tr("Original error message:")+"\n"+
					e.getLocalizedMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#printDocument()
	 */
	public void printDocument() {
		try {
			PrinterJob job = PrinterJob.getPrinterJob();
			docPrinter.setData(Application.getCurrentDocument().getData());
			job.setPageable(docPrinter);
			if (job.printDialog())
				try {
					job.print();
				} catch (PrinterException pe) {
					showMessage(MainWindow.ERROR, this, 
							Application.tr("The print job could not be processed.")+"\n"+
							Application.tr("Original error message:")+"\n"+
							pe.getLocalizedMessage());
				}
		} catch (PrintException e) {
			showMessage(MainWindow.ERROR, this, 
					Application.tr("The print job could not be processed.")+"\n"+
					Application.tr("Original error message:")+"\n"+
					e.getLocalizedMessage());
		} catch (HeadlessException e) {
			// I don't think we have to react on this...
			// Who would run an mdi framework in a headless environment?
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	

	/* (non-Javadoc)
	 * @see org.bs.mdi.MessageProcessor#processMessage(java.lang.Object, int, java.lang.Object)
	 */
	public void processMessage(Object source, int type, Object argument) {
		SwingDocumentWindow win;
		switch (type) {
		case MessageDispatcher.DOCUMENT_SELECTED:
			windowManager.windowSelected(getDesktop(), Application.getCurrentWindow());
			break;
		case MessageDispatcher.WINDOW_CREATED:
			// the window manager should take care of the new document window
			win = (SwingDocumentWindow)argument;
			windowManager.addWindow(getDesktop(), win);
			break;
		case MessageDispatcher.WINDOW_OPENED:
			// add window to window menu
			if (windowMenu == null) return;
			win = (SwingDocumentWindow)argument;
			AbstractButton item = SwingCommandButton.createMenuItem(win.getWindowCommand());
			windowMenu.add(item);
			break;
		case MessageDispatcher.WINDOW_CLOSED:
			// remove window from window menu
			if (windowMenu == null) return;
			win = (SwingDocumentWindow)argument;
			win.getWindowCommand().removeFrom(windowMenu);
			// notify the window manager that this window was removed
			windowManager.windowRemoved(getDesktop(), win);
			break;
		}
	}
	
	/**
	 * Gets the current set of commands.
	 * 
	 * @return the set of commands
	 */
	public SwingDefaultCommands getCommands() {
		return commands;
	}
	
	/**
	 * Returns the desktop component.
	 * @return	the desktop component
	 */
	public JComponent getDesktop() {
		return desktop;
	}
	
	/**
	 * Sets the desktop component.
	 * @param c	the desktop component
	 */
	protected void setDesktop(JComponent c) {
		if (desktop != null) {
			getContentPane().remove(desktop);
		}
		desktop = c;
		getContentPane().add(desktop, BorderLayout.CENTER);
	}
	
	/**
	 * Gets the current document window manager. 
	 * @return	the current window manager
	 */
	public MDIWindowManager getWindowManager() {
		return windowManager;
	}
	
	/**
	 * Sets another document window manager to be used.
	 * @param manager	the new window manager
	 */
	public void setWindowManager(MDIWindowManager manager) {
		
		// remove old document windows
		if (windowManager != null) {
			Iterator iter = windowManager.getWindows().iterator();
			while (iter.hasNext()) {
				DocumentWindow w = (DocumentWindow)iter.next();
				windowManager.windowRemoved(getDesktop(), w);
			}
		}
		
		windowManager = manager;
		setDesktop(windowManager.createDesktopComponent());
		
		// "convert" already existing document windows
		Document documents[] = Application.getDocuments();
		for (int i=0; i<documents.length; i++) {
			Document doc = documents[i];
			RootView views[] = doc.getViews();
			for (int j=0; j<views.length; j++) {
				RootView v = views[j];
				DocumentWindow win = createDocumentWindow();
				win.setView(v);
				win.setTitle(doc.getFilename());
				win.setVisible(true);
			}
		}
		
		// update special commands in the window menu
		removeAllSpecialWindowCommands();
		addSpecialWindowCommands(windowManager.getSpecialCommands());
	}


    // DR - Accessor methods for menus commented out until it's clear whether or not they're
    // actually useful. Note that they won't work in their current form anyway because
    // the items in question have either been converted to local fields or stored in the
    // menus Map

	/**
	 * Returns the toolbar.
	 * @return	the tool bar
	 */
//	public JToolBar getToolBar() {
//		return toolBar;
//	}
	
	/**
	 * Returns the file menu.
	 * @return	the file menu
	 */
//	public JMenu getFileMenu() {
//		return fileMenu;
//	}
	
	/**
	 * Returns the edit menu.
	 * @return	the edit menu
	 */
//	public JMenu getEditMenu() {
//		return editMenu;
//	}
	
	/**
	 * Returns the window menu.
	 * Warning! This items in this menu are automatically managed by the MDI framework.
	 * Don't mess around with this menu unless you know what you are doing!
	 * @return	the window menu
	 */
//	public JMenu getWindowMenu() {
//		return windowMenu;
//	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#setBusy(boolean)
	 */
	public void setBusy(boolean busy) {
		if (busy) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
		} else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	/**
	 * Gets the status bar.
	 * @return	the status bar
	 */
	public SwingStatusBar getStatusBar() {
		return statusBar;
	}
	
	/**
	 * Sets the status bar.
	 * The status bar is added to the bottom of the main window.
	 * @param statusBar
	 */
	public void setStatusBar(SwingStatusBar statusBar) {
		if (this.statusBar != null) {
			getContentPane().remove(this.statusBar);
		}
		this.statusBar = statusBar;
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		progressMonitor = new SwingProgressMonitor(statusBar.getProgressBar());
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.MainWindow#getProgressMonitor()
	 */
	public org.bs.mdi.ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}
	
	/**
	 * Creates a new set of commands to be used by the application.
	 * You can overwrite this method to make the application use your own
	 * set of commands instead.
	 * @return	the set of commands to be used
	 */
	protected SwingDefaultCommands createCommands() {
		return new SwingDefaultCommands();
	}
	
	/**
	 * Adds a default set of items to the file menu - override this if necessary (you will probably
     * want to copy the method and tweak it). Note the convenience methods for adding the recent
     * files menu item and the file export menu item.
     * @param fileMenu Menu to which file menu items should be added
     */
	protected void addFileMenuItems(JMenu fileMenu) {
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileNewCommand()));
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileOpenCommand()));

        addRecentFilesMenu(fileMenu);

		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileSaveCommand()));
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileSaveAsCommand()));

        addFileExportMenuIfRequired(fileMenu);
        fileMenu.addSeparator();
		
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFilePrintSetupCommand()));
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFilePrintPreviewCommand()));
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFilePrintCommand()));
		fileMenu.addSeparator();
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileCloseCommand()));
		fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileQuitCommand()));
	}

    /**
     * Convenience method which you may want to call if overriding #addFileMenuItems which adds
     * a recent files menu and a listener to disable it if there are none.
     * @param fileMenu Menu to which the export menu should be added
     */
    protected void addRecentFilesMenu(JMenu fileMenu) {
        final JMenu recentFilesMenu = SwingCommandMenu.createMenu(commands.getShowFileRecentMenuCommand());
        recentFilesMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        String filename = event.getActionCommand();
                        Application.getInstance().openDocument(filename);
                    }
                };
                String[] files = Application.getRecentFiles().getList();
                recentFilesMenu.removeAll();
                for (int i=0; i<files.length; i++) {
                    JMenuItem item = new JMenuItem(files[i]);
                    item.addActionListener(listener);
                    recentFilesMenu.add(item);
                }
            }

            public void menuDeselected(MenuEvent e) {}

            public void menuCanceled(MenuEvent e) {}
        });
        RecentFiles rf = Application.getRecentFiles();
        recentFilesMenu.setEnabled(rf.count() > 0);
        rf.addRecentFilesListener(new RecentFiles.RecentFilesListener() {
            public void recentFileAdded(String fileName, int count) {
                recentFilesMenu.setEnabled(count > 0);
            }

            public void recentFileRemoved(String fileName, int count) {
                recentFilesMenu.setEnabled(count > 0);
            }
        });

        fileMenu.add(recentFilesMenu);
    }

    /**
     * Convenience method which you may want to call if overriding #addFileMenuItems which adds
     * an export menu if some exporters have been defined for the application.
     * @param fileMenu Menu to which the export menu should be added
     */

    protected void addFileExportMenuIfRequired(JMenu fileMenu) {
        if (Application.getFileIOManager().getFileExporters().length != 0) {
            JMenu fileExportMenu = SwingCommandMenu.createMenu(commands.getShowFileExportMenuCommand());
            FileExporter exporters[] = Application.getFileIOManager().getFileExporters();
            for (int i=0; i<exporters.length; i++) {
                ExportMenuItem item = new ExportMenuItem(exporters[i]);
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ExportMenuItem item = (ExportMenuItem)e.getSource();
                        application.exportDocument(item.getExporter());
                    }});
                fileExportMenu.add(item);
            }
            fileMenu.addSeparator();
			fileMenu.add(fileExportMenu);
		}
    }

    /**
     * Adds a default set of items to the edit menu - override this if necessary (you will probably
     * want to copy the method and tweak it).
     * @param editMenu Menu to which edit menu items should be added
     */
	protected void addEditMenuItems(JMenu editMenu) {
		editMenu.add(SwingCommandButton.createMenuItem(commands.getEditUndoCommand()));
		editMenu.add(SwingCommandButton.createMenuItem(commands.getEditRedoCommand()));
		editMenu.addSeparator();
		editMenu.add(SwingCommandButton.createMenuItem(commands.getEditCutCommand()));
		editMenu.add(SwingCommandButton.createMenuItem(commands.getEditCopyCommand()));
		editMenu.add(SwingCommandButton.createMenuItem(commands.getEditPasteCommand()));
		editMenu.addSeparator();
		editMenu.add(SwingCommandButton.createMenuItem(commands.getEditDeleteCommand()));
	}
	
	/**
     * Adds a default set of items to the toolbar - override this if necessary (you will probably
     * want to copy the method and tweak it).
     * @param toolBar Toolbar to which items should be added
     */
	protected void addToolBarItems(JToolBar toolBar) {
		toolBar.add(SwingCommandButton.createToolButton(commands.getFileNewCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getFileOpenCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getFileSaveCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getFileSaveAsCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getFileCloseCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getFileQuitCommand()));
		toolBar.addSeparator();
		toolBar.add(SwingCommandButton.createToolButton(commands.getFilePrintCommand()));
		toolBar.addSeparator();
		toolBar.add(SwingCommandButton.createToolButton(commands.getEditUndoCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getEditRedoCommand()));
		toolBar.addSeparator();
		toolBar.add(SwingCommandButton.createToolButton(commands.getEditCutCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getEditCopyCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getEditPasteCommand()));
		toolBar.add(SwingCommandButton.createToolButton(commands.getEditDeleteCommand()));
	}
	
	
	
	/*
	 * The following code inserts or removes so-called "special commands" to/from
	 * the window menu. "Special commands" (like "tile" and "cascade") can be 
	 * defined by the windowmanager and should be accessible via the GUI.
	 */
	
	protected void addSpecialWindowCommands(SwingCommand commands[]) {
		if (commands != null) {
			for (int i=0; i<commands.length; i++) {
				AbstractButton item = SwingCommandButton.createMenuItem(commands[i]);
				windowMenu.add(item, numSpecialCommands);
				numSpecialCommands++;	
			}
		}
		windowMenu.addSeparator();
	}

	protected void removeAllSpecialWindowCommands() {
		windowMenu.remove(0);
		while (numSpecialCommands > 0) {
			windowMenu.remove(0);
			numSpecialCommands--;
		}
	}


    class ExportMenuItem extends JMenuItem {
		FileExporter exporter;
		
		public ExportMenuItem(FileExporter exporter) {
			super(exporter.getDescription());
			this.exporter = exporter;
		}
		
		public FileExporter getExporter() {
			return exporter;
		}
	}	

	class MyWindowListener extends WindowAdapter {
		
		public void windowClosing(WindowEvent e) {
			int numViews;
			Document[] docs = Application.getDocuments();
			for (int i = 0; i < docs.length; i++) {				
				RootView[] views = docs[i].getViews();
				for (int j=0; j < views.length; j++) {
					try {
						if (views[j].getWindow().close() == false) return;
					} catch (RuntimeException ignored) {}
				}
			}
			Application.getPreferences().setRectangle("mdi.mainwindow.bounds", getBounds());
			// TODO: save if the window is maximized or not!
			//Application.getPreferences().setBoolean("mdi.mainwindow.maximized", ???);
			application.exit();
		}
		
		public void windowClosed(WindowEvent e) {
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.APP_QUIT, null);
		}
		
		public void windowOpened(WindowEvent e) {
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.APP_INIT, null);		
		}
		
	}

}

