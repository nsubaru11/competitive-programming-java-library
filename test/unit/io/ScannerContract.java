package unit.io;

import java.math.*;
import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FastScanner・InteractiveScanner（Java 24 版と Java 17 互換版）に共通する振る舞いのテスト。
 * 4クラスは同じ名前のメソッドを持つため、{@link Adapter#call} で呼び出す。期待値は FastScannerGuide の記載に従う。
 */
public abstract class ScannerContract {

	/**
	 * input を読むスキャナを作る。
	 */
	protected abstract Object create(String input);

	/**
	 * java.util.function の述語を、そのクラスの CharPredicate へ変換する。
	 */
	protected abstract Object charPredicate(Predicate<Character> p);

	<R> R call(final Class<R> type, final Object sc, final String name, final Object... args) {
		return Adapter.call(type, sc, name, args);
	}

	long nextLong(final Object sc) {
		return call(Long.class, sc, "nextLong");
	}

	@Nested
	class Primitives {

		@Test
		void numbersCharsAndTokens() {
			final Object sc = create("  12 -34\n5000000000 -900000000000000000\t3.25 -0.5 x hello world\n");
			assertEquals(12, (int) call(Integer.class, sc, "nextInt"));
			assertEquals(-34, (int) call(Integer.class, sc, "nextInt"));
			assertEquals(5000000000L, nextLong(sc));
			assertEquals(-900000000000000000L, nextLong(sc));
			assertEquals(3.25, call(Double.class, sc, "nextDouble"), 1e-12);
			assertEquals(-0.5, call(Double.class, sc, "nextDouble"), 1e-12);
			assertEquals('x', (char) call(Character.class, sc, "nextChar"));
			assertEquals("hello", call(String.class, sc, "next"));
			assertEquals("world", call(StringBuilder.class, sc, "nextStringBuilder").toString());
			assertFalse(call(Boolean.class, sc, "hasNext"));
		}

		@Test
		void zeroBasedValues() {
			final Object sc = create("5 7");
			assertEquals(4, (int) call(Integer.class, sc, "nextInt0"));
			assertEquals(6L, (long) call(Long.class, sc, "nextLong0"));
		}

		@Test
		void bigNumbers() {
			final Object sc = create("123456789012345678901234567890 -3.14159265358979323846");
			assertEquals(new BigInteger("123456789012345678901234567890"), call(BigInteger.class, sc, "nextBigInteger"));
			assertEquals(new BigDecimal("-3.14159265358979323846"), call(BigDecimal.class, sc, "nextBigDecimal"));
		}

		@Test
		void linesWithoutLineBreaks() {
			final Object sc = create("abc\nline one\r\nline two\nlast");
			assertEquals("abc", call(String.class, sc, "next"));
			assertEquals("line one", call(String.class, sc, "nextLine"), "CRLF も改行として扱い、改行文字は含めない");
			assertEquals("line two", call(String.class, sc, "nextLine"));
			assertEquals("last", call(String.class, sc, "nextLine"));
		}

		@Test
		void booleans() {
			final Object sc = create("true false a 3 3 7 x 5");
			assertTrue(call(Boolean.class, sc, "nextBoolean"));
			assertFalse(call(Boolean.class, sc, "nextBoolean"));
			assertTrue(call(Boolean.class, sc, "nextBoolean", 'a'));
			assertTrue(call(Boolean.class, sc, "nextBoolean", 3));
			assertFalse(call(Boolean.class, sc, "nextBoolean", 4L));
			assertTrue(call(Boolean.class, sc, "nextBoolean", (IntPredicate) v -> v > 5));
			assertTrue(call(Boolean.class, sc, "nextBoolean", charPredicate(c -> c == 'x')));
			assertFalse(call(Boolean.class, sc, "nextBoolean", (LongPredicate) v -> v > 5));
		}

		@Test
		void hasNext() {
			final Object sc = create("  a b \n ");
			assertTrue(call(Boolean.class, sc, "hasNext"));
			assertEquals('a', (char) call(Character.class, sc, "nextChar"));
			assertEquals("b", call(String.class, sc, "next"));
			assertFalse(call(Boolean.class, sc, "hasNext"));
		}
	}

	@Nested
	class Arrays1D {

		@Test
		void primitiveAndStringArrays() {
			final Object sc = create("1 2 3\n4 5\n1.5 2.5\nabc xyz\nhello 4 2 9 3\n");
			assertArrayEquals(new int[]{1, 2, 3}, call(int[].class, sc, "nextInt", 3));
			assertArrayEquals(new long[]{4, 5}, call(long[].class, sc, "nextLong", 2));
			assertArrayEquals(new double[]{1.5, 2.5}, call(double[].class, sc, "nextDouble", 2), 1e-12);
			assertArrayEquals("abc".toCharArray(), call(char[].class, sc, "nextChars"));
			assertArrayEquals("xy".toCharArray(), call(char[].class, sc, "nextChars", 2), "非空白文字を n 個");
			assertArrayEquals(new String[]{"z", "hello"}, call(String[].class, sc, "nextStrings", 2));
			assertArrayEquals(new int[]{3, 1}, call(int[].class, sc, "nextInt0", 2));
			assertArrayEquals(new long[]{8, 2}, call(long[].class, sc, "nextLong0", 2));
		}

		@Test
		void sortedArrays() {
			final Object sc = create("3 1 2 30 10 20 2.5 1.5 dcba zx ab aa b");
			assertArrayEquals(new int[]{1, 2, 3}, call(int[].class, sc, "nextSortedInt", 3));
			assertArrayEquals(new long[]{10, 20, 30}, call(long[].class, sc, "nextSortedLong", 3));
			assertArrayEquals(new double[]{1.5, 2.5}, call(double[].class, sc, "nextSortedDouble", 2), 1e-12);
			assertArrayEquals("abcd".toCharArray(), call(char[].class, sc, "nextSortedChars"));
			assertArrayEquals("xz".toCharArray(), call(char[].class, sc, "nextSortedChars", 2));
			assertArrayEquals(new String[]{"aa", "ab", "b"}, call(String[].class, sc, "nextSortedStrings", 3));
		}

		@Test
		void prefixSumsAndInverseMapping() {
			final Object sc = create("1 2 3 4 5 6\n3 1 2");
			assertArrayEquals(new int[]{1, 3, 6}, call(int[].class, sc, "nextIntPrefixSum", 3));
			assertArrayEquals(new long[]{4, 9, 15}, call(long[].class, sc, "nextLongPrefixSum", 3));
			// 1-indexed の値 3 1 2 の逆写像
			assertArrayEquals(new int[]{1, 2, 0}, call(int[].class, sc, "nextIntInverseMapping", 3));
		}

		@Test
		void booleanArrays() {
			final Object sc = create("a b a 1 2 1 5 6 x y 1 9 7 8");
			assertArrayEquals(new boolean[]{true, false, true}, call(boolean[].class, sc, "nextBoolean", 3, 'a'));
			assertArrayEquals(new boolean[]{true, false, true}, call(boolean[].class, sc, "nextBoolean", 3, 1));
			assertArrayEquals(new boolean[]{false, true}, call(boolean[].class, sc, "nextBoolean", 2, 6L));
			assertArrayEquals(new boolean[]{true, false}, call(boolean[].class, sc, "nextBoolean", 2, charPredicate(c -> c == 'x')));
			assertArrayEquals(new boolean[]{false, true}, call(boolean[].class, sc, "nextBoolean", 2, (IntPredicate) v -> v > 5));
			assertArrayEquals(new boolean[]{true, true}, call(boolean[].class, sc, "nextBoolean", 2, (LongPredicate) v -> v > 5));
		}
	}

	@Nested
	class Matrices {

		@Test
		void matricesAndTransposes() {
			final String m = "1 2 3\n4 5 6\n";
			assertArrayEquals(new int[][]{{1, 2, 3}, {4, 5, 6}}, call(int[][].class, create(m), "nextIntMat", 2, 3));
			assertArrayEquals(new int[][]{{0, 1, 2}, {3, 4, 5}}, call(int[][].class, create(m), "nextIntMat0", 2, 3));
			assertArrayEquals(new int[][]{{1, 4}, {2, 5}, {3, 6}}, call(int[][].class, create(m), "nextIntMatInv", 2, 3), "転置した w × h");
			assertArrayEquals(new int[][]{{0, 3}, {1, 4}, {2, 5}}, call(int[][].class, create(m), "nextIntMatInv0", 2, 3));
			assertArrayEquals(new long[][]{{1, 2, 3}, {4, 5, 6}}, call(long[][].class, create(m), "nextLongMat", 2, 3));
			assertArrayEquals(new long[][]{{0, 1, 2}, {3, 4, 5}}, call(long[][].class, create(m), "nextLongMat0", 2, 3));
			assertArrayEquals(new long[][]{{1, 4}, {2, 5}, {3, 6}}, call(long[][].class, create(m), "nextLongMatInv", 2, 3));
			assertArrayEquals(new long[][]{{0, 3}, {1, 4}, {2, 5}}, call(long[][].class, create(m), "nextLongMatInv0", 2, 3));
			assertArrayEquals(new double[]{4, 5, 6}, call(double[][].class, create(m), "nextDoubleMat", 2, 3)[1], 1e-12);
			assertArrayEquals(new String[][]{{"1", "2", "3"}, {"4", "5", "6"}}, call(String[][].class, create(m), "nextStringMat", 2, 3));
		}

		@Test
		void charAndBooleanMatrices() {
			final String grid = "#.#\n..#\n";
			assertArrayEquals(new char[][]{"#.#".toCharArray(), "..#".toCharArray()}, call(char[][].class, create(grid), "nextCharMat", 2));
			assertArrayEquals(new char[][]{"#.#".toCharArray(), "..#".toCharArray()}, call(char[][].class, create(grid), "nextCharMat", 2, 3));
			final boolean[][] expected = {{true, false, true}, {false, false, true}};
			assertArrayEquals(expected, call(boolean[][].class, create(grid), "nextBooleanMat", 2, 3, '#'));
			assertArrayEquals(expected, call(boolean[][].class, create(grid), "nextBooleanMat", 2, 3, charPredicate(c -> c == '#')));
			assertArrayEquals(expected, call(boolean[][].class, create("1 0 1 0 0 1"), "nextBooleanMat", 2, 3, 1));
			assertArrayEquals(expected, call(boolean[][].class, create("1 0 1 0 0 1"), "nextBooleanMat", 2, 3, 1L));
			assertArrayEquals(expected, call(boolean[][].class, create("9 0 9 0 0 9"), "nextBooleanMat", 2, 3, (IntPredicate) v -> v > 5));
			assertArrayEquals(expected, call(boolean[][].class, create("9 0 9 0 0 9"), "nextBooleanMat", 2, 3, (LongPredicate) v -> v > 5));
		}

		@Test
		void threeDimensional() {
			final String in = "1 2 3 4 5 6 7 8";
			final int[][][] expected = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
			assertArrayEquals(expected, call(int[][][].class, create(in), "nextInt3D", 2, 2, 2));
			assertEquals(6, call(int[][][].class, create(in), "nextInt3D0", 2, 2, 2)[1][1][0]);
			assertEquals(8L, call(long[][][].class, create(in), "nextLong3D", 2, 2, 2)[1][1][1]);
			assertEquals(0L, call(long[][][].class, create(in), "nextLong3D0", 2, 2, 2)[0][0][0]);
		}

		@Test
		void multiDimensionalPrefixSums() {
			// 2次元・3次元は先頭に0の行・列を持つ (h + 1) × (w + 1) の配列
			final int[][] p2 = call(int[][].class, create("1 2 3 4"), "nextIntPrefixSum", 2, 2);
			assertArrayEquals(new int[][]{{0, 0, 0}, {0, 1, 3}, {0, 4, 10}}, p2);
			assertEquals(10L, call(long[][].class, create("1 2 3 4"), "nextLongPrefixSum", 2, 2)[2][2]);
			final int[][][] p3 = call(int[][][].class, create("1 2 3 4 5 6 7 8"), "nextIntPrefixSum", 2, 2, 2);
			assertEquals(36, p3[2][2][2]);
			assertEquals(1, p3[1][1][1]);
			assertEquals(36L, call(long[][][].class, create("1 2 3 4 5 6 7 8"), "nextLongPrefixSum", 2, 2, 2)[2][2][2]);
		}
	}

	@Nested
	class Collections {

		@Test
		void listsAndSets() {
			assertEquals(List.of(3, 1, 3), call(ArrayList.class, create("3 1 3"), "nextIntAL", 3));
			assertEquals(Set.of(3, 1), call(HashSet.class, create("3 1 3"), "nextIntHS", 3));
			assertEquals(List.of(1, 3), new ArrayList<>(call(TreeSet.class, create("3 1 3"), "nextIntTS", 3)));
			assertEquals(List.of(3L, 1L, 3L), call(ArrayList.class, create("3 1 3"), "nextLongAL", 3));
			assertEquals(Set.of(3L, 1L), call(HashSet.class, create("3 1 3"), "nextLongHS", 3));
			assertEquals(List.of(1L, 3L), new ArrayList<>(call(TreeSet.class, create("3 1 3"), "nextLongTS", 3)));
			assertEquals(List.of('b', 'a', 'b'), call(ArrayList.class, create("b a b"), "nextCharacterAL", 3));
			assertEquals(Set.of('a', 'b'), call(HashSet.class, create("b a b"), "nextCharacterHS", 3));
			assertEquals(List.of('a', 'b'), new ArrayList<>(call(TreeSet.class, create("b a b"), "nextCharacterTS", 3)));
			assertEquals(List.of("y", "x", "y"), call(ArrayList.class, create("y x y"), "nextStringAL", 3));
			assertEquals(Set.of("x", "y"), call(HashSet.class, create("y x y"), "nextStringHS", 3));
			assertEquals(List.of("x", "y"), new ArrayList<>(call(TreeSet.class, create("y x y"), "nextStringTS", 3)));
		}

		@Test
		void multisets() {
			assertEquals(Map.of(3, 2, 1, 1), call(HashMap.class, create("3 1 3"), "nextIntMultisetHM", 3));
			assertEquals(Map.of(3, 2, 1, 1), call(TreeMap.class, create("3 1 3"), "nextIntMultisetTM", 3));
			assertEquals(Map.of(3L, 2, 1L, 1), call(HashMap.class, create("3 1 3"), "nextLongMultisetHM", 3));
			assertEquals(Map.of(3L, 2, 1L, 1), call(TreeMap.class, create("3 1 3"), "nextLongMultisetTM", 3));
			assertEquals(Map.of('b', 2, 'a', 1), call(HashMap.class, create("b a b"), "nextCharMultisetHM", 3));
			assertEquals(Map.of('b', 2, 'a', 1), call(TreeMap.class, create("b a b"), "nextCharMultisetTM", 3));
			assertEquals(Map.of("y", 2, "x", 1), call(HashMap.class, create("y x y"), "nextStringMultisetHM", 3));
			assertEquals(Map.of("y", 2, "x", 1), call(TreeMap.class, create("y x y"), "nextStringMultisetTM", 3));
			assertArrayEquals(new int[]{1, 0, 2, 0}, call(int[].class, create("0 2 2"), "nextIntMultiset", 3, 4), "値域 [0, m)");
			assertArrayEquals(new int[]{0, 2, 1}, call(int[].class, create("2 3 2"), "nextIntMultiset0", 3, 3), "値域 [1, m] を 0-indexed にする");
			final int[] upper = call(int[].class, create("A Z A"), "nextUpperCharMultiset", 3);
			assertEquals(26, upper.length);
			assertEquals(2, upper[0]);
			assertEquals(1, upper[25]);
			final int[] lower = call(int[].class, create("b b z"), "nextLowerCharMultiset", 3);
			assertEquals(2, lower[1]);
			assertArrayEquals(new int[]{1, 0, 2}, call(int[].class, create("c e e"), "nextCharMultiset", 3, 'c', 'e'), "閉区間 [l, r]");
		}
	}

	@RepeatedTest(30)
	void numbersMatchNaive(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final List<Long> values = new ArrayList<>();
		final StringBuilder sb = new StringBuilder();
		final String[] spaces = {" ", "\n", "  ", "\r\n", "\t"};
		for (int i = random.nextInt(1, 300); i > 0; i--) {
			final long v = random.nextBoolean() ? random.nextInt() : random.nextLong(-1_000_000_000_000_000_000L, 1_000_000_000_000_000_000L);
			values.add(v);
			sb.append(v).append(spaces[random.nextInt(spaces.length)]);
		}
		final Object sc = create(sb.toString());
		for (final long v : values) {
			if (v == (int) v && random.nextBoolean()) assertEquals((int) v, (int) call(Integer.class, sc, "nextInt"), "seed=" + info.getCurrentRepetition());
			else assertEquals(v, nextLong(sc), "seed=" + info.getCurrentRepetition());
		}
		assertFalse(call(Boolean.class, sc, "hasNext"));
	}
}
