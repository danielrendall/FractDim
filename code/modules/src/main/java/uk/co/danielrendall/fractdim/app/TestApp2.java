package uk.co.danielrendall.fractdim.app;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import uk.co.danielrendall.fractdim.app.gui.CanvasStack;
import uk.co.danielrendall.fractdim.app.gui.StackLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Apr-2010
 * Time: 12:11:10
 * To change this template use File | Settings | File Templates.
 */
public class TestApp2 {

    public static void main(String[] args) {
        JFrame f = new JFrame("Batik");
        TestApp2 app = new TestApp2(f);
        f.getContentPane().add(app.createComponents());

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(400, 400);
        f.setVisible(true);
    }

    JFrame frame;
    JButton button = new JButton("Load...");
    JLabel label = new JLabel();

    public TestApp2(JFrame f) {
        frame = f;
    }

    public JComponent createComponents() {
        final JPanel panel = new JPanel(new BorderLayout());

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(button);
        p.add(label);

        panel.add("North", p);

        final CanvasStack stack = new CanvasStack();

        panel.add("Center", stack);

        final int[] fileCount = new int[1];
        fileCount[0] = 0;

        // Set the button action.
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser fc = new JFileChooser("/home/daniel/Development/FractDim/resources/samples");
                int choice = fc.showOpenDialog(panel);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    try {
                        // Set the JSVGCanvas listeners.
                        stack.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
                            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                                label.setText("Document Loading...");
                            }
                            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                                label.setText("Document Loaded.");
                            }
                        });

                        stack.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
                            public void gvtBuildStarted(GVTTreeBuilderEvent e) {
                                label.setText("Build Started...");
                            }
                            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                                label.setText("Build Done.");
                                //frame.pack();
                            }
                        });

                        stack.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
                            public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
                                label.setText("Rendering Started...");
                            }
                            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                                label.setText("");
                            }
                        });


                        if (fileCount[0] > 0) {
                            stack.addFromURI(f.toURL().toString());
                        } else {
                            stack.setRootURI(f.toURL().toString());
                        }
                        fileCount[0]++;

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        return panel;
    }

}
