/* 
 * Action.java - action interface
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

/**
 * Actions are the means of communication between {@link Data} 
 * and {@link View} objects.
 * An <code>Action</code> encapsulates information about a modification
 * which took place either in the <code>View</code> or in the
 * <code>Data</code> object. An <code>Action</code> is supposed
 * to remember enough information about the modification to be able to undo
 * it at a later time, unless its {@link #isUndoable} method returns
 * false.<p>
 * In order to keep everything synchronized, the <code>View</code>
 * has to inform the <code>Data</code> whenever it has been 
 * modified, and vice versa.
 * This bi-directional communication between <code>View</code> and
 * <code>Data</code> is done by using <code>Action</code> objects
 * as some kind of adaptors which know how to change data and view to
 * reflect the changes done by the user.
 */
public abstract class Action {
	
	ActionObservable source;
	boolean retarded;
	
	/**
	 * <code>Action</code> is an abstract class and cannot be instantiated.
	 * @param source	the {@link ActionObservable} which this 
	 * 	<code>Action</code> originated from
	 */
	protected Action(ActionObservable source) {
		this(source, false);
	}
	
	/**
	 * <code>Action</code> is an abstract class and cannot be instantiated.
	 * @param source	the {@link ActionObservable} which this 
	 * 	<code>Action</code> originated from
	 * @param retarded	true if the action is retarded, i.e. if
	 * the View is already updated and the purpose of this action
	 * is just to update the Data "en retard". 
	 */
	protected Action(ActionObservable source, boolean retarded) {
		this.source = source;
		this.retarded = retarded;
	}
	
	/**
	 * Returns the source of this action.
	 * @return	the {@link ActionObservable} which this 
	 * 	<code>Action</code> originated from
	 */
	public ActionObservable getSource() {
		return source;
	}
	
	/**
	 * Returns true if this is a so-called "retarded" action.<p>
	 * Retarded actions originate from a {@link View} which has been
	 * modified and which has already made these modifications visible to the user.
	 * This is different from the usual behaviour, where the <code>View</code>
	 * detects a modification (e.g. user input) which is not initially visible to the user.
	 * In this scenario, the <code>View</code> would notify the {@link Data},
	 * and the <code>Data</code> would in turn notify the <code>View</code>, 
	 * hence making the modifications visible.
	 * If the action is retarded however, a part of this notification chain can be omitted:
	 * Only the <code>Data</code> objects which this action did <strong>not</strong>
	 * originate from are notified of the changes.<p>
	 * A good example for the use of retarded actions is a text editor.
	 * Whenever the user types text into a text area, the input becomes visible
	 * immediately. The <code>Data</code> and perhaps
	 * other <code>View</code> objects have to be notified of the
	 * modifications, but not the text area where the modified text is already visible.<p>
	 * @return	true if this is a retarded action, false otherwise
	 */
	public boolean isRetarded() {
		return retarded;
	}
	
	/**
	 * Changes the retarded status of this Action.
	 * There should be no need to call this method from
	 * outside the Java MDI Framework.
	 * @param retarded	true if this Action should be considered regarded, false otherwise
	 */
	public void setRetarded(boolean retarded) {
		this.retarded = retarded;
	}
	
	/**
	 * Applies this action to a <code>Data</code> object.
	 * Subclasses of Action should implement this method
	 * to initiate a state change on the given data object. 
	 * @param data	the <code>Data</code> object
	 */
	public abstract void applyTo(Data data);
	
	/**
	 * Applies this action to a <code>View</code> object.
	 * Subclasses of Action should implement this method
	 * to initiate a state change on the given view object. 
	 * @param view	the <code>View</code> object
	 */
	public abstract void applyTo(View view);
	
	/**
	 * Undoes this Action from a <code>Data</code> object.
	 * Subclasses of Action should implement this method
	 * to initiate a state change on the given data object. 
	 * @param data	the <code>Data</code> object
	 */
	public abstract void undoFrom(Data data);
	
	/**
	 * Undoes this Action from a <code>View</code> object.
	 * Subclasses of Action should implement this method
	 * to initiate a state change on the given view object. 
	 * @param view	the <code>View</code> object
	 */
	public abstract void undoFrom(View view);
	
	/**
	 * Returns true if the action can be made undone.
	 * An action should identify itself as non-undoable if it includes too few information
	 * in order to be able to undo the changes. 
	 * @return	true if this action is undoable
	 */
	public boolean isUndoable() {
		return true;
	}
	
	/**
	 * Returns true if this action should be clustered together with the action given 
	 * as the parameter.
	 * This mechanism allows you to "glue" similar consecutive actions together
	 * to one "action cluster". This is done by combining them to a {@link CompositeAction}.
	 * The actual clustering is done by the {@link RootData} object. 
	 * The usual calling convention is: <code>oldAction.clustersWith(newAction)</code>.
	 * @param action	the Action to be tested whether it can be clustered or not
	 * @return	true if clustering is desirable, false otherwise
	 */
	public boolean clustersWith(Action action) {
		return false;
	}
	

//// Action joining
//// Don't know if this is really necessary...
// 	
// 	public boolean joinsWith(Action action) {
//		return false;
//	}
//	
//	public void join(Action action) throws ActionJoinException {
//	}
//	
//	public class ActionJoinException extends Exception {
//	}

	
	/**
	 * Returns the name of the action type.
	 * If this action performs text insertion in a text editor for example, this method
	 * should return something like "text insertion".
	 * @return	the name this action type
	 */
	public abstract String getName();
	
	/**
	 * Returns a string representation of this action.
	 * The returned string should describe exactly what this action does.
	 * @return	the string representation of this action.
	 */
	public abstract String toString();

}
