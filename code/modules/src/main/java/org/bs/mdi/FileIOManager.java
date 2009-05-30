/* 
 * FileIOManager.java - File I/O Module Manager
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
import java.io.*;
 
/**
 * Manages File I/O modules
 */
public class FileIOManager {

	public static final int OPEN = 1;
	public static final int SAVE = 2;
	public static final int EXPORT = 3;
	
	List fileLoaders;
	List fileSavers;
	List fileExporters;
	List loadableFormats;
	List saveableFormats;
	List exportableFormats;
	
	
	/**
	 * Creates a new file I/O manager.
	 */
	public FileIOManager() {
		fileLoaders = new ArrayList();
		fileSavers = new ArrayList();
		fileExporters = new ArrayList();
		loadableFormats = new ArrayList();
		saveableFormats = new ArrayList();
		exportableFormats = new ArrayList();
	}
	
	/**
	 * Tries to load data from disk using the specified loader module.
	 * @param filename	the filename to be opened
	 * @param loader	the loader module
	 * @return	the document data
	 * @throws FileIOException
	 */
	public RootData load(String filename, FileLoader loader) throws FileIOException {
		if (!(new File(filename).exists())) throw new FileIOException(FileIOException.ERR_NOSUCHFILE, filename);
		if (loader.canHandle(filename)) 
			return loader.load(filename);
		else
			throw new FileIOException(FileIOException.ERR_UNSUPPORTEDFORMAT, filename);
	}

	/**
	 * Tries to load data from disk.
	 * This is done by finding an apropriate file I/O module for the given filename,
	 * and delegating the actual loading process to the module.
	 * @param filename	the filename to be opened
	 * @return	the document data
	 * @throws FileIOException
	 */
	public RootData load(String filename) throws FileIOException {
		FileLoader loader;
		if (!(new File(filename).exists())) throw new FileIOException(FileIOException.ERR_NOSUCHFILE, filename);
		Iterator iter = fileLoaders.iterator();
		while (iter.hasNext()) {
			loader = (FileLoader)iter.next();
			if (loader.canHandle(filename)) return loader.load(filename);
		}
		throw new FileIOException(FileIOException.ERR_UNSUPPORTEDFORMAT, filename);
	}
	
	/**
	 * Tries to save a document to disk using the specified saver module.
	 * @param data	the document data to be saved
	 * @param filename	the filename to be written to
	 * @param saver	the saver I/O module
	 * @throws FileIOException
	 */
	public void save(RootData data, String filename, FileSaver saver) throws FileIOException {
		if (saver.canHandle(filename)) 
			saver.save(data, filename);
		else
			throw new FileIOException(FileIOException.ERR_UNSUPPORTEDFORMAT, filename);
	}

	/**
	 * Tries to save a document to disk.
	 * This is done by finding an apropriate file I/O module for the given filename,
	 * and delegating the actual saving process to the module.
	 * @param data	the document data to be saved
	 * @param filename	the filename to be written to
	 * @throws FileIOException
	 */
	public void save(RootData data, String filename) throws FileIOException {
		Iterator iter = fileSavers.iterator();
		while (iter.hasNext()) {
			FileSaver fs = (FileSaver)iter.next();
			if (fs.canHandle(filename)) {
				fs.save(data, filename);
				return;
			}
		}
		throw new FileIOException(FileIOException.ERR_UNSUPPORTEDFORMAT, filename);
	}

  	/**
  	 * Exports the data using the specified exporter module.
	 * @param data	the document data to be exported 
	 * @param exporter	the exporter module to be used
	 * @throws FileIOException
	 */
	public void export(RootData data, FileExporter exporter) throws FileIOException {
		exporter.export(data);
  	}
  	
  	/**
  	 * Returns a list of all registered file loader modules.
	 * @return	all registered file loaders
	 */
	public FileLoader[] getFileLoaders() {
  		return (FileLoader[])fileLoaders.toArray(new FileLoader[0]);
  	}
  	
	/**
	 * Returns a list of all registered file saver modules.
	 * @return	all registered file savers
	 */
	public FileSaver[] getFileSavers() {
		return (FileSaver[])fileSavers.toArray(new FileSaver[0]);
	}
		
	/**
	 * Returns a list of all registered file exporter modules.
	 * @return	all registered file exporters
	 */
	public FileExporter[] getFileExporters() {
		return (FileExporter[])fileExporters.toArray(new FileExporter[0]);
	}
	
	/**
	 * Returns a list of all supported formats for loading, saving or exporting data.
	 * @param mode	<code>OPEN</code>, <code>SAVE</code> or <code>EXPORT</code>
	 * @return	an array of all supported file formats
	 */
	public FileFormat[] getSupportedFormats(int mode) {
		List list;
		switch (mode) {
			case OPEN:	list = loadableFormats; break;
			case SAVE: list = saveableFormats; break;
			case EXPORT: list = exportableFormats; break;
			default: return null;
		}
		return (FileFormat[])list.toArray(new FileFormat[0]);
	}
	
