package uk.co.danielrendall.fractdim.app.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 19-Jul-2009
 * Time: 18:22:37
 * To change this template use File | Settings | File Templates.
 */
public class GenerateSettings {
    private double endX;
    private double endY;
    private int depth;
    private String fractalType;

    public GenerateSettings() {
        this.endX = 1000;
        this.endY = 1000;
        this.depth = 3;
        fractalType = "KochCurve";
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getFractalType() {
        return fractalType;
    }

    public void setFractalType(String fractalType) {
        this.fractalType = fractalType;
    }
}
