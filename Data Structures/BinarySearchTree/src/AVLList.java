import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings({"unused", "unchecked"})
public final class AVLList<T extends Comparable<T>> implements Iterable<T> {
	private final Comparator<? super T> comparator;
	private Node root;
	private T first, last;
	private long size;
	private int distinctSize;

	// -------------- Constructors --------------
	public AVLList() {
		this(Comparator.naturalOrder());
	}

	public AVLList(Comparator<? super T> comparator) {
		this.comparator = comparator;
		clear();
	}

	// -------------- Size & State --------------
	public long size() {
		return size;
	}

	public int distinctSize() {
		return distinctSize;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		root = null;
		first = last = null;
		size = distinctSize = 0;
	}

	// -------------- Add --------------
	public boolean add(T t) {
		return add(t, 1);
	}

	public boolean add(T t, int occurrences) {
		if (size == 0) {
			first = last = t;
			root = new Node(t, null, occurrences);
			distinctSize = 1;
			size = occurrences;
			return true;
		}
		if (comparator.compare(t, first) < 0) first = t;
		if (comparator.compare(t, last) > 0) last = t;
		root = root.applyDelta(t, occurrences, false);
		boolean isNotContain = size != root.size;
		size = root.size;
		distinctSize = root.distinctSize;
		root.parent = null;
		return isNotContain;
	}

	public boolean addAll(Collection<T> c) {
		long oldSize = size;
		for (T a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(T t) {
		return remove(t, 1);
	}

	public boolean remove(T t, int occurrences) {
		if (size == 0) return false;
		long oldSize = size;
		Node newRoot = root.applyDelta(t, -occurrences, false);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			size = root.size;
			if (distinctSize != root.distinctSize) {
				if (comparator.compare(t, first) == 0) first = leftmost(root).label;
				if (comparator.compare(t, last) == 0) last = rightmost(root).label;
			}
			distinctSize = root.distinctSize;
		}
		return size != oldSize;
	}

	public boolean removeAllOccurrences(T t) {
		if (size == 0) return false;
		long oldSize = size;
		Node newRoot = root.applyDelta(t, 0, true);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			size = root.size;
			if (distinctSize != root.distinctSize) {
				if (comparator.compare(t, first) == 0) first = leftmost(root).label;
				if (comparator.compare(t, last) == 0) last = rightmost(root).label;
			}
			distinctSize = root.distinctSize;
		}
		return oldSize != size;
	}

	public boolean removeAll(Collection<T> c) {
		if (isEmpty()) return false;
		long oldSize = size;
		HashSet<?> hs = new HashSet<>(c);
		for (Iterator<T> it = distinctIterator(); it.hasNext(); ) {
			T v = it.next();
			if (hs.contains(v)) {
				removeAllOccurrences(v);
			}
		}
		return size != oldSize;
	}

