package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
        tabPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                FractalPanel panel = (FractalPanel) tabPane.getSelectedComponent();
                FractDim.instance().notifyCurrentPanel(panel);
            }
        });
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(tabPane, BorderLayout.CENTER);
    }

    public void close() {
        dispose();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createDiagramMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(repository.getFileOpen());
        menu.add(repository.getFileClose());
        menu.addSeparator();
        menu.add(repository.getFileExit());
        return menu;
    }

    private JMenu createDiagramMenu() {
        JMenu menu = new JMenu("Diagram");
        menu.add(repository.getDiagramZoomIn());
        menu.add(repository.getDiagramZoomOut());
        return menu;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(repository.getFileOpen());
        toolBar.add(repository.getFileClose());
        toolBar.addSeparator();
        toolBar.add(repository.getFileExit());
        toolBar.addSeparator();
        toolBar.add(repository.getDiagramZoomIn());
        toolBar.add(repository.getDiagramZoomOut());
        return toolBar;
    }

    private JTabbedPane createTabPane() {
        return new JTabbedPane();
    }

    public void addTab(String title, FractalPanel panel) {
        tabPane.add(title, panel);
    }

    public void updateTabTitle(String title, FractalPanel panel) {
        int index = tabPane.indexOfComponent(panel);
        if (index > -1) {
            tabPane.setTitleAt(index, title);
        } else {
            Log.gui.warn("Asked to set title for non-existent panel");
        }
    }

    public void removeTab(FractalPanel panel) {
        tabPane.remove(panel);
    }

    public void disableMenuItems() {
        ActionRepository repository = ActionRepository.instance();
        repository.getFileClose().setEnabled(false);
        repository.getDiagramZoomIn().setEnabled(false);
        repository.getDiagramZoomOut().setEnabled(false);
    }

    public void enableMenuItems() {
        ActionRepository repository = ActionRepository.instance();
        repository.getFileClose().setEnabled(true);
        repository.getDiagramZoomIn().setEnabled(true);
        repository.getDiagramZoomOut().setEnabled(true);
    }

}
