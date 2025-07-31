import static java.lang.Math.*;

@SuppressWarnings("unused")
public final class AVL<T extends Comparable<T>> {
	private int size;
	private Node<T> root;

	public AVL() {
		size = 0;
		root = null;
	}

	public boolean add(T value) {
		if (size == 0) {
			root = new Node<>(value);
			return true;
		}
		root = root.add(value);
		boolean isAdded = size != root.size;
		size = root.size;
		return isAdded;
	}

	private static final class Node<T extends Comparable<T>> {
		private final T label;
		private int cnt, size, height;
		private Node<T> left, right;

		public Node(T label) {
			this.label = label;
			cnt = size = height = 1;
		}

		private Node<T> add(T value) {
			int cmp = label.compareTo(value);
			if (cmp < 0) {
				right = right == null ? new Node<>(value) : right.add(value);
			} else if (cmp > 0) {
				left = left == null ? new Node<>(value) : left.add(value);
			} else {
				cnt++;
			}
			updateNode(this);
			int balance = balanceFactor();
			return balance == 0 ? this : rotate(balance);
		}

		private Node<T> rotate(int balance) {
			return balance > 0 ? rotateLeft(balance) : rotateRight(balance);
		}

		// 左回転
		private Node<T> rotateLeft(int balance) {
			Node<T> newRoot;
			if (balance == 2) {
				// RR 1 重回転
				newRoot = this.right;
				Node<T> temp = newRoot.left;
				newRoot.left = this;
				newRoot.left.right = temp;
			} else {
				// RL 2重回転
				newRoot = this.right.left;
				Node<T> tempLeft = newRoot.left;
				Node<T> tempRight = newRoot.right;
				newRoot.left = this;
				newRoot.right = this.right;
				newRoot.left.right = tempLeft;
				newRoot.right.left = tempRight;
				updateNode(newRoot.right);
			}
			updateNode(newRoot.left);
			updateNode(newRoot);
			return newRoot;
		}

		// 右回転
		private Node<T> rotateRight(int balance) {
			Node<T> newRoot;
			if (balance == -1) {
				// LR 2重回転
				newRoot = this.left.right;
				Node<T> tempLeft = newRoot.left;
				Node<T> tempRight = newRoot.right;
				newRoot.right = this;
				newRoot.left = this.left;
				newRoot.right.left = tempRight;
				newRoot.left.right = tempLeft;
				updateNode(newRoot.left);
			} else {
				// LL 1 重回転
				newRoot = this.left;
				Node<T> temp = newRoot.right;
				newRoot.right = this;
				newRoot.right.left = temp;
			}
			updateNode(newRoot.right);
			updateNode(newRoot);
			return newRoot;
		}

		private void updateNode(Node<T> node) {
			Node<T> left = node.left, right = node.right;
			node.size = (left == null ? 0 : left.size) + (right == null ? 0 : right.size) + 1;
			node.height = 1 + max(left == null ? 0 : left.height, right == null ? 0 : right.height);
		}

		private int balanceFactor() {
			int l = left == null ? 0 : left.height;
			int r = right == null ? 0 : right.height;
			int ll = l == 0 ? 0 : left.left == null ? 0 : left.left.height;
			int lr = l == 0 ? 0 : left.right == null ? 0 : left.right.height;
			int rl = r == 0 ? 0 : right.left == null ? 0 : right.left.height;
			int rr = r == 0 ? 0 : right.right == null ? 0 : right.right.height;
			if (l + 1 < r) {
				return rl > rr ? 1 : 2;
			} else if (l - 1 > r) {
				return ll < lr ? -1 : -2;
			} else {
				return 0;
			}
		}
	}
}