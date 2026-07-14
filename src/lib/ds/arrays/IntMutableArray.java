package lib.ds.arrays;

/**
 * int値を格納する可変配列の共通インターフェースです。
 */
public interface IntMutableArray extends IntArray {

	/**
	 * 指定位置を更新し、更新前の値を返します。
	 */
	int set(final int i, final int v);
}
