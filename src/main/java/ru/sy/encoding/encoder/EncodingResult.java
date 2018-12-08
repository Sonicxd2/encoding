package ru.sy.encoding.encoder;

import java.util.Map;

public class EncodingResult {
    private Map<Integer, String> dictionary;
    private String[] encodedMessage;

    public EncodingResult(Map<Integer, String> dictionary, String[] encodedMessage) {
        this.dictionary = dictionary;
        this.encodedMessage = encodedMessage;
    }

    public Map<Integer, String> getDictionary() {
        return dictionary;
    }

    public String[] getEncodedMessage() {
        return encodedMessage;
    }
}
