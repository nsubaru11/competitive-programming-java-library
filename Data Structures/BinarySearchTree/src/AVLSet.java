import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings({"unused", "unchecked"})
public final class AVLSet<T extends Comparable<T>> implements Iterable<T> {
	private final Comparator<? super T> comparator;
	private int size;
	private Node root;
	private T first, last;

	// -------------- Constructors --------------
	public AVLSet() {
		this(Comparator.naturalOrder());
	}

	public AVLSet(Comparator<? super T> comparator) {
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

	// -------------- Add --------------
	public boolean add(T t) {
		if (size == 0) {
			first = last = t;
			root = new Node(t, null);
			size = 1;
			return true;
		}
		if (comparator.compare(t, first) < 0) first = t;
		if (comparator.compare(t, last) > 0) last = t;
		root = root.add(t);
		boolean isNotContain = size != root.size;
		size = root.size;
		root.parent = null;
		return isNotContain;
	}

	public boolean addAll(Collection<T> c) {
		int oldSize = size;
		for (T a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(T value) {
		if (value == null) return false;
		final T t;
		try {
			t = (T) value;
		} catch (ClassCastException e) {
			return false;
		}
		if (size == 0) return false;
		int oldSize = size;
		Node newRoot = root.allRemove(t);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			if (size != root.size) {
				if (comparator.compare(t, first) == 0) first = leftmost(root).label;
				if (comparator.compare(t, last) == 0) last = rightmost(root).label;
			}
			size = root.size;
		}
		return size != oldSize;
	}

	public boolean removeAll(Collection<T> c) {
		if (c == null) throw new NullPointerException();
		if (isEmpty()) return false;
		int oldSize = size;
		HashSet<T> targets = new HashSet<>(c);
		for (T v : this) {
			if (targets.contains(v)) remove(v);
		}
		return size != oldSize;
	}

	public boolean removeAt(int index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		int oldSize = size;
		Node newRoot = root.removeAt(index);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			if (size != root.size) {
				if (index == 0) first = leftmost(root).label;
				if (index == size - 1) last = rightmost(root).label;
			}
			size = root.size;
		}
		return size != oldSize;
	}

	// -------------- Arrays --------------
	public T[] toArray() {
		if (size == 0) return (T[]) new Object[0];
		T[] arr = (T[]) Array.newInstance(first.getClass(), (int) size);
		int i = 0;
		for (T t : this) arr[i++] = t;
		return arr;
	}

	// -------------- Streams --------------
	public Stream<T> stream() {
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.SORTED;
		Iterator<T> it = iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private Node cur = root;
			private boolean first = true;

			public boolean hasNext() {
				return cur != null;
			}

			public T next() {
				if (cur == null) throw new NoSuchElementException();
				if (first) {
					cur = leftmost(cur);
					first = false;
					if (cur == null) throw new NoSuchElementException();
				}
				T val = cur.label;
				cur = successor(cur);
				return val;
			}
		};
	}

	// -------------- String --------------
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for (T t : this) sj.add(String.valueOf(t));
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(T t) {
		if (size == 0) return false;
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
		return cur != null;
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
	public T getByIndex(int index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			int leftSize = cur.left == null ? 0 : cur.left.size;
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
	public int indexOf(T t) {
		int index = rank(t);
		return index < 0 ? -1 : index;
	}

	public int rank(T t) {
		Node cur = root;
		int index = 0;
		while (cur != null) {
			int cmp = comparator.compare(cur.label, t);
			if (cmp < 0) {
				index += cur.leftSize() + 1;
				if (cur.right == null) return ~index;
				cur = cur.right;
			} else if (cmp > 0) {
				if (cur.left == null) return ~index;
				cur = cur.left;
			} else {
				return index + cur.leftSize();
			}
		}
		throw new IllegalStateException("Invalid tree state");
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
			int cmp = comparator.compare(cur.label, key);
			if (higher) {
				if (cmp > 0 || (inclusive && cmp == 0)) {
					t = cur.label;
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			} else {
				if (cmp < 0 || (inclusive && cmp == 0)) {
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
	private Node leftmost(Node n) {
		if (n == null) return null;
		while (n.left != null) n = n.left;
		return n;
	}

	private Node rightmost(Node n) {
		if (n == null) return null;
		while (n.right != null) n = n.right;
		return n;
	}

	private Node successor(Node n) {
		if (n == null) return null;
		if (n.right != null) {
			return leftmost(n.right);
		}
		Node p = n.parent, ch = n;
		while (p != null && p.right == ch) {
			ch = p;
			p = p.parent;
		}
		return p;
	}

	// -------------- Nested classes --------------
	private final class Node {
		private final T label;
		private int size, height;
		private Node left, right, parent;

		public Node(T label, Node parent) {
			this.parent = parent;
			this.label = label;
			size = height = 1;
		}

		public Node removeAt(int index) {
			int lIdx = leftSize();
			int rIdx = lIdx + 1;
			if (rIdx <= index) {
				index -= rIdx;
				setRight(right.removeAt(index));
			} else if (index < lIdx) {
				setLeft(left.removeAt(index));
			} else {
				return allRemove();
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node add(T t) {
			int cmp = AVLSet.this.comparator.compare(label, t);
			if (cmp < 0) {
				setRight(right == null ? new Node(t, this) : right.add(t));
			} else if (cmp > 0) {
				setLeft(left == null ? new Node(t, this) : left.add(t));
			} else {
				return this;
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node allRemove(T t) {
			int cmp = AVLSet.this.comparator.compare(label, t);
			if (cmp < 0) {
				if (right == null) {
					return this;
				} else {
					setRight(right.allRemove(t));
				}
			} else if (cmp > 0) {
				if (left == null) {
					return this;
				} else {
					setLeft(left.allRemove(t));
				}
			} else {
				return allRemove();
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