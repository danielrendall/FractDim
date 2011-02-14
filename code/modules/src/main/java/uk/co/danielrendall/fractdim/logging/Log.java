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

package uk.co.danielrendall.fractdim.logging;

import org.apache.log4j.Logger;


/**
 * @author Daniel Rendall
 * @created 31-May-2009 11:38:44
 */
public final class Log {

    public final static Logger gui = Logger.getLogger("fractdim.gui");
    public final static Logger thread = Logger.getLogger("fractdim.thread");
    public final static Logger app = Logger.getLogger("fractdim.app");
    public final static Logger misc = Logger.getLogger("fractdim.misc");
    public final static Logger test = Logger.getLogger("fractdim.test");
    public final static Logger messages = Logger.getLogger("fractdim.messages");
    public final static Logger geom = Logger.getLogger("fractdim.geom");
    public final static Logger calc = Logger.getLogger("fractdim.calculation");
    public final static Logger points = Logger.getLogger("fractdim.calculation.points");
    public final static Logger squares = Logger.getLogger("fractdim.calculation.squares");
    public final static Logger recursion = Logger.getLogger("fractdim.calculation.recursion");


    
}
