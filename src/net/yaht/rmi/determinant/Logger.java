package net.yaht.rmi.determinant;

public class Logger {
    private final boolean isQuiet;

    public Logger(boolean isQuiet) {
        this.isQuiet = isQuiet;
    }
    public void LogLine(String message) {
        if(isQuiet) {
            return;
        }

        System.out.println(message);
    }

    public void Log(String message) {
        if(isQuiet) {
            return;
        }

        System.out.print(message);
    }

    public void Log(double number) {
        if(isQuiet) {
            return;
        }

        System.out.print(number);
    }


}
