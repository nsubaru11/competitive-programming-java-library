package lib.util;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.function.*;

/**
 * Mo's Algorithm。静的な区間クエリをオフラインで処理します。
 *
 * <p>クエリは {@code lr[0][i] = l}, {@code lr[1][i] = r} と表す 0-indexed の半開区間
 * {@code [l, r)} です。各クエリの答えは {@code query} に渡される登録順の番号を使って、
 * 呼び出し側で保存します。{@code n < 2^20} かつクエリ数が {@code 2^20} 以下であることを
 * 前提とします。</p>
 */
@SuppressWarnings("unused")
public final class MoAlgorithm {
	private static final int IDX_BITS = 20;
	private static final long IDX_MASK = (1L << IDX_BITS) - 1;

	private MoAlgorithm() {
	}

	/**
	 * 4 方向の区間伸縮処理を使って Mo's Algorithm を実行します。
	 *
	 * @param n           列の長さ
	 * @param lr          {@code lr[0][i] = l}, {@code lr[1][i] = r} であるクエリ群。各区間は {@code [l, r)}
	 * @param addLeft     左端を 1 減らして要素を追加する処理
	 * @param addRight    右端を 1 増やして要素を追加する処理
	 * @param removeLeft  左端を 1 増やして要素を削除する処理
	 * @param removeRight 右端を 1 減らして要素を削除する処理
	 * @param query       区間を作った後にクエリ番号を受け取る処理
	 */
	public static void run(final int n, final int[][] lr, final IntConsumer addLeft, final IntConsumer addRight, final IntConsumer removeLeft, final IntConsumer removeRight, final IntConsumer query) {
		final int[] left = lr[0], right = lr[1];
		final int q = left.length;
		if (q == 0) return;
		final int block = max(1, (int) (n / sqrt(q * 2.0 / 3.0)));
		runPacked(n, left, right, block, addLeft, addRight, removeLeft, removeRight, query);
	}

	/**
	 * 左右で共通の追加・削除処理を使って Mo's Algorithm を実行します。
	 *
	 * @param n      列の長さ
	 * @param lr     {@code lr[0][i] = l}, {@code lr[1][i] = r} であるクエリ群。各区間は {@code [l, r)}
	 * @param add    要素を区間へ追加する処理
	 * @param remove 要素を区間から削除する処理
	 * @param query  区間を作った後にクエリ番号を受け取る処理
	 */
	public static void run(final int n, final int[][] lr, final IntConsumer add, final IntConsumer remove, final IntConsumer query) {
		run(n, lr, add, add, remove, remove, query);
	}

	private static void runPacked(final int n, final int[] left, final int[] right, final int block, final IntConsumer addLeft, final IntConsumer addRight, final IntConsumer removeLeft, final IntConsumer removeRight, final IntConsumer query) {
		final int q = left.length;
		final long[] order = new long[q];
		for (int i = 0; i < q; i++) {
			int lb = left[i] / block, r = right[i];
			if ((lb & 1) == 1) r = n - r;
			order[i] = ((long) lb << (IDX_BITS << 1)) | ((long) r << IDX_BITS) | i;
		}
		sort(order);
		int curL = 0, curR = 0;
		for (final long entry : order) {
			final int id = (int) (entry & IDX_MASK);
			final int ql = left[id], qr = right[id];
			while (curL > ql) addLeft.accept(--curL);
			while (curR < qr) addRight.accept(curR++);
			while (curL < ql) removeLeft.accept(curL++);
			while (curR > qr) removeRight.accept(--curR);
			query.accept(id);
		}
	}
}
