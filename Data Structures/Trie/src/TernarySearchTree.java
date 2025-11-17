public final class TernarySearchTree {
	private Node root;

	public void insert(String word) {
		root = insert(root, word, 0);
	}

	private Node insert(Node node, String word, int pos) {
		char c = word.charAt(pos);

		if (node == null) node = new Node(c);

		if (c < node.c) {
			node.left = insert(node.left, word, pos);
		} else if (c > node.c) {
			node.right = insert(node.right, word, pos);
		} else {
			if (pos + 1 < word.length()) {
				node.mid = insert(node.mid, word, pos + 1);
			} else {
				node.end = true;
			}
		}
		return node;
	}

	public boolean search(String word) {
		Node node = search(root, word, 0);
		return node != null && node.end;
	}

	private Node search(Node node, String word, int pos) {
		if (node == null) return null;

		char c = word.charAt(pos);

		if (c < node.c) return search(node.left, word, pos);
		if (c > node.c) return search(node.right, word, pos);

		if (pos + 1 < word.length()) {
			return search(node.mid, word, pos + 1);
		}
		return node;
	}

	// プレフィックス検索
	public int countPrefix(String prefix) {
		Node node = search(root, prefix, 0);
		return node == null ? 0 : countNodes(node.mid);
	}

	private int countNodes(Node node) {
		if (node == null) return 0;
		int count = node.end ? 1 : 0;
		count += countNodes(node.left);
		count += countNodes(node.mid);
		count += countNodes(node.right);
		return count;
	}

	private static class Node {
		char c;
		Node left, mid, right; // <, =, >
		boolean end;

		Node(char c) {
			this.c = c;
		}
	}
}
