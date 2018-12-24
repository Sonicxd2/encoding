package ru.sy.encoding.encoder;

import java.util.Map;

public class EncodingResult {
    private Map<Integer, String> dictionary;
    private String[] encodedMessage;
    private String messageFromEncoding;

    public EncodingResult(Map<Integer, String> dictionary, String[] encodedMessage) {
        this.dictionary = dictionary;
        this.encodedMessage = encodedMessage;
    }

    public EncodingResult(Map<Integer, String> dictionary, String[] encodedMessage, String messageFromEncoding) {
        this.dictionary = dictionary;
        this.encodedMessage = encodedMessage;
        this.messageFromEncoding = messageFromEncoding;
    }


    public String getMessageFromEncoding() {
        return messageFromEncoding;
    }

    public Map<Integer, String> getDictionary() {
        return dictionary;
    }

    public String[] getEncodedMessage() {
        return encodedMessage;
    }
}
