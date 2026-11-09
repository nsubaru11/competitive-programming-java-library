package unit.ds.map;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.map.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongLongMapTest {

	@Test
	void defaultsAndUpdates() {
		final LongLongMap map = new LongLongMap(1, 10L);
		assertTrue(map.isEmpty());
		assertEquals(0, map.size());
		assertEquals(10L, map.get(7L));
		assertEquals(-1L, map.getOrDefault(7L, -1L));
		assertFalse(map.containsKey(7L));

		map.setDefaultValue(-3L);
		assertEquals(-3L, map.getDefaultValue());
		assertEquals(-3L, map.get(7L));
		assertEquals(2L, map.add(7L, 5L));
		assertEquals(2L, map.get(7L));
		assertEquals(3L, map.increment(7L));
		assertEquals(2L, map.decrement(7L));
		assertEquals(10L, map.add(7L, 8L));
		assertEquals(1, map.size());

		map.put(7L, 100L);
		map.put(7L, 200L);
		assertEquals(1, map.size());
		map.putIfAbsent(7L, 300L);
		assertEquals(200L, map.get(7L));

		assertEquals(50L, map.addOrDefault(8L, 4L, 50L));
		assertEquals(54L, map.addOrDefault(8L, 4L, 60L));
		assertEquals(60L, map.merge(8L, 6L, Long::sum));
		assertEquals(12L, map.merge(9L, 12L, Long::sum));
		map.putIfAbsent(10L, 13L);
		assertEquals(4, map.size());

		map.setDefaultValue(999L);
		assertEquals(999L, map.get(12345L));
		assertEquals(200L, map.get(7L));
		map.remove(12345L);
		map.remove(7L);
		assertFalse(map.containsKey(7L));
		assertEquals(999L, map.get(7L));
		assertEquals(3, map.size());
	}

	@Test
	void resizeAndDeletion() {
		final LongLongMap map = new LongLongMap(1, -1L);
		final HashMap<Long, Long> expected = new HashMap<>();
		final int count = 4096;

		for (int i = 0; i < count; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			final long value = i * 3L + 1L;
			map.put(key, value);
			assertEquals(value, map.get(key), "insert i=" + i);
			expected.put(key, value);
		}
		// キーの全範囲を扱える
		map.put(Long.MIN_VALUE, 11L);
		map.put(Long.MAX_VALUE, 12L);
		expected.put(Long.MIN_VALUE, 11L);
		expected.put(Long.MAX_VALUE, 12L);
		assertEquivalent(expected, map, -1L, -count, count, "");

		for (int i = 0; i < count; i += 3) {
			final long key = i * 0x9e3779b97f4a7c15L;
			map.remove(key);
			expected.remove(key);
		}
		map.remove(0x13579bdf2468aceL);
		assertEquivalent(expected, map, -1L, -count, count, "");

		for (int i = count; i < count * 2; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			assertEquals(-i, map.addOrDefault(key, 100L, -i));
			expected.put(key, (long) -i);
		}
		assertEquivalent(expected, map, -1L, -count * 2, count * 2, "");
	}

	@Test
	void clear() {
		final LongLongMap map = new LongLongMap(1, -1L);
		for (int i = 0; i < 100; i++) map.put(i, i);
		map.clear();
		assertTrue(map.isEmpty());
		assertEquals(0, map.size());
		assertEquals(-1L, map.get(1L));
		assertFalse(map.containsKey(1L));
		assertEquals(0, map.keys().length);
		assertEquals(0, map.values().length);
		assertEquals(0, map.entries()[0].length);

		map.put(1L, 2L);
		assertEquals(2L, map.get(1L));
		map.clear();
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	void viewsAndReducers() {
		final LongLongMap map = new LongLongMap(1);
		map.put(4L, 10L);
		map.put(1L, 20L);
		map.put(9L, 30L);

		final HashMap<Long, Long> entries = new HashMap<>();
		map.forEach(entries::put);
		assertEquals(Map.of(1L, 20L, 4L, 10L, 9L, 30L), entries);

		final HashSet<Long> keys = new HashSet<>();
		map.forEachKey(keys::add);
		assertEquals(Set.of(1L, 4L, 9L), keys);
		final ArrayList<Long> values = new ArrayList<>();
		map.forEachValue(values::add);
		values.sort(null);
		assertEquals(List.of(10L, 20L, 30L), values);

		assertEquals(330L, map.reduce(0L, (sum, key, value) -> sum + key * value));
		assertEquals(14L, map.reduceKeys(0L, Long::sum));
		assertEquals(60L, map.reduceValues(0L, Long::sum));

		final long[] actualKeys = map.keys();
		final long[] actualValues = map.values();
		Arrays.sort(actualKeys);
		Arrays.sort(actualValues);
		assertArrayEquals(new long[]{1, 4, 9}, actualKeys);
		assertArrayEquals(new long[]{10, 20, 30}, actualValues);

		final long[][] actualEntries = map.entries();
		final HashMap<Long, Long> copied = new HashMap<>();
		for (int i = 0; i < actualEntries[0].length; i++) copied.put(actualEntries[0][i], actualEntries[1][i]);
		assertEquals(entries, copied);
	}

	@Test
	void copyIsIndependent() {
		final LongLongMap map = new LongLongMap(1, 5L);
		for (int i = 0; i < 100; i++) map.put(i, i * 2L);
		map.remove(3L);
		final LongLongMap copy = new LongLongMap(map);
		assertEquals(99, copy.size());
		assertEquals(5L, copy.getDefaultValue());
		assertEquals(5L, copy.get(3L));
		assertEquals(20L, copy.get(10L));
		copy.put(10L, -1L);
		copy.put(1000L, 1L);
		assertEquals(20L, map.get(10L));
		assertFalse(map.containsKey(1000L));
		map.clear();
		assertEquals(100, copy.size());
		assertEquals(-1L, copy.get(10L));
	}

	// 愚直解との照合: HashMap に同じ操作を適用し、各操作の後に全キーを比べる
	@RepeatedTest(10)
	void matchesHashMap(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final long defaultValue = -1_000_000L;
		final LongLongMap map = new LongLongMap(1, defaultValue);
		final HashMap<Long, Long> expected = new HashMap<>();
		for (int op = 0; op < 3000; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final long key = random.nextInt(129) - 64L;
			final long value = random.nextInt(2001) - 1000L;
			final long delta = random.nextInt(101) - 50L;
			switch (random.nextInt(9)) {
				case 0 -> {
					map.put(key, value);
					expected.put(key, value);
				}
				case 1 -> {
					final long result = expected.getOrDefault(key, defaultValue) + delta;
					assertEquals(result, map.add(key, delta), at);
					expected.put(key, result);
				}
				case 2 -> {
					final long result = expected.containsKey(key) ? expected.get(key) + delta : value;
					assertEquals(result, map.addOrDefault(key, delta, value), at);
					expected.put(key, result);
				}
				case 3 -> {
					final long result = expected.containsKey(key) ? expected.get(key) * 31L + value : value;
					assertEquals(result, map.merge(key, value, (a, b) -> a * 31L + b), at);
					expected.put(key, result);
				}
				case 4 -> {
					map.putIfAbsent(key, value);
					expected.putIfAbsent(key, value);
				}
				case 5 -> {
					assertEquals(expected.containsKey(key), map.containsKey(key), at);
					map.remove(key);
					expected.remove(key);
				}
				case 6 -> assertEquals(expected.getOrDefault(key, value), map.getOrDefault(key, value), at);
				case 7 -> {
					map.clear();
					expected.clear();
				}
				default -> {
					final long result = expected.getOrDefault(key, defaultValue) + 1L;
					assertEquals(result, map.increment(key), at);
					expected.put(key, result);
				}
			}
			assertEquivalent(expected, map, defaultValue, -64, 65, at);
		}
	}

	private static void assertEquivalent(final Map<Long, Long> expected, final LongLongMap map, final long defaultValue, final long from, final long to, final String at) {
		assertEquals(expected.size(), map.size(), at);
		assertEquals(expected.isEmpty(), map.isEmpty(), at);
		for (final Map.Entry<Long, Long> entry : expected.entrySet()) {
			final long key = entry.getKey();
			assertEquals(entry.getValue(), map.get(key), () -> at + " key=" + key);
			assertTrue(map.containsKey(key), () -> at + " key=" + key);
		}
		for (long k = from; k < to; k++) {
			final long key = k;
			assertEquals(expected.getOrDefault(key, defaultValue), map.get(key), () -> at + " key=" + key);
			assertEquals(expected.getOrDefault(key, 123456789L), map.getOrDefault(key, 123456789L), () -> at + " key=" + key);
			assertEquals(expected.containsKey(key), map.containsKey(key), () -> at + " key=" + key);
		}
	}
}
