/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of FractDim.
 *
 * FractDim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FractDim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FractDim.  If not, see <http://www.gnu.org/licenses/>
 */

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
import uk.co.danielrendall.mathlib.geom2d.Point;

/**
 * @author Daniel Rendall
 * @created 04-Jun-2009 21:11:05
 */
public class FractalMetadataUtil extends FDGraphics2D {

    private final SVGDocument svgDoc;
    private int curveCount = 0;
    private BoundingBox boundingBox = BoundingBox.empty();


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
        boundingBox = boundingBox.expandToInclude(curve.getBoundingBox());
    }

    private int getCurveCount() {
        return curveCount;
    }

    private BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
