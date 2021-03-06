package ru.sy.encoding.encoder;

import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder implements Encoder {
    private String generateString(int[] message) {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < message.length; i++) {
            builder.append((char) message[i]);
        }
        return builder.toString();
    }

    @Override
    public EncodingResult encode(int[] message) {
        HuffmanCode huffmanCode = new HuffmanCode(generateString(message));
        Map<Character, String> preDictionary = huffmanCode.getCompressedResult();
        Map<Integer, String> dictionary = new HashMap<>();
        preDictionary.entrySet().stream().forEach((entry) -> {
            dictionary.put(Integer.valueOf(entry.getKey()), entry.getValue());
        });
        String[] encoded = new String[message.length];
        for (int i = 0; i < message.length; i++) {
            encoded[i] = dictionary.get(message[i]);
        }
        return new EncodingResult(dictionary, encoded, huffmanCode.toString());
    }
}
