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
        firstDataRow.createCell(0).setCellValue(ch.createRichTextString("Grid Angle"));
        firstDataRow.createCell(1).setCellValue(ch.createRichTextString("Grid Resolution"));
        firstDataRow.createCell(2).setCellValue(ch.createRichTextString("X displacement"));
        firstDataRow.createCell(3).setCellValue(ch.createRichTextString("Y displacement"));
        firstDataRow.createCell(4).setCellValue(ch.createRichTextString("Square count"));
        rawDataSheet.createFreezePane(0, 1);

        Row firstIntermediateRow = intermediateSheet.createRow(currentIntermediateRow++);
        Row secondIntermediateRow = intermediateSheet.createRow(currentIntermediateRow++);

        firstIntermediateRow.createCell(0).setCellValue(ch.createRichTextString("Angles"));
        secondIntermediateRow.createCell(0).setCellValue(ch.createRichTextString("Resolution"));
        secondIntermediateRow.createCell(1).setCellValue(ch.createRichTextString("Reciprocal Resolution"));
        secondIntermediateRow.createCell(2).setCellValue(ch.createRichTextString("Log Reciprocal Resolution"));
        int currentCell = 3;
        for (Double d : result.getAngleGridCollection().getAvailableAngles()) {
            firstIntermediateRow.createCell(currentCell).setCellValue(d);
            secondIntermediateRow.createCell(currentCell).setCellValue(ch.createRichTextString("Count"));
            secondIntermediateRow.createCell(currentCell + 1).setCellValue(ch.createRichTextString("Log Count"));
            currentCell += 3;
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
        firstResultRow.createCell(0).setCellValue(ch.createRichTextString("Grid Angle"));
        firstResultRow.createCell(1).setCellValue(ch.createRichTextString("Fractal Dimension"));
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

    // the current row on the intermediate sheet as we loop through the resolution
    int intermediateResolutionRow;
    // column representing the angle in the second (intermediate sheet)
    int intermediateAngleColumn;
    // row representing the angle in the first (results) sheet
    int resultsRow;

    public void visit(AngleGridCollection collection) {
        intermediateAngleColumn = 3;
        resultsRow = 1;
        for (Double angle : collection.getAvailableAngles()) {
            currentAngle = angle;

            ResolutionGridCollection rgc = collection.collectionForAngle(angle);
            rgc.accept(this);

            final Row angleRow = resultsSheet.createRow(resultsRow);
            angleRow.createCell(0).setCellValue(angle);

            CellReference topXRange = new CellReference(2, 2, false, false);
            CellReference bottomXRange = new CellReference(intermediateResolutionRow - 1, 2, false, false);

            CellReference topYRange = new CellReference(2, intermediateAngleColumn + 1, false, false);
            CellReference bottomYRange = new CellReference(intermediateResolutionRow - 1, intermediateAngleColumn + 1, false, false);

            angleRow.createCell(1).setCellFormula(String.format("SLOPE('Intermediate'!%s:%s,'Intermediate'!%s:%s)", topYRange.formatAsString(), bottomYRange.formatAsString(), topXRange.formatAsString(), bottomXRange.formatAsString()));
            intermediateAngleColumn += 3;
            resultsRow += 1;
        }

        Row grandAverageRow = resultsSheet.createRow(resultsRow + 1);
        grandAverageRow.createCell(0).setCellValue(ch.createRichTextString("Average:"));
        grandAverageRow.createCell(1).setCellFormula(String.format("AVERAGE(B2:B%d)", resultsRow));

    }

    public void visit(ResolutionGridCollection collection) {
        intermediateResolutionRow = 2;
        for (Double resolution: collection.getAvailableResolutions()) {
            currentResolution = resolution;
            int initialRawDataRow = currentDataRow + 1; // rows are 0 based, but formulas are 1-based
            DisplacementGridCollection dgc = collection.collectionForResolution(resolution);

            dgc.accept(this);

            int finalRawDataRow = currentDataRow; // actually currentdataRow - 1 + 1
            final Row intermediateAverageRow = intermediateSheet.getRow(intermediateResolutionRow);
            Cell displacementSum = intermediateAverageRow.createCell(intermediateAngleColumn);
            Cell reciprocal = intermediateAverageRow.createCell(intermediateAngleColumn + 1);

            displacementSum.setCellFormula(String.format("AVERAGE('Data'!E%d:E%d)", initialRawDataRow, finalRawDataRow));
            reciprocal.setCellFormula(String.format("LOG(%s)", new CellReference(intermediateResolutionRow, intermediateAngleColumn, false, false).formatAsString()));

            intermediateResolutionRow++;
        }
    }

    public void visit(DisplacementGridCollection collection) {
        for (Vec displacement : collection.getAvailableDisplacements()) {

            Grid g = collection.gridForDisplacement(displacement);
            Row row = rawDataSheet.createRow(currentDataRow++);
            row.createCell(0).setCellValue(currentAngle);
            row.createCell(1).setCellValue(currentResolution);
            row.createCell(2).setCellValue(displacement.x());
            row.createCell(3).setCellValue(displacement.y());
            row.createCell(4).setCellValue(g.getSquareCount());

            visitedGrids++;
            publish((int)(100.0d * (double) visitedGrids / (double) gridCount));
        }

    }
}
