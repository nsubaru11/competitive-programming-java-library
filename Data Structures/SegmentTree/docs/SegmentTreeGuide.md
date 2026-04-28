# SegmentTree 利用ガイド

## 概要

`SegmentTree` は、配列に対する範囲クエリと点更新を効率的に処理するためのデータ構造「セグメント木」をジェネリック型で実装したクラスです。
和、最小値、最大値など、結合法則を満たす様々な演算に適用できます。

## 特徴

- **汎用性**:
	- ジェネリック型 `<T>` を採用しており、任意のオブジェクトを格納できます。
	- 演算内容を `BinaryOperator<T>` として外部から注入できるため、柔軟なカスタマイズが可能です。
- **パフォーマンス**:
	- 配列を利用したボトムアップ方式で実装されており、再帰呼び出しのオーバーヘッドがありません。
	- `set` / `apply` による更新は都度木へ反映され、`query` 系メソッドは常に最新の状態を参照します。
- **境界探索**:
	- `maxRight` と `minLeft` を備え、条件を満たす最大/最小の端点を $O(\log N)$ で探索できます。

## 依存関係

- `java.util.function.BinaryOperator`
- `java.util.function.IntFunction`
- `java.util.function.Predicate`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                            | 説明                                                           |
|-----------------------------------------------------------------|--------------------------------------------------------------|
| `SegmentTree(int n, BinaryOperator<T> operator, T identity)`    | サイズ `n`、集約演算 `operator`、単位元 `identity` を指定して空のセグメント木を初期化します。 |
| `SegmentTree(T[] data, BinaryOperator<T> operator, T identity)` | 既存配列 `data` から木を構築します。配列長が自動でサイズとして採用され、即座にクエリ可能な状態になります。    |

### 2. 更新系メソッド

| メソッド                                      | 説明                                             |
|-------------------------------------------|------------------------------------------------|
| `set(int i, T e)`                         | インデックス `i` の要素を値 `e` に更新します。更新はその場で木全体に反映されます。 |
| `apply(int i, T v, BinaryOperator<T> op)` | インデックス `i` の現在値に `op` で `v` を適用し、結果で上書きします。    |
| `fill(T value)`                           | すべての葉を `value` で埋め、木全体を再構築します。                 |
| `setAll(IntFunction<T> func)`             | インデックス `i` を受け取り、`func.apply(i)` で全葉を一括設定します。  |

### 3. クエリ系メソッド

| メソッド                                   | 説明                                                                      |
|----------------------------------------|-------------------------------------------------------------------------|
| `get(int i)`                           | インデックス `i` の現在の値を取得します。                                                 |
| `query(int l, int r)`                  | 半開区間 `[l, r)` における集約結果を返します。                                            |
| `queryAll()`                           | すべての要素を集約した結果（根ノードの値）を返します。                                             |
| `maxRight(int l, Predicate<T> tester)` | 区間の左端 `l` を固定し、条件 `tester` を満たす最大右端を返します。満たさない場合は `elementCount` を返します。 |
| `minLeft(int r, Predicate<T> tester)`  | 区間の右端 `r` を固定し、条件を満たす最小左端を返します。満たさない場合は `0` を返します。                      |

### 4. その他

| メソッド         | 説明                                       |
|--------------|------------------------------------------|
| `size()`     | 管理している要素数（葉の数）を返します。                     |
| `iterator()` | 木が保持する要素（葉）を順番に走査する `Iterator<T>` を返します。 |
| `toString()` | 葉ノード（保持している要素）を空白区切りで列挙した文字列を返します。       |

## 利用例

`SegmentTree` を使って、配列の範囲合計（Range Sum Query）を管理する例です。

```java
import java.util.function.BinaryOperator;

public class SegmentTreeExample {
    public static void main(String[] args) {
        int n = 10;
        BinaryOperator<Integer> sumFunc = Integer::sum;
        int identity = 0;
        var segTree = new SegmentTree<>(n, sumFunc, identity);

        segTree.set(0, 5);
        segTree.set(1, 3);
        segTree.set(2, 7);
        segTree.set(3, 1);
        segTree.set(4, 8);

        int sum1 = segTree.query(1, 4);
        System.out.println("Sum of range [1, 4): " + sum1);

        segTree.apply(2, 3, Integer::sum);
        int sum2 = segTree.query(1, 4);
        System.out.println("Sum of range [1, 4) after apply: " + sum2);

        int right = segTree.maxRight(0, acc -> acc <= 20);
        System.out.println("Max right with sum <= 20: " + right);
    }
}
```

## 注意事項

- クエリ `query(l, r)` の範囲は半開区間 `[l, r)`（`l` を含み `r` を含まない）です。
- `set` / `apply` による更新は即時反映されるため、追加のビルド処理は不要です。
- `identity` に `null` を指定することも可能ですが、演算結果で `operator` が `null` を返さないよう注意してください。
- `IntSegmentTree` や `LongSegmentTree` などのプリミティブ特化版を利用すると、ボクシング/アンボクシングを避けられるため高速です。

## パフォーマンス特性

- **時間計算量**:
	- `set`/`apply`: $O(\log N)$
	- `query`/`queryAll`/`maxRight`/`minLeft`: $O(\log N)$
	- `get`: $O(1)$
- **空間計算量**:
	- 全体: $O(N)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                              |
|:--------------|:-----------|:----------------------------------------------------------------|
| **バージョン 1.0** | 2025-11-25 | 初回実装。                                                           |
| **バージョン 2.0** | 2025-11-27 | API 仕様の記載を最新コードに合わせて全面更新。                                       |
| **バージョン 2.1** | 2026-04-28 | `apply` / `setAll(IntFunction)` / 半開区間 `[l, r)` など、現行実装との差分を反映。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
