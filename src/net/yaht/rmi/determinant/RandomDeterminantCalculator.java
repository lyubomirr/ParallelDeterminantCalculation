package net.yaht.rmi.determinant;

import java.util.concurrent.ThreadLocalRandom;

public class RandomDeterminantCalculator extends DeterminantCalculator {
    private static final int MAX_RANDOM_VALUE = 20;
    private static final int MIN_RANDOM_VALUE = -20;

    private final int size;

    public RandomDeterminantCalculator(int threadCount, int size, Logger logger, CalculationStrategy strategy) {
        super(threadCount, logger, strategy);
        this.size = size;
    }

    @Override
    protected double[][] getMatrix() {
        logger.LogLine("Generating random matrix with size " + size + "...");
        double[][] matrix = new double[this.size][this.size];

        for(int i=0; i<this.size; i++) {
            for(int j=0; j<this.size; j++) {
                matrix[i][j] = ThreadLocalRandom.current().nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            }
        }

        logger.LogLine("Random matrix generated: ");
        printMatrix(matrix);

        return matrix;
    }
}
