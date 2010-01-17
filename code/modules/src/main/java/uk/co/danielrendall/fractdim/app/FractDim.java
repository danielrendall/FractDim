package uk.co.danielrendall.fractdim.app;

import org.apache.log4j.Logger;
import org.bs.mdi.*;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.generate.Procedure;
import uk.co.danielrendall.fractdim.geom.Point;
import uk.co.danielrendall.fractdim.app.datamodel.ModelStatusListener;
import uk.co.danielrendall.fractdim.app.datamodel.CompoundDataModel;
import uk.co.danielrendall.fractdim.app.datamodel.GenerateSettings;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.fractdim.app.FDDocument;
import uk.co.danielrendall.fractdim.app.gui.GenerateDialog;

import javax.swing.*;

/**
 * Run Fractal Dimension as a GUI app
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

    public void generateNewFractal() {
        CompoundDataModel model = new CompoundDataModel(GenerateSettings.class);

        GenerateDialog gd = new GenerateDialog();
        gd.bindToModel(model);
        gd.setModel(new GenerateSettings());

        int res = JOptionPane.showOptionDialog((JFrame) getMainWindow(), gd, "Fractal options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);


        if (res == JOptionPane.YES_OPTION) {
            setStatus(tr("Generating..."));
            setBusy(true);

            Generator gen = new Generator();

            GenerateSettings settings = (GenerateSettings) model.getNewModel();

            String type = settings.getFractalType();
            Document doc = null;
            try {
                Class clazz = Class.forName("uk.co.danielrendall.fractdim.generate.fractals." + type);

                SVGDocument svg = gen.generateFractal(((Procedure)clazz.newInstance()), new Point(0, 0), new Point(settings.getEndX(), settings.getEndY()), settings.getDepth());

                doc = Document.createNew();
                ((FDData) doc.getData()).setSvgWithMetadata(svg);
                documents.add(doc);
                doc.syncViewsWithData();
                setStatus(tr("Ready"));
            } catch (Exception e) {
                Log.app.warn("Couldn't load fractal type '" + type + "' - " + e.getMessage());
            }

            setBusy(false);
            Application.getMessageDispatcher().dispatch(
                    doc, FractDim.DOCUMENT_GENERATED, doc);
        }
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
