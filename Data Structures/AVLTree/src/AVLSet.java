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
public final class AVLSet<T extends Comparable<T>> implements Iterable<T> {
	// -------------- Fields --------------
	private final Comparator<? super T> comparator;
	private Node root;
	private T first, last;
	private int size;

	// -------------- Constructors --------------
	public AVLSet() {
		this(Comparator.naturalOrder());
	}

	public AVLSet(final Comparator<? super T> comparator) {
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
		private final T label;
		private int height, size;
		private Node left, right, parent;

		private Node(final T label, final Node parent) {
			this.label = label;
			this.height = this.size = 1;
			this.parent = parent;
		}

		private Node removeAt(int index) {
			final int lIdx = leftSize();
			if (lIdx < index) {
				index -= lIdx + 1;
				setRight(right.removeAt(index));
			} else if (index < lIdx) {
				setLeft(left.removeAt(index));
			} else {
				return removeInternal();
			}
			updateNode();
			final int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node add(final T t) {
			final int cmp = AVLSet.this.comparator.compare(label, t);
			if (cmp < 0) {
				setRight(right == null ? new Node(t, this) : right.add(t));
			} else if (cmp > 0) {
				setLeft(left == null ? new Node(t, this) : left.add(t));
			} else {
				return this;
			}
			updateNode();
			final int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node remove(final T t) {
			final int cmp = AVLSet.this.comparator.compare(label, t);
			if (cmp < 0) {
				if (right == null) {
					return this;
				} else {
					setRight(right.remove(t));
				}
			} else if (cmp > 0) {
				if (left == null) {
					return this;
				} else {
					setLeft(left.remove(t));
				}
			} else {
				return removeInternal();
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
			size = leftSize() + rightSize() + 1;
			height = 1 + max(leftHeight(), rightHeight());
		}

		private int leftHeight() {
			return left == null ? 0 : left.height;
		}

		private int rightHeight() {
			return right == null ? 0 : right.height;
		}

		private int leftSize() {
			return left == null ? 0 : left.size;
		}

		private int rightSize() {
			return right == null ? 0 : right.size;
		}
	}
}
