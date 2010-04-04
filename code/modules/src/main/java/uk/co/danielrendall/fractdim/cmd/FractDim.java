package uk.co.danielrendall.fractdim.cmd;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.apache.batik.util.XMLResourceDescriptor;

import java.io.File;
import java.io.IOException;

import uk.co.danielrendall.fractdim.app.controller.FractalController;
import uk.co.danielrendall.fractdim.app.model.FractalDocument;
import uk.co.danielrendall.fractdim.calculation.*;
import uk.co.danielrendall.fractdim.calculation.iterators.*;

/**
 * Run app from the command line
 */
public class FractDim {

    @Option(name = "-f", usage = "the svg file to process", required = true)
    private File svgFile;

    @Option(name = "-d", usage = "the maximum recursion depth when evaluating curves")
    private int maxDepth = 1;

    @Option(name = "-max", usage = "maximum resolution")
    private double maxResolution;

    @Option(name = "-min", usage = "minimum resolution")
    private double minResolution;

    @Option(name = "-type", usage = "resolution step type")
    private StepType stepType = StepType.Uniform;

    @Option(name = "-s", usage = "number of resolution steps")
    private int numberOfResolutionSteps;

    @Option(name = "-a", usage = "number of angles")
    private int numberOfAngles = 1;

    @Option(name = "-p", usage = "number of displacement points")
    private int numberOfDisplacementPoints = 1;

    @Option(name = "-do", usage = "action")
    private Action action = Action.Count;

    public static void main(String[] args) {
        new FractDim().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        parser.setUsageWidth(80);

        try {
            // parse the arguments.
            parser.parseArgument(args);
            process();

        } catch (CmdLineException e) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java FractDim [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
        }
    }

    private void process() {
        try {
            FractalController controller = FractalController.fromFile(svgFile);
            FractalDocument document = controller.getDocument();

            System.out.println("File: " + svgFile.getName());
            System.out.println("Approximate bounding box: " + document.getMetadata().getBoundingBox());
            System.out.println("Number of curves: " + document.getMetadata().getCurveCount());

            switch (action) {

                case Count:
                    SquareCounterBuilder squareCounterBuilder = new SquareCounterBuilder();
                    squareCounterBuilder.maxDepth(maxDepth).
                            angleIterator(getAngleIterator()).
                            resolutionIterator(getResolutionIterator()).
                            displacementIterator(getDisplacementIterator()).
                            fractalDocument(document);
                    SquareCounter calc = squareCounterBuilder.build();
                    SquareCountingResult result = calc.process();
                    System.out.println(result.getAvailableAngles());
                    break;
                case Stats:
                    StatisticsCalculatorBuilder statsCalculatorBuilder = new StatisticsCalculatorBuilder();
                    statsCalculatorBuilder.minAngle(StatisticsCalculator.TWO_DEGREES).
                            fractalDocument(document);
                    StatisticsCalculator sc = statsCalculatorBuilder.build();
                    Statistics stats = sc.process();
                    System.out.println("Accurate bounding box: " + stats.getBoundingBox());
                    System.out.println("Shortest line: " + stats.getShortestLine());
                    System.out.println("Longest line: " + stats.getLongestLine());
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private AngleIterator getAngleIterator() {
        return new UniformAngleIterator(numberOfAngles);
    }

    private ResolutionIterator getResolutionIterator() {
        if (stepType == StepType.Log) {
            return new LogarithmicResolutionIterator(minResolution, maxResolution, numberOfResolutionSteps);
        } else {
            return new UniformResolutionIterator(minResolution, maxResolution, numberOfResolutionSteps);
        }
    }

    private DisplacementIterator getDisplacementIterator() {
        return new UniformDisplacementIterator(numberOfDisplacementPoints);
    }
}
 enum Action {
     Count,
     Stats
 }

enum StepType {
    Uniform,
    Log
}