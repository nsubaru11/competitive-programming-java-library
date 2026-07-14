package lib.ds.arrays;

/**
 * long値を格納する可変配列の共通インターフェースです。
 */
public interface LongMutableArray extends LongArray {

	/**
	 * 指定位置を更新し、更新前の値を返します。
	 */
	long set(final int i, final long v);
}
