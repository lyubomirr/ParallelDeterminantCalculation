package net.yaht.rmi.determinant;

import org.apache.commons.cli.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Main {
    private static String outputFilePath;

    public static void main(String[] args) {
        run(args);
    }

    private static void run(String args[]) {
        try {
            CommandLine commandLine = getCommandLineArguments(args);
            DeterminantCalculator calculator = createCalculator(commandLine);

            calculator.loadMatrix();

            long startTime = System.currentTimeMillis();
            double determinant = calculator.calculateDeterminant();
            long stopTime = System.currentTimeMillis();

            System.out.println("Result: " + determinant);
            System.out.println("Total execution time for current run (millis): " + (stopTime - startTime));

            if(outputFilePath != null && !outputFilePath.isEmpty()) {
                writeToFile(calculator.getCachedMatrix(), determinant);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CommandLine getCommandLineArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption(CLIArguments.Short.THREADS, CLIArguments.Long.THREADS,
                true,"Thread count");
        options.addOption(CLIArguments.Short.SIZE, CLIArguments.Long.SIZE,
                true,"Random matrix size");
        options.addOption(CLIArguments.Short.INPUT_FILE, CLIArguments.Long.INPUT_FILE,
                true,"Input matrix file");
        options.addOption(CLIArguments.Short.OUTPUT_FILE, CLIArguments.Long.OUTPUT_FILE,
                true,"Output file with determinant");
        options.addOption(CLIArguments.Short.QUIET_MODE, CLIArguments.Long.QUIET_MODE,
                false,"Quiet mode");
        options.addOption(CLIArguments.Short.ALGORITHM, CLIArguments.Long.ALGORITHM, true,
                "Algorithm to be used - 'g' for Gaussian elimination, " +
                        "'l' for Laplace expansion and 'lt' for Laplace expansion using fork join pool.");


        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    private static DeterminantCalculator createCalculator(CommandLine commandLine) {
        int threads = 1;

        if(commandLine.hasOption(CLIArguments.Short.THREADS)) {
            threads = Integer.parseInt(commandLine.getOptionValue(CLIArguments.Short.THREADS));
        } else {
            System.out.println("No threads parameter provided...running on one thread.");
        }

        if(threads < 1) {
            throw new IllegalArgumentException("Threads must be at least 1!");
        }

        if(commandLine.hasOption(CLIArguments.Short.OUTPUT_FILE)) {
            outputFilePath = commandLine.getOptionValue(CLIArguments.Short.OUTPUT_FILE);
        }

        boolean isQuietMode = commandLine.hasOption(CLIArguments.Short.QUIET_MODE);

        if(commandLine.hasOption(CLIArguments.Short.INPUT_FILE)
                && commandLine.hasOption(CLIArguments.Short.SIZE)) {
            System.out.println("You have provided input file and matrix size. Matrix size will be ignored"
            + " and the matrix will be loaded from the file");
        }

        if(commandLine.hasOption(CLIArguments.Short.INPUT_FILE)) {
            String filePath = commandLine.getOptionValue(CLIArguments.Short.INPUT_FILE);
            return new FileDeterminantCalculator(threads, filePath, new Logger(isQuietMode),
                    getAlgorithm(commandLine));
        }

        if(commandLine.hasOption(CLIArguments.Short.SIZE)) {
            int size = Integer.parseInt(commandLine.getOptionValue(CLIArguments.Short.SIZE));
            return new RandomDeterminantCalculator(threads, size, new Logger(isQuietMode),
                    getAlgorithm(commandLine));
        }

        System.out.println("Please provide random matrix size or input file path!");
        System.exit(0);
        return null;
    }

    private static CalculationStrategy getAlgorithm(CommandLine commandLine) {
        if(!commandLine.hasOption(CLIArguments.Short.ALGORITHM)
                || commandLine.getOptionValue(CLIArguments.Short.ALGORITHM).equals("l")) {
            return new LaplaceExpansionCalculationStrategy();
        }

        if(commandLine.getOptionValue(CLIArguments.Short.ALGORITHM).equals("g")) {
            return new GaussianEliminationCalculationStrategy();
        }

        if(commandLine.getOptionValue(CLIArguments.Short.ALGORITHM).equals("lt")) {
            return new ForkJoinLaplaceExpansionCalculationStrategy();
        }

        return new LaplaceExpansionCalculationStrategy();
    }

    private static void writeToFile(double[][] matrix, double determinant) throws IOException {
        try(Writer writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Matrix:");
            writer.write(System.lineSeparator());

            for(int i=0; i < matrix.length; i++) {
                for(int j=0; j < matrix.length; j++) {
                    writer.write(String.valueOf(matrix[i][j]));
                    writer.write(" ");
                }

                writer.write(System.lineSeparator());
            }

            writer.write(System.lineSeparator());
            writer.write("Determinant:");
            writer.write(System.lineSeparator());
            writer.write(String.valueOf(determinant));
        }
    }
}
