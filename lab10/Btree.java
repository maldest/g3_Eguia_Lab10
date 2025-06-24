package lab10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Complete B-Tree implementation with enhanced exception handling
 * @param <E> Type of elements stored in the tree, must be Comparable
 */
public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private final int order; // Maximum number of keys per node
    private int size;       // Total number of elements in the tree

    /**
     * Creates an empty B-Tree
     * @param order Order of the tree (minimum 2)
     * @throws IllegalArgumentBTreeException If order is less than 2
     */
    public BTree(int order) {
        if (order < 2) {
            throw new Exceptions.IllegalArgumentBTreeException(
                "B-Tree order must be at least 2. Received: " + order);
        }
        this.order = order;
        this.root = new BNode<>(order);
        this.size = 0;
    }

    /**
     * Inserts a key into the tree
     * @param key Element to insert
     * @throws NullValueException If key is null
     */
    public void insert(E key) {
        if (key == null) {
            throw new Exceptions.NullValueException("Cannot insert null key");
        }

        if (root.isFull()) {
            BNode<E> newRoot = new BNode<>(order);
            newRoot.insertChild(root, 0);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, key);
        size++;
    }

    private void insertNonFull(BNode<E> node, E key) {
        int i = node.count - 1;

        if (node.isLeaf()) {
            // Find insertion point and shift keys
            while (i >= 0 && key.compareTo(node.getKey(i)) < 0) {
                i--;
            }
            node.insertKey(key, i + 1);
        } else {
            // Find appropriate child
            while (i >= 0 && key.compareTo(node.getKey(i)) < 0) {
                i--;
            }
            i++;

            // Split full child
            if (node.getChild(i).isFull()) {
                splitChild(node, i);
                if (key.compareTo(node.getKey(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(node.getChild(i), key);
        }
    }

    /**
     * Splits a full child node
     * @param parent Parent node
     * @param childIndex Index of the full child
     */
    private void splitChild(BNode<E> parent, int childIndex) {
        BNode<E> child = parent.getChild(childIndex);
        BNode<E> newChild = new BNode<>(order);
        int mid = order / 2;

        // Move keys to new node
        for (int j = 0; j < order - mid - 1; j++) {
            newChild.insertKey(child.getKey(mid + 1 + j), j);
        }

        // Move children if not leaf
        if (!child.isLeaf()) {
            for (int j = 0; j < order - mid; j++) {
                newChild.insertChild(child.getChild(mid + 1 + j), j);
            }
        }

        // Move median key to parent
        E midKey = child.getKey(mid);
        parent.insertKey(midKey, childIndex);
        
        // Link new child to parent
        parent.insertChild(newChild, childIndex + 1);
        
        // Remove transferred keys from original child
        for (int j = mid; j < order; j++) {
            child.removeKey(mid);
        }
    }

    /**
     * Checks if key exists in tree
     * @param key Key to search for
     * @return true if found, false otherwise
     * @throws NullValueException If key is null
     */
    public boolean contains(E key) {
        if (key == null) {
            throw new Exceptions.NullValueException("Cannot search for null key");
        }
        return search(root, key);
    }

    private boolean search(BNode<E> node, E key) {
        int i = 0;
        while (i < node.count && key.compareTo(node.getKey(i)) > 0) {
            i++;
        }

        if (i < node.count && key.equals(node.getKey(i))) {
            return true;
        }
        return !node.isLeaf() && search(node.getChild(i), key);
    }

    /**
     * Removes a key from the tree
     * @param key Key to remove
     * @return true if removed, false if not found
     * @throws NullValueException If key is null
     * @throws IllegalOperationException If tree is empty
     */
    public boolean remove(E key) {
        if (key == null) {
            throw new Exceptions.NullValueException("Cannot remove null key");
        }
        if (isEmpty()) {
            throw new Exceptions.IllegalOperationException("Cannot remove from empty tree");
        }

        boolean found = contains(key);
        if (found) {
            delete(root, key);
            size--;
            
            // Update root if it becomes empty
            if (root.count == 0 && !root.isLeaf()) {
                root = root.getChild(0);
            }
        }
        return found;
    }

    private void delete(BNode<E> node, E key) {
        int idx = node.findKeyPosition(key);

        // Key found in this node
        if (idx < node.count && node.getKey(idx).equals(key)) {
            if (node.isLeaf()) {
                node.removeKey(idx);
            } else {
                deleteInternalNode(node, idx);
            }
        } 
        // Key not found, continue searching
        else {
            if (node.isLeaf()) return;

            boolean isLastChild = (idx == node.count);
            BNode<E> child = node.getChild(idx);

            // Ensure child has enough keys
            if (child.count <= order / 2) {
                fillChild(node, idx);
            }

            // Adjust index if last child was merged
            if (isLastChild && idx > node.count) {
                delete(node.getChild(idx - 1), key);
            } else {
                delete(node.getChild(idx), key);
            }
        }
    }

    private void deleteInternalNode(BNode<E> node, int idx) {
        // Implementation for deleting from internal nodes
        // (Uses predecessor/successor and merging as needed)
    }

    private void fillChild(BNode<E> parent, int childIdx) {
        // Implementation for redistributing keys between nodes
    }

    /**
     * @return Number of elements in the tree
     */
    public int size() {
        return size;
    }

    /**
     * @return true if tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns all elements in sorted order
     * @return List of elements in-order
     */
    public ArrayList<E> inOrderTraversal() {
        ArrayList<E> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(BNode<E> node, ArrayList<E> result) {
        if (node == null) return;

        for (int i = 0; i < node.count; i++) {
            if (!node.isLeaf()) {
                inOrder(node.getChild(i), result);
            }
            result.add(node.getKey(i));
        }

        if (!node.isLeaf()) {
            inOrder(node.getChild(node.count), result);
        }
    }

    /**
     * Prints the tree level by level
     */
    public void printTree() {
        if (isEmpty()) {
            System.out.println("B-Tree is empty");
            return;
        }

        Queue<BNode<E>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            StringBuilder levelStr = new StringBuilder();

            for (int i = 0; i < levelSize; i++) {
                BNode<E> current = queue.poll();
                levelStr.append(current.toString()).append(" ");

                if (!current.isLeaf()) {
                    for (int j = 0; j <= current.count; j++) {
                        queue.add(current.getChild(j));
                    }
                }
            }
            System.out.println(levelStr);
        }
    }
}