package uk.co.danielrendall.fractdim.svgbridge;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-Jan-2010
 * Time: 21:38:53
 * To change this template use File | Settings | File Templates.
 */
public class SVGWithMetadataFactory {

    private final SAXSVGDocumentFactory factory;

    public SVGWithMetadataFactory() {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
    }

    public SVGWithMetadata createFromFile(File file) throws IOException {
        SVGDocument doc = factory.createSVGDocument(file.toURI().toString());
        return SVGDocumentAnnotator.annotate(doc);
    }

    public SVGWithMetadata createFromInputStream(InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("Null inputstream");
        }
        File tempFile = File.createTempFile("test", ".svg");
        tempFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tempFile);
        byte[] bytes = new byte[8192];
        int read = 1;
        int pos = 0;
        for(read = is.read(bytes, pos, 8192); read > 0; read = is.read(bytes, pos, 8192)) {
            fos.write(bytes, 0, read);
        }
        fos.close();
        return createFromFile(tempFile);
    }
}
