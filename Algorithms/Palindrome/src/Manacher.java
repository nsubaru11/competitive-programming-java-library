import static java.lang.Math.max;

/**
 * Manacherアルゴリズムに基づいて文字列内の回文情報を計算するクラス。
 * 主に最長回文部分文字列の取得や、任意区間が回文かどうかの判定に利用される。
 */
@SuppressWarnings("unused")
public class Manacher {
	private final int[] radii;
	private final int len;
	private final char[] chars;
	private int maxOddLength, maxEvenLength;

	/**
	 * 指定された文字列に対してManacherアルゴリズムを実行し、回文情報を初期化します。
	 *
	 * @param str 対象となる文字列
	 */
	Manacher(String str) {
		this(str.toCharArray());
	}

	/**
	 * 指定された文字配列に対してManacherアルゴリズムを実行し、回文情報を初期化します。
	 *
	 * @param chr 対象となる文字配列
	 */
	Manacher(char[] chr) {
		chars = chr;
		len = (chr.length << 1) - 1;
		radii = new int[len];
		radii[0] = radii[len - 1] = 1;
		computePalindromicRadii();
	}

	/**
	 * Manacherアルゴリズムに基づき、各仮想中心における回文半径をO(n)で計算します。
	 * 各中心i (0 <= i < 2n - 1) に対して、回文区間[l, r)の長さ (r - l) を radii[i] として保存します。
	 */
	private void computePalindromicRadii() {
		int clen = chars.length;
		for (int pos = 1, radius = 0; pos < len - 1; ) {
			int l = (pos - radius >> 1);
			int r = (pos + radius >> 1) + 1;
			while (0 <= l && r < clen && chars[l--] == chars[r++]) {
				radius += 2;
			}
			radii[pos] = radius;
			if ((radius & 1) == 0) {
				maxEvenLength = max(radius, maxEvenLength);
			} else {
				maxOddLength = max(radius, maxOddLength);
			}
			int k = 1;
			while (k <= pos && k + radii[pos - k] < radius) {
				radii[pos + k] = radii[pos - k];
				k++;
			}
			radius = max(radius - k, pos & 1);
			pos += k;
		}
	}

	public int[] getRadii() {
		return radii;
	}

	/**
	 * 指定した区間 [l, r) が回文かどうかを判定します。
	 *
	 * @param l 区間の開始インデックス（含む）
	 * @param r 区間の終了インデックス（含まない）
	 * @return 区間が回文であれば true、そうでなければ false
	 */
	public boolean isPalindromeRange(int l, int r) {
		return (radii[l + r - 1] + r - l & 1) == 0 && radii[l + r - 1] >= r - l;
	}

	/**
	 * 最長回文の長さを返します。
	 *
	 * @return 最長回文の長さ
	 */
	public int getLongestLen() {
		return max(maxEvenLength, maxOddLength);
	}

	/**
	 * 最長の奇数長の回文の長さを返します。
	 *
	 * @return 最長回文の長さ
	 */
	public int getOddLongestLen() {
		return maxOddLength;
	}

	/**
	 * 最長の偶数長の回文の長さを返します。
	 *
	 * @return 最長回文の長さ
	 */
	public int getEvenLongestLen() {
		return maxEvenLength;
	}

	/**
	 * 指定した文字位置を中心とする奇数長の回文列の長さを返します。
	 *
	 * @param center 元の文字列上の中心インデックス
	 * @return 該当する奇数長回文の長さ
	 */
	public int getOddPalindromeLengthAt(int center) {
		return radii[center << 1];
	}

	/**
	 * 指定した文字位置の直後を中心とする偶数長の回文列の長さを返します。
	 *
	 * @param center 元の文字列上の中心インデックス
	 * @return 該当する偶数長回文の長さ
	 */
	public int getEvenPalindromeLengthAt(int center) {
		return radii[(center << 1) + 1];
	}
}

