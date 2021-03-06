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

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Apr-2010
 * Time: 08:13:52
 * To change this template use File | Settings | File Templates.
 */
public class SettingsPanel extends JPanel {

    private final JSlider minimumSquareSizeSlider;
    private final JSlider maximumSquareSizeSlider;
    private final JSlider angleSlider;
    private final JSlider resolutionSlider;
    private final JSlider displacementSlider;

    private final JComboBox resolutionIteratorList;
    private final JList resolutionList;

    SettingsPanel() {

        this.setLayout(new MigLayout("fill", "[grow, fill][grow, fill][grow, fill]", "[min][min][min][grow, fill]"));

        angleSlider = new JSlider();

        JPanel anglePanel = new JPanel(new BorderLayout());
        anglePanel.setBorder(BorderFactory.createTitledBorder("Angles"));
        angleSlider.setLabelTable(angleSlider.createStandardLabels(5));
        angleSlider.setPaintLabels(true);
        angleSlider.setMinorTickSpacing(1);
        angleSlider.setPaintTicks(true);
        angleSlider.setSnapToTicks(true);
        anglePanel.add(angleSlider, BorderLayout.CENTER);
        add(anglePanel, "cell 0 0 3 1, growx");

        resolutionSlider = new JSlider();
        resolutionIteratorList = new JComboBox();

        JPanel resolutionPanel = new JPanel(new BorderLayout());
        resolutionPanel.setBorder(BorderFactory.createTitledBorder("Resolutions"));
        resolutionSlider.setLabelTable(resolutionSlider.createStandardLabels(5));
        resolutionSlider.setPaintLabels(true);
        resolutionSlider.setMinorTickSpacing(1);
        resolutionSlider.setPaintTicks(true);
        resolutionSlider.setSnapToTicks(true);
        resolutionPanel.add(resolutionSlider, BorderLayout.CENTER);
        resolutionPanel.add(resolutionIteratorList, BorderLayout.SOUTH);
        add(resolutionPanel, "cell 0 1 3 1, growx");

        displacementSlider = new JSlider();

        JPanel displacementPanel = new JPanel(new BorderLayout());
        displacementPanel.setBorder(BorderFactory.createTitledBorder("Displacements"));
        displacementSlider.setLabelTable(angleSlider.createStandardLabels(1));
        displacementSlider.setPaintLabels(true);
        displacementSlider.setMinorTickSpacing(1);
        displacementSlider.setPaintTicks(true);
        displacementSlider.setSnapToTicks(true);
        displacementPanel.add(displacementSlider, BorderLayout.CENTER);

        add(displacementPanel, "cell 0 2 3 1, growx");

        minimumSquareSizeSlider = new JSlider();

        minimumSquareSizeSlider.setOrientation(JSlider.VERTICAL);
        minimumSquareSizeSlider.setBorder(BorderFactory.createTitledBorder("Min"));

        add(minimumSquareSizeSlider, "cell 0 3 1 1");

        maximumSquareSizeSlider = new JSlider();

        maximumSquareSizeSlider.setOrientation(JSlider.VERTICAL);
        maximumSquareSizeSlider.setBorder(BorderFactory.createTitledBorder("Max"));

        add(maximumSquareSizeSlider, "cell 1 3 1 1");

        resolutionList = new JList();
        resolutionList.setBorder(BorderFactory.createTitledBorder("Grid sizes"));
        final ListCellRenderer existingRenderer = resolutionList.getCellRenderer();
        resolutionList.setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String toDisplay = String.format("%5.1f", (Double) value);
                return existingRenderer.getListCellRendererComponent(list, toDisplay, index, isSelected,cellHasFocus);

            }
        });
        add(resolutionList, "cell 2 3 1 1");

    }

    void disableAllControls() {
        setAllEnabled(false);
    }

    void enableAllControls() {
        setAllEnabled(true);
    }

    public void setAllEnabled(boolean state) {
        minimumSquareSizeSlider.setEnabled(state);
        maximumSquareSizeSlider.setEnabled(state);
        angleSlider.setEnabled(state);
        resolutionSlider.setEnabled(state);
        resolutionIteratorList.setEnabled(state);
        displacementSlider.setEnabled(state);
        resolutionList.setEnabled(state);
    }

    JSlider getMinimumSquareSizeSlider() {
        return minimumSquareSizeSlider;
    }

    JSlider getMaximumSquareSizeSlider() {
        return maximumSquareSizeSlider;
    }

    JSlider getAngleSlider() {
        return angleSlider;
    }

    JSlider getResolutionSlider() {
        return resolutionSlider;
    }

    JSlider getDisplacementSlider() {
        return displacementSlider;
    }

    JComboBox getResolutionIteratorList() {
        return resolutionIteratorList;
    }

    JList getResolutionList() {
        return resolutionList;
    }
}
