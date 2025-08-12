import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings({"unused", "unchecked"})
public final class AVLSet<T extends Comparable<T>> implements Collection<T> {
	private int size;
	private Node<T> root;

	public AVLSet() {
		size = 0;
		root = null;
	}

	// Collection<T> API
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(Object o) {
		if (o == null) return false;
		try {
			T t = (T) o;
			return count(t) > 0;
		} catch (ClassCastException e) {
			return false;
		}
	}

	public Iterator<T> iterator() {
		return new AvlIterator(root);
	}

	public Object[] toArray() {
		Object[] arr = new Object[size];
		int i = 0;
		for (T t : this) {
			arr[i++] = t;
		}
		return arr;
	}

	public <T1> T1[] toArray(T1[] a) {
		int sz = size;
		if (a.length < sz) {
			a = (T1[]) Array.newInstance(a.getClass().getComponentType(), sz);
		}
		int i = 0;
		for (T t : this) {
			a[i++] = (T1) t;
		}
		if (a.length > sz) a[sz] = null;
		return a;
	}

	public boolean add(T value) {
		if (size == 0) {
			root = new Node<>(value, null);
			size = 1;
			return true;
		}
		root = root.add(value);
		boolean isContain = size != root.size;
		size = root.size;
		root.parent = null;
		return isContain;
	}

	public boolean remove(Object value) {
		if (value == null) return false;
		final T t;
		try {
			t = (T) value;
		} catch (ClassCastException e) {
			return false;
		}
		if (size == 0) return false;
		int oldSize = size;
		Node<T> newRoot = root.remove(t);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			size = root.size;
		}
		return size != oldSize;
	}

	public boolean containsAll(Collection<?> c) {
		return c.stream().allMatch(this::contains);
	}

	public boolean addAll(Collection<? extends T> c) {
		int oldSize = size;
		c.forEach(this::add);
		return size != oldSize;
	}

	public boolean removeAll(Collection<?> c) {
		if (c == null) throw new NullPointerException();
		if (isEmpty()) return false;
		int oldSize = size;
		HashSet<?> targets = new HashSet<>(c);
		for (T v : this) {
			if (targets.contains(v)) {
				remove(v);
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
		int oldSize = size;
		HashSet<?> hs = new HashSet<>(c);
		for (T t : this) {
			if (!hs.contains(t)) remove(t);
		}
		return size != oldSize;
	}

	public void clear() {
		size = 0;
		root = null;
	}

	// Additional public API
	public int count(T t) {
		if (t == null || size == 0) return 0;
		Node<T> temp = root.findNode(t);
		return temp == null ? 0 : temp.cnt;
	}

	public Stream<T> stream() {
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.SORTED;
		Iterator<T> it = iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// Private helpers
	private Node<T> leftmost(Node<T> n) {
		if (n == null) return null;
		while (n.left != null) n = n.left;
		return n;
	}

	private Node<T> successor(Node<T> n) {
		if (n == null) return null;
		if (n.right != null) {
			return leftmost(n.right);
		}
		Node<T> p = n.parent, ch = n;
		while (p != null && p.right == ch) {
			ch = p;
			p = p.parent;
		}
		return p;
	}

	// Nested classes
	private static final class Node<T extends Comparable<T>> {
		private final T label;
		private int cnt, size, height;
		private Node<T> left, right, parent;

		public Node(T label, Node<T> parent) {
			this.parent = parent;
			this.label = label;
			cnt = size = height = 1;
		}

		private Node<T> add(T t) {
			int cmp = label.compareTo(t);
			if (cmp < 0) {
				setRight(right == null ? new Node<>(t, this) : right.add(t));
			} else if (cmp > 0) {
				setLeft(left == null ? new Node<>(t, this) : left.add(t));
			} else {
				cnt++;
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		public Node<T> remove(T t) {
			int cmp = label.compareTo(t);
			if (cmp < 0) {
				if (right == null) return this;
				setRight(right.remove(t));
			} else if (cmp > 0) {
				if (left == null) return this;
				setLeft(left.remove(t));
			} else {
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
				temp.left = left;
				temp.right = right;
				temp.updateNode();
				int bf = temp.leftHeight() - temp.rightHeight();
				return abs(bf) <= 1 ? temp : temp.rotate(bf);
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
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
			size = (left == null ? 0 : left.size) + (right == null ? 0 : right.size) + 1;
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
		private Node<T> cur;
		private boolean first = true;

		AvlIterator(Node<T> root) {
			this.cur = root;
		}

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
	}
}