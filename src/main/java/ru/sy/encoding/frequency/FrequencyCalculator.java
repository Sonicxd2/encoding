package ru.sy.encoding.frequency;

import java.util.Map;

public interface FrequencyCalculator {
    Map<Integer, Integer> getFrequency(int[] message);
}
