package verify.ds.map.longintmap;

import java.util.*;

import lib.ds.map.*;

public final class Test {

	public static void main(final String[] args) {
		testDefaultsAndUpdates();
		testResizeAndDeletion();
		testViewsAndReducers();
		testRandomized();
	}

	private static void testDefaultsAndUpdates() {
		final LongIntMap map = new LongIntMap(1, 10);
		check(map.isEmpty());
		check(map.size() == 0);
		check(map.get(7L) == 10);
		check(map.getOrDefault(7L, -1) == -1);
		check(!map.containsKey(7L));

		map.setDefaultValue(-3);
		check(map.getDefaultValue() == -3);
		check(map.get(7L) == -3);
		check(map.add(7L, 5) == 2);
		check(map.get(7L) == 2);
		check(map.increment(7L) == 3);
		check(map.decrement(7L) == 2);
		check(map.add(7L, 8) == 10);
		check(map.size() == 1);

		check(map.put(7L, 100) == 100);
		check(map.put(7L, 200) == 200);
		check(map.size() == 1);
		check(map.putIfAbsent(7L, 300) == 200);
		check(map.get(7L) == 200);

		check(map.addOrDefault(8L, 4, 50) == 50);
		check(map.addOrDefault(8L, 4, 60) == 54);
		check(map.merge(8L, 6, Integer::sum) == 60);
		check(map.merge(9L, 12, Integer::sum) == 12);
		check(map.putIfAbsent(10L, 13) == 13);
		check(map.size() == 4);

		map.setDefaultValue(999);
		check(map.get(12345L) == 999);
		check(map.get(7L) == 200);
		check(!map.remove(12345L));
		check(map.remove(7L));
		check(!map.containsKey(7L));
		check(map.get(7L) == 999);
		check(map.size() == 3);
	}

	private static void testResizeAndDeletion() {
		final LongIntMap map = new LongIntMap(1, -1);
		final HashMap<Long, Integer> expected = new HashMap<>();
		final int count = 4096;

		for (int i = 0; i < count; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			final int value = i * 3 + 1;
			check(map.put(key, value) == value);
			check(map.get(key) == value, "insert i=" + i + ", key=" + key);
			expected.put(key, value);
		}
		checkEquivalent(map, expected, -1, -count, count);

		for (int i = 0; i < count; i += 3) {
			final long key = i * 0x9e3779b97f4a7c15L;
			check(map.remove(key), "remove i=" + i + ", key=" + key);
			expected.remove(key);
		}
		check(!map.remove(0x13579bdf2468aceL));
		checkEquivalent(map, expected, -1, -count, count);

		for (int i = count; i < count * 2; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			final int value = -i;
			check(map.addOrDefault(key, 100, value) == value);
			expected.put(key, value);
		}
		checkEquivalent(map, expected, -1, -count * 2, count * 2);

		map.clear();
		check(map.isEmpty());
		check(map.size() == 0);
		check(map.get(1L) == -1);
		check(!map.containsKey(1L));
		check(map.keys().length == 0);
		check(map.values().length == 0);
		check(map.entries()[0].length == 0);

		map.put(1L, 2);
		check(map.get(1L) == 2);
		map.clear();
		map.clear();
		check(map.isEmpty());
	}

