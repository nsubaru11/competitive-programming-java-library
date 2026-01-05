import java.util.*;
import java.util.function.*;


/**
 * 不変長整数配列 + 高速回転操作
 */
@SuppressWarnings("unused")
public final class LongArray1D implements Iterable<Long> {
	public final long sum, max, min;
	public final int size;
	private final long[] arr, ps;
	private int offset = 0;

	public LongArray1D(final int n, final IntToLongFunction init) {
		size = n;
		arr = new long[2 * n];
		ps = new long[2 * n + 1];
		long v0 = init.applyAsLong(0);
		arr[0] = v0;
		long s = v0;
		long mx = v0;
		long mn = v0;
		for (int i = 1; i < n; i++) {
			long v = init.applyAsLong(i);
			arr[i] = v;
			s += v;
			if (v > mx) mx = v;
			if (v < mn) mn = v;
		}
		sum = s;
		max = mx;
		min = mn;
		System.arraycopy(arr, 0, arr, n, n);
		for (int i = 0; i < 2 * n; i++) {
			ps[i + 1] = ps[i] + arr[i];
		}
	}

	public long get(final int i) {
		return arr[offset + i];
	}

	public long sum(final int l, final int r) {
		return ps[offset + r] - ps[offset + l - 1];
	}

	public long sum() {
		return sum;
	}

	public long getMax() {
		return max;
	}

	public long getMin() {
		return min;
	}

	public void lShift() {
		offset++;
		if (offset == size) offset = 0;
	}

	public void rShift() {
		offset--;
		if (offset == -1) offset = size - 1;
	}

	public void lShift(final int n) {
		if (n < 0) rShift(-n);
		offset += n % size;
		if (offset >= size) offset -= size;
	}

	public void rShift(final int n) {
		if (n < 0) lShift(-n);
		offset -= n % size;
		if (offset < 0) offset += size;
	}

	public void resetRotation() {
		offset = 0;
	}

	/**
	 * 極大値（両隣より大きい）の個数
	 *
	 * @return 極大値の個数
	 */
	public int localMaxCnt() {
		if (size < 3) return 0;
		int cnt = 0;
		long l = arr[offset], m = arr[offset + 1];
		for (int i = 2; i < size; i++) {
			final long r = arr[offset + i];
			if (l < m && m > r) cnt++;
			l = m;
			m = r;
		}
		return cnt;
	}

	/**
	 * 極小値の個数
	 *
	 * @return 極小値の個数
	 */
	public int localMinCnt() {
		if (size < 3) return 0;
		int cnt = 0;
		long l = arr[offset], m = arr[offset + 1];
		for (int i = 2; i < size; i++) {
			final long r = arr[offset + i];
			if (l > m && m < r) cnt++;
			l = m;
			m = r;
		}
		return cnt;
	}

	/**
	 * 同じ要素が連続する最長の長さを返します
	 *
	 * @return 同じ要素が連続する最長の長さ
	 */
	public int runLen() {
		if (size == 1) return 1;
		int len = 1, cnt = 1;
		long prev = arr[offset];
		for (int i = 1; i < size; i++) {
			final long cur = arr[offset + i];
			if (prev == cur) {
				if (len < ++cnt) len = cnt;
			} else {
				cnt = 1;
				prev = cur;
			}
		}
		return len;
	}

	/**
	 * 長さ k の連続部分列の最大和
	 *
	 * @param k 長さ
	 * @return 最大連続部分和
	 */
	public long maxWin(final int k) {
		if (k == size) return sum;
		long win = 0;
		for (int i = 0; i < k; i++) win += arr[offset + i];
		long res = win;
		for (int i = k; i < size; i++) {
			win += arr[offset + i] - arr[offset + i - k];
			if (win > res) res = win;
		}
		return res;
	}

	/**
	 * 長さ k の連続部分列の最小和
	 *
	 * @param k 長さ
	 * @return 最小連続部分和
	 */
	public long minWin(final int k) {
		if (k == size) return sum;
		long win = 0;
		for (int i = 0; i < k; i++) win += arr[offset + i];
		long res = win;
		for (int i = k; i < size; i++) {
			win += arr[offset + i] - arr[offset + i - k];
			if (win < res) res = win;
		}
		return res;
	}

	/**
	 * 長さ k のスライディングウィンドウにおいて、
	 * ある要素が最大値として連続して存在する最長期間
	 *
	 * @param k ウィンドウサイズ
	 * @return 最大値が連続する最長期間
	 */
	public int winMaxLen(final int k) {
		if (size < k) return -1;
		final ArrayDeque<Integer> dq = new ArrayDeque<>(k);
		for (int i = 0; i < k; i++) {
			final long v = arr[offset + i];
			while (!dq.isEmpty() && arr[offset + dq.peekLast()] < v) dq.pollLast();
			dq.addLast(i);
		}
		int len = 1, maxIdx = dq.peekFirst();
		for (int i = k; i < size; i++) {
			if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
			final long v = arr[offset + i];
			while (!dq.isEmpty() && arr[offset + dq.peekLast()] < v) dq.pollLast();
			dq.addLast(i);
			if (dq.peek() == maxIdx) len++;
			else {
				maxIdx = dq.peekFirst();
				len = 1;
			}
		}
		return len;
	}

