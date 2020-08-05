package net.yaht.rmi.determinant;

import java.util.Arrays;

public class LaplaceExpansionCalculationStrategy implements CalculationStrategy {
    @Override
    public double calculateDeterminant(double[][] matrix, int threadCount, Logger logger)
            throws InterruptedException {
        System.out.println("Using Laplace expansion...");

        if(matrix == null || matrix.length == 0) {
            return 0;
        }

        Thread[] threads = new Thread[Math.min(threadCount, matrix.length)];
        Result result = new Result();

        int chunkSize = matrix.length / threadCount;
        int remainder = matrix.length % threadCount;

        int currentThread = 0;
        int startIndex = 0;

        while(startIndex < matrix.length) {
            int endIndex = startIndex + chunkSize;
            if(remainder > 0) {
                endIndex++;
                remainder--;
            }

            endIndex = Math.min(endIndex, matrix.length);

            threads[currentThread] = new Thread(
                    new LaplaceExpansionTask(
                            matrix, startIndex, endIndex, result, logger, currentThread));

            threads[currentThread].start();
            startIndex = endIndex;
            currentThread++;
        }

        for(Thread thread : threads) {
            thread.join();
        }

        return result.value;
    }
}
