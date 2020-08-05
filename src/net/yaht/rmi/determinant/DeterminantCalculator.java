package net.yaht.rmi.determinant;

import java.io.IOException;

public abstract class DeterminantCalculator {

    protected final Logger logger;
    protected final int threadCount;
    private double[][] matrix;
    private final CalculationStrategy strategy;

    public DeterminantCalculator(int threadCount, Logger logger, CalculationStrategy strategy) {
        if(threadCount < 1) {
            throw new IllegalArgumentException("Thread count cannot be less than 1!");
        }

        this.threadCount = threadCount;
        this.logger = logger;
        this.strategy = strategy;
    }

    public double calculateDeterminant() throws InterruptedException {
        return this.strategy.calculateDeterminant(matrix, threadCount, logger);
    }

    protected void printMatrix(double[][] matrix) {
        for(int i=0; i < matrix.length; i++) {
            for(int j=0; j < matrix.length; j++) {
                logger.Log(matrix[i][j]);
                logger.Log(" ");
            }

            logger.LogLine("");
        }
    }

    public void loadMatrix() throws IOException {
        this.matrix = getMatrix();
    }

    public double[][] getCachedMatrix() {
        return this.matrix;
    }

    protected abstract double[][] getMatrix() throws IOException;

}
