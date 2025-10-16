import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.StringJoiner;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings({"unused", "unchecked"})
public final class AVLMultiset<T extends Comparable<T>> implements Iterable<T> {
	// -------------- Fields --------------
	private final Comparator<? super T> comparator;
	private Node root;
	private T first, last;
	private long size;
	private int uniqueSize;

	// -------------- Constructors --------------
	public AVLMultiset() {
		this(Comparator.naturalOrder());
	}

	public AVLMultiset(final Comparator<? super T> comparator) {
		this.comparator = comparator;
		clear();
	}

	// -------------- Size & State --------------
	public long size() {
		return size;
	}

	public int uniqueSize() {
		return uniqueSize;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		root = null;
		first = last = null;
		size = uniqueSize = 0;
	}

	// -------------- String --------------
	public String toString() {
		final StringJoiner sj = new StringJoiner(", ", "[", "]");
		for (final T t : this) sj.add(t.toString());
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(final T t) {
		if (size == 0) return false;
		return count(t) > 0;
	}

	public boolean containsAll(final Collection<T> c) {
		if (size == 0) return c.isEmpty();
		boolean contains = true;
		for (final T t : c) {
			if (!contains(t)) {
				contains = false;
				break;
			}
		}
		return contains;
	}

	// -------------- Add --------------
	public boolean add(final T t) {
		return applyDeltaAndUpdate(t, 1, false);
	}

	public boolean add(T t, final long cnt) {
		if (cnt <= 0) throw new IllegalArgumentException("cnt must be > 0");
		return applyDeltaAndUpdate(t, cnt, false);
	}

	public boolean addAll(Collection<T> c) {
		final long oldSize = size;
		for (final T a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(final T t) {
		return applyDeltaAndUpdate(t, -1, false);
	}

	public boolean remove(final T t, final long cnt) {
		if (cnt <= 0) throw new IllegalArgumentException("cnt must be > 0");
		return applyDeltaAndUpdate(t, -cnt, false);
	}

	public boolean removeAll(final T t) {
		return applyDeltaAndUpdate(t, 0, true);
	}

	public boolean removeAll(final Collection<T> c) {
		if (isEmpty()) return false;
		final long oldSize = size;
		final Collection<T> hs = c instanceof Set ? c : new HashSet<>(c);
		for (final T v : hs) removeAll(v);
		return size != oldSize;
	}

	public boolean removeAt(final long index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		return removeByIndex(index, false);
	}

	public boolean removeUniqueAt(final int index) {
		if (index < 0 || uniqueSize <= index) throw new IndexOutOfBoundsException();
		return removeByIndex(index, true);
	}

	private boolean removeByIndex(final long index, final boolean unique) {
		final long oldSize = size;
		final int oldUniqueSize = uniqueSize;
		root = root.removeAt(index, unique);
		update();
		final boolean updated = size != oldSize;
		if (size > 0 && uniqueSize != oldUniqueSize) {
			if (!unique) {
				if (index == 0) first = leftmost(root).label;
				if (index == oldSize - 1) last = rightmost(root).label;
			} else {
				if (index == 0) first = leftmost(root).label;
				if (index == oldUniqueSize - 1) last = rightmost(root).label;
			}
		}
		return updated;
	}

	// -------------- Arrays --------------
	public T[] toArray() {
		if (size == 0) return (T[]) new Object[0];
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("Array too large: " + size + " elements (exceeds single-array limit)");
		final T[] arr = (T[]) Array.newInstance(first.getClass(), (int) size);
		int i = 0;
		for (final T t : this) arr[i++] = t;
		return arr;
	}

	public T[] toUniqueArray() {
		if (uniqueSize == 0) return (T[]) new Object[0];
		final Iterator<T> it = uniqueIterator();
		final T[] arr = (T[]) Array.newInstance(first.getClass(), uniqueSize);
		for (int i = 0; it.hasNext(); i++) arr[i] = it.next();
		return arr;
	}

	// -------------- Streams --------------
	public Stream<T> stream() {
		return toStream(false);
	}

	public Stream<T> uniqueStream() {
		return toStream(true);
	}

	private Stream<T> toStream(final boolean unique) {
		final long size = unique ? uniqueSize : this.size;
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		if (unique) characteristics |= Spliterator.DISTINCT;
		final Iterator<T> it = unique ? uniqueIterator() : iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public Iterator<T> iterator() {
		return new AvlIterator(root, false);
	}

	public Iterator<T> uniqueIterator() {
		return new AvlIterator(root, true);
	}

	// -------------- Access by Index --------------
	public T getByIndex(long index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			final long leftSize = cur.left == null ? 0 : cur.left.size;
			if (index < leftSize) {
				cur = cur.left;
			} else if (index >= leftSize + cur.cnt) {
				index -= leftSize + cur.cnt;
				cur = cur.right;
			} else {
				break;
			}
		}
		return cur.label;
	}

	public T getByUniqueIndex(int index) {
		if (index < 0 || uniqueSize <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			final int leftSize = cur.left == null ? 0 : cur.left.uniqueSize;
			if (index < leftSize) {
				cur = cur.left;
			} else if (index >= leftSize + 1) {
				index -= leftSize + 1;
				cur = cur.right;
			} else {
				break;
			}
		}
		return cur.label;
	}

	// -------------- Search & Rank --------------
	public long indexOf(final T t) {
		if (size == 0) return -1;
		final long index = index(t, false);
		return index >= 0 ? index : -1;
	}

	public int uniqueIndexOf(final T t) {
		if (size == 0) return -1;
		final int index = (int) index(t, true);
		return index >= 0 ? index : -1;
	}

	public long rank(final T t) {
		final long index = index(t, false);
		return index < 0 ? ~index : index;
	}

	public long uniqueRank(final T t) {
		final long index = index(t, true);
		return index < 0 ? ~index : index;
	}

	private long index(final T t, final boolean unique) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			final int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += unique ? cur.leftUniqueSize() + 1 : cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (cmp > 0) {
				cur = cur.left;
			} else {
				index += unique ? cur.leftUniqueSize() : cur.leftSize();
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	// -------------- Bounds --------------
	public long upperBound(final T t) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			final int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (cmp > 0) {
				cur = cur.left;
			} else {
				index += cur.leftSize() + cur.cnt - 1;
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	public long lowerBound(final T t) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			final int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (cmp > 0) {
				cur = cur.left;
			} else {
				index += cur.leftSize();
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	// -------------- Counts --------------
	public long count(final T t) {
		if (size == 0) return 0;
		Node cur = root;
		while (cur != null) {
			final int cmp = comparator.compare(cur.label, t);
			if (cmp == 0) break;
			cur = cmp < 0 ? cur.right : cur.left;
		}
		return cur == null ? 0 : cur.cnt;
	}

	// -------------- Navigation --------------
	public T higher(final T key) {
		return boundary(key, false, true);
	}

	public T ceiling(final T key) {
		return boundary(key, true, true);
	}

	public T lower(final T key) {
		return boundary(key, false, false);
	}

	public T floor(final T key) {
		return boundary(key, true, false);
	}

	private T boundary(final T key, final boolean inclusive, final boolean higher) {
		if (size == 0) return null;
		final int c1 = comparator.compare(first, key);
		if (c1 == 0 && inclusive) return first;
		if (c1 > 0) return higher ? first : null;
		final int c2 = comparator.compare(last, key);
		if (c2 == 0 && inclusive) return last;
		if (c2 < 0) return higher ? null : last;
		T t = null;
		Node cur = root;
		while (cur != null) {
			final T label = cur.label;
			final int c = comparator.compare(label, key);
			if (higher) {
				if (c > 0 || (inclusive && c == 0)) {
					t = label;
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			} else {
				if (c < 0 || (inclusive && c == 0)) {
					t = label;
					cur = cur.right;
				} else {
					cur = cur.left;
				}
			}
		}
		return t;
	}

	// -------------- Endpoints --------------
	public T first() {
		return first;
	}

	public T last() {
		return last;
	}

	public T pollFirst() {
		if (size == 0) return null;
		final T temp = first;
		removeByIndex(0, false);
		return temp;
	}

	public T pollLast() {
		if (size == 0) return null;
		final T temp = last;
		removeByIndex(size - 1, false);
		return temp;
	}

	public T pollFirstAll() {
		if (size == 0) return null;
		final T temp = first;
		removeByIndex(0, true);
		return temp;
	}

	public T pollLastAll() {
		if (size == 0) return null;
		final T temp = last;
		removeByIndex(uniqueSize - 1, true);
		return temp;
	}

	// -------------- Internal Helpers --------------
	private boolean applyDeltaAndUpdate(final T t, final long delta, final boolean removeAll) {
		if (size == 0) {
			if (delta <= 0 || removeAll) return false;
			first = last = t;
			root = new Node(t, null, delta);
			uniqueSize = 1;
			size = delta;
			return true;
		}
		if (!removeAll && delta > 0) {
			if (comparator.compare(t, first) < 0) first = t;
			if (comparator.compare(t, last) > 0) last = t;
		}
		final long oldSize = size;
		final int oldUniqueSize = uniqueSize;
		root = root.applyDelta(t, delta, removeAll);
		update();
		final boolean updated = size != oldSize;
		if (updated && size > 0 && uniqueSize != oldUniqueSize && (removeAll || delta <= 0)) {
			if (comparator.compare(t, first) == 0) first = leftmost(root).label;
			if (comparator.compare(t, last) == 0) last = rightmost(root).label;
		}
		return updated;
	}

	private void update() {
		if (root == null) {
			clear();
			return;
		}
		size = root.size;
		uniqueSize = root.uniqueSize;
		root.parent = null;
	}

	private Node leftmost(Node cur) {
		if (cur == null) return null;
		while (cur.left != null) cur = cur.left;
		return cur;
	}

	private Node rightmost(Node cur) {
		if (cur == null) return null;
		while (cur.right != null) cur = cur.right;
		return cur;
	}

	private Node successor(Node cur) {
		if (cur == null) return null;
		if (cur.right != null) return leftmost(cur.right);
		while (cur.parent != null && cur.parent.right == cur) cur = cur.parent;
		return cur.parent;
	}

	// -------------- Nested classes --------------
	private final class Node {
		private final T label;
		private long cnt, size;
		private int height, uniqueSize;
		private Node left, right, parent;

		private Node(final T label, final Node parent, final long cnt) {
			this.label = label;
			this.cnt = this.size = cnt;
			this.height = this.uniqueSize = 1;
			this.parent = parent;
		}

		private Node removeAt(long index, final boolean unique) {
			final long lIdx = unique ? leftUniqueSize() : leftSize();
			final long rIdx = lIdx + (unique ? 1 : cnt);
			if (rIdx <= index) {
				index -= rIdx;
				setRight(right.removeAt(index, unique));
			} else if (index < lIdx) {
				setLeft(left.removeAt(index, unique));
			} else {
				cnt--;
				if (unique || cnt <= 0) return removeInternal();
			}
			updateNode();
			final int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node applyDelta(final T t, final long delta, final boolean all) {
			final int cmp = AVLMultiset.this.comparator.compare(label, t);
			if (cmp < 0) {
				if (right == null) {
					if (delta <= 0) return this;
					setRight(new Node(t, this, delta));
				} else {
					setRight(right.applyDelta(t, delta, all));
				}
			} else if (cmp > 0) {
				if (left == null) {
					if (delta <= 0) return this;
					setLeft(new Node(t, this, delta));
				} else {
					setLeft(left.applyDelta(t, delta, all));
				}
			} else {
				cnt += delta;
				if (all || cnt <= 0) return removeInternal();
			}
			updateNode();
			final int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node removeInternal() {
			if (left == null) return right;
			if (right == null) return left;
			final Node temp;
			if (leftHeight() >= rightHeight()) {
				temp = left.extractMax();
				if (temp == left) {
					setLeft(temp.left);
				} else {
					final int bf = left.leftHeight() - left.rightHeight();
					setLeft(abs(bf) <= 1 ? left : left.rotate(bf));
				}
			} else {
				temp = right.extractMin();
				if (temp == right) {
					setRight(temp.right);
				} else {
					final int bf = right.leftHeight() - right.rightHeight();
					setRight(abs(bf) <= 1 ? right : right.rotate(bf));
				}
			}
			temp.parent = parent;
			temp.setLeft(left);
			temp.setRight(right);
			temp.updateNode();
			final int bf = temp.leftHeight() - temp.rightHeight();
			return abs(bf) <= 1 ? temp : temp.rotate(bf);
		}

		private Node extractMin() {
			if (left == null) return this;
			final Node min = left.extractMin();
			if (left == min) setLeft(left.right);
			if (left != null) {
				final int bf = left.leftHeight() - left.rightHeight();
				if (abs(bf) > 1) setLeft(left.rotate(bf));
			}
			updateNode();
			return min;
		}

		private Node extractMax() {
			if (right == null) return this;
			final Node max = right.extractMax();
			if (right == max) setRight(right.left);
			if (right != null) {
				final int bf = right.leftHeight() - right.rightHeight();
				if (abs(bf) > 1) setRight(right.rotate(bf));
			}
			updateNode();
			return max;
		}

		private Node rotate(final int bf) {
			final Node prevParent = parent;
			final Node newRoot;
			if (bf > 0) {
				final int bfl = left.leftHeight() - left.rightHeight();
				newRoot = bfl >= 0 ? rotateLL() : rotateLR();
				newRoot.right.updateNode();
			} else {
				final int bfr = right.leftHeight() - right.rightHeight();
				newRoot = bfr > 0 ? rotateRL() : rotateRR();
				newRoot.left.updateNode();
			}
			newRoot.updateNode();
			newRoot.parent = prevParent;
			return newRoot;
		}

		private Node rotateLR() {
			final Node newRoot = this.left.right;
			final Node tempLeft = newRoot.left;
			final Node tempRight = newRoot.right;
			newRoot.setRight(this);
			newRoot.setLeft(this.left);
			newRoot.right.setLeft(tempRight);
			newRoot.left.setRight(tempLeft);
			newRoot.left.updateNode();
			return newRoot;
		}

		private Node rotateLL() {
			final Node newRoot = this.left;
			setLeft(newRoot.right);
			newRoot.setRight(this);
			return newRoot;
		}

		private Node rotateRR() {
			final Node newRoot = this.right;
			setRight(newRoot.left);
			newRoot.setLeft(this);
			return newRoot;
		}

		private Node rotateRL() {
			final Node newRoot = this.right.left;
			final Node tempLeft = newRoot.left;
			final Node tempRight = newRoot.right;
			newRoot.setLeft(this);
			newRoot.setRight(this.right);
			newRoot.left.setRight(tempLeft);
			newRoot.right.setLeft(tempRight);
			newRoot.right.updateNode();
			return newRoot;
		}

		private void setLeft(final Node child) {
			left = child;
			if (child != null) child.parent = this;
		}

		private void setRight(final Node child) {
			right = child;
			if (child != null) child.parent = this;
		}

		private void updateNode() {
			size = leftSize() + rightSize() + cnt;
			height = 1 + max(leftHeight(), rightHeight());
			uniqueSize = leftUniqueSize() + rightUniqueSize() + 1;
		}

		private int leftHeight() {
			return left == null ? 0 : left.height;
		}

		private int rightHeight() {
			return right == null ? 0 : right.height;
		}

		private long leftSize() {
			return left == null ? 0 : left.size;
		}

		private long rightSize() {
			return right == null ? 0 : right.size;
		}

		private int leftUniqueSize() {
			return left == null ? 0 : left.uniqueSize;
		}

		private int rightUniqueSize() {
			return right == null ? 0 : right.uniqueSize;
		}
	}

	private final class AvlIterator implements Iterator<T> {
		private final boolean unique;
		private Node cur;
		private long remainingCnt;

		AvlIterator(final Node root, final boolean unique) {
			this.unique = unique;
			cur = leftmost(root);
			remainingCnt = cur == null ? 0 : cur.cnt;
		}

		public boolean hasNext() {
			return cur != null;
		}

		public T next() {
			if (cur == null) throw new NoSuchElementException();
			final T label = cur.label;
			if (!unique && remainingCnt > 1) {
				remainingCnt--;
				return label;
			}
			cur = successor(cur);
			remainingCnt = cur == null ? 0 : cur.cnt;
			return label;
		}
	}
}
