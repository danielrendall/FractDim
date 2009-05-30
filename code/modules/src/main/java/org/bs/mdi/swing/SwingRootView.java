/* 
 * SwingRootView.java - Swing document view, derived from JPanel
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

import java.util.*;
import javax.swing.*;
import org.bs.mdi.*;

/**
 * An implementation of a {@link RootView} using Swing technology.
 */
public abstract class SwingRootView extends JPanel implements RootView {
	
	Data data;
	DocumentWindow window;
	ArrayList observers;
	View parentView;
	ArrayList children;
	boolean copyPossible;
	boolean cutPossible;
	boolean pastePossible;
	boolean deletePossible;
	
	
	/**
	 * Creates a new root view.	
	 */
	public SwingRootView() {
		parentView = null;
		observers = new ArrayList();
		children = new ArrayList();
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#getDocument()
	 */
	public Document getDocument() {
		if (data == null) return null;
		return data.getDocument();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#setData(org.bs.mdi.Data)
	 */
	public void setData(Data data) {
		this.data = data;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#getData()
	 */
	public Data getData() {
		return data;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#getParentView()
	 */
	public View getParentView() {
		return parentView;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#addChild(org.bs.mdi.View)
	 */
	public void addChild(View child) {
		children.add(child);
		((SwingView)child).parentView = this;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#removeChild(org.bs.mdi.View)
	 */
	public void removeChild(View child) {
		children.remove(child);
		((SwingView)child).parentView = null;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#countChildren()
	 */
	public int countChildren() {
		return children.size();
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#getChild(int)
	 */
	public View getChild(int index) {
		return (View)children.get(index);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.RootView#windowChanged(org.bs.mdi.DocumentWindow)
	 */
	public void windowChanged(DocumentWindow window) {
		this.window = window;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.RootView#getWindow()
	 */
	public DocumentWindow getWindow() {
		return window;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#copy()
	 */
	public abstract org.bs.mdi.Action copy();

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#cut()
	 */
	public abstract org.bs.mdi.Action cut();

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#paste(org.bs.mdi.Action)
	 */
	public abstract void paste(org.bs.mdi.Action action);

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#delete()
	 */
	public abstract void delete();
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#isCopyPossible()
	 */
	public final boolean isCopyPossible() {
		return copyPossible;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#isCutPossible()
	 */
	public final boolean isCutPossible() {
		return cutPossible;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#isPastePossible()
	 */
	public final boolean isPastePossible() {
		return pastePossible;
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#isDeletePossible()
	 */
	public final boolean isDeletePossible() {
		return deletePossible;
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#setCopyPossible(boolean)
	 */
	public void setCopyPossible(boolean possible) {
		if (copyPossible == possible) return;
		copyPossible = possible;
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.SELECTION_CHANGED, null);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#setCutPossible(boolean)
	 */
	public void setCutPossible(boolean possible) {
		if (cutPossible == possible) return;
		cutPossible = possible;
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.SELECTION_CHANGED, null);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#setPastePossible(boolean)
	 */
	public void setPastePossible(boolean possible) {
		if (pastePossible == possible) return;
		pastePossible = possible;
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.SELECTION_CHANGED, null);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.View#setDeletePossible(boolean)
	 */
	public void setDeletePossible(boolean possible) {
		if (deletePossible == possible) return;
		deletePossible = possible;
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.SELECTION_CHANGED, null);
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.View#syncWithData()
	 */
	public abstract void syncWithData();
	
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
	public void notifyObservers(org.bs.mdi.Action action, boolean undo) {
		ActionObserver o;
		for (int i=0; i<observers.size(); i++) {
			o = (ActionObserver)observers.get(i);
			if (undo)
				o.actionUndone(this, action);
			else
				o.actionPerformed(this, action);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObserver#actionPerformed(org.bs.mdi.ActionObservable, org.bs.mdi.Action)
	 */
	public void actionPerformed(ActionObservable observable,
			org.bs.mdi.Action action) {
		applyAction(action);
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionObserver#actionUndone(org.bs.mdi.ActionObservable, org.bs.mdi.Action)
	 */
	public void actionUndone(ActionObservable observable,
			org.bs.mdi.Action action) {
		undoAction(action);
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionProcessor#applyAction(org.bs.mdi.Action)
	 */
	public void applyAction(org.bs.mdi.Action action) {
		if (action.isRetarded() && action.getSource() == this)
			return;
		action.applyTo(this);		
	}
	
	/* (non-Javadoc)
	 * @see org.bs.mdi.ActionProcessor#undoAction(org.bs.mdi.Action)
	 */
	public void undoAction(org.bs.mdi.Action action) {
		action.undoFrom(this);
		action.setRetarded(false);
	}

}
