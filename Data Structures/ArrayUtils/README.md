# Array Utils

## 概要

Java24競技プログラミング向けの高速配列ユーティリティライブラリです。
1次元・2次元配列に対して、O(1)の回転・シフト操作、統計計算、部分列・部分和アルゴリズムを提供し、
物理的なデータ移動を最小限に抑えた効率的な実装を実現します。

## 実装クラス

### [IntArray1D](./src/IntArray1D.java)

- **用途**: 整数配列の回転と各種統計・数列処理
- **特徴**:
	- **Double Buffering**: 配列を2周確保し、参照時のビット演算・剰余演算を排除
	- 循環シフト操作（lShift/rShift）: O(1)
	- 区間和取得（sum）: O(1)（分岐なし単純加算）
	- 統計分析: localMaxCnt, localMinCnt, runLen
	- 部分列: lis, lnds, lds, lnis
	- 部分和判定: subset（複数アルゴリズムから自動選択）
- **時間計算量**:
	- 基本操作（get/set/シフト）: O(1)
	- 統計メソッド: O(n)
	- 部分列（LIS等）: O(n log n)
	- 部分和: O(n√n) 〜 O(2^(n/2))
- **空間計算量**: O(n) （2周確保のため実質 2n）

### [LongArray1D](./src/LongArray1D.java)

- **用途**: 長整数配列の回転と各種統計・数列処理
- **特徴**: IntArray1Dと同等（long型対応）
- **時間計算量**: IntArray1Dと同じ
- **空間計算量**: O(n)

### [IntArray2D](./src/IntArray2D.java)

- **用途**: 2次元int配列の論理回転・転置対応アクセス
- **特徴**:
	- 回転操作（lRotate/rRotate）: O(1)
	- 正方行列の転置トグル（transpose）: O(1) 論理トグル
	- 論理座標での統一的なアクセス（get/set）
- **時間計算量**:
	- アクセス/設定: O(1)
	- 回転操作: O(1)
	- 転置トグル: O(1)
- **空間計算量**: O(h*w)

### [LongArray2D](./src/LongArray2D.java)

- **用途**: 2次元long配列の論理回転・転置対応アクセス
- **特徴**: IntArray2Dと同等（long型対応）
- **計算量/空間**: IntArray2Dと同じ

## アルゴリズム選択ガイド

### IntArray1D / LongArray1D

**シフト・区間和**: 循環配列が必要なら使用。回転後も要素アクセス・区間和が高速。

**統計分析**:

- `localMaxCnt()` / `localMinCnt()`: 極値の個数を O(n) で計算
- `runLen()`: 同じ値が連続する最長長さ
- `winMaxLen(k)` / `winMinLen(k)`: 長さk窓での最大/最小値が連続する期間

**部分列**:

- `lis()`: 狭義単調増加の最長部分列
- `lnds()`: 広義単調増加の最長部分列
- `lds()` / `lnis()`: 減少版

**部分和**:

- `subset(target)`: 任意個選択して和がtargetになるか（自動アルゴリズム選択）
- `subset(target, k)`: ちょうどk個選んで和がtargetになるか
	- 自動選択: DP版 → 中程左右分割 → 探索版

### IntArray2D / LongArray2D

**回転が必要**な場合（90度回転に相当）:

- lRotate()/rRotate()でO(1)に
- get(i,j)で論理座標から物理インデックスに変換

**転置が必要**な場合: transpose()で正方行列に限り論理転置トグル

## 利用例

```java
// IntArray1D: 循環シフト + 区間和
IntArray1D arr = new IntArray1D(5, i -> i + 1); // [1,2,3,4,5]
arr.lShift(); // 論理的に [2,3,4,5,1]
System.out.println(arr.sum(0, 2)); // 2+3+4 = 9

// LongArray1D: 部分和判定
LongArray1D la = new LongArray1D(4, i -> (long)(i * 2 + 1)); // [1,3,5,7]
boolean can = la.subset(10, 2); // 3+7=10, true

// IntArray2D: 2次元配列の回転
IntArray2D grid = new IntArray2D(2, 3, (i, j) -> i * 3 + j);
grid.lRotate(); // 左回転
int val = grid.get(0, 0); // 物理位置に変換してアクセス
```

## パフォーマンス特性

### ステップ数算出（IntArray1D.lis）

```
lis(): O(n log n)
ステップ数 = n * (bs: 2 log n + dp操作: 3) ≈ 5n log n + n
```

### 最適化技法

- **Double Buffering (2周確保)**: 参照・累積和計算から剰余(%)・ビット演算(&)を排除し、単純加算のみでアクセス
- **プリミティブ配列**: int[], long[] で GC 負荷を最小化
- **JIT最適化指向**: 分岐を減らし、ホットループを展開しやすい構造
- **回転カウント**: 物理移動を O(1) に削減

## 注意事項

- IntArray1D / LongArray1D は配列構築後、要素値は不変（`set`メソッドなし）
- Double Buffering によりメモリ使用量は `2*n` となります
- 部分和問題（subset）は回転を考慮しない（物理順に処理）

## バージョン情報

| バージョン   | 日付         | 詳細                                                 |
|---------|------------|----------------------------------------------------|
| **1.0** | 2025-01-15 | 初版。IntArray1D / Array2D 実装                         |
| **2.0** | 2025-10-13 | メソッド名短縮、LongArray1D追加、Array2D回転対応set等              |
| **2.1** | 2025-11-19 | ガイド再整備・用語統一、2Dガイド追加、Java 24.0.2 動作確認、2Dのswitch互換化  |
| **3.0** | 2026-01-06 | 1Dについて、Double Buffering導入。Mask/Capacity方式を廃止し完全高速化 |
