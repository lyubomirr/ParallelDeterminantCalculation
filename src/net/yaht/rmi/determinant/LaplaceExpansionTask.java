package net.yaht.rmi.determinant;


public class LaplaceExpansionTask implements Runnable {
    private final double[][] matrix;
    private final int start;
    private final int end;
    private final Logger logger;
    private final int threadNum;
    private Result result;

    public LaplaceExpansionTask(double[][] matrix, int start, int end,
                                Result result, Logger logger, int threadNum) {
        this.matrix = matrix;
        this.start = start;
        this.end = end;
        this.logger = logger;
        this.result = result;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        logger.LogLine("Thread-" + threadNum + " started.");
        long startTime = System.currentTimeMillis();

        double taskResult = calculateDeterminant(matrix, start, end);
        synchronized (this) {
            this.result.value = taskResult;
        }

        long endTime = System.currentTimeMillis();

        logger.LogLine("Thread-" + threadNum + " finished.");
        logger.LogLine("Thread-" + threadNum + " execution time was (milis): " + (endTime - startTime));
    }


    private double calculateDeterminant(double[][] matrix, int start, int end) {
        if(matrix.length <= 3) {
            return calculateSmallSizeMatrixDeterminant(matrix);
        }

        double result = 0;

        double coefficient = Math.pow(-1, start);

        for(int i=start; i < end; i++) {
            double[][] minor = getMinor(matrix, i);
            result += coefficient * matrix[0][i] * calculateDeterminant(minor, 0, minor.length);
            coefficient *= -1;
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
            return calcuateSizeThreeDeterminant(matrix);
        }

        throw new IllegalArgumentException("Matrix has size more than 3!");
    }

    private static double calculateSizeOneDeterminant(double[][] matrix) {
        return  matrix[0][0];
    }

    private static double calculateSizeTwoDeterminant(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
    }

    private static double calcuateSizeThreeDeterminant(double[][] matrix) {
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
