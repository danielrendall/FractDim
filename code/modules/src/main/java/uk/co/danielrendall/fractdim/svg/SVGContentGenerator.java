package uk.co.danielrendall.fractdim.svg;

import org.w3c.dom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 24-Apr-2010
 * Time: 15:32:54
 * To change this template use File | Settings | File Templates.
 */
public interface SVGContentGenerator {

    public void generateContent(Element rootElement, SVGElementCreator creator);
}
