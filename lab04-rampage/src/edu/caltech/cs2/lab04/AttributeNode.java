package edu.caltech.cs2.lab04;

import java.util.Map;

public class AttributeNode implements DecisionTreeNode {
    public final String attribute;
    // Map from features to child nodes
    public final Map<String, DecisionTreeNode> children;

    public AttributeNode(String attribute, Map<String, DecisionTreeNode> children) {
        this.attribute = attribute;
        this.children = children;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
