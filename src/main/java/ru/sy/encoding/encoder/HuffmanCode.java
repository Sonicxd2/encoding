package ru.sy.encoding.encoder;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * From https://github.com/amir734jj/compress-string/blob/master/src/HuffmanCode.java
 */
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final double frequency;

    public HuffmanTree(double freq) {
        frequency = freq;
    }

    public int compareTo(HuffmanTree tree) {
        return Double.compare(frequency, tree.frequency);
    }
}

class HuffmanLeaf extends HuffmanTree {
    public final char value;

    public HuffmanLeaf(double freq, char val) {
        super(freq);
        value = val;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right;

    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", left.toString(), right.toString());
    }
}

public class HuffmanCode {
    private static final int ASCII_LENGTH = 30;

    public String originalString;
    public int originalStringLength;
    private HashMap<Character, String> compressedResult;
    private HashMap<Character, Double> characterFrequency;
    private double entropy;
    private PriorityQueue<HuffmanTree> huffmanTrees;
    HuffmanTree mainTree;
    private double averageLengthBefore;
    private double averageLengthAfter;
    private boolean probabilityIsGiven;
    private StringBuilder treeBuildLog = new StringBuilder();

    public HuffmanCode(String str) {
        super();
        originalString = str;
        originalStringLength = str.length();
        characterFrequency = new HashMap<Character, Double>();
        compressedResult = new HashMap<Character, String>();
        entropy = 0.0;
        averageLengthBefore = 0.0;
        averageLengthAfter = 0.0;
        huffmanTrees = new PriorityQueue<HuffmanTree>();
        probabilityIsGiven = false;

        this.calculateFrequency();
        this.buildTree();
        this.buildString(mainTree, new StringBuffer(), compressedResult);
        this.calculateEntropy();
        this.calculateAverageLengthBeforeCompression();
        this.calculateAverageLengthAfterCompression();
    }

    public HuffmanCode(String str, HashMap<Character, Double> probablity) {
        super();
        originalString = str;
        originalStringLength = str.length();

        characterFrequency = new HashMap<Character, Double>();

        double checkPoint = 0;
        for (Character c : originalString.toCharArray()) {
            checkPoint += probablity.get(c);
            characterFrequency.put(c, originalStringLength * probablity.get(c));
        }

        assert checkPoint == 1.0; // Invariant, make sure sum of probabilities
        // is 1

        compressedResult = new HashMap<Character, String>();
        entropy = 0.0;
        averageLengthBefore = 0.0;
        averageLengthAfter = 0.0;
        huffmanTrees = new PriorityQueue<HuffmanTree>();
        probabilityIsGiven = true;

        treeBuildLog.append("Tree builder:\n");

        this.buildTree();
        this.buildString(mainTree, new StringBuffer(), compressedResult);
        this.calculateEntropy();
        this.calculateAverageLengthBeforeCompression();
        this.calculateAverageLengthAfterCompression();
    }

    private void buildTree() {
        for (Character c : characterFrequency.keySet()) {
            huffmanTrees.offer(new HuffmanLeaf(characterFrequency.get(c), c));
        }

        assert huffmanTrees.size() >= 1;

        while (huffmanTrees.size() >= 2) {
            HuffmanTree a = huffmanTrees.poll();
            HuffmanTree b = huffmanTrees.poll();

            HuffmanNode node = new HuffmanNode(a, b);
            treeBuildLog.append(node.toString()).append('\n');
            huffmanTrees.offer(node);
        }
        mainTree = huffmanTrees.poll();
    }

    private void buildString(HuffmanTree tree, StringBuffer prefix, HashMap<Character, String> result) {
        assert tree != null; // Invariant, make sure tree is not empty
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            result.put(leaf.value, prefix.toString());

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            prefix.append('0');
            buildString(node.left, prefix, result);
            prefix.deleteCharAt(prefix.length() - 1);

            prefix.append('1');
            buildString(node.right, prefix, result);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    private void calculateFrequency() {
        for (char c : originalString.toCharArray()) {
            if (characterFrequency.containsKey(c)) {
                characterFrequency.put(c, new Double(characterFrequency.get(c) + 1.0));
            } else {
                characterFrequency.put(c, 1.0);
            }
        }
    }

    private void calculateEntropy() {
        double probability = 0.0;
        for (Character c : originalString.toCharArray()) {
            probability = 1.0 * characterFrequency.get(c) / originalStringLength;
            entropy += probability * (Math.log(1.0 / probability) / Math.log(2));
        }
    }

    private void calculateAverageLengthBeforeCompression() {
        double probability = 0.0;
        for (Character c : originalString.toCharArray()) {
            probability = 1.0 * characterFrequency.get(c) / originalStringLength;
            averageLengthBefore += probability * ASCII_LENGTH;
        }
    }

    private void calculateAverageLengthAfterCompression() {
        double probability = 0.0;
        for (Character c : originalString.toCharArray()) {
            probability = 1.0 * characterFrequency.get(c) / originalStringLength;
            averageLengthAfter += probability * compressedResult.get(c).length();
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<Character, Double> getCharacterFrequency() {
        return (HashMap<Character, Double>) characterFrequency.clone();
    }

    @SuppressWarnings("unchecked")
    public HashMap<Character, String> getCompressedResult() {
        return (HashMap<Character, String>) compressedResult.clone();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("------------------------------------------------------------------------\n");
        str.append("Efficiency before Compression: ").append(100 * (Math.round((entropy / averageLengthBefore) * 100.0) / 100.0)).append("%\n");
        str.append("Efficiency after Compression: " + 100 * (Math.round((entropy / averageLengthAfter) * 100.0) / 100.0)
                + "%\n");
        str.append("------------------------------------------------------------------------\n");
        str.append(treeBuildLog);
        return str.toString();
    }
}