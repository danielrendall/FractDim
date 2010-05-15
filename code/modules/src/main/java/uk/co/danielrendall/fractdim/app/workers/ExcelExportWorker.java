package uk.co.danielrendall.fractdim.app.workers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import uk.co.danielrendall.fractdim.calculation.SquareCountingResult;
import uk.co.danielrendall.fractdim.calculation.grids.*;
import uk.co.danielrendall.fractdim.logging.Log;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 15-May-2010
 * Time: 17:33:58
 * To change this template use File | Settings | File Templates.
 */
public class ExcelExportWorker extends SwingWorker<String, Integer> implements CollectionVisitor {
    
    private boolean useful = true;
    private final String name;
    private final SquareCountingResult result;
    private final File xlsFile;
    private final Notifiable<ExcelExportWorker> notifiable;
    private final Workbook wb;
    private final Sheet sheet;
    private final CreationHelper ch;
    private final int gridCount;
    private int visitedGrids = 0;

    private double currentAngle = 0;
    private double currentResolution = 0;
    private Vec currentDisplacement = Vec.ZERO;
    private int currentRow = 0;

    public ExcelExportWorker(String name, SquareCountingResult result, File xlsFile, Notifiable<ExcelExportWorker> notifiable) {
        this.name = name;
        this.result = result;
        this.xlsFile = xlsFile;
        this.notifiable = notifiable;

        wb = new HSSFWorkbook();
        sheet = wb.createSheet();
        ch = wb.getCreationHelper();
        final int[] g = {0};
        result.getAngleGridCollection().accept(new CollectionVisitor() {
            public void visit(AngleGridCollection collection) {
                for (Double angle: collection.getAvailableAngles()) {
                    collection.collectionForAngle(angle).accept(this);
                }
            }

            public void visit(ResolutionGridCollection collection) {
                for (Double resolution: collection.getAvailableResolutions()) {
                    collection.collectionForResolution(resolution).accept(this);
                }
            }

            public void visit(DisplacementGridCollection collection) {
                for (Vec vec : collection.getAvailableDisplacements()) {
                    g[0]++;
                }
            }
        });
        this.gridCount = g[0];
        Log.gui.debug("There are " + gridCount + " to export");
    }

    @Override
    protected String doInBackground() throws Exception {
        Row firstRow = sheet.createRow(currentRow++);
        int currentCell = 0;
        firstRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Grid Angle"));
        firstRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Grid Resolution"));
        firstRow.createCell(currentCell++).setCellValue(ch.createRichTextString("X displacement"));
        firstRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Y displacement"));
        firstRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Square count"));
        result.getAngleGridCollection().accept(this);

        FileOutputStream out = new FileOutputStream(xlsFile);
        wb.write(out);
        out.close();
        return "Finished";
    }

    @Override
    protected void process(List<Integer> chunks) {
        if (useful && !Thread.currentThread().isInterrupted()) {
            try {
                int last = chunks.get(chunks.size() - 1);
                notifiable.updateProgress(last);
            } catch (Exception e) {
                Log.thread.warn("Problem getting hold of view - " + e.getMessage());
            }
        } else {
            useful = false;
        }

    }

    @Override
    protected void done() {
        try {
            if (useful && !Thread.currentThread().isInterrupted()) {
                notifiable.notifyComplete(this);
            } else {
                useful = false;
            }
        } catch (Exception e) {
            Log.thread.warn("Problem getting hold of view - " + e.getMessage());
        }
    }

    public void visit(AngleGridCollection collection) {
        for (Double angle : collection.getAvailableAngles()) {
            currentAngle = angle;
            ResolutionGridCollection rgc = collection.collectionForAngle(angle);
            rgc.accept(this);
        }
    }

    public void visit(ResolutionGridCollection collection) {
        for (Double resolution: collection.getAvailableResolutions()) {
            currentResolution = resolution;
            DisplacementGridCollection dgc = collection.collectionForResolution(resolution);
            dgc.accept(this);
        }
    }

    public void visit(DisplacementGridCollection collection) {
        for (Vec displacement : collection.getAvailableDisplacements()) {
            currentDisplacement = displacement;
            Grid g = collection.gridForDisplacement(displacement);
            Row row = sheet.createRow(currentRow++);
            int currentCell = 0;
            row.createCell(currentCell++).setCellValue(currentAngle);
            row.createCell(currentCell++).setCellValue(currentResolution);
            row.createCell(currentCell++).setCellValue(currentDisplacement.x());
            row.createCell(currentCell++).setCellValue(currentDisplacement.y());
            row.createCell(currentCell++).setCellValue(g.getSquareCount());

            visitedGrids++;
            publish((int)(100.0d * (double) visitedGrids / (double) gridCount));
        }
    }
}
