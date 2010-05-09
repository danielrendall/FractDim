package uk.co.danielrendall.fractdim.app.gui;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 09-May-2010
 * Time: 15:41:35
 * To change this template use File | Settings | File Templates.
 */
interface SquareCountingModelTreeNode {
    public Object getValueAt(int column);

    public Object getChild(int index);

    public int getChildCount();

    public int getIndexOfChild(Object child);
}
