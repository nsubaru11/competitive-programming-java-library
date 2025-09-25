import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 優先度キュークラス
 */
@SuppressWarnings({"unchecked", "unused"})
public final class PriorityQueue<T extends Comparable<T>> implements Iterable<T> {
	private final Comparator<? super T> comparator;
	private T[] buf;
	private int size, capacity;

	public PriorityQueue() {
		this(16, Comparator.naturalOrder(), false);
	}

	public PriorityQueue(int capacity) {
		this(capacity, Comparator.naturalOrder(), false);
	}

	public PriorityQueue(Comparator<T> comparator) {
		this(16, comparator, false);
	}

	public PriorityQueue(boolean isDescendingOrder) {
		this(16, Comparator.naturalOrder(), isDescendingOrder);
	}

	public PriorityQueue(int capacity, Comparator<T> comparator) {
		this(capacity, comparator, false);
	}

	public PriorityQueue(Comparator<T> comparator, boolean isDescendingOrder) {
		this(16, comparator, isDescendingOrder);
	}

	public PriorityQueue(int capacity, boolean isDescendingOrder) {
		this(capacity, Comparator.naturalOrder(), isDescendingOrder);
	}

	public PriorityQueue(int capacity, Comparator<T> comparator, boolean isDescendingOrder) {
		this.capacity = capacity;
		this.comparator = isDescendingOrder ? comparator.reversed() : comparator;
		buf = (T[]) new Comparable<?>[capacity];
		size = 0;
	}

	public void push(T v) {
		if (isFull()) buf = Arrays.copyOf(buf, capacity *= 2);
		int i = size++;
		while (i > 0) {
			int j = (i - 1) >> 1;
			if (comparator.compare(v, buf[j]) >= 0) break;
			buf[i] = buf[j];
			i = j;
		}
		buf[i] = v;
	}

	public T poll() {
		if (isEmpty()) throw new NoSuchElementException();
		T res = buf[0];
		T v = buf[--size];
		int half = size >> 1;
		int i = 0;
		while (i < half) {
			int child = (i << 1) + 1, r = child + 1;
			if (r < size && comparator.compare(buf[child], buf[r]) > 0) {
				child = r;
			}
			if (comparator.compare(v, buf[child]) <= 0) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
		return res;
	}

	public T peek() {
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
	public Iterator<T> iterator() {
		return new Iterator<>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}
}
