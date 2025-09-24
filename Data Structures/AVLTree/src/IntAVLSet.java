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
public final class IntAVLSet implements Iterable<Integer> {
	// -------------- Fields --------------
	private Node root;
	private int first, last;
	private int size;

	// -------------- Constructors --------------
	public IntAVLSet() {
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
		first = last = 0;
		size = 0;
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
		Node cur = root;
		while (cur != null) {
			if (cur.label == t) break;
			cur = cur.label < t ? cur.right : cur.left;
		}
		return cur != null;
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
		if (size == 0) {
			first = last = t;
			root = new Node(t, null);
			size = 1;
			return true;
		}
		if (t < first) first = t;
		if (t > last) last = t;
		int oldSize = size;
		root = root.add(t);
		update();
		return size != oldSize;
	}

	public boolean addAll(final Collection<Integer> c) {
		int oldSize = size;
		for (int a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(final int t) {
		if (size == 0) return false;
		int oldSize = size;
		root = root.remove(t);
		update();
		boolean removed = size != oldSize;
		if (size > 0 && removed) {
			if (t == first) first = leftmost(root).label;
			if (t == last) last = rightmost(root).label;
		}
		return removed;
	}

	public boolean removeAll(final Collection<Integer> c) {
		if (isEmpty()) return false;
		int oldSize = size;
		Collection<Integer> hs = c instanceof Set ? c : new HashSet<>(c);
		for (int v : hs) remove(v);
		return size != oldSize;
	}

	public boolean removeAt(final int index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		int oldSize = size;
		root = root.removeAt(index);
		update();
		if (size > 0) {
			if (index == 0) first = leftmost(root).label;
			if (index == oldSize - 1) last = rightmost(root).label;
		}
		return size != oldSize;
	}

	// -------------- Arrays --------------
	public int[] toArray() {
		if (size == 0) return new int[0];
		int[] arr = new int[size];
		PrimitiveIterator.OfInt it = iterator();
		for (int i = 0; it.hasNext(); i++) arr[i] = it.nextInt();
		return arr;
	}

	// -------------- Streams --------------
	public Stream<Integer> stream() {
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		return StreamSupport.stream(Spliterators.spliterator(iterator(), size, characteristics), false);
	}

	// -------------- Iteration --------------
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private Node cur;

			{
				cur = leftmost(root);
			}

			public boolean hasNext() {
				return cur != null;
			}

			public int nextInt() {
				if (cur == null) throw new NoSuchElementException();
				int val = cur.label;
				cur = successor(cur);
				return val;
			}
		};
	}

	// -------------- Access by Index --------------
	public int getByIndex(int index) {
		if (index < 0 || size <= index) throw new IndexOutOfBoundsException();
		Node cur = root;
		while (cur != null) {
			int leftSize = cur.left == null ? 0 : cur.left.size;
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
	public int indexOf(final int t) {
		if (size == 0) return -1;
		int index = rank(t);
		return index >= 0 ? index : -1;
	}

	public int rank(final int t) {
		Node cur = root;
		int index = 0;
		while (cur != null) {
			if (cur.label < t) {
				index += cur.leftSize() + 1;
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
		removeAt(0);
		return temp;
	}

	public int pollLast() {
		if (size == 0) throw new NoSuchElementException();
		int temp = last;
		removeAt(size - 1);
		return temp;
	}

	// -------------- Internal Helpers --------------
	private void update() {
		if (root == null) {
			clear();
			return;
		}
		size = root.size;
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
	private static final class Node {
		private final int label;
		private int height, size;
		private Node left, right, parent;

		private Node(final int label, final Node parent) {
			this.label = label;
			this.height = this.size = 1;
			this.parent = parent;
		}

		private Node removeAt(int index) {
			int lIdx = leftSize();
			if (lIdx < index) {
				index -= lIdx + 1;
				setRight(right.removeAt(index));
			} else if (index < lIdx) {
				setLeft(left.removeAt(index));
			} else {
				return removeInternal();
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node add(final int t) {
			if (label < t) {
				setRight(right == null ? new Node(t, this) : right.add(t));
			} else if (label > t) {
				setLeft(left == null ? new Node(t, this) : left.add(t));
			} else {
				return this;
			}
			updateNode();
			int bf = leftHeight() - rightHeight();
			return abs(bf) <= 1 ? this : rotate(bf);
		}

		private Node remove(final int t) {
			if (label < t) {
				if (right == null) {
					return this;
				} else {
					setRight(right.remove(t));
				}
			} else if (label > t) {
				if (left == null) {
					return this;
				} else {
					setLeft(left.remove(t));
				}
			} else {
				return removeInternal();
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