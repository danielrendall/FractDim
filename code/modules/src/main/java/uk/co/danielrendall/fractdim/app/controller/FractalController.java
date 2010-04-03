package uk.co.danielrendall.fractdim.app.controller;

import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 21:02:09
 * To change this template use File | Settings | File Templates.
 */
public class FractalController {
    private final FractalDocument document;
    private final FractalPanel panel;

    public FractalController(FractalDocument document) {
        this.document = document;
        panel = new FractalPanel();
        FractDim.instance().add(panel);
    }
}
