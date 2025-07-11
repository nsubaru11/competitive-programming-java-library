public final class AVL<T extends Comparable<T>> {
	private int capacity, size;
	private Node<T>[] nodes;

	public AVL() {
		capacity = 1024;
		size = 0;
		nodes = new Node[capacity];
	}

	public AVL(int capacity) {
		this.capacity = Integer.highestOneBit(capacity - 1) << 1;
		size = 0;
		nodes = new Node[capacity];
	}

	private void resize() {
		Node[] nodes = new Node[capacity <<= 1];
		System.arraycopy(this.nodes, 0, nodes, 0, this.capacity);
		this.nodes = nodes;
	}

	private boolean add(T value) {
		for (int i = 0; i < this.capacity; ) {
			if (nodes[i] == null) {
				nodes[i] = new Node<>(value);
				break;
			}
			int cmp = value.compareTo(nodes[i].label);
			switch (cmp) {
				case -1:
					i = ((i + 1) << 1) - 1;
					break;
				case 1:
					i = (i + 1) << 1;
					break;
				case 0:
					nodes[i].cnt++;
					return false;
			}
		}
		return true;
	}

	private void rotate(int index) {
		int r = (index + 1) << 1;
		if (nodes[r].height + 1 < nodes[r - 1].height) {
			Node<T> tmp = nodes[index];
			nodes[index] = nodes[r - 1];
			nodes[r - 1] = nodes[(r << 1) - 1];
			nodes[(r << 1) - 1] = nodes[(((r << 1) - 1) << 1) - 1];
			nodes[((r + 1) << 1) - 1] = tmp;
		}
	}

	private static final class Node<T> {
		private final T label;
		private int cnt, size, height;
		private Node<T> left, right;

		public Node(T label) {
			this.label = label;
			cnt = 1;
			size = 1;
			height = 1;
		}


	}
}