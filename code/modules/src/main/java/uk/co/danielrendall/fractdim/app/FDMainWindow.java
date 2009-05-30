package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.swing.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * @author Daniel Rendall
 * @created 13-May-2009 23:39:39
 */
public class FDMainWindow extends SwingMainWindow {

    private static final Logger log = Logger.getLogger(FDMainWindow.class);


    private final static int DEFAULT_WIDTH = 640;
    private final static int DEFAULT_HEIGHT = 480;


    @Override
    protected SwingDefaultCommands createCommands() {
        return new FDCommands();
    }

    @Override
    protected Dimension getDefaultSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    protected void addDefaultMenus(JMenuBar menuBar) {
        addFileMenu(menuBar);
    }

    @Override
    protected void addFileMenuItems(JMenu fileMenu) {
        fileMenu.add(SwingCommandButton.createMenuItem(((FDCommands)commands).getFileGenerateCommand()));
        fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileOpenCommand()));

        addRecentFilesMenu(fileMenu);

        fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileSaveCommand()));
        fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileSaveAsCommand()));

        addFileExportMenuIfRequired(fileMenu);
        fileMenu.addSeparator();

        fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileCloseCommand()));
        fileMenu.add(SwingCommandButton.createMenuItem(commands.getFileQuitCommand()));
    }

    @Override
    protected void addToolBarItems(JToolBar toolBar) {
        toolBar.add(SwingCommandButton.createToolButton(((FDCommands)commands).getFileGenerateCommand()));
        toolBar.add(SwingCommandButton.createToolButton(commands.getFileOpenCommand()));
        toolBar.add(SwingCommandButton.createToolButton(commands.getFileSaveCommand()));
        toolBar.add(SwingCommandButton.createToolButton(commands.getFileSaveAsCommand()));
        toolBar.add(SwingCommandButton.createToolButton(commands.getFileCloseCommand()));
        toolBar.add(SwingCommandButton.createToolButton(commands.getFileQuitCommand()));
    }

}
