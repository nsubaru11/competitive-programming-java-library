# RingBuffer / IntegerRingBuffer / LongRingBuffer 利用ガイド

## 概要

`RingBuffer`、`IntegerRingBuffer`、`LongRingBuffer`は、競技プログラミングでの利用に特化して設計された、固定長のリングバッファです。
ジェネリクス版、およびプリミティブ型（`int`, `long`）に特化した実装を提供し、パフォーマンスを重視した設計となっています。

## 特徴

- **容量の自動正規化**: コンストラクタに指定された容量（`capacity`）は、内部的に最も近い2のべき乗の値に自動的に正規化されます。
- **高速なインデックス計算**: 容量を2のべき乗に保つことで、剰余演算子（`%`）の代わりに高速なビット演算子（`&`
	）を用いて配列のインデックスを計算します。これにより、要素の追加や取得処理が高速化されています。
- **プリミティブ型への特化**: `IntegerRingBuffer`と`LongRingBuffer`は、それぞれ`int`型と`long`
	型に特化しています。これにより、ジェネリクス版で発生するボクシング・アンボクシングのオーバーヘッドを回避し、さらなるパフォーマンス向上を実現しています。
- **可読性のためのStream API**: `contains`や`toString`など、全要素のスキャンが必要な`O(N)`
	の計算量を持つメソッドでは、パフォーマンスよりも可読性を重視し、Stream APIを意図的に採用しています。

## 依存関係

標準のJavaライブラリ以外に、外部の依存関係はありません。

## 主な機能（メソッド一覧）

3つのクラスはほぼ共通のAPIを持っています。以下は`IntegerRingBuffer`を例としたメソッド一覧です。ジェネリクス版では型引数
`T`、`long`版では`long`が使われます。

### 1. 要素の追加・削除

| メソッド          | 戻り値の型     | 説明                | 計算量  |
|:--------------|:----------|:------------------|:----:|
| `addLast(e)`  | `boolean` | バッファの末尾に要素を追加します。 | O(1) |
| `addFirst(e)` | `boolean` | バッファの先頭に要素を追加します。 | O(1) |
| `pollLast()`  | `int`     | 末尾の要素を取得して削除します。  | O(1) |
| `pollFirst()` | `int`     | 先頭の要素を取得して削除します。  | O(1) |

### 2. 要素の参照

| メソッド          | 戻り値の型 | 説明                   | 計算量  |
|:--------------|:------|:---------------------|:----:|
| `get(index)`  | `int` | 指定インデックスの要素を取得します。   | O(1) |
| `peekLast()`  | `int` | 末尾の要素を取得します（削除はしない）。 | O(1) |
| `peekFirst()` | `int` | 先頭の要素を取得します（削除はしない）。 | O(1) |

### 3. 更新・操作

| メソッド                | 戻り値の型               | 説明                  | 計算量  |
|:--------------------|:--------------------|:--------------------|:----:|
| `set(index, e)`     | `IntegerRingBuffer` | 指定インデックスの要素を上書きします。 | O(1) |
| `setLength(newLen)` | `IntegerRingBuffer` | バッファの長さを変更します。      | O(L) |
| `clear()`           | `IntegerRingBuffer` | バッファを空にします。         | O(1) |
| `fill(e)`           | `IntegerRingBuffer` | 全ての要素を指定値で埋めます。     | O(C) |
| `setAll(generator)` | `IntegerRingBuffer` | 関数を用いて全ての要素を初期化します。 | O(C) |

*L: 変更後の長さと現在の長さの差, C: 容量*

## 利用例

`IntegerRingBuffer`を使って、いくつかの整数を格納し、先頭から取り出す例です。

```java
import competitive.programming.java.library.DataStructures.RingBuffer.src.IntegerRingBuffer;

public class Main {
    public static void main(String[] args) {
        // 容量8で作成（最も近い2のべき乗である8に正規化される）
        IntegerRingBuffer buffer = new IntegerRingBuffer(8);

        // 末尾に要素を追加
        buffer.addLast(10); // [10]
        buffer.addLast(20); // [10, 20]
        buffer.addLast(30); // [10, 20, 30]

        // 先頭に要素を追加
        buffer.addFirst(5); // [5, 10, 20, 30]

        System.out.println("Size: " + buffer.size()); // => Size: 4
        System.out.println("Buffer: " + buffer.toString()); // => Buffer: 5 10 20 30

        // 先頭から要素を取得・削除
        int first = buffer.pollFirst();
        System.out.println("Polled: " + first); // => Polled: 5
        System.out.println("New Buffer: " + buffer.toString()); // => New Buffer: 10 20 30

        // インデックスで要素を取得
        System.out.println("Element at index 1: " + buffer.get(1)); // => Element at index 1: 20
    }
}
```

## 注意事項

- **堅牢性よりパフォーマンス**: このクラスは競技プログラミングでの使用を前提としており、一般的なライブラリに求められる厳密な引数チェックを意図的に省略しています。
	- `capacity`に`0`以下の値を渡すと、意図しない容量（`1`）でバッファが生成される可能性があります。
	- `RingBuffer<T>`（ジェネリクス版）で`null`を扱うと`NullPointerException`が発生する可能性があります。
- **メモリリークの許容**: `RingBuffer<T>`では、`poll`操作時に配列内の古いオブジェクト参照を`null`
	でクリアしません。これは、プログラムが短時間で終了する競技プログラミングの文脈では、パフォーマンスへの影響が無視できるためです。
- **`Deque`インターフェース非実装**: 標準の`java.util.Deque`インターフェースは実装していません。APIは似ていますが、相互運用性はありません。

## パフォーマンス特性

- **時間計算量**:
	- `addFirst`, `addLast`, `pollFirst`, `pollLast`, `get`, `set`, `peekFirst`, `peekLast`: **O(1)**
	- `contains`, `toString`, `equals`, `stream`, `toList`, `toArray`: **O(N)** （Nはバッファ内の要素数）
	- `setLength`: **O(|newLen - size|)**
	- `fill`, `setAll`: **O(C)** （Cはバッファの容量）
- **空間計算量**: **O(C)** （Cはバッファの容量）

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                                                                                      |
|:--------------|:-----------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-07-20 | 初期バージョンのリリース。                                                                                                                                                           |
| **バージョン 2.0** | 2025-09-26 | パフォーマンス向上のためのメジャーアップデート。<ul><li>コンストラクタで指定された容量を2のべき乗に正規化する機能を追加。</li><li>剰余演算（`%`）を高速なビット演算（`&`）に置き換え。</li><li>可読性のため、`O(N)`メソッドでのStream API使用を許容する設計方針を明確化。</li></ul> |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
