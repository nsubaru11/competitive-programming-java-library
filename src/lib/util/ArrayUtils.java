package lib.util;

import java.util.*;

import lib.ds.arrays.*;

/**
 * 配列に対する代表的なアルゴリズムを提供します。
 */
@SuppressWarnings("unused")
public final class ArrayUtils {
	private ArrayUtils() {
	}

	// region < basic >

	/**
	 * 配列の総和を返します。
	 */
	public static long sum(final int[] a) {
		long sum = 0;
		for (final int v : a) sum += v;
		return sum;
	}

	/**
	 * 配列の総和を返します。
	 */
	public static long sum(final long[] a) {
		long sum = 0;
		for (final long v : a) sum += v;
		return sum;
	}

	/**
	 * 配列の総和を返します。
	 */
	public static long sum(final IntArray a) {
		long sum = 0;
		for (int i = 0; i < a.size(); i++) sum += a.get(i);
		return sum;
	}

	/**
	 * 配列の総和を返します。
	 */
	public static long sum(final LongArray a) {
		long sum = 0;
		for (int i = 0; i < a.size(); i++) sum += a.get(i);
		return sum;
	}

	/**
	 * 配列の最小値を返します。
	 */
	public static int min(final int[] a) {
		int min = a[0];
		for (int i = 1; i < a.length; i++) if (a[i] < min) min = a[i];
		return min;
	}

	/**
	 * 配列の最小値を返します。
	 */
	public static long min(final long[] a) {
		long min = a[0];
		for (int i = 1; i < a.length; i++) if (a[i] < min) min = a[i];
		return min;
	}

