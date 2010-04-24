package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.app.model.FractalDocumentMetadata;
import uk.co.danielrendall.mathlib.geom2d.ParametricCurve;
import uk.co.danielrendall.mathlib.geom2d.BoundingBox;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 21:11:05
 */
public class FractalMetadataUtil extends FDGraphics2D {

    private final SVGDocument svgDoc;
    private int curveCount = 0;
    private BoundingBox boundingBox = new BoundingBox();


    public static FractalDocumentMetadata getMetadata(SVGDocument svgDocument) {
        FractalMetadataUtil annotator = new FractalMetadataUtil(svgDocument);
        return annotator.analyse(svgDocument);
    }

    public FractalMetadataUtil(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
    }

    private FractalDocumentMetadata analyse(SVGDocument svgDoc) {
        TranscoderInput input = new TranscoderInput(svgDoc);
        FDTranscoder transcoder = new FDTranscoder(this);
        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Transcoder exception - couldn't transcode at - " + e.getMessage());
        } catch (Exception e) {
            Log.app.warn("Exception - couldn't transcode at - " + e.getMessage());
        }
        return new FractalDocumentMetadata(curveCount, boundingBox);
    }

    public void handleCurve(ParametricCurve curve) {
        curveCount++;
//        Log.geom.debug("Curve: " + curve);

        boundingBox = boundingBox.expandToInclude(curve.getBoundingBox());
    }

    private int getCurveCount() {
        return curveCount;
    }

    private BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
