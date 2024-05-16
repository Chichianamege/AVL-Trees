// Name: Chidera Anamege
// Class: CS 3305/ W01
// Term: Spring 2024
//Instructor: Carla McManus
//Assignment: 9-Part-2-AVL

import java.util.ArrayList;

// Tree interface
interface Tree<E extends Comparable<E>> {
    boolean search(E e);  // Checks if the element e is in the tree
    boolean insert(E e);  // Inserts element e into the tree
    boolean delete(E e);  // Deletes element e from the tree
    void inorder();       // Traverses the tree in inorder fashion
    void postorder();     // Traverses the tree in postorder fashion
    void preorder();      // Traverses the tree in preorder fashion
    int getSize();        // Returns the size of the tree
    boolean isEmpty();    // Checks if the tree is empty
}

// AbstractTree class
abstract class AbstractTree<E extends Comparable<E>> implements Tree<E> {

    // Traverses the tree in inorder (left-root-right) and performs the specified action
    @Override
    public void inorder() {
    }

    // Traverses the tree in postorder (left-right-root) and performs the specified action
    @Override
    public void postorder() {
    }

    // Traverses the tree in preorder (root-left-right) and performs the specified action
    @Override
    public void preorder() {
    }

    // Checks if the tree is empty
    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }
}


// BST class
class BST<E extends Comparable<E>> extends AbstractTree<E> {
    protected TreeNode<E> root;
    protected int size = 0;

    // Constructor
    public BST() {
    }

    // Constructor with elements
    public BST(E[] objects) {
        for (E object : objects)
            insert(object);
    }

    // Inner class for TreeNode
    protected static class TreeNode<E extends Comparable<E>> {
        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;
        protected int height; // Height of the node

        public TreeNode(E e) {
            element = e;
            height = 1; //  node has height 1
        }
    }


    // Override insert method
    @Override
    public boolean insert(E e) {
        if (root == null) {
            root = createNewNode(e);
        } else {
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null) {
                parent = current;
                if (e.compareTo(current.element) < 0) {
                    current = current.left;
                } else if (e.compareTo(current.element) > 0) {
                    current = current.right;
                } else {
                    return false; // Duplicate not allowed
                }
            }

            // Insert the new node as a leaf
            TreeNode<E> newNode = createNewNode(e);
            if (e.compareTo(parent.element) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
        size++;
        return true;
    }

    // Create a new TreeNode
    protected TreeNode<E> createNewNode(E e) {
        return new TreeNode<>(e);
    }

    // Override search method
    @Override
    public boolean search(E e) {
        TreeNode<E> current = root;
        while (current != null)
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else
                return true;
        return false;
    }

    // Override delete method
    @Override
    public boolean delete(E e) {
        // Left as an exercise
        return false;
    }

    // Override inorder method
    @Override
    public void inorder() {
        inorder(root);
    }

    // Inorder helper method
    protected void inorder(TreeNode<E> root) {
        if (root == null) return;
        inorder(root.left);
        System.out.print(root.element + " ");
        inorder(root.right);
    }

    // Override postorder method
    @Override
    public void postorder() {
        postorder(root);
    }

    // Postorder helper method
    protected void postorder(TreeNode<E> root) {
        if (root == null) return;
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.element + " ");
    }

    // Override preorder method
    @Override
    public void preorder() {
        preorder(root);
    }

    // Preorder helper method
    protected void preorder(TreeNode<E> root) {
        if (root == null) return;
        System.out.print(root.element + " ");
        preorder(root.left);
        preorder(root.right);
    }

    // Override getSize method
    @Override
    public int getSize() {
        return size;
    }
}

// AVL class
class AVL<E extends Comparable<E>> extends BST<E> {
    // Override insert method
    @Override
    public boolean insert(E e) {
        root = insertRecursive(root, e);
        if (root == null)
            return false;
        size++;
        return true;
    }

    // Recursive insert method
    private TreeNode<E> insertRecursive(TreeNode<E> node, E e) {
        if (node == null)
            return new TreeNode<>(e);

        if (e.compareTo(node.element) < 0)
            node.left = insertRecursive(node.left, e);
        else if (e.compareTo(node.element) > 0)
            node.right = insertRecursive(node.right, e);
        else
            return node; // Duplicate not allowed

        // Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Balance the tree
        int balance = getBalance(node);

        // Left Left case
        if (balance > 1 && e.compareTo(node.left.element) < 0)
            return rotateRight(node);

        // Right Right case
        if (balance < -1 && e.compareTo(node.right.element) > 0)
            return rotateLeft(node);

        // Left Right case
        if (balance > 1 && e.compareTo(node.left.element) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left case
        if (balance < -1 && e.compareTo(node.right.element) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // AVL balancing methods
    private TreeNode<E> rotateRight(TreeNode<E> root) {
        TreeNode<E> newRoot = root.left;
        TreeNode<E> temp = newRoot.right;
        newRoot.right = root;
        root.left = temp;

        // Update heights
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;

        return newRoot;
    }

    private TreeNode<E> rotateLeft(TreeNode<E> root) {
        TreeNode<E> newRoot = root.right;
        TreeNode<E> temp = newRoot.left;
        newRoot.left = root;
        root.right = temp;

        // Update heights
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;

        return newRoot;
    }

    private int height(TreeNode<E> node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private int getBalance(TreeNode<E> node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }
}


// Test class
public class dsassignment9partone {
    public static void main(String[] args) {
        // Test AVL tree
        AVL<Integer> avlTree = new AVL<>();
        avlTree.insert(30);
        avlTree.insert(20);
        avlTree.insert(10);
        avlTree.insert(40);
        avlTree.insert(50);
        avlTree.insert(25);
        avlTree.insert(35);

        // Test BST methods
        System.out.print("Inorder traversal: ");
        avlTree.inorder();
        System.out.println();

        System.out.print("Postorder traversal: ");
        avlTree.postorder();
        System.out.println();

        System.out.print("Preorder traversal: ");
        avlTree.preorder();
        System.out.println();
    }
}
