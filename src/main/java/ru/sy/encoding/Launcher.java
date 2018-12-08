package ru.sy.encoding;

import ru.sy.encoding.encoder.*;
import ru.sy.encoding.frequency.FrequencyCalculator;
import ru.sy.encoding.frequency.HashMapFrequencyCalculator;
import ru.sy.encoding.logger.ConsoleLogger;
import ru.sy.encoding.logger.DelegateLogger;
import ru.sy.encoding.logger.FileLogger;
import ru.sy.encoding.logger.Logger;
import ru.sy.encoding.utils.SupplierWithException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Launcher {
    public static HashMap<LoggerType, Logger> loggers = new HashMap<>();

    public static void initLoggers() throws Exception {
        for (LoggerType loggerType : LoggerType.values()) {
            loggers.put(loggerType, loggerType.getCreateSupplier().get());
        }
    }

    public static void closeLoggers() {
        loggers.forEach((loggerType, logger) -> logger.close());
    }

    public static void encodeByAll(int[] message) {
        for (EncoderType encoderType : EncoderType.values()) {
            EncodingResult encodingResult = encoderType.encoder.encode(message);
            Logger logger = loggers.get(encoderType.getLoggerType());
            logger.log("Dictionary:");
            logger.log("┌───────────┐");
            for (Map.Entry entry : encodingResult.getDictionary().entrySet()) {
                logger.logf("|%-5s|%-5s|", entry.getKey(), entry.getValue());
            }
            logger.log("└───────────┘");
            logger.log("Message: " + Arrays.toString(encodingResult.getEncodedMessage()));
        }
    }

    public static void main(String[] args) throws Exception {
        initLoggers();
        String fileName = "pic.jpg";
        if (args.length != 0) {
            fileName = args[0];
        }
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        int[] message;

        MessageGenerator messageGenerator = new PictureMessageGenerator();
        try {
            message = messageGenerator.generateMessageByFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        loggers.get(LoggerType.MESSAGE).log(Arrays.toString(message));

        loggers.get(LoggerType.FREQUENCY).logf("┌──────┐");
        FrequencyCalculator frequencyCalculator = new HashMapFrequencyCalculator();
        frequencyCalculator.getFrequency(message).entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .forEach((entry) -> {
                    loggers.get(LoggerType.FREQUENCY).logf("|%-3s|%-2s|", entry.getKey(), entry.getValue());
                });
        loggers.get(LoggerType.FREQUENCY).logf("└──────┘");

        encodeByAll(message);

        closeLoggers();
    }


    public enum LoggerType {
        MESSAGE(() -> {
            return new DelegateLogger(new ConsoleLogger(), new FileLogger(new File("message.log")));
        }),
        FREQUENCY(() -> {
            return new FileLogger(new File("frequency.log"));
        }),
        UNIFORM_ENCODING(() -> {
            return new FileLogger(new File("uniform_encoding.log"));
        }),
        HUFFMAN_ENCODING(() -> {
            return new FileLogger(new File("huffman_encoding.log"));
        }),
        SHANNON_FANO_ENCODING(() -> {
            return new FileLogger(new File("shannon_fano_encoding.log"));
        });

        LoggerType(SupplierWithException<Logger, Exception> createSupplier) {
            this.createSupplier = createSupplier;
        }

        private SupplierWithException<Logger, Exception> createSupplier;


        public SupplierWithException<Logger, Exception> getCreateSupplier() {
            return createSupplier;
        }

    }

    public enum EncoderType {
        UNIFORM(new UniformEncoder(), LoggerType.UNIFORM_ENCODING),
        SHANNON_FANO(new ShannonFanoEncoder(), LoggerType.SHANNON_FANO_ENCODING),
        HUFFMAN(new HuffmanEncoder(), LoggerType.HUFFMAN_ENCODING);

        EncoderType(Encoder encoder, LoggerType loggerType) {
            this.encoder = encoder;
            this.loggerType = loggerType;
        }

        private Encoder encoder;
        private LoggerType loggerType;

        public Encoder getEncoder() {
            return encoder;
        }

        public LoggerType getLoggerType() {
            return loggerType;
        }
    }
}
