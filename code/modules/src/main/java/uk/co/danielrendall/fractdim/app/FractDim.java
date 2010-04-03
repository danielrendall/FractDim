package uk.co.danielrendall.fractdim.app;

import org.apache.log4j.Logger;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.MainWindow;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Run Fractal Dimension as a GUI app
 */
public class FractDim {

    private final static FractDim fractDim = new FractDim();

    private final static int DEFAULT_WIDTH = 640;
    private final static int DEFAULT_HEIGHT = 480;

    private final MainWindow window;
    private final JFileChooser chooser;

    public static void main(String[] args) throws Exception {
        System.out.println("Fractal Dimension Calculator");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY");
        System.out.println("This is free software, and you are welcome to redistribute it under certain conditions");

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            fractDim.run(args);
        }
        catch (Exception e) {
            // handle exception
        }
    }

    public static FractDim instance() {
        return fractDim;
    }
    public FractDim() {
        window = new MainWindow();
        chooser = new JFileChooser();
    }
    
    private void run(String[] args) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int xCenter = (int)(d.getWidth() / 2.0d);
        int yCenter = (int)(d.getHeight() / 2.0d);
        int left = xCenter - (DEFAULT_WIDTH / 2);
        int top = yCenter - (DEFAULT_HEIGHT / 2);
        window.setBounds(left, top, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        window.setVisible(true);
    }

//    public void generateNewFractal() {
//        CompoundDataModel model = new CompoundDataModel(GenerateSettings.class);
//
//        GenerateDialog gd = new GenerateDialog();
//        gd.bindToModel(model);
//        gd.setModel(new GenerateSettings());
//
//        int res = JOptionPane.showOptionDialog((JFrame) getMainWindow(), gd, "Fractal options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
//
//
//        if (res == JOptionPane.YES_OPTION) {
//            setStatus(tr("Generating..."));
//            setBusy(true);
//
//            Generator gen = new Generator();
//
//            GenerateSettings settings = (GenerateSettings) model.getNewModel();
//
//            String type = settings.getFractalType();
//            Document doc = null;
//            try {
//                Class clazz = Class.forName("uk.co.danielrendall.fractdim.generate.fractals." + type);
//
//                SVGDocument svg = gen.generateFractal(((Procedure)clazz.newInstance()), new Point(0, 0), new Point(settings.getEndX(), settings.getEndY()), settings.getDepth());
//
//                doc = Document.createNew();
//                ((FDData) doc.getData()).setSvgWithMetadata(svg);
//                documents.add(doc);
//                doc.syncViewsWithData();
//                setStatus(tr("Ready"));
//            } catch (Exception e) {
//                Log.app.warn("Couldn't load fractal type '" + type + "' - " + e.getMessage());
//            }
//
//            setBusy(false);
//            Application.getMessageDispatcher().dispatch(
//                    doc, FractDim.DOCUMENT_GENERATED, doc);
//        }
//    }


    public void openFile(ActionEvent e) {
        Log.app.debug("Open File");
        chooser.setFileFilter(new FileNameExtensionFilter("SVG Files", "svg"));
        int returnVal = chooser.showOpenDialog(window);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           Log.app.debug("Chosen file: " +
                chooser.getSelectedFile().getName());
            
        }
    }

    public void closeFile(ActionEvent e) {
        Log.app.debug("Close File");
    }

    public void quit(ActionEvent e) {
        Log.app.debug("Quit");
        // TODO - some cleanup
        window.close();
    }

    public void add(FractalPanel panel) {
        window.addTab(panel);
    }

    public void remove(FractalPanel panel) {
        window.removeTab(panel);
    }
}
