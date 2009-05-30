package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 21:42:55
 */
public class FDTranscoder extends SVGAbstractTranscoder {
    private static final Logger log = Logger.getLogger(FDTranscoder.class);

    private Grid grid;

    public void transcode(TranscoderInput input, TranscoderOutput output, Grid grid) throws TranscoderException {
        this.grid = grid;
        super.transcode(input, output);
    }

    @Override
    protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
        super.transcode(document, uri, output);
        FDGraphics2D g2d = new FDGraphics2D(grid);
        root.paint(g2d);
    }

    
}
