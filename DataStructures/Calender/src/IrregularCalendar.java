// 競技プログラミング用カレンダーライブラリ
// 単一ファイル / 各クラス独立 / 単一スレッド前提 / long を基本
//
// 共通モデル:
//   toDays(y,m,d) = beforeYear(y) + beforeMonth(y,m) + (d-1)
//   通し日数の原点は (y=0, m=1, d=1) -> 0。前提として日付・通し日数は非負。
//   月は 1-based、日は 1-based。
//   月軸は「周期 K の日数パターン」で表現:
//     beforeMonth(m) = ((m-1)/K)*blockSum + mpre[(m-1)%K]   (うるう補正は別途)
//     累積和方式は K = 月数 として同じ式に吸収される。
//   うるう年補正は leapMonth(1-based) より後の月に delta 日を加える単一挿入モデル。
//   dow は原点基準の相対値 (0..6)。実暦の曜日が要る場合は既知の1日で校正する。

// ===========================================================================
//  GregCal : グレゴリオ暦固定 (12ヶ月 / 2月にうるう1日)。完全静的・最速。
//  標準 java.time.LocalDate との比較: 正確性・可読性は標準が上だが、
//  本クラスはオブジェクト生成を伴わない long 直計算でホットパス向けに高速。
// ===========================================================================
final class GregCal {
	// CUM[m] = 平年で 1..m 月の累積日数。CUM[m-1] = m 月より前の日数。
	private static final int[] CUM = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

	private GregCal() {}

	static boolean isLeap(long y) {
		return (y & 3) == 0 && (y % 100 != 0 || y % 400 == 0);
	}

	// [0, y) のうるう年数 (year 0 はうるう扱い)。
	private static long leaps(long y) {
		return (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400;
	}

	private static long beforeYear(long y) {
		return y * 365 + leaps(y);
	}

	static long toDays(long y, int m, int d) {
		long days = beforeYear(y) + CUM[m - 1] + (d - 1);
		if (m > 2 && isLeap(y)) days++;
		return days;
	}

	// 通し日数 -> {year, month, day}
	static long[] toDate(long days) {
		long lo = 0, hi = days / 365 + 1;
		while (lo < hi) {
			long mid = (lo + hi + 1) >>> 1;
			if (beforeYear(mid) <= days) lo = mid; else hi = mid - 1;
		}
		long y = lo;
		long r = days - beforeYear(y); // 年内日 (0-based)
		boolean leap = isLeap(y);
		int m = 12;
		while (cumBefore(m, leap) > r) m--;
		int day = (int) (r - cumBefore(m, leap)) + 1;
		return new long[]{y, m, day};
	}

	private static long cumBefore(int m, boolean leap) {
		return CUM[m - 1] + ((leap && m > 2) ? 1 : 0);
	}

	static int dow(long y, int m, int d) {
		long t = toDays(y, m, d) % 7;
		return (int) ((t + 7) % 7);
	}

	static long diff(long y1, int m1, int d1, long y2, int m2, int d2) {
		return toDays(y2, m2, d2) - toDays(y1, m1, d1);
	}

	static long[] add(long y, int m, int d, long n) {
		return toDate(toDays(y, m, d) + n);
	}

	static long nextLeap(long y) {
		long z = y + 1;
		while (!isLeap(z)) z++;
		return z; // 最大ギャップ8年
	}

	static long prevLeap(long y) {
		long z = y - 1;
		while (!isLeap(z)) z--;
		return z;
	}
}

// ===========================================================================
//  FixedCal : n 年に1回うるう年 (y % n == 0)。月軸は周期K。
//  beforeYear は除算1回で O(1)、最速の可変暦。
// ===========================================================================
final class FixedCal {
	private final long n;          // うるう周期(年)
	private final int leapMonth;   // この月より後に delta を加える (1-based)
	private final int delta;       // うるう年に増える日数
	private final int mcnt;        // 1年の月数
	private final int K;           // 月日数パターンの周期
	private final long[] mpre;     // size K+1, パターンの累積和
	private final long blockSum;   // 1周期(K月)の総日数
	private final long commonLen;  // 平年の総日数

	// 累積和方式: 各月の日数を直接指定 (K = 月数)。
	FixedCal(long n, int[] monthLens, int leapMonth, int delta) {
		this(n, monthLens.length, monthLens, leapMonth, delta);
	}

	// 周期方式: pattern が K 月ごとに繰り返す。mcnt は実際の月数。
	FixedCal(long n, int mcnt, int[] pattern, int leapMonth, int delta) {
		this.n = n;
		this.leapMonth = leapMonth;
		this.delta = delta;
		this.mcnt = mcnt;
		this.K = pattern.length;
		this.mpre = new long[K + 1];
		for (int i = 0; i < K; i++) mpre[i + 1] = mpre[i] + pattern[i];
		this.blockSum = mpre[K];
		this.commonLen = bmc(mcnt + 1);
	}

	// 平年での m 月より前の日数 (1-based)。
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
		return y % n == 0;
	}

	private long leaps(long y) {
		return (y + n - 1) / n; // [0,y) のうるう年数
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
		return (y / n + 1) * n;
	}

	long prevLeap(long y) {
		return ((y - 1) / n) * n; // y >= 1 前提
	}
}