	private static void testViewsAndReducers() {
		final LongIntMap map = new LongIntMap(1);
		map.put(4L, 10);
		map.put(1L, 20);
		map.put(9L, 30);

		final HashMap<Long, Integer> entries = new HashMap<>();
		map.forEach(entries::put);
		check(entries.equals(Map.of(1L, 20, 4L, 10, 9L, 30)));

		final HashSet<Long> keys = new HashSet<>();
		map.forEachKey(keys::add);
		check(keys.equals(Set.of(1L, 4L, 9L)));
		final ArrayList<Integer> values = new ArrayList<>();
		map.forEachValue(values::add);
		check(values.size() == 3 && values.containsAll(List.of(10, 20, 30)));

		check(map.reduce(0L, (sum, key, value) -> sum + key * value) == 330L);
		check(map.reduceKeys(0L, Long::sum) == 14L);
		check(map.reduceValues(0L, Long::sum) == 60L);

		final long[] actualKeys = map.keys();
		final int[] actualValues = map.values();
		Arrays.sort(actualKeys);
		Arrays.sort(actualValues);
		check(Arrays.equals(actualKeys, new long[]{1L, 4L, 9L}));
		check(Arrays.equals(actualValues, new int[]{10, 20, 30}));

		final long[][] actualEntries = map.entries();
		final HashMap<Long, Integer> copied = new HashMap<>();
		for (int i = 0; i < actualEntries[0].length; i++) copied.put(actualEntries[0][i], (int) actualEntries[1][i]);
		check(copied.equals(entries));
	}

	private static void testRandomized() {
		final int defaultValue = -1000;
		final LongIntMap map = new LongIntMap(1, defaultValue);
		final HashMap<Long, Integer> expected = new HashMap<>();
		final Random random = new Random(2);

		for (int step = 0; step < 30_000; step++) {
			final long key = random.nextInt(129) - 64L;
			final int value = random.nextInt(2001) - 1000;
			final int delta = random.nextInt(101) - 50;
			switch (random.nextInt(9)) {
				case 0 -> {
					check(map.put(key, value) == value);
					expected.put(key, value);
				}
				case 1 -> {
					final int result = expected.containsKey(key) ? expected.get(key) + delta : defaultValue + delta;
					check(map.add(key, delta) == result);
					expected.put(key, result);
				}
				case 2 -> {
					final int result = expected.containsKey(key) ? expected.get(key) + delta : value;
					check(map.addOrDefault(key, delta, value) == result);
					expected.put(key, result);
				}
				case 3 -> {
					final int result = expected.containsKey(key) ? expected.get(key) * 31 + value : value;
					check(map.merge(key, value, (a, b) -> a * 31 + b) == result);
					expected.put(key, result);
				}
				case 4 -> {
					final Integer old = expected.putIfAbsent(key, value);
					check(map.putIfAbsent(key, value) == (old == null ? value : old));
				}
				case 5 -> {
					final boolean existed = expected.remove(key) != null;
					check(map.remove(key) == existed);
				}
				case 6 -> {
					check(map.get(key) == expected.getOrDefault(key, defaultValue));
					check(map.getOrDefault(key, value) == expected.getOrDefault(key, value));
					check(map.containsKey(key) == expected.containsKey(key));
				}
				case 7 -> {
					map.clear();
					expected.clear();
				}
				default -> {
					final int result = expected.containsKey(key) ? expected.get(key) + 1 : defaultValue + 1;
					check(map.increment(key) == result);
					expected.put(key, result);
				}
			}
			checkEquivalent(map, expected, defaultValue, -64L, 65L);
		}
	}

	private static void checkEquivalent(final LongIntMap map, final Map<Long, Integer> expected, final int defaultValue, final long from, final long to) {
		check(map.size() == expected.size(), "size");
		check(map.isEmpty() == expected.isEmpty(), "empty");
		for (final Map.Entry<Long, Integer> entry : expected.entrySet()) {
			final long key = entry.getKey();
			final int value = entry.getValue();
			check(map.get(key) == value, "value key=" + key);
			check(map.getOrDefault(key, 123456789) == value, "orDefault key=" + key);
			check(map.containsKey(key), "contains key=" + key);
		}
		for (long key = from; key < to; key++) {
			check(map.get(key) == expected.getOrDefault(key, defaultValue), "range value key=" + key);
			check(map.getOrDefault(key, 123456789) == expected.getOrDefault(key, 123456789), "range orDefault key=" + key);
			check(map.containsKey(key) == expected.containsKey(key), "range contains key=" + key);
		}
	}

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}

	private static void check(final boolean condition, final String message) {
		if (!condition) throw new AssertionError(message);
	}
}
