import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.function.*;

/**
 * Mo's Algorithm。区間クエリをオフラインで平方分割により O((N+Q) sqrt(N)) で処理する。
 *
 * <p>各クエリは閉区間 [l, r]（0-indexed）として与える。{@code add}/{@code remove} は
 * それぞれ要素 {@code idx}（0-indexed）を現在の区間に追加・削除する際の処理であり、
 * {@code answer} は現在の区間状態に対する回答を取得する処理である。</p>
 */
@SuppressWarnings("unused")
public final class MoAlgorithm {

	private static final int IDX_BITS = 20;
	private static final long IDX_MASK = (1L << IDX_BITS) - 1;

	private MoAlgorithm() {
	}

	/**
	 * Mo's Algorithm を実行し、各クエリに対する回答（long）を求める。
	 *
	 * @param n      列の長さ
	 * @param queries     各クエリ（{queries[i][0], queries[i][1]} = 閉区間 [l, r]、0-indexed）。
	 *               {@code FastScanner} 等で {@code l1 r1 / l2 r2 / ... / lq rq} の形式を
	 *               一括で読み込んだ結果をそのまま渡せる
	 * @param add    要素 idx を区間に追加する処理
	 * @param remove 要素 idx を区間から削除する処理
	 * @param answer 現在の区間に対する回答を取得する処理
	 * @return 各クエリ（[l, r] と同じ順序）に対応する回答
	 */
	public static long[] solve(final int n, final int[][] queries, final IntConsumer add, final IntConsumer remove, final LongSupplier answer) {
		final int q = queries.length;
		final long[] ans = new long[q];
		final int block = max(1, (int) (n / sqrt(q)));
		final long[] order = new long[q];
		for (int i = 0; i < q; i++) {
			final int l = queries[i][0];
			int r = queries[i][1];
			final int lb = l / block;
			if ((lb & 1) == 1) r = n - r;
			order[i] = ((long) lb << (IDX_BITS << 1)) | ((long) r << IDX_BITS) | i;
		}
		sort(order);
		int curL = 0, curR = -1;
		for (final long id : order) {
			final int idx = (int) (id & IDX_MASK);
			final int ql = queries[idx][0], qr = queries[idx][1];
			while (curR < qr) add.accept(++curR);
			while (curL > ql) add.accept(--curL);
			while (curR > qr) remove.accept(curR--);
			while (curL < ql) remove.accept(curL++);
			ans[idx] = answer.getAsLong();
		}
		return ans;
	}

	/**
	 * 回答が long 以外の型の場合のオーバーロード。
	 *
	 * @param n         列の長さ
	 * @param lr        各クエリ（{lr[i][0], lr[i][1]} = 閉区間 [l, r]、0-indexed）
	 * @param add       要素 idx を区間に追加する処理
	 * @param remove    要素 idx を区間から削除する処理
	 * @param answer    現在の区間に対する回答を取得する処理
	 * @param generator 結果配列を生成するファクトリ（例: {@code Long[]::new}）
	 * @return 各クエリ（lr と同じ順序）に対応する回答
	 */
	public static <T> T[] solve(final int n, final int[][] lr, final IntConsumer add, final IntConsumer remove, final Supplier<T> answer, final IntFunction<T[]> generator) {
		final int q = lr.length;
		final T[] ans = generator.apply(q);
		final int block = max(1, (int) (n / sqrt(q)));
		final long[] order = new long[q];
		for (int i = 0; i < q; i++) {
			final int l = lr[i][0];
			int r = lr[i][1];
			final int lb = l / block;
			if ((lb & 1) == 1) r = n - r;
			order[i] = ((long) lb << (IDX_BITS << 1)) | ((long) r << IDX_BITS) | i;
		}
		sort(order);
		int curL = 0, curR = -1;
		for (final long id : order) {
			final int idx = (int) (id & IDX_MASK);
			final int ql = lr[idx][0], qr = lr[idx][1];
			while (curR < qr) add.accept(++curR);
			while (curL > ql) add.accept(--curL);
			while (curR > qr) remove.accept(curR--);
			while (curL < ql) remove.accept(curL++);
			ans[idx] = answer.get();
		}
		return ans;
	}

}
