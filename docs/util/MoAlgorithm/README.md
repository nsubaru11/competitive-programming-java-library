# MoAlgorithm

## 概要

静的な区間クエリをオフラインで処理する Mo's Algorithm を提供します。クエリは登録順の番号を保持し、ジグザグ順序で区間を伸縮させます。

## 実装クラス

### [MoAlgorithm](../../../src/lib/util/MoAlgorithm.java)

- **区間表現**：`lr[0][i] = l`, `lr[1][i] = r` とする 0-indexed 半開区間 `[l, r)`
- **callback**：左・右それぞれの追加と削除を渡す 4 方向版と、共通の `add` / `remove` を渡す短縮版を提供
- **回答**：`query` callback はクエリ番号を受け取る。回答配列やクエリ固有の情報は呼び出し側が番号で管理する
- **順序**：左端のブロック番号、ブロックの偶奇で反転する右端の順序による snake order
- **ソート**：`long` パッキングとプリミティブソートを使う。`N < 2^20`, `Q <= 2^20` を前提とする

## 使用例

```java
int[][] lr = sc.nextIntMatInv(q, 2); // lr[0][i] = l, lr[1][i] = r
int[] freq = new int[valueCount];
int[] answer = new int[q];
int[] distinct = {0};

MoAlgorithm.run(n, lr,
		i -> {
			if (++freq[a[i]] == 1) distinct[0]++;
		},
		i -> {
			if (--freq[a[i]] == 0) distinct[0]--;
		},
		id -> answer[id] = distinct[0]
);
```

1-indexed 閉区間 `[L, R]` を読む場合は、`l = L - 1`, `r = R` としてから渡してください。MoAlgorithm 自体は常に `[l, r)` を受け取ります。

## 時間計算量

ブロック幅を `W` とすると、callback 呼び出し回数は `O(QW + N^2 / W)`、クエリソートを含めた全体は `O(Q log Q + QW + N^2 / W)` です。各 callback の計算量を掛け合わせて評価してください。

## 注意事項

- `lr` は 2 行 `Q` 列の配列です。`int[Q][2]` ではありません。
- 空区間 `[l, l)` を扱えます。
- `addLeft` / `addRight`、`removeLeft` / `removeRight` の処理が異なる問題では 4 方向版を使用してください。
- 区間和など結合可能な集計や、静的問題専用の高速データ構造がある場合は、Mo より適した手法があることがあります。
