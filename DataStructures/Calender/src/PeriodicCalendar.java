
// ===========================================================================
//  PeriodCal : 周期 P。y % P の余りでうるう年か決まる任意周期規則。
//  1周期分のうるう累積を前計算し beforeYear は O(1)。月軸は周期K。
// ===========================================================================
final class PeriodCal {
	private final int P;           // 年周期
	private final boolean[] mask;  // mask[r] = (年 ≡ r) がうるうか
	private final long[] lpre;     // size P+1, [0,r) のうるう数
	private final long leapPer;    // 1周期のうるう数
	private final int[] leapRems;  // うるう余りの昇順リスト
	private final int leapMonth;
	private final int delta;
	private final int mcnt;
	private final int K;
	private final long[] mpre;
	private final long blockSum;
	private final long commonLen;

	PeriodCal(boolean[] mask, int[] monthLens, int leapMonth, int delta) {
		this(mask, monthLens.length, monthLens, leapMonth, delta);
	}

	PeriodCal(boolean[] mask, int mcnt, int[] pattern, int leapMonth, int delta) {
		this.P = mask.length;
		this.mask = mask;
		this.lpre = new long[P + 1];
		int cnt = 0;
		for (int r = 0; r < P; r++) {
			lpre[r + 1] = lpre[r] + (mask[r] ? 1 : 0);
			if (mask[r]) cnt++;
		}
		this.leapPer = lpre[P];
		this.leapRems = new int[cnt];
		for (int r = 0, j = 0; r < P; r++) if (mask[r]) leapRems[j++] = r;
		this.leapMonth = leapMonth;
		this.delta = delta;
		this.mcnt = mcnt;
		this.K = pattern.length;
		this.mpre = new long[K + 1];
		for (int i = 0; i < K; i++) mpre[i + 1] = mpre[i] + pattern[i];
		this.blockSum = mpre[K];
		this.commonLen = bmc(mcnt + 1);
	}

	private long bmc(int m) {
		long q = (m - 1L) / K;
		int r = (int) ((m - 1L) % K);
		return q * blockSum + mpre[r];
	}

	private long beforeMonth(long y, int m) {
		long v = bmc(m);
		if (m > leapMonth && isLeap(y)) v += delta;
		return v;
	}

	boolean isLeap(long y) {
		return mask[(int) (y % P)];
	}

	private long leaps(long y) {
		return (y / P) * leapPer + lpre[(int) (y % P)];
	}

	private long beforeYear(long y) {
		return y * commonLen + leaps(y) * delta;
	}

	long toDays(long y, int m, int d) {
		return beforeYear(y) + beforeMonth(y, m) + (d - 1);
	}

	long[] toDate(long days) {
		long lo = 0, hi = days / commonLen + 1;
		while (lo < hi) {
			long mid = (lo + hi + 1) >>> 1;
			if (beforeYear(mid) <= days) lo = mid; else hi = mid - 1;
		}
		long y = lo;
		long r = days - beforeYear(y);
		long ml = 1, mh = mcnt;
		while (ml < mh) {
			long mid = (ml + mh + 1) >>> 1;
			if (beforeMonth(y, (int) mid) <= r) ml = mid; else mh = mid - 1;
		}
		int m = (int) ml;
		int day = (int) (r - beforeMonth(y, m)) + 1;
		return new long[]{y, m, day};
	}

	int dow(long y, int m, int d) {
		long t = toDays(y, m, d) % 7;
		return (int) ((t + 7) % 7);
	}

	long diff(long y1, int m1, int d1, long y2, int m2, int d2) {
		return toDays(y2, m2, d2) - toDays(y1, m1, d1);
	}

	long[] add(long y, int m, int d, long k) {
		return toDate(toDays(y, m, d) + k);
	}

	long nextLeap(long y) {
		int r = (int) (y % P);
		long base = y - r;
		int i = upper(r);                 // r より大きい最小のうるう余り
		if (i < leapRems.length) return base + leapRems[i];
		return base + P + leapRems[0];    // 次周期へ
	}

	long prevLeap(long y) {
		int r = (int) (y % P);
		long base = y - r;
		int i = lower(r);                 // r より小さい最大のうるう余り
		if (i >= 0) return base + leapRems[i];
		return base - P + leapRems[leapRems.length - 1];
	}

	// leapRems で r より大きい最小要素の添字 (upper bound)
	private int upper(int r) {
		int lo = 0, hi = leapRems.length;
		while (lo < hi) {
			int mid = (lo + hi) >>> 1;
			if (leapRems[mid] > r) hi = mid; else lo = mid + 1;
		}
		return lo;
	}

	// leapRems で r より小さい最大要素の添字 (なければ -1)
	private int lower(int r) {
		int lo = 0, hi = leapRems.length;
		while (lo < hi) {
			int mid = (lo + hi) >>> 1;
			if (leapRems[mid] < r) lo = mid + 1; else hi = mid;
		}
		return lo - 1;
	}
}