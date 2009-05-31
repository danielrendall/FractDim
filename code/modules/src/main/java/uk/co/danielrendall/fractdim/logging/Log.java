package uk.co.danielrendall.fractdim.logging;

import org.apache.log4j.Logger;


/**
 * @author Daniel Rendall
 * @created 31-May-2009 11:38:44
 */
public final class Log {

    public final static Logger gui = Logger.getLogger("fractdim.gui");
    public final static Logger app = Logger.getLogger("fractdim.app");
    public final static Logger misc = Logger.getLogger("fractdim.misc");
    public final static Logger test = Logger.getLogger("fractdim.test");
    public final static Logger points = Logger.getLogger("fractdim.calculation.points");
    public final static Logger squares = Logger.getLogger("fractdim.calculation.squares");
    public final static Logger recursion = Logger.getLogger("fractdim.calculation.recursion");


    
}
