package uk.co.danielrendall.fractdim.generate;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 12:05:15
 */
public abstract class FractalVisitor {

    protected final int desiredDepth;

    public FractalVisitor(int desiredDepth) {
        this.desiredDepth = desiredDepth;
    }

    public int getDesiredDepth() {
        return desiredDepth;
    }

    public abstract void visit(Fractal aFractal);
}
