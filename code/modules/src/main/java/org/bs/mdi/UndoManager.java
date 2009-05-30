/* 
 * UndoManager.java - Allows undo/redo operations
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


package org.bs.mdi;

import java.util.*;


/**
 * Allows to undo or redo Actions.
 * The UndoManager keeps track of Actions which were previously
 * applied to the document and allows to undo or redo these Actions.
 */
public class UndoManager {
	
	public static int DEFAULT_NUM_ACTIONS = 64;
	
	Document document;
	LinkedList appliedActions;
	LinkedList undoneActions;
	int limit;
	
	
	/**
	 * Creates a new UndoManager.
	 * This UndoManager will remember the last 64 occurred actions.
	 * @param document	the document to be associated with this UndoManager
	 */
	public UndoManager(Document document) {
		this(document, DEFAULT_NUM_ACTIONS);
	}
	
	/**
	 * Creates a new UndoManager.
	 * The UndoManager keeps track of all Actions which occur
	 * while editing a document and know how to undo/redo
	 * an action.
	 * @param document	the document to be associated with this UndoManager
	 * @param limit	the maximum number of actions to remember
	 */
	public UndoManager(Document document, int limit) {
		this.document = document;
		appliedActions = new LinkedList();
		undoneActions = new LinkedList();
		this.limit = limit;
	}
	
	
	/**
	 * Gets the current action number limit, i.e. the maximum
	 * number of action which this UndoManager will remember.
	 * @return	the action number limit
	 */
	public int getLimit() {
		return limit;
	}
	
	
	/**
	 * Sets the current action number limit.
	 * @param limit	the action number limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
	/**
	 * Tells the UndoManager that an action has occurred.
	 * The UndoManager will keep track of that action and
	 * allow it to be undone or redone, unless the action
	 * number limit is exceeded.
	 * @param action	the action which has just occurred
	 * @param data	the corresponding data object
	 */
	public void add(Action action, Data data) {
		Step step = new Step(action, data);
		appliedActions.addLast(step);
		if (appliedActions.size() > limit)
			appliedActions.removeFirst();
		flushRedoLog();
		Application.getMessageDispatcher().dispatch(
				this, MessageDispatcher.ACTION_OCCURRED, action);
	}
	
	
	/**
	 * Gets the number of elements in the queue of
	 * applied actions. Usually, this number increases
	 * with every call to the {@link #add} method, except
	 * when the actions limit is exceeded.
	 * @return	the number of applied actions
	 */
	public int countAppliedActions() {
		return appliedActions.size();
	}
	
	
	/**
	 * Gets the number of elements in the queue of
	 * undone actions. Usually, this number increases
	 * with every call to the {@link #undo} method, and is
	 * set to zero when {@link #add} is called.
	 * @return	the number of undone actions
	 */
	public int countUndoneActions() {
		return undoneActions.size();
	}
	
	
	/**
	 * Removes the last (i.e. most recent) action in the
	 * queue of applied actions. 
	 */
	public void removeLastAppliedAction() {
		appliedActions.removeLast();
	}
	
	
	/**
	 * Returns the last action which has been applied.
	* @return	the last action that has been applied to the document
	*/
	public Action getLastAction() {
		try {
			Step s = (Step)appliedActions.getLast();
			return s.action;
		} catch (Exception e) {
			return null;
		}		
	}

	/**
	 * Returns the last action which has been undone.
	 * @return	the last action that has been undone to the document
	 */
	public Action getLastUndoneAction() {
		try {
			Step s = (Step)undoneActions.getLast();
			return s.action;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Performs an undo operation.
	 * @return	the action which has been undone
	 */
	public Action undo() throws UndoException {
		try {
			Step s = (Step)appliedActions.getLast();
			s.data.undoAction(s.action);
			appliedActions.removeLast();
			undoneActions.addLast(s);
			s.data.notifyObservers(s.action, true);
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.ACTION_UNDONE, s.action);
			return s.action;
		} catch (Exception e) {
			throw new UndoException();
		}		
	}

	/**
	 * Performs a redo operation.
	 * @return	the action which has been redone.
	 */
	public Action redo() throws UndoException {
		try {
			Step s = (Step)undoneActions.getLast();
			s.data.applyAction(s.action);
			undoneActions.removeLast();
			appliedActions.addLast(s);
			s.data.notifyObservers(s.action, false);
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.ACTION_REDONE, s.action);
			return s.action;
		} catch (Exception e) {
			throw new UndoException();
		}	
	}

	/**
	 * Clears the undo log. Deletes the list of actions which have been
	 * applied to the document until now. These actions cannot be undone
	 * any more.
	 */
	public void flushUndoLog() {
		appliedActions.clear();
	}

	/**
	 * Clears the redo log. Deletes the list of actions which have been undone
	 * until now. These actions cannot be redone any more.
	 */
	public void flushRedoLog() {
		undoneActions.clear();
	}

	/**
	 * Flushes undo and redo logs.
	 * See {@link #flushUndoLog} and {@link #flushRedoLog}.
	 */
	public void flushLogs() {
		flushUndoLog();
		flushRedoLog();
	}

	/**
	 * Returns true if the last action can be undone.
	 * @return	true if an undo is possible
	 */
	public boolean isUndoPossible() {
		return (appliedActions.size() != 0);
	}

	/**
	 * Returns true if the last undone action can be redone. 
	 * @return	true if a redo is possible
	 */
	public boolean isRedoPossible() {
		return (undoneActions.size() != 0);
	}
	
	
	class Step {
		Action action;
		Data data;
		
		Step(Action action, Data data) {
			this.action = action;
			this.data = data;
		}
		
	}
	
	public class UndoException extends Exception {
	}

}