	/**
	 * Looks up a file I/O module for the specified format.
	 * @param format	the file format
	 * @param mode	<code>OPEN</code>, <code>SAVE</code> or <code>EXPORT</code>
	 * @return	the appropriate file I/O module, or null on error 
	 */
	public FileIOModule getIOModule(FileFormat format, int mode) {
		List list;
		switch (mode) {
			case OPEN:	list = fileLoaders; break;
			case SAVE: list = fileSavers; break;
			case EXPORT: list = fileExporters; break;
			default: return null;
		}
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			FileIOModule module = (FileIOModule)iter.next();
			FileFormat formats[] = module.getSupportedFormats();
			for (int j=0; j<formats.length; j++) {
				if (formats[j] == format) return module;
			}
		}
		return null;
	}
	
	/**
	 * Registers a file I/O module. 
	 * The type of the module (loader, saver, exporter) is determined 
	 * automatically. All available modules should be registered at application startup.
	 * @param module	the module to be registered
	 */
	public synchronized void registerModule(FileIOModule module) {
		if (module instanceof FileLoader) { 
			fileLoaders.add(module);
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.IO_LOADER_REGISTERED, module);
			try {
				for (int i=0; i<module.getSupportedFormats().length; i++) {
					loadableFormats.add(module.getSupportedFormats()[i]);
				}
			} catch (RuntimeException e) {}
		} 
		if (module instanceof FileSaver) { 
			fileSavers.add(module); 
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.IO_SAVER_REGISTERED, module);
			try {
				for (int i=0; i<module.getSupportedFormats().length; i++) {
					saveableFormats.add(module.getSupportedFormats()[i]);
				}
			} catch (RuntimeException e) {}
		} 
		if (module instanceof FileExporter) { 
			fileExporters.add(module); 
			Application.getMessageDispatcher().dispatch(
					this, MessageDispatcher.IO_EXPORTER_REGISTERED, module);
			try {
				for (int i=0; i<module.getSupportedFormats().length; i++) {
					exportableFormats.add(module.getSupportedFormats()[i]);
				}
			} catch (RuntimeException e) {}
		} 
	}
	
	/**
	 * Registers all given file I/O modules in the given array.
	 * @param modules	the file I/O modules to be registered
	 */
	public void registerAllModules(FileIOModule[] modules) {
		if (modules == null) return;
		for (int i=0; i<modules.length; i++) {
			registerModule(modules[i]);
		}
	}
	
	/**
	 * Returns true if this file I/O manager is "ready".
	 * A file I/O manager is considered ready, if at least one
	 * file saving module and at least one file loading module have been registered.
	 * @return	true is this I/O manager is ready
	 */
	public boolean isReady() {
		return (fileLoaders.size() != 0 && fileSavers.size() != 0);
	}
	
	/**
	 * Determines if the file manager is able to load a file.
	 * @return	true if at least one FileLoader has been registered,
	 * false otherwise
	 */
	public boolean canLoad() {
		return fileLoaders.size() != 0;
	}
	
	/**
	 * Determines if the file manager is able to save a file.
	 * @return	true if at least one FileSaver has been registered,
	 * false otherwise
	 */
	public boolean canSave() {
		return fileSavers.size() != 0;
	}
	
	/**
	 * Determines if the file manager is able to export a file.
	 * @return	true if at least one FileExporter has been registered,
	 * false otherwise
	 */
	public boolean canExport() {
		return fileExporters.size() != 0;
	}
	
	/**
	 * Returns true if the given file exists.
	 * @param filename	the filename
	 * @return	true if the file exists, false otherwise
	 */
	public boolean fileExists(String filename) {
		File f = new File(filename);
		return f.exists();
	}
	
	/**
	 * Counts all registered FileLoader modules.
	 * @return	the number of registered loader modules
	 */
	public int countLoaders() {
		return fileLoaders.size();
	}
	
	/**
	 * Counts all registered FileSaver modules.
	 * @return	the number of registered saver modules.
	 */
	public int countSavers() {
		return fileSavers.size();
	}
	
	/**
	 * Counts all registered FileExporter modules.
	 * @return	the number of registered exporter modules.
	 */
	public int countExporters() {
		return fileExporters.size();
	}
	
	/**
	 * Counts all registered modules.
	 * Note that a module can be counted multiple times if it provides more
	 * than one I/O service, e.g. if it supports loading as well as saving. 
	 * @return	the number of all registered modules.
	 */
	public int countAllModules() {
		return fileLoaders.size() + fileSavers.size() + fileExporters.size();
	}

 }

