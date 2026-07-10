package lib.ds;

@SuppressWarnings("unused")
public final class Int128 extends Number implements Comparable<Int128> {
	private long hi, lo;

	public Int128() {
		this(0, 0);
	}

	public Int128(long lo) {
		this(0, lo);
	}

	public Int128(long hi, long lo) {
		this.hi = hi;
		this.lo = lo;
	}

	public Int128(String s) {
		int len = s.length();
		if (len == 0) throw new NumberFormatException();
		boolean neg = s.charAt(0) == '-';
		int i = neg ? 1 : 0;
		for (; i < len; i++) {
			char c = s.charAt(i);
			int d = c - '0';
			long al = lo, ah = hi, pl = al * 10;
			lo = pl + d;
			hi = ah * 10 + unsignedMultiplyHigh(al, 10) + (Long.compareUnsigned(lo, pl) < 0 ? 1 : 0);
		}
		if (neg) {
			long old = lo;
			lo = -lo;
			hi = ~hi + (old == 0 ? 1 : 0);
		}
	}

	public static Int128 of(long lo) {
		return new Int128(lo);
	}

	public static Int128 of(long hi, long lo) {
		return new Int128(hi, lo);
	}

	public static Int128 of(String s) {
		return new Int128(s);
	}

	private static int bitLength(long hi, long lo) {
		if (hi != 0) return 128 - Long.numberOfLeadingZeros(hi);
		return lo == 0 ? 0 : 64 - Long.numberOfLeadingZeros(lo);
	}

	private static int compareUnsigned(long ah, long al, long bh, long bl) {
		int c = Long.compareUnsigned(ah, bh);
		return c != 0 ? c : Long.compareUnsigned(al, bl);
	}

	private static long unsignedMultiplyHigh(long x, long y) {
		long x0 = x & 0xffffffffL, x1 = x >>> 32;
		long y0 = y & 0xffffffffL, y1 = y >>> 32;

		long z2 = x0 * y0;
		long t = x1 * y0 + (z2 >>> 32);
		long z1 = t & 0xffffffffL;
		long z0 = t >>> 32;
		z1 += x0 * y1;

		return x1 * y1 + z0 + (z1 >>> 32);
	}

	private static double toUnsignedDouble(long x) {
		if (x >= 0) return (double) x;
		return (double) (x & Long.MAX_VALUE) + 0x1.0p63;
	}

	private static long[] divRemUnsigned(long hi, long lo) {
		long qh = 0, ql = 0, rem = 0;
		for (int i = 127; i >= 0; i--) {
			rem <<= 1;
			if (i >= 64) rem |= (hi >>> (i - 64)) & 1L;
			else rem |= (lo >>> i) & 1L;
			if (Long.compareUnsigned(rem, 1000000000000000000L) >= 0) {
				rem -= 1000000000000000000L;
				if (i >= 64) qh |= 1L << (i - 64);
				else ql |= 1L << i;
			}
		}
		return new long[]{qh, ql, rem};
	}

	public Int128 add(Int128 o) {
		long old = lo;
		lo = lo + o.lo;
		hi = hi + o.hi + (Long.compareUnsigned(lo, old) < 0 ? 1 : 0);
		return this;
	}

	public Int128 sub(Int128 o) {
		long old = lo;
		lo = lo - o.lo;
		hi = hi - o.hi - (Long.compareUnsigned(old, o.lo) < 0 ? 1 : 0);
		return this;
	}

	public Int128 mul(Int128 o) {
		long al = lo, ah = hi;
		long bl = o.lo, bh = o.hi;
		lo = al * bl;
		hi = ah * bl + al * bh + unsignedMultiplyHigh(al, bl);
		return this;
	}

	public Int128 div(Int128 o) {
		if (o.isZero()) throw new ArithmeticException();
		boolean neg = (hi < 0) ^ (o.hi < 0);
		long al = lo, ah = hi;
		if (ah < 0) {
			long old = al;
			al = -al;
			ah = ~ah + (Long.compareUnsigned(old, 0) == 0 ? 1 : 0);
		}
		long bl = o.lo, bh = o.hi;
		if (bh < 0) {
			long old = bl;
			bl = -bl;
			bh = ~bh + (Long.compareUnsigned(old, 0) == 0 ? 1 : 0);
		}
		if (compareUnsigned(ah, al, bh, bl) < 0) {
			hi = 0;
			lo = 0;
			return this;
		}

		int shift = bitLength(ah, al) - bitLength(bh, bl);

		if (shift >= 64) {
			bh = bl << (shift - 64);
			bl = 0;
		} else if (shift > 0) {
			bh = (bh << shift) | (bl >>> (64 - shift));
			bl <<= shift;
		}

		long rl = al, rh = ah;
		long ql = 0, qh = 0;

		for (int i = shift; i >= 0; i--) {

			if (compareUnsigned(rh, rl, bh, bl) >= 0) {
				long old = rl;
				rl -= bl;
				rh -= bh + (Long.compareUnsigned(old, bl) < 0 ? 1 : 0);

				if (i >= 64) qh |= 1L << (i - 64);
				else ql |= 1L << i;
			}

			bl = (bl >>> 1) | (bh << 63);
			bh >>>= 1;
		}

		hi = qh;
		lo = ql;

		if (neg) {
			long old = lo;
			lo = -lo;
			hi = ~hi + (Long.compareUnsigned(old, 0) == 0 ? 1 : 0);
		}

		return this;
	}

	public boolean getBit(int i) {
		if (i < 0 || i >= 128) throw new IndexOutOfBoundsException();
		if (i >= 64) return (hi >>> (i - 64) & 1) == 1;
		return (lo >>> i & 1) == 1;
	}

	public boolean isZero() {
		return lo == 0 && hi == 0;
	}

	public boolean isNegative() {
		return hi < 0;
	}

	public String toString() {
		if (hi == 0 && lo == 0) return "0";
		boolean neg = hi < 0;
		long ah = hi, al = lo;
		if (neg) {
			long old = al;
			al = -al;
			ah = ~ah + (old == 0 ? 1 : 0);
		}
		final long base = 1_000_000_000_000_000_000L;
		String[] parts = new String[8];
		int m = 0;
		while (ah != 0 || al != 0) {
			long[] qr = divRemUnsigned(ah, al);
			ah = qr[0];
			al = qr[1];
			parts[m++] = Long.toString(qr[2]);
		}
		StringBuilder sb = new StringBuilder();
		if (neg) sb.append('-');
		sb.append(parts[m - 1]);
		for (int k = m - 2; k >= 0; k--) {
			String block = parts[k];
			sb.repeat("0", Math.max(0, 18 - block.length()));
			sb.append(block);
		}
		return sb.toString();
	}

	public int intValue() {
		return (int) lo;
	}

	public long longValue() {
		return lo;
	}

	public float floatValue() {
		return (float) doubleValue();
	}

	public double doubleValue() {
		return (double) hi * 0x1.0p64 + toUnsignedDouble(lo);
	}

	public int compareTo(Int128 o) {
		int c = Long.compare(hi, o.hi);
		return c != 0 ? c : Long.compareUnsigned(lo, o.lo);
	}
}
