package lib.ds.arrays;

/**
 * 座標圧縮で同じ値を同順位として扱う方式。
 * 以下の例は先頭の順位が1（{@code oneBased = true}）の場合で、先頭が0のときはすべて1小さくなる。
 */
public enum RankType {
	/**
	 * 1, 2, 2, 3形式。
	 */
	DENSE,
	/**
	 * 1, 2, 2, 4形式。
	 */
	COMPETITION,
	/**
	 * 1, 3, 3, 4形式。
	 */
	MODIFIED_COMPETITION
}
