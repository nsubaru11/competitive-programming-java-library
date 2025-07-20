# Ring Buffer

## 概要

リングバッファ（環状バッファ、循環バッファとも呼ばれる）は、固定サイズの配列を循環的に使用するデータ構造です。先頭と末尾からの追加・削除が効率的に行えるため、キューやデックの実装に適しています。このライブラリでは、汎用的なリングバッファと、int型およびlong型に特化した高効率な実装を提供しています。

## 実装クラス

### [RingBuffer](./src/RingBuffer.java)

- **用途**：任意の型の要素を扱う汎用的なリングバッファ
- **特徴**：
	- ジェネリック型をサポート
	- 先頭と末尾からの効率的な追加・削除操作
	- ランダムアクセス（インデックスによる要素の取得・設定）
	- 動的な論理サイズの変更
	- Iterableインターフェースの実装によるfor-each構文のサポート
	- Cloneableインターフェースの実装によるディープコピーのサポート
	- ストリームAPIのサポート
- **主な操作**:
	- `addLast(T e)/addFirst(T e)`: 末尾/先頭に要素を追加
	- `pollLast()/pollFirst()`: 末尾/先頭の要素を取り出して削除
	- `peekLast()/peekFirst()`: 末尾/先頭の要素を参照（削除しない）
	- `get(int index)/set(int index, T e)`: インデックスによる要素の取得/設定
	- `setLength(int newLen)`: 論理サイズの変更
	- `size()/capacity()/remainingCapacity()`: サイズ、容量、残り容量の取得
	- `isEmpty()/isFull()`: 空/満杯かどうかの判定
	- `clear()`: すべての要素を削除
	- `toArray()/toList()`: 配列/リストへの変換
- **時間計算量**：
	- 先頭/末尾への追加/削除: O(1)
	- インデックスによるアクセス/設定: O(1)
	- 要素の検索: O(n)
- **空間計算量**：O(n)、ここでnは容量

### [IntegerRingBuffer](./src/IntegerRingBuffer.java)

- **用途**：int型の要素に特化したリングバッファ
- **特徴**：
	- プリミティブ型の配列を使用し、ボクシング/アンボクシングのオーバーヘッドを回避
	- RingBufferと同様の機能セット
	- IntStreamによるストリーム操作のサポート
- **主な操作**:
	- `addLast(int e)/addFirst(int e)`: 末尾/先頭に要素を追加
	- `pollLast()/pollFirst()`: 末尾/先頭の要素を取り出して削除
	- `peekLast()/peekFirst()`: 末尾/先頭の要素を参照
	- `get(int index)/set(int index, int e)`: インデックスによる要素の取得/設定
	- `setLength(int newLen)`: 論理サイズの変更
	- `size()/capacity()/remainingCapacity()`: サイズ、容量、残り容量の取得
	- `isEmpty()/isFull()`: 空/満杯かどうかの判定
	- `clear()`: すべての要素を削除
	- `toArray()/toList()`: 配列/リストへの変換
	- `stream()/parallelStream()`: IntStreamの取得
- **時間計算量**：
	- 先頭/末尾への追加/削除: O(1)
	- インデックスによるアクセス/設定: O(1)
	- 要素の検索: O(n)
- **空間計算量**：O(n)

### [LongRingBuffer](./src/LongRingBuffer.java)

- **用途**：long型の要素に特化したリングバッファ
- **特徴**：
	- プリミティブ型の配列を使用し、ボクシング/アンボクシングのオーバーヘッドを回避
	- RingBufferと同様の機能セット
	- LongStreamによるストリーム操作のサポート
- **主な操作**:
	- `addLast(long e)/addFirst(long e)`: 末尾/先頭に要素を追加
	- `pollLast()/pollFirst()`: 末尾/先頭の要素を取り出して削除
	- `peekLast()/peekFirst()`: 末尾/先頭の要素を参照
	- `get(int index)/set(int index, long e)`: インデックスによる要素の取得/設定
	- `setLength(int newLen)`: 論理サイズの変更
	- `size()/capacity()/remainingCapacity()`: サイズ、容量、残り容量の取得
	- `isEmpty()/isFull()`: 空/満杯かどうかの判定
	- `clear()`: すべての要素を削除
	- `toArray()/toList()`: 配列/リストへの変換
	- `stream()/parallelStream()`: LongStreamの取得
- **時間計算量**：
	- 先頭/末尾への追加/削除: O(1)
	- インデックスによるアクセス/設定: O(1)
	- 要素の検索: O(n)
- **空間計算量**：O(n)

## 選択ガイド

- **RingBuffer<T>**: 整数以外の型（文字列、カスタムオブジェクトなど）を扱う場合に使用します。
- **IntegerRingBuffer**: int型の値のみを扱う場合に使用します。ボクシング/アンボクシングのオーバーヘッドがないため、整数値を扱う場合はこちらの方が高速です。
- **LongRingBuffer**: long型の値を扱う場合や、int型の範囲を超える可能性がある場合に使用します。IntegerRingBufferと同様に、プリミティブ型に特化しているため高速です。

## 注意事項

- リングバッファが満杯の状態で追加操作を行うと、`false`が返され、バッファは変更されません。
- 空のリングバッファから要素を取り出そうとすると、`NoSuchElementException`が発生します。
- インデックスが範囲外の場合、`RingBufferIndexException`が発生します。
- 論理インデックスと物理インデックスの変換は内部で自動的に行われるため、ユーザーは循環的なアクセスを意識する必要はありません。
- `setLength`メソッドを使用して論理サイズを変更する場合、新しいサイズが容量を超えることはできません。