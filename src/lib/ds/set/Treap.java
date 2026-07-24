package lib.ds.set;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

@SuppressWarnings({"unused", "unchecked"})
public final class Treap<T extends Comparable<T>> implements Iterable<T> {
	// -------------- Fields --------------
	private final Comparator<? super T> comparator;
	private Node root;
	private T first, last;
	private int size;

	// -------------- Constructors --------------
	public Treap() {
		this(Comparator.naturalOrder());
	}

	public Treap(final Comparator<? super T> comparator) {
		this.comparator = comparator;
		clear();
	}

	// -------------- Size & State --------------
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		root = null;
		first = last = null;
		size = 0;
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
		Node cur = root;
		while (cur != null) {
			final int cmp = comparator.compare(cur.label, t);
			if (cmp == 0) break;
			cur = cmp < 0 ? cur.right : cur.left;
		}
		return cur != null;
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
		if (size == 0) {
			first = last = t;
			root = new Node(t, null);
			size = 1;
			return true;
		}
		if (comparator.compare(t, first) < 0) first = t;
		if (comparator.compare(t, last) > 0) last = t;
		final int oldSize = size;
		root = root.add(t);
		update();
		return size != oldSize;
	}

	public boolean addAll(final Collection<T> c) {
		final int oldSize = size;
		for (final T a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(final T t) {
		if (size == 0) return false;
		final int oldSize = size;
		root = root.remove(t);
		update();
		final boolean removed = size != oldSize;
		if (size > 0 && removed) {
			if (comparator.compare(t, first) == 0) first = leftmost(root).label;
			if (comparator.compare(t, last) == 0) last = rightmost(root).label;
		}
		return removed;
	}

	public boolean removeAll(final Collection<T> c) {
		if (isEmpty()) return false;
		final int oldSize = size;
		final Collection<T> hs = c instanceof Set ? c : new HashSet<>(c);
		for (final T v : hs) remove(v);
		return size != oldSize;
	}

	public boolean removeAt(final int index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		final int oldSize = size;
		root = root.removeAt(index);
		update();
		if (size > 0) {
			if (index == 0) first = leftmost(root).label;
			if (index == oldSize - 1) last = rightmost(root).label;
		}
		return size != oldSize;
	}

	// -------------- Arrays --------------
	public T[] toArray() {
		if (size == 0) return (T[]) new Object[0];
		final T[] arr = (T[]) Array.newInstance(first.getClass(), size);
		int i = 0;
		for (final T t : this) arr[i++] = t;
		return arr;
	}

	// -------------- Streams --------------
	public Stream<T> stream() {
		final int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT;
		return StreamSupport.stream(Spliterators.spliterator(iterator(), size, characteristics), false);
	}

	// -------------- Iteration --------------
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private Node cur;

			{
				cur = leftmost(root);
			}

			public boolean hasNext() {
				return cur != null;
			}

			public T next() {
				if (cur == null) throw new NoSuchElementException();
				final T label = cur.label;
				cur = successor(cur);
				return label;
			}
		};
	}

	// -------------- Access by Index --------------
	public T getByIndex(int index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			final int leftSize = cur.left == null ? 0 : cur.left.size;
			if (index < leftSize) {
				cur = cur.left;
			} else if (index > leftSize) {
				index -= leftSize + 1;
				cur = cur.right;
			} else {
				break;
			}
		}
		return cur.label;
	}

	// -------------- Search & Rank --------------
	public int indexOf(final T t) {
		if (size == 0) return -1;
		final int index = rank(t);
		return index >= 0 ? index : -1;
	}

	public int rank(final T t) {
		Node cur = root;
		int index = 0;
		while (cur != null) {
			final int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += cur.leftSize() + 1;
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
		removeAt(0);
		return temp;
	}

	public T pollLast() {
		if (size == 0) return null;
		final T temp = last;
		removeAt(size - 1);
		return temp;
	}

	// -------------- Internal Helpers --------------
	private void update() {
		if (root == null) {
			clear();
			return;
		}
		root.parent = null;
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
	private final class Node {
		private static int seed = (int) System.nanoTime();
		private final T label;
		private final int priority;
		private int size;
		private Node left, right, parent;

		private Node(final T label, final Node parent) {
			this.label = label;
			this.size = 1;
			this.priority = nextRandom();
			this.parent = parent;
		}

		private static int nextRandom() {
			seed ^= seed << 13;
			seed ^= seed >>> 17;
			seed ^= seed << 5;
			return seed & Integer.MAX_VALUE;
		}

		private Node removeAt(int index) {
			final int lIdx = leftSize();
			if (lIdx < index) {
				index -= lIdx + 1;
				setRight(right.removeAt(index));
			} else if (index < lIdx) {
				setLeft(left.removeAt(index));
			} else {
				return removeDown();
			}
			size = leftSize() + rightSize() + 1;
			return this;
		}

		private Node add(final T t) {
			final int cmp = Treap.this.comparator.compare(label, t);
			if (cmp < 0) {
				setRight(right == null ? new Node(t, this) : right.add(t));
				if (priority < right.priority) return rotateL();
				size = leftSize() + rightSize() + 1;
			} else if (cmp > 0) {
				setLeft(left == null ? new Node(t, this) : left.add(t));
				if (priority < left.priority) return rotateR();
				size = leftSize() + rightSize() + 1;
			}
			return this;
		}

		private Node remove(final T t) {
			final int cmp = Treap.this.comparator.compare(label, t);
			if (cmp < 0) {
				if (right != null) setRight(right.remove(t));
			} else if (cmp > 0) {
				if (left != null) setLeft(left.remove(t));
			} else {
				return removeDown();
			}
			size = leftSize() + rightSize() + 1;
			return this;
		}

		private Node removeDown() {
			if (left == null) return right;
			if (right == null) return left;
			final Node n;
			if (left.priority > right.priority) {
				n = rotateR();
				n.setRight(n.right.removeDown());
			} else {
				n = rotateL();
				n.setLeft(n.left.removeDown());
			}
			n.size = n.leftSize() + n.rightSize() + 1;
			return n;
		}

		private Node rotateR() {
			final Node newRoot = this.left;
			setLeft(newRoot.right);
			newRoot.setRight(this);
			size = leftSize() + rightSize() + 1;
			newRoot.size = newRoot.leftSize() + newRoot.rightSize() + 1;
			return newRoot;
		}

		private Node rotateL() {
			final Node newRoot = this.right;
			setRight(newRoot.left);
			newRoot.setLeft(this);
			size = leftSize() + rightSize() + 1;
			newRoot.size = newRoot.leftSize() + newRoot.rightSize() + 1;
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

		private int leftSize() {
			return left == null ? 0 : left.size;
		}

		private int rightSize() {
			return right == null ? 0 : right.size;
		}
	}
}
