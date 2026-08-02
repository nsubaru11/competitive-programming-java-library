# HashMap

## 概要

オープンアドレス法（線形探索）で実装した競技プログラミング向けの整数型マップクラス群を提供します。
`int`/`long` のキーと値に特化したマップと、`int` のペア・トリプルをキーとして扱うラッパークラスを含みます。

## 実装クラス

### [IntIntMap](../../../src/lib/ds/map/IntIntMap.java)

- **用途**：`int -> int` の高速マップ
- **特徴**：
	- backward-shift deletion による削除
	- 世代番号を使った `clear()` の O(1) 初期化
	- 未存在キー用の `defaultValue` を設定可能
	- `put`/`add`/`merge`/`putIfAbsent` をサポート
	- `reduce`/`reduceKeys`/`reduceValues` をサポート
- **時間計算量**：
	- 平均: 参照・更新ともに $\mathcal{O}(1)$
	- 最悪: $\mathcal{O}(N)$
- **空間計算量**：$\mathcal{O}(N)$
- **ガイド**：[MapGuide](./MapGuide.md)

### [LongIntMap](../../../src/lib/ds/map/LongIntMap.java)

- **用途**：`long -> int` の高速マップ
- **特徴**：
	- backward-shift deletion による削除
	- 世代番号を使った `clear()` の O(1) 初期化
	- 未存在キー用の `defaultValue` を設定可能
	- `long` キーを直接扱えるため、複合キーのパック先として使いやすい
	- `forEach`/`keys`/`entries` などの走査 API を提供
	- `reduce`/`reduceKeys`/`reduceValues` をサポート
- **時間計算量**：`IntIntMap` と同様
- **空間計算量**：`IntIntMap` と同様
- **ガイド**：[MapGuide](./MapGuide.md)

### [LongLongMap](../../../src/lib/ds/map/LongLongMap.java)

- **用途**：`long -> long` の高速マップ
- **特徴**：
	- backward-shift deletion による削除
	- 世代番号を使った `clear()` の O(1) 初期化
	- 未存在キー用の `defaultValue` を設定可能
	- `long` 値のカウンタや重み管理にそのまま使える
	- `LongBinaryOperator` による `merge` を利用可能
	- `reduce`/`reduceKeys`/`reduceValues` をサポート
- **時間計算量**：`IntIntMap` と同様
- **空間計算量**：`IntIntMap` と同様
- **ガイド**：[MapGuide](./MapGuide.md)

### [IntPairIntMap](../../../src/lib/ds/map/IntPairIntMap.java) / [IntPairLongMap](../../../src/lib/ds/map/IntPairLongMap.java)

- **用途**：`(int, int)` をキーにしたマップ
- **特徴**：
	- 2つの `int` を `long` にパックして整数型マップに委譲
	- API は `a, b` を直接受け取るため、呼び出し側の可読性が高い
	- `reduce`/`reduceKeys`/`reduceValues` による集約をサポート
- **時間計算量**：委譲先の整数型マップと同様
- **空間計算量**：委譲先の整数型マップと同様
- **ガイド**：[PairMapGuide](./PairMapGuide.md)

### [IntTripleIntMap](../../../src/lib/ds/map/IntTripleIntMap.java) / [IntTripleLongMap](../../../src/lib/ds/map/IntTripleLongMap.java)

- **用途**：`(int, int, int)` をキーにしたマップ
- **特徴**：
	- 各キーを 21bit でパックして `long` 化（`MASK = 0x1FFFFF`）
	- `forEach`/`forEachKey` で自動的に 3 要素へ復元して処理可能
	- `reduce`/`reduceKeys`/`reduceValues` による集約をサポート
- **時間計算量**：委譲先の整数型マップと同様
- **空間計算量**：委譲先の整数型マップと同様
- **ガイド**：[TripleMapGuide](./TripleMapGuide.md)

## アルゴリズム（データ構造）選択ガイド

- **`IntIntMap`**:
	- 単純な整数キー・整数値の管理なら最優先。
- **`LongIntMap`**:
	- 64bit キーや、複合キーを自前で `long` にパックして管理したい場合に選択。
- **`LongLongMap`**:
	- 値も `long` で保持したい場合に選択。
- **`IntPair*Map`**:
	- 2次元状態（例: 座標、頂点ペア）のキー管理向け。
- **`IntTriple*Map`**:
	- 3次元状態のキー管理向け。各キー成分が 21bit に収まる前提で使う。

## 注意事項

- 本実装は競技プログラミング用途を想定し、平均計算量重視です。
- `IntTriple*Map` は内部で 21bit パックを行うため、キー範囲前提を満たして使用してください。
- `get` はキー未存在時に設定済みの `defaultValue` を返します。
- `getOrDefault` は、その呼び出しだけに適用する任意の既定値を指定します。
- `containsKey` を使うと、格納値と未存在時の `defaultValue` を区別できます。
