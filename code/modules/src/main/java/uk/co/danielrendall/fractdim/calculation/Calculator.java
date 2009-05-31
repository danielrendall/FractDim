package uk.co.danielrendall.fractdim.calculation;

import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderException;
import uk.co.danielrendall.fractdim.logging.Log;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 11:00:39
 */
public class Calculator {

    private final FDTranscoder transcoder;
    private final TranscoderOutput output;

    public Calculator() {
        transcoder = new FDTranscoder();
        output = new TranscoderOutput();
    }

    public CalculationResult process(SVGDocument svgDoc) {

        TranscoderInput input = new TranscoderInput(svgDoc);

        GridCollection grids = new GridCollection(1000);

//        for (double d = 0.1d; d < 11.0d; d += 0.1d) {
        for (double d = 100d; d < 101d; d += 50d) {
            grids.addGrid(new Grid(d));
        }
        
        try {
            transcoder.transcode(input, output, grids);
        } catch (TranscoderException e) {
            Log.app.warn("Couldn't transcode at - " + e.getMessage());
        }
        return new CalculationResult(grids);
    }
}
