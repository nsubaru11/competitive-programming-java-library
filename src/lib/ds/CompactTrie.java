package lib.ds;

/**
 * ビットマップ圧縮 Trie。各ノードが最大64文字（'a'起点）の子の有無を long のビットマップで保持し、
 * {@code Long.bitCount} による添字計算で子配列を密に管理する省メモリ設計。
 */
@SuppressWarnings("unused")
public final class CompactTrie {
	// TODO: ビットマップ圧縮Trie（insert / search / countPrefix、単一子はNode直持ちで省メモリ化）の実装を行う
}
