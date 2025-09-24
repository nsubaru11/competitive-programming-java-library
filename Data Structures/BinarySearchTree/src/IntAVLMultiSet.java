import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.StringJoiner;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings("unused")
public final class IntAVLMultiSet implements Iterable<Integer> {
	// -------------- Fields --------------
	private Node root;
	private int first, last;
	private long size;
	private int uniqueSize;

	// -------------- Constructors --------------
	public IntAVLMultiSet() {
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
		first = last = 0;
		size = uniqueSize = 0;
	}

	// -------------- String --------------
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		PrimitiveIterator.OfInt it = iterator();
		while (it.hasNext()) sj.add(Integer.toString(it.nextInt()));
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(final int t) {
		if (size == 0) return false;
		return count(t) > 0;
	}

	public boolean containsAll(final Collection<Integer> c) {
		if (size == 0) return c.isEmpty();
		boolean contains = true;
		for (int t : c) {
			if (!contains(t)) {
				contains = false;
				break;
			}
		}
		return contains;
	}

	// -------------- Add --------------
	public boolean add(final int t) {
		return applyDeltaAndUpdate(t, 1, false);
	}

	public boolean add(final int t, final long cnt) {
		if (cnt <= 0) throw new IllegalArgumentException("cnt must be > 0");
		return applyDeltaAndUpdate(t, cnt, false);
	}

