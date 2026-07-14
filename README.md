# Competitive Programming Java Library

AtCoder などの競技プログラミングで使用することを想定した、Java 製のアルゴリズム・データ構造ライブラリ集です。

- 作者: [nsubaru (AtCoder)](https://atcoder.jp/users/nsubaru)

## 特徴

- **パッケージ化されたAPI**: `lib.*` を単一ソースツリーとして管理し、IDEの補完・定義ジャンプ・安全なリファクタリングをライブラリ全域で利用できます
- **提出時の自動バンドル**: AtCoder側のランナーが `import lib.*` と推移的依存を単一の `Main.java` へ展開します
- **プリミティブ特化版を併設**: 主要なデータ構造はジェネリクス版に加えて int / long 特化版を提供し、オートボクシングのオーバーヘッドを回避します
- **低レベル最適化**: ビット演算・手動バッファリング・SWAR・`VarHandle` などを活用した高速化を行っています（特に FastIO）
- **ドキュメント完備**: 各モジュールに README を、主要クラスには `docs/` 配下の詳細ガイドを用意しています

## 動作環境

| 項目     | 内容                                                      |
|--------|---------------------------------------------------------|
| JDK    | **Java 24（24.0.2 想定）** ※`lib.io.compat17` のみ Java 17 互換 |
| ビルドツール | 不要（`javac` / `java` のみで動作）                              |
| 外部依存   | なし（標準ライブラリのみ）                                           |

## クイックスタート

ビルドツールは使わず、`src/` をソースルートとして必要なクラスをコンパイルします。

```powershell
# 例: FastIOを使う場合（PowerShell）
javac --release 24 -encoding UTF-8 -d out "src\lib\io\FastScanner.java" "src\lib\io\FastPrinter.java"
javac --release 24 -encoding UTF-8 -cp out -d out Main.java
java -cp out Main
```

```java
import lib.io.FastPrinter;
import lib.io.FastScanner;

public class Main {

	private static void solve(final FastScanner sc, final FastPrinter out) {
		int n = sc.nextInt();
		int[] a = sc.nextInt(n);
		long sum = 0;
		for (int x : a) sum += x;
		out.println(sum);
	}

	public static void main(String[] args) {
		try (final FastPrinter out = new FastPrinter()) {
			final FastScanner sc = new FastScanner();
			solve(sc, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
```

AtCoderへはこのimport版を直接提出できません。`run` / `test` / `localtest` / `tomain` / `submit` は、必ずライブラリをバンドルした提出形へ変換してから実行します。バンドラを使えない場合は、`src/lib/` の依存元ファイルを確認して従来どおり手動展開できます。

## ライブラリ一覧

実装状況: 印なし = 実装済み ／ 🚧 = 開発中・一部未実装 ／ 📝 = 未実装（TODO のみ）

### アルゴリズム（`lib.graph` / `lib.math` / `lib.search` / `lib.sort` / `lib.string` / `lib.util`）

| モジュール                                                             | 内容                                                     |
|-------------------------------------------------------------------|--------------------------------------------------------|
| [Conversion](./docs/util/Conversion)                              | 数値・文字列・配列の相互変換などの基本ユーティリティ                             |
| [DP](./src/patterns/dp)                                           | 代表的な動的計画法の実装例（Frog・ナップサック・部分和）                         |
| [DivideAndConquer/MoAlgorithm](./docs/util/MoAlgorithm)           | Mo's Algorithm（オフライン区間クエリの平方分割、add/remove関数によるジグザグ走査）  |
| [Graph/CentroidDecomposition](./docs/graph/CentroidDecomposition) | 📝 重心分解（TODO）                                          |
| [Graph/Connectivity](./docs/graph/Connectivity)                   | 📝 橋・関節点・二重連結成分分解（TODO）                                |
| [Graph/Core](./src/lib/graph/DirectedGraph.java)                  | 有向/無向グラフ（SCC・トポロジカルソート・二部判定）、木（直径）、根付き木（LCA・HLD）       |
| [Graph/Flow/MaxFlow](./docs/graph/MaxFlow)                        | 📝 Dinic法による最大流（TODO）                                  |
| [Graph/Flow/MinCostFlow](./docs/graph/MinCostFlow)                | 📝 最小費用流（TODO）                                         |
| [Graph/MinimumSpanningTree](./docs/graph/MinimumSpanningTree)     | Kruskal・Prim（最小/最大全域木）※Edmonds 📝                      |
| [Graph/ShortestPath](./docs/graph/ShortestPath)                   | Dijkstra（最短/最長）・Bellman-Ford（負閉路検出）・Warshall-Floyd     |
| [Graph/TwoSat](./docs/graph/TwoSat)                               | 📝 2-SAT（含意グラフのSCC分解による充足判定、TODO）                      |
| [Math/Combinatorics](./docs/math/Combinatorics)                   | 組み合わせ数・順列数・重複組み合わせ・スターリング数・ベル数などの組合せ論ユーティリティ           |
| [Math/Convolution](./docs/math/Convolution)                       | 🚧 NTT / FFT / ビット演算系畳み込み（内部変換ロジックが未実装）                |
| [Math/Factorial](./docs/math/Factorial)                           | 階乗・逆元の前計算テーブル、nCr / nPr / カタラン数・ベル数等                   |
| [Math/Geometry](./docs/math/Geometry)                             | 線分/長方形の交差判定、点と図形の位置関係、各種距離計算などの計算幾何ユーティリティ             |
| [Math/LinearAlgebra](./docs/math/LinearAlgebra)                   | 📝 ガウスの消去法による連立一次方程式・行列式・ランク（TODO）                     |
| [Math/Matrix](./src/lib/math/IntMatrixUtils.java)                 | int / long 行列演算（加減乗・累乗・mod 演算、in-place 版あり）            |
| [Math/NumberTheory](./docs/math/NumberTheory)                     | GCD/LCM・拡張ユークリッド・オイラーのトーシェント関数などの数論ユーティリティ             |
| [Math/NumberUtils](./docs/math/NumberUtils)                       | べき乗計算・数値フォーマット変換・数値の性質判定ユーティリティ                        |
| [Math/Permutation](./docs/math/Permutation)                       | 辞書順 next/prev permutation（int / long / char / 2次元配列対応） |
| [Math/Polynomial](./docs/math/Polynomial)                         | 多項式の加減乗・微分・積分・評価などの多項式ユーティリティ                          |
| [Math/PrimeNumber](./docs/math/PrimeNumber)                       | 素数判定・エラトステネスの篩（ビット圧縮）・素因数分解・k 番目の素数                    |
| [Randomized](./src/lib/search/QuickSelect.java)                   | QuickSelect（k 番目の要素を期待 O(n) で取得）                       |
| [Search/BinarySearch](./docs/search/BinarySearch)                 | 条件関数・ソート済み配列に対する二分探索（lower/upper bound、`-(挿入位置+1)` 形式） |
| [Search/UnimodalUtils](./docs/search/UnimodalUtils)               | 📝 三分探索・黄金分割探索・ニュートン法（TODO）                            |
| [Sort](./docs/sort)                                               | 学習用ソートアルゴリズム11種 ※FordJohnson 📝                        |
| [String/Levenshtein](./docs/string/Levenshtein)                   | 編集距離（標準 DP・距離制限付き banded DP）※Myers / Wu 📝             |
| [String/Palindrome](./docs/string/Palindrome)                     | Manacher（全回文検出 O(n)）・素朴な回文判定/生成 ※Eertree 📝            |
| [String/StringSearch](./docs/string/StringSearch)                 | Z-Algorithm ※KMP / BM / RollingHash / AhoCorasick 📝   |

### データ構造（`lib.ds`）

| モジュール                                                    | 内容                                                                                                       |
|----------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| [Primitive Array Utilities](docs/ds/Arrays)              | プリミティブ配列インターフェース、累積和、循環配列、deque、2D配列、座標圧縮、LIS・固定幅窓・部分集合アルゴリズム                                            |
| [AVLTree](./docs/ds/AVLTree)                             | AVL 木による順序付き Set / Multiset（順位検索・近傍検索対応、int / long 特化版あり）                                                |
| [BinaryIndexedTree](./docs/ds/BinaryIndexedTree)         | BIT（点更新区間和・2D・区間加算区間和、BIT 上の二分探索付き）                                                                      |
| [BinarySearchTree](./docs/ds/BinarySearchTree)           | 基本 BST・Treap（順序統計付き乱択平衡 BST）※赤黒木・B木・vEB木 📝                                                              |
| [CartesianTree](./src/lib/ds/CartesianTree.java)         | 📝 デカルト木（TODO）                                                                                           |
| [EulerTour](./docs/ds/EulerTour)                         | 📝 オイラーツアー（TODO）                                                                                         |
| [FastIO/Java17](./docs/io/Java17)                        | 高速入出力（**Java 17 互換**）・対話問題用 InteractiveScanner                                                           |
| [FastIO/Java24](./docs/io/Java24)                        | 高速入出力（Java 24 最適化、SWAR・`VarHandle` 使用）+ [ベンチマーク環境](./docs/io/Java24/Benchmark)                           |
| [HashMap](./docs/ds/HashMap)                             | オープンアドレス法のプリミティブ特化ハッシュマップ（O(1) clear、ペア/トリプルキー対応）                                                        |
| [Int128](./src/lib/ds/Int128.java)                       | 128bit 符号付き整数（四則演算・文字列変換・比較）                                                                             |
| [LiChaoTree](./docs/ds/LiChaoTree)                       | 📝 Li Chao Tree（直線群へのCHT、TODO）                                                                           |
| [ModNumbers](./docs/math/ModNumbers)                     | 剰余演算ラッパー ModInt / ModLong（逆元・冪乗付き）                                                                       |
| [PersistentSegmentTree](./docs/ds/PersistentSegmentTree) | 📝 永続セグメント木（TODO）                                                                                        |
| [PersistentUnionFind](./docs/ds/PersistentUnionFind)     | 📝 永続Union-Find（TODO）                                                                                    |
| [PriorityQueue](./docs/ds/PriorityQueue)                 | 遅延ヒープ構築による高速優先度キュー（汎用 / int / long / インデックス付き）                                                           |
| [RingBuffer](./docs/ds/RingBuffer)                       | 容量を2の冪に正規化した高速リングバッファ（汎用 / int / long）                                                                   |
| [SegmentTree](./docs/ds/SegmentTree)                     | セグメント木・遅延評価セグメント木・区間アフィン変換+二乗和（各 int / long 特化版あり）                                                       |
| [SegmentTree2D](./docs/ds/SegmentTree2D)                 | 📝 2次元セグメント木（TODO）                                                                                       |
| [SkipList](./docs/ds/SkipList)                           | 📝 スキップリスト（TODO）                                                                                         |
| [SparseTable](./docs/ds/SparseTable)                     | 📝 Sparse Table（TODO）                                                                                    |
| [Trie](./docs/ds/Trie)                                   | Trie / SuffixTrie / RadixTrie / PatriciaTrie / 三分探索木 / DoubleArrayTrie / SuffixArray ※SuffixAutomaton 📝 |
| [UnionFind](./docs/ds/UnionFind)                         | 経路圧縮 + rank 併用の素集合データ構造（グループ数・サイズ・辺数の管理付き）                                                               |
| [WaveletTree](./docs/ds/WaveletTree)                     | 📝 Wavelet Tree（TODO）                                                                                    |

## ベンチマーク

FastIO の性能計測・比較用の環境を [docs/io/Java24/Benchmark/](./docs/io/Java24/Benchmark)
に用意しています。
AtCoder 想定の JVM オプションでの繰り返し計測、CSV 出力、JIT / GC / JFR プロファイルの取得に対応しています。
使い方は同フォルダの [README](./docs/io/Java24/Benchmark/README.md) を参照してください。

## リポジトリ構成

```
.
├── src/
│   ├── lib/             # import対象の再利用API
│   │   ├── ds/
│   │   ├── graph/
│   │   ├── io/
│   │   ├── math/
│   │   ├── search/
│   │   ├── sort/
│   │   ├── string/
│   │   └── util/
│   └── patterns/        # 読んで写経・改変する実装パターン
├── test/verify/         # Example・Check・ベンチマークドライバ
├── docs/                # モジュールREADME・詳細ガイド・ベンチマークランナー
├── README_TEMPLATE.md   # モジュール README のテンプレート
├── GuideTemplate.md     # docs ガイドのテンプレート
└── qodana.yaml          # 静的解析（Qodana）の設定
```

- `src/lib` と `src/patterns` は通常のSources Root、`test` はTest Sources Rootとして扱います
- `test/verify` の `Example.java` は使用例、`Check*.java` はオンラインジャッジ（[Library Checker](https://judge.yosupo.jp/) 等）への提出による検証用です

## 開発ポリシー

- **競技用途を優先**: 実行速度を最優先とし、バリデーションは最低限に留めています
- **コーディング規約**: インデントはタブ、定数は UPPER_SNAKE_CASE、非圧縮クラスには JavaDoc を記述（詳細は [.github/copilot-instructions.md](./.github/copilot-instructions.md)）
- **静的解析**: [Qodana](https://www.jetbrains.com/qodana/)（qodana-jvm 2025.2 / JDK 24）を使用
- **Issue / PR**: [テンプレート](./.github)を用意しています

## 免責事項

本ライブラリはバグを含む可能性があり、完全な動作は保証しません。あくまでライブラリ作成のヒントとしてご利用ください。
