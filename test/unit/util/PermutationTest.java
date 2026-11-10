package unit.util;

import java.util.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * index は重複を除いた辞書順の0始まりの位置、next・prev は辞書順で次・前の並びへその場で並べ替える。
 * 区間版は [fromIdx, toIdx)、2次元版は指定した列の値で行を比べる。int・long・char 版を同じ期待値で検査する。
 */
final class PermutationTest {

	private static long expectedIndexFull(final long[] base) {
		return new ArrayList<>(permutations(base)).indexOf(Arrays.stream(base).boxed().toList());
	}

	private static List<Long> toList(final Object a) {
		return Arrays.stream((long[]) Adapter.convert(a, long[].class)).boxed().toList();
	}

	/**
	 * すべての並び（重複あり）。TreeSet に入れて重複を除き、辞書順に並べる。
	 */
	private static Set<List<Long>> permutations(final long[] a) {
		final Set<List<Long>> res = new TreeSet<>((x, y) -> {
			for (int i = 0; i < x.size(); i++) {
				final int c = Long.compare(x.get(i), y.get(i));
				if (c != 0) return c;
			}
			return 0;
		});
		permute(a, new boolean[a.length], new ArrayList<>(), res);
		return res;
	}

	private static void permute(final long[] a, final boolean[] used, final List<Long> cur, final Set<List<Long>> res) {
		if (cur.size() == a.length) {
			res.add(List.copyOf(cur));
			return;
		}
		for (int i = 0; i < a.length; i++) {
			if (used[i]) continue;
			used[i] = true;
			cur.add(a[i]);
			permute(a, used, cur, res);
			cur.removeLast();
			used[i] = false;
		}
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void example(final Kind kind) {
		final Object a = Adapter.convert(new long[]{'b', 'b', 'a'}, kind.type1D);
		// 重複を除いた並びは abb, bab, bba
		assertEquals(2L, (long) Adapter.callStatic(Long.class, Permutation.class, "index", a));
		assertFalse(Adapter.callStatic(Boolean.class, Permutation.class, "next", a), "最後の並び");
		assertTrue(Adapter.callStatic(Boolean.class, Permutation.class, "prev", a));
		assertArrayEquals(new long[]{'b', 'a', 'b'}, (long[]) Adapter.convert(a, long[].class));
		assertTrue(Adapter.callStatic(Boolean.class, Permutation.class, "prev", a));
		assertFalse(Adapter.callStatic(Boolean.class, Permutation.class, "prev", a), "最初の並び");
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void enumeratesAllDistinctPermutations(final Kind kind) {
		final Random random = new Random(kind.ordinal());
		for (int t = 0; t < 30; t++) {
			final long[] base = random.longs(random.nextInt(1, 6), 'a', 'd').sorted().toArray();
			final List<List<Long>> all = new ArrayList<>(permutations(base));
			// long 版は変換しても同じ配列が返るため、コピーを渡す
			final Object a = Adapter.convert(base.clone(), kind.type1D);
			for (int i = 0; i < all.size(); i++) {
				final String at = "base=" + Arrays.toString(base) + ", i=" + i;
				assertEquals(all.get(i), toList(a), at);
				assertEquals(i, (long) Adapter.callStatic(Long.class, Permutation.class, "index", a), at);
				assertEquals(i + 1 < all.size(), Adapter.callStatic(Boolean.class, Permutation.class, "next", a), at);
			}
			assertEquals(all.getLast(), toList(a), "最後の並びで next を呼んでも並びは変えない");
		}
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void rangeAndColumnVariants(final Kind kind) {
		final Random random = new Random(10 + kind.ordinal());
		for (int t = 0; t < 100; t++) {
			final long[] base = random.longs(random.nextInt(1, 7), 'a', 'd').toArray();
			final int from = random.nextInt(base.length), to = from + 1 + random.nextInt(base.length - from);
			final long[] mid = Arrays.copyOfRange(base, from, to);
			final List<List<Long>> all = new ArrayList<>(permutations(mid));
			final int expectedIndex = all.indexOf(Arrays.stream(mid).boxed().toList());
			final String at = "base=" + Arrays.toString(base) + ", [" + from + ", " + to + ")";
			// long 版は変換しても同じ配列が返るため、コピーを渡す
			final Object a = Adapter.convert(base.clone(), kind.type1D);
			assertEquals(expectedIndex, (long) Adapter.callStatic(Long.class, Permutation.class, "index", a, from, to), at);
			final boolean hasNext = expectedIndex + 1 < all.size();
			assertEquals(hasNext, Adapter.callStatic(Boolean.class, Permutation.class, "next", a, from, to), at);
			final long[] after = (long[]) Adapter.convert(a, long[].class);
			if (hasNext) assertEquals(all.get(expectedIndex + 1), Arrays.stream(after, from, to).boxed().toList(), at);
			assertArrayEquals(Arrays.copyOfRange(base, 0, from), Arrays.copyOfRange(after, 0, from), at + ", 区間外は変えない");
			assertArrayEquals(Arrays.copyOfRange(base, to, base.length), Arrays.copyOfRange(after, to, after.length), at);
			// 2次元版: 列1の値で行を並べ替え、行は丸ごと動く
			final long[][] rows = new long[base.length][];
			for (int i = 0; i < base.length; i++) rows[i] = new long[]{i, base[i]};
			final Object grid = Adapter.convert(Arrays.stream(rows).map(long[]::clone).toArray(long[][]::new), kind.type2D);
			assertEquals(expectedIndexFull(base), (long) Adapter.callStatic(Long.class, Permutation.class, "index", grid, 1), at);
			assertEquals(expectedIndex, (long) Adapter.callStatic(Long.class, Permutation.class, "index", grid, 1, from, to), at);
			final boolean prevExists = expectedIndex > 0;
			assertEquals(prevExists, Adapter.callStatic(Boolean.class, Permutation.class, "prev", grid, 1, from, to), at);
			final long[][] moved = (long[][]) Adapter.convert(grid, long[][].class);
			for (final long[] row : moved) assertEquals(base[(int) row[0]], row[1], at + ", 行は丸ごと動く");
		}
	}

	enum Kind {
		INT(int[].class, int[][].class), LONG(long[].class, long[][].class), CHAR(char[].class, char[][].class);

		final Class<?> type1D, type2D;

		Kind(final Class<?> type1D, final Class<?> type2D) {
			this.type1D = type1D;
			this.type2D = type2D;
		}
	}
}
