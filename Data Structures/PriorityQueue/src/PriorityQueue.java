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
		if (isFull()) {
			buf = Arrays.copyOf(buf, capacity *= 2);
		}
		int i = size++;
		buf[i] = v;
		while (i > 0) {
			int j = (i - 1) / 2;
			if (comparator.compare(buf[i], buf[j]) >= 0) break;
			T temp = buf[i];
			buf[i] = buf[j];
			buf[j] = temp;
			i = j;
		}
	}

	public T poll() {
		if (isEmpty()) throw new NoSuchElementException();
		T res = buf[0];
		buf[0] = buf[--size];
		int i = 0;
		while (true) {
			int l = 2 * i + 1, r = l + 1, m = i;
			if (l < size && comparator.compare(buf[l], buf[m]) < 0) {
				m = l;
			}
			if (r < size && comparator.compare(buf[r], buf[m]) < 0) {
				m = r;
			}
			if (m == i) break;
			T temp = buf[i];
			buf[i] = buf[m];
			buf[m] = temp;
			i = m;
		}
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
