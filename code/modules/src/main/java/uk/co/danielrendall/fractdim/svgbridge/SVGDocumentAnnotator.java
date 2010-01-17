package uk.co.danielrendall.fractdim.svgbridge;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.geom.BoundingBox;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 21:11:05
 */
public class SVGDocumentAnnotator extends FDGraphics2D {

    private int curveCount = 0;
    private BoundingBox boundingBox = new BoundingBox();

    public static SVGWithMetadata annotate(SVGDocument svgdoc) {
        if (svgdoc instanceof SVGWithMetadata) return (SVGWithMetadata) svgdoc;
        SVGDocumentAnnotator annotator = new SVGDocumentAnnotator();
        annotator.analyse(svgdoc);
        return new SVGWithMetadata(svgdoc, annotator.getCurveCount(), annotator.getBoundingBox());
    }

    private void analyse(SVGDocument svgDoc) {
        TranscoderInput input = new TranscoderInput(svgDoc);
        FDTranscoder transcoder = new FDTranscoder(this);
        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Transcoder exception - couldn't transcode at - " + e.getMessage());
        } catch (Exception e) {
            Log.app.warn("Exception - couldn't transcode at - " + e.getMessage());
        }
    }

    public void handleCurve(ParametricCurve curve) {
        curveCount++;
        Log.geom.debug("Curve: " + curve);

        boundingBox = boundingBox.expandToInclude(curve.getBoundingBox());
    }

    private int getCurveCount() {
        return curveCount;
    }

    private BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
