# LongSegmentTree 利用ガイド

## 概要

`LongSegmentTree` は、`long` 型の配列に対する範囲クエリと点更新を効率的に処理するための、セグメント木の `long` 特化実装です。
`long` 型の合計、最小値、最大値などを高速に計算する必要がある場合に使用します。

## 特徴

- **`long` 型への特化**:
	- `long` プリミティブ型を直接扱うことで、ジェネリック版の `SegmentTree`
	  で発生するボクシング・アンボクシングのオーバーヘッドをなくし、優れたパフォーマンスを実現します。
- **パフォーマンス**:
	- 配列を利用したボトムアップ方式で実装されており、再帰呼び出しのオーバーヘッドがありません。
	- `set` による更新処理は遅延評価され、`query` 系メソッド実行前にまとめて実行されるため、更新が連続する際の効率が向上します。
- **境界探索**:
	- `maxRight` と `minLeft` を備え、条件を満たす最大/最小の端点を $O(\log N)$ で探索できます。

## 依存関係

- `java.util.function.LongBinaryOperator`
- `java.util.function.LongUnaryOperator`
- `java.util.function.LongPredicate`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                                   | 説明                                                         |
|------------------------------------------------------------------------|------------------------------------------------------------|
| `LongSegmentTree(int n, LongBinaryOperator func, long identity)`       | サイズ `n`、`long` 集約ルール `func`、単位元 `identity` を指定して空の木を構築します。 |
| `LongSegmentTree(long[] data, LongBinaryOperator func, long identity)` | 既存配列 `data` から木を構築します。長さは自動でサイズとして採用され、即利用可能です。            |

### 2. 更新系メソッド

| メソッド                                    | 説明                                                      |
|-----------------------------------------|---------------------------------------------------------|
| `set(int i, long e)`                    | インデックス `i` の要素を `e` に更新します。更新は遅延キューに記録され、クエリ前に一括反映されます。 |
| `update(int i, LongUnaryOperator func)` | インデックス `i` の値に `func` を適用して更新します。                       |
| `fill(long value)`                      | すべての葉を `value` で埋め、木全体を再構築します。                          |
| `setAll(LongUnaryOperator func)`        | 全葉に `func` を適用し、一括更新します。                                |

### 3. クエリ系メソッド

| メソッド                                    | 説明                                                                   |
|-----------------------------------------|----------------------------------------------------------------------|
| `get(int i)`                            | インデックス `i` の現在値を取得します。                                               |
| `query(int l, int r)`                   | 閉区間 `[l, r]` の集約結果を返します。                                             |
| `queryAll()`                            | すべての要素を集約した結果（根ノードの値）を返します。                                          |
| `maxRight(int l, LongPredicate tester)` | 左端 `l` を固定し、条件 `tester` を満たす最大右端を返します。満たさない場合は `elementCount` を返します。 |
| `minLeft(int r, LongPredicate tester)`  | 右端 `r` を固定し、条件を満たす最小左端を返します。満たさない場合は `0` を返します。                      |

### 4. その他

| メソッド         | 説明                                         |
|--------------|--------------------------------------------|
| `size()`     | 管理している要素数（葉の数）を返します。                       |
| `iterator()` | 葉を順に走査する `PrimitiveIterator.OfLong` を返します。 |
| `toString()` | 内部配列全体を空白区切りで列挙した文字列を返します。                 |

## 利用例

`LongSegmentTree` を使って、配列の範囲合計（Range Sum Query）を管理する例です。

```java
import java.util.function.LongBinaryOperator;

public class LongSegmentTreeExample {
    public static void main(String[] args) {
        int n = 10;
        LongBinaryOperator sumFunc = (a, b) -> a + b;
        long identity = 0L;
        var segTree = new LongSegmentTree(n, sumFunc, identity);

        segTree.set(0, 5L);
        segTree.set(1, 3L);
        segTree.set(2, 7L);
        segTree.set(3, 1L);
        segTree.set(4, 8L);

        long sum1 = segTree.query(1, 3);
        System.out.println("Sum of range [1, 3]: " + sum1);

        segTree.update(2, v -> v + 3L);
        long sum2 = segTree.query(1, 3);
        System.out.println("Sum of range [1, 3] after update: " + sum2);

        int right = segTree.maxRight(0, acc -> acc <= 20L);
        System.out.println("Max right with sum <= 20: " + right);
    }
}
```

## 注意事項

- クエリ `query(l, r)` の範囲は閉区間 `[l, r]`（`l` と `r` を両方含む）です。
- `set` 系更新は遅延キューに積まれ、`query`/`queryAll`/`maxRight`/`minLeft` などを呼び出す際に自動で反映されるため、利用者が
  `build` を明示的に呼ぶ必要はありません。
- `identity` が `0L` でない場合のみ、空の葉を単位元で初期化します。必要な単位元を必ず渡してください。

## パフォーマンス特性

- **時間計算量**:
	- `set`/`update`: $O(\log N)$
	- `query`/`queryAll`/`maxRight`/`minLeft`: $O(\log N)$
	- `get`: $O(1)$
- **空間計算量**:
	- 全体: $O(N)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                           |
|:--------------|:-----------|:-----------------------------|
| **バージョン 1.0** | 2025-11-25 | 初回実装。                        |
| **バージョン 2.0** | 2025-11-27 | コンストラクタ・API 説明、境界探索機能の記述を追加。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
