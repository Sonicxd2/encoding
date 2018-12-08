package ru.sy.encoding.logger;

public interface Logger {
    void log(String info);

    void logf(String info, Object... objects);

    default void close() {

    }
}
