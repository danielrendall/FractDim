package uk.co.danielrendall.fractdim.app.workers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
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
    private final Sheet resultsSheet;
    private final Sheet intermediateSheet;
    private final Sheet rawDataSheet;
    private final CreationHelper ch;
    private final int gridCount;
    private int visitedGrids = 0;

    private double currentAngle = 0;
    private double currentResolution = 0;
    private Vec currentDisplacement = Vec.ZERO;
    private int currentDataRow = 0;
    private int currentIntermediateRow = 0;
    private int currentResultsRow = 0;

    public ExcelExportWorker(String name, SquareCountingResult result, File xlsFile, Notifiable<ExcelExportWorker> notifiable) {
        this.name = name;
        this.result = result;
        this.xlsFile = xlsFile;
        this.notifiable = notifiable;

        wb = new HSSFWorkbook();
        resultsSheet = wb.createSheet("Results");
        intermediateSheet = wb.createSheet("Intermediate");
        rawDataSheet = wb.createSheet("Data");
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
        Row firstDataRow = rawDataSheet.createRow(currentDataRow++);
        int currentCell = 0;
        firstDataRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Grid Angle"));
        firstDataRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Grid Resolution"));
        firstDataRow.createCell(currentCell++).setCellValue(ch.createRichTextString("X displacement"));
        firstDataRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Y displacement"));
        firstDataRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Square count"));
        rawDataSheet.createFreezePane(0, 1);

        currentCell = 0;
        Row firstIntermediateRow = intermediateSheet.createRow(currentIntermediateRow++);
        Row secondIntermediateRow = intermediateSheet.createRow(currentIntermediateRow++);

        firstIntermediateRow.createCell(currentCell).setCellValue(ch.createRichTextString("Angles"));
        secondIntermediateRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Resolution"));
        secondIntermediateRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Reciprocal Resolution"));
        secondIntermediateRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Log Reciprocal Resolution"));
        for (Double d : result.getAngleGridCollection().getAvailableAngles()) {
            firstIntermediateRow.createCell(currentCell).setCellValue(d);
            secondIntermediateRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Count"));
            secondIntermediateRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Log Count"));
            currentCell++;
        }
        double d = result.getAngleGridCollection().getAvailableAngles().iterator().next();
        for (double resolution: result.getAngleGridCollection().collectionForAngle(d).getAvailableResolutions()) {
            Row intermediateRow = intermediateSheet.createRow(currentIntermediateRow++);
            intermediateRow.createCell(0).setCellValue(resolution);
            intermediateRow.createCell(1).setCellFormula("1 / A" + currentIntermediateRow);
            intermediateRow.createCell(2).setCellFormula("LOG(B" + currentIntermediateRow + ")");
        }

        intermediateSheet.createFreezePane(3, 2);


        Row firstResultRow = resultsSheet.createRow(currentResultsRow++);
        currentCell = 0;
        firstResultRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Grid Angle"));
        firstResultRow.createCell(currentCell++).setCellValue(ch.createRichTextString("Fractal Dimension"));
        resultsSheet.createFreezePane(0, 1);

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

    // column representing the angle in the second (intermediate sheet)
    int intermediateAngleColumn = 0;

    public void visit(AngleGridCollection collection) {
        intermediateAngleColumn = 3;
        for (Double angle : collection.getAvailableAngles()) {
            currentAngle = angle;
            ResolutionGridCollection rgc = collection.collectionForAngle(angle);
            rgc.accept(this);
            intermediateAngleColumn += 3;
        }
    }

    int intermediateResolutionRow = 0;
    public void visit(ResolutionGridCollection collection) {
        intermediateResolutionRow = 2;
        for (Double resolution: collection.getAvailableResolutions()) {
            currentResolution = resolution;
            DisplacementGridCollection dgc = collection.collectionForResolution(resolution);
            dgc.accept(this);
            intermediateResolutionRow++;
        }
    }

    public void visit(DisplacementGridCollection collection) {
        int initialRawDataRow = currentDataRow + 1; // rows are 0 based, but formulas are 1-based
        for (Vec displacement : collection.getAvailableDisplacements()) {
            currentDisplacement = displacement;
            Grid g = collection.gridForDisplacement(displacement);
            Row row = rawDataSheet.createRow(currentDataRow++);
            int currentCell = 0;
            row.createCell(currentCell++).setCellValue(currentAngle);
            row.createCell(currentCell++).setCellValue(currentResolution);
            row.createCell(currentCell++).setCellValue(currentDisplacement.x());
            row.createCell(currentCell++).setCellValue(currentDisplacement.y());
            row.createCell(currentCell++).setCellValue(g.getSquareCount());

            visitedGrids++;
            publish((int)(100.0d * (double) visitedGrids / (double) gridCount));
        }
        int finalRawDataRow = currentDataRow; // actually currentdataRow - 1 + 1
        final Row intermediateAverageRow = intermediateSheet.getRow(intermediateResolutionRow);
        Cell displacementSum = intermediateAverageRow.createCell(intermediateAngleColumn);
        Cell reciprocal = intermediateAverageRow.createCell(intermediateAngleColumn + 1);

        displacementSum.setCellFormula("AVERAGE('Data'!E" + initialRawDataRow + ":E" + finalRawDataRow + ")");
        reciprocal.setCellFormula("LOG(" + new CellReference(intermediateResolutionRow, intermediateAngleColumn, false, false).formatAsString() + ")");

    }
}
