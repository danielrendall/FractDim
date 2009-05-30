package uk.co.danielrendall.fractdim.calculation;

import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.log4j.Logger;

/**
 * @author Daniel Rendall
 * @created 24-May-2009 11:00:39
 */
public class Calculator {
    private static final Logger log = Logger.getLogger(Calculator.class);

    private final FDTranscoder transcoder;
    private final TranscoderOutput output;

    public Calculator() {
        transcoder = new FDTranscoder();
        output = new TranscoderOutput();
    }

    public CalculationResult process(SVGDocument svgDoc) {

        CalculationResult result = new CalculationResult();
        TranscoderInput input = new TranscoderInput(svgDoc);

//        for (double d = 0.1d; d < 11.0d; d += 0.1d) {
        double d = 100;
            Grid grid = new Grid(d);
            try {
                transcoder.transcode(input, output, grid);
                result.addResult(0.0d, d, grid.getSquareCount());
            } catch (TranscoderException e) {
                log.warn("Couldn't transcode at resolution " + d + " - " + e.getMessage());
            }
//        }
        return result;
    }
}
