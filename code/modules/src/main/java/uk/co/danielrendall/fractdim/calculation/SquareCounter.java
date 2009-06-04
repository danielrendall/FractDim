package uk.co.danielrendall.fractdim.calculation;

import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderException;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.svgbridge.FDTranscoder;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 11:00:39
 */
public class SquareCounter  {

    private final SVGDocument svgDoc;

    public SquareCounter(SVGDocument svgDoc) {
        this.svgDoc = svgDoc;
    }

    public SquareCountingResult process() {

        TranscoderInput input = new TranscoderInput(svgDoc);

        GridCollection grids = GridCollection.createCollection(1000, 1, 10, 1, 4, 4);
        FDTranscoder transcoder = new FDTranscoder(grids);


        try {
            transcoder.transcode(input, new TranscoderOutput());
        } catch (TranscoderException e) {
            Log.app.warn("Couldn't transcode at - " + e.getMessage());
        }
        Log.misc.info("There were " + GridSquare.createCount + " squares created");
        return new SquareCountingResult(grids);
    }
}
