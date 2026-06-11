import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class IntArray2D implements Iterable<Integer> {
	private final int[] arr;
	private final int hw, h, w;
	private int rCnt = 0;
	private boolean transposed = false;

	public IntArray2D(final int h, final int w, final IntBinaryOperator init) {
		this.h = h;
		this.w = w;
		this.hw = h * w;
		this.arr = new int[hw];
		for (int i = 0, ij = 0; i < h; i++) for (int j = 0; j < w; j++) arr[ij++] = init.applyAsInt(i, j);
	}

	public int get(int i, int j) {
		if (transposed) {
			int t = i;
			i = j;
			j = t;
		}
		return switch (rCnt & 3) {
			case 0 -> arr[i * w + j];
			case 1 -> arr[j * w - i + w - 1];
			case 2 -> arr[hw - i * w - j - 1];
			case 3 -> arr[hw - j * w + i - w];
			default -> throw new IllegalStateException("Unexpected value: " + rCnt);
		};
	}

	public void set(int i, int j, final int value) {
		if (transposed) {
			int t = i;
			i = j;
			j = t;
		}
		int idx = switch (rCnt & 3) {
			case 0 -> i * w + j;
			case 1 -> j * w - i + w - 1;
			case 2 -> hw - i * w - j - 1;
			case 3 -> hw - j * w + i - w;
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

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int index = 0;

			public boolean hasNext() {
				return index < hw;
			}

			public int nextInt() {
				return arr[index++];
			}
		};
	}
}
