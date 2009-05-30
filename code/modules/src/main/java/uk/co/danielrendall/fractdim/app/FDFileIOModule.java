package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.*;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.io.IOException;
import java.io.File;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:35:43
 */
public class FDFileIOModule implements FileLoader, FileSaver, FileExporter {

    static FileFormat[] formats = {new SVGFileFormat() };

    public RootData load(String filename) throws FileIOException {
        FDData data = new FDData();

        File file = new File(filename);
        if (!file.exists()) {
            throw new FileIOException(FileIOException.ERR_NOSUCHFILE, filename);
        }

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

        try {
            data.setSvgDoc((SVGDocument)f.createDocument(file.toURI().toString()));
        } catch (IOException e) {
            throw new FileIOException(FileIOException.ERR_UNKNOWN, filename);
        }

        return data;
    }

    public FileFormat[] getSupportedFormats() {
        return formats;
    }

    public boolean canHandle(String filename) {
        return formats[0].accept(filename);
    }

    public String getDescription() {
        return Application.tr("SVG File");
    }

    public void save(RootData data, String filename) throws FileIOException {
        // Nothing to do
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void export(RootData data) throws FileIOException {
        // Nothing to do...
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public static class SVGFileFormat extends FileFormat {

        static String extensions[] = {"svg"};
        static String description = Application.tr("SVG Files");

        public SVGFileFormat() {
            super(extensions, description);
        }

    }
}
