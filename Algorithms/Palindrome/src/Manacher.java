import java.nio.CharBuffer;

import static java.lang.Math.max;

/**
 * Manacher'sアルゴリズムに基づいて文字列内の回文情報を計算するクラス。
 * 主に最長回文部分文字列の取得や、任意区間が回文かどうかの判定に利用する。
 * 時間計算量O(n)で全ての回文を検出可能。
 */
@SuppressWarnings("unused")
public final class Manacher {
	private final int[] palindromeLengths;
	private final char[] chars;
	private int maxOddLen, maxEvenLen;
	private int maxOddCtr, maxEvenCtr;

	/**
	 * 指定された文字列に対してManacher'sアルゴリズムを実行し、回文情報を初期化します。
	 *
	 * @param s 対象となる文字列
	 */
	public Manacher(String s) {
		this(s.toCharArray());
	}

	/**
	 * 指定された文字配列に対してManacherアルゴリズムを実行し、回文情報を初期化します。
	 *
	 * @param chr 対象となる文字配列
	 */
	public Manacher(char[] chr) {
		chars = chr;
		int len = (chr.length << 1) - 1;
		palindromeLengths = new int[len];
		palindromeLengths[0] = palindromeLengths[len - 1] = 1;
		computePalindromicLengths();
	}

	/**
	 * Manacher'sアルゴリズムに基づき、各仮想中心における回文長さをO(n)で計算します。
	 * 各中心i (0 <= i < 2n - 1) に対して、回文区間[l, r)の長さ (r - l) を palindromeLengths[i] として保存します。
	 */
	private void computePalindromicLengths() {
		final char[] chars = this.chars;
		final int clen = chars.length;
		final int len = (clen << 1) - 1;

		for (int pos = 1, length = 0; pos < len - 1; ) {
			int l = (pos - length >> 1);
			int r = (pos + length >> 1) + 1;

			while (0 <= l && r < clen && chars[l--] == chars[r++]) {
				length += 2;
			}

			palindromeLengths[pos] = length;

			if ((length & 1) == 0) {
				if (length > maxEvenLen) {
					maxEvenLen = length;
					maxEvenCtr = pos;
				}
			} else {
				if (length > maxOddLen) {
					maxOddLen = length;
					maxOddCtr = pos;
				}
			}

			int k = 1;
			while (k <= pos && k + palindromeLengths[pos - k] < length) {
				palindromeLengths[pos + k] = palindromeLengths[pos - k];
				k++;
			}

			length = Math.max(length - k, pos & 1);
			pos += k;
		}
	}

	/**
	 * 内部で計算された回文長さの配列を返します。
	 *
	 * @return 各仮想中心における回文長さの配列
	 */
	public int[] getPalindromeLengths() {
		return palindromeLengths;
	}

	/**
	 * 指定した区間 [l, r) が回文かどうかを判定します。
	 *
	 * @param l 区間の開始インデックス（含む）
	 * @param r 区間の終了インデックス（含まない）
	 * @return 区間が回文であれば true、そうでなければ false
	 */
	public boolean isPalindromeRange(int l, int r) {
		if (l < 0 || r > chars.length || l >= r) return false;
		return ((palindromeLengths[l + r - 1] + r - l) & 1) == 0 && palindromeLengths[l + r - 1] >= r - l;
	}

	/**
	 * 最長回文の長さを返します。
	 *
	 * @return 最長回文の長さ
	 */
	public int getLongestLen() {
		return max(maxEvenLen, maxOddLen);
	}

	/**
	 * 最長の奇数長の回文の長さを返します。
	 *
	 * @return 最長回文の長さ
	 */
	public int getOddLongestLen() {
		return maxOddLen;
	}

	/**
	 * 最長の偶数長の回文の長さを返します。
	 *
	 * @return 最長回文の長さ
	 */
	public int getEvenLongestLen() {
		return maxEvenLen;
	}

	/**
	 * 最長回文部分文字列を返します。
	 *
	 * @return 最長回文部分文字列
	 */
	public String getLongestPalindrome() {
		if (maxOddLen >= maxEvenLen) {
			return getLongestOddPalindrome();
		} else {
			return getLongestEvenPalindrome();
		}
	}

	/**
	 * 最長の奇数長の回文部分文字列を返します。
	 *
	 * @return 最長の奇数長の回文部分文字列
	 */
	public String getLongestOddPalindrome() {
		int ctr = maxOddCtr >> 1;
		int len = maxOddLen;
		int from = ctr - (len >> 1);
		return slice(from, from + len);
	}

	/**
	 * 最長の偶数長の回文部分文字列を返します。
	 *
	 * @return 最長の偶数長の回文部分文字列
	 */
	public String getLongestEvenPalindrome() {
		int ctr = maxEvenCtr >> 1;
		int len = maxEvenLen;
		int from = ctr - ((len - 1) >> 1);
		return slice(from, from + len);
	}

	/**
	 * 指定した文字位置を中心とする奇数長の回文列の長さを返します。
	 *
	 * @param center 元の文字列上の中心インデックス
	 * @return 該当する奇数長回文の長さ
	 * @throws IllegalArgumentException インデックスが範囲外の場合
	 */
	public int getOddPalindromeLengthAt(int center) {
		if (center < 0 || center >= chars.length) {
			throw new IllegalArgumentException("Center index out of bounds: " + center);
		}
		return palindromeLengths[center << 1];
	}

	/**
	 * 指定した文字位置の直後を中心とする偶数長の回文列の長さを返します。
	 *
	 * @param center 元の文字列上の中心インデックス
	 * @return 該当する偶数長回文の長さ
	 * @throws IllegalArgumentException インデックスが範囲外の場合
	 */
	public int getEvenPalindromeLengthAt(int center) {
		if (center < 0 || center >= chars.length - 1) {
			throw new IllegalArgumentException("Center index out of bounds: " + center);
		}
		return palindromeLengths[(center << 1) + 1];
	}

	/**
	 * 指定した文字位置を中心とする奇数長の回文部分文字列を返します。
	 *
	 * @param center 元の文字列上の中心インデックス
	 * @return 該当する奇数長回文部分文字列
	 * @throws IllegalArgumentException インデックスが範囲外の場合
	 */
	public String getOddPalindromeAt(int center) {
		if (center < 0 || center >= chars.length) {
			throw new IllegalArgumentException("Center index out of bounds: " + center);
		}
		int len = getOddPalindromeLengthAt(center);
		int start = center - (len >> 1);
		return slice(start, start + len);
	}

	/**
	 * 指定した文字位置の直後を中心とする偶数長の回文部分文字列を返します。
	 *
	 * @param center 元の文字列上の中心インデックス
	 * @return 該当する偶数長回文部分文字列
	 * @throws IllegalArgumentException インデックスが範囲外の場合
	 */
	public String getEvenPalindromeAt(int center) {
		if (center < 0 || center >= chars.length - 1) {
			throw new IllegalArgumentException("Center index out of bounds: " + center);
		}
		int length = getEvenPalindromeLengthAt(center);
		int start = center - ((length - 1) >> 1);
		return slice(start, start + length);
	}


	/**
	 * 文字配列の指定範囲を文字列として効率的に取得します。
	 *
	 * @param l 開始インデックス（含む）
	 * @param r 終了インデックス（含まない）
	 * @return 指定範囲の文字列
	 */
	private String slice(int l, int r) {
		return CharBuffer.wrap(chars, l, r - l).toString();
	}
}
