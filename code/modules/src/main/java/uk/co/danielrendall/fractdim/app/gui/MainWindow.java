package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 03-Apr-2010
 * Time: 18:55:47
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame {

    private final ActionRepository repository = ActionRepository.instance();
    private final JTabbedPane tabPane;


    public MainWindow() throws HeadlessException {
        super("Fractal Dimension");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                  Log.gui.debug("Window Activated");
               }

               public void windowDeiconified(WindowEvent e) {
                  Log.gui.debug("Window Deiconified");
               }

               public void windowIconified(WindowEvent e) {
                  Log.gui.debug("Window Iconified");
               }

               public void windowOpened(WindowEvent e) {
                  Log.gui.debug("Window Opened");
               }
        });

        setJMenuBar(createMenuBar());
        getContentPane().setLayout(new BorderLayout());
        JToolBar toolBar = createToolBar();
        tabPane = createTabPane();
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(tabPane, BorderLayout.CENTER);
    }

    public void close() {
        dispose();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(repository.getFileOpen());
        fileMenu.add(repository.getFileClose());
        fileMenu.addSeparator();
        fileMenu.add(repository.getFileExit());
        return fileMenu;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(repository.getFileOpen());
        toolBar.add(repository.getFileClose());
        toolBar.addSeparator();
        toolBar.add(repository.getFileExit());
        return toolBar;
    }

    private JTabbedPane createTabPane() {
        return new JTabbedPane();
    }

    public void addTab(FractalPanel panel) {
        tabPane.add(panel);
    }

    public void removeTab(FractalPanel panel) {
        tabPane.remove(panel);
    }
}
