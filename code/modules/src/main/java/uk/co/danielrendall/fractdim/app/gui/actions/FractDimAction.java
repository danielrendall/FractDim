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

import uk.co.danielrendall.fractdim.app.FractDim;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 19:21:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class FractDimAction extends AbstractAction {

    protected FractDimAction() {
    }


    protected void setIcons(String name) {
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(FractDimAction.class.getResource("/icons/22/" + name + ".png")));
        putValue(Action.SMALL_ICON, new ImageIcon(FractDimAction.class.getResource("/icons/16/" + name + ".png")));
    }


}
