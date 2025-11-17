import java.util.HashMap;
import java.util.Map;

/**
 * シンプルなTrieデータ構造。
 * Trieは文字列の集合を効率的に管理するデータ構造であり、
 * 単語の挿入、検索、プレフィックス検索などを高速に実行できる。
 */
public final class Trie {
	private final Map<Character, TrieNode> children;

	/**
	 * Trieのルートノードを初期化するコンストラクタ。
	 */
	public Trie() {
		children = new HashMap<>();
	}

	/**
	 * 単語をTrieに挿入する。
	 *
	 * @param word 挿入する単語。nullであってはならない。
	 */
	public void insert(final String word) {
		Map<Character, TrieNode> nodes = children;
		TrieNode node = null;
		for (final char c : word.toCharArray()) {
			node = nodes.computeIfAbsent(c, _ -> new TrieNode());
			node.increment();
			nodes = node.getChildren();
		}
		node.setEnd();
	}

	/**
	 * 単語がTrieに存在するかどうかを検索する。
	 *
	 * @param word 検索する単語。nullであってはならない。
	 * @return 単語が存在すればtrue、存在しなければfalse。
	 */
	public boolean search(final String word) {
		final TrieNode node = findLastNode(word);
		return node != null && node.isEnd();
	}

	/**
	 * 指定したプレフィックスで始まる単語の出現回数を返す。
	 *
	 * @param prefix 検索するプレフィックス。nullであってはならない。
	 * @return プレフィックスに一致する単語の出現回数。
	 */
	public int countPrefix(final String prefix) {
		final TrieNode node = findLastNode(prefix);
		return node != null ? node.frequency() : 0;
	}

	/**
	 * 指定した文字列に対応するTrieNodeを走査して返す。
	 *
	 * @param key 走査する文字列。nullであってはならない。
	 * @return 最後の文字に対応するTrieNode。存在しなければnull。
	 */
	private TrieNode findLastNode(final String key) {
		Map<Character, TrieNode> nodes = children;
		TrieNode node = null;
		for (final char c : key.toCharArray()) {
			node = nodes.get(c);
			if (node == null) return null;
			nodes = node.getChildren();
		}
		return node;
	}

	/**
	 * Trie内の各ノードを表す内部クラス。
	 */
	private static class TrieNode {
		private final Map<Character, TrieNode> children;
		private boolean end;
		private int frequency;

		/**
		 * ノードを生成するコンストラクタ。
		 */
		public TrieNode() {
			this.end = false;
			this.frequency = 0;
			this.children = new HashMap<>();
		}

		/**
		 * 子ノードのマップを返す。
		 *
		 * @return 子ノードのマップ。
		 */
		public Map<Character, TrieNode> getChildren() {
			return children;
		}

		/**
		 * このノードに登録された単語の出現回数を返す。
		 *
		 * @return 出現回数。
		 */
		public int frequency() {
			return frequency;
		}

		/**
		 * このノードが単語の終端であるかを返す。
		 *
		 * @return 終端であればtrue、そうでなければfalse。
		 */
		public boolean isEnd() {
			return end;
		}

		/**
		 * このノードを単語の終端としてマークする。
		 */
		public void setEnd() {
			this.end = true;
		}

		/**
		 * 単語の出現回数をインクリメントする。
		 */
		public void increment() {
			frequency++;
		}
	}
}
