import java.util.HashMap;
import java.util.Map;

/**
 * シンプルなTrieデータ構造。
 * Trieは文字列の集合を効率的に管理するデータ構造であり、
 * 単語の挿入、検索、プレフィックス検索などを高速に実行できる。
 */
public class PatriciaTrie {
	private final Map<Character, TrieNode> children;

	/**
	 * Trieのルートノードを初期化するコンストラクタ。
	 */
	public PatriciaTrie() {
		children = new HashMap<>();
	}

	/**
	 * 単語をTrieに挿入する。
	 *
	 * @param word 挿入する単語。nullであってはならない。
	 * @throws IllegalArgumentException 単語がnullの場合にスローされる。
	 */
	public void insert(String word) {
		if (word == null || word.isEmpty())
			throw new IllegalArgumentException("word cannot be null");
		Map<Character, TrieNode> nodes = children;
		TrieNode node = null;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			node = nodes.get(c);
			if (node != null) {
				String label = node.label;
				int pos = 1;
				for (; pos < label.length() || pos + i < word.length(); pos++) {
					if (label.charAt(pos) != word.charAt(i + pos)) {
						break;
					}
				}
				/*
				一回保留いつかまた逢う日まで
				https://rightcode.co.jp/blogs/7192
				https://ja.wikipedia.org/wiki/%E3%83%88%E3%83%A9%E3%82%A4_(%E3%83%87%E3%83%BC%E3%82%BF%E6%A7%8B%E9%80%A0)
				https://ja.wikipedia.org/wiki/%E4%B8%89%E5%88%86%E6%8E%A2%E7%B4%A2%E6%9C%A8
				https://qiita.com/butsurizuki/items/7c1dd4916b9495beacea
				https://algo-logic.info/trie-tree/
				*/
				// このノードは共通部分を保存
				node.increment();
				node.setLabel(c + "");
				TrieNode newNode = new TrieNode();
				newNode.setLabel(label.substring(1));
				newNode.setChildren(node.getChildren());
				nodes = node.getChildren();

				// 次のノードにlabelの続き
				nodes.put(label.charAt(1), new TrieNode());
				node = nodes.get(label.charAt(1));
				node.setLabel(label.substring(1));
				node.increment();
				node.setEnd();

				// 次のノードにwordの続き
				nodes.put(word.charAt(i + 1), new TrieNode());
				node = nodes.get(word.charAt(i + 1));
				node.setLabel(word.substring(i + 1));
				node.increment();
				node.setEnd();

				break;
			} else {
				node = new TrieNode();
				node.label = word.substring(i);
				nodes.put(c, node);
				node.increment();
				node.setEnd();
				break;
			}
		}
	}

	/**
	 * 単語がTrieに存在するかどうかを検索する。
	 *
	 * @param word 検索する単語。nullであってはならない。
	 * @return 単語が存在すればtrue、存在しなければfalse。
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
	 * @return プレフィックスに一致する単語の出現回数。
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
	 * @param key 走査する文字列。nullであってはならない。
	 * @return 最後の文字に対応するTrieNode。存在しなければnull。
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
	 * Trie内の各ノードを表す内部クラス。
	 */
	private static class TrieNode {
		private Map<Character, TrieNode> children;
		private boolean end;
		private String label;
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
		 * 子ノードのマップを更新する。
		 */
		public void setChildren(Map<Character, TrieNode> children) {
			this.children = children;
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
		public void setEnd(boolean isEnd) {
			end = isEnd;
		}

		/**
		 * このノードを単語の終端としてマークする。
		 */
		public void setEnd() {
			this.end = true;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String label() {
			return label;
		}

		/**
		 * 単語の出現回数をインクリメントする。
		 */
		public void increment() {
			frequency++;
		}
	}
}
