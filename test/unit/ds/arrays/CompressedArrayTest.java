package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class CompressedArrayTest {

	@Test
	void intFromCollection() {
		final IntBIT src = new IntBIT(new int[]{3, 1, 2});
		final IntCompressedArray compressed = new IntCompressedArray(src);
		assertArrayEquals(new int[]{2, 0, 1}, compressed.toArray());
		assertArrayEquals(new int[]{3, 1, 2}, new IntCompressedArray(src, RankType.DENSE, true).toArray());
		assertArrayEquals(new int[]{3, 1, 2}, new IntCompressedArray(compressed).restore());
	}

	@Test
	void longFromCollection() {
		final LongCompressedArray compressed = new LongCompressedArray(new LongBIT(new long[]{3_000_000_000L, 1, 2}));
		assertArrayEquals(new int[]{2, 0, 1}, compressed.toArray());
		assertArrayEquals(new long[]{3_000_000_000L, 1, 2}, new LongCompressedArray(compressed).restore());
	}
}
