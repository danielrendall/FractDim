/* 
 * SwingActionMonitor.java - Swing implementation of org.bs.mdi.ActionMonitor
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

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.bs.mdi.*;


/**
 * A ProgressMonitor which uses Swing components for display.
 */
public class SwingProgressMonitor implements org.bs.mdi.ProgressMonitor, ActionListener {
	
	JProgressBar progressBar;
	ArrayList tasks;
	javax.swing.Timer timer;
	boolean indeterminateToggle = false;
	static final int UPDATE_INTERVAL = 1000;
	
	
	/**
	 * Creates a new SwingProgressMonitor, which uses the specified
	 * progress bar to give the user feedback about currently
	 * running tasks.
	 * @param progressBar	the progress bar used for display 
	 */
	public SwingProgressMonitor(JProgressBar progressBar) {
		this.progressBar = progressBar;
		tasks = new ArrayList();
		timer = new javax.swing.Timer(UPDATE_INTERVAL, this);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setString(null);		
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.ProgressMonitor#add(org.bs.mdi.Task)
	 */
	public void add(Task task) {
		tasks.add(task);		
		if (tasks.size() == 1) {
			timer.start();
		}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.ProgressMonitor#remove(org.bs.mdi.Task)
	 */
	public void remove(Task task) {
		tasks.remove(task);
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
	}
	
	protected void update() {
		Task t;
		Task lastActiveTask = null;
		int progress = 0;
		int count = 0;
		boolean indeterminate = false;
		
		for (int i=0; i<tasks.size(); i++) {
			t = (Task)tasks.get(i);
			if (!t.isActive()) continue;
			lastActiveTask = t;
			if (t.getProgress() == Task.PROGRESS_UNAVAILABLE && !indeterminate) {
				indeterminate = true;
				indeterminateToggle = !indeterminateToggle;
			}
			progress += 100*(t.getProgress() - t.getMinimumProgress())/(t.getMaximumProgress() - t.getMinimumProgress());
			count++;
		}
		
		switch (count) {
		case 0:			
			progressBar.setValue(progressBar.getMinimum());
			progressBar.setStringPainted(false);
			progressBar.setString(null);
			break;			
		case 1:
			progressBar.setMinimum(lastActiveTask.getMinimumProgress());
			progressBar.setMaximum(lastActiveTask.getMaximumProgress());
			if (!indeterminate) {
				progressBar.setValue(lastActiveTask.getProgress());
			} else {
				if (indeterminateToggle) {
					progressBar.setValue(progressBar.getMaximum());
				} else {
					progressBar.setValue(progressBar.getMinimum());
				}
			}
			progressBar.setString(lastActiveTask.getName());
			progressBar.setStringPainted(true);
			break;
		default:		
			progressBar.setMinimum(0);
			progressBar.setMaximum(100);
			if (!indeterminate) {
				progressBar.setValue(progress);
			} else {
				if (indeterminateToggle) {
					progressBar.setValue(progressBar.getMaximum());
				} else {
					progressBar.setValue(progressBar.getMinimum());
				}
			}
			progressBar.setString("" + count + " " + Application.tr("tasks"));
			progressBar.setStringPainted(true);
			break;
		}
		
		progressBar.paintImmediately(0, 0, 
				progressBar.getSize().width, 
				progressBar.getSize().height);
		if (tasks.size() == 0) timer.stop();

	}
	
	protected void finalize() throws Throwable {
		timer.stop();
		super.finalize();
	}

}
