package net.yaht.rmi.determinant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class LaplaceExpansionRecursiveTask extends RecursiveTask<Double> {
    private final ForkJoinPool pool;
    private final double[][] matrix;
    private final double scalar;
    private final Logger logger;

    public LaplaceExpansionRecursiveTask(ForkJoinPool pool, double[][] matrix,
                                         double scalar, Logger logger) {
        this.pool = pool;
        this.matrix = matrix;
        this.scalar = scalar;
        this.logger = logger;
    }

    @Override
    protected Double compute() {
        logger.LogLine("Starting new task on thread with id " + Thread.currentThread().getId());
        return scalar * calculateDeterminant();
    }

    private double calculateDeterminant() {
        if(matrix.length <= 3) {
            return calculateSmallSizeMatrixDeterminant(matrix);
        }

        double result = 0;
        double coefficient = 1;

        List<LaplaceExpansionRecursiveTask> subTasks = new ArrayList<>();

        for(int i=0; i < matrix.length; i++) {
            double[][] minor = getMinor(matrix, i);
            subTasks.add(new LaplaceExpansionRecursiveTask(
                    pool, minor, coefficient * matrix[0][i], logger));
            coefficient *= -1;
        }

        for (final LaplaceExpansionRecursiveTask task : invokeAll(subTasks)) {
            result += task.getRawResult();
        }

        return result;
    }

    private static double calculateSmallSizeMatrixDeterminant(double[][] matrix) {
        if(matrix.length == 0) {
            return 0;
        }
        if(matrix.length == 1) {
            return calculateSizeOneDeterminant(matrix);
        }

        if(matrix.length == 2) {
            return calculateSizeTwoDeterminant(matrix);
        }

        if(matrix.length == 3) {
            return calculateSizeThreeDeterminant(matrix);
        }

        throw new IllegalArgumentException("Matrix has size more than 3!");
    }

    private static double calculateSizeOneDeterminant(double[][] matrix) {
        return  matrix[0][0];
    }

    private static double calculateSizeTwoDeterminant(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
    }

    private static double calculateSizeThreeDeterminant(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] * matrix[2][2]
                + matrix[0][1] * matrix[1][2] * matrix[2][0]
                + matrix[0][2] * matrix[1][0] * matrix[2][1]
                - matrix[0][2] * matrix[1][1] * matrix[2][0]
                - matrix[0][0] * matrix[1][2] * matrix[2][1]
                - matrix[0][1] * matrix[1][0] * matrix[2][2];

    }

    private double[][] getMinor(double[][] matrix, int column) {
        double[][] minor = new double[matrix.length-1][matrix.length-1];
        for(int i=1; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                if(j == column) {
                    continue;
                }

                int columnIndex = j < column ? j : j-1;
                minor[i-1][columnIndex] = matrix[i][j];
            }
        }

        return minor;
    }
}
