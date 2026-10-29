package unit.ds.arrays;

import java.util.*;

import lib.ds.arrays.*;
import lib.ds.fenwick.*;

public final class Test {

	public static void main(final String[] args) {
		testIntCollectionConstructors();
		testLongCollectionConstructors();
		testArrayConstructors();
		testDequeCopies();
	}

	private static void testIntCollectionConstructors() {
		final IntBIT src = new IntBIT(new int[]{3, 1, 2});
		check(new IntCircularArray(src).toString().equals("3 1 2"));
		final IntCompressedArray compressed = new IntCompressedArray(src);
		check(Arrays.equals(compressed.toArray(), new int[]{2, 0, 1}));
		check(Arrays.equals(new IntCompressedArray(src, RankType.DENSE, true).toArray(), new int[]{3, 1, 2}));
		check(Arrays.equals(new IntCompressedArray(compressed).restore(), new int[]{3, 1, 2}));
		final IntPrefixSum sum = new IntPrefixSum(src);
		check(sum.sumAll() == 6);
		check(sum.sum(1) == 4);
		check(sum.get(2) == 2);
		final IntPrefixModSum modSum = new IntPrefixModSum(src, 5);
		check(modSum.sumAll() == 1);
		check(modSum.sum(1, 2) == 3);
	}

	private static void testLongCollectionConstructors() {
		final LongBIT src = new LongBIT(new long[]{3_000_000_000L, 1, 2});
		check(new LongCircularArray(src).toString().equals("3000000000 1 2"));
		final LongCompressedArray compressed = new LongCompressedArray(src);
		check(Arrays.equals(compressed.toArray(), new int[]{2, 0, 1}));
		check(Arrays.equals(new LongCompressedArray(compressed).restore(), new long[]{3_000_000_000L, 1, 2}));
		final LongPrefixSum sum = new LongPrefixSum(src);
		check(sum.sumAll() == 3_000_000_003L);
		check(sum.sum(1, 2) == 3);
		final LongPrefixModSum modSum = new LongPrefixModSum(new LongBIT(new long[]{4, 1, 2}), 5);
		check(modSum.sumAll() == 2);
		check(modSum.get(0) == 4);
	}

	private static void testArrayConstructors() {
		final LongPrefixSum sum = new LongPrefixSum(new long[]{5, 7});
		check(sum.sumAll() == 12);
		check(sum.get(1) == 7);
		check(new IntPrefixModSum(new int[]{4, 3}, 5).sumAll() == 2);
		check(new LongPrefixModSum(new long[]{4, 3}, 5).sumAll() == 2);
		check(new LongPrefixSum(new long[0]).sumAll() == 0);
	}

	private static void testDequeCopies() {
		final IntArrayDeque deque = new IntArrayDeque(4);
		deque.addLast(1);
		deque.addLast(2);
		deque.addFirst(0);
		final IntArrayDeque copy = new IntArrayDeque(deque);
		copy.addLast(3);
		copy.set(0, 9);
		check(deque.toString().equals("0 1 2"));
		check(copy.toString().equals("9 1 2 3"));

		final LongArrayDeque longDeque = new LongArrayDeque(2);
		longDeque.addLast(5);
		longDeque.addFirst(4);
		final LongArrayDeque longCopy = new LongArrayDeque(longDeque);
		check(longCopy.removeFirst() == 4);
		check(longDeque.size() == 2);
		check(longCopy.toString().equals("5"));
	}

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}
}
