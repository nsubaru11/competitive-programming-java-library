import java.io.IOException;
import java.io.InputStream;

public class TestInputLongNumbers {

	public static void main(String[] args) {
		int len = args.length;
		final int which = len >= 1 ? Integer.parseInt(args[0]) : 0;
		final int buf = len >= 2 ? Integer.parseInt(args[1]) : 65536;
		switch (which) {
			case 0 -> {
				double ms = benchFastScannerFrom(0, buf);
				System.out.printf("nextLong0: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 1 -> {
				double ms = benchFastScannerFrom(1, buf);
				System.out.printf("nextLong1: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 2 -> {
				double ms = benchFastScannerFrom(2, buf);
				System.out.printf("nextLong2: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 3 -> {
				double ms = benchFastScannerFrom(3, buf);
				System.out.printf("nextLong3: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 4 -> {
				double ms = benchFastScannerFrom(4, buf);
				System.out.printf("nextLong4: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 5 -> {
				double ms = benchFastScannerFrom(5, buf);
				System.out.printf("nextLong5: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 6 -> {
				double ms = benchFastScannerFrom(6, buf);
				System.out.printf("nextLong6: %.3f ms (buf=%d)%n", ms, buf);
			}
			case 7 -> {
				double ms = benchFastScannerFrom(7, buf);
				System.out.printf("nextLong7: %.3f ms (buf=%d)%n", ms, buf);
			}
		}
	}

	private static double benchFastScannerFrom(final int type, final int bufferSize) {
		try (final FastScanner sc = new FastScanner(System.in, bufferSize)) {
			final long t0 = System.nanoTime();
			boolean checksum = false;
			switch (type) {
				case 0 -> checksum = consume0(sc);
				case 1 -> checksum = consume1(sc);
				case 2 -> checksum = consume2(sc);
				case 3 -> checksum = consume3(sc);
				case 4 -> checksum = consume4(sc);
				case 5 -> checksum = consume5(sc);
				case 6 -> checksum = consume6(sc);
				case 7 -> checksum = consume7(sc);
			}
			assert checksum;
			final long t1 = System.nanoTime();
			return (t1 - t0) / 1_000_000.0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static boolean consume0(final FastScanner sc) {
		int n = (int) sc.nextLong0();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong0();
		long checksum = sc.nextLong0();
		return sum == checksum;
	}

	private static boolean consume1(final FastScanner sc) {
		int n = (int) sc.nextLong1();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong1();
		long checksum = sc.nextLong1();
		return sum == checksum;
	}

	private static boolean consume2(final FastScanner sc) {
		int n = (int) sc.nextLong2();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong2();
		long checksum = sc.nextLong2();
		return sum == checksum;
	}

	private static boolean consume3(final FastScanner sc) {
		int n = (int) sc.nextLong3();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong3();
		long checksum = sc.nextLong3();
		return sum == checksum;
	}

	private static boolean consume4(final FastScanner sc) {
		int n = (int) sc.nextLong4();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong4();
		long checksum = sc.nextLong4();
		return sum == checksum;
	}

	private static boolean consume5(final FastScanner sc) {
		int n = (int) sc.nextLong5();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong5();
		long checksum = sc.nextLong5();
		return sum == checksum;
	}

	private static boolean consume6(final FastScanner sc) {
		int n = (int) sc.nextLong6();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong6();
		long checksum = sc.nextLong6();
		return sum == checksum;
	}

	private static boolean consume7(final FastScanner sc) {
		int n = (int) sc.nextLong7();
		long sum = 0;
		while (n-- > 0) sum += sc.nextLong7();
		long checksum = sc.nextLong7();
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

		private static boolean isWhitespace(final int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t';
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

		public long nextLong0() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong1() {
			int b = skipSpaces();
			boolean negative = b == '-';
			if (negative) b = read();
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong2() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = b == 45;
			if (negative) b = read();
			long result = 0;
			while (48 <= b && b <= 57) {
				result = result * 10 + b - 48;
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong3() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong4() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong5() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = false;
			if (b != '-') {
			} else {
				negative = true;
				b = read();
			}
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong6() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong7() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}
	}
}
