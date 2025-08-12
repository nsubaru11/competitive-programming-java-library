import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings({"unused", "unchecked"})
public final class AVLList<T extends Comparable<T>> implements Collection<T> {
	private final Comparator<? super T> comparator;
	private int distinctSize;
	private long size;
	private Node<T> root;

	public AVLList() {
		this(Comparator.naturalOrder());
	}

	public AVLList(Comparator<? super T> comparator) {
		if (comparator == null) throw new NullPointerException();
		this.comparator = comparator;
		size = 0;
	}

	private int compare(T a, T b) {
		return comparator.compare(a, b);
	}

	// Collection<T> API
	public int size() {
		return (int) Math.min(size, Integer.MAX_VALUE);
	}

	public long longSize() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(Object o) {
		if (o == null || size == 0) return false;
		try {
			return count((T) o) > 0;
		} catch (ClassCastException e) {
			return false;
		}
	}

	public Iterator<T> iterator() {
		return new AvlIterator(root, false);
	}

	public Object[] toArray() {
		Object[] arr = new Object[(int) Math.min(size, 1_000_000_000)];
		int i = 0;
		for (T t : this) arr[i++] = t;
		return arr;
	}

	public <T1> T1[] toArray(T1[] a) {
		int sz = (int) Math.min(size, 1_000_000_000);
		if (a.length < sz) {
			a = (T1[]) Array.newInstance(a.getClass().getComponentType(), sz);
		}
		int i = 0;
		for (T t : this) a[i++] = (T1) t;
		if (a.length > sz) a[sz] = null;
		return a;
	}

	public boolean add(T t) {
		return add(t, 1);
	}

	public boolean add(T t, int occurrences) {
		if (t == null) return false;
		if (size == 0) {
			root = new Node<>(t, null, occurrences);
			distinctSize = 1;
			size = occurrences;
			return true;
		}
		root = root.applyDelta(t, occurrences, false);
		boolean isContain = size != root.size;
		size = root.size;
		distinctSize = root.distinctSize;
		root.parent = null;
		return isContain;
	}

