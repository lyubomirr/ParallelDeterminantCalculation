package net.yaht.rmi.determinant;

public class CLIArguments {
    public static class Short {
        public static final String THREADS = "t";
        public static final String SIZE = "n";
        public static final String INPUT_FILE = "i";
        public static final String OUTPUT_FILE = "o";
        public static final String QUIET_MODE = "q";
        public static final String ALGORITHM = "a";
    }

    public static class Long {
        public static final String THREADS = "tasks";
        public static final String SIZE = "size";
        public static final String INPUT_FILE = "input";
        public static final String OUTPUT_FILE = "output";
        public static final String QUIET_MODE = "quiet";
        public static final String ALGORITHM = "algorithm";
    }
}
