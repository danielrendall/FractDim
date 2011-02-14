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

package uk.co.danielrendall.fractdim.svg;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.logging.PrettyPrinter;

import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 24-Apr-2010
 * Time: 18:02:27
 * To change this template use File | Settings | File Templates.
 */
public class Utilities {

    public final static DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
    private final static SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());

    public static SAXSVGDocumentFactory getDocumentFactory() {
        return factory;
    }

    public static SVGDocument cloneSVGDocument(SVGDocument document) {
        SVGDocument newDoc = (SVGDocument) document.cloneNode(true);
        return newDoc;

    }

    public static String prettyPrinted(Node doc) {
        StringWriter sw = new StringWriter();
        new PrettyPrinter(doc).prettyPrint(sw);
        return sw.toString();
    }
}
