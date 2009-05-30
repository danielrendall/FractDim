package uk.co.danielrendall.fractdim.app;

import org.bs.mdi.*;

import javax.swing.*;

/**
 * Hello world!
 */
public class FractDim extends Application {

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
}
