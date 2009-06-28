package uk.co.danielrendall.fractdim.app;

import org.apache.log4j.Logger;
import org.bs.mdi.*;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.app.workers.CalculateStatisticsWorker;
import uk.co.danielrendall.fractdim.app.workers.Notifiable;
import uk.co.danielrendall.fractdim.app.workers.NotifyingWorker;
import uk.co.danielrendall.fractdim.app.datamodel.ModelStatusListener;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.FDDocument;

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

    private FDDocument lastSelectedDocument = null;

    public FractDim() {
        super();
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
        return new FileIOModule[]{new FDFileIOModule()};
    }

    protected ActionConverter[] createActionConverters() {
        return new ActionConverter[]{new FDActionConverter()};
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

    public Document createDocument() {
        return new FDDocument(this);
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
        ((FDData) doc.getData()).setSvgDoc(svg);
        documents.add(doc);
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
        Log.messages.debug("Message: " + type + " argument " + (argument != null ? argument.toString() : "null"));
        switch (type) {
            case DOCUMENT_GENERATED:
            case MessageDispatcher.DOCUMENT_OPENED: {
                final FDDocument doc = (FDDocument) argument;
                doc.init();
            }
            break;
            case MessageDispatcher.DOCUMENT_SELECTED:
                if (argument != null) {
                    final FDDocument doc = (FDDocument) argument;
                    if (lastSelectedDocument != null) {
                        lastSelectedDocument.removeModelStatusListener(calculateOptionListener);

                    }
                    doc.addModelStatusListener(calculateOptionListener);
                    lastSelectedDocument = doc;
                }
        }
    }

    private final ModelStatusListener calculateOptionListener = new ModelStatusListener() {
        public void modelIsGood() {
            ((FDCommands)((FDMainWindow)getMainWindow()).getCommands()).getFileCalculateCommand().setAvailable(true);
        }

        public void modelIsBad() {
            ((FDCommands)((FDMainWindow)getMainWindow()).getCommands()).getFileCalculateCommand().setAvailable(false);
        }
    };

}
