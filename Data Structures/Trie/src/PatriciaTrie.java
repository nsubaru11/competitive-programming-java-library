public final class PatriciaTrie {
	private Node root = new Node(0);

	public void insert(String key) {
		root = insert(root, key, 0);
	}

	private Node insert(Node node, String key, int bit) {
		if (node == null) {
			Node leaf = new Node(bit);
			leaf.key = key;
			leaf.end = true;
			return leaf;
		}

		if (node.end) {
			// 既存のキーと比較
			int diffBit = findDifferingBit(node.key, key);
			if (diffBit == -1) return node; // 重複

			return split(node, key, diffBit);
		}

		if (getBit(key, node.bit) == 0) {
			node.left = insert(node.left, key, node.bit + 1);
		} else {
			node.right = insert(node.right, key, node.bit + 1);
		}
		return node;
	}

	private int getBit(String key, int bit) {
		int bytePos = bit / 8;
		int bitPos = bit % 8;
		if (bytePos >= key.length()) return 0;
		return (key.charAt(bytePos) >> (7 - bitPos)) & 1;
	}

	private int findDifferingBit(String a, String b) {
		int minLen = Math.min(a.length(), b.length());
		for (int i = 0; i < minLen * 8; i++) {
			if (getBit(a, i) != getBit(b, i)) return i;
		}
		return a.length() == b.length() ? -1 : minLen * 8;
	}

	private Node split(Node oldLeaf, String newKey, int diffBit) {
		Node newNode = new Node(diffBit);

		if (getBit(oldLeaf.key, diffBit) == 0) {
			newNode.left = oldLeaf;
			newNode.right = new Node(diffBit + 1);
			newNode.right.key = newKey;
			newNode.right.end = true;
		} else {
			newNode.right = oldLeaf;
			newNode.left = new Node(diffBit + 1);
			newNode.left.key = newKey;
			newNode.left.end = true;
		}
		return newNode;
	}

	public boolean search(String key) {
		Node cur = root;
		while (cur != null && !cur.end) {
			cur = getBit(key, cur.bit) == 0 ? cur.left : cur.right;
		}
		return cur != null && cur.key.equals(key);
	}

	private static class Node {
		int bit; // 判定するビット位置
		Node left, right; // 0分岐, 1分岐
		String key; // 終端ノードのキー
		boolean end;

		Node(int bit) {
			this.bit = bit;
		}
	}
}
