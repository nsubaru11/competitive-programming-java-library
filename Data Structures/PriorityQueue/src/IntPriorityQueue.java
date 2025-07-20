import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
		if (isFull()) {
			buf = Arrays.copyOf(buf, capacity *= 2);
		}
		int i = size++;
		buf[i] = v;
		while (i > 0) {
			int j = (i - 1) / 2;
			if (isDescendingOrder) {
				if (buf[i] <= buf[j]) break;
			} else {
				if (buf[i] >= buf[j]) break;
			}
			int temp = buf[i];
			buf[i] = buf[j];
			buf[j] = temp;
			i = j;
		}
	}

	public int poll() {
		if (isEmpty()) throw new NoSuchElementException();
		int res = buf[0];
		buf[0] = buf[--size];
		int i = 0;
		while (true) {
			int l = 2 * i + 1, r = l + 1, m = i;
			if (l < size && ((isDescendingOrder && buf[l] > buf[m]) || (!isDescendingOrder && buf[l] < buf[m]))) {
				m = l;
			}
			if (r < size && ((isDescendingOrder && buf[r] > buf[m]) || (!isDescendingOrder && buf[r] < buf[m]))) {
				m = r;
			}
			if (m == i) break;
			int temp = buf[i];
			buf[i] = buf[m];
			buf[m] = temp;
			i = m;
		}
		return res;
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
	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public Integer next() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}
}
