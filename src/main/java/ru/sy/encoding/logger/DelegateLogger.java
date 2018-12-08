package ru.sy.encoding.logger;

public class DelegateLogger implements Logger{
    Logger[] loggers;
    public DelegateLogger(Logger... loggers) {
        this.loggers = loggers;
    }

    @Override
    public void log(String info) {
        for(Logger logger : loggers) {
            logger.log(info);
        }
    }

    @Override
    public void logf(String info, Object... objects) {
        for(Logger logger : loggers) {
            logger.logf(info, objects);
        }
    }

    @Override
    public void close() {
        for(Logger logger : loggers) {
            logger.close();
        }
    }
}
