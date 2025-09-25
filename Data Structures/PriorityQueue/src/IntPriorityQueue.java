import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * int型特化優先度キュークラス:
 */
@SuppressWarnings("unused")
public final class IntPriorityQueue implements Iterable<Integer> {
	private final boolean isDescendingOrder;
	private int[] buf;
	private int size, capacity;

	public IntPriorityQueue() {
		this(16, false);
	}

	public IntPriorityQueue(int capacity) {
		this(capacity, false);
	}

	public IntPriorityQueue(boolean isDescendingOrder) {
		this(16, isDescendingOrder);
	}

	public IntPriorityQueue(int capacity, boolean isDescendingOrder) {
		this.capacity = capacity;
		this.isDescendingOrder = isDescendingOrder;
		buf = new int[capacity];
		size = 0;
	}

	public void push(int v) {
		if (isFull()) buf = Arrays.copyOf(buf, capacity <<= 1);
		int i = size++;
		if (isDescendingOrder) siftUpMax(v, i);
		else siftUpMin(v, i);
	}

	private void siftUpMax(int v, int i) {
		while (i > 0) {
			int j = (i - 1) >> 1;
			if (v <= buf[j]) break;
			buf[i] = buf[j];
			i = j;
		}
		buf[i] = v;
	}

	private void siftUpMin(int v, int i) {
		while (i > 0) {
			int j = (i - 1) >> 1;
			if (v >= buf[j]) break;
			buf[i] = buf[j];
			i = j;
		}
		buf[i] = v;
	}

	public int poll() {
		if (isEmpty()) throw new NoSuchElementException();
		int res = buf[0];
		int v = buf[--size];
		if (size > 0) {
			if (isDescendingOrder) siftDownMax(v);
			else siftDownMin(v);
		}
		return res;
	}

	private void siftDownMax(int v) {
		int half = size >> 1;
		int i = 0;
		while (i < half) {
			int child = (i << 1) + 1, r = child + 1;
			if (r < size && buf[child] < buf[r]) {
				child = r;
			}
			if (v >= buf[child]) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
	}

	private void siftDownMin(int v) {
		int half = size >> 1;
		int i = 0;
		while (i < half) {
			int child = (i << 1) + 1, r = child + 1;
			if (r < size && buf[child] > buf[r]) {
				child = r;
			}
			if (v <= buf[child]) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
	}

	public int peek() {
		if (isEmpty()) throw new NoSuchElementException();
		return buf[0];
	}

	public int size() {
		return size;
	}

	public void clear() {
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private boolean isFull() {
		return size == capacity;
	}

	@Override
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}
}
