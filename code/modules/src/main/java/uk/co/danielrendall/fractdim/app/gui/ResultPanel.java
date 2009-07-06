package uk.co.danielrendall.fractdim.app.gui;

import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.app.datamodel.CalculationResultTableModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 05-Jul-2009
 * Time: 11:39:41
 * To change this template use File | Settings | File Templates.
 */
public class ResultPanel extends JPanel {
    final JTable resultTable = new JTable();

    private SquareCountingResult result;

    public ResultPanel() {
        super();
        Box vertBox = Box.createVerticalBox();
        Box horizBox = Box.createHorizontalBox();

        horizBox.add(new JLabel("Angle selector goes here"));

        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data"));
        tablePanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        horizBox.add(tablePanel);

        vertBox.add(horizBox);
        vertBox.add(new JLabel("Final result goes here"));

        add(vertBox, BorderLayout.CENTER);

    }

    public void update(SquareCountingResult result) {
        this.result = result;
        resultTable.setModel(new CalculationResultTableModel(result));
    }

}
