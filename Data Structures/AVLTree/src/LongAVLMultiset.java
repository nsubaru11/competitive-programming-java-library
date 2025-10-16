import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.StringJoiner;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings("unused")
public final class LongAVLMultiset implements Iterable<Long> {
	// -------------- Fields --------------
	private Node root;
	private long first, last;
	private long size, sum, uniqueSum;
	private int uniqueSize;

	// -------------- Constructors --------------
	public LongAVLMultiset() {
		clear();
	}

	// -------------- Size & State --------------
	public long sum() {
		return sum;
	}

	public long uniqueSum() {
		return uniqueSum;
	}

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
		first = last = 0;
		sum = uniqueSum = 0;
		size = uniqueSize = 0;
	}

	// -------------- String --------------
	public String toString() {
		final StringJoiner sj = new StringJoiner(", ", "[", "]");
		final PrimitiveIterator.OfLong it = iterator();
		while (it.hasNext()) sj.add(Long.toString(it.nextLong()));
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(final long t) {
		if (size == 0) return false;
		return count(t) > 0;
	}

	public boolean containsAll(final Collection<Long> c) {
		if (size == 0) return c.isEmpty();
		boolean contains = true;
		for (final long t : c) {
			if (!contains(t)) {
				contains = false;
				break;
			}
		}
		return contains;
	}

	// -------------- Add --------------
	public boolean add(final long t) {
		return applyDeltaAndUpdate(t, 1, false);
	}

	public boolean add(final long t, final long cnt) {
		if (cnt <= 0) throw new IllegalArgumentException("cnt must be > 0");
		return applyDeltaAndUpdate(t, cnt, false);
	}

	public boolean addAll(final Collection<Long> c) {
		final long oldSize = size;
		for (final long a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(final long t) {
		return applyDeltaAndUpdate(t, -1, false);
	}

	public boolean remove(final long t, final long cnt) {
		if (cnt <= 0) throw new IllegalArgumentException("cnt must be > 0");
		return applyDeltaAndUpdate(t, -cnt, false);
	}

	public boolean removeAll(final long t) {
		return applyDeltaAndUpdate(t, 0, true);
	}

	public boolean removeAll(final Collection<Long> c) {
		if (isEmpty()) return false;
		final long oldSize = size;
		final Collection<Long> hs = c instanceof Set ? c : new HashSet<>(c);
		for (final long v : hs) removeAll(v);
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
	public long[] toArray() {
		if (size == 0) return new long[0];
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("Array too large: " + size + " elements (exceeds single-array limit)");
		final long[] arr = new long[(int) size];
		final PrimitiveIterator.OfLong it = iterator();
		for (int i = 0; it.hasNext(); i++) arr[i] = it.nextLong();
		return arr;
	}

	public long[] toUniqueArray() {
		if (uniqueSize == 0) return new long[0];
		final long[] arr = new long[uniqueSize];
		final PrimitiveIterator.OfLong it = uniqueIterator();
		for (int i = 0; it.hasNext(); i++) arr[i] = it.nextLong();
		return arr;
	}

	// -------------- Streams --------------
	public LongStream stream() {
		return toStream(false);
	}

	public LongStream uniqueStream() {
		return toStream(true);
	}

	private LongStream toStream(final boolean unique) {
		final long size = unique ? uniqueSize : this.size;
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		if (unique) characteristics |= Spliterator.DISTINCT;
		final PrimitiveIterator.OfLong it = unique ? uniqueIterator() : iterator();
		return StreamSupport.longStream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public PrimitiveIterator.OfLong iterator() {
		return new AvlIterator(root, false);
	}

	public PrimitiveIterator.OfLong uniqueIterator() {
		return new AvlIterator(root, true);
	}

	// -------------- Access by Index --------------
	public long getByIndex(long index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		return getByIndex(index, false).label;
	}

	public long getByUniqueIndex(int index) {
		if (index < 0 || uniqueSize <= index) throw new IndexOutOfBoundsException();
		return getByIndex(index, true).label;
	}

	private Node getByIndex(long index, boolean unique) {
		Node cur = root;
		while (cur != null) {
			final Node left = cur.left;
			final long leftSize = left == null ? 0 : unique ? left.uniqueSize : left.size;
			final long delta = unique ? 1 : cur.cnt;
			if (index < leftSize) {
				cur = left;
			} else if (index >= leftSize + delta) {
				index -= leftSize + delta;
				cur = cur.right;
			} else {
				break;
			}
		}
		return cur;
	}

	// -------------- Search & Rank --------------
	public long indexOf(final long t) {
		if (size == 0) return -1;
		final long index = index(t, false);
		return index >= 0 ? index : -1;
	}

	public int uniqueIndexOf(final long t) {
		if (size == 0) return -1;
		final int index = (int) index(t, true);
		return index >= 0 ? index : -1;
	}

	public long rank(final long t) {
		final long index = index(t, false);
		return index < 0 ? ~index : index;
	}

	public long uniqueRank(final long t) {
		final long index = index(t, true);
		return index < 0 ? ~index : index;
	}

	private long index(final long t, final boolean unique) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			final long label = cur.label;
			if (label < t) {
				index += unique ? cur.leftUniqueSize() + 1 : cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (label > t) {
				cur = cur.left;
			} else {
				index += unique ? cur.leftUniqueSize() : cur.leftSize();
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	// -------------- Bounds --------------
	public long upperBound(final long t) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			final long label = cur.label;
			if (label < t) {
				index += cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (label > t) {
				cur = cur.left;
			} else {
				index += cur.leftSize() + cur.cnt - 1;
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	public long lowerBound(final long t) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			final long label = cur.label;
			if (label < t) {
				index += cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (label > t) {
				cur = cur.left;
			} else {
				index += cur.leftSize();
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	// -------------- Counts --------------
	public long count(final long t) {
		if (size == 0) return 0;
		Node cur = root;
		while (cur != null) {
			final long label = cur.label;
			if (label < t) {
				cur = cur.right;
			} else if (label > t) {
				cur = cur.left;
			} else {
				break;
			}
		}
		return cur == null ? 0 : cur.cnt;
	}

	// -------------- Navigation --------------
	public Long higher(final long key) {
		return boundary(key, false, true);
	}

	public Long ceiling(final long key) {
		return boundary(key, true, true);
	}

	public Long lower(final long key) {
		return boundary(key, false, false);
	}

	public Long floor(final long key) {
		return boundary(key, true, false);
	}

	private Long boundary(final long key, final boolean inclusive, final boolean higher) {
		if (size == 0) return null;
		if (first == key && inclusive) return first;
		if (first > key) return higher ? first : null;
		if (last == key && inclusive) return last;
		if (last < key) return higher ? null : last;
		Long t = null;
		Node cur = root;
		while (cur != null) {
			final long label = cur.label;
			if (higher) {
				if (label > key || (inclusive && label == key)) {
					t = label;
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			} else {
				if (label < key || (inclusive && label == key)) {
					t = label;
					cur = cur.right;
				} else {
					cur = cur.left;
				}
			}
		}
		return t;
	}

	// -------------- sum --------------
	public long prefixSum(final int i) {
		if (i < 0 || size <= i) throw new IndexOutOfBoundsException();
		final Node t = getByIndex(i, false);
		return t.sum - t.rightSum();
	}

	public long prefixUniqueSum(final int i) {
		if (i < 0 || uniqueSize <= i) throw new IndexOutOfBoundsException();
		final Node t = getByIndex(i, true);
		return t.uniqueSum - t.rightUniqueSum();
	}

	public long suffixSum(final int i) {
		if (i < 0 || size <= i) throw new IndexOutOfBoundsException();
		final Node t = getByIndex(i, false);
		return t.sum - t.leftSum();
	}

	public long suffixUniqueSum(final int i) {
		if (i < 0 || uniqueSize <= i) throw new IndexOutOfBoundsException();
		final Node t = getByIndex(i, true);
		return t.uniqueSum - t.leftUniqueSum();
	}

	public long sumRange(final int l, final int r) {
		if (l > r) throw new IndexOutOfBoundsException();
		return prefixSum(r) - prefixSum(l);
	}

	public long uniqueSumRange(final int l, final int r) {
		if (l > r) throw new IndexOutOfBoundsException();
		return prefixUniqueSum(r) - prefixUniqueSum(l);
	}

	// -------------- Endpoints --------------
	public long first() {
		return first;
	}

	public long last() {
		return last;
	}

	public long pollFirst() {
		if (size == 0) throw new NoSuchElementException();
		final long temp = first;
		removeByIndex(0, false);
		return temp;
	}

	public long pollLast() {
		if (size == 0) throw new NoSuchElementException();
		final long temp = last;
		removeByIndex(size - 1, false);
		return temp;
	}

	public long pollFirstAll() {
		if (size == 0) throw new NoSuchElementException();
		final long temp = first;
		removeByIndex(0, true);
		return temp;
	}

	public long pollLastAll() {
		if (size == 0) throw new NoSuchElementException();
		final long temp = last;
		removeByIndex(uniqueSize - 1, true);
		return temp;
	}

	// -------------- Internal Helpers --------------
	private boolean applyDeltaAndUpdate(final long t, final long delta, final boolean removeAll) {
		if (size == 0) {
			if (delta <= 0 || removeAll) return false;
			first = last = t;
			root = new Node(t, null, delta);
			update();
			return true;
		}
		if (!removeAll && delta > 0) {
			if (t < first) first = t;
			if (t > last) last = t;
		}
		final long oldSize = size;
		final int oldUniqueSize = uniqueSize;
		root = root.applyDelta(t, delta, removeAll);
		update();
		final boolean updated = size != oldSize;
		if (updated && size > 0 && uniqueSize != oldUniqueSize && (removeAll || delta <= 0)) {
			if (t == first) first = leftmost(root).label;
			if (t == last) last = rightmost(root).label;
		}
		return updated;
	}

	private void update() {
		if (root == null) {
			clear();
			return;
		}
		root.parent = null;
		sum = root.sum;
		uniqueSum = root.uniqueSum;
		size = root.size;
		uniqueSize = root.uniqueSize;
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
	private static final class Node {
		private final long label;
		private long cnt, size, sum, uniqueSum;
		private int height, uniqueSize;
		private Node left, right, parent;

		private Node(final long label, final Node parent, final long cnt) {
			this.label = label;
			this.sum = label * cnt;
			this.uniqueSum = label;
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
				updateNode();
			} else if (index < lIdx) {
				setLeft(left.removeAt(index, unique));
				updateNode();
			} else {
				if (unique || cnt <= 1) return removeInternal();
				cnt--;
				size--;
				sum -= label;
			}
			final int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node applyDelta(final long t, final long delta, final boolean all) {
			if (label < t) {
				if (right == null) {
					if (delta <= 0) return this;
					setRight(new Node(t, this, delta));
				} else {
					setRight(right.applyDelta(t, delta, all));
				}
				updateNode();
			} else if (label > t) {
				if (left == null) {
					if (delta <= 0) return this;
					setLeft(new Node(t, this, delta));
				} else {
					setLeft(left.applyDelta(t, delta, all));
				}
				updateNode();
			} else {
				if (all || cnt <= -delta) return removeInternal();
				cnt += delta;
				size += delta;
				sum += label * delta;
			}
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
			sum = leftSum() + rightSum() + label * cnt;
			uniqueSum = leftUniqueSum() + rightUniqueSum() + label;
			size = leftSize() + rightSize() + cnt;
			height = 1 + max(leftHeight(), rightHeight());
			uniqueSize = leftUniqueSize() + rightUniqueSize() + 1;
		}

		private long leftSum() {
			return left == null ? 0 : left.sum;
		}

		private long rightSum() {
			return right == null ? 0 : right.sum;
		}

		private long leftUniqueSum() {
			return left == null ? 0 : left.uniqueSum;
		}

		private long rightUniqueSum() {
			return right == null ? 0 : right.uniqueSum;
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

	private final class AvlIterator implements PrimitiveIterator.OfLong {
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

		public long nextLong() {
			if (cur == null) throw new NoSuchElementException();
			final long label = cur.label;
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
