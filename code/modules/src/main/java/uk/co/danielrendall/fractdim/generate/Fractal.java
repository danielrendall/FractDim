package uk.co.danielrendall.fractdim.generate;

import uk.co.danielrendall.mathlib.geom2d.Line;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 11:37:56
 */
public class Fractal {

    private final Line line;
    private final int depth;
    private final List<Fractal> children;
    private final Procedure procedure;

    public Fractal(Line line, Procedure procedure) {
        this(0, line, procedure);
    }

    private Fractal(int depth, Line line, Procedure procedure) {
        this.depth = depth;
        this.line = line;
        children = new ArrayList<Fractal>();
        this.procedure = procedure;
    }

    public Point getStart() {
        return line.getStart();
    }

    public void accept(FractalVisitor fv) {
        Line[] lines = procedure.expand(line, depth);
        for (Line newLine : lines) {
            children.add(new Fractal(depth + 1, newLine, procedure));
        }
        fv.visit(this);
        children.clear();
    }

    public Point getEndPoint() {
        return line.getEnd();
    }

    public Iterator<Fractal> childIterator() {
        return Collections.unmodifiableList(children).iterator();
    }

    public int getDepth() {
        return depth;
    }
}
