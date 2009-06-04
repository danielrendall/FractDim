package uk.co.danielrendall.fractdim.calculation;

import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;
import uk.co.danielrendall.fractdim.geom.Line;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.logging.Log;
import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderException;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 21:11:05
 */
public class CurveCounter extends FDGraphics2D {

    private final SVGDocument svgDoc;
    private int curveCount = 0;

    public CurveCounter(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
    }

    public int getCurveCount() {
        TranscoderInput input = new TranscoderInput(svgDoc);
        FDTranscoder transcoder = new FDTranscoder(this);
        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Couldn't transcode at - " + e.getMessage());
        }
        return curveCount;
    }

    public void handleCurve(ParametricCurve curve) {
        curveCount++;
    }

}
