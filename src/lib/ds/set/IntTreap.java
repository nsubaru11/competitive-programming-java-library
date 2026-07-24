package lib.ds.set;

import java.util.*;
import java.util.stream.*;

@SuppressWarnings("unused")
public final class IntTreap implements Iterable<Integer> {
	// -------------- Fields --------------
	private Node root;
	private int first, last;
	private int size;

	// -------------- Constructors --------------
	public IntTreap() {
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
		final StringJoiner sj = new StringJoiner(", ", "[", "]");
		final PrimitiveIterator.OfInt it = iterator();
		while (it.hasNext()) sj.add(Integer.toString(it.nextInt()));
		return sj.toString();
	}

	// -------------- Contains --------------
	public boolean contains(final int t) {
		if (size == 0) return false;
		Node cur = root;
		while (cur != null) {
			final int label = cur.label;
			if (label == t) break;
			cur = label < t ? cur.right : cur.left;
		}
		return cur != null;
	}

	public boolean containsAll(final Collection<Integer> c) {
		if (size == 0) return c.isEmpty();
		boolean contains = true;
		for (final int t : c) {
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
		final int oldSize = size;
		root = root.add(t);
		update();
		return size != oldSize;
	}

	public boolean addAll(final Collection<Integer> c) {
		final int oldSize = size;
		for (final int a : c) add(a);
		return size != oldSize;
	}

	// -------------- Remove --------------
	public boolean remove(final int t) {
		if (size == 0) return false;
		final int oldSize = size;
		root = root.remove(t);
		update();
		final boolean removed = size != oldSize;
		if (size > 0 && removed) {
			if (t == first) first = leftmost(root).label;
			if (t == last) last = rightmost(root).label;
		}
		return removed;
	}

	public boolean removeAll(final Collection<Integer> c) {
		if (isEmpty()) return false;
		final int oldSize = size;
		final Collection<Integer> hs = c instanceof Set ? c : new HashSet<>(c);
		for (final int v : hs) remove(v);
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

	// -------------- Merge --------------
	// TODO: 現在の実装はキー範囲が交差する2つのTreapに対してBST性を破壊する（splitを使った正しいマージへの再実装が必要）
	public void merge(final IntTreap treap2) {
		if (treap2.isEmpty()) return;
		if (size == 0) {
			root = treap2.root;
			first = treap2.first();
			last = treap2.last();
			size = treap2.size();
			treap2.clear();
			return;
		}
		int mergeFirst = treap2.first(), mergeLast = treap2.last();
		if (mergeFirst < first) first = mergeFirst;
		if (mergeLast > last) last = mergeLast;
		root = root.merge(treap2.root);
		update();
		treap2.clear();
	}

	// -------------- Split --------------
	// TODO: split系4メソッドの実装（key超過/以上/未満/以下の要素を新しいTreapとして切り出す）。
	//  未実装のため呼び出し時は例外を送出する（以前は空のTreapを返しサイレントに誤動作していた）

	public IntTreap splitHigher(final int key) {
		throw new UnsupportedOperationException("TODO: splitHigher は未実装");
	}

	public IntTreap splitCeiling(final int key) {
		throw new UnsupportedOperationException("TODO: splitCeiling は未実装");
	}

	public IntTreap splitLower(final int key) {
		throw new UnsupportedOperationException("TODO: splitLower は未実装");
	}

	public IntTreap splitFloor(final int key) {
		throw new UnsupportedOperationException("TODO: splitFloor は未実装");
	}

	// -------------- Arrays --------------
	public int[] toArray() {
		if (size == 0) return new int[0];
		final int[] arr = new int[size];
		final PrimitiveIterator.OfInt it = iterator();
		for (int i = 0; it.hasNext(); i++) arr[i] = it.nextInt();
		return arr;
	}

	// -------------- Streams --------------
	public IntStream stream() {
		final int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT;
		return StreamSupport.intStream(Spliterators.spliterator(iterator(), size, characteristics), false);
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
				final int label = cur.label;
				cur = successor(cur);
				return label;
			}
		};
	}

	// -------------- Access by Index --------------
	public int getByIndex(int index) {
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
	public int indexOf(final int t) {
		if (size == 0) return -1;
		final int index = rank(t);
		return index >= 0 ? index : -1;
	}

	public int rank(final int t) {
		Node cur = root;
		int index = 0;
		while (cur != null) {
			final int label = cur.label;
			if (label < t) {
				index += cur.leftSize() + 1;
				cur = cur.right;
			} else if (label > t) {
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
			final int label = cur.label;
			if (higher) {
				if (label > key || (inclusive && label == key)) {
					t = label;
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			} else {
				if (label < key || (inclusive && label == key)) {
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
	public int first() {
		return first;
	}

	public int last() {
		return last;
	}

	public int pollFirst() {
		if (size == 0) throw new NoSuchElementException();
		final int temp = first;
		removeAt(0);
		return temp;
	}

	public int pollLast() {
		if (size == 0) throw new NoSuchElementException();
		final int temp = last;
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
	private static final class Node {
		private static int seed = (int) System.nanoTime();
		private final int label, priority;
		private int size;
		private Node left, right, parent;

		private Node(final int label, final Node parent) {
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

		private Node add(final int t) {
			if (label < t) {
				setRight(right == null ? new Node(t, this) : right.add(t));
				if (priority < right.priority) return rotateL();
				size = leftSize() + rightSize() + 1;
			} else if (label > t) {
				setLeft(left == null ? new Node(t, this) : left.add(t));
				if (priority < left.priority) return rotateR();
				size = leftSize() + rightSize() + 1;
			}
			return this;
		}

		private Node remove(final int t) {
			if (label < t) {
				if (right != null) setRight(right.remove(t));
			} else if (label > t) {
				if (left != null) setLeft(left.remove(t));
			} else {
				return removeDown();
			}
			size = leftSize() + rightSize() + 1;
			return this;
		}

		private Node merge(Node n) {
			if (n == null) return this;
			if (label == n.label) n = n.removeDown();
			if (priority > n.priority) {
				if (label < n.label) {
					setRight(right != null ? right.merge(n) : n);
				} else {
					setLeft(left != null ? left.merge(n) : n);
				}
				return this;
			} else {
				if (label < n.label) {
					n.setLeft(n.left != null ? n.left.merge(this) : this);
				} else {
					n.setRight(n.right != null ? n.right.merge(this) : this);
				}
				return n;
			}
		}

		// TODO: split（境界keyでノードを2つの部分木に分割する再帰処理）の実装を行う

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
