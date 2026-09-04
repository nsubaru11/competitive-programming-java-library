# ライブラリ現状監査レポート

監査基準日: 2026-09-05

`src/lib/`、`src/patterns/`、`docs/`、`test/verify/`をファイル単位で突き合わせた結果です。
ここでいう「実装済み」は、TODO雛形ではなく処理本体を持つという静的な分類であり、すべての入力に対する正当性を保証するものではありません。

## 集計

| モジュール   | Javaファイル | 実装本体あり | 一部未実装 | TODOのみ |
|--------------|-------------:|-------------:|-----------:|---------:|
| `lib.ds`     |           97 |           74 |          1 |       22 |
| `lib.graph`  |           31 |           16 |          1 |       14 |
| `lib.io`     |            6 |            6 |          0 |        0 |
| `lib.math`   |           27 |           15 |          2 |       10 |
| `lib.search` |            6 |            3 |          0 |        3 |
| `lib.sort`   |           14 |           11 |          0 |        3 |
| `lib.string` |           16 |            5 |          0 |       11 |
| `lib.util`   |           21 |           20 |          1 |        0 |
| **合計**     |      **218** |      **150** |      **5** |   **63** |

その他に、コピー・改変用の`src/patterns/`が2分野5ファイル、`docs/`のMarkdownが本レポートを含め137ファイル、`test/verify/`のJava検証ソースが94ファイルあります。
ビルドツールと自動テストランナーはなく、`javac`と各mainクラスの個別実行が前提です。

## 未完成または制約を確認すべき公開API

| クラス                                                       | 状態                                                                                  | 影響                                                                               |
|--------------------------------------------------------------|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [`Convolution`](../src/lib/math/polynomial/Convolution.java) | NTT / FFTとGarner復元が未実装。AND / OR / XOR / GCD / LCM畳み込みと各種変換は利用可能 | `convolveNtt`、`convolveArbitraryMod`、`convolveFft`は正しい畳み込み結果を返さない |
| [`DivideConquer`](../src/lib/util/DivideConquer.java)        | `inversionCount`のループ本体が空                                                      | 入力にかかわらず`0`を返す                                                          |
| [`IntTreap`](../src/lib/ds/set/IntTreap.java)                | `split*`は例外、`merge`はキー範囲が交差するとBST性を壊す                              | 一般のsplit/merge用途には利用不可                                                  |

TODOのみのクラスは公開メソッドを持たない雛形であり、APIとしては利用できません。
雛形がコンパイルに成功することとアルゴリズムが利用可能であることを区別してください。

また、Java 17/24両版の`FastScanner(InputStream)`は、`InputStream.available() + 64`バイトの配列へ1回だけ`read`します。
Javaの`available()`は全入力長を保証せず、`read`もshort readを許すため、一般のネットワーク・パイプ・カスタムストリームでは有効な入力が途中で切れる可能性があります。
提出環境の標準入力・ファイル入力に最適化した前提をガイドへ明記しましたが、将来はEOFまで読む実装と性能比較する必要があります。

## TODOのみのクラス

### データ構造（22）

既存のTODO:

- `CartesianTree`、`LiChaoTree`、`SparseTable`、`WaveletTree`
- `PersistentSegmentTree`、`SegmentTree2D`
- `BTree`、`RedBlackTree`、`SkipList`、`VanEmdeBoasTree`
- `CompactTrie`、`PersistentUnionFind`

今回、未作成だった定番候補として追加したTODO:

- `DisjointSparseTable`、`IntervalSet`、`SlidingWindowAggregation`、`WaveletMatrix`
- `SegmentTreeBeats`、`ImplicitTreap`、`BinaryTrie`、`LinkCutTree`
- `RollbackUnionFind`、`WeightedUnionFind`

### グラフ（14）

既存のTODO:

- `CentroidDecomposition`、`Dinic`、`Edmonds`
- `MinCostFlow`、`TwoSat`、`EulerTour`

今回追加したTODO:

