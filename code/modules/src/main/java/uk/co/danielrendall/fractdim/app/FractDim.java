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

package uk.co.danielrendall.fractdim.app;

import org.apache.log4j.*;
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

    private final static Preferences prefs;

    static {
        prefs = Preferences.userRoot().node("/uk/co/danielrendall/fractdim");
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        System.out.println("Using look and feel: " + lookAndFeel);
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.out.println("Couldn't set look and feel - " + e.getMessage());
        }
        try {
            Preferences loggingPrefs = prefs.node("logging");
            Preferences loggers = loggingPrefs.node("loggers");
            String layout = loggingPrefs.get("layout", "%m%n");
            loggingPrefs.put("layout", layout);
            File logFile = new File(System.getProperty("user.home"), "fractdim.log");
            String logFileName = loggingPrefs.get("logfile", logFile.getAbsolutePath());
            loggingPrefs.put("logfile", logFileName);

            Logger.getRootLogger().addAppender(new FileAppender(new PatternLayout(layout),logFileName));
            setLogLevel(Log.gui, loggers, Level.INFO);
            setLogLevel(Log.thread, loggers, Level.INFO);
            setLogLevel(Log.app, loggers, Level.INFO);
            setLogLevel(Log.misc, loggers, Level.INFO);
            setLogLevel(Log.test, loggers, Level.INFO);
            setLogLevel(Log.messages, loggers, Level.INFO);
            setLogLevel(Log.geom, loggers, Level.INFO);
            setLogLevel(Log.calc, loggers, Level.INFO);
            setLogLevel(Log.points, loggers, Level.INFO);
            setLogLevel(Log.squares, loggers, Level.INFO);
            setLogLevel(Log.recursion, loggers, Level.INFO);
        } catch (IOException e) {
            System.out.println("Unable to configure logging - " + e.getMessage());
        }
    }

    private static void setLogLevel(Logger aLog, Preferences loggingPrefs, Level def) {
        String level = loggingPrefs.get(aLog.getName(), def.toString());
        loggingPrefs.put(aLog.getName(), level);
        aLog.setLevel(Level.toLevel(level));
        System.out.println("Log " + aLog.getName() + " is at level " + level);
    }

    private final static FractDim fractDim = new FractDim();

    private final static int DEFAULT_WIDTH = 1024;
    private final static int DEFAULT_HEIGHT = 768;

    private final MainWindow window;
    private final JFileChooser openFileChooser;
    private final JFileChooser exportFileChooser;
    private final Map<FractalPanel, FractalController> controllers = new HashMap<FractalPanel, FractalController>();
    private FractalController currentController = null;


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

        fractDim.run(args);
    }

    public static FractDim instance() {
        return fractDim;
    }
    public FractDim() {
        window = new MainWindow();
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
