import java.util.*;
import java.util.function.*;


/**
 * 不変整数配列 + 高速回転操作
 */
@SuppressWarnings("unused")
public final class IntArray1D implements Iterable<Integer> {
	public final long sum;
	public final int size, capacity, mask, max, min;
	private final int[] arr;
	private final long[] ps;
	private int offset = 0;

	public IntArray1D(final int n, final IntUnaryOperator init) {
		size = n;
		capacity = n == 1 ? 1 : Integer.highestOneBit((n - 1) << 1);
		mask = capacity - 1;
		arr = new int[capacity];
		ps = new long[capacity + 1];
		int v = init.applyAsInt(0);
		arr[0] = v;
		int mx, mn;
		long s = mx = mn = v;
		for (int i = 1; i < n; i++) {
			v = init.applyAsInt(i);
			arr[i] = v;
			s += v;
			if (v > mx) mx = v;
			if (v < mn) mn = v;
		}
		sum = s;
		max = mx;
		min = mn;
		System.arraycopy(arr, 0, arr, n, capacity - n);
		for (int i = 1; i < capacity; i++) {
			ps[i] = ps[i - 1] + arr[i - 1];
		}
	}

	public int get(final int i) {
		return arr[(i + offset) & mask];
	}

	public long sum(final int l, final int r) {
		final int pl = (l + offset) & mask;
		final int pr = (r + offset) & mask;
		return pr >= pl ? ps[pr + 1] - ps[pl] : ps[size] - ps[pl] + ps[pr + 1];
	}

	public long sum() {
		return sum;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
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
		int cnt = 0, l = arr[offset], m = arr[(offset + 1) & mask];
		for (int i = 2; i < size; i++) {
			final int r = arr[(i + offset) & mask];
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
		int cnt = 0, l = arr[offset], m = arr[(offset + 1) & mask];
		for (int i = 2; i < size; i++) {
			final int r = arr[(i + offset) & mask];
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
		int len = 1, cnt = 1, prev = arr[offset];
		for (int i = 1; i < size; i++) {
			final int cur = arr[(i + offset) & mask];
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
		for (int i = 0; i < k; i++) win += arr[(i + offset) & mask];
		long res = win;
		for (int i = k; i < size; i++) {
			win += arr[(i + offset) & mask] - arr[(i - k + offset) & mask];
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
		for (int i = 0; i < k; i++) win += arr[(i + offset) & mask];
		long res = win;
		for (int i = k; i < size; i++) {
			win += arr[(i + offset) & mask] - arr[(i - k + offset) & mask];
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
			final int v = arr[(i + offset) & mask];
			while (!dq.isEmpty() && arr[(dq.peekLast() + offset) & mask] < v) dq.pollLast();
			dq.addLast(i);
		}
		int len = 1, maxIdx = dq.peekFirst();
		for (int i = k; i < size; i++) {
			if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
			final int v = arr[(i + offset) & mask];
			while (!dq.isEmpty() && arr[(dq.peekLast() + offset) & mask] < v) dq.pollLast();
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
			final int v = arr[(i + offset) & mask];
			while (!dq.isEmpty() && arr[(dq.peekLast() + offset) & mask] > v) dq.pollLast();
			dq.addLast(i);
		}
		int len = 1, minIdx = dq.peekFirst();
		for (int i = k; i < size; i++) {
			final int v = arr[(i + offset) & mask];
			if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
			while (!dq.isEmpty() && arr[(dq.peekLast() + offset) & mask] > v) dq.pollLast();
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
		final int[] dp = new int[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final int v = arr[(i + offset) & mask];
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
		final int[] dp = new int[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final int v = arr[(i + offset) & mask];
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
		final int[] dp = new int[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final int v = -arr[(i + offset) & mask];
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
		final int[] dp = new int[size];
		int len = 0;
		for (int i = 0; i < size; i++) {
			final int v = -arr[(i + offset) & mask];
			int pos = bsUpper(dp, len, v);
			pos = pos < 0 ? ~pos : pos + 1;
			dp[pos] = v;
			if (pos == len) len++;
		}
		return len;
	}

	/**
	 * 任意個選択の部分和判定（実測最適化版）
	 *
	 * @param target 目標和
	 * @return 部分集合の和が target になるか
	 * @throws UnsupportedOperationException n > 60 の場合
	 */
	public boolean subset(final long target) {
		if (size == 1) return target == arr[0] || target == 0;
		if (target == 0 || target == sum) return true;
		if (target < 0 || target > sum) return false;
		if (size <= 15) return subsetMitm(target);
		if (size <= 20) {
			if (target <= 1_000_000_000 && 0 <= min && target * 2 < sum) {
				return subsetBit((int) target);
			}
			return subsetMitm(target);
		}
		if (size <= 25) {
			if (target <= 1_000_000_000 && 0 <= min && target * 5 < sum * 3) {
				return subsetBit((int) target);
			}
			return subsetMitm(target);
		}
		if (target <= 1_000_000_000 && 0 <= min) return subsetBit((int) target);
		return subsetMitm(target);
	}

	/**
	 * ちょうど k 個選択の部分和判定（回転考慮なし）
	 *
	 * @param target 目標和
	 * @param k      選択個数
	 * @return k 個選んで和が target になるか
	 */
	public boolean subset(final long target, final int k) {
		if (size == 1) return target == arr[0];
		if (size <= 30 && (1 << size) < size * k) {
			return subsetRec(target, k, size - 1);
		} else {
			return subsetBit((int) target, k);
		}
	}

	private boolean subsetBit(final int t) {
		final BitSet bs = new BitSet(t + 1);
		bs.set(0);
		if (t >= arr[0]) bs.set(arr[0]);
		for (int i = 1; i < size; i++) {
			final int v = arr[i];
			if (t < v) continue;
			for (int j = bs.previousSetBit(t - v); j >= 0; j = bs.previousSetBit(j - 1)) {
				bs.set(j + v);
			}
			if (bs.get(t)) return true;
		}
		return false;
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

	public boolean subsetBit(final int target, final int k) {
		final int len = target * k;
		final BitSet bs = new BitSet(len + 1);
		if (target >= arr[0]) bs.set(arr[0]);
		for (int i = 1; i < size; i++) {
			final int v = arr[i];
			if (target < v) continue;
			if (v == target) return true;
			if (len < v + target) continue;
			for (int b = bs.previousSetBit(len - v - target); b >= 0; b = bs.previousSetBit(b - 1)) {
				int r = b % size;
				if ((r + v <= target && r != 0) || (r == 0 && v == 0)) bs.set(b + target + v);
			}
			bs.set(v);
			if (bs.get(len)) return true;
		}
		return false;
	}

	private int bs(final int[] a, final int len, final int t) {
		int l = 0, r = len - 1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (a[m] > t) r = m - 1;
			else if (a[m] < t) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private int bsUpper(final int[] a, final int len, final int t) {
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

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < size;
			}

			public int nextInt() {
				return get(idx++);
			}
		};
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final PrimitiveIterator.OfInt it = iterator();
		sb.append(it.nextInt());
		while (it.hasNext()) sb.append(' ').append(it.nextInt());
		return sb.toString();
	}
}
