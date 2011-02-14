/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.fractdim.app.gui.actions;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 20:02:56
 * To change this template use File | Settings | File Templates.
 */
public class ActionRepository {

    private final static Map<String, FractDimAction> actions = new HashMap<String, FractDimAction>();
    private final static Map<String, FractDimDelegatedAction> delegatedActions = new HashMap<String, FractDimDelegatedAction>();
    private final static ActionRepository instance = new ActionRepository();
    public static final String FILE_OPEN = "FileOpen";
    public static final String FILE_CLOSE = "FileClose";
    public static final String FILE_CALCULATE = "FileCalculate";
    public static final String FILE_EXPORT = "FileExport";
    public static final String FILE_EXIT = "FileExit";
    public static final String DIAGRAM_ZOOM_IN = "DiagramZoomIn";
    public static final String DIAGRAM_ZOOM_OUT = "DiagramZoomOut";

    public static ActionRepository instance() {
        return instance;
    }


    private ActionRepository() {
        actions.put(FILE_OPEN, new FileOpen());
        actions.put(FILE_EXIT, new FileExit());

        delegatedActions.put(FILE_CLOSE, new FileClose());
        delegatedActions.put(FILE_CALCULATE, new FileCalculate());
        delegatedActions.put(FILE_EXPORT, new FileExport());
        delegatedActions.put(DIAGRAM_ZOOM_IN, new DiagramZoomIn());
        delegatedActions.put(DIAGRAM_ZOOM_OUT, new DiagramZoomOut());
    }

    public FractDimAction getFileOpen() {
        return actions.get(FILE_OPEN);
    }

    public FractDimAction getFileExit() {
        return actions.get(FILE_EXIT);
    }

    public FractDimDelegatedAction getFileClose() {
        return delegatedActions.get(FILE_CLOSE);
    }

    public FractDimDelegatedAction getFileCalculate() {
        return delegatedActions.get(FILE_CALCULATE);
    }

    public FractDimDelegatedAction getFileExport() {
        return delegatedActions.get(FILE_EXPORT);
    }

    public FractDimDelegatedAction getDiagramZoomIn() {
        return delegatedActions.get(DIAGRAM_ZOOM_IN);
    }

    public FractDimDelegatedAction getDiagramZoomOut() {
        return delegatedActions.get(DIAGRAM_ZOOM_OUT);
    }
}
