package ru.sy.encoding.encoder;

import ru.sy.encoding.frequency.HashMapFrequencyCalculator;

import java.util.HashMap;
import java.util.Map;

public class UniformEncoder implements Encoder {
    public int binlog(int bits) {
        int log = 0;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }

    public String toBinaryFormat(int val, int amountOfNumber) {
        StringBuilder res = new StringBuilder();
        while (val != 0) {
            res.insert(0, val % 2);
            val = val / 2;
        }
        for (int i = res.length(); i < amountOfNumber; i++) {
            res.insert(0, "0");
        }
        return res.toString();
    }


    @Override
    public EncodingResult encode(int[] message) {
        Map<Integer, Integer> frequency = new HashMapFrequencyCalculator().getFrequency(message);
        int length = binlog(frequency.size());
        if (frequency.size() != (1 << length)) {
            length++;
        }
        HashMap<Integer, String> dictionary = new HashMap<>();
        int counter = 0;
        for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
            dictionary.put(entry.getKey(), toBinaryFormat(counter++, length));
        }
        String[] encoded = new String[message.length];
        for (int i = 0; i < message.length; i++) {
            encoded[i] = dictionary.get(message[i]);
        }
        return new EncodingResult(dictionary, encoded);
    }
}
