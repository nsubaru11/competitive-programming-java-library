package unit.ds.map;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.map.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongIntMapTest {

	@Test
	void defaultsAndUpdates() {
		final LongIntMap map = new LongIntMap(1, 10);
		assertTrue(map.isEmpty());
		assertEquals(0, map.size());
		assertEquals(10, map.get(7L));
		assertEquals(-1, map.getOrDefault(7L, -1));
		assertFalse(map.containsKey(7L));

		map.setDefaultValue(-3);
		assertEquals(-3, map.getDefaultValue());
		assertEquals(-3, map.get(7L));
		assertEquals(2, map.add(7L, 5));
		assertEquals(2, map.get(7L));
		assertEquals(3, map.increment(7L));
		assertEquals(2, map.decrement(7L));
		assertEquals(10, map.add(7L, 8));
		assertEquals(1, map.size());

		map.put(7L, 100);
		map.put(7L, 200);
		assertEquals(1, map.size());
		map.putIfAbsent(7L, 300);
		assertEquals(200, map.get(7L));

		assertEquals(50, map.addOrDefault(8L, 4, 50));
		assertEquals(54, map.addOrDefault(8L, 4, 60));
		assertEquals(60, map.merge(8L, 6, Integer::sum));
		assertEquals(12, map.merge(9L, 12, Integer::sum));
		map.putIfAbsent(10L, 13);
		assertEquals(4, map.size());

		map.setDefaultValue(999);
		assertEquals(999, map.get(12345L));
		assertEquals(200, map.get(7L));
		map.remove(12345L);
		map.remove(7L);
		assertFalse(map.containsKey(7L));
		assertEquals(999, map.get(7L));
		assertEquals(3, map.size());
	}

	@Test
	void resizeAndDeletion() {
		final LongIntMap map = new LongIntMap(1, -1);
		final HashMap<Long, Integer> expected = new HashMap<>();
		final int count = 4096;

		for (int i = 0; i < count; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			final int value = i * 3 + 1;
			map.put(key, value);
			assertEquals(value, map.get(key), "insert i=" + i);
			expected.put(key, value);
		}
		assertEquivalent(expected, map, -1, -count, count, "");

		for (int i = 0; i < count; i += 3) {
			final long key = i * 0x9e3779b97f4a7c15L;
			map.remove(key);
			expected.remove(key);
		}
		map.remove(0x13579bdf2468aceL);
		assertEquivalent(expected, map, -1, -count, count, "");

		for (int i = count; i < count * 2; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			assertEquals(-i, map.addOrDefault(key, 100, -i));
			expected.put(key, -i);
		}
		assertEquivalent(expected, map, -1, -count * 2, count * 2, "");
	}

	@Test
	void clear() {
		final LongIntMap map = new LongIntMap(1, -1);
		for (int i = 0; i < 100; i++) map.put(i, i);
		map.clear();
		assertTrue(map.isEmpty());
		assertEquals(0, map.size());
		assertEquals(-1, map.get(1L));
		assertFalse(map.containsKey(1L));
		assertEquals(0, map.keys().length);
		assertEquals(0, map.values().length);
		assertEquals(0, map.entries()[0].length);

		map.put(1L, 2);
		assertEquals(2, map.get(1L));
		map.clear();
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	void viewsAndReducers() {
		final LongIntMap map = new LongIntMap(1);
		map.put(4L, 10);
		map.put(1L, 20);
		map.put(9L, 30);

		final HashMap<Long, Integer> entries = new HashMap<>();
		map.forEach(entries::put);
		assertEquals(Map.of(1L, 20, 4L, 10, 9L, 30), entries);

		final HashSet<Long> keys = new HashSet<>();
		map.forEachKey(keys::add);
		assertEquals(Set.of(1L, 4L, 9L), keys);
		final ArrayList<Integer> values = new ArrayList<>();
		map.forEachValue(values::add);
		values.sort(null);
		assertEquals(List.of(10, 20, 30), values);

		assertEquals(330L, map.reduce(0L, (sum, key, value) -> sum + key * value));
		assertEquals(14L, map.reduceKeys(0L, Long::sum));
		assertEquals(60L, map.reduceValues(0L, Long::sum));

		final long[] actualKeys = map.keys();
		final int[] actualValues = map.values();
		Arrays.sort(actualKeys);
		Arrays.sort(actualValues);
		assertArrayEquals(new long[]{1, 4, 9}, actualKeys);
		assertArrayEquals(new int[]{10, 20, 30}, actualValues);

		final long[][] actualEntries = map.entries();
		final HashMap<Long, Integer> copied = new HashMap<>();
		for (int i = 0; i < actualEntries[0].length; i++) copied.put(actualEntries[0][i], (int) actualEntries[1][i]);
		assertEquals(entries, copied);
	}

	@Test
	void copyIsIndependent() {
		final LongIntMap map = new LongIntMap(1, 5);
		for (int i = 0; i < 100; i++) map.put(i, i * 2);
		map.remove(3L);
		final LongIntMap copy = new LongIntMap(map);
		assertEquals(99, copy.size());
		assertEquals(5, copy.getDefaultValue());
		assertEquals(5, copy.get(3L));
		assertEquals(20, copy.get(10L));
		copy.put(10L, -1);
		copy.put(1000L, 1);
		assertEquals(20, map.get(10L));
		assertFalse(map.containsKey(1000L));
		map.clear();
		assertEquals(100, copy.size());
		assertEquals(-1, copy.get(10L));
	}

	// 愚直解との照合: HashMap に同じ操作を適用し、各操作の後に全キーを比べる
	@RepeatedTest(10)
	void matchesHashMap(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int defaultValue = -1000;
		final LongIntMap map = new LongIntMap(1, defaultValue);
		final HashMap<Long, Integer> expected = new HashMap<>();
		for (int op = 0; op < 3000; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final long key = random.nextInt(129) - 64L;
			final int value = random.nextInt(2001) - 1000;
			final int delta = random.nextInt(101) - 50;
			switch (random.nextInt(9)) {
				case 0 -> {
					map.put(key, value);
					expected.put(key, value);
				}
				case 1 -> {
					final int result = expected.getOrDefault(key, defaultValue) + delta;
					assertEquals(result, map.add(key, delta), at);
					expected.put(key, result);
				}
				case 2 -> {
					final int result = expected.containsKey(key) ? expected.get(key) + delta : value;
					assertEquals(result, map.addOrDefault(key, delta, value), at);
					expected.put(key, result);
				}
				case 3 -> {
					final int result = expected.containsKey(key) ? expected.get(key) * 31 + value : value;
					assertEquals(result, map.merge(key, value, (a, b) -> a * 31 + b), at);
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
					final int result = expected.getOrDefault(key, defaultValue) + 1;
					assertEquals(result, map.increment(key), at);
					expected.put(key, result);
				}
			}
			assertEquivalent(expected, map, defaultValue, -64, 65, at);
		}
	}

	private static void assertEquivalent(final Map<Long, Integer> expected, final LongIntMap map, final int defaultValue, final long from, final long to, final String at) {
		assertEquals(expected.size(), map.size(), at);
		assertEquals(expected.isEmpty(), map.isEmpty(), at);
		for (final Map.Entry<Long, Integer> entry : expected.entrySet()) {
			final long key = entry.getKey();
			assertEquals(entry.getValue(), map.get(key), () -> at + " key=" + key);
			assertTrue(map.containsKey(key), () -> at + " key=" + key);
		}
		for (long k = from; k < to; k++) {
			final long key = k;
			assertEquals(expected.getOrDefault(key, defaultValue), map.get(key), () -> at + " key=" + key);
			assertEquals(expected.getOrDefault(key, 123456789), map.getOrDefault(key, 123456789), () -> at + " key=" + key);
			assertEquals(expected.containsKey(key), map.containsKey(key), () -> at + " key=" + key);
		}
	}
}
