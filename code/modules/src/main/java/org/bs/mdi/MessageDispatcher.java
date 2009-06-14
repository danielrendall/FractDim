/* 
 * MessageDispatcher.java - The framework's communication manager 
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
 * The MessageDispatcher manages communication between various parts of the framework.
 */
public class MessageDispatcher {
	
	/**
	 * Indicates that a new document window has been created.
	 */
	public static final int WINDOW_CREATED = 1;
	/**
	 * Indicates that a document window has been opened.
	 */
	public static final int WINDOW_OPENED = 2;
	/**
	 * Indicates that a document window has been closed.
	 */
	public static final int WINDOW_CLOSED = 3;
	/**
	 * Indicated that a document window has been selected/activated.
	 */
	public static final int WINDOW_SELECTED = 4;
	/**
	 * Indicates that the application has just been launched.
	 */
	public static final int APP_INIT = 5;
	/**
	 * Indicates that the application is shutting down.
	 */
	public static final int APP_QUIT = 6;
	/**
	 * Indicates that the user has issued a command, for example clicked a menu entry.
	 */
	public static final int COMMAND_ISSUED = 7;
	/**
	 * Indicates that the current selection has been changed.
	 */
	public static final int SELECTION_CHANGED = 8;
	/**
	 * Indicates that the UndoManager registered an Action.
	 */
	public static final int ACTION_OCCURRED = 9;
	/**
	 * Indicates that the UndoManager just performed an undo operation.
	 */
	public static final int ACTION_UNDONE = 10;
	/**
	 * Indicates that the UndoManager just performed a redo operation.
	 */
	public static final int ACTION_REDONE = 11;
	/**
	 * Indicates that the FileIOManager has registered a new FileLoader. 
	 */
	public static final int IO_LOADER_REGISTERED = 12;
	/**
	 * Indicates that the FileIOManager has registered a new FileSaver. 
	 */
	public static final int IO_SAVER_REGISTERED = 13;
	/**
	 * Indicates that the FileIOManager has registered a new FileExporter. 
	 */
	public static final int IO_EXPORTER_REGISTERED = 14;
	/**
	 * Indicates that another document has been selected.
	 */
	public static final int DOCUMENT_SELECTED = 15;
	/**
	 * Indicates that a document has been saved.
	 */
	public static final int DOCUMENT_SAVED = 16;
	/**
	 * Indicates that a documents dirty status has been changed.
	 */
	public static final int DOCUMENT_DIRTY = 17;
    /**
     * Indicates that a document has been created.
     */
    public static final int DOCUMENT_CREATED = 18;
    /**
     * Indicates that a documents has been opened.
     */
    public static final int DOCUMENT_OPENED = 19;
    /**
     * Indicates that a document is about to close
     */
    public static final int DOCUMENT_CLOSING = 20;
    /**
     * Indicates that the closing of the document has been vetoed by something
     */
    public static final int DOCUMENT_CLOSE_ABORTED = 21;
    /**
     * Indicates that a document has closed all its windows and is about to expire
     */
    public static final int DOCUMENT_CLOSED = 22;
	/**
	 * This is the last message ID reserved by the MDI framework. Custom messages may
	 * use id's > LAST_RESERVED.
	 */
	public static final int LAST_RESERVED = 255;
	
	
	List messageProcessors = new LinkedList();

    /**
	 * Creates a new message dispatcher.
	 * Note that there is a default dispatcher which can be accessed using
	 * {@link Application#getMessageDispatcher}.
	 */
	public MessageDispatcher() {
	}
	
	/**
	 * Register a message processor so that it will be notified of future
	 * messages.
	 * @param processor	the message processor
	 */
	public void registerProcessor(MessageProcessor processor) {
		messageProcessors.add(processor);
	}
	
	/**
	 * Unregister a message processor so that it will no longer be notified
	 * of any messages.
	 * @param processor	the message processor
	 */
	public void unregisterProcessor(MessageProcessor processor) {
		messageProcessors.remove(processor);
	}
	
	/**
	 * Counts the registered MessageProcessors.
	 * @return	the number of registered MessageProcessors
	 */
	public int countProcessors() {
		return messageProcessors.size();
	}
	
	/**
	 * Unregisters all MessageProcessors, so that nobody will be notified of
	 * future messages.
	 */
	public void unregisterAllProcessors() {
		messageProcessors.clear();
	}
	
	/**
	 * Dispatches a message. All registered MessageProcessors will be notified of
	 * this message. Note that it cannot be guaranteed that a particular MessageProcessor 
	 * will handle the message. It is possible that none, just one or all of the registered
	 * MessageProcessors will react.  
	 * @param source	the source where this message originated from
	 * @param type	the message type
	 * @param argument	an argument
	 */
	public void dispatch(Object source, int type, Object argument) {
		Iterator iter = messageProcessors.iterator();
		while (iter.hasNext()) {
			((MessageProcessor)iter.next()).processMessage(source, type, argument);
		}
	}

}
