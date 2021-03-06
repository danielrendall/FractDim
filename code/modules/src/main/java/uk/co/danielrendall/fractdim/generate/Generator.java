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

package uk.co.danielrendall.fractdim.generate;

import org.apache.commons.lang.text.StrTokenizer;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.Point;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 12:18:58
 */
public class Generator {

    @Option(name = "-rules", aliases = {"-r"}, usage = "Procedure to use (default KochCurve)")
    private String rules = "KochCurve";

    @Option(name = "-output", aliases = {"-o"}, usage = "Output file (default <rule>.svg)")
    private String output = "";

    @Option(name = "-start", aliases = {"-s"}, usage = "Start as x,y (default 0,0)")
    private String start = "0,0";

    @Option(name = "-end", aliases = {"-e"}, usage = "End as x,y (default 1000,0)")
    private String end = "1000,0";

    @Option(name = "-depth", aliases = {"-d"}, usage = "Iteration depth (default 3)")
    int depth = 3;


    public static void main(String[] args) {
        Generator generator = new Generator();
        CmdLineParser parser = new CmdLineParser(generator);
        try {
            parser.parseArgument(args);
            generator.run();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }

    public void run() throws CmdLineException {
        Procedure ruleClass;

        try {
            Class clazz = Class.forName("uk.co.danielrendall.fractdim.generate.fractals." + rules);
            if (!Procedure.class.isAssignableFrom(clazz)) {
                throw new CmdLineException("The rules '" + rules + "' are not valid fractal rules");
            }
            ruleClass = (Procedure) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new CmdLineException("The rules '" + rules + "' could not be found");
        } catch (IllegalAccessException e) {
            throw new CmdLineException("The rules '" + rules + "' could not be accessed");
        } catch (InstantiationException e) {
            throw new CmdLineException("The rules '" + rules + "' could not be created");
        }
        if ("".equals(output)) {
            output = rules + ".svg";
        }

        Point startPoint;
        Point endPoint;

        try {
            startPoint = getPoint(start);
        } catch (Exception e) {
            throw new CmdLineException("Start point was invalid");
        }
        try {
            endPoint = getPoint(end);
        } catch (Exception e) {
            throw new CmdLineException("End point was invalid");
        }

        SVGDocument doc = generateFractal(ruleClass, startPoint, endPoint, depth);

        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);

            // Prepare the output file
            File file = new File(output);

            OutputStream fos = new FileOutputStream(file);

            Result result = new StreamResult(fos);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new CmdLineException("Couldn't create file '" + output + "'");
        }


    }

    public SVGDocument generateFractal(Procedure ruleClass, Point startPoint, Point endPoint, int iterDepth) {
        Fractal f = new Fractal(new Line(startPoint, endPoint), ruleClass);
        SVGGeneratorVisitor fv = new SVGGeneratorVisitor(iterDepth);
        f.accept(fv);
        SVGDocument doc = fv.getDocument();
        return doc;
    }


    private Point getPoint(String point) throws Exception {
        StrTokenizer tok = new StrTokenizer(point, ",");
        if (tok.size() != 2) throw new Exception("Wrong number of tokens");
        String xs = tok.getTokenArray()[0].trim();
        String ys = tok.getTokenArray()[1].trim();

        double x = Double.parseDouble(xs);
        double y = Double.parseDouble(ys);
        return new Point(x,y);
    }
}
