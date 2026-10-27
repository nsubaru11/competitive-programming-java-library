# Edit Distance

## 概要

編集距離（レーベンシュタイン距離）は、2つの文字列間の類似性を測定するアルゴリズムである。
一方の文字列を他方に変換するために必要な最小操作回数（挿入、削除、置換）を計算する。

## クラス一覧

| クラス          | 用途                                                               | Guide                                         | ソース                                                         | Example / 検証                                                                |
|-----------------|--------------------------------------------------------------------|-----------------------------------------------|----------------------------------------------------------------|-------------------------------------------------------------------------------|
| `LevenshteinDP` | 2つの文字列、文字配列、または整数配列間の編集距離を計算する        | [LevenshteinDPGuide](./LevenshteinDPGuide.md) | [Java](../../../src/lib/string/approximate/LevenshteinDP.java) | [Test.java](../../../test/example/string/approximate/levenshteindp/Test.java) |
| `Myers`         | Myers のビットパラレル法による編集距離計算（$\mathcal{O}(n*m/w)$） | —                                             | [Java](../../../src/lib/string/approximate/Myers.java)         | —                                                                             |
| `Wu`            | Wu–Manber 法（$\mathcal{O}(NP)$アルゴリズム）による編集距離計算    | —                                             | [Java](../../../src/lib/string/approximate/Wu.java)            | —                                                                             |

## 実装クラス

### [LevenshteinDP](../../../src/lib/string/approximate/LevenshteinDP.java)

- **用途**: 2つの文字列、文字配列、または整数配列間の編集距離を計算する
- **特徴**:
	- 複数のデータ型（String、char[]、int[]）に対応
	- 最大距離を指定して早期終了する最適化バージョンを提供
	- 空間効率のために2行のDPテーブルのみを使用

### [Myers](../../../src/lib/string/approximate/Myers.java)（未実装）

- **用途**: Myers のビットパラレル法による編集距離計算（$\mathcal{O}(n*m/w)$）
- LevenshteinDP と同形の API を提供する予定である

### [Wu](../../../src/lib/string/approximate/Wu.java)（未実装）

- **用途**: Wu–Manber 法（$\mathcal{O}(NP)$アルゴリズム）による編集距離計算
- LevenshteinDP と同形の API を提供する予定である
