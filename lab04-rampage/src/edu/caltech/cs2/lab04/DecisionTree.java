package edu.caltech.cs2.lab04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTree {
    private final DecisionTreeNode root;

    public DecisionTree(DecisionTreeNode root) {
        this.root = root;
    }

    public String predict(Dataset.Datapoint point) {
        return recurse(this.root, point);
    }

    private String recurse(DecisionTreeNode node, Dataset.Datapoint point) {
        if (node.isLeaf()) {
            OutcomeNode new_node = (OutcomeNode) node;
            return new_node.outcome;
        }
        AttributeNode new_node = (AttributeNode) node;
        String attribute = new_node.attribute;
        Map<String, String> point_features = point.attributes;
        String feature = point_features.get(attribute);
        DecisionTreeNode child = new_node.children.get(feature);
        return recurse(child, point);
    }

    public static DecisionTree id3(Dataset dataset, List<String> attributes) {
        String sameOutcome = dataset.pointsHaveSameOutcome();
        if (sameOutcome.length() > 0) {
            return new DecisionTree(new OutcomeNode(sameOutcome));
        }

        if (attributes.isEmpty()) {
            return new DecisionTree(new OutcomeNode(dataset.getMostCommonOutcome()));
        }

        String lowestEntropy = dataset.getAttributeWithMinEntropy(attributes);
        List<String> features = dataset.getFeaturesForAttribute(lowestEntropy);

        HashMap<String, DecisionTreeNode> subtreeMap = new HashMap<>();

        for (String feature : features) {
            Dataset hasFeature = dataset.getPointsWithFeature(feature);
            if (hasFeature.isEmpty()) {
                subtreeMap.put(feature, new OutcomeNode(dataset.getMostCommonOutcome()));
            } else {
                List<String> copy = new ArrayList<>(attributes.size());
                copy.addAll(attributes);
                copy.remove(lowestEntropy);
                DecisionTree subtree = id3(hasFeature, copy);
                subtreeMap.put(feature, subtree.root);
            }
        }
        return new DecisionTree(new AttributeNode(lowestEntropy, subtreeMap));
    }
}