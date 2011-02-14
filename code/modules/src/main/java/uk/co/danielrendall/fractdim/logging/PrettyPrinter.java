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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Apr-2010
 * Time: 11:36:05
 * To change this template use File | Settings | File Templates.
 */
public class PrettyPrinter {

    private final Node root;
    private static final String spaces = "                                                ";

    public PrettyPrinter(Node root) {
        this.root = root;
    }

    public void prettyPrint(Writer w) {
        BufferedWriter bw = new BufferedWriter(w);
        try {
            prettyPrint(bw, root, 0);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void prettyPrint(BufferedWriter bw, Node node, int indent) throws IOException {
        if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.DOCUMENT_NODE) {
            spaces(bw, indent);
            bw.write("<" + node.getNodeName() + prettyPrint(node.getAttributes()) + ">");
            bw.newLine();
            NodeList children = node.getChildNodes();
            for(int i=0; i<children.getLength(); i++) {
                prettyPrint(bw, children.item(i), indent + 2);
            }
            spaces(bw, indent);
            bw.write("</" + node.getNodeName() + ">");
            bw.newLine();
        } else {
            spaces(bw, indent);
            bw.write(node.getTextContent());
            bw.newLine();
        }
    }

    private String prettyPrint(NamedNodeMap attributes) {
        StringBuffer sb = new StringBuffer();
        if (attributes != null) {
            for(int i=0; i<attributes.getLength(); i++) {
                Node node = attributes.item(i);
                sb.append(" ").append(node.getNodeName()).append("=\"").append(node.getNodeValue()).append("\"");
            }
        }
        return sb.toString();
    }


    private void spaces(BufferedWriter bw, int indent) throws IOException {
        bw.write(spaces.substring(0, indent));
    }
}
