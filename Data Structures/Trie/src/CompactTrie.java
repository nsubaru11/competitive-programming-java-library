public final class CompactTrie {
	private Node root = new Node();

	private static class Node {
		long bitmap; // 64文字までビットマップ
		Object children; // Node[] or Node (単一子の場合)
		boolean end;

		Node getChild(char c) {
			int idx = c - 'a';
			if (idx >= 64 || (bitmap & (1L << idx)) == 0) return null;

			if (children instanceof Node) {
				return (Node) children; // 単一子
			}

			// ビットカウントで実際のインデックス計算
			int actualIdx = Long.bitCount(bitmap & ((1L << idx) - 1));
			return ((Node[]) children)[actualIdx];
		}
	}
}
