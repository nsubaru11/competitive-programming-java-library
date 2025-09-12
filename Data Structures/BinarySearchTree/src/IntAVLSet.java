import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings("unused")
public final class IntAVLSet implements Iterable<Integer> {
	private int size;
	private Node root;
	private int first, last;

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

	// -------------- Add --------------
	public boolean add(int t) {
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

	public boolean addAll(Collection<Integer> c) {
		int oldSize = size;
		for (int a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(int t) {
		if (size == 0) return false;
		int oldSize = size;
		Node newRoot = root.allRemove(t);
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

	public boolean removeAll(Collection<Integer> c) {
		if (isEmpty()) return false;
		int oldSize = size;
		HashSet<Integer> hs = new HashSet<>(c);
		for (int v : hs) remove(v);
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
	public int[] toArray() {
		if (size == 0) return new int[0];
		int[] arr = new int[size];
		int i = 0;
		for (int t : this) arr[i++] = t;
		return arr;
	}

	// -------------- Streams --------------
	public Stream<Integer> stream() {
		int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED;
		Iterator<Integer> it = iterator();
		return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
	}

	// -------------- Iteration --------------
	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private Node cur = root;
			private boolean first = true;

			public boolean hasNext() {
				return cur != null;
			}

			public Integer next() {
				if (cur == null) throw new NoSuchElementException();
				if (first) {
					cur = leftmost(cur);
					first = false;
					if (cur == null) throw new NoSuchElementException();
				}
				int val = cur.label;
				cur = successor(cur);
				return val;
			}
		};
	}

	// -------------- String --------------
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for (int t : this) sj.add(String.valueOf(t));
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(int t) {
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

	public boolean containsAll(Collection<Integer> c) {
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

	// -------------- Access by Index --------------
	public int getByIndex(int index) {
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
				break;
			}
		}
		return cur.label;
	}

	// -------------- Search & Rank --------------
	public int indexOf(int t) {
		int index = rank(t);
		return index < 0 ? -1 : index;
	}

	public int rank(int t) {
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
	public Integer higher(int key) {
		return boundary(key, false, true);
	}

	public Integer ceiling(int key) {
		return boundary(key, true, true);
	}

	public Integer lower(int key) {
		return boundary(key, false, false);
	}

	public Integer floor(int key) {
		return boundary(key, true, false);
	}

	private Integer boundary(int key, boolean inclusive, boolean higher) {
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
		remove(first);
		return temp;
	}

	public int pollLast() {
		if (size == 0) throw new NoSuchElementException();
		int temp = last;
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
	private static final class Node {
		private final int label;
		private int size, height;
		private Node left, right, parent;

		public Node(int label, Node parent) {
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

		private Node add(int t) {
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

		private Node allRemove(int t) {
			if (label < t) {
				if (right == null) {
					return this;
				} else {
					setRight(right.allRemove(t));
				}
			} else if (label > t) {
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