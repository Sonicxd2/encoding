package ru.sy.encoding;

import java.io.File;

public interface MessageGenerator {
    int[] generateMessageByFile(File file) throws Exception;
}
