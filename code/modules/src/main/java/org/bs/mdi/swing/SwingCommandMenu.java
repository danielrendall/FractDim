/* 
 * SwingCommandMenu.java - JMenu/CommandTrigger wrapper
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.bs.mdi.*;

/**
 * A Wrapper for JMenus which adds CommandTrigger functionality.
 */
public class SwingCommandMenu extends SwingCommandTrigger implements ActionListener {
	
	JMenu menu;
	
	/**
	 * Creates a new SwingCommandMenu and associates it with the given command.
	 * @param command	the command to associate this menu with
	 */
	public SwingCommandMenu(SwingCommand command) {
		super(command);
		if (command == null)
			throw new NullPointerException();
		menu = new JMenu(Application.getResources().i18n(command.getName()));
		menu.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		command.addTrigger(this);
		menu.setEnabled(command.isAvailable());
		menu.setActionCommand(command.getName());
		menu.addActionListener(this);
		setEnabled(command.isAvailable());
	}
	
	/**
	 * Creates a new menu and associates it with the given command.
	 * This is just a convenience function for calling
	 * <code>return new SwingCommandMenu(command).getMenu()</code>
	 * @param command	the command to associated this menu with
	 * @return	the new menu
	 */
	public static JMenu createMenu(SwingCommand command) {
		return new SwingCommandMenu(command).getMenu();
	}
	
	/**
	 * Gets the menu "wrapped" behind this object.
	 * @return	the wrapped menu
	 */
	public JMenu getMenu() {
		return menu;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		menu.setEnabled(enabled);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#commandUpdated()
	 */
	public void commandUpdated() {
		menu.setText(Application.getResources().i18n(command.getName()));
		menu.setMnemonic(Application.getResources().getMnemonic(command.getName()));
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.SwingCommandTrigger#removeFrom(java.awt.Container)
	 */
	public boolean removeFrom(Container container) {
		if (container instanceof JMenu) {
			JMenu m = (JMenu)container;
			if (!m.isMenuComponent(menu)) return false;
			m.remove(menu);
		} else {
			if (menu.getParent() != container) return false;
			container.remove(menu);
		}
		return true;
	}

}
