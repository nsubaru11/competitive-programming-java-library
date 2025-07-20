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
		if ()
	}

	private static final class Node<T extends Comparable<T>> {
		private final T label;
		private int cnt, size, height;
		private Node<T> parent, left, right;

		public Node(T label) {
			this.label = label;
			cnt = size = height = 1;
		}

		private Node add(T value) {

			int cmp = label.compareTo(value);
			if (cmp < 0) {
				if (right == null) {
					right = new Node<>(value);
					return true;
				}
				boolean isAdded = right.add(this, value);
				return isAdded;
			} else if (cmp > 0) {
				if (left == null) {
					left = new Node<>(value);
					return true;
				}
				boolean isAdded = left.add(this, value);
				return isAdded;
			} else {
				cnt++;
				return false;
			}
		}
	}
}