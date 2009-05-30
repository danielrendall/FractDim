/* 
 * SwingStatusBar.java - Swing implementation of a status bar
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

package org.bs.mdi.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Swing implementation of a status bar.
 */
public class SwingStatusBar extends JPanel {
	
	JLabel statusLabel;
	JProgressBar progressBar;
	GridLayout layout;
	
	
	/**
	 * Creates a new status bar, which provides by default
	 * a status label and a progress bar.
	 */
	public SwingStatusBar() {
		super();
		//setBorder(new EtchedBorder(BevelBorder.RAISED));
		layout = new GridLayout();
		layout.setColumns(2);		
		setLayout(layout);
		setBorder(new EmptyBorder(2, 2, 2, 2));
		setLayout(new GridLayout(1, 4));
		setMaximumSize(new Dimension(32767, 32));
		setMinimumSize(new Dimension(0, 32));
		statusLabel = new JLabel();
		add(statusLabel);
		progressBar = new JProgressBar();
		add(progressBar);
	}
	
	/**
	 * Gets the status label.
	 * @return the status label
	 */
	public JLabel getStatusLabel() {
		return statusLabel;
	}
	
	/**
	 * Returns the status bar.
	 * @return the status bar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
	protected void addImpl(Component comp,
            Object constraints,
            int index) {
		if (comp instanceof JComponent) {
			((JComponent)comp).setBorder(new CompoundBorder(
				new EtchedBorder(BevelBorder.LOWERED),
				new EmptyBorder(1, 3, 1, 3)));
		}
		layout.setColumns(layout.getColumns()+1);
		setLayout(layout);
		super.addImpl(comp, constraints, index);
	}

}
