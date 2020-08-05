package net.yaht.rmi.determinant;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinLaplaceExpansionCalculationStrategy implements CalculationStrategy {
    private ForkJoinPool pool;

    @Override
    public double calculateDeterminant(double[][] matrix, int threadCount, Logger logger) {
        System.out.println("Using Laplace expansion with fork join pool...");

        pool = new ForkJoinPool(threadCount);
        return pool.invoke(new LaplaceExpansionRecursiveTask(pool, matrix, 1, logger));
    }
}
