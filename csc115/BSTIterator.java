import java.util.LinkedList;

/**
 * @author Mike Zastre, UVic
 *
 * Part of CSC 115, Summer 2015, Assignment #5
 */

public class BSTIterator implements java.util.Iterator<WordRefs> {
    private BSTRefBased t;
    private WordRefs currentItem;
    private LinkedList<WordRefs> list;

    public BSTIterator(BSTRefBased t) {
        this.t = t;
        currentItem = null;
        list = new LinkedList<>();
        setInorder();
    }

    public boolean hasNext() {
        return !list.isEmpty();
    }

    public WordRefs next() throws java.util.NoSuchElementException {
        currentItem = list.remove();
		return currentItem;
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setPreorder() {
		list.clear();
		preorder(t.getRoot());
    }

    public void setInorder() {
		list.clear();
		inorder(t.getRoot());
    }

    public void setPostorder() {
		list.clear();
		postorder(t.getRoot());
    }

    private void preorder(TreeNode node) {
		if (node != null) {
            list.add(node.item);
            preorder(node.left);
            preorder(node.right);
        }
    }

    private void inorder(TreeNode node) {
		if (node != null) {
            inorder(node.left);
            list.add(node.item);
            inorder(node.right);
        }
    }

    private void postorder(TreeNode node) {
		if (node != null) {
            postorder(node.left);
            postorder(node.right);
            list.add(node.item);
        }
    }
}