	public boolean remove(Object o) {
		if (o == null) return false;
		try {
			return remove((T) o, 1);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public boolean remove(T t, int occurrences) {
		if (t == null || size == 0) return false;
		long oldSize = size;
		Node<T> newRoot = root.applyDelta(t, -occurrences, false);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			size = root.size;
			distinctSize = root.distinctSize;
		}
		return size != oldSize;
	}

	public boolean containsAll(Collection<?> c) {
		return c.stream().allMatch(this::contains);
	}

	public boolean addAll(Collection<? extends T> c) {
		long oldSize = size;
		c.forEach(this::add);
		return size != oldSize;
	}

	public boolean removeAll(Collection<?> c) {
		if (c == null) throw new NullPointerException();
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

	public boolean retainAll(Collection<?> c) {
		if (c == null) throw new NullPointerException();
		if (isEmpty()) return false;
		if (c.isEmpty()) {
			clear();
			return true;
		}
		long oldSize = size;
		HashSet<?> hs = new HashSet<>(c);
		T[] arr = toDistinctArray();
		for (T t : arr) {
			if (!hs.contains(t)) removeAllOccurrences(t);
		}
		return size != oldSize;
	}

	public void clear() {
		size = distinctSize = 0;
		root = null;
	}

	// Additional public API
	public Iterator<T> distinctIterator() {
		return new AvlIterator(root, true);
	}

	public T[] toDistinctArray() {
		if (distinctSize == 0) return (T[]) Array.newInstance(Object.class, 0);
		Iterator<T> it = distinctIterator();
		T first = it.next();
		T[] arr = (T[]) Array.newInstance(first.getClass(), distinctSize);
		arr[0] = first;
		int i = 1;
		while (it.hasNext()) {
			arr[i++] = it.next();
		}
		return arr;
	}

	public Stream<T> stream() {
		return toStream(false);
	}

	public Stream<T> distinctStream() {
		return toStream(true);
	}

	public long count(T t) {
		if (t == null || size == 0) return 0;
		Node<T> temp = root.findNode(t);
		return temp == null ? 0 : temp.cnt;
	}

	public boolean removeAllOccurrences(T t) {
		if (size == 0) return false;
		long oldSize = size;
		Node<T> newRoot = root.applyDelta(t, 0, true);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			size = root.size;
			distinctSize = root.distinctSize;
		}
		return oldSize != size;
	}

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
		if (key == null) return null;
		T t = null;
		Node<T> cur = root;
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

	// Private helpers
	private Stream<T> toStream(boolean distinct) {
		long size = distinct ? distinctSize : this.size;
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.SORTED;
		if (distinct) characteristics |= Spliterator.DISTINCT;
		Iterator<T> it = distinct ? distinctIterator() : iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	private Node<T> leftmost(Node<T> cur) {
		if (cur == null) return null;
		while (cur.left != null) cur = cur.left;
		return cur;
	}

	private Node<T> successor(Node<T> cur) {
		if (cur == null) return null;
		if (cur.right != null) {
			return leftmost(cur.right);
		}
		while (cur.parent != null && cur.parent.right == cur) {
			cur = cur.parent;
		}
		return cur;
	}

	// Nested classes
	private static final class Node<T extends Comparable<T>> {
		private final T label;
		private int distinctSize, height;
		private long cnt, size;
		private Node<T> left, right, parent;

		public Node(T label, Node<T> parent, long occurrences) {
			this.parent = parent;
			this.label = label;
			distinctSize = height = 1;
			cnt = size = occurrences;
		}

		public Node<T> applyDelta(T t, long delta, boolean all) {
			int cmp = label.compareTo(t);
			if (cmp < 0) {
				if (right == null) {
					if (delta > 0) {
						setRight(new Node<>(t, this, delta));
					} else {
						return this;
					}
				} else {
					setRight(right.applyDelta(t, delta, all));
				}
			} else if (cmp > 0) {
				if (left == null) {
					if (delta > 0) {
						setLeft(new Node<>(t, this, delta));
					} else {
						return this;
					}
				} else {
					setLeft(left.applyDelta(t, delta, all));
				}
			} else {
				if (all) {
					return allRemove();
				} else {
					cnt += delta;
					if (cnt <= 0) return allRemove();
				}
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node<T> allRemove() {
			if (left == null) return right;
			if (right == null) return left;
			Node<T> temp;
			if (leftHeight() >= rightHeight()) {
				temp = left.findMax();
				int bf = left.leftHeight() - left.rightHeight();
				left = abs(bf) <= 1 ? left : left.rotate(bf);
			} else {
				temp = right.findMin();
				int bf = right.leftHeight() - right.rightHeight();
				right = abs(bf) <= 1 ? right : right.rotate(bf);
			}
			temp.parent = parent;
			temp.setLeft(left);
			temp.setRight(right);
			temp.updateNode();
			int bf = temp.leftHeight() - temp.rightHeight();
			return abs(bf) <= 1 ? temp : temp.rotate(bf);
		}

		private Node<T> findMin() {
			if (left == null) return this;
			Node<T> min = left.findMin();
			if (left == min) setLeft(left.right);
			if (left != null) {
				int bf = left.leftHeight() - left.rightHeight();
				if (abs(bf) > 1) setLeft(left.rotate(bf));
			}
			updateNode();
			return min;
		}

		private Node<T> findMax() {
			if (right == null) return this;
			Node<T> max = right.findMax();
			if (right == max) setRight(right.left);
			if (right != null) {
				int bf = right.leftHeight() - right.rightHeight();
				if (abs(bf) > 1) setRight(right.rotate(bf));
			}
			updateNode();
			return max;
		}

		private Node<T> findNode(T t) {
			int cmp = label.compareTo(t);
			if (cmp < 0) {
				return right == null ? null : right.findNode(t);
			} else if (cmp > 0) {
				return left == null ? null : left.findNode(t);
			} else {
				return this;
			}
		}

		private Node<T> rotate(int bf) {
			Node<T> newRoot;
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
			newRoot.parent = this.parent;
			if (newRoot.left != null) {
				newRoot.left.parent = newRoot;
				if (newRoot.left.right != null) {
					newRoot.left.right.parent = newRoot.left;
				}
			}
			if (newRoot.right != null) {
				newRoot.right.parent = newRoot;
				if (newRoot.right.left != null) {
					newRoot.right.left.parent = newRoot.right;
				}
			}
			return newRoot;
		}

		private Node<T> rotateLR() {
			Node<T> newRoot = this.left.right;
			Node<T> tempLeft = newRoot.left;
			Node<T> tempRight = newRoot.right;
			newRoot.right = this;
			newRoot.left = this.left;
			newRoot.right.left = tempRight;
			newRoot.left.right = tempLeft;
			newRoot.left.updateNode();
			return newRoot;
		}

		private Node<T> rotateLL() {
			Node<T> newRoot = this.left;
			this.left = newRoot.right;
			newRoot.right = this;
			return newRoot;
		}

		private Node<T> rotateRR() {
			Node<T> newRoot = this.right;
			this.right = newRoot.left;
			newRoot.left = this;
			return newRoot;
		}

		private Node<T> rotateRL() {
			Node<T> newRoot = this.right.left;
			Node<T> tempLeft = newRoot.left;
			Node<T> tempRight = newRoot.right;
			newRoot.left = this;
			newRoot.right = this.right;
			newRoot.left.right = tempLeft;
			newRoot.right.left = tempRight;
			newRoot.right.updateNode();
			return newRoot;
		}

		private void setLeft(Node<T> child) {
			left = child;
			if (child != null) child.parent = this;
		}

		private void setRight(Node<T> child) {
			right = child;
			if (child != null) child.parent = this;
		}

		private void updateNode() {
			distinctSize = (left == null ? 0 : left.distinctSize) + (right == null ? 0 : right.distinctSize) + 1;
			size = (left == null ? 0 : left.size) + (right == null ? 0 : right.size) + cnt;
			height = 1 + max(leftHeight(), rightHeight());
		}

		private int leftHeight() {
			return left == null ? 0 : left.height;
		}

		private int rightHeight() {
			return right == null ? 0 : right.height;
		}
	}

	private final class AvlIterator implements Iterator<T> {
		private final boolean distinct;
		private Node<T> cur;
		private long remainingCnt;
		private boolean first = true;

		AvlIterator(Node<T> root, boolean distinct) {
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
				if (cur == null) throw new NoSuchElementException();
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
}