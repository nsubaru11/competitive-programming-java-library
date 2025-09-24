import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings({"unused", "unchecked"})
public class BinarySearchTree<T extends Comparable<T>> implements Collection<T> {
	private final Comparator<? super T> comparator;
	private int size;
	private Node root;

	public BinarySearchTree() {
		this(Comparator.naturalOrder());
	}

	public BinarySearchTree(Comparator<? super T> comparator) {
		this.comparator = comparator;
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
			return root != null && root.findNode((T) o) != null;
		} catch (ClassCastException e) {
			return false;
		}
	}

	public Iterator<T> iterator() {
		return new BstIterator(root);
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

	public boolean add(T t) {
		if (t == null) return false;
		if (size == 0) {
			root = new Node(t, null);
			size = 1;
			return true;
		}
		root.add(t);
		boolean isContain = size != root.size;
		size = root.size;
		return isContain;
	}

	public boolean remove(Object o) {
		if (o == null || size == 0) return false;
		final T t;
		try {
			t = (T) o;
		} catch (ClassCastException e) {
			return false;
		}
		int oldSize = size;
		Node newRoot = root.remove(t);
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
	public Stream<T> stream() {
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.SORTED;
		Iterator<T> it = iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
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

	// Private helpers
	private Node leftmost(Node cur) {
		if (cur == null) return null;
		while (cur.left != null) cur = cur.left;
		return cur;
	}

	private Node successor(Node cur) {
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
	private final class Node {
		private final T label;
		private int size;
		private Node left, right, parent;

		public Node(T label, Node parent) {
			this.parent = parent;
			this.label = label;
			size = 1;
		}

		private Node add(T t) {
			int cmp = comparator.compare(t, label);
			if (cmp < 0) {
				setLeft(left == null ? new Node(t, this) : left.add(t));
			} else if (cmp > 0) {
				setRight(right == null ? new Node(t, this) : right.add(t));
			} else {
				return this;
			}
			updateNode();
			return this;
		}

		public Node remove(T t) {
			int cmp = comparator.compare(t, label);
			if (cmp < 0) {
				if (left == null) return this;
				setLeft(left.remove(t));
			} else if (cmp > 0) {
				if (right == null) return this;
				setRight(right.remove(t));
			} else {
				if (left == null) return right;
				if (right == null) return left;
				Node temp = left.extractMax();
				temp.left = left;
				temp.right = right;
				temp.updateNode();
				return temp;
			}
			updateNode();
			return this;
		}

		private Node extractMin() {
			if (left == null) return this;
			Node min = left.extractMin();
			if (left == min) setLeft(left.right);
			updateNode();
			return min;
		}

		private Node extractMax() {
			if (right == null) return this;
			Node max = right.extractMax();
			if (right == max) setRight(right.left);
			updateNode();
			return max;
		}

		private Node findNode(T t) {
			int cmp = comparator.compare(t, label);
			if (cmp < 0) {
				return left == null ? null : left.findNode(t);
			} else if (cmp > 0) {
				return right == null ? null : right.findNode(t);
			} else {
				return this;
			}
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
			size = (left == null ? 0 : left.size) + (right == null ? 0 : right.size) + 1;
		}
	}

	private final class BstIterator implements Iterator<T> {
		private Node cur;

		BstIterator(Node root) {
			cur = leftmost(root);
		}

		public boolean hasNext() {
			return cur != null;
		}

		public T next() {
			if (cur == null) throw new NoSuchElementException();
			T val = cur.label;
			cur = successor(cur);
			return val;
		}
	}
}
