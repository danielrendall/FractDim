package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.*;
import org.w3c.dom.svg.SVGDocument;
import org.apache.log4j.Logger;

import javax.swing.*;

import uk.co.danielrendall.fractdim.generate.Generator;
import uk.co.danielrendall.fractdim.generate.fractals.KochCurve;
import uk.co.danielrendall.fractdim.geom.Point;

/**
 * Hello world!
 */
public class FractDim extends Application {

    private static final Logger log = Logger.getLogger(FractDim.class);


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
        return doc;

    }


}