- `Johnson`、`EulerianTrail`、`RerootingDP`
- `BipartiteMatching`、`GeneralMatching`、`StoerWagner`、`DominatorTree`
- `OfflineDynamicConnectivity`

### 数学（10）

既存のTODO:

- `LinearAlgebra`

今回追加したTODO:

- `ChineseRemainderTheorem`、`FloorSum`、`DiscreteLogarithm`、`ModularSquareRoot`
- `PollardRho`、`BerlekampMassey`、`Kitamasa`
- `ConvexHull`、`FormalPowerSeries`

### 探索（3）

- `TernarySearch`、`GoldenSectionSearch`、`Newton`

### ソート（3）

- 既存: `FordJohnson`
- 今回追加: `CountingSort`、`RadixSort`

### 文字列（11）

既存のTODO:

- `Myers`、`Wu`
- `KMP`、`BM`、`RollingHash`、`AhoCorasick`
- `Eertree`、`SuffixAutomaton`

今回追加したTODO:

- `LongestCommonSubsequence`、`LyndonFactorization`、`MinimumRotation`

今回追加した33クラスは、競技プログラミング用の再利用APIとして頻出し、既存クラスで同じ責務を満たせないものに限定しています。
問題固有DP、ヒューリスティック、Java標準ライブラリの単純なラッパーは対象外です。

## 実装済み領域の概況

- データ構造は配列ラッパー・累積和、Fenwick Tree、AVL Tree、基本/Treap、プリミティブHashMap、優先度キュー、1次元Segment Tree、Trie群、Union-Findが中心です。実装ファイル数は最も多い一方、検証はクラス間で偏りがあります。
- グラフは固定長グラフ表現、BFS・0-1 BFS・SCC・二部判定、Dijkstra、Bellman-Ford、Warshall-Floyd、Kruskal、Prim、木・根付き木を利用できます。`Connectivity.lowLink`は橋・関節点を検出できます。フロー、2-SAT、二重連結成分分解は未実装です。
- 入出力はJava 24版とJava 17互換版があり、いずれも`FastScanner`、`FastPrinter`、`InteractiveScanner`を持ちます。`Unsafe`を利用するため、JDK更新時の警告と互換性確認が必要です。
- 数学は基本整数演算、組合せ、素数・素因数、幾何ユーティリティ、行列の基本演算、数値型、多項式の基本演算があります。部分集合・上位集合・約数・倍数変換、FWHT、AND / OR / XOR / GCD / LCM畳み込みは利用できますが、NTT・FFT、掃き出し法は未実装です。
- 探索は二分探索とQuickSelectのみが利用可能です。単峰探索・Newton法はTODOです。
- ソートは実装・比較用の基本アルゴリズム11種があり、専用の検証ソースはありません。実用上の単一配列ソートではまず`Arrays.sort`を選択し、将来は連動ソートなど標準ライブラリだけでは表現しにくい競技向けユーティリティを追加します。
- 文字列はLevenshtein DP、Z-Algorithm、Manacher、素朴な回文処理、SuffixArrayが実装済みです。標準的なKMPさえTODOであり、完成度の偏りが大きい領域です。
- utilは配列処理、変換、数字・書式、順列、Mo's Algorithm、プリミティブ関数型が中心です。`DivideConquer`だけは未完成です。

## ドキュメント監査

