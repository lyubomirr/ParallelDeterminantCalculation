package net.yaht.rmi.determinant;

public interface CalculationStrategy {
    double calculateDeterminant(double[][] matrix, int threadCount, Logger logger) throws InterruptedException;
}
