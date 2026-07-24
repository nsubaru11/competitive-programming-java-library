package lib.math.number;

import static java.lang.Math.*;

@SuppressWarnings("unused")
public final class Fraction {
	public long numerator, denominator;

	public Fraction(long numerator, long denominator) {
		if (denominator == 0) throw new ArithmeticException(" / by zero");
		this.numerator = numerator;
		this.denominator = denominator;
		reduction();
	}

	public Fraction(long numerator) {
		this.numerator = numerator;
		this.denominator = 1;
	}

	public Fraction(Fraction f) {
		numerator = f.numerator;
		denominator = f.denominator;
	}

	private static long gcd(long a, long b) {
		a = abs(a);
		b = abs(b);
		if (a == 0) return b;
		if (b == 0) return a;
		int commonShift = Long.numberOfTrailingZeros(a | b);
		a >>= Long.numberOfTrailingZeros(a);
		while (b != 0) {
			b >>= Long.numberOfTrailingZeros(b);
			if (a > b) {
				long tmp = a;
				a = b;
				b = tmp;
			}
			b -= a;
		}
		return a << commonShift;
	}

	private void reduction() {
		long gcd = gcd(numerator, denominator);
		numerator /= gcd;
		denominator /= gcd;
		if (denominator < 0) {
			numerator *= -1L;
			denominator *= -1L;
		}
	}

	public Fraction add(Fraction f) {
		long n = numerator * f.denominator + f.numerator * denominator;
		long d = denominator * f.denominator;
		return new Fraction(n, d);
	}

	public Fraction add(long k) {
		long n = numerator + k * denominator;
		long d = denominator;
		return new Fraction(n, d);
	}

	public Fraction subtract(Fraction f) {
		long n = numerator * f.denominator - f.numerator * denominator;
		long d = denominator * f.denominator;
		return new Fraction(n, d);
	}

	public Fraction subtract(long k) {
		long n = numerator - k * denominator;
		long d = denominator;
		return new Fraction(n, d);
	}

	public Fraction multiply(Fraction f) {
		long n = numerator * f.numerator;
		long d = denominator * f.denominator;
		return new Fraction(n, d);
	}

	public Fraction multiply(long k) {
		long n = numerator * k;
		long d = denominator;
		return new Fraction(n, d);
	}

	public Fraction divide(Fraction f) {
		long n = numerator * f.denominator;
		long d = denominator * f.numerator;
		return new Fraction(n, d);
	}

	public Fraction divide(long k) {
		long n = numerator;
		long d = denominator * k;
		return new Fraction(n, d);
	}

	public void pow(int i) {
		long nmrtr = 1L;
		long dnmntr = 1L;
		while (i > 0) {
			if ((i & 1) == 1) {
				nmrtr *= numerator;
				dnmntr *= denominator;
			}
			numerator *= numerator;
			denominator *= denominator;
			i >>= 1;
		}
		numerator = nmrtr;
		denominator = dnmntr;
	}

	public long getNumerator() {
		return numerator;
	}

	public long getDenominator() {
		return denominator;
	}

	public Fraction getInverse() {
		return new Fraction(denominator, numerator);
	}

	public String getDecimal() {
		double d = (double) numerator / denominator;
		return String.format("%.5f", d);
	}

	public boolean equals(Object o) {
		if (!(o instanceof Fraction f)) return false;
		return (numerator == 0 && f.numerator == 0) || (numerator == f.numerator && denominator == f.denominator);
	}

	public String toString() {
		if (denominator == 1) return Long.toString(numerator);
		return numerator + "/" + denominator;
	}
}
