package verify.ds.map.longlongmap;

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
		final LongLongMap map = new LongLongMap(1, 10L);
		check(map.isEmpty());
		check(map.size() == 0);
		check(map.get(7L) == 10L);
		check(map.getOrDefault(7L, -1L) == -1L);
		check(!map.containsKey(7L));

		map.setDefaultValue(-3L);
		check(map.getDefaultValue() == -3L);
		check(map.get(7L) == -3L);
		check(map.add(7L, 5L) == 2L);
		check(map.get(7L) == 2L);
		check(map.increment(7L) == 3L);
		check(map.decrement(7L) == 2L);
		check(map.add(7L, 8L) == 10L);
		check(map.size() == 1);

		check(map.put(7L, 100L) == 100L);
		check(map.put(7L, 200L) == 200L);
		check(map.size() == 1);
		check(map.putIfAbsent(7L, 300L) == 200L);
		check(map.get(7L) == 200L);

		check(map.addOrDefault(8L, 4L, 50L) == 50L);
		check(map.addOrDefault(8L, 4L, 60L) == 54L);
		check(map.merge(8L, 6L, Long::sum) == 60L);
		check(map.merge(9L, 12L, Long::sum) == 12L);
		check(map.putIfAbsent(10L, 13L) == 13L);
		check(map.size() == 4);

		map.setDefaultValue(999L);
		check(map.get(12345L) == 999L);
		check(map.get(7L) == 200L);
		check(!map.remove(12345L));
		check(map.remove(7L));
		check(!map.containsKey(7L));
		check(map.get(7L) == 999L);
		check(map.size() == 3);
	}

	private static void testResizeAndDeletion() {
		final LongLongMap map = new LongLongMap(1, -1L);
		final HashMap<Long, Long> expected = new HashMap<>();
		final int count = 4096;

		for (int i = 0; i < count; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			final long value = i * 3L + 1L;
			check(map.put(key, value) == value);
			check(map.get(key) == value, "insert i=" + i + ", key=" + key);
			expected.put(key, value);
		}
		map.put(Long.MIN_VALUE, 11L);
		map.put(Long.MAX_VALUE, 12L);
		expected.put(Long.MIN_VALUE, 11L);
		expected.put(Long.MAX_VALUE, 12L);
		checkEquivalent(map, expected, -1L, -count, count);
		check(map.get(Long.MIN_VALUE) == 11L);
		check(map.get(Long.MAX_VALUE) == 12L);

		for (int i = 0; i < count; i += 3) {
			final long key = i * 0x9e3779b97f4a7c15L;
			check(map.remove(key), "remove i=" + i + ", key=" + key);
			expected.remove(key);
		}
		check(!map.remove(0x13579bdf2468aceL));
		checkEquivalent(map, expected, -1L, -count, count);

		for (int i = count; i < count * 2; i++) {
			final long key = i * 0x9e3779b97f4a7c15L;
			final long value = -i;
			check(map.addOrDefault(key, 100L, value) == value);
			expected.put(key, value);
		}
		checkEquivalent(map, expected, -1L, -count * 2, count * 2);

		map.clear();
		check(map.isEmpty());
		check(map.size() == 0);
		check(map.get(1L) == -1L);
		check(!map.containsKey(1L));
		check(map.keys().length == 0);
		check(map.values().length == 0);
		check(map.entries()[0].length == 0);

		map.put(1L, 2L);
		check(map.get(1L) == 2L);
		map.clear();
		map.clear();
		check(map.isEmpty());
	}

	private static void testViewsAndReducers() {
		final LongLongMap map = new LongLongMap(1);
		map.put(4L, 10L);
		map.put(1L, 20L);
		map.put(9L, 30L);

		final HashMap<Long, Long> entries = new HashMap<>();
		map.forEach(entries::put);
		check(entries.equals(Map.of(1L, 20L, 4L, 10L, 9L, 30L)));

		final HashSet<Long> keys = new HashSet<>();
		map.forEachKey(keys::add);
		check(keys.equals(Set.of(1L, 4L, 9L)));
		final ArrayList<Long> values = new ArrayList<>();
		map.forEachValue(values::add);
		check(values.size() == 3 && values.containsAll(List.of(10L, 20L, 30L)));

		check(map.reduce(0L, (sum, key, value) -> sum + key * value) == 330L);
		check(map.reduceKeys(0L, Long::sum) == 14L);
		check(map.reduceValues(0L, Long::sum) == 60L);

		final long[] actualKeys = map.keys();
		final long[] actualValues = map.values();
		Arrays.sort(actualKeys);
		Arrays.sort(actualValues);
		check(Arrays.equals(actualKeys, new long[]{1L, 4L, 9L}));
		check(Arrays.equals(actualValues, new long[]{10L, 20L, 30L}));

		final long[][] actualEntries = map.entries();
		final HashMap<Long, Long> copied = new HashMap<>();
		for (int i = 0; i < actualEntries[0].length; i++) copied.put(actualEntries[0][i], actualEntries[1][i]);
		check(copied.equals(entries));
	}

	private static void testRandomized() {
		final long defaultValue = -1_000_000L;
		final LongLongMap map = new LongLongMap(1, defaultValue);
		final HashMap<Long, Long> expected = new HashMap<>();
		final Random random = new Random(3);

		for (int step = 0; step < 30_000; step++) {
			final long key = random.nextInt(129) - 64L;
			final long value = random.nextInt(2001) - 1000L;
			final long delta = random.nextInt(101) - 50L;
			switch (random.nextInt(9)) {
				case 0 -> {
					check(map.put(key, value) == value);
					expected.put(key, value);
				}
				case 1 -> {
					final long result = expected.containsKey(key) ? expected.get(key) + delta : defaultValue + delta;
					check(map.add(key, delta) == result);
					expected.put(key, result);
				}
				case 2 -> {
					final long absentValue = value;
					final long result = expected.containsKey(key) ? expected.get(key) + delta : absentValue;
					check(map.addOrDefault(key, delta, absentValue) == result);
					expected.put(key, result);
				}
				case 3 -> {
					final long result = expected.containsKey(key) ? expected.get(key) * 31L + value : value;
					check(map.merge(key, value, (a, b) -> a * 31L + b) == result);
					expected.put(key, result);
				}
				case 4 -> {
					final Long old = expected.putIfAbsent(key, value);
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
					final long result = expected.containsKey(key) ? expected.get(key) + 1L : defaultValue + 1L;
					check(map.increment(key) == result);
					expected.put(key, result);
				}
			}
			checkEquivalent(map, expected, defaultValue, -64L, 65L);
		}
	}

	private static void checkEquivalent(final LongLongMap map, final Map<Long, Long> expected, final long defaultValue, final long from, final long to) {
		check(map.size() == expected.size(), "size");
		check(map.isEmpty() == expected.isEmpty(), "empty");
		for (final Map.Entry<Long, Long> entry : expected.entrySet()) {
			final long key = entry.getKey();
			final long value = entry.getValue();
			check(map.get(key) == value, "value key=" + key);
			check(map.getOrDefault(key, 123456789L) == value, "orDefault key=" + key);
			check(map.containsKey(key), "contains key=" + key);
		}
		for (long key = from; key < to; key++) {
			check(map.get(key) == expected.getOrDefault(key, defaultValue), "range value key=" + key);
			check(map.getOrDefault(key, 123456789L) == expected.getOrDefault(key, 123456789L), "range orDefault key=" + key);
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
