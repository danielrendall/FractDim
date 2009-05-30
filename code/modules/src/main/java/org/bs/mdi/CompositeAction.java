/* 
 * CompositeAction.java - composite action object
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
 * A <code>CompositeAction</code> object consists of one or more single {@link Action} objects, and can be handled
 * like a single <code>Action</code> object. Use this class for grouping similar actions to logical units, which
 * the user should be able to undo/redo at once.<p>
 * Think of a text editor for example. It usually won't let you undo a single keystroke, but rather
 * just whole words or lines. The single keystrokes are somehow "glued together" to bigger
 * units. You can use this class to simulate this behaviour.
 */
public class CompositeAction extends Action {
	
	ArrayList actions;
	String overrideName;
	
	/**
	 * Creates a new CompositeAction.
	 * Note that you should at least add one action (using {@link #add}) before this CompositeAction can be
	 * considered fully functional.
	 * @param observable	the source where this CompositeAction originated from
	 */
	public CompositeAction(ActionObservable observable) {
		super(observable);
		actions = new ArrayList();
		overrideName = null;
	}
	
	/**
	 * Creates a new CompositeAction and overrides its name.
	 * @param observable	the source where this CompositeAction originated from
	 * @param overrideName	the name to override the default name with
	 */
	public CompositeAction(ActionObservable observable, String overrideName) {
		this(observable);
		this.overrideName = overrideName;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#applyTo(org.bs.mdi.Data)
	 */
	public void applyTo(Data data) {
		Action a;
		for (int i=0; i<actions.size(); i++) {
			a = (Action)actions.get(i);
			a.applyTo(data);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#applyTo(org.bs.mdi.View)
	 */
	public void applyTo(View view) {
		Action a;
		for (int i=0; i<actions.size(); i++) {
			a = (Action)actions.get(i);
			a.applyTo(view);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#undoFrom(org.bs.mdi.Data)
	 */
	public void undoFrom(Data data) {
		Action a;
		for (int i = actions.size() - 1; i >= 0; i--) {
			a = (Action) actions.get(i);
			a.undoFrom(data);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#undoFrom(org.bs.mdi.View)
	 */
	public void undoFrom(View view) {
		Action a;
		for (int i = actions.size() - 1; i >= 0; i--) {
			a = (Action) actions.get(i);
			a.undoFrom(view);
		}
	}
	
	/**
	 * Add an action to this CompositeAction.
	 * Note that nesting CompositeActions has not been tested yet, but is supposed to work.
	 * @param action	the action to be added
	 */
	public void add(Action action) {
		actions.add(action);
	}
	
	/**
	 * Remove the given action from this CompositeAction.
	 * @param action	the action to be removed
	 */
	public void remove(Action action) {
		actions.remove(action);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#clustersWith(org.bs.mdi.Action)
	 */
	public boolean clustersWith(Action a) {
		return getLastAction().clustersWith(a);
	}
	
	/**
	 * Returns the first action of this CompositeAction.
	 * @return	the first action
	 */
	public Action getFirstAction() {
		if (actions.size() == 0) return null;
		return (Action)actions.get(0);
	}
	
	/**
	 * Returns the last action of this CompositeAction
	 * @return	the last action
	 */
	public Action getLastAction() {
		if (actions.size() == 0) return null;
		return (Action)actions.get(actions.size() - 1);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#isUndoable()
	 */
	public boolean isUndoable() {
		for (int i=0; i<actions.size(); i++) {
			Action a = (Action)actions.get(i);
			if (!a.isUndoable()) return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.Action#getName()
	 */
	public String getName() {
		if (overrideName != null) return overrideName;
		String lastName = null;
		String result = "";
		for (int i=0; i<actions.size(); i++) {
			Action a = (Action)actions.get(i);
			if (!a.getName().equals(lastName)) {
				if (lastName == null) {
					result = a.getName();
				} else {
					result = result + ", " + lastName;
				}
				lastName = a.getName();
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

}
