/* 
 * SwingCommandButton.java - AbstractButton/CommandTrigger wrapper
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
import javax.swing.border.*;
import org.bs.mdi.*;

/**
 * A Wrapper for AbstractButtons which adds CommandTrigger functionality.
 */
public class SwingCommandButton extends SwingCommandTrigger implements ActionListener {
	
	AbstractButton button;
	boolean isToggleButton;
	int type;
	
	protected final static int TYPE_CUSTOM = 0;
	protected final static int TYPE_TOOLBUTTON = 1;
	protected final static int TYPE_TOOLTOGGLEBUTTON = 2;
	protected final static int TYPE_BUTTON = 3;
	protected final static int TYPE_MENUITEM = 4;
	protected final static int TYPE_CHECKBOX = 5;
	protected final static int TYPE_RADIOBUTTON = 6;
	
	/**
	 * Creates a new custom SwingCommandButton based on an AbstractButton.
	 * Use this constructor use this Wrapper with custom components.
	 * @param command	the command to be associated with the SwingCommandButton
	 * @param button	the component to be wrapped
	 */
	public SwingCommandButton(SwingCommand command, AbstractButton button) {
		this(command, button, TYPE_CUSTOM);
	}
	
	/**
	 * This constructor is for framework internal use only.
	 * @param command
	 * @param button
	 * @param type
	 */
	protected SwingCommandButton(SwingCommand command, AbstractButton button, int type) {
		super(command);
		if (command == null || button == null)
			throw new NullPointerException();
		this.button = button;
		this.type = type;
		command.addTrigger(this);
		button.setActionCommand(command.getName());
		button.addActionListener(this);
		isToggleButton = (button instanceof JToggleButton);
		setEnabled(command.isAvailable());
	}
	
