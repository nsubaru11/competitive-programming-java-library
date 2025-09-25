import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * long型特化優先度キュークラス:
 */
@SuppressWarnings("unused")
public final class LongPriorityQueue implements Iterable<Long> {
	private final boolean isDescendingOrder;
	private long[] buf;
	private int size, capacity;

	public LongPriorityQueue() {
		this(16, false);
	}

	public LongPriorityQueue(int capacity) {
		this(capacity, false);
	}

	public LongPriorityQueue(boolean isDescendingOrder) {
		this(16, isDescendingOrder);
	}

	public LongPriorityQueue(int capacity, boolean isDescendingOrder) {
		this.capacity = capacity;
		this.isDescendingOrder = isDescendingOrder;
		buf = new long[capacity];
		size = 0;
	}

	public void push(long v) {
		if (isFull()) buf = Arrays.copyOf(buf, capacity <<= 1);
		int i = size++;
		if (isDescendingOrder) siftUpMax(v, i);
		else siftUpMin(v, i);
	}

	private void siftUpMax(long v, int i) {
		while (i > 0) {
			int j = (i - 1) >> 1;
			if (v <= buf[j]) break;
			buf[i] = buf[j];
			i = j;
		}
		buf[i] = v;
	}

	private void siftUpMin(long v, int i) {
		while (i > 0) {
			int j = (i - 1) >> 1;
			if (v >= buf[j]) break;
			buf[i] = buf[j];
			i = j;
		}
		buf[i] = v;
	}

	public long poll() {
		if (isEmpty()) throw new NoSuchElementException();
		long res = buf[0];
		long v = buf[--size];
		if (size > 0) {
			if (isDescendingOrder) siftDownMax(v);
			else siftDownMin(v);
		}
		return res;
	}

	private void siftDownMax(long v) {
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

	private void siftDownMin(long v) {
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

	public long peek() {
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
	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}
}
