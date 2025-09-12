import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings("unused")
public final class LongAVLSet implements Iterable<Long> {
	private int size;
	private Node root;
	private long first, last;

	// -------------- Constructors --------------
	public LongAVLSet() {
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

	// -------------- Add --------------
	public boolean add(long t) {
		if (size == 0) {
			first = last = t;
			root = new Node(t, null);
			size = 1;
			return true;
		}
		if (first > t) first = t;
		if (last < t) last = t;
		root = root.add(t);
		boolean isNotContain = size != root.size;
		size = root.size;
		root.parent = null;
		return isNotContain;
	}

	public boolean addAll(Collection<Long> c) {
		int oldSize = size;
		for (long a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(long t) {
		if (size == 0) return false;
		int oldSize = size;
		Node newRoot = root.remove(t);
		if (newRoot == null) {
			clear();
		} else {
			root = newRoot;
			root.parent = null;
			if (size != root.size) {
				if (first == t) first = leftmost(root).label;
				if (last == t) last = rightmost(root).label;
			}
			size = root.size;
		}
		return size != oldSize;
	}

	public boolean removeAll(Collection<Long> c) {
		if (isEmpty()) return false;
		int oldSize = size;
		HashSet<Long> hs = new HashSet<>(c);
		for (long v : hs) remove(v);
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
	public long[] toArray() {
		if (size == 0) return new long[0];
		long[] arr = new long[size];
		int i = 0;
		for (long t : this) arr[i++] = t;
		return arr;
	}

	// -------------- Streams --------------
	public Stream<Long> stream() {
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED;
		Iterator<Long> it = iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public Iterator<Long> iterator() {
		return new Iterator<>() {
			private Node cur = root;
			private boolean first = true;

			public boolean hasNext() {
				return cur != null;
			}

			public Long next() {
				if (cur == null) throw new NoSuchElementException();
				if (first) {
					cur = leftmost(cur);
					first = false;
					if (cur == null) throw new NoSuchElementException();
				}
				long val = cur.label;
				cur = successor(cur);
				return val;
			}
		};
	}

	// -------------- String --------------
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for (long t : this) sj.add(String.valueOf(t));
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(long t) {
		if (size == 0) return false;
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
		return cur != null;
	}

	public boolean containsAll(Collection<Long> c) {
		if (size == 0) return c.isEmpty();
		boolean contains = true;
		for (long t : c) {
			if (!contains(t)) {
				contains = false;
				break;
			}
		}
		return contains;
	}

	// -------------- Access by Index --------------
	public long getByIndex(int index) {
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
	public int indexOf(long t) {
		int index = rank(t);
		return index < 0 ? -1 : index;
	}

	public int rank(long t) {
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
	public Long higher(long key) {
		return boundary(key, false, true);
	}

	public Long ceiling(long key) {
		return boundary(key, true, true);
	}

	public Long lower(long key) {
		return boundary(key, false, false);
	}

	public Long floor(long key) {
		return boundary(key, true, false);
	}

	private Long boundary(long key, boolean inclusive, boolean higher) {
		if (size == 0) return null;
		if (first == key && inclusive) return first;
		if (first > key) return higher ? first : null;
		if (last == key && inclusive) return last;
		if (last < key) return higher ? null : last;
		Long t = null;
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
	public long first() {
		return first;
	}

	public long last() {
		return last;
	}

	public long pollFirst() {
		if (size == 0) throw new NoSuchElementException();
		long temp = first;
		remove(first);
		return temp;
	}

	public long pollLast() {
		if (size == 0) throw new NoSuchElementException();
		long temp = last;
		remove(last);
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
		if (n.right != null) return leftmost(n.right);
		Node p = n.parent, ch = n;
		while (p != null && p.right == ch) {
			ch = p;
			p = p.parent;
		}
		return p;
	}

	// -------------- Nested classes --------------
	private static final class Node {
		private final long label;
		private int size, height;
		private Node left, right, parent;

		public Node(long label, Node parent) {
			this.parent = parent;
			this.label = label;
			size = height = 1;
		}

		public Node removeAt(int index) {
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

		private Node add(long t) {
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

		private Node remove(long t) {
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