	public boolean addAll(final Collection<Integer> c) {
		long oldSize = size;
		for (int a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(final int t) {
		return applyDeltaAndUpdate(t, -1, false);
	}

	public boolean remove(final int t, final long cnt) {
		if (cnt <= 0) throw new IllegalArgumentException("cnt must be > 0");
		return applyDeltaAndUpdate(t, -cnt, false);
	}

	public boolean removeAll(final int t) {
		return applyDeltaAndUpdate(t, 0, true);
	}

	public boolean removeAll(final Collection<Integer> c) {
		if (isEmpty()) return false;
		long oldSize = size;
		Collection<Integer> hs = c instanceof Set ? c : new HashSet<>(c);
		for (int v : hs) removeAll(v);
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
		long oldSize = size;
		int oldUniqueSize = uniqueSize;
		root = root.removeAt(index, unique);
		update();
		boolean updated = size != oldSize;
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
	public int[] toArray() {
		if (size == 0) return new int[0];
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("Array too large: " + size + " elements (exceeds single-array limit)");
		int[] arr = new int[(int) size];
		PrimitiveIterator.OfInt it = iterator();
		for (int i = 0; it.hasNext(); i++) arr[i] = it.nextInt();
		return arr;
	}

	public int[] toUniqueArray() {
		if (uniqueSize == 0) return new int[0];
		int[] arr = new int[uniqueSize];
		PrimitiveIterator.OfInt it = uniqueIterator();
		for (int i = 0; it.hasNext(); i++) arr[i] = it.nextInt();
		return arr;
	}

	// -------------- Streams --------------
	public Stream<Integer> stream() {
		return toStream(false);
	}

	public Stream<Integer> uniqueStream() {
		return toStream(true);
	}

	private Stream<Integer> toStream(final boolean unique) {
		long size = unique ? uniqueSize : this.size;
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		if (unique) characteristics |= Spliterator.DISTINCT;
		PrimitiveIterator.OfInt it = unique ? uniqueIterator() : iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public PrimitiveIterator.OfInt iterator() {
		return new AvlIterator(root, false);
	}

	public PrimitiveIterator.OfInt uniqueIterator() {
		return new AvlIterator(root, true);
	}

	// -------------- Access by Index --------------
	public int getByIndex(long index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			long leftSize = cur.left == null ? 0 : cur.left.size;
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

	public int getByUniqueIndex(int index) {
		if (index < 0 || uniqueSize <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			int leftSize = cur.left == null ? 0 : cur.left.uniqueSize;
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
	public long indexOf(final int t) {
		if (size == 0) return -1;
		long index = index(t, false);
		return index >= 0 ? index : -1;
	}

	public int uniqueIndexOf(final int t) {
		if (size == 0) return -1;
		int index = (int) index(t, true);
		return index >= 0 ? index : -1;
	}

	public long rank(final int t) {
		long index = index(t, false);
		return index < 0 ? ~index : index;
	}

	public long uniqueRank(final int t) {
		long index = index(t, true);
		return index < 0 ? ~index : index;
	}

	private long index(final int t, final boolean unique) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			if (cur.label < t) {
				index += unique ? cur.leftUniqueSize() + 1 : cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (cur.label > t) {
				cur = cur.left;
			} else {
				index += unique ? cur.leftUniqueSize() : cur.leftSize();
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	// -------------- Bounds --------------
	public long upperBound(final int t) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			if (cur.label < t) {
				index += cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (cur.label > t) {
				cur = cur.left;
			} else {
				index += cur.leftSize() + cur.cnt - 1;
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	public long lowerBound(final int t) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			if (cur.label < t) {
				index += cur.leftSize() + cur.cnt;
				cur = cur.right;
			} else if (cur.label > t) {
				cur = cur.left;
			} else {
				index += cur.leftSize();
				break;
			}
		}
		return cur == null ? ~index : index;
	}

	// -------------- Counts --------------
	public long count(final int t) {
		if (size == 0) return 0;
		Node cur = root;
		while (cur != null) {
			if (cur.label < t) {
				cur = cur.right;
			} else if (cur.label > t) {
				cur = cur.left;
			} else {
				break;
			}
		}
		return cur == null ? 0 : cur.cnt;
	}

	// -------------- Navigation --------------
	public Integer higher(final int key) {
		return boundary(key, false, true);
	}

	public Integer ceiling(final int key) {
		return boundary(key, true, true);
	}

	public Integer lower(final int key) {
		return boundary(key, false, false);
	}

	public Integer floor(final int key) {
		return boundary(key, true, false);
	}

	private Integer boundary(final int key, final boolean inclusive, final boolean higher) {
		if (size == 0) return null;
		if (first == key && inclusive) return first;
		if (first > key) return higher ? first : null;
		if (last == key && inclusive) return last;
		if (last < key) return higher ? null : last;
		Integer t = null;
		Node cur = root;
		while (cur != null) {
			if (higher) {
				if (cur.label > key || (inclusive && cur.label == key)) {
					t = cur.label;
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			} else {
				if (cur.label < key || (inclusive && cur.label == key)) {
					t = cur.label;
					cur = cur.right;
				} else {
					cur = cur.left;
				}
			}
		}
		return t;
	}

	// -------------- Endpoints --------------
	public int first() {
		return first;
	}

	public int last() {
		return last;
	}

	public int pollFirst() {
		if (size == 0) throw new NoSuchElementException();
		int temp = first;
		removeByIndex(0, false);
		return temp;
	}

	public int pollLast() {
		if (size == 0) throw new NoSuchElementException();
		int temp = last;
		removeByIndex(size - 1, false);
		return temp;
	}

	public int pollFirstAll() {
		if (size == 0) throw new NoSuchElementException();
		int temp = first;
		removeByIndex(0, true);
		return temp;
	}

	public int pollLastAll() {
		if (size == 0) throw new NoSuchElementException();
		int temp = last;
		removeByIndex(uniqueSize - 1, true);
		return temp;
	}

	// -------------- Internal Helpers --------------
	private boolean applyDeltaAndUpdate(final int t, final long delta, final boolean removeAll) {
		if (size == 0) {
			if (delta <= 0 || removeAll) return false;
			first = last = t;
			root = new Node(t, null, delta);
			uniqueSize = 1;
			size = delta;
			return true;
		}
		if (!removeAll && delta > 0) {
			if (t < first) first = t;
			if (t > last) last = t;
		}
		long oldSize = size;
		int oldUniqueSize = uniqueSize;
		root = root.applyDelta(t, delta, removeAll);
		update();
		boolean updated = size != oldSize;
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
		uniqueSize = root.uniqueSize;
		size = root.size;
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
		private final int label;
		private long cnt, size;
		private int height, uniqueSize;
		private Node left, right, parent;

		private Node(final int label, final Node parent, final long cnt) {
			this.label = label;
			this.cnt = this.size = cnt;
			this.height = this.uniqueSize = 1;
			this.parent = parent;
		}

		private Node removeAt(long index, final boolean unique) {
			long lIdx = unique ? leftUniqueSize() : leftSize();
			long rIdx = lIdx + (unique ? 1 : cnt);
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
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node applyDelta(final int t, final long delta, final boolean all) {
			if (label < t) {
				if (right == null) {
					if (delta <= 0) return this;
					setRight(new Node(t, this, delta));
				} else {
					setRight(right.applyDelta(t, delta, all));
				}
			} else if (label > t) {
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
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node removeInternal() {
			if (left == null) return right;
			if (right == null) return left;
			Node temp;
			if (leftHeight() >= rightHeight()) {
				temp = left.extractMax();
				if (temp == left) {
					setLeft(temp.left);
				} else {
					int bf = left.leftHeight() - left.rightHeight();
					setLeft(abs(bf) <= 1 ? left : left.rotate(bf));
				}
			} else {
				temp = right.extractMin();
				if (temp == right) {
					setRight(temp.right);
				} else {
					int bf = right.leftHeight() - right.rightHeight();
					setRight(abs(bf) <= 1 ? right : right.rotate(bf));
				}
			}
			temp.parent = parent;
			temp.setLeft(left);
			temp.setRight(right);
			temp.updateNode();
			int bf = temp.leftHeight() - temp.rightHeight();
			return abs(bf) <= 1 ? temp : temp.rotate(bf);
		}

		private Node extractMin() {
			if (left == null) return this;
			Node min = left.extractMin();
			if (left == min) setLeft(left.right);
			if (left != null) {
				int bf = left.leftHeight() - left.rightHeight();
				if (abs(bf) > 1) setLeft(left.rotate(bf));
			}
			updateNode();
			return min;
		}

		private Node extractMax() {
			if (right == null) return this;
			Node max = right.extractMax();
			if (right == max) setRight(right.left);
			if (right != null) {
				int bf = right.leftHeight() - right.rightHeight();
				if (abs(bf) > 1) setRight(right.rotate(bf));
			}
			updateNode();
			return max;
		}

		private Node rotate(final int bf) {
			Node prevParent = parent;
			Node newRoot;
			if (bf > 0) {
				int bfl = left.leftHeight() - left.rightHeight();
				newRoot = bfl >= 0 ? rotateLL() : rotateLR();
				newRoot.right.updateNode();
			} else {
				int bfr = right.leftHeight() - right.rightHeight();
				newRoot = bfr > 0 ? rotateRL() : rotateRR();
				newRoot.left.updateNode();
			}
			newRoot.updateNode();
			newRoot.parent = prevParent;
			return newRoot;
		}

		private Node rotateLR() {
			Node newRoot = this.left.right;
			Node tempLeft = newRoot.left;
			Node tempRight = newRoot.right;
			newRoot.setRight(this);
			newRoot.setLeft(this.left);
			newRoot.right.setLeft(tempRight);
			newRoot.left.setRight(tempLeft);
			newRoot.left.updateNode();
			return newRoot;
		}

		private Node rotateLL() {
			Node newRoot = this.left;
			setLeft(newRoot.right);
			newRoot.setRight(this);
			return newRoot;
		}

		private Node rotateRR() {
			Node newRoot = this.right;
			setRight(newRoot.left);
			newRoot.setLeft(this);
			return newRoot;
		}

		private Node rotateRL() {
			Node newRoot = this.right.left;
			Node tempLeft = newRoot.left;
			Node tempRight = newRoot.right;
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

	private final class AvlIterator implements PrimitiveIterator.OfInt {
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

		public int nextInt() {
			if (cur == null) throw new NoSuchElementException();
			int val = cur.label;
			if (!unique && remainingCnt > 1) {
				remainingCnt--;
				return val;
			}
			cur = successor(cur);
			remainingCnt = cur == null ? 0 : cur.cnt;
			return val;
		}
	}
}
