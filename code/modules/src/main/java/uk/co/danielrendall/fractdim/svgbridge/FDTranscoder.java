package uk.co.danielrendall.fractdim.svgbridge;

import org.apache.batik.transcoder.*;
import org.w3c.dom.Document;
import uk.co.danielrendall.fractdim.calculation.GridCollection;

/**
 * @author Daniel Rendall
 * @created 20-May-2009 21:42:55
 */
public class FDTranscoder extends SVGAbstractTranscoder {

    private final FDGraphics2D graphics;

    public FDTranscoder(FDGraphics2D graphics) {
        this.graphics = graphics;
    }

    public void transcode(TranscoderInput input, TranscoderOutput output) throws TranscoderException {
        super.transcode(input, output);
    }

    @Override
    protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
        super.transcode(document, uri, output);
        root.paint(graphics);
    }

    
}
