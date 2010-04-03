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
    private final static ActionRepository instance = new ActionRepository();

    public static ActionRepository instance() {
        return instance;
    }


    private ActionRepository() {
        actions.put("FileOpen", new FileOpen());
        actions.put("FileClose", new FileClose());
        actions.put("FileExit", new FileExit());

        actions.put("DiagramZoomIn", new DiagramZoomIn());
        actions.put("DiagramZoomOut", new DiagramZoomOut());
    }

    public FractDimAction getFileOpen() {
        return actions.get("FileOpen");
    }

    public FractDimAction getFileClose() {
        return actions.get("FileClose");
    }

    public FractDimAction getFileExit() {
        return actions.get("FileExit");
    }

    public FractDimAction getDiagramZoomIn() {
        return actions.get("DiagramZoomIn");
    }

    public FractDimAction getDiagramZoomOut() {
        return actions.get("DiagramZoomOut");
    }
}
