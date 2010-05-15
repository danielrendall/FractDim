package uk.co.danielrendall.fractdim.app;

import org.apache.batik.swing.JSVGCanvas;
import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.MainWindow;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Run Fractal Dimension as a GUI app
 */
public class FractDim {

    private final static FractDim fractDim = new FractDim();

    private final static int DEFAULT_WIDTH = 1024;
    private final static int DEFAULT_HEIGHT = 768;

    private final MainWindow window;
    private final JFileChooser chooser;
    private final Map<FractalPanel, FractalController> controllers = new HashMap<FractalPanel, FractalController>();
    private FractalController currentController = null;

    private static AtomicInteger ID = new AtomicInteger(1);

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
//        chooser = new JFileChooser("/home/daniel/Development/FractDim/resources/samples");
        chooser = new JFileChooser("/home/daniel/Development/FractDim/code/modules/src/test/resources/svg");
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
            File selectedFile = chooser.getSelectedFile();
            Log.app.debug("Chosen file: " +
                selectedFile.getName());
            try {
                FractalController controller = FractalController.fromFile(selectedFile);
                add(controller);
            } catch (IOException ex) {
                Log.app.warn("Unable to load document: " + ex.getMessage());
            }

        }
    }

    public void quit(ActionEvent e) {
        Log.app.debug("Quit");
        // TODO - some cleanup
        window.close();
    }

    public void add(FractalController controller) {
        FractalDocument document = controller.getDocument();
        FractalPanel panel = controller.getPanel();
        controllers.put(panel, controller);
        window.addTab(document.getName(), panel);
        controller.notifyAdded();
    }

    public void remove(FractalController controller) {
        FractalPanel panel = controller.getPanel();
        window.removeTab(panel);
        controllers.remove(panel);
        controller.notifyRemoved();
    }

    /**
     * Called when the the user has switched tabs, so we can set the currentController
     * to be the correct one for the currently visible panel.
     * @param panel The newly selected panel
     */
    public synchronized void notifyPanelChange(FractalPanel panel) {
        if (panel == null) {
            // all closed
            currentController = null;
            window.disableMenuItems();

        } else {
            FractalController controller = controllers.get(panel);
            currentController = controller;
            if (controller == null) {
                Log.app.warn("Controller shouldn't be null!");
            } else {
                currentController.enableMenuItems();
            }
        }
    }


    /**
     * Called when a controller has done something (which may have been running in the background) and may want
     * to update global state (i.e. selected menu items). If the controller doing the calling is current, it gets
     * to do the update, otherwise it can wait until its panel is next selected.
     * @param fractalController
     */
    public synchronized void updateMeIfCurrent(FractalController fractalController) {
        if (fractalController == currentController) {
            fractalController.enableMenuItems();
        }
    }

    public static int newId() {
        return ID.getAndIncrement();
    }
}
