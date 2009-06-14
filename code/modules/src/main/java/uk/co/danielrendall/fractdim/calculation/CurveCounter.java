package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.geom.ParametricCurve;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDGraphics2D;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;

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
            Log.app.warn("Transcoder exception - couldn't transcode at - " + e.getMessage());
        } catch (Exception e) {
            Log.app.warn("Exception - couldn't transcode at - " + e.getMessage());
        }
        return curveCount;
    }

    public void handleCurve(ParametricCurve curve) {
        curveCount++;
    }

}
