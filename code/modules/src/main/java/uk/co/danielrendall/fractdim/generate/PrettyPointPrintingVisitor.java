package uk.co.danielrendall.fractdim.generate;

import uk.co.danielrendall.fractdim.geom.Point;

import java.util.Iterator;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 12:08:17
 */
public class PrettyPointPrintingVisitor extends FractalVisitor {


    public PrettyPointPrintingVisitor(int desiredDepth) {
        super(desiredDepth);
    }

    public void visit(Fractal aFractal) {
        int depth = aFractal.getDepth();
        String indent = "               ".substring(0, depth);
        if (depth == desiredDepth) {
            Point endPoint = aFractal.getEndPoint();
            System.out.println(indent + " " + endPoint);
        } else {
            for (Iterator<Fractal> it = aFractal.childIterator(); it.hasNext();) {
                it.next().accept(this);
            }
        }
    }
}
