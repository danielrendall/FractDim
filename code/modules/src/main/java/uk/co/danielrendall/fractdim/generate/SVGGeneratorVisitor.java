package uk.co.danielrendall.fractdim.generate;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.geom.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Rendall
 * @created 23-May-2009 12:33:44
 */
public class SVGGeneratorVisitor  extends FractalVisitor {

    private final SVGDocument doc;

    private final Element root;
    private final Element rootGroup;
    private Element currentParent;

    private int elID = 0;

    private final static String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    private final static String pathStyle = "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1";

    private double minX = Double.MAX_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;
    private double maxY = Double.MIN_VALUE;

    public SVGGeneratorVisitor(int desiredDepth) {
        super(desiredDepth);
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
        root = doc.getDocumentElement();
        rootGroup = doc.createElementNS(svgNS, "g");
        rootGroup.setAttributeNS(null, "id", "g" + newID());
        root.appendChild(rootGroup);

        currentParent = rootGroup;

    }

    public SVGDocument getDocument() {

        int width = (int) Math.ceil(maxX - minX);
        int height = (int) Math.ceil(maxY - minY);

        rootGroup.setAttributeNS(null, "transform", String.format("translate(%s,%s)", -minX, -minY));
        
        root.setAttributeNS(null, "width", "" + width);
        root.setAttributeNS(null, "height", "" + height);
        return doc;
    }

    public void visit(Fractal aFractal) {

        int depth = aFractal.getDepth();

        if (depth == desiredDepth) {
            Point startPoint = aFractal.getStart();
            List<Point> endPoints = new ArrayList<Point>();
            for (Iterator<Fractal> it = aFractal.childIterator(); it.hasNext();) {
                endPoints.add(it.next().getEndPoint());
            }

            Element path = doc.createElementNS(svgNS, "path");
            path.setAttributeNS(null, "id", "path" + newID());
            path.setAttributeNS(null, "style", pathStyle);
            path.setAttributeNS(null, "d", genPath(startPoint, endPoints));

            currentParent.appendChild(path);

        } else {
            Element oldRoot = currentParent;
            Element group = doc.createElementNS(svgNS, "g");
            group.setAttributeNS(null, "id", "g" + newID());
            currentParent = group;
            for (Iterator<Fractal> it = aFractal.childIterator(); it.hasNext();) {
                it.next().accept(this);
            }
            currentParent = oldRoot;
            currentParent.appendChild(group);
        }
    }

    private String genPath(Point startPoint, List<Point> endPoints) {
        updateBounds(startPoint);
        StringBuffer sb = new StringBuffer(String.format("M %s,%s", startPoint.x(), startPoint.y()));
        for (Point point : endPoints) {
            updateBounds(point);
            sb.append(String.format(" L %s,%s", point.x(), point.y()));
        }
        return sb.toString();
    }

    private void updateBounds(Point p) {
        minX = Math.min(minX, p.x());
        minY = Math.min(minY, p.y());
        maxX = Math.max(maxX, p.x());
        maxY = Math.max(maxY, p.y());
    }

    private int newID() {
        return ++elID;
    }
}
