package com.endava.internship.collections;

import java.util.*;

public class StudentSet implements Set<Student> {
    private Node root;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Student) {
            return containsHelper(root, new Node((Student) o));
        } else return false;
    }

    private boolean containsHelper(Node current, Node value) {
        if (Objects.isNull(current)) {
            return false;
        }

        final int toCompare = current.compareTo(value);

        if (toCompare == 0) {
            return true;
        }

        if (toCompare > 0) {
            return containsHelper(current.getLeft(), value);
        } else {
            return containsHelper(current.getRight(), value);
        }
    }

    @Override
    public Iterator<Student> iterator() {
        return new StudentIterator(root);
    }

    @Override
    public Object[] toArray() {
        final Object[] arrayFromSet = new Object[size];

        int count = 0;
        if (root != null) {
            for (Object node : root) {
                arrayFromSet[count] = node;
                count++;
            }
            return arrayFromSet;
        } else {
            throw new NullPointerException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] ts) {
        T[] result = (T[]) toArray();
        int size = size();

        if (ts.length < size) {
            ts = Arrays.copyOf(ts, size);
        } else if (ts.length > size) {
            ts[size] = null;
        }

        System.arraycopy(result, 0, ts, 0, size);

        return ts;
    }

    @Override
    public boolean add(Student student) {
        if (root == null) {
            root = new Node(student);
            size++;
            return true;
        } else {
            return addHelper(student, root);
        }
    }

    private boolean addHelper(Student student, Node node) {
        if (student.compareTo(node.getStudent()) == 0) return false;

        if (student.compareTo(node.getStudent()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new Node(student));
                size++;
            } else {
                addHelper(student, node.getLeft());
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(new Node(student));
                size++;
            } else {
                addHelper(student, node.getRight());
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        // node was not found
        if (root != null) {
            Node parent = null;
            Node current = root;
            while (current != null) {
                int cmp = ((Comparable<Student>) o).compareTo(current.getStudent());
                if (cmp == 0) { // found the node to remove
                    removeNode(current, parent);
                    size--;
                    return true; // node was successfully removed
                } else if (cmp < 0) {
                    parent = current;
                    current = current.getLeft();
                } else {
                    parent = current;
                    current = current.getRight();
                }
            }
        }
        return false; // tree is empty
    }

    private void removeNode(Node node, Node parent) {
        if (node.getLeft() == null && node.getRight() == null) {
            // case 1: node has no children
            removeLeafNode(node, parent);
        } else if (node.getLeft() == null) {
            // case 2: node has one child on the right
            removeNodeWithOneChild(node, parent, node.getRight());
        } else if (node.getRight() == null) {
            // case 2: node has one child on the left
            removeNodeWithOneChild(node, parent, node.getLeft());
        } else {
            // case 3: node has two children
            removeNodeWithTwoChildren(node, parent);
        }
    }

    private void removeLeafNode(Node node, Node parent) {
        if (parent == null) {
            root = null;
        } else if (parent.getLeft() == node) {
            parent.setLeft(null);
        } else {
            parent.setRight(null);
        }
    }

    private void removeNodeWithOneChild(Node node, Node parent, Node child) {
        if (parent == null) {
            root = child;
        } else if (parent.getLeft() == node) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }
    }

    private void removeNodeWithTwoChildren(Node node, Node parent) {
        Node successor = getSuccessor(node);
        if (parent == null) {
            root = successor;
        } else if (parent.getLeft() == node) {
            parent.setLeft(successor);
        } else {
            parent.setRight(successor);
        }
        successor.setLeft(node.getLeft());
    }

    private Node getSuccessor(Node node) {
        Node parent = node;
        Node successor = node.getRight();
        while (successor.getLeft() != null) {
            parent = successor;
            successor = successor.getLeft();
        }
        if (parent != node) {
            parent.setLeft(successor.getRight());
            successor.setRight(node.getRight());
        }
        return successor;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean addAll(Collection<? extends Student> collection) {
        boolean isAdded = false;
        for (Student student : collection) {
            isAdded = add(student);
            if (!isAdded) {
                return false;
            }
        }
        return isAdded;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (element instanceof Student) {
                if (!contains(element)) {
                    return false;
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;

        Iterator<Student> it = iterator();

        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;

        for (Object element : collection) {
            if (!(element instanceof Student)) throw new ClassCastException();
            if (contains(element)) {
                modified |= remove(element);
            }
        }
        return modified;
    }

    public class StudentIterator implements Iterator<Student> {
        private final TreeIterator iterator;
        private Node lastReturned = null;

        public StudentIterator(Node root) {
            this.iterator = new TreeIterator(root);
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Student next() {
            lastReturned = iterator.next();
            return lastReturned.getStudent();
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalArgumentException("Cannot remove before calling next");
            }
            StudentSet.this.remove(lastReturned.getStudent());
            lastReturned = null;
        }
    }

}
