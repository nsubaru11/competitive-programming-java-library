@SuppressWarnings("unused")
public class RadixTrie {
	/* ------------------------ フィールド ------------------------ */
	/**
	 * このノードのラベルとなる文字
	 */
	private final String label;

	/**
	 * インデックス計算の基準となる文字番号
	 */
	private final int baseChar;

	/**
	 * 基準文字からの有効文字数
	 */
	private final int charSize;

	/**
	 * 子ノードの配列
	 */
	private RadixTrie[] childNodes;

	/**
	 * このノードを共有する文字列の総数。（重複あり）
	 */
	private int totalWords;

	/**
	 * このノードを共有する文字列の総数。（重複なし）
	 */
	private int uniqueWordCount;

	/**
	 * このノードを終点とする文字の総数
	 */
	private int terminalCount;

	/* ------------------------ コンストラクタ ------------------------ */

	/**
	 * デフォルトの設定で初期化します。
	 */
	public RadixTrie() {
		this('A', 'z' - 'A' + 1);
	}

	/**
	 * 基準文字と、有効文字数を指定し、初期化します。
	 *
	 * @param baseChar 基準文字
	 * @param charSize 有効文字数
	 */
	public RadixTrie(final int baseChar, final int charSize) {
		this.baseChar = baseChar;
		this.charSize = charSize;
		childNodes = new RadixTrie[charSize];
		uniqueWordCount = totalWords = terminalCount = 0;
		label = null;
	}

	/**
	 * 新しいノードを作成する用の内部コンストラクタ
	 *
	 * @param label このノードの文字
	 */
	private RadixTrie(final String label, final int baseChar, final int charSize) {
		this.label = label;
		this.baseChar = baseChar;
		this.charSize = charSize;
		childNodes = new RadixTrie[charSize];
	}

	/* ------------------------ insertメソッド ------------------------ */

	/**
	 * このTrieに文字列を追加します。
	 *
	 * @param word 追加する文字列
	 * @return 追加した文字の総数
	 */
	public int insert(final String word) {
		if (word == null || word.isBlank()) return 0;
		return insert(word, 0);
	}

	/**
	 * insertメソッドの内部メソッド
	 *
	 * @param word  文字列
	 * @param index 読み込んだ文字の番号
	 * @return 追加した文字の総数
	 */
	private int insert(final String word, final int index) {
		totalWords++;
		char c = word.charAt(index);
		int i = c - baseChar;
		RadixTrie child = childNodes[i];
		if (child == null) {
			child = childNodes[i] = new RadixTrie(word.substring(index), baseChar, charSize);
			child.terminalCount = 1;
			child.uniqueWordCount = 1;
			return 1;
		}
		int cnt = 0;
		String label = child.label;
		while (index + cnt < word.length() && cnt < label.length()) {
			if (label.charAt(cnt) != word.charAt(index + cnt)) {
				break;
			}
			cnt++;
		}
		return 0;
	}

	/* ------------------------ remove系メソッド ------------------------ */

	/**
	 * このTrieに登録されている文字列を削除します。
	 *
	 * @param word 削除する文字列
	 * @return 削除した文字の総数
	 */
	public int remove(final String word) {
		if (word == null || word.isBlank()) return 0;
		return remove(word, 0);
	}

	/**
	 * removeメソッドの内部メソッド
	 *
	 * @param word  文字列
	 * @param index 読み込んだ文字の番号
	 * @return 削除した文字の総数
	 */
	private int remove(final String word, final int index) {
		char c = word.charAt(index);
		int i = c - baseChar;
		RadixTrie child = childNodes[i];
		if (child == null) return 0;
		if (word.length() - 1 == index) {
			int endCount = child.terminalCount;
			if (child.totalWords == endCount) {
				childNodes[i] = null;
			} else {
				child.uniqueWordCount--;
				child.terminalCount = 0;
				child.totalWords -= endCount;
			}
			return endCount;
		}
		int rmCnt = child.remove(word, index + 1);
		if (rmCnt == 0) return 0;
		if (rmCnt == totalWords) childNodes[i] = null;
		uniqueWordCount--;
		totalWords -= rmCnt;
		return rmCnt;
	}

