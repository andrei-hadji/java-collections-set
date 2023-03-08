package com.endava.internship.collections;

import java.util.Iterator;

public class Node implements Comparable<Node>, Iterable<Node> {
    private Node value;
    private Node left;
    private Node right;
    private Student student;

    public Node(Student student) {
        this.student = student;
        this.left = null;
        this.right = null;
    }

    public Node getValue() {
        return value;
    }

    public void setValue(Node value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Student getStudent() {
        return student;
    }

    public Iterator<Node> iterator(){
        return new TreeIterator(this);
    }
    @Override
    public int compareTo(final Node nodeToCompare) {
        return this.student.compareTo(nodeToCompare.student);
    }
}