- 相対Markdownリンクの存在確認では、ルートREADMEを含む138ファイルでリンク切れは0件です。
- ルートREADMEの「ドキュメント完備」は実態より強かったため、段階的整備という表現へ変更しました。
- `ZAlgorithm`を未実装とする誤記、存在しない`BinarySearchTreeTemp`と`PalindromeUtils`への言及を修正しました。
- `GoldenSectionSearch`、`Newton`、`AhoCorasick`、`Eertree`、`SuffixAutomaton`、`IntDoubleEndedPriorityQueue`の索引漏れを補いました。
- Javaコードだけが入っていた`BTreeGuide` / `RedBlackTreeGuide`、空だった`SparseTableGuide` / `ZAlgorithmGuide`を正しいMarkdownガイドへ置き換えました。
- `Transform`が公開APIとなったため、Polynomial READMEとConvolution設計書へ公開範囲・配列長・実装状況を追加しました。
- 旧API名を掲載していたConvolution設計書を、現行の`convolve*` APIとオーバーロード群に同期しました。
- 多項式畳み込みの論理的な返却長、ビット演算畳み込みの定義域長、`Transform`のコピー版とin-place版の契約をPolynomial READMEとConvolution設計書へ反映しました。
- FWHTとXOR畳み込みの実装完了に合わせ、利用可能な畳み込み・変換の一覧、逆変換のmod条件、FWHTの処理概要を更新しました。
- 約数・倍数ゼータ／メビウス変換とGCD / LCM畳み込みの実装完了に合わせ、0-indexed APIの添字規約、LCM畳み込みの返却定義域、公式検証先を追加しました。
- 空だったソート説明8件を補い、基本ソートを実装・比較用と位置付けました。単一配列の実戦用途では`Arrays.sort`を優先し、連動ソートなどの競技向けユーティリティを今後追加します。
- 詳細ガイドのメソッド表は、公開API追加時にシグネチャ単位で継続して突き合わせる必要があります。

## 検証状況

`test/verify/`には94ファイルあります。
内訳はds 30、graph 14、io 19、math 14、search 2、string 5、util 10で、sortは0です。
検証コードはオンラインジャッジ提出用の`Check*.java`、実行例、ベンチマークが混在しています。クラス参照数だけでは実際のカバレッジを表せないため、この監査では数値化していません。

特に次の領域は優先して検証を増やす必要があります。

- ソート11クラス。基本ソートは教育・比較用として維持し、連動ソートなどの新規ユーティリティには用途に対応した決定的テストを必須とする
- `Convolution` / `Transform`。AND / OR / XOR / GCD / LCMを含む素朴解とのランダム照合、変換・逆変換の性質、コピー版とin-place版の結果一致を検証する
- `RootedTree` / `Tree`、線形代数、`Fraction` / `ModInt` / `ModLong`
- 多くの配列派生クラス、2D/3D累積和、Range/2D BIT
- Trie派生、`SuffixArray`、`IntDoubleEndedPriorityQueue`
- プリミティブ関数型インターフェースはコンパイル利用例でシグネチャを固定する

オンラインジャッジ提出用の`Check*.java`、実行例、ベンチマークが混在しており、全件を一括実行して成否を判定する仕組みはありません。
少なくともコンパイル検証と、決定的な小規模テストを分離したランナーの追加が望まれます。

監査時には次を確認しました。

- `src/`全体を`javac --release 24`でコンパイル: 成功
- `src/lib/io/compat17/*.java`を`javac --release 17`でコンパイル: 成功（`Unsafe`警告あり）
- `src/`と`test/`の全Javaソースを`--release 24`で同時コンパイル: 成功
- 入力不要でassertionを持つ代表的な11実行クラス（Fenwick、HashMap 3種、PriorityQueue、FactorialTable、FactorUtils、LevenshteinDP、Conversions、FormatUtils、MoAlgorithm）: すべて終了コード0

## 推奨優先順位

1. `Convolution`は利用可能なビット演算・GCD / LCM畳み込みの契約を維持しつつ、NTT、Garner復元、FFTの順に実装する。
2. `DivideConquer`を実装するか公開APIから隔離する。
3. `FastScanner`の一括読み込みをEOF/short read対応にし、現行高速パスとベンチマーク比較する。
4. 頻出度が高い`KMP`、`Dinic`、`TwoSat`、`SparseTable`、`WeightedUnionFind`を実装する。
5. `IntTreap`のsplit/mergeを完成させ、ランダムテストでAVL/TreeSetと照合する。
6. 実装済みだが未検証のクラスへ小規模決定的テストを追加し、詳細ガイドのメソッド表をソースからシグネチャ単位で機械比較する保守チェックを追加する。
