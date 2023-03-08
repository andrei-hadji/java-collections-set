package com.endava.internship.collections;

import java.util.Iterator;
import java.util.Stack;

public class TreeIterator implements Iterator<Node> {
    private final Stack<Node> stack;

    public TreeIterator(Node root) {
        stack = new Stack<>();
        pushLeft(root);
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }

    public Node next() {
        Node current = stack.pop();
        pushLeft(current.getRight());
        return current;
    }

    private void pushLeft(Node node) {
        while (node != null) {
            stack.push(node);
            node = node.getLeft();
        }
    }

}
