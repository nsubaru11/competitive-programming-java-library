import java.io.IOException;
import java.io.InputStream;

import static java.util.Arrays.setAll;

public class TestInputLongArray {

	public static void main(String[] args) {
		int len = args.length;
		final int which = len >= 1 ? Integer.parseInt(args[0]) : 0;
		final int buf = len >= 2 ? Integer.parseInt(args[1]) : 65536;
		try (FastScanner sc = new FastScanner(buf)) {
			switch (which) {
				case 0 -> {
					double ms = consume0(sc);
					System.out.printf("for: %.3f ms (buf=%d)%n", ms, buf);
				}
				case 1 -> {
					double ms = consume1(sc);
					System.out.printf("setAll: %.3f ms (buf=%d)%n", ms, buf);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static double consume0(final FastScanner sc) {
		final long t0 = System.nanoTime();
		int n = (int) sc.nextLong();
		long[] arr = sc.nextLong0(n);
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
	}

	private static double consume1(final FastScanner sc) {
		final long t0 = System.nanoTime();
		int n = (int) sc.nextLong();
		long[] arr = sc.nextLong1(n);
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
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

		public long nextLong() {
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

		public long[] nextLong0(int n) {
			long[] arr = new long[n];
			for (int i = 0; i < n; i++) arr[i] = nextLong();
			return arr;
		}

		public long[] nextLong1(int n) {
			long[] arr = new long[n];
			setAll(arr, i -> nextLong());
			return arr;
		}
	}
}