	public boolean removeAt(long index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		long oldSize = size;
		Node newRoot = root.removeAt(index, false);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			if (distinctSize != root.distinctSize) {
				if (index == 0) first = leftmost(root).label;
				if (index == size - 1) last = rightmost(root).label;
			}
			size = root.size;
			distinctSize = root.distinctSize;
		}
		return size != oldSize;
	}

	public boolean removeDistinctAt(int index) {
		if (index < 0 || distinctSize <= index) throw new IndexOutOfBoundsException();
		long oldSize = size;
		Node newRoot = root.removeAt(index, true);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			size = root.size;
			if (distinctSize != root.distinctSize) {
				if (index == 0) first = leftmost(root).label;
				if (index == distinctSize - 1) last = rightmost(root).label;
			}
			distinctSize = root.distinctSize;
		}
		return size != oldSize;
	}

	// -------------- Arrays --------------
	public T[] toArray() {
		if (size == 0) return (T[]) new Object[0];
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("Array too large: " + size + " elements (exceeds single-array limit)");
		T[] arr = (T[]) Array.newInstance(first.getClass(), (int) size);
		int i = 0;
		for (T t : this) arr[i++] = t;
		return arr;
	}

	public T[] toDistinctArray() {
		if (distinctSize == 0) return (T[]) new Object[0];
		Iterator<T> it = distinctIterator();
		T[] arr = (T[]) Array.newInstance(first.getClass(), distinctSize);
		for (int i = 0; it.hasNext(); i++) arr[i] = it.next();
		return arr;
	}

	// -------------- Streams --------------
	public Stream<T> stream() {
		return toStream(false);
	}

	public Stream<T> distinctStream() {
		return toStream(true);
	}

	private Stream<T> toStream(boolean distinct) {
		long size = distinct ? distinctSize : this.size;
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.SORTED;
		if (distinct) characteristics |= Spliterator.DISTINCT;
		Iterator<T> it = distinct ? distinctIterator() : iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public Iterator<T> iterator() {
		return new AvlIterator(root, false);
	}

	public Iterator<T> distinctIterator() {
		return new AvlIterator(root, true);
	}

	private final class AvlIterator implements Iterator<T> {
		private final boolean distinct;
		private Node cur;
		private long remainingCnt;
		private boolean first = true;

		AvlIterator(Node root, boolean distinct) {
			this.distinct = distinct;
			this.cur = root;
		}

		public boolean hasNext() {
			return cur != null;
		}

		public T next() {
			if (cur == null) throw new NoSuchElementException();
			if (first) {
				cur = leftmost(cur);
				remainingCnt = cur == null ? 0 : cur.cnt;
				first = false;
			}
			T val = cur.label;
			if (!distinct && remainingCnt > 1) {
				remainingCnt--;
				return val;
			}
			cur = successor(cur);
			remainingCnt = cur == null ? 0 : cur.cnt;
			return val;
		}
	}

	// -------------- String --------------
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for (T t : this) sj.add(t.toString());
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(T t) {
		if (size == 0) return false;
		return count(t) > 0;
	}

	public boolean containsAll(Collection<T> c) {
		if (size == 0) return c.isEmpty();
		boolean contains = true;
		for (T t : c) {
			if (!contains(t)) {
				contains = false;
				break;
			}
		}
		return contains;
	}

	// -------------- Access by Index --------------
	public T getByIndex(long index) {
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
				return cur.label;
			}
		}
		throw new IllegalStateException("Invalid tree state");
	}

	public T getByDistinctIndex(int index) {
		if (index < 0 || distinctSize <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			int leftSize = cur.left == null ? 0 : cur.left.distinctSize;
			if (index < leftSize) {
				cur = cur.left;
			} else if (index >= leftSize + 1) {
				index -= leftSize + 1;
				cur = cur.right;
			} else {
				return cur.label;
			}
		}
		throw new IllegalStateException("Invalid tree state");
	}

	// -------------- Search & Rank --------------
	public long indexOf(T t) {
		if (size == 0) return -1;
		long index = index(t, false);
		return index >= 0 ? index : -1;
	}

	public int distinctIndexOf(T t) {
		if (size == 0) return -1;
		int index = (int) index(t, true);
		return index >= 0 ? index : -1;
	}

	public long rank(T t) {
		long index = index(t, false);
		return index < 0 ? ~index : index;
	}

	public long distinctRank(T t) {
		long index = index(t, true);
		return index < 0 ? ~index : index;
	}

	private long index(T t, boolean distinct) {
		Node cur = root;
		long index = 0;
		while (cur != null) {
			int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += distinct ? cur.leftDistinctSize() + 1 : cur.leftSize() + cur.cnt;
				if (cur.right == null) return ~index;
				cur = cur.right;
			} else if (cmp > 0) {
				if (cur.left == null) return ~index;
				cur = cur.left;
			} else {
				index += distinct ? cur.leftDistinctSize() : cur.leftSize();
				return index;
			}
		}
		throw new IllegalStateException("Invalid tree state");
	}

	// -------------- Bounds --------------
	public long upperBound(T t) {
		if (size == 0) return -1;
		Node cur = root;
		long index = 0;
		while (cur != null) {
			int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += cur.leftSize() + cur.cnt;
				if (cur.right == null) return ~index;
				cur = cur.right;
			} else if (cmp > 0) {
				if (cur.left == null) return ~index;
				cur = cur.left;
			} else {
				index += cur.leftSize() + cur.cnt - 1;
				return index;
			}
		}
		throw new IllegalStateException("Invalid tree state");
	}

	public long lowerBound(T t) {
		if (size == 0) return -1;
		Node cur = root;
		long index = 0;
		while (cur != null) {
			int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += cur.leftSize() + cur.cnt;
				if (cur.right == null) return ~index;
				cur = cur.right;
			} else if (cmp > 0) {
				if (cur.left == null) return ~index;
				cur = cur.left;
			} else {
				index += cur.leftSize();
				return index;
			}
		}
		throw new IllegalStateException("Invalid tree state");
	}

	// -------------- Counts --------------
	public long count(T t) {
		if (size == 0) return 0;
		Node cur = root;
		while (cur != null) {
			int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				cur = cur.right;
			} else if (cmp > 0) {
				cur = cur.left;
			} else {
				break;
			}
		}
		return cur == null ? 0 : cur.cnt;
	}

	// -------------- Navigation --------------
	public T higher(T key) {
		return boundary(key, false, true);
	}

	public T ceiling(T key) {
		return boundary(key, true, true);
	}

	public T lower(T key) {
		return boundary(key, false, false);
	}

	public T floor(T key) {
		return boundary(key, true, false);
	}

	private T boundary(T key, boolean inclusive, boolean higher) {
		T t = null;
		Node cur = root;
		while (cur != null) {
			int c = comparator.compare(cur.label, key);
			if (higher) {
				if (c > 0 || (inclusive && c == 0)) {
					t = cur.label;
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			} else {
				if (c < 0 || (inclusive && c == 0)) {
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
	public T first() {
		return first;
	}

	public T last() {
		return last;
	}

	public T pollFirst() {
		T temp = first;
		if (first != null) remove(first);
		return temp;
	}

	public T pollLast() {
		T temp = last;
		if (last != null) remove(last);
		return temp;
	}

	// -------------- Internal Helpers --------------
	private Node leftmost(Node cur) {
		if (cur == null) return null;
		while (cur.left != null) cur = cur.left;
		return cur;
	}

	// keep position
	private Node rightmost(Node cur) {
		if (cur == null) return null;
		while (cur.right != null) cur = cur.right;
		return cur;
	}

	private Node successor(Node cur) {
		if (cur == null) return null;
		if (cur.right != null) return leftmost(cur.right);
		while (cur.parent != null && cur.parent.right == cur) {
			cur = cur.parent;
		}
		return cur.parent;
	}

	// -------------- Nested classes --------------
	private final class Node {
		private final T label;
		private long cnt, size;
		private int height, distinctSize;
		private Node left, right, parent;

		public Node(T label, Node parent, long occurrences) {
			this.label = label;
			this.parent = parent;
			cnt = size = occurrences;
			height = distinctSize = 1;
		}

		public Node removeAt(long index, boolean distinct) {
			long lIdx = distinct ? leftDistinctSize() : leftSize();
			long rIdx = lIdx + (distinct ? 1 : cnt);
			if (rIdx <= index) {
				index -= rIdx;
				setRight(right.removeAt(index, distinct));
			} else if (index < lIdx) {
				setLeft(left.removeAt(index, distinct));
			} else {
				cnt--;
				if (distinct || cnt <= 0) return allRemove();
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node applyDelta(T t, long delta, boolean all) {
			int cmp = AVLList.this.comparator.compare(label, t);
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
				if (all || cnt <= 0) return allRemove();
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node allRemove() {
			if (left == null) return right;
			if (right == null) return left;
			Node temp;
			if (leftHeight() >= rightHeight()) {
				temp = left.findMax();
				if (temp == left) {
					setLeft(temp.left);
				} else {
					int bf = left.leftHeight() - left.rightHeight();
					setLeft(abs(bf) <= 1 ? left : left.rotate(bf));
				}
			} else {
				temp = right.findMin();
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

		private Node findMin() {
			if (left == null) return this;
			Node min = left.findMin();
			if (left == min) setLeft(left.right);
			if (left != null) {
				int bf = left.leftHeight() - left.rightHeight();
				if (abs(bf) > 1) setLeft(left.rotate(bf));
			}
			updateNode();
			return min;
		}

		private Node findMax() {
			if (right == null) return this;
			Node max = right.findMax();
			if (right == max) setRight(right.left);
			if (right != null) {
				int bf = right.leftHeight() - right.rightHeight();
				if (abs(bf) > 1) setRight(right.rotate(bf));
			}
			updateNode();
			return max;
		}

		private Node rotate(int bf) {
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

		private void setLeft(Node child) {
			left = child;
			if (child != null) child.parent = this;
		}

		private void setRight(Node child) {
			right = child;
			if (child != null) child.parent = this;
		}

		private void updateNode() {
			height = 1 + max(leftHeight(), rightHeight());
			distinctSize = leftDistinctSize() + rightDistinctSize() + 1;
			size = leftSize() + rightSize() + cnt;
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

		private int leftDistinctSize() {
			return left == null ? 0 : left.distinctSize;
		}

		private int rightDistinctSize() {
			return right == null ? 0 : right.distinctSize;
		}
	}
}