	/**
	 * Creates a new toolbar button and associates it with the given command.
	 * @param command	the command to be associated with the button
	 * @return	a button linked with the given command
	 */
	public static AbstractButton createToolButton(SwingCommand command) {
		JButton button = new JButton(command.getIcon(Resources.TOOLBAR_ICON));
		//button.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		button.setToolTipText(command.getLocalizedName());
		button.setPreferredSize(new Dimension(Resources.TOOLBAR_ICON + 11, 
				Resources.TOOLBAR_ICON + 11));
		button.setMinimumSize(button.getPreferredSize());
		button.setMaximumSize(button.getPreferredSize());
		button.setSize(button.getPreferredSize());
		button.setFocusable(false);
		//button.setRolloverEnabled(true);
		button.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
				new CompoundBorder(new EtchedBorder(), new EmptyBorder(3, 3, 3,
						3))));					
		return new SwingCommandButton(command, button, TYPE_TOOLBUTTON).getButton();
	}
	
	/**
	 * Creates a new toolbar togglebutton and associates it with the given command.
	 * @param command	the command to be associated with the button
	 * @return	a togglebutton linked with the given command
	 */
	public static AbstractButton createToolToggleButton(SwingCommand command) {
		JToggleButton button = new JToggleButton(command.getIcon(Resources.TOOLBAR_ICON));
		//button.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		button.setToolTipText(command.getLocalizedName());
		button.setPreferredSize(new Dimension(Resources.TOOLBAR_ICON + 11, 
				Resources.TOOLBAR_ICON + 11));
		button.setMinimumSize(button.getPreferredSize());
		button.setMaximumSize(button.getPreferredSize());
		button.setSize(button.getPreferredSize());
		button.setFocusable(false);
		//button.setRolloverEnabled(true);
		button.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
				new CompoundBorder(new EtchedBorder(), new EmptyBorder(3, 3, 3,
						3))));					
		return new SwingCommandButton(command, button, TYPE_TOOLTOGGLEBUTTON).getButton();
	}
	
	/**
	 * Creates a new standard button and associates it with the given command.
	 * @param command	the command to be associated with the button
	 * @return	a button linked with the given command
	 */
	public static AbstractButton createButton(SwingCommand command) {
		JButton button = new JButton(Application.getResources().i18n(command.getName()),
				command.getIcon(Resources.TOOLBAR_ICON));
		button.setHorizontalTextPosition(SwingConstants.LEADING);
		//button.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		button.setToolTipText(command.getLocalizedDescription());
		return new SwingCommandButton(command, button, TYPE_BUTTON).getButton();
	}
	
	/**
	 * Creates a new menu item and associates it with the given command.
	 * @param command	the command to be associated with the menu item
	 * @return	a menu item linked with the given command
	 */
	public static AbstractButton createMenuItem(SwingCommand command) {
		JMenuItem item = new JMenuItem(Application.getResources().i18n(command.getName()), 
				command.getIcon(Resources.MENU_ICON));
		item.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		if (command.getAccelerator() != null) {
			item.setAccelerator(command.getAccelerator());
		}
		item.setToolTipText(command.getLocalizedDescription());
		return new SwingCommandButton(command, item, TYPE_MENUITEM).getButton();
	}
	
	/**
	 * Creates a new checkbox and associates it with the given command.
	 * @param command	the command to be associated with the checkbox
	 * @return	a checkbox linked with the given command
	 */
	public static AbstractButton createCheckBox(SwingCommand command) {
		JCheckBox checker = new JCheckBox(Application.getResources().i18n(command.getName()));
		//checker.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		checker.setToolTipText(command.getLocalizedDescription());
		return new SwingCommandButton(command, checker, TYPE_CHECKBOX).getButton();
	}
	
	/**
	 * Creates a new radio button and associates it with the given command.
	 * @param command	the command to be associated with the radio button
	 * @return	a radio button linked with the given command
	 */
	public static AbstractButton createRadioButton(SwingCommand command) {
		JRadioButton radio = new JRadioButton(Application.getResources().i18n(command.getName()));
		//radio.setMnemonic(Application.getResources().getMnemonic(command.getName()));
		radio.setToolTipText(command.getLocalizedDescription());
		return new SwingCommandButton(command, radio, TYPE_RADIOBUTTON).getButton();
	}
	
	/**
	 * Gets the button "wrapped" behind this object.
	 * @return	the wrapped button
	 */
	public AbstractButton getButton() {
		return button;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#commandExecuted(org.bs.mdi.CommandTrigger)
	 */
	public void commandExecuted(CommandTrigger trigger) {
		if (trigger != this && isToggleButton) {
			SwingCommandButton sct = (SwingCommandButton)trigger;
			if (sct.isToggleButton) {
				button.setSelected(sct.getButton().isSelected());
			} else {
				button.setSelected(true);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.CommandTrigger#commandUpdated()
	 */
	public void commandUpdated() {
		switch (type) {
			case TYPE_CUSTOM:
				// FIXME: that's not the correct way to do this...
				button.setText(command.getLocalizedName());
				break;
			case TYPE_TOOLBUTTON:
			case TYPE_TOOLTOGGLEBUTTON:
				button.setToolTipText(command.getLocalizedName());
				break;
			case TYPE_BUTTON:
			case TYPE_CHECKBOX:
			case TYPE_RADIOBUTTON:
				button.setText(command.getLocalizedName());
				button.setToolTipText(command.getLocalizedDescription());
				break;
			case TYPE_MENUITEM:
				button.setText(command.getLocalizedName());
				button.setMnemonic(Application.getResources().getMnemonic(command.getName()));
				button.setToolTipText(command.getLocalizedDescription());
				break;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.swing.SwingCommandTrigger#removeFrom(java.awt.Container)
	 */
	public boolean removeFrom(Container container) {
		if (container instanceof JMenu) {
			JMenu menu = (JMenu)container;
			if (!menu.isMenuComponent(button)) return false;
			menu.remove(button);
		} else {
			if (button.getParent() != container) return false;
			container.remove(button);
		}
		return true;
	}

}
