# HashMap

> `lib.ds.map` の int・long 特化のオープンアドレス法ハッシュマップと、int の組・3つ組をキーとするマップをまとめる。

全クラスが未存在キー用の `defaultValue`、`java.util.Map` と同様に操作前の値を返す `put`・`putIfAbsent`・`remove`、O(1) の `clear`、`computeIfAbsent`・`mergeMin`・`mergeMax` などの更新、コピーコンストラクタを持つ。Pair・Triple Map はキーを long へパックして `LongIntMap`・`LongLongMap` へ委譲する。

## クラス一覧

| クラス             | 用途                                                                             | Guide                                 | ソース                                                | Example / 検証                                                                                                        |
|--------------------|----------------------------------------------------------------------------------|---------------------------------------|-------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `IntIntMap`        | int キー・int 値のオープンアドレス法ハッシュマップ                               | [MapGuide](./MapGuide.md)             | [Java](../../../src/lib/ds/map/IntIntMap.java)        | [Test](../../../test/unit/ds/map/intintmap/Test.java)                                                                 |
| `IntLongMap`       | int キー・long 値のオープンアドレス法ハッシュマップ                              | [MapGuide](./MapGuide.md)             | [Java](../../../src/lib/ds/map/IntLongMap.java)       | —                                                                                                                     |
| `LongIntMap`       | long キー・int 値のオープンアドレス法ハッシュマップ。Pair・Triple Map の委譲先   | [MapGuide](./MapGuide.md)             | [Java](../../../src/lib/ds/map/LongIntMap.java)       | [Test](../../../test/unit/ds/map/longintmap/Test.java)                                                                |
| `LongLongMap`      | long キー・long 値のオープンアドレス法ハッシュマップ。Pair・Triple Map の委譲先  | [MapGuide](./MapGuide.md)             | [Java](../../../src/lib/ds/map/LongLongMap.java)      | [Test](../../../test/unit/ds/map/longlongmap/Test.java) · [Judge](../../../test/judge/ds/map/longlongmap/Judge1.java) |
| `IntPairIntMap`    | (int, int) キー・int 値のハッシュマップ                                          | [PairMapGuide](./PairMapGuide.md)     | [Java](../../../src/lib/ds/map/IntPairIntMap.java)    | —                                                                                                                     |
| `IntPairLongMap`   | (int, int) キー・long 値のハッシュマップ                                         | [PairMapGuide](./PairMapGuide.md)     | [Java](../../../src/lib/ds/map/IntPairLongMap.java)   | —                                                                                                                     |
| `IntTripleIntMap`  | (int, int, int) キー・int 値のハッシュマップ。各成分は -2^20 以上 2^20 - 1 以下  | [TripleMapGuide](./TripleMapGuide.md) | [Java](../../../src/lib/ds/map/IntTripleIntMap.java)  | —                                                                                                                     |
| `IntTripleLongMap` | (int, int, int) キー・long 値のハッシュマップ。各成分は -2^20 以上 2^20 - 1 以下 | [TripleMapGuide](./TripleMapGuide.md) | [Java](../../../src/lib/ds/map/IntTripleLongMap.java) | —                                                                                                                     |

ハッシュ関数の選定に使った測定結果は [ハッシュ関数の検証](./hashFunc.md) にある。
