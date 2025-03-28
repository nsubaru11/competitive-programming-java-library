import java.util.HashMap;
import java.util.Map;

/**
 * シンプルなTrieデータ構造。
 */
public class Trie {
	private final Map<Character, TrieNode> children;

	/**
	 * デフォルトコンストラクタ。
	 */
	public Trie() {
		children = new HashMap<>();
	}

	/**
	 * 単語をTrieに挿入する。
	 *
	 * @param word 挿入する単語。nullであってはならない。
	 * @throws IllegalArgumentException 単語がnullの場合にスローされる。
	 */
	public void insert(String word) {
		if (word == null)
			throw new IllegalArgumentException("word cannot be null");
		Map<Character, TrieNode> nodes = children;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			boolean isEnd = (i == word.length() - 1);
			TrieNode node = getOrCreateChild(nodes, c, isEnd);
			nodes = node.getChildren();
		}
	}

	/**
	 * 単語がTrieに存在するかどうかを検索する。
	 *
	 * @param word 検索する単語。nullであってはならない。
	 * @return 単語が存在すればtrue、存在しなければfalse
	 * @throws IllegalArgumentException 単語がnullの場合にスローされる。
	 */
	public boolean search(String word) {
		if (word == null)
			throw new IllegalArgumentException("word cannot be null");
		TrieNode node = findLastNode(word);
		return node != null && node.isEnd();
	}

	/**
	 * 指定したプレフィックスで始まる単語の出現回数を返す。
	 *
	 * @param prefix 検索するプレフィックス。nullであってはならない。
	 * @return プレフィックスに一致する単語の出現回数
	 * @throws IllegalArgumentException プレフィックスがnullの場合にスローされる。
	 */
	public int countPrefix(String prefix) {
		if (prefix == null)
			throw new IllegalArgumentException("prefix cannot be null");
		TrieNode node = findLastNode(prefix);
		return node != null ? node.frequency() : 0;
	}

	/**
	 * 指定した文字列に対応するTrieNodeを走査して返す。
	 *
	 * @param key 走査する文字列 (nullであってはならない)
	 * @return 最後の文字に対応するTrieNode、存在しなければnull
	 */
	private TrieNode findLastNode(String key) {
		Map<Character, TrieNode> nodes = children;
		TrieNode node = null;
		for (int i = 0; i < key.length(); i++) {
			char c = key.charAt(i);
			node = nodes.get(c);
			if (node == null)
				return null;
			nodes = node.getChildren();
		}
		return node;
	}

	/**
	 * 指定したキーの子ノードを取得する。存在しなければ新規作成し、存在する場合は終端フラグを更新する。
	 *
	 * @param nodes 子ノードのマップ
	 * @param key   子ノードのキー
	 * @param isEnd 終端フラグ
	 * @return 対応する子ノード
	 */
	private TrieNode getOrCreateChild(Map<Character, TrieNode> nodes, char key, boolean isEnd) {
		TrieNode node = nodes.get(key);
		if (node == null) {
			node = new TrieNode(isEnd);
			nodes.put(key, node);
		} else {
			node.increment(isEnd);
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
		 *
		 * @param end このノードが単語の終端であるか
		 */
		public TrieNode(boolean end) {
			this.end = end;
			frequency = 1;
			children = new HashMap<>();
		}

		/**
		 * 子ノードのマップを返す。
		 *
		 * @return 子ノードのマップ
		 */
		public Map<Character, TrieNode> getChildren() {
			return children;
		}

		/**
		 * このノードに登録された単語の出現回数を返す。
		 *
		 * @return 出現回数
		 */
		public int frequency() {
			return frequency;
		}

		/**
		 * このノードが単語の終端であるかを返す。
		 *
		 * @return 終端であればtrue、そうでなければfalse
		 */
		public boolean isEnd() {
			return end;
		}

		/**
		 * 終端フラグを更新する。
		 *
		 * @param isEnd この単語が終端かどうか
		 */
		public void increment(boolean isEnd) {
			frequency++;
			this.end |= isEnd;
		}
	}
}