	/**
	 * 長さ k のスライディングウィンドウにおいて、
	 * ある要素が最小値として連続して存在する最長期間
	 *
	 * @param k ウィンドウサイズ
	 * @return 最小値が連続する最長期間
	 */
	public int winMinLen(final int k) {
		if (size < k) return -1;
		final ArrayDeque<Integer> dq = new ArrayDeque<>(k);
		for (int i = 0; i < k; i++) {
			final long v = arr[offset + i];
			while (!dq.isEmpty() && arr[offset + dq.peekLast()] > v) dq.pollLast();
			dq.addLast(i);
		}
		int len = 1, minIdx = dq.peekFirst();
		for (int i = k; i < size; i++) {
			final long v = arr[offset + i];
			if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
			while (!dq.isEmpty() && arr[offset + dq.peekLast()] > v) dq.pollLast();
			dq.addLast(i);
			if (dq.peek() == minIdx) len++;
			else {
				minIdx = dq.peekFirst();
				len = 1;
			}
		}
		return len;
	}

	/**
	 * 最長増加部分列の長さ（狭義単調増加）
	 *
	 * @return LIS の長さ
	 */
	public int lis() {
		if (size <= 1) return size;
		final long[] dp = new long[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final long v = arr[offset + i];
			int pos = bs(dp, len, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == len) len++;
		}
		return len;
	}

	/**
	 * 最長増加部分列の長さ（広義単調増加）
	 *
	 * @return LIS の長さ
	 */
	public int lnds() {
		if (size <= 1) return size;
		final long[] dp = new long[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final long v = arr[offset + i];
			int pos = bsUpper(dp, len, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == len) len++;
		}
		return len;
	}

	/**
	 * 最長減少部分列の長さ（狭義単調減少）
	 *
	 * @return LDS の長さ
	 */
	public int lds() {
		if (size <= 1) return size;
		final long[] dp = new long[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final long v = -arr[offset + i];
			int pos = bs(dp, len, v);
			if (pos < 0) {
				pos = ~pos;
				dp[pos] = v;
			}
			if (pos == len) len++;
		}
		return len;
	}

	/**
	 * 最長減少部分列の長さ（広義単調減少）
	 *
	 * @return LDS の長さ
	 */
	public int lnis() {
		if (size <= 1) return size;
		final long[] dp = new long[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final long v = -arr[offset + i];
			int pos = bsUpper(dp, len, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == len) len++;
		}
		return len;
	}

	/**
	 * 任意個選択の部分和判定
	 *
	 * @param target 目標和
	 * @return 部分集合の和が target になるか
	 * @throws UnsupportedOperationException n > 60 の場合
	 */
	public boolean subset(final long target) {
		if (size == 1) return target == arr[0] || target == 0;
		if (target == 0 || target == sum) return true;
		if (target < 0 || target > sum) return false;
		return subsetMitm(target);
	}

	/**
	 * ちょうど k 個選択の部分和判定
	 *
	 * @param target 目標和
	 * @param k      選択個数
	 * @return k 個選んで和が target になるか
	 */
	public boolean subset(final long target, final int k) {
		if (size == 1) return target == arr[0];
		return subsetRec(target, k, size - 1);
	}

	private boolean subsetMitm(final long t) {
		final int half = size >> 1;
		final HashSet<Long> set = new HashSet<>();
		for (int m = 0; m < 1 << half; m++) {
			long s = 0;
			for (int i = 0; i < half; i++) {
				if ((m & (1 << i)) != 0) s += arr[i];
			}
			if (s == t) return true;
			set.add(s);
		}
		for (int m = 0; m < (1 << (size - half)); m++) {
			long s = 0;
			for (int i = 0, x = half; i < size - half; i++, x++) {
				if ((m & (1 << i)) != 0) s += arr[x];
			}
			if (set.contains(t - s)) return true;
		}
		return false;
	}

	public boolean subsetRec(final long target, final int k, final int idx) {
		if (k == 0) return target == 0;
		if (idx < 0 || idx + 1 < k) return false;
		if (idx == k - 1) return target == ps[idx + 1];
		if (ps[idx + 1] < target) return false;
		if (subsetRec(target - arr[idx], k - 1, idx - 1)) return true;
		return subsetRec(target, k, idx - 1);
	}

	private int bs(final long[] a, final int len, final long t) {
		int l = 0, r = len - 1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (a[m] > t) r = m - 1;
			else if (a[m] < t) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private int bsUpper(final long[] a, final int len, final long t) {
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

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < size;
			}

			public long nextLong() {
				return arr[offset + idx++];
			}
		};
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final PrimitiveIterator.OfLong it = iterator();
		sb.append(it.nextLong());
		while (it.hasNext()) sb.append(' ').append(it.nextLong());
		return sb.toString();
	}
}
