package uk.co.danielrendall.fractdim.calculation;

import org.apache.batik.transcoder.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 21:42:55
 */
public class FDTranscoder extends SVGAbstractTranscoder {

    private GridCollection grids;

    public void transcode(TranscoderInput input, TranscoderOutput output, GridCollection grids) throws TranscoderException {
        this.grids = grids;
        super.transcode(input, output);
    }

    @Override
    protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
        super.transcode(document, uri, output);
        FDGraphics2D g2d = new FDGraphics2D(grids);
        root.paint(g2d);
    }

    
}
