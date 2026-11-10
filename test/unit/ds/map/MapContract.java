package unit.ds.map;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * lib.ds.map のハッシュマップ（キーが1〜3成分、値が int・long）に共通する振る舞いのテスト。
 * キーの成分数によって引数の個数が変わるため、キーは long[] で受け渡し、{@link Adapter#call} で呼び出す。
 */
abstract class MapContract {

	interface Fn1 {
		long apply(long a);
	}

	interface Fn2 {
		long apply(long a, long b);
	}

	interface Fn3 {
		long apply(long a, long b, long c);
	}

	interface Fn4 {
		long apply(long a, long b, long c, long d);
	}

	interface Fn5 {
		long apply(long a, long b, long c, long d, long e);
	}

	interface Consumer2 {
		void accept(long a, long b);
	}

	interface Consumer3 {
		void accept(long a, long b, long c);
	}

	interface Consumer4 {
		void accept(long a, long b, long c, long d);
	}

	/**
	 * キーの成分数。1〜3。
	 */
	abstract int arity();

	abstract Object create();

	abstract Object create(int expectedSize);

	abstract Object create(int expectedSize, long defaultValue);

	abstract Object copy(Object map);

	/**
	 * ランダムな照合で使うキーの1成分。
	 */
	long keyComponent(final Random random) {
		return random.nextInt(-20, 20);
	}

	@Nested
	class Construction {

		@Test
		void defaultValueIsZeroUnlessGiven() {
			assertEquals(0L, call(create(), "getDefaultValue"));
			assertEquals(0L, call(create(4), "getDefaultValue"));
			assertEquals(-1L, call(create(4, -1), "getDefaultValue"));
			assertEquals(-1L, get(create(4, -1), key(5)));
		}

		@Test
		void copyIsIndependent() {
			final Object m = create(2, -1);
			put(m, key(1), 10);
			final Object c = copy(m);
			put(c, key(2), 20);
			put(c, key(1), 11);
			assertEquals(1, size(m));
			assertEquals(10L, get(m, key(1)));
			assertEquals(2, size(c));
			assertEquals(11L, get(c, key(1)));
			assertEquals(-1L, call(c, "getDefaultValue"));
		}
	}

	@Nested
	class State {

		@Test
		void sizeAndIsEmpty() {
			final Object m = create();
			assertTrue((boolean) Adapter.call(Boolean.class, m, "isEmpty"));
			put(m, key(1), 1);
			put(m, key(1), 2);
			put(m, key(2), 3);
			assertEquals(2, size(m));
			assertFalse((boolean) Adapter.call(Boolean.class, m, "isEmpty"));
		}

		@Test
		void setDefaultValueAffectsAbsentKeys() {
			final Object m = create();
			Adapter.call(Void.class, m, "setDefaultValue", 7L);
			assertEquals(7L, call(m, "getDefaultValue"));
			assertEquals(7L, get(m, key(1)));
			assertEquals(8L, call(m, "increment", args(key(1))), "increment は既定値に1を足して挿入する");
		}
	}

	@Nested
	class Access {

		@Test
		void getAbsentDoesNotInsert() {
			final Object m = create(4, -1);
			assertEquals(-1L, get(m, key(3)));
			assertEquals(0, size(m));
			assertFalse(contains(m, key(3)));
		}

		@Test
		void containsAndGetOrDefault() {
			final Object m = create();
			put(m, key(3), 0);
			assertTrue(contains(m, key(3)));
			assertEquals(0L, call(m, "getOrDefault", args(key(3), 9L)));
			assertEquals(9L, call(m, "getOrDefault", args(key(4), 9L)));
		}

		@Test
		void negativeAndDistinctComponents() {
			final Object m = create();
			final long[] a = new long[arity()], b = new long[arity()];
			Arrays.fill(a, -1);
			b[arity() - 1] = -2;
			put(m, a, 1);
			put(m, b, 2);
			assertEquals(1L, get(m, a));
			assertEquals(2L, get(m, b));
		}
	}

	@Nested
	class Updates {

		@Test
		void putAndPutIfAbsentReturnPreviousOrDefault() {
			final Object m = create(4, -1);
			assertEquals(-1L, put(m, key(1), 10), "キーがなければ既定値");
			assertEquals(10L, put(m, key(1), 11), "あれば更新前の値");
			assertEquals(11L, call(m, "putIfAbsent", args(key(1), 12L)));
			assertEquals(11L, get(m, key(1)));
			assertEquals(-1L, call(m, "putIfAbsent", args(key(2), 20L)));
			assertEquals(20L, get(m, key(2)));
		}

		@Test
		void computeIfAbsent() {
			final Object m = create();
			assertEquals(5L, call(m, "computeIfAbsent", args(key(2), fn(arity(), 5))));
			assertEquals(5L, call(m, "computeIfAbsent", args(key(2), fn(arity(), 6))), "キーがあれば関数を使わない");
			assertEquals(1, size(m));
		}

		@Test
		void computeIfAbsentWhenFunctionModifiesMap() {
			final Object m = create(2);
			// 関数の中で別のキーを追加してリサイズさせても、結果のキーが正しく入る
			final Object f = fnWith(arity(), () -> {
				for (int i = 10; i < 30; i++) put(m, key(i), i);
				return 7;
			});
			assertEquals(7L, call(m, "computeIfAbsent", args(key(1), f)));
			assertEquals(7L, get(m, key(1)));
			assertEquals(21, size(m));
			// 関数の中で同じキーを入れた場合は、その値を残す
			final Object g = fnWith(arity(), () -> {
				put(m, key(2), 100);
				return 8;
			});
			assertEquals(100L, call(m, "computeIfAbsent", args(key(2), g)));
		}

		@Test
		void removeReturnsPreviousOrDefault() {
			final Object m = create(4, -1);
			put(m, key(1), 10);
			assertEquals(10L, call(m, "remove", args(key(1))));
			assertEquals(-1L, call(m, "remove", args(key(1))));
			assertEquals(0, size(m));
		}

		@Test
		void counting() {
			final Object m = create(4, 10);
			assertEquals(11L, call(m, "increment", args(key(1))));
			assertEquals(10L, call(m, "decrement", args(key(1))));
			assertEquals(9L, call(m, "decrement", args(key(2))), "キーがなければ既定値 - 1");
			assertEquals(15L, call(m, "add", args(key(3), 5L)));
			assertEquals(20L, call(m, "add", args(key(3), 5L)));
			assertEquals(100L, call(m, "addOrDefault", args(key(4), 5L, 100L)), "キーがなければ absentValue");
			assertEquals(105L, call(m, "addOrDefault", args(key(4), 5L, 100L)), "あれば delta を足す");
		}

		@Test
		void merges() {
			final Object m = create();
			assertEquals(5L, call(m, "mergeMin", args(key(1), 5L)));
			assertEquals(3L, call(m, "mergeMin", args(key(1), 3L)));
			assertEquals(3L, call(m, "mergeMin", args(key(1), 4L)));
			assertEquals(5L, call(m, "mergeMax", args(key(2), 5L)));
			assertEquals(7L, call(m, "mergeMax", args(key(2), 7L)));
			assertEquals(7L, call(m, "mergeMax", args(key(2), 6L)));
			final LongBinaryOperator mul = (x, y) -> x * y;
			assertEquals(4L, call(m, "merge", args(key(3), 4L, mul)), "なければ value");
			assertEquals(12L, call(m, "merge", args(key(3), 3L, mul)), "あれば f(現在値, value)");
		}
	}

	@Nested
	class Bulk {

		@Test
		void clearThenReuse() {
			final Object m = create(2);
			for (int i = 0; i < 10; i++) put(m, key(i), i);
			Adapter.call(Void.class, m, "clear");
			assertEquals(0, size(m));
			assertFalse(contains(m, key(3)));
			assertEquals(0, ((long[]) Adapter.call(long[].class, m, "values")).length);
			put(m, key(3), 30);
			assertEquals(30L, get(m, key(3)));
			assertEquals(1, size(m));
		}
	}

	@Nested
	class Conversion {

		@Test
		void keysValuesAndEntriesAgree() {
			final Object m = sample();
			final Map<List<Long>, Long> expected = expectedSample();
			final long[][] keys = keyColumns(m);
			final long[] values = Adapter.call(long[].class, m, "values");
			final long[][] entries = Adapter.call(long[][].class, m, "entries");
			assertEquals(arity() + 1, entries.length);
			final Map<List<Long>, Long> fromKeys = new HashMap<>(), fromEntries = new HashMap<>();
			for (int i = 0; i < values.length; i++) {
				fromKeys.put(column(keys, i), values[i]);
				fromEntries.put(column(Arrays.copyOf(entries, arity()), i), entries[arity()][i]);
			}
			assertEquals(expected, fromKeys);
			assertEquals(expected, fromEntries);
		}

		@Test
		void forEachVisitsEveryEntry() {
			final Object m = sample();
			final Map<List<Long>, Long> seen = new HashMap<>();
			final List<List<Long>> keys = new ArrayList<>();
			final List<Long> values = new ArrayList<>();
			switch (arity()) {
				case 1 -> {
					Adapter.call(Void.class, m, "forEach", (Consumer2) (a, v) -> seen.put(List.of(a), v));
					Adapter.call(Void.class, m, "forEachKey", (LongConsumer) a -> keys.add(List.of(a)));
				}
				case 2 -> {
					Adapter.call(Void.class, m, "forEach", (Consumer3) (a, b, v) -> seen.put(List.of(a, b), v));
					Adapter.call(Void.class, m, "forEachKey", (Consumer2) (a, b) -> keys.add(List.of(a, b)));
				}
				default -> {
					Adapter.call(Void.class, m, "forEach", (Consumer4) (a, b, c, v) -> seen.put(List.of(a, b, c), v));
					Adapter.call(Void.class, m, "forEachKey", (Consumer3) (a, b, c) -> keys.add(List.of(a, b, c)));
				}
			}
			Adapter.call(Void.class, m, "forEachValue", (LongConsumer) values::add);
			assertEquals(expectedSample(), seen);
			assertEquals(expectedSample().keySet(), new HashSet<>(keys));
			assertEquals(expectedSample().values().stream().sorted().toList(), values.stream().sorted().toList());
		}

		@Test
		void reduces() {
			final Object m = sample();
			long keySum = 0, valueSum = 0;
			for (final Map.Entry<List<Long>, Long> e : expectedSample().entrySet()) {
				for (final long k : e.getKey()) keySum += k;
				valueSum += e.getValue();
			}
			final Object entryAcc = switch (arity()) {
				case 1 -> (Fn3) (acc, a, v) -> acc + a + v;
				case 2 -> (Fn4) (acc, a, b, v) -> acc + a + b + v;
				default -> (Fn5) (acc, a, b, c, v) -> acc + a + b + c + v;
			};
			final Object keyAcc = switch (arity()) {
				case 1 -> (LongBinaryOperator) Long::sum;
				case 2 -> (Fn3) (acc, a, b) -> acc + a + b;
				default -> (Fn4) (acc, a, b, c) -> acc + a + b + c;
			};
			assertEquals(100 + keySum + valueSum, call(m, "reduce", 100L, entryAcc));
			assertEquals(100 + keySum, call(m, "reduceKeys", 100L, keyAcc));
			assertEquals(100 + valueSum, call(m, "reduceValues", 100L, (LongBinaryOperator) Long::sum));
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final long def = random.nextInt(-3, 4);
		final Object m = create(1 + random.nextInt(8), def);
		final Map<List<Long>, Long> naive = new HashMap<>();
		new RandomOperations()
			.add("put", 3, (r, at) -> {
				final long[] k = randomKey(r);
				final long v = r.nextInt(-100, 100);
				final Long old = naive.put(list(k), v);
				assertEquals(old == null ? def : old, put(m, k, v), at);
			})
			.add("remove", 2, (r, at) -> {
				final long[] k = randomKey(r);
				final Long old = naive.remove(list(k));
				assertEquals(old == null ? def : old, call(m, "remove", args(k)), at);
			})
			.add("add", 2, (r, at) -> {
				final long[] k = randomKey(r);
				final long d = r.nextInt(-5, 6);
				assertEquals((long) naive.merge(list(k), def + d, (x, y) -> x + d), call(m, "add", args(k, d)), at);
			})
			.add("mergeMax", (r, at) -> {
				final long[] k = randomKey(r);
				final long v = r.nextInt(-100, 100);
				assertEquals((long) naive.merge(list(k), v, Math::max), call(m, "mergeMax", args(k, v)), at);
			})
			.add("get", 3, (r, at) -> {
				final long[] k = randomKey(r);
				assertEquals(naive.getOrDefault(list(k), def), get(m, k), at);
				assertEquals(naive.containsKey(list(k)), contains(m, k), at);
			})
			.add("clear", (r, at) -> {
				if (r.nextInt(10) != 0) return;
				naive.clear();
				Adapter.call(Void.class, m, "clear");
			})
			.add("entries", (r, at) -> {
				assertEquals(naive.size(), size(m), at);
				final long[][] entries = Adapter.call(long[][].class, m, "entries");
				final Map<List<Long>, Long> actual = new HashMap<>();
				for (int i = 0; i < naive.size(); i++) actual.put(column(Arrays.copyOf(entries, arity()), i), entries[arity()][i]);
				assertEquals(naive, actual, at);
			})
			.run(seed, random, 500);
	}

	// ---- 成分数によらない呼び出し ----

	long[] key(final long base) {
		final long[] k = new long[arity()];
		for (int i = 0; i < k.length; i++) k[i] = base + i;
		return k;
	}

	long[] randomKey(final Random random) {
		final long[] k = new long[arity()];
		for (int i = 0; i < k.length; i++) k[i] = keyComponent(random) / (i + 1);
		return k;
	}

	static Object[] args(final long[] key, final Object... rest) {
		final Object[] res = new Object[key.length + rest.length];
		for (int i = 0; i < key.length; i++) res[i] = key[i];
		System.arraycopy(rest, 0, res, key.length, rest.length);
		return res;
	}

	static long call(final Object m, final String name, final Object... args) {
		return Adapter.call(Long.class, m, name, args);
	}

	static long get(final Object m, final long[] key) {
		return call(m, "get", args(key));
	}

	static long put(final Object m, final long[] key, final long v) {
		return call(m, "put", args(key, v));
	}

	static boolean contains(final Object m, final long[] key) {
		return Adapter.call(Boolean.class, m, "containsKey", args(key));
	}

	static int size(final Object m) {
		return Adapter.call(Integer.class, m, "size");
	}

	/**
	 * keys() を成分ごとの列にそろえる。1成分のマップは1次元配列を返すため、1行の2次元配列にする。
	 */
	long[][] keyColumns(final Object m) {
		if (arity() == 1) return new long[][]{Adapter.call(long[].class, m, "keys")};
		return Adapter.call(long[][].class, m, "keys");
	}

	static List<Long> column(final long[][] cols, final int i) {
		final List<Long> res = new ArrayList<>();
		for (final long[] c : cols) res.add(c[i]);
		return res;
	}

	static List<Long> list(final long[] k) {
		return Arrays.stream(k).boxed().toList();
	}

	/**
	 * キーの成分を受け取り、定数 v を返す関数。
	 */
	static Object fn(final int arity, final long v) {
		return fnWith(arity, () -> v);
	}

	static Object fnWith(final int arity, final LongSupplier body) {
		return switch (arity) {
			case 1 -> (Fn1) a -> body.getAsLong();
			case 2 -> (Fn2) (a, b) -> body.getAsLong();
			default -> (Fn3) (a, b, c) -> body.getAsLong();
		};
	}

	Object sample() {
		final Object m = create();
		expectedSample().forEach((k, v) -> put(m, k.stream().mapToLong(Long::longValue).toArray(), v));
		return m;
	}

	Map<List<Long>, Long> expectedSample() {
		final Map<List<Long>, Long> res = new HashMap<>();
		for (int i = -3; i <= 3; i++) res.put(list(key(i * 7L)), 10L * i);
		return res;
	}
}