	/**
	 * 配列の最小値を返します。
	 */
	public static int min(final IntArray a) {
		int min = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final int v = a.get(i);
			if (v < min) min = v;
		}
		return min;
	}

	/**
	 * 配列の最小値を返します。
	 */
	public static long min(final LongArray a) {
		long min = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final long v = a.get(i);
			if (v < min) min = v;
		}
		return min;
	}

	/**
	 * 配列の最大値を返します。
	 */
	public static int max(final int[] a) {
		int max = a[0];
		for (int i = 1; i < a.length; i++) if (a[i] > max) max = a[i];
		return max;
	}

	/**
	 * 配列の最大値を返します。
	 */
	public static long max(final long[] a) {
		long max = a[0];
		for (int i = 1; i < a.length; i++) if (a[i] > max) max = a[i];
		return max;
	}

	/**
	 * 配列の最大値を返します。
	 */
	public static int max(final IntArray a) {
		int max = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final int v = a.get(i);
			if (v > max) max = v;
		}
		return max;
	}

	/**
	 * 配列の最大値を返します。
	 */
	public static long max(final LongArray a) {
		long max = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final long v = a.get(i);
			if (v > max) max = v;
		}
		return max;
	}

	/**
	 * 最小値が最初に現れる添字を返します。
	 */
	public static int argMin(final int[] a) {
		int idx = 0;
		for (int i = 1; i < a.length; i++) if (a[i] < a[idx]) idx = i;
		return idx;
	}

	/**
	 * 最小値が最初に現れる添字を返します。
	 */
	public static int argMin(final long[] a) {
		int idx = 0;
		for (int i = 1; i < a.length; i++) if (a[i] < a[idx]) idx = i;
		return idx;
	}

	/**
	 * 最小値が最初に現れる添字を返します。
	 */
	public static int argMin(final IntArray a) {
		int idx = 0, min = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final int v = a.get(i);
			if (v < min) {
				min = v;
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * 最小値が最初に現れる添字を返します。
	 */
	public static int argMin(final LongArray a) {
		int idx = 0;
		long min = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final long v = a.get(i);
			if (v < min) {
				min = v;
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * 最大値が最初に現れる添字を返します。
	 */
	public static int argMax(final int[] a) {
		int idx = 0;
		for (int i = 1; i < a.length; i++) if (a[i] > a[idx]) idx = i;
		return idx;
	}

	/**
	 * 最大値が最初に現れる添字を返します。
	 */
	public static int argMax(final long[] a) {
		int idx = 0;
		for (int i = 1; i < a.length; i++) if (a[i] > a[idx]) idx = i;
		return idx;
	}

	/**
	 * 最大値が最初に現れる添字を返します。
	 */
	public static int argMax(final IntArray a) {
		int idx = 0, max = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final int v = a.get(i);
			if (v > max) {
				max = v;
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * 最大値が最初に現れる添字を返します。
	 */
	public static int argMax(final LongArray a) {
		int idx = 0;
		long max = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			final long v = a.get(i);
			if (v > max) {
				max = v;
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * 指定値の出現回数を返します。
	 */
	public static int count(final int[] a, final int t) {
		int cnt = 0;
		for (final int v : a) if (v == t) cnt++;
		return cnt;
	}

	/**
	 * 指定値の出現回数を返します。
	 */
	public static int count(final long[] a, final long t) {
		int cnt = 0;
		for (final long v : a) if (v == t) cnt++;
		return cnt;
	}

	/**
	 * 指定値の出現回数を返します。
	 */
	public static int count(final IntArray a, final int t) {
		int cnt = 0;
		for (int i = 0; i < a.size(); i++) if (a.get(i) == t) cnt++;
		return cnt;
	}

	/**
	 * 指定値の出現回数を返します。
	 */
	public static int count(final LongArray a, final long t) {
		int cnt = 0;
		for (int i = 0; i < a.size(); i++) if (a.get(i) == t) cnt++;
		return cnt;
	}

	/**
	 * 指定値が最初に現れる添字を返します。
	 */
	public static int indexOf(final int[] a, final int t) {
		for (int i = 0; i < a.length; i++) if (a[i] == t) return i;
		return -1;
	}

	/**
	 * 指定値が最初に現れる添字を返します。
	 */
	public static int indexOf(final long[] a, final long t) {
		for (int i = 0; i < a.length; i++) if (a[i] == t) return i;
		return -1;
	}

	/**
	 * 指定値が最初に現れる添字を返します。
	 */
	public static int indexOf(final IntArray a, final int t) {
		for (int i = 0; i < a.size(); i++) if (a.get(i) == t) return i;
		return -1;
	}

	/**
	 * 指定値が最初に現れる添字を返します。
	 */
	public static int indexOf(final LongArray a, final long t) {
		for (int i = 0; i < a.size(); i++) if (a.get(i) == t) return i;
		return -1;
	}

	/**
	 * 指定値が最後に現れる添字を返します。
	 */
	public static int lastIndexOf(final int[] a, final int t) {
		for (int i = a.length - 1; i >= 0; i--) if (a[i] == t) return i;
		return -1;
	}

	/**
	 * 指定値が最後に現れる添字を返します。
	 */
	public static int lastIndexOf(final long[] a, final long t) {
		for (int i = a.length - 1; i >= 0; i--) if (a[i] == t) return i;
		return -1;
	}

	/**
	 * 指定値が最後に現れる添字を返します。
	 */
	public static int lastIndexOf(final IntArray a, final int t) {
		for (int i = a.size() - 1; i >= 0; i--) if (a.get(i) == t) return i;
		return -1;
	}

	/**
	 * 指定値が最後に現れる添字を返します。
	 */
	public static int lastIndexOf(final LongArray a, final long t) {
		for (int i = a.size() - 1; i >= 0; i--) if (a.get(i) == t) return i;
		return -1;
	}

	/**
	 * 配列を反転します。
	 */
	public static void reverse(final int[] a) {
		reverse(a, 0, a.length);
	}

	/**
	 * 配列の指定範囲を反転します。
	 */
	public static void reverse(final int[] a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swap(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列を反転します。
	 */
	public static void reverse(final long[] a) {
		reverse(a, 0, a.length);
	}

	/**
	 * 配列の指定範囲を反転します。
	 */
	public static void reverse(final long[] a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swap(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列を反転します。
	 */
	public static void reverse(final char[] a) {
		reverse(a, 0, a.length);
	}

	/**
	 * 配列の指定範囲を反転します。
	 */
	public static void reverse(final char[] a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swap(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列の行を反転します。
	 */
	public static void reverse(final int[][] a) {
		reverse(a, 0, a.length);
	}

	/**
	 * 配列の指定範囲の行を反転します。
	 */
	public static void reverse(final int[][] a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swapRow(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列の行を反転します。
	 */
	public static void reverse(final long[][] a) {
		reverse(a, 0, a.length);
	}

	/**
	 * 配列の指定範囲の行を反転します。
	 */
	public static void reverse(final long[][] a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swapRow(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列の行を反転します。
	 */
	public static void reverse(final char[][] a) {
		reverse(a, 0, a.length);
	}

	/**
	 * 配列の指定範囲の行を反転します。
	 */
	public static void reverse(final char[][] a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swapRow(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列を反転します。
	 */
	public static void reverse(final IntMutableArray a) {
		reverse(a, 0, a.size());
	}

	/**
	 * 配列の指定範囲を反転します。
	 */
	public static void reverse(final IntMutableArray a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swap(a, fromIdx++, --toIdx);
	}

	/**
	 * 配列を反転します。
	 */
	public static void reverse(final LongMutableArray a) {
		reverse(a, 0, a.size());
	}

	/**
	 * 配列の指定範囲を反転します。
	 */
	public static void reverse(final LongMutableArray a, int fromIdx, int toIdx) {
		while (fromIdx < toIdx) swap(a, fromIdx++, --toIdx);
	}

	/**
	 * 二要素を交換します。
	 */
	public static void swap(final int[] a, final int i, final int j) {
		final int v = a[i];
		a[i] = a[j];
		a[j] = v;
	}

	/**
	 * 二要素を交換します。
	 */
	public static void swap(final long[] a, final int i, final int j) {
		final long v = a[i];
		a[i] = a[j];
		a[j] = v;
	}

	/**
	 * 二要素を交換します。
	 */
	public static void swap(final char[] a, final int i, final int j) {
		final char v = a[i];
		a[i] = a[j];
		a[j] = v;
	}

	/**
	 * 二要素を交換します。
	 */
	public static void swap(final IntMutableArray a, final int i, final int j) {
		final int v = a.get(i);
		a.set(i, a.get(j));
		a.set(j, v);
	}

	/**
	 * 二要素を交換します。
	 */
	public static void swap(final LongMutableArray a, final int i, final int j) {
		final long v = a.get(i);
		a.set(i, a.get(j));
		a.set(j, v);
	}

	/**
	 * 二つの行を交換します。
	 */
	public static void swapRow(final int[][] a, final int i, final int j) {
		final int[] row = a[i];
		a[i] = a[j];
		a[j] = row;
	}

	/**
	 * 二つの行を交換します。
	 */
	public static void swapRow(final long[][] a, final int i, final int j) {
		final long[] row = a[i];
		a[i] = a[j];
		a[j] = row;
	}

	/**
	 * 二つの行を交換します。
	 */
	public static void swapRow(final char[][] a, final int i, final int j) {
		final char[] row = a[i];
		a[i] = a[j];
		a[j] = row;
	}

	/**
	 * 異なる値の個数を返します。
	 */
	public static int uniqueSize(final int[] a) {
		if (a.length == 0) return 0;
		final int[] b = a.clone();
		Arrays.sort(b);
		int size = 1;
		for (int i = 1; i < b.length; i++) if (b[i - 1] != b[i]) size++;
		return size;
	}

	/**
	 * 異なる値の個数を返します。
	 */
	public static int uniqueSize(final long[] a) {
		if (a.length == 0) return 0;
		final long[] b = a.clone();
		Arrays.sort(b);
		int size = 1;
		for (int i = 1; i < b.length; i++) if (b[i - 1] != b[i]) size++;
		return size;
	}

	/**
	 * 異なる値の個数を返します。
	 */
	public static int uniqueSize(final IntArray a) {
		final int n = a.size();
		if (n == 0) return 0;
		final int[] b = new int[n];
		for (int i = 0; i < n; i++) b[i] = a.get(i);
		Arrays.sort(b);
		int size = 1;
		for (int i = 1; i < n; i++) if (b[i - 1] != b[i]) size++;
		return size;
	}

	/**
	 * 異なる値の個数を返します。
	 */
	public static int uniqueSize(final LongArray a) {
		final int n = a.size();
		if (n == 0) return 0;
		final long[] b = new long[n];
		for (int i = 0; i < n; i++) b[i] = a.get(i);
		Arrays.sort(b);
		int size = 1;
		for (int i = 1; i < n; i++) if (b[i - 1] != b[i]) size++;
		return size;
	}

	/**
	 * 0以上size未満の各値の出現回数を返します。
	 */
	public static int[] freq(final int[] a, final int size) {
		final int[] freq = new int[size];
		for (final int v : a) freq[v]++;
		return freq;
	}

	/**
	 * 0以上size未満の各値の出現回数を返します。
	 */
	public static int[] freq(final long[] a, final int size) {
		final int[] freq = new int[size];
		for (final long v : a) freq[(int) v]++;
		return freq;
	}

	/**
	 * 0以上size未満の各値の出現回数を返します。
	 */
	public static int[] freq(final IntArray a, final int size) {
		final int[] freq = new int[size];
		for (int i = 0; i < a.size(); i++) freq[a.get(i)]++;
		return freq;
	}

	/**
	 * 0以上size未満の各値の出現回数を返します。
	 */
	public static int[] freq(final LongArray a, final int size) {
		final int[] freq = new int[size];
		for (int i = 0; i < a.size(); i++) freq[(int) a.get(i)]++;
		return freq;
	}

	/**
	 * base以上base+size未満の各文字の出現回数を返します。
	 */
	public static int[] freq(final char[] a, final char base, final int size) {
		final int[] freq = new int[size];
		for (final char c : a) freq[c - base]++;
		return freq;
	}

	/**
	 * 配列に含まれない最小の非負整数を返します。
	 */
	public static int mex(final int[] a) {
		final int n = a.length;
		final boolean[] used = new boolean[n + 1];
		for (final int v : a) if (0 <= v && v <= n) used[v] = true;
		for (int i = 0; ; i++) if (!used[i]) return i;
	}

	/**
	 * 配列に含まれない最小の非負整数を返します。
	 */
	public static int mex(final long[] a) {
		final int n = a.length;
		final boolean[] used = new boolean[n + 1];
		for (final long v : a) if (0 <= v && v <= n) used[(int) v] = true;
		for (int i = 0; ; i++) if (!used[i]) return i;
	}

	/**
	 * 配列に含まれない最小の非負整数を返します。
	 */
	public static int mex(final IntArray a) {
		final int n = a.size();
		final boolean[] used = new boolean[n + 1];
		for (int i = 0; i < n; i++) {
			final int v = a.get(i);
			if (0 <= v && v <= n) used[v] = true;
		}
		for (int i = 0; ; i++) if (!used[i]) return i;
	}

	/**
	 * 配列に含まれない最小の非負整数を返します。
	 */
	public static int mex(final LongArray a) {
		final int n = a.size();
		final boolean[] used = new boolean[n + 1];
		for (int i = 0; i < n; i++) {
			final long v = a.get(i);
			if (0 <= v && v <= n) used[(int) v] = true;
		}
		for (int i = 0; ; i++) if (!used[i]) return i;
	}

	/**
	 * 配列が広義昇順ならtrueを返します。
	 */
	public static boolean isSorted(final int[] a) {
		for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
		return true;
	}

	/**
	 * 配列が広義昇順ならtrueを返します。
	 */
	public static boolean isSorted(final long[] a) {
		for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
		return true;
	}

	/**
	 * 配列が広義昇順ならtrueを返します。
	 */
	public static boolean isSorted(final IntArray a) {
		for (int i = 1; i < a.size(); i++) if (a.get(i - 1) > a.get(i)) return false;
		return true;
	}

	/**
	 * 配列が広義昇順ならtrueを返します。
	 */
	public static boolean isSorted(final LongArray a) {
		for (int i = 1; i < a.size(); i++) if (a.get(i - 1) > a.get(i)) return false;
		return true;
	}

	/**
	 * 全要素が等しければtrueを返します。
	 */
	public static boolean allEqual(final int[] a) {
		for (int i = 1; i < a.length; i++) if (a[i - 1] != a[i]) return false;
		return true;
	}

	/**
	 * 全要素が等しければtrueを返します。
	 */
	public static boolean allEqual(final long[] a) {
		for (int i = 1; i < a.length; i++) if (a[i - 1] != a[i]) return false;
		return true;
	}

	/**
	 * 全要素が等しければtrueを返します。
	 */
	public static boolean allEqual(final IntArray a) {
		for (int i = 1; i < a.size(); i++) if (a.get(i - 1) != a.get(i)) return false;
		return true;
	}

	/**
	 * 全要素が等しければtrueを返します。
	 */
	public static boolean allEqual(final LongArray a) {
		for (int i = 1; i < a.size(); i++) if (a.get(i - 1) != a.get(i)) return false;
		return true;
	}
	// endregion

	// region < localMaxCnt >
	public static int localMaxCnt(final int[] a) {
		return localMaxCnt(a, a.length);
	}

	public static int localMaxCnt(final int[] a, final int len) {
		if (len < 3) return 0;
		int cnt = 0;
		for (int i = 1; i < len - 1; i++) if (a[i - 1] < a[i] && a[i] > a[i + 1]) cnt++;
		return cnt;
	}

	public static int localMaxCnt(final long[] a) {
		return localMaxCnt(a, a.length);
	}

	public static int localMaxCnt(final long[] a, final int len) {
		if (len < 3) return 0;
		int cnt = 0;
		for (int i = 1; i < len - 1; i++) if (a[i - 1] < a[i] && a[i] > a[i + 1]) cnt++;
		return cnt;
	}

	public static int localMaxCnt(final IntArray a) {
		int cnt = 0;
		for (int i = 1; i < a.size() - 1; i++) if (a.get(i - 1) < a.get(i) && a.get(i) > a.get(i + 1)) cnt++;
		return cnt;
	}

	public static int localMaxCnt(final LongArray a) {
		int cnt = 0;
		for (int i = 1; i < a.size() - 1; i++) if (a.get(i - 1) < a.get(i) && a.get(i) > a.get(i + 1)) cnt++;
		return cnt;
	}
	// endregion

	// region < localMinCnt >
	public static int localMinCnt(final int[] a) {
		return localMinCnt(a, a.length);
	}

	public static int localMinCnt(final int[] a, final int len) {
		if (len < 3) return 0;
		int cnt = 0;
		for (int i = 1; i < len - 1; i++) if (a[i - 1] > a[i] && a[i] < a[i + 1]) cnt++;
		return cnt;
	}

	public static int localMinCnt(final long[] a) {
		return localMinCnt(a, a.length);
	}

	public static int localMinCnt(final long[] a, final int len) {
		if (len < 3) return 0;
		int cnt = 0;
		for (int i = 1; i < len - 1; i++) if (a[i - 1] > a[i] && a[i] < a[i + 1]) cnt++;
		return cnt;
	}

	public static int localMinCnt(final IntArray a) {
		int cnt = 0;
		for (int i = 1; i < a.size() - 1; i++) if (a.get(i - 1) > a.get(i) && a.get(i) < a.get(i + 1)) cnt++;
		return cnt;
	}

	public static int localMinCnt(final LongArray a) {
		int cnt = 0;
		for (int i = 1; i < a.size() - 1; i++) if (a.get(i - 1) > a.get(i) && a.get(i) < a.get(i + 1)) cnt++;
		return cnt;
	}
	// endregion

	// region < runLen >

	/**
	 * 同じ値が連続する最長区間の長さを返します。
	 */
	public static int runLen(final int[] a) {
		return runLen(a, a.length);
	}

	public static int runLen(final int[] a, final int len) {
		if (len == 0) return 0;
		int ans = 1, cnt = 1;
		for (int i = 1; i < len; i++) {
			if (a[i - 1] == a[i]) {
				if (ans < ++cnt) ans = cnt;
			} else cnt = 1;
		}
		return ans;
	}

	public static int runLen(final long[] a) {
		return runLen(a, a.length);
	}

	public static int runLen(final long[] a, final int len) {
		if (len == 0) return 0;
		int ans = 1, cnt = 1;
		for (int i = 1; i < len; i++) {
			if (a[i - 1] == a[i]) {
				if (ans < ++cnt) ans = cnt;
			} else cnt = 1;
		}
		return ans;
	}

	public static int runLen(final IntArray a) {
		int len = a.size();
		if (len == 0) return 0;
		int ans = 1, cnt = 1;
		for (int i = 1; i < len; i++) {
			if (a.get(i - 1) == a.get(i)) {
				if (ans < ++cnt) ans = cnt;
			} else cnt = 1;
		}
		return ans;
	}

	public static int runLen(final LongArray a) {
		int len = a.size();
		if (len == 0) return 0;
		int ans = 1, cnt = 1;
		for (int i = 1; i < len; i++) {
			if (a.get(i - 1) == a.get(i)) {
				if (ans < ++cnt) ans = cnt;
			} else cnt = 1;
		}
		return ans;
	}
	// endregion

	// region < maxWin >

	/**
	 * 長さkの連続部分列の和の最大値を返します。
	 */
	public static long maxWin(final int[] a, final int k) {
		return maxWin(a, a.length, k);
	}

	public static long maxWin(final int[] a, final int len, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a[i];
		long res = win;
		for (int i = k; i < len; i++) {
			win += a[i] - a[i - k];
			if (win > res) res = win;
		}
		return res;
	}

	public static long maxWin(final long[] a, final int k) {
		return maxWin(a, a.length, k);
	}

	public static long maxWin(final long[] a, final int len, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a[i];
		long res = win;
		for (int i = k; i < len; i++) {
			win += a[i] - a[i - k];
			if (win > res) res = win;
		}
		return res;
	}

	public static long maxWin(final IntArray a, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a.get(i);
		long res = win;
		for (int i = k; i < a.size(); i++) {
			win += a.get(i) - a.get(i - k);
			if (win > res) res = win;
		}
		return res;
	}

	public static long maxWin(final LongArray a, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a.get(i);
		long res = win;
		for (int i = k; i < a.size(); i++) {
			win += a.get(i) - a.get(i - k);
			if (win > res) res = win;
		}
		return res;
	}
	// endregion

	// region < minWin >

	/**
	 * 長さkの連続部分列の和の最小値を返します。
	 */
	public static long minWin(final int[] a, final int k) {
		return minWin(a, a.length, k);
	}

	public static long minWin(final int[] a, final int len, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a[i];
		long res = win;
		for (int i = k; i < len; i++) {
			win += a[i] - a[i - k];
			if (win < res) res = win;
		}
		return res;
	}

	public static long minWin(final long[] a, final int k) {
		return minWin(a, a.length, k);
	}

	public static long minWin(final long[] a, final int len, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a[i];
		long res = win;
		for (int i = k; i < len; i++) {
			win += a[i] - a[i - k];
			if (win < res) res = win;
		}
		return res;
	}

	public static long minWin(final IntArray a, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a.get(i);
		long res = win;
		for (int i = k; i < a.size(); i++) {
			win += a.get(i) - a.get(i - k);
			if (win < res) res = win;
		}
		return res;
	}

	public static long minWin(final LongArray a, final int k) {
		long win = 0;
		for (int i = 0; i < k; i++) win += a.get(i);
		long res = win;
		for (int i = k; i < a.size(); i++) {
			win += a.get(i) - a.get(i - k);
			if (win < res) res = win;
		}
		return res;
	}
	// endregion

	// region < winMaxLen >

	/**
	 * 各幅k区間の最大値を担う同一要素が最長で続く位置と区間数を返します。
	 */
	public static int[] winMaxLen(final int[] a, final int k) {
		return winMaxLen(a, a.length, k);
	}

	public static int[] winMaxLen(final int[] a, final int len, final int k) {
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a[dq[t - 1]] < a[i]) t--;
			dq[t++] = i;
		}
		int maxLen = 1, maxIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a[dq[t - 1]] < a[i]) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					maxIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{maxIdx, maxLen};
	}

	public static int[] winMaxLen(final long[] a, final int k) {
		return winMaxLen(a, a.length, k);
	}

	public static int[] winMaxLen(final long[] a, final int len, final int k) {
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a[dq[t - 1]] < a[i]) t--;
			dq[t++] = i;
		}
		int maxLen = 1, maxIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a[dq[t - 1]] < a[i]) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					maxIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{maxIdx, maxLen};
	}

	public static int[] winMaxLen(final IntArray a, final int k) {
		int len = a.size();
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a.get(dq[t - 1]) < a.get(i)) t--;
			dq[t++] = i;
		}
		int maxLen = 1, maxIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a.get(dq[t - 1]) < a.get(i)) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					maxIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{maxIdx, maxLen};
	}

	public static int[] winMaxLen(final LongArray a, final int k) {
		int len = a.size();
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a.get(dq[t - 1]) < a.get(i)) t--;
			dq[t++] = i;
		}
		int maxLen = 1, maxIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a.get(dq[t - 1]) < a.get(i)) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					maxIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{maxIdx, maxLen};
	}
	// endregion

	// region < winMinLen >

	/**
	 * 各幅k区間の最小値を担う同一要素が最長で続く位置と区間数を返します。
	 */
	public static int[] winMinLen(final int[] a, final int k) {
		return winMinLen(a, a.length, k);
	}

	public static int[] winMinLen(final int[] a, final int len, final int k) {
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a[dq[t - 1]] > a[i]) t--;
			dq[t++] = i;
		}
		int maxLen = 1, minIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a[dq[t - 1]] > a[i]) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					minIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{minIdx, maxLen};
	}

	public static int[] winMinLen(final long[] a, final int k) {
		return winMinLen(a, a.length, k);
	}

	public static int[] winMinLen(final long[] a, final int len, final int k) {
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a[dq[t - 1]] > a[i]) t--;
			dq[t++] = i;
		}
		int maxLen = 1, minIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a[dq[t - 1]] > a[i]) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					minIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{minIdx, maxLen};
	}

	public static int[] winMinLen(final IntArray a, final int k) {
		int len = a.size();
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a.get(dq[t - 1]) > a.get(i)) t--;
			dq[t++] = i;
		}
		int maxLen = 1, minIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a.get(dq[t - 1]) > a.get(i)) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					minIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{minIdx, maxLen};
	}

	public static int[] winMinLen(final LongArray a, final int k) {
		int len = a.size();
		if (len < k) return new int[]{-1, -1};
		int[] dq = new int[len];
		int h = 0, t = 0;
		for (int i = 0; i < k; i++) {
			while (h != t && a.get(dq[t - 1]) > a.get(i)) t--;
			dq[t++] = i;
		}
		int maxLen = 1, minIdx = dq[h];
		for (int i = k, curLen = 1, curIdx = dq[h]; i < len; i++) {
			if (h != t && dq[h] <= i - k) h++;
			while (h != t && a.get(dq[t - 1]) > a.get(i)) t--;
			dq[t++] = i;
			if (dq[h] == curIdx) {
				if (maxLen < ++curLen) {
					maxLen = curLen;
					minIdx = curIdx;
				}
			} else {
				curIdx = dq[h];
				curLen = 1;
			}
		}
		return new int[]{minIdx, maxLen};
	}
	// endregion

	// region < lis, lnds, lds, lnis >
	public static int lis(final int[] a) {
		return lis(a, a.length);
	}

	public static int lis(final int[] a, final int len) {
		if (len <= 1) return len;
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = a[i];
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lis(final long[] a) {
		return lis(a, a.length);
	}

	public static int lis(final long[] a, final int len) {
		if (len <= 1) return len;
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = a[i];
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnds(final int[] a) {
		return lnds(a, a.length);
	}

	public static int lnds(final int[] a, final int len) {
		if (len <= 1) return len;
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = a[i];
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnds(final long[] a) {
		return lnds(a, a.length);
	}

	public static int lnds(final long[] a, final int len) {
		if (len <= 1) return len;
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = a[i];
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lds(final int[] a) {
		return lds(a, a.length);
	}

	public static int lds(final int[] a, final int len) {
		if (len <= 1) return len;
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = -a[i];
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lds(final long[] a) {
		return lds(a, a.length);
	}

	public static int lds(final long[] a, final int len) {
		if (len <= 1) return len;
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = -a[i];
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnis(final int[] a) {
		return lnis(a, a.length);
	}

	public static int lnis(final int[] a, final int len) {
		if (len <= 1) return len;
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = -a[i];
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnis(final long[] a) {
		return lnis(a, a.length);
	}

	public static int lnis(final long[] a, final int len) {
		if (len <= 1) return len;
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = -a[i];
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lis(final IntArray a) {
		int len = a.size();
		if (len <= 1) return len;
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = a.get(i);
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lis(final LongArray a) {
		int len = a.size();
		if (len <= 1) return len;
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = a.get(i);
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnds(final IntArray a) {
		int len = a.size();
		if (len <= 1) return len;
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = a.get(i);
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnds(final LongArray a) {
		int len = a.size();
		if (len <= 1) return len;
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = a.get(i);
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lds(final IntArray a) {
		int len = a.size();
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = -a.get(i);
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lds(final LongArray a) {
		int len = a.size();
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = -a.get(i);
			int pos = bs(dp, ans, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnis(final IntArray a) {
		int len = a.size();
		final int[] dp = new int[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final int v = -a.get(i);
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}

	public static int lnis(final LongArray a) {
		int len = a.size();
		final long[] dp = new long[len];
		int ans = 0;
		for (int i = 0; i < len; i++) {
			final long v = -a.get(i);
			int pos = bsUpper(dp, ans, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == ans) ans++;
		}
		return ans;
	}
	// endregion

	// region < subset >

	/**
	 * 和がtとなる部分集合の個数を半分全列挙で求めます。
	 */
	public static int subsetMitm(final int[] a, final long t) {
		return subsetMitm(a, a.length, t);
	}

	public static int subsetMitm(final int[] a, final int len, final long t) {
		int size1 = len >> 1, size2 = len - size1;
		final HashMap<Long, Integer> hm = new HashMap<>();
		for (int m = 0; m < 1 << size1; m++) {
			long s = 0;
			for (int i = 0; i < size1; i++) if ((m & (1 << i)) != 0) s += a[i];
			hm.merge(s, 1, Integer::sum);
		}
		int ans = 0;
		for (int m = 0; m < 1 << size2; m++) {
			long s = 0;
			for (int i = 0, x = size1; i < size2; i++, x++) if ((m & (1 << i)) != 0) s += a[x];
			ans += hm.getOrDefault(t - s, 0);
		}
		return ans;
	}

	public static int subsetMitm(final long[] a, final long t) {
		return subsetMitm(a, a.length, t);
	}

	public static int subsetMitm(final long[] a, final int len, final long t) {
		int size1 = len >> 1, size2 = len - size1;
		final HashMap<Long, Integer> hm = new HashMap<>();
		for (int m = 0; m < 1 << size1; m++) {
			long s = 0;
			for (int i = 0; i < size1; i++) if ((m & (1 << i)) != 0) s += a[i];
			hm.merge(s, 1, Integer::sum);
		}
		int ans = 0;
		for (int m = 0; m < 1 << size2; m++) {
			long s = 0;
			for (int i = 0, x = size1; i < size2; i++, x++) if ((m & (1 << i)) != 0) s += a[x];
			ans += hm.getOrDefault(t - s, 0);
		}
		return ans;
	}

	public static int subsetMitm(final IntArray a, final long t) {
		int len = a.size(), size1 = len >> 1, size2 = len - size1;
		final HashMap<Long, Integer> hm = new HashMap<>();
		for (int m = 0; m < 1 << size1; m++) {
			long s = 0;
			for (int i = 0; i < size1; i++) if ((m & (1 << i)) != 0) s += a.get(i);
			hm.merge(s, 1, Integer::sum);
		}
		int ans = 0;
		for (int m = 0; m < 1 << size2; m++) {
			long s = 0;
			for (int i = 0, x = size1; i < size2; i++, x++) if ((m & (1 << i)) != 0) s += a.get(x);
			ans += hm.getOrDefault(t - s, 0);
		}
		return ans;
	}

	public static int subsetMitm(final LongArray a, final long t) {
		int len = a.size(), size1 = len >> 1, size2 = len - size1;
		final HashMap<Long, Integer> hm = new HashMap<>();
		for (int m = 0; m < 1 << size1; m++) {
			long s = 0;
			for (int i = 0; i < size1; i++) if ((m & (1 << i)) != 0) s += a.get(i);
			hm.merge(s, 1, Integer::sum);
		}
		int ans = 0;
		for (int m = 0; m < 1 << size2; m++) {
			long s = 0;
			for (int i = 0, x = size1; i < size2; i++, x++) if ((m & (1 << i)) != 0) s += a.get(x);
			ans += hm.getOrDefault(t - s, 0);
		}
		return ans;
	}

	/**
	 * 和がtとなる部分集合の個数をHashMapによるDPで求めます。
	 */
	public static int subsetDp(final int[] a, final long t) {
		return subsetDp(a, a.length, t);
	}

	public static int subsetDp(final int[] a, final int len, final long t) {
		HashMap<Long, Integer> dp = new HashMap<>();
		dp.put(0L, 1);
		for (int i = 0; i < len; i++) {
			HashMap<Long, Integer> next = new HashMap<>(dp);
			for (final var e : dp.entrySet()) next.merge(e.getKey() + a[i], e.getValue(), Integer::sum);
			dp = next;
		}
		return dp.getOrDefault(t, 0);
	}

	public static int subsetDp(final long[] a, final long t) {
		return subsetDp(a, a.length, t);
	}

	public static int subsetDp(final long[] a, final int len, final long t) {
		HashMap<Long, Integer> dp = new HashMap<>();
		dp.put(0L, 1);
		for (int i = 0; i < len; i++) {
			HashMap<Long, Integer> next = new HashMap<>(dp);
			for (final var e : dp.entrySet()) next.merge(e.getKey() + a[i], e.getValue(), Integer::sum);
			dp = next;
		}
		return dp.getOrDefault(t, 0);
	}

	public static int subsetDp(final IntArray a, final long t) {
		HashMap<Long, Integer> dp = new HashMap<>();
		dp.put(0L, 1);
		for (int i = 0; i < a.size(); i++) {
			HashMap<Long, Integer> next = new HashMap<>(dp);
			for (final var e : dp.entrySet()) next.merge(e.getKey() + a.get(i), e.getValue(), Integer::sum);
			dp = next;
		}
		return dp.getOrDefault(t, 0);
	}

	public static int subsetDp(final LongArray a, final long t) {
		HashMap<Long, Integer> dp = new HashMap<>();
		dp.put(0L, 1);
		for (int i = 0; i < a.size(); i++) {
			HashMap<Long, Integer> next = new HashMap<>(dp);
			for (final var e : dp.entrySet()) next.merge(e.getKey() + a.get(i), e.getValue(), Integer::sum);
			dp = next;
		}
		return dp.getOrDefault(t, 0);
	}

	/**
	 * 要素数k、和tとなる部分集合の個数を再帰で求めます。
	 */
	public static int subsetRecursion(final int[] a, final long t, final int k) {
		return subsetRecursion(a, a.length, t, k);
	}

	public static int subsetRecursion(final int[] a, final int len, final long t, final int k) {
		return subsetRec(a, 0, len, t, k);
	}

	public static int subsetRecursion(final long[] a, final long t, final int k) {
		return subsetRecursion(a, a.length, t, k);
	}

	public static int subsetRecursion(final long[] a, final int len, final long t, final int k) {
		return subsetRec(a, 0, len, t, k);
	}

	public static int subsetRecursion(final IntArray a, final long t, final int k) {
		return subsetRec(a, 0, t, k);
	}

	public static int subsetRecursion(final LongArray a, final long t, final int k) {
		return subsetRec(a, 0, t, k);
	}

	private static int subsetRec(final int[] a, final int i, final int len, final long t, final int k) {
		if (k == 0) return t == 0 ? 1 : 0;
		if (i == len || len - i < k) return 0;
		return subsetRec(a, i + 1, len, t - a[i], k - 1) + subsetRec(a, i + 1, len, t, k);
	}

	private static int subsetRec(final long[] a, final int i, final int len, final long t, final int k) {
		if (k == 0) return t == 0 ? 1 : 0;
		if (i == len || len - i < k) return 0;
		return subsetRec(a, i + 1, len, t - a[i], k - 1) + subsetRec(a, i + 1, len, t, k);
	}

	private static int subsetRec(final IntArray a, final int i, final long t, final int k) {
		if (k == 0) return t == 0 ? 1 : 0;
		if (i == a.size() || a.size() - i < k) return 0;
		return subsetRec(a, i + 1, t - a.get(i), k - 1) + subsetRec(a, i + 1, t, k);
	}

	private static int subsetRec(final LongArray a, final int i, final long t, final int k) {
		if (k == 0) return t == 0 ? 1 : 0;
		if (i == a.size() || a.size() - i < k) return 0;
		return subsetRec(a, i + 1, t - a.get(i), k - 1) + subsetRec(a, i + 1, t, k);
	}
	// endregion

	// region < binary search >
	private static int bs(final int[] a, final int len, final int t) {
		int l = 0, r = len - 1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (a[m] > t) r = m - 1;
			else if (a[m] < t) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private static int bs(final long[] a, final int len, final long t) {
		int l = 0, r = len - 1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (a[m] > t) r = m - 1;
			else if (a[m] < t) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private static int bsUpper(final int[] a, final int len, final int t) {
		int ans = -1;
		int l = 0, r = len - 1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (a[m] > t) r = m - 1;
			else {
				if (a[m] == t) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int bsUpper(final long[] a, final int len, final long t) {
		int ans = -1;
		int l = 0, r = len - 1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (a[m] > t) r = m - 1;
			else {
				if (a[m] == t) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}
	// endregion
}
