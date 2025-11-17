import java.util.HashMap;
import java.util.Map;

public final class RadixTrie {
	private final Node root = new Node("", false);

	public void insert(String word) {
		Node cur = root;
		int pos = 0;

		while (pos < word.length()) {
			char firstChar = word.charAt(pos);
			Node child = cur.children.get(firstChar);

			if (child == null) {
				// 残り全てを新ノードとして追加
				cur.children.put(firstChar, new Node(word.substring(pos), true));
				return;
			}

			// プレフィックスとのマッチング
			String prefix = child.prefix;
			int matchLen = getMatchLength(word, pos, prefix);

			if (matchLen < prefix.length()) {
				// ノード分割が必要
				splitNode(cur, child, firstChar, matchLen);
			}

			pos += matchLen;

			if (pos == word.length()) {
				child.end = true;
				return;
			}

			cur = child;
		}
	}

	private int getMatchLength(String word, int wordPos, String prefix) {
		int len = 0;
		while (len < prefix.length() &&
				wordPos + len < word.length() &&
				word.charAt(wordPos + len) == prefix.charAt(len)) {
			len++;
		}
		return len;
	}

	private void splitNode(Node parent, Node child, char firstChar, int splitPos) {
		// 元のノード: "testing" → "test"
		// 新しい子: "ing"
		String oldPrefix = child.prefix;
		String newPrefix = oldPrefix.substring(0, splitPos);
		String remainder = oldPrefix.substring(splitPos);

		Node newNode = new Node(newPrefix, false);
		newNode.children.put(remainder.charAt(0),
				new Node(remainder, child.end));

		// 元の子の children を移動
		newNode.children.get(remainder.charAt(0)).children = child.children;

		parent.children.put(firstChar, newNode);
	}

	public boolean search(String word) {
		Node node = findNode(word);
		return node != null && node.end;
	}

	private Node findNode(String word) {
		Node cur = root;
		int pos = 0;

		while (pos < word.length()) {
			char c = word.charAt(pos);
			Node child = cur.children.get(c);
			if (child == null) return null;

			String prefix = child.prefix;
			if (!word.startsWith(prefix, pos)) return null;

			pos += prefix.length();
			cur = child;
		}
		return cur;
	}

	private static class Node {
		String prefix; // 圧縮された文字列
		Map<Character, Node> children; // 次の文字 → 子ノード
		boolean end;
		int freq;

		Node(String prefix, boolean end) {
			this.prefix = prefix;
			this.end = end;
			this.children = new HashMap<>();
		}
	}
}