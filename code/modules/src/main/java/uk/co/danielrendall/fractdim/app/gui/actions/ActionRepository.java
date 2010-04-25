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
    public static final String FILE_EXIT = "FileExit";
    public static final String DIAGRAM_ZOOM_IN = "DiagramZoomIn";
    public static final String DIAGRAM_ZOOM_OUT = "DiagramZoomOut";

    public static ActionRepository instance() {
        return instance;
    }


    private ActionRepository() {
        actions.put(FILE_OPEN, new FileOpen());
        actions.put(FILE_CLOSE, new FileClose());
        actions.put(FILE_CALCULATE, new FileCalculate());
        actions.put(FILE_EXIT, new FileExit());

        delegatedActions.put(DIAGRAM_ZOOM_IN, new DiagramZoomIn());
        delegatedActions.put(DIAGRAM_ZOOM_OUT, new DiagramZoomOut());
    }

    public FractDimAction getFileOpen() {
        return actions.get(FILE_OPEN);
    }

    public FractDimAction getFileClose() {
        return actions.get(FILE_CLOSE);
    }

    public FractDimAction getFileCalculate() {
        return actions.get(FILE_CALCULATE);
    }

    public FractDimAction getFileExit() {
        return actions.get(FILE_EXIT);
    }

    public FractDimDelegatedAction getDiagramZoomIn() {
        return delegatedActions.get(DIAGRAM_ZOOM_IN);
    }

    public FractDimDelegatedAction getDiagramZoomOut() {
        return delegatedActions.get(DIAGRAM_ZOOM_OUT);
    }
}
