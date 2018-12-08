package ru.sy.encoding.logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger implements Logger {
    PrintWriter printWriter;

    public FileLogger(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        printWriter = new PrintWriter(file);
    }

    @Override
    public void log(String info) {
        printWriter.println(info);
    }

    @Override
    public void logf(String info, Object... objects) {
        printWriter.printf(info + '\n', objects);
    }

    @Override
    public void close() {
        printWriter.close();
    }
}
