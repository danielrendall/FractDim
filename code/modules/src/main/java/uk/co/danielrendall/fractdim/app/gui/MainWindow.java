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

package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.app.FractDim;
import uk.co.danielrendall.fractdim.app.gui.actions.ActionRepository;
import uk.co.danielrendall.fractdim.logging.Log;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/icons/16/fractdim.png")));
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
                FractDim.instance().notifyPanelChange(panel);
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
        menu.add(repository.getFileCalculate());
        menu.addSeparator();
        menu.add(repository.getFileExport());
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
        toolBar.add(repository.getFileCalculate());
        toolBar.addSeparator();
        toolBar.add(repository.getFileExport());
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
        tabPane.setSelectedComponent(panel);
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
        repository.getFileCalculate().setEnabled(false);
        repository.getFileExport().setEnabled(false);
        repository.getDiagramZoomIn().setEnabled(false);
        repository.getDiagramZoomOut().setEnabled(false);
    }

}
