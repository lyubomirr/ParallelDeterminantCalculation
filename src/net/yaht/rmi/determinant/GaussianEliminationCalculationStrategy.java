package net.yaht.rmi.determinant;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GaussianEliminationCalculationStrategy implements CalculationStrategy {
    @Override
    public double calculateDeterminant(double[][] matrix, int threadCount, Logger logger)
            throws InterruptedException {
        System.out.println("Using Gaussian elimination...");
        double det = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for(int i=0; i < matrix.length; i++) {
            CountDownLatch latch = new CountDownLatch(matrix.length - i - 1);

            for(int j = i + 1; j < matrix.length; j++) {
                int finalI = i;
                int finalJ = j;

                executorService.execute(() -> {
                    double z = matrix[finalJ][finalI] / matrix[finalI][finalI];
                    for(int k = finalI + 1; k < matrix.length; k++) {
                        matrix[finalJ][k] = matrix[finalJ][k] - z * matrix[finalI][k];
                    }

                    latch.countDown();
                });
            }

            latch.await();
        }

        for(int i=0; i< matrix.length; i++) det = det * matrix[i][i];
        return det;
    }
}
