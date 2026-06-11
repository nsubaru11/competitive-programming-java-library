import java.io.*;
import java.lang.management.*;

public final class Test2 {
	private static final long PROGRAM_START_NS = System.nanoTime();
	private static final FastScanner2 sc = new FastScanner2();
	private static final long SCANNER_READY_NS = System.nanoTime();
	private static final int DEFAULT_N = 10_000_000;
	private static final int STRING_LEN = 100_000;
	private static final int DEFAULT_STRING_PRINT_COUNT = 256;
	private static final String S0 = "a".repeat(STRING_LEN);
	private static final String S1 = "b".repeat(STRING_LEN);

	private static long benchPrintlnInt(final FastPrinter2 out, final int n) {
		long x = 0x1234ABCDL;
		final long t = System.nanoTime();
		for (int i = 0; i < n; i++) {
			x = x * 1664525L + 1013904223L;
			out.println((int) x);
		}
		out.flush();
		return System.nanoTime() - t;
	}

	private static long benchPrintlnLong(final FastPrinter2 out, final int n) {
		long x = 0x123456789ABCDEFL;
		final long t = System.nanoTime();
		for (int i = 0; i < n; i++) {
			x = x * 6364136223846793005L + 1442695040888963407L;
			out.println(x);
		}
		out.flush();
		return System.nanoTime() - t;
	}

	private static long benchPrintlnString(final FastPrinter2 out, final int n) {
		final long t = System.nanoTime();
		for (int i = 0; i < n; i++) {
			out.println((i & 1) == 0 ? S0 : S1);
		}
		out.flush();
		return System.nanoTime() - t;
	}

	private static long benchPrintlnBoolean(final FastPrinter2 out, final int n) {
		final long t = System.nanoTime();
		for (int i = 0; i < n; i++) {
			out.println((i & 1) == 0);
		}
		out.flush();
		return System.nanoTime() - t;
	}

	private static long testNextInt(final int n) {
		int m = n;
		long sum = 0;
		while (m-- > 0) {
			sum += sc.nextInt();
		}
		return sum;
	}

	private static long testNextLong(final int n) {
		int m = n;
		long sum = 0;
		while (m-- > 0) {
			sum += sc.nextLong();
		}
		return sum;
	}

	private static long testNextString(final int n) {
		int m = n;
		long strHash = 0;
		while (m-- > 0) {
			strHash = (strHash * 1_000_003L) ^ sc.next().hashCode();
		}
		return strHash;
	}

	public static void main(String[] args) {
		// JVMが起動した時刻（ミリ秒）を取得
		long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		// mainメソッドに到達した現在の時刻（ミリ秒）を取得
		long mainStartTime = System.currentTimeMillis();

		// 起動からmain到達までのオーバーヘッドを標準エラー出力へ表示
		System.err.println("JVM Startup to Main: " + (mainStartTime - jvmStartTime) + " ms");

		final long mainStartNs = System.nanoTime();
		final int n = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_N;
		final int pn = args.length > 1 ? Integer.parseInt(args[1]) : n;
		final int psn = args.length > 2 ? Integer.parseInt(args[2]) : Math.min(pn, DEFAULT_STRING_PRINT_COUNT);

		long t = System.nanoTime();
		final long sumInt = testNextInt(n);
		final long nextIntNs = System.nanoTime() - t;

		t = System.nanoTime();
		final long sumLong = testNextLong(n);
		final long nextLongNs = System.nanoTime() - t;

		t = System.nanoTime();
		final long strHash = testNextString(n);
		final long nextNs = System.nanoTime() - t;
		final long scannerInitNs = SCANNER_READY_NS - PROGRAM_START_NS;
		final long nextTotalNs = nextIntNs + nextLongNs + nextNs;
		final long scannerWithNextTotalNs = scannerInitNs + nextTotalNs;

		final FastPrinter2 out = new FastPrinter2(OutputStream.nullOutputStream(), 1 << 20, false);
		final long printlnIntNs = benchPrintlnInt(out, pn);
		final long printlnLongNs = benchPrintlnLong(out, pn);
		final long printlnStringNs = benchPrintlnString(out, psn);
		final long printlnBoolNs = benchPrintlnBoolean(out, pn);
		out.close();
		final long mainTotalNs = System.nanoTime() - mainStartNs;
		final long appTotalNs = System.nanoTime() - PROGRAM_START_NS;

		System.out.println(
				"RESULT,Test2," + n + "," + STRING_LEN + "," + psn + "," + nextIntNs + "," + nextLongNs + "," + nextNs + ","
						+ scannerInitNs + "," + nextTotalNs + "," + scannerWithNextTotalNs + "," + sumInt + "," + sumLong + ","
						+ strHash + "," + printlnIntNs + "," + printlnLongNs + "," + printlnStringNs + "," + printlnBoolNs + ","
						+ mainTotalNs + "," + appTotalNs
		);
	}
}
