package ru.sy.encoding.frequency;

import java.util.HashMap;
import java.util.Map;

public class HashMapFrequencyCalculator implements FrequencyCalculator {
    @Override
    public Map<Integer, Integer> getFrequency(int[] message) {
        HashMap<Integer, Integer> frequency = new HashMap<>();
        for (int character: message) {
            Integer val = frequency.get(character);
            frequency.put(character, val == null ? 1 : val + 1);
        }
        return frequency;
    }
}
