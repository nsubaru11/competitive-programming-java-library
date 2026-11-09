package unit.ds.map;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.map.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntIntMapTest {

	@Test
	void defaultsAndUpdates() {
		final IntIntMap map = new IntIntMap(1, 10);
		assertTrue(map.isEmpty());
		assertEquals(10, map.get(7));
		assertEquals(-1, map.getOrDefault(7, -1));
		assertFalse(map.containsKey(7));

		map.setDefaultValue(-3);
		assertEquals(-3, map.getDefaultValue());
		assertEquals(-3, map.get(7));
		assertEquals(2, map.add(7, 5));
		assertEquals(2, map.get(7));
		assertEquals(3, map.increment(7));
		assertEquals(2, map.decrement(7));
		assertEquals(10, map.add(7, 8));
		assertEquals(1, map.size());

		map.put(7, 100);
		map.put(7, 200);
		assertEquals(1, map.size());
		map.putIfAbsent(7, 300);
		assertEquals(200, map.get(7));

		assertEquals(50, map.addOrDefault(8, 4, 50));
		assertEquals(54, map.addOrDefault(8, 4, 60));
		assertEquals(60, map.merge(8, 6, Integer::sum));
		assertEquals(12, map.merge(9, 12, Integer::sum));
		map.putIfAbsent(10, 13);
		assertEquals(4, map.size());

		map.setDefaultValue(999);
		assertEquals(999, map.get(12345));
		assertEquals(200, map.get(7));
		map.remove(12345);
		map.remove(7);
		assertFalse(map.containsKey(7));
		assertEquals(999, map.get(7));
		assertEquals(3, map.size());
	}

	@Test
	void resizeAndDeletion() {
		final IntIntMap map = new IntIntMap(1, -1);
		final HashMap<Integer, Integer> expected = new HashMap<>();
		final int count = 4096;

		for (int i = 0; i < count; i++) {
			final int key = i * 0x9e3779b9;
			final int value = i * 3 + 1;
			map.put(key, value);
			assertEquals(value, map.get(key), "insert i=" + i);
			expected.put(key, value);
		}
		assertEquivalent(expected, map, -1, -count, count);

		for (int i = 0; i < count; i += 3) {
			final int key = i * 0x9e3779b9;
			map.remove(key);
			expected.remove(key);
		}
		map.remove(0x13579bdf);
		assertEquivalent(expected, map, -1, -count, count);

		for (int i = count; i < count * 2; i++) {
			final int key = i * 0x9e3779b9;
			assertEquals(-i, map.addOrDefault(key, 100, -i));
			expected.put(key, -i);
		}
		assertEquivalent(expected, map, -1, -count * 2, count * 2);
	}

	@Test
	void clear() {
		final IntIntMap map = new IntIntMap(1, -1);
		for (int i = 0; i < 100; i++) map.put(i, i);
		map.clear();
		assertTrue(map.isEmpty());
		assertEquals(-1, map.get(1));
		assertFalse(map.containsKey(1));
		assertEquals(0, map.keys().length);
		assertEquals(0, map.values().length);
		assertEquals(0, map.entries()[0].length);

		map.put(1, 2);
		assertEquals(2, map.get(1));
		map.clear();
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	void viewsAndReducers() {
		final IntIntMap map = new IntIntMap(1);
		map.put(4, 10);
		map.put(1, 20);
		map.put(9, 30);

		final HashMap<Integer, Integer> entries = new HashMap<>();
		map.forEach(entries::put);
		assertEquals(Map.of(1, 20, 4, 10, 9, 30), entries);

		final HashSet<Integer> keys = new HashSet<>();
		map.forEachKey(keys::add);
		assertEquals(Set.of(1, 4, 9), keys);
		final ArrayList<Integer> values = new ArrayList<>();
		map.forEachValue(values::add);
		values.sort(null);
		assertEquals(List.of(10, 20, 30), values);

		assertEquals(330L, map.reduce(0L, (sum, key, value) -> sum + (long) key * value));
		assertEquals(14L, map.reduceKeys(0L, Long::sum));
		assertEquals(60L, map.reduceValues(0L, Long::sum));

		final int[] actualKeys = map.keys();
		final int[] actualValues = map.values();
		Arrays.sort(actualKeys);
		Arrays.sort(actualValues);
		assertArrayEquals(new int[]{1, 4, 9}, actualKeys);
		assertArrayEquals(new int[]{10, 20, 30}, actualValues);

		final int[][] actualEntries = map.entries();
		final HashMap<Integer, Integer> copied = new HashMap<>();
		for (int i = 0; i < actualEntries[0].length; i++) copied.put(actualEntries[0][i], actualEntries[1][i]);
		assertEquals(entries, copied);
	}

	@Test
	void copyIsIndependent() {
		final IntIntMap map = new IntIntMap(1, 5);
		for (int i = 0; i < 100; i++) map.put(i, i * 2);
		map.remove(3);
		final IntIntMap copy = new IntIntMap(map);
		assertEquals(99, copy.size());
		assertEquals(5, copy.getDefaultValue());
		assertEquals(5, copy.get(3));
		assertEquals(20, copy.get(10));
		copy.put(10, -1);
		copy.put(1000, 1);
		assertEquals(20, map.get(10));
		assertFalse(map.containsKey(1000));
		map.clear();
		assertEquals(100, copy.size());
		assertEquals(-1, copy.get(10));
	}

	// 愚直解との照合: HashMap に同じ操作を適用し、各操作の後に全キーを比べる
	@RepeatedTest(10)
	void matchesHashMap(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int defaultValue = -1000;
		final IntIntMap map = new IntIntMap(1, defaultValue);
		final HashMap<Integer, Integer> expected = new HashMap<>();
		for (int op = 0; op < 3000; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int key = random.nextInt(129) - 64;
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
				case 6 -> {
					assertEquals(expected.getOrDefault(key, value), map.getOrDefault(key, value), at);
				}
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

	private static void assertEquivalent(final Map<Integer, Integer> expected, final IntIntMap map, final int defaultValue, final int from, final int to) {
		assertEquivalent(expected, map, defaultValue, from, to, "");
	}

	private static void assertEquivalent(final Map<Integer, Integer> expected, final IntIntMap map, final int defaultValue, final int from, final int to, final String at) {
		assertEquals(expected.size(), map.size(), at);
		assertEquals(expected.isEmpty(), map.isEmpty(), at);
		for (final Map.Entry<Integer, Integer> entry : expected.entrySet()) {
			final int key = entry.getKey();
			assertEquals(entry.getValue(), map.get(key), () -> at + " key=" + key);
			assertTrue(map.containsKey(key), () -> at + " key=" + key);
		}
		for (int k = from; k < to; k++) {
			final int key = k;
			assertEquals(expected.getOrDefault(key, defaultValue), map.get(key), () -> at + " key=" + key);
			assertEquals(expected.getOrDefault(key, 123456789), map.getOrDefault(key, 123456789), () -> at + " key=" + key);
			assertEquals(expected.containsKey(key), map.containsKey(key), () -> at + " key=" + key);
		}
	}
}
