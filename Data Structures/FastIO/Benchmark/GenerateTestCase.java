import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.SplittableRandom;

import static java.lang.Math.abs;

public final class GenerateTestCase {
	private static final long DEFAULT_COUNT = 10_000_0000L;
	private static final long DEFAULT_SEED = 123456789L;

	public static void main(String[] args) {
		int type = args.length >= 1 ? Integer.parseInt(args[0]) : 0;
		long seed = args.length >= 2 ? Long.parseLong(args[1]) : DEFAULT_SEED;
		long count = args.length >= 3 ? Long.parseLong(args[2]) : DEFAULT_COUNT;
		String FileName = "TestCase" + switch (type) {
			case 0 -> "_LongNumbers_";
			case 1 -> "_PositiveLongNumbers_";
			case 2 -> "_DoubleNumbers_";
			case 3 -> "_Strings_";
			default -> throw new IllegalStateException("Unexpected value: " + type);
		} + seed + ".txt";
		Path path = Path.of("Data Structures", "FastIO", "Benchmark", "TestCases", FileName);
		try (FastPrinter out = new FastPrinter(new FileOutputStream(path.toFile()))) {
			switch (type) {
				case 0 -> generateLongNumbers(out, seed, count);
				case 1 -> generatePositiveLongNumbers(out, seed, count);
				case 2 -> generateDoubleNumbers(out, seed, count);
				case 3 -> generateStrings(out, seed, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void generateLongNumbers(final FastPrinter out, final long seed, final long count) {
		out.println(count);
		long n = count, sum = 0;
		SplittableRandom rnd = new SplittableRandom(seed);
		while (n-- > 0) {
			long value = rnd.nextLong();
			out.println(value);
			sum += value;
		}
		out.println(sum);
	}

	private static void generatePositiveLongNumbers(final FastPrinter out, final long seed, final long count) {
		out.println(count);
		long n = count, sum = 0;
		SplittableRandom rnd = new SplittableRandom(seed);
		while (n-- > 0) {
			long value = rnd.nextLong();
			out.println(abs(value));
			sum += value;
		}
		out.println(sum);
	}

	private static void generateDoubleNumbers(final FastPrinter out, final long seed, final long count) {
		out.println(count);
		long n = count;
		double sum = 0;
		SplittableRandom rnd = new SplittableRandom(seed);
		while (n-- > 0) {
			double value = rnd.nextDouble(Long.MIN_VALUE, Long.MAX_VALUE);
			out.println(abs(value));
			sum += value;
		}
		out.println(sum);
	}

	private static void generateStrings(final FastPrinter out, final long seed, final long count) {
		out.println(count);
		long n = count;
		SplittableRandom rnd = new SplittableRandom(seed);
		while (n-- > 0) {
			long value = rnd.nextLong();
			out.print(value);
			if (rnd.nextInt(100) == 0) out.println();
		}
	}

}
