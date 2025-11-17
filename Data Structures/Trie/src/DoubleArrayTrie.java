import java.util.HashMap;
import java.util.Map;

public final class DoubleArrayTrie {
	private int[] base;  // BASE配列
	private int[] check; // CHECK配列
	private boolean[] terminal; // 終端フラグ
	private int size;

	public DoubleArrayTrie(int capacity) {
		this.base = new int[capacity];
		this.check = new int[capacity];
		this.terminal = new boolean[capacity];
		this.size = 1; // インデックス0は未使用
	}

	/**
	 * 静的構築（全単語を一度に登録）
	 */
	public void build(String[] words) {
		// 1. ソート
		java.util.Arrays.sort(words);

		// 2. Trie構築
		Node root = buildTrie(words);

		// 3. Double Array変換
		base[0] = 1; // ルートのBASE
		allocate(root, 0);
	}

	private Node buildTrie(String[] words) {
		Node root = new Node();
		for (String word : words) {
			Node cur = root;
			for (char c : word.toCharArray()) {
				cur = cur.children.computeIfAbsent(c, _ -> new Node());
			}
			cur.end = true;
		}
		return root;
	}

	private void allocate(Node node, int parentIdx) {
		if (node.children.isEmpty()) return;

		// 空きBASE値を探索
		int baseVal = findBase(node.children.keySet());
		base[parentIdx] = baseVal;

		for (var entry : node.children.entrySet()) {
			char c = entry.getKey();
			Node child = entry.getValue();
			int childIdx = baseVal + c;

			// 配列拡張
			ensureCapacity(childIdx + 1);

			check[childIdx] = parentIdx;
			terminal[childIdx] = child.end;

			allocate(child, childIdx);
		}
	}

	private int findBase(java.util.Set<Character> chars) {
		int base = 1;
		outer:
		while (true) {
			for (char c : chars) {
				if (check[base + c] != 0) {
					base++;
					continue outer;
				}
			}
			return base;
		}
	}

	public boolean search(String word) {
		int idx = 0;
		for (char c : word.toCharArray()) {
			int nextIdx = base[idx] + c;
			if (check[nextIdx] != idx) return false;
			idx = nextIdx;
		}
		return terminal[idx];
	}

	private void ensureCapacity(int required) {
		if (required >= base.length) {
			int newSize = Math.max(required + 1, base.length * 2);
			base = java.util.Arrays.copyOf(base, newSize);
			check = java.util.Arrays.copyOf(check, newSize);
			terminal = java.util.Arrays.copyOf(terminal, newSize);
		}
	}

	private static class Node {
		Map<Character, Node> children = new HashMap<>();
		boolean end;
	}
}
