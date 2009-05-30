/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
 *
 *  Written by: 2003 Jens Kaiser <jens.kaiser@web.de>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.jk.printpreview;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.border.*;
import org.bs.mdi.*;

public class PreviewDialog extends JDialog implements ActionListener {
    private final static double DEFAULT_ZOOM_FACTOR_STEP = 0.1;
	Preview preview;
	JMenuBar menubar;
	JTextField pageField;
	JButton firstPageButton;
	JMenuItem firstPageItem;
	JButton prevPageButton;
	JMenuItem prevPageItem;
	JButton nextPageButton;
	JMenuItem nextPageItem;
	JButton lastPageButton;
	JMenuItem lastPageItem;
	Pageable pageable;

    public PreviewDialog(String title, JFrame owner, Pageable pageable, double zoom) {
        super(owner, title, true);
        
        JButton print, cancel;
        
        this.pageable = pageable;        
        preview = new Preview(pageable, zoom);
        JPanel panel = new JPanel(new BorderLayout());
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalGlue());        
        box.add(preview);
		box.add(Box.createHorizontalGlue());
		box.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.add(box);        
        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
                
        JToolBar toolbar = new JToolBar();
        //toolbar.setRollover(true);
		toolbar.add(cancel = createButton("Close", true));
		toolbar.add(print = createButton("Print...", true));				
		getRootPane().setDefaultButton(cancel);
		toolbar.addSeparator();
		toolbar.add(firstPageButton = createButton("First Page"));
        toolbar.add(prevPageButton = createButton("Previous Page"));
		pageField = new JTextField("1");
		pageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					preview.setPageIndex(Integer.parseInt(pageField.getText()) - 1);
				} catch (NumberFormatException e1) {
				}
				previewUpdated();
			}
		});
		pageField.setMaximumSize(new Dimension(48, 32));
        toolbar.add(pageField);
        toolbar.add(nextPageButton = createButton("Next Page"));
		toolbar.add(lastPageButton = createButton("Last Page"));		
		toolbar.addSeparator();
        toolbar.add(createButton("Zoom In")); 
        toolbar.add(createButton("Zoom Out")); 
        toolbar.addSeparator();
		getContentPane().add(toolbar, BorderLayout.NORTH);
		constructMenu();
		setJMenuBar(menubar);
        addWindowListener(new MyWindowAdapter());
        previewUpdated();     
    }
    
    public PreviewDialog(String title, JFrame owner, Pageable pageable) {
        this(title, owner, pageable, 0.0);
    }

    public PreviewDialog(String title, JFrame owner, Printable printable, PageFormat format, int pages, double zoom) {
        this(title, owner, new MyPageable(printable, format, pages), zoom);
    }

    public PreviewDialog(String title, JFrame owner, Printable printable, PageFormat format, int pages) {
        this(title, owner, printable, format, pages, 0.0);
    }
    
    private static class MyPageable implements Pageable {
        public MyPageable(Printable printable, PageFormat format, int pages) {
            this.printable = printable;
            this.format = format;
            this.pages = pages;
        }
        
        public int getNumberOfPages() { 
            return pages; 
        }
        
        public Printable getPrintable(int index) {
            if (index >= pages) throw new IndexOutOfBoundsException();
            return printable;
        }
        
        public PageFormat getPageFormat(int index) {
            if (index >= pages) throw new IndexOutOfBoundsException();
            return format;
        }
        
        private Printable printable;
        private PageFormat format;
        private int pages;
    }
    
    private JButton createButton(String key) {
        return createButton(key, false);
    }

    private JButton createButton(String key, boolean withText) {
		String label = Application.getResources().i18n(key);
		int mnemonic = Application.getResources().getMnemonic(key);
		Icon icon = Application.getResources().getIcon(key, Resources.MENU_ICON);
				        
		JButton result = new JButton();
		result.setIcon(icon);
		result.setMnemonic(mnemonic);
		if (withText) 
			result.setText(label);		
		else 
			result.setToolTipText(label);
		result.setActionCommand(key);
		result.addActionListener(this);
        
		return result;
    }
    
    private void constructMenu() {
    	menubar = new JMenuBar();
    	JMenu previewMenu = new JMenu(Application.getResources().i18n("Preview"));
    	previewMenu.setMnemonic(Application.getResources().getMnemonic("Preview"));
    	previewMenu.add(createMenuItem("Print..."));
    	previewMenu.addSeparator();
		previewMenu.add(createMenuItem("Close"));
    	menubar.add(previewMenu);
    	JMenu pageMenu = new JMenu(Application.getResources().i18n("Page"));
		pageMenu.setMnemonic(Application.getResources().getMnemonic("Page"));
    	pageMenu.add(firstPageItem = createMenuItem("First Page"));
		pageMenu.add(prevPageItem = createMenuItem("Previous Page"));
		pageMenu.add(nextPageItem = createMenuItem("Next Page"));
		pageMenu.add(lastPageItem = createMenuItem("Last Page"));
		menubar.add(pageMenu);				
		JMenu viewMenu = new JMenu(Application.getResources().i18n("View"));
		viewMenu.setMnemonic(Application.getResources().getMnemonic("View"));
		viewMenu.add(createMenuItem("Zoom In"));
		viewMenu.add(createMenuItem("Zoom Out"));
		menubar.add(viewMenu);		
    }
    
    private JMenuItem createMenuItem(String key) {
    	JMenuItem item = new JMenuItem(Application.getResources().i18n(key));
    	item.setMnemonic(Application.getResources().getMnemonic(key));
		item.setIcon(Application.getResources().getIcon(key, Resources.MENU_ICON));
		item.setActionCommand(key);
		item.addActionListener(this);
		return item;
    }
    
    protected void previewUpdated() {
    	int page = preview.getPageIndex();
    	int max = preview.getMaxPageIndex();
    	pageField.setText("" + (page+1));
    	firstPageButton.setEnabled(page > 0);    
		firstPageItem.setEnabled(page > 0);
		prevPageButton.setEnabled(page > 0);
		prevPageItem.setEnabled(page > 0);
		nextPageButton.setEnabled(page < max);
		nextPageItem.setEnabled(page < max);
		lastPageButton.setEnabled(page < max);
		lastPageItem.setEnabled(page < max);
    }
    
    public void actionPerformed(ActionEvent e) {
    	String cmd = e.getActionCommand();		
    	if (cmd.equals("Print...")) {
			Application.getInstance().printDocument(); return;
    	}
    	if (cmd.equals("Close")) {
    		dispose(); return;
    	}
    	if (cmd.equals("First Page")) {
			preview.setPageIndex(0); previewUpdated(); return;
    	}
		if (cmd.equals("Previous Page")) {
			preview.movePageIndex(-1); previewUpdated(); return;
		}
		if (cmd.equals("Next Page")) {
			preview.movePageIndex(1); previewUpdated(); return;
		}
		if (cmd.equals("Last Page")) {
			preview.setPageIndex(preview.getMaxPageIndex()); previewUpdated(); return;
		}		
		if (cmd.equals("Zoom In")) {
			preview.changeZoom(DEFAULT_ZOOM_FACTOR_STEP); return;
		}
		if (cmd.equals("Zoom Out")) {
			preview.changeZoom(-DEFAULT_ZOOM_FACTOR_STEP); return;
		}
    }
    
	class MyWindowAdapter extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			preview.center(); 
		}
	}
      
}
