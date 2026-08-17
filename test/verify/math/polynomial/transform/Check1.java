package verify.math.polynomial.transform;

import java.io.*;

import lib.math.polynomial.*;

// https://cses.fi/problemset/task/1654
public final class Check1 {

	// region < Constants & Globals >
	private static final boolean DEBUG = true;
	private static final int MOD = 998244353;
	// private static final int MOD = 1_000_000_007;
	private static final char[] op = new char[]{'L', 'U', 'R', 'D'};
	private static final int[] di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
	private static final int[] dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
	private static final FastScanner sc = new FastScanner();
	private static final PrintWriter out = new PrintWriter(System.out, false);
	// endregion

	private static void solve() {
		int n = sc.nextInt();
		int[] x = new int[n];
		int mx = 0;
		for (int i = 0; i < n; i++) {
			x[i] = sc.nextInt();
			if (x[i] > mx) mx = x[i];
		}
		int k = Integer.highestOneBit(mx) << 1;
		int[] multiset = new int[k];
		for (int i = 0; i < n; i++) multiset[x[i]]++;
		long[] a = Transform.subsetZeta(multiset);
		long[] b = Transform.supersetZeta(multiset);
		StringBuilder sb = new StringBuilder(1 << 22);
		for (int i = 0; i < n; i++) {
			sb.append(a[x[i]]).append(' ').append(b[x[i]]).append(' ').append(n - (a[x[i] ^ (k - 1)])).append('\n');
		}
		out.println(sb);
	}

	public static void main(final String[] args) {
		try {
			solve();
		} finally {
			out.flush();
			out.close();
		}
	}

	private static final class FastScanner {
		private final InputStream in = System.in;
		private final byte[] buffer = new byte[1024];
		private int head = 0, tail = 0;

		private int read() {
			if (head >= tail) {
				head = 0;
				try {
					tail = in.read(buffer, 0, buffer.length);
				} catch (IOException _) {
				}
				if (tail <= 0) return -1;
			}
			return buffer[head++];
		}

		public int nextInt() {
			int c = read();
			while (c <= 32) c = read();
			int res = 0;
			while (c > 32) {
				res = res * 10 + c - '0';
				c = read();
			}
			return res;
		}
	}
}
