/**
 * KMP（Knuth-Morris-Pratt）Algorithmに基づいて、文字列探索を行います。
 */
public class KMP {

	private KMP() {
		// コンストラクタはプライベート
	}

	/**
	 * 文字列 s に文字列 t が含まれているかを探し、その場所を返します。
	 *
	 * @param s 探索対象
	 * @param t 目的文字列
	 * @return 目的の文字列が存在する始点のインデックス。存在しなければ-1
	 */
	public static int solve(String s, String t) {
		int n = s.length();
		int m = t.length();
		int[] table = new int[n];
		for (int i = 0; i < n; i++) {

		}
		return -1;
	}
}