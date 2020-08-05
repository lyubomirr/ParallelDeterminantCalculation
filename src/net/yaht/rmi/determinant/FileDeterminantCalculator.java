package net.yaht.rmi.determinant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileDeterminantCalculator extends DeterminantCalculator {
    private final String filePath;

    public FileDeterminantCalculator(int threadCount, String filePath, Logger logger, CalculationStrategy strategy) {
        super(threadCount, logger, strategy);
        this.filePath = filePath;
    }

    @Override
    protected double[][] getMatrix() throws IOException {
        logger.LogLine("Reading matrix from file " + filePath + "...");

        try(BufferedReader reader = new BufferedReader(new FileReader(this.filePath))) {
            String line = reader.readLine();
            if(line == null) {
                throw new IllegalArgumentException("Matrix file empty!");
            }

            int size = Integer.parseInt(line);
            double[][] matrix = new double[size][size];
            int currentRow = 0;

            while((line = reader.readLine()) != null) {
                double[] row = Arrays.stream(line.split("\\s+"))
                                .mapToDouble(num -> Double.parseDouble(num))
                                .toArray();

                if(row.length != size) {
                    throw new IllegalArgumentException("File matrix has more columns than the size!");
                }

                matrix[currentRow] = row;
                currentRow++;
            }

            if(currentRow != size) {
                throw new IllegalArgumentException("Rows in the file matrix are different than the size!");
            }

            logger.LogLine("Matrix from file read: ");
            printMatrix(matrix);

            return matrix;
        }
    }
}
