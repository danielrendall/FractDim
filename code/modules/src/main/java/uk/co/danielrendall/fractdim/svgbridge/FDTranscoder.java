package uk.co.danielrendall.fractdim.svgbridge;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.w3c.dom.Document;

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
