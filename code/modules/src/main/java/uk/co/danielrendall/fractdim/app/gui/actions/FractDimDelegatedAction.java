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
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 25-Apr-2010
 * Time: 10:19:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class FractDimDelegatedAction extends FractDimAction {

    protected Action delegate = NullAction.INSTANCE;

    public void setDelegate(Action action) {
        delegate = action;
        setEnabled(true);
    }

    public void removeDelegate() {
        setEnabled(false);
        delegate = NullAction.INSTANCE;
    }

    public final void actionPerformed(ActionEvent e) {
        delegate.actionPerformed(e);
    }
}
