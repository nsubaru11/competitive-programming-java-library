package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class LongArray2D implements Iterable<Long> {
	public final int length;
	private final long[] arr;
	private final int h, w;
	private int rCnt = 0;
	private boolean transposed = false;

	public LongArray2D(final int h, final int w, final LongBinaryOperator init) {
		this.h = h;
		this.w = w;
		length = h * w;
		arr = new long[length];
		for (int i = 0, ij = 0; i < h; i++) for (int j = 0; j < w; j++) arr[ij++] = init.applyAsLong(i, j);
	}

	public long get(int i, int j) {
		if (transposed) {
			int t = i;
			i = j;
			j = t;
		}
		return switch (rCnt & 3) {
			case 0 -> arr[i * w + j];
			case 1 -> arr[j * w - i + w - 1];
			case 2 -> arr[length - i * w - j - 1];
			case 3 -> arr[length - j * w + i - w];
			default -> throw new IllegalStateException();
		};
	}

	public void set(int i, int j, final long value) {
		if (transposed) {
			int t = i;
			i = j;
			j = t;
		}
		int idx = switch (rCnt & 3) {
			case 0 -> i * w + j;
			case 1 -> j * w - i + w - 1;
			case 2 -> length - i * w - j - 1;
			case 3 -> length - j * w + i - w;
			default -> throw new IllegalStateException();
		};
		arr[idx] = value;
	}

	public void lRotate() {
		rCnt++;
	}

	public void rRotate() {
		rCnt--;
	}

	public void transpose() {
		if (h == w) transposed = !transposed;
	}

	public int h() {
		return (rCnt & 1) == 0 ? h : w;
	}

	public int w() {
		return (rCnt & 1) == 0 ? w : h;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int index = 0;

			public boolean hasNext() {
				return index < length;
			}

			public long nextLong() {
				return arr[index++];
			}
		};
	}

	/**
	 * 論理状態の各行を半角スペース区切り、行間を改行した文字列として返します。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		final int h = h(), w = w();
		sb.append(get(0, 0));
		for (int j = 1; j < w; j++) sb.append(' ').append(get(0, j));
		for (int i = 1; i < h; i++) {
			sb.append('\n').append(get(i, 0));
			for (int j = 1; j < w; j++) sb.append(' ').append(get(i, j));
		}
		return sb.toString();
	}
}
