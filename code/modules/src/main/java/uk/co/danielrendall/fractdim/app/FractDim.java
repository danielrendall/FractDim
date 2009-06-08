package uk.co.danielrendall.fractdim.app;

import org.apache.log4j.Logger;
import org.bs.mdi.*;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.app.workers.CalculateStatisticsWorker;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

/**
 * Hello world!
 */
public class FractDim extends Application {

    private static final Logger log = Logger.getLogger(FractDim.class);

    private final static int DOCUMENT_GENERATED = 256;

    private final Map<Document, List<SwingWorker<?, ?>>> workersForDoc;

    public FractDim() {
        super();
//        threadManager = new FDThreadManager(5);
        workersForDoc = new HashMap<Document, List<SwingWorker<?,?>>>();
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Fractal Dimension Calculator");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY");
        System.out.println("This is free software, and you are welcome to redistribute it under certain conditions");

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            FractDim fractDim = new FractDim();
            fractDim.run(args);
        }
        catch (Exception e) {
            // handle exception
        }

    }

    public String getName() {
        return "Fractal Dimension";
    }

    protected FileIOModule[] createFileIOModules() {
        return new FileIOModule[] { new FDFileIOModule() };
    }

    protected ActionConverter[] createActionConverters() {
        return new ActionConverter[] { new FDActionConverter() };
    }

    protected MainWindow createMainWindow() {
        return new FDMainWindow();
    }

    protected Resources createResources() {
        return new FDResources();
    }

    public RootData createRootData() {
        return new FDData();
    }

    public RootView createRootView() {
        return new FDView();
    }

    protected synchronized List<SwingWorker<?,?>> getWorkersForDocument(Document doc) {
        List<SwingWorker<?,?>> workers = workersForDoc.get(doc);
        if (workers == null) {
            workers = new LinkedList<SwingWorker<?,?>>();
            workersForDoc.put(doc, workers);
        }
        return workers;
    }

    @Override
    public boolean close() {
        // do some tidying
        return super.close();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Document generateNewFractal() {
        setStatus(tr("Generating..."));
        setBusy(true);

        Generator gen = new Generator();
        SVGDocument svg = gen.generateFractal(new KochCurve(), new Point(0, 0), new Point(1000, 750), 4);

        Document doc = Document.createNew();
        documents.add(doc);
        ((FDData)doc.getData()).setSvgDoc(svg);
        doc.syncViewsWithData();

        setStatus(tr("Ready"));
        setBusy(false);
        Application.getMessageDispatcher().dispatch(
                doc, FractDim.DOCUMENT_GENERATED, doc);

        return doc;

    }

    @Override
    public void processMessage(Object source, int type, Object argument) {
        super.processMessage(source, type, argument);
        switch (type) {
            case DOCUMENT_GENERATED:
            case MessageDispatcher.DOCUMENT_OPENED:
                Document doc = (Document) argument;
                CalculateStatisticsWorker csw = new CalculateStatisticsWorker(doc);
                csw.execute();
                break;
        }
    }

}
