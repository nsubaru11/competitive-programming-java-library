import java.io.IOException;
import java.io.InputStream;

public class TestInputDoubleNumbers {

	public static void main(String[] args) {
		int len = args.length;
		final int which = len >= 1 ? Integer.parseInt(args[0]) : 0;
		final int buf = len >= 2 ? Integer.parseInt(args[1]) : 65536;
		switch (which) {
			case 0 -> {
				double ms = benchFastScannerFrom(0, buf);
				System.out.printf("nextDouble0: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 1 -> {
				double ms = benchFastScannerFrom(1, buf);
				System.out.printf("nextDouble1: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 2 -> {
				double ms = benchFastScannerFrom(2, buf);
				System.out.printf("nextDouble2: %.3f ms (buf=%d)%n", ms, buf);
			}
		}
	}

	private static double benchFastScannerFrom(final int type, final int bufferSize) {
		try (final FastScanner sc = new FastScanner(bufferSize)) {
			final long t0 = System.nanoTime();
			boolean checksum = false;
			switch (type) {
				case 0 -> checksum = consume0(sc);
				case 1 -> checksum = consume1(sc);
				case 2 -> checksum = consume2(sc);
			}
			final long t1 = System.nanoTime();
			assert checksum;
			return (t1 - t0) / 1_000_000.0;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static boolean consume0(final FastScanner sc) {
		int n = (int) sc.nextDouble0();
		double sum = 0;
		while (n-- > 0) sum += sc.nextDouble0();
		double checksum = sc.nextDouble0();
		return sum == checksum;
	}

	private static boolean consume1(final FastScanner sc) {
		int n = (int) sc.nextDouble1();
		double sum = 0;
		while (n-- > 0) sum += sc.nextDouble1();
		double checksum = sc.nextDouble1();
		return sum == checksum;
	}

	private static boolean consume2(final FastScanner sc) {
		int n = (int) sc.nextDouble2();
		double sum = 0;
		while (n-- > 0) sum += sc.nextDouble2();
		double checksum = sc.nextDouble2();
		return sum == checksum;
	}

	@SuppressWarnings("unused")
	private static final class FastScanner implements AutoCloseable {
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private final InputStream in;
		private final byte[] buffer;
		private int pos = 0, bufferLength = 0;

		public FastScanner() {
			this(System.in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final InputStream in) {
			this(in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final int bufferSize) {
			this(System.in, bufferSize);
		}

		public FastScanner(final InputStream in, final int bufferSize) {
			this.in = in;
			this.buffer = new byte[bufferSize];
		}

		private int skipSpaces() {
			int b = read();
			while (b <= 32) b = read();
			return b;
		}

		@Override
		public void close() throws IOException {
			if (in != System.in) in.close();
		}

		public byte read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength < 0)
					throw new RuntimeException(new IOException("End of input reached"));
			}
			return buffer[pos++];
		}

		public double nextDouble0() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			double result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + (b & 15);
				b = read();
			}
			if (b == '.') {
				b = read();
				long f = 0, d = 1;
				while ('0' <= b && b <= '9') {
					f = f * 10 + b - '0';
					d *= 10;
					b = read();
				}
				result += (double) f / d;
			}
			return negative ? -result : result;
		}

		public double nextDouble1() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
			} else {
				negative = true;
				b = read();
			}
			double result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + (b & 15);
				b = read();
			}
			if (b == '.') {
				b = read();
				double scale = 0.1;
				while ('0' <= b && b <= '9') {
					result += (b & 15) * scale;
					scale *= 0.1;
					b = read();
				}
			}
			return negative ? -result : result;
		}

		public double nextDouble2() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
			} else {
				negative = true;
				b = read();
			}
			double result = 0;
			while ('0' <= b && b <= '9') {
				result = ((long) result << 3) + ((long) result << 1) + (b & 15);
				b = read();
			}
			if (b == '.') {
				b = read();
				double scale = 0.1;
				while ('0' <= b && b <= '9') {
					result += (b & 15) * scale;
					scale *= 0.1;
					b = read();
				}
			}
			return negative ? -result : result;
		}
	}
}
