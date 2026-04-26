# HashMap

## 概要

オープンアドレス法（線形探索）で実装した競技プログラミング向け HashMap クラス群を提供します。  
`int`/`long` の値型に特化したベース実装と、`int` のペア・トリプルをキーとして扱うラッパークラスを含みます。

## 実装クラス

### [BaseIntIntMap](src/BaseIntIntMap.java)

- **用途**：`int -> int` の高速マップ
- **特徴**：
	- 墓石（tombstone）付きオープンアドレス法
	- `put`/`add`/`merge`/`putIfAbsent` をサポート
- **時間計算量**：
	- 平均: 参照・更新ともに $O(1)$
	- 最悪: $O(N)$
- **空間計算量**：$O(N)$
- **ガイド**：[BaseMapGuide](docs/BaseMapGuide.md)

### [BaseLongIntMap](src/BaseLongIntMap.java)

- **用途**：`long -> int` の高速マップ
- **特徴**：
	- `long` キーを直接扱えるため、複合キーのパック先として使いやすい
	- `forEach`/`keys`/`entries` などの走査 API を提供
- **時間計算量**：`BaseIntIntMap` と同様
- **空間計算量**：`BaseIntIntMap` と同様
- **ガイド**：[BaseMapGuide](docs/BaseMapGuide.md)

### [BaseLongLongMap](src/BaseLongLongMap.java)

- **用途**：`long -> long` の高速マップ
- **特徴**：
	- `long` 値のカウンタや重み管理にそのまま使える
	- `LongBinaryOperator` による `merge` を利用可能
- **時間計算量**：`BaseIntIntMap` と同様
- **空間計算量**：`BaseIntIntMap` と同様
- **ガイド**：[BaseMapGuide](docs/BaseMapGuide.md)

### [IntPairIntMap](src/IntPairIntMap.java) / [IntPairLongMap](src/IntPairLongMap.java)

- **用途**：`(int, int)` をキーにしたマップ
- **特徴**：
	- 2つの `int` を `long` にパックしてベース実装に委譲
	- API は `a, b` を直接受け取るため、呼び出し側の可読性が高い
- **時間計算量**：ベース実装と同様
- **空間計算量**：ベース実装と同様
- **ガイド**：[PairMapGuide](docs/PairMapGuide.md)

### [IntTripleIntMap](src/IntTripleIntMap.java) / [IntTripleLongMap](src/IntTripleLongMap.java)

- **用途**：`(int, int, int)` をキーにしたマップ
- **特徴**：
	- 各キーを 21bit でパックして `long` 化（`MASK = 0x1FFFFF`）
	- `forEach`/`forEachKey` で自動的に 3 要素へ復元して処理可能
- **時間計算量**：ベース実装と同様
- **空間計算量**：ベース実装と同様
- **ガイド**：[TripleMapGuide](docs/TripleMapGuide.md)

## アルゴリズム（データ構造）選択ガイド

- **`BaseIntIntMap`**:
	- 単純な整数キー・整数値の管理なら最優先。
- **`BaseLongIntMap`**:
	- 64bit キーや、複合キーを自前で `long` にパックして管理したい場合に選択。
- **`BaseLongLongMap`**:
	- 値も `long` で保持したい場合に選択。
- **`IntPair*Map`**:
	- 2次元状態（例: 座標、頂点ペア）のキー管理向け。
- **`IntTriple*Map`**:
	- 3次元状態のキー管理向け。各キー成分が 21bit に収まる前提で使う。

## 注意事項

- 本実装は競技プログラミング用途を想定し、平均計算量重視です。
- `IntTriple*Map` は内部で 21bit パックを行うため、キー範囲前提を満たして使用してください。
- `get` はキー未存在時に例外を送出するため、必要に応じて `getOrDefault` を利用してください。
