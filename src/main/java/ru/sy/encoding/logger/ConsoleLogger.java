package ru.sy.encoding.logger;

public class ConsoleLogger implements Logger {
    @Override
    public void log(String info) {
        System.out.println(info + '\n');
    }

    @Override
    public void logf(String info, Object... objects) {
        System.out.printf(info + '\n', objects);
    }
}
