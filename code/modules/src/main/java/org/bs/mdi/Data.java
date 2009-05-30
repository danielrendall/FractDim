/* 
 * Data.java - Data interface
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
 * Represents a piece of information in a document.
 * Data objects can be nested, but there is only one
 * {@link RootData} object which is directly associated with
 * the document.
 */
public abstract class Data implements ActionObserver, ActionObservable, ActionProcessor {
		
	Document document;	
	ArrayList observers;
	ArrayList children;
	Data parent;

	
	/**
	 * Creates a new Data object which has no parent element and no
	 * associated Document object.
	 */
	protected Data() {
		this(null, null);
	}
	
	/**
	 * Creates a new Data object which has no parent element.
	 * @param document	the Document which this Data object belongs to
	 */
	protected Data(Document document) {
		this(document, null);
	}
	
	/**
	 * Creates a new Data object.
	 * Automatically calls <code>parent.addChild(this)</code> if parent
	 * is not null.
	 * @param document	the Document which this Data object belongs to
	 * @param parent	the parent element of this Data object
	 */
	protected Data(Document document, Data parent) {
		children = new ArrayList();
		observers = new ArrayList();
		setDocument(document);
		if (parent != null)
			parent.addChild(this);
	}	
	
	/**
	 * Returns the document associated with this Data object.
	 * @return	the associated document
	 */
	public Document getDocument() {
		return document;
	}
	
	/**
	 * Associates this Data object with the given document.
	 * This means that this Data object will use the document's
	 * {@link UndoManager}.
	 * @param document	the document
	 */
	public void setDocument(Document document) {
		this.document = document;
	}
	
	/**
	 * Gets the parent Data object, or null if there is no parent object
	 * (i.e. this is probably a {@link RootData} object).
	 * @return	the parent object
	 */
	public Data getParentData() {
		return parent;
	}
	
	/**
	 * Adds a child element.
	 * @param child	the child element
	 */
	public void addChild(Data child) {
		children.add(child);
		child.parent = this;
	}
	
	/**
	 * Removes a child element.
	 * @param child	the child element
	 */
	public void removeChild(Data child) {
		children.remove(child);
		child.parent = null;
	}
	
	/**
	 * Counts all child elements.
	 * @return	the number of child elements
	 */
	public int countChildren() {
		return children.size();
	}
	
	/**
	 * Gets the child element at the specified index.
	 * @param index	the index
	 * @return	the child element at the specified position
	 */
	public Data getChild(int index) {
		return (Data)children.get(index);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObservable#addObserver(org.bs.mdi.ActionObserver)
	 */
	public void addObserver(ActionObserver observer) {
		observers.add(observer);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObservable#removeObserver(org.bs.mdi.ActionObserver)
	 */
	public void removeObserver(ActionObserver observer) {
		observers.remove(observer);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObservable#countObservers()
	 */
	public int countObservers() {
		return observers.size();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObservable#isObserver(org.bs.mdi.ActionObserver)
	 */
	public boolean isObserver(ActionObserver observer) {
		return observers.contains(observer);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObservable#notifyObservers(org.bs.mdi.Action, boolean)
	 */
	public void notifyObservers(Action action, boolean undo) {
		//notificationThread.dispatch(action, undo);
		ActionObserver o;
		for (int i = 0; i < observers.size(); i++) {
			o = (ActionObserver) observers.get(i);
			if (undo)
				o.actionUndone(getInstance(), action);
			else
				o.actionPerformed(getInstance(), action);
		}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObserver#actionPerformed(org.bs.mdi.ActionObservable, org.bs.mdi.Action)
	 */	
	public void actionPerformed(ActionObservable observable, Action action) {
		if (action == null || document == null) return;		
		applyAction(action);
		Action coalescedAction = coalesceAction(action);
		if (coalescedAction != null)
			document.getUndoManager().add(coalescedAction, this);
		if (!action.isUndoable())
			// if we apply a non-undoable action, we can throw away our undo/redo logs			
			document.getUndoManager().flushLogs();
		document.setDirty(true);		
		notifyObservers(action, false);		
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObserver#actionUndone(org.bs.mdi.ActionObservable, org.bs.mdi.Action)
	 */
	public void actionUndone(ActionObservable observable, Action action) {
		undoAction(action);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionProcessor#applyAction(org.bs.mdi.Action)
	 */
	public void applyAction(Action action) {
		action.applyTo(this);		
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionProcessor#undoAction(org.bs.mdi.Action)
	 */
	public void undoAction(Action action) {
		action.undoFrom(this);		
	}
	
	/**
	 * Glues similar consecutive Actions together.
	 * @param a	the recently occurred action
	 * @return	the coalesced action to be inserted
	 */
	protected Action coalesceAction(Action a) {
		UndoManager undoManager = getDocument().getUndoManager();
		Action lastAction = undoManager.getLastAction();
		if (lastAction == null) return a;
		if (lastAction instanceof CompositeAction) {
			CompositeAction ca = (CompositeAction)lastAction;
			if (ca.clustersWith(a)) {
				ca.add(a);
				return null;
			}			
		} else if (lastAction.clustersWith(a)) {
			CompositeAction ca = new CompositeAction(a.getSource());
			ca.add(lastAction);
			ca.add(a);
			undoManager.removeLastAppliedAction();			
			return ca;				
		}
		return a;
	}
	
	private Data getInstance() {
		return this;
	}

}