	/**
	 * このTrieに登録されている文字列の接頭辞を削除します。
	 *
	 * @param word 接頭辞
	 * @return 削除した文字の総数
	 */
	public int removePrefix(final String word) {
		if (word == null || word.isBlank()) return 0;
		int[] rmCnt = new int[2];
		removePrefix(word, 0, rmCnt);
		return rmCnt[0];
	}

	/**
	 * removePrefixメソッドの内部メソッド
	 *
	 * @param word  文字列
	 * @param index 読み込んだ文字の番号
	 * @param rmCnt 削除した文字の総数
	 */
	private void removePrefix(final String word, final int index, final int[] rmCnt) {
		char c = word.charAt(index);
		int i = c - baseChar;
		RadixTrie child = childNodes[i];
		if (child == null) return;
		if (word.length() - 1 == index) {
			childNodes[i] = null;
			uniqueWordCount -= child.uniqueWordCount;
			totalWords -= child.totalWords;
			rmCnt[0] = child.uniqueWordCount;
			rmCnt[1] = child.totalWords;
			return;
		}
		child.removePrefix(word, index + 1, rmCnt);
		if (rmCnt[1] == 0) return;
		if (rmCnt[1] == totalWords) {
			childNodes[i] = null;
		}
		uniqueWordCount -= rmCnt[0];
		totalWords -= rmCnt[1];
	}

	/* ------------------------ 検索系メソッド ------------------------ */

	/**
	 * このTrieに指定の文字列が登録されているか判定します。
	 *
	 * @param word 判定する文字列
	 * @return 登録されているなら true, そうでなければ false
	 */
	public boolean contains(final String word) {
		if (word == null || word.isBlank()) return false;
		int index = 0;
		int len = word.length();
		RadixTrie child = this;
		while (index < len) {
			char c = word.charAt(index);
			int i = c - baseChar;
			if (child.childNodes[i] == null) return false;
			child = child.childNodes[i];
			index++;
		}
		return child.terminalCount > 0;
	}

	/**
	 * このTrieに指定の文字列の接頭辞が登録されているか判定します。
	 *
	 * @return 登録されているなら true, そうでなければ false
	 */
	public boolean hasPrefix(final String word) {
		if (word == null || word.isBlank()) return false;
		int index = 0;
		int len = word.length();
		RadixTrie child = this;
		while (index < len) {
			char c = word.charAt(index);
			int i = c - baseChar;
			if (child.childNodes[i] == null) return false;
			child = child.childNodes[i];
			if (child.terminalCount > 0) return true;
			index++;
		}
		return false;
	}

	/* ------------------------ 指定文字数取得系メソッド ------------------------ */

	/**
	 * このTrieに登録されている指定の文字列の数を取得します。
	 *
	 * @param word 文字列
	 * @return 登録されている文字列の数
	 */
	public int count(final String word) {
		if (word == null || word.isBlank()) return 0;
		RadixTrie trie = findLastNode(word);
		return trie == null ? 0 : trie.terminalCount;
	}

	/**
	 * このTrieに登録されている指定の接頭辞から始まる文字列の数を取得します。
	 *
	 * @param prefix 接頭辞
	 * @return 接頭辞の数
	 */
	public int countPrefix(final String prefix) {
		if (prefix == null || prefix.isBlank()) return 0;
		RadixTrie trie = findLastNode(prefix);
		return trie == null ? 0 : trie.uniqueWordCount;
	}

	private RadixTrie findLastNode(final String word) {
		int index = 0;
		int len = word.length();
		RadixTrie trie = this;
		while (index < len) {
			char c = word.charAt(index);
			int i = c - baseChar;
			if (trie.childNodes[i] == null) return null;
			trie = trie.childNodes[i];
			index++;
		}
		return trie;
	}

	/**
	 * このTrieに登録されている文字列の数を取得します。
	 *
	 * @return 登録されている文字列の数
	 */
	public int size() {
		return uniqueWordCount;
	}

	/**
	 * このTrieに追加した文字列の総数を取得します。
	 *
	 * @return 追加した文字列の総数
	 */
	public int totalWords() {
		return totalWords;
	}

	/**
	 * このTrieをクリアします。
	 */
	public void clear() {
		uniqueWordCount = totalWords = 0;
		childNodes = new RadixTrie[charSize];
	}
}
