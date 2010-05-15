package uk.co.danielrendall.fractdim.app;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.gui.FractalPanel;
import uk.co.danielrendall.fractdim.app.gui.MainWindow;
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
import java.util.prefs.Preferences;

/**
 * Run Fractal Dimension as a GUI app
 */
public class FractDim {

    private final static FractDim fractDim = new FractDim();

    private final static int DEFAULT_WIDTH = 1024;
    private final static int DEFAULT_HEIGHT = 768;

    private final MainWindow window;
    private final JFileChooser openFileChooser;
    private final JFileChooser exportFileChooser;
    private final Map<FractalPanel, FractalController> controllers = new HashMap<FractalPanel, FractalController>();
    private FractalController currentController = null;

    private final Preferences prefs;

    private static AtomicInteger ID = new AtomicInteger(1);
    private final String PREF_DIRECTORY_SVG = "initial.directory.svg";
    private final String PREF_DIRECTORY_XLS = "initial.directory.xls";
    private final String PREF_SCREEN_X = "initial.screenx";
    private final String PREF_SCREEN_Y = "initial.screeny";
    private final String PREF_WIDTH = "initial.width";
    private final String PREF_HEIGHT = "initial.height";

    private String xlsFileDir = "";
    private String svgFileDir = "";

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
        prefs = Preferences.userRoot().node("/uk/co/danielrendall/fractdim");
        File svgDir = new File(prefs.get(PREF_DIRECTORY_SVG, System.getProperty("user.home")));
        if (svgDir.exists()) {
            openFileChooser = new JFileChooser(svgDir);
            svgFileDir = svgDir.getAbsolutePath();
        } else {
            openFileChooser = new JFileChooser();
        }
        File xlsDir = new File(prefs.get(PREF_DIRECTORY_XLS, svgDir.getAbsolutePath()));
        if (xlsDir.exists()) {
            exportFileChooser = new JFileChooser(xlsDir);
            xlsFileDir = xlsDir.getAbsolutePath();
        } else {
            exportFileChooser = new JFileChooser();
        }

    }
    
    private void run(String[] args) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int xCenter = (int)(d.getWidth() / 2.0d);
        int yCenter = (int)(d.getHeight() / 2.0d);

        int width = prefs.getInt(PREF_WIDTH, DEFAULT_WIDTH);
        int height = prefs.getInt(PREF_HEIGHT, DEFAULT_HEIGHT);

        if ((width < 100) || (height < 100)) {
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
        }

        int defaultLeft = xCenter - (width / 2);
        int defaultTop = yCenter - (height / 2);

        int left = prefs.getInt(PREF_SCREEN_X, defaultLeft);
        int top = prefs.getInt(PREF_SCREEN_Y, defaultTop);

        window.setBounds(left, top, width, height);
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

    public File getExportFile(String possibleName) {
        exportFileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls"));
        if (possibleName.toLowerCase().endsWith(".svg")) {
            possibleName = possibleName.substring(0, possibleName.lastIndexOf(".")) + ".xls";
        }
        File suggested = new File(xlsFileDir, possibleName);
        exportFileChooser.setSelectedFile(suggested);
        int returnVal = exportFileChooser.showSaveDialog(window);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = exportFileChooser.getSelectedFile();
            Log.app.debug("Chosen file: " +
                selectedFile.getName());
            xlsFileDir = selectedFile.getParent();
            prefs.put(PREF_DIRECTORY_XLS, xlsFileDir);

            return selectedFile;
        }
        return null;
    }


    public void openFile(ActionEvent e) {
        Log.app.debug("Open File");
        openFileChooser.setFileFilter(new FileNameExtensionFilter("SVG Files", "svg"));
        int returnVal = openFileChooser.showOpenDialog(window);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = openFileChooser.getSelectedFile();
            Log.app.debug("Chosen file: " +
                selectedFile.getName());
            try {
                FractalController controller = FractalController.fromFile(selectedFile);
                add(controller);
                svgFileDir = selectedFile.getParent();
                prefs.put(PREF_DIRECTORY_SVG, svgFileDir);
            } catch (IOException ex) {
                Log.app.warn("Unable to load document: " + ex.getMessage());
            }

        }
    }

    public void quit(ActionEvent e) {
        Log.app.debug("Quit");
        Rectangle bounds = window.getBounds();

        prefs.putInt(PREF_SCREEN_X, bounds.x);
        prefs.putInt(PREF_SCREEN_Y, bounds.y);
        prefs.putInt(PREF_WIDTH, bounds.width);
        prefs.putInt(PREF_HEIGHT, bounds.height);
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
        controller.notifyRemoving();
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
