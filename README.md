# Competitive Programming Java Library

AtCoder などの競技プログラミングで使用することを想定した、Java 製のアルゴリズム・データ構造ライブラリ集です。

- 作者: [nsubaru (AtCoder)](https://atcoder.jp/users/nsubaru)

## 特徴

- **コピペ前提の設計**: 各クラスはパッケージ宣言なし・外部依存なしの単一ファイルで完結しており、コンテスト中にそのまま貼り付けて使えます
- **プリミティブ特化版を併設**: 主要なデータ構造はジェネリクス版に加えて int / long 特化版を提供し、オートボクシングのオーバーヘッドを回避します
- **低レベル最適化**: ビット演算・手動バッファリング・SWAR・`VarHandle` などを活用した高速化を行っています（特に FastIO）
- **ドキュメント完備**: 各モジュールに README を、主要クラスには `docs/` 配下の詳細ガイドを用意しています

## 動作環境

| 項目     | 内容                                                                   |
|--------|----------------------------------------------------------------------|
| JDK    | **Java 24（24.0.2 想定）** ※`DataStructures/FastIO/Java17` のみ Java 17 互換 |
| ビルドツール | 不要（`javac` / `java` のみで動作）                                           |
| 外部依存   | なし（標準ライブラリのみ）                                                        |

## クイックスタート

ビルドツールは使わず、必要なクラスだけを `javac` でコンパイルします。

```powershell
# 例: FastIO24 を使う場合（PowerShell）
javac -encoding UTF-8 -d out "DataStructures\FastIO\Java24\src\FastScanner.java" "DataStructures\FastIO\Java24\src\FastPrinter.java"
javac -encoding UTF-8 -cp out -d out Main.java
java -cp out Main
```

```java
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

各クラスはデフォルトパッケージ前提のため、コピペ利用時は同一ディレクトリ（クラスパス直下）に配置してください。

## ライブラリ一覧

実装状況: 印なし = 実装済み ／ 🚧 = 開発中・一部未実装 ／ 📝 = 未実装（TODO のみ）

### アルゴリズム（[Algorithms/](./Algorithms)）

| モジュール                                                                                         | 内容                                                     |
|-----------------------------------------------------------------------------------------------|--------------------------------------------------------|
| [Conversion](./Algorithms/Conversion)                                                         | 数値・文字列・配列の相互変換などの基本ユーティリティ                             |
| [DP](./Algorithms/DP)                                                                         | 代表的な動的計画法の実装例（Frog・ナップサック・部分和）                         |
| [DivideAndConquer/CentroidDecomposition](./Algorithms/DivideAndConquer/CentroidDecomposition) | 📝 重心分解（TODO）                                          |
| [DivideAndConquer/MoAlgorithm](./Algorithms/DivideAndConquer/MoAlgorithm)                     | 📝 Mo's Algorithm（オフライン区間クエリの平方分割、TODO）                |
| [Graph/Connectivity](./Algorithms/Graph/Connectivity)                                         | 📝 橋・関節点・二重連結成分分解（TODO）                                |
| [Graph/Core](./Algorithms/Graph/Core)                                                         | 有向/無向グラフ（SCC・トポロジカルソート・二部判定）、木（直径）、根付き木（LCA・HLD）       |
| [Graph/Flow/MaxFlow](./Algorithms/Graph/Flow/MaxFlow)                                         | 📝 Dinic法による最大流（TODO）                                  |
| [Graph/Flow/MinCostFlow](./Algorithms/Graph/Flow/MinCostFlow)                                 | 📝 最小費用流（TODO）                                         |
| [Graph/MinimumSpanningTree](./Algorithms/Graph/MinimumSpanningTree)                           | Kruskal・Prim（最小/最大全域木）※Edmonds 📝                      |
| [Graph/ShortestPath](./Algorithms/Graph/ShortestPath)                                         | Dijkstra（最短/最長）・Bellman-Ford（負閉路検出）・Warshall-Floyd     |
| [Graph/TwoSat](./Algorithms/Graph/TwoSat)                                                     | 📝 2-SAT（含意グラフのSCC分解による充足判定、TODO）                      |
| [Math/Combinatorics](./Algorithms/Math/Combinatorics)                                         | 組み合わせ数・順列数・重複組み合わせ・スターリング数・ベル数などの組合せ論ユーティリティ           |
| [Math/Convolution](./Algorithms/Math/Convolution)                                             | 🚧 NTT / FFT / ビット演算系畳み込み（内部変換ロジックが未実装）                |
| [Math/Factorial](./Algorithms/Math/Factorial)                                                 | 階乗・逆元の前計算テーブル、nCr / nPr / カタラン数・ベル数等                   |
| [Math/Geometry](./Algorithms/Math/Geometry)                                                   | 線分/長方形の交差判定、点と図形の位置関係、各種距離計算などの計算幾何ユーティリティ             |
| [Math/LinearAlgebra](./Algorithms/Math/LinearAlgebra)                                         | 📝 ガウスの消去法による連立一次方程式・行列式・ランク（TODO）                     |
| [Math/Matrix](./Algorithms/Math/Matrix)                                                       | int / long 行列演算（加減乗・累乗・mod 演算、in-place 版あり）            |
| [Math/NumberTheory](./Algorithms/Math/NumberTheory)                                           | GCD/LCM・拡張ユークリッド・オイラーのトーシェント関数などの数論ユーティリティ             |
| [Math/NumberUtils](./Algorithms/Math/NumberUtils)                                             | べき乗計算・数値フォーマット変換・数値の性質判定ユーティリティ                        |
| [Math/Permutation](./Algorithms/Math/Permutation)                                             | 辞書順 next/prev permutation（int / long / char / 2次元配列対応） |
| [Math/Polynomial](./Algorithms/Math/Polynomial)                                               | 多項式の加減乗・微分・積分・評価などの多項式ユーティリティ                          |
| [Math/PrimeNumber](./Algorithms/Math/PrimeNumber)                                             | 素数判定・エラトステネスの篩（ビット圧縮）・素因数分解・k 番目の素数                    |
| [Randomized](./Algorithms/Randomized)                                                         | QuickSelect（k 番目の要素を期待 O(n) で取得）                       |
| [Search/BinarySearch](./Algorithms/Search/BinarySearch)                                       | 条件関数・ソート済み配列に対する二分探索（lower/upper bound、`-(挿入位置+1)` 形式） |
| [Search/UnimodalUtils](./Algorithms/Search/UnimodalUtils)                                     | 📝 三分探索・黄金分割探索・ニュートン法（TODO）                            |
| [Sort](./Algorithms/Sort)                                                                     | 学習用ソートアルゴリズム11種 ※FordJohnson 📝                        |
| [String/Levenshtein](./Algorithms/String/Levenshtein)                                         | 編集距離（標準 DP・距離制限付き banded DP）※Myers / Wu 📝             |
| [String/Palindrome](./Algorithms/String/Palindrome)                                           | Manacher（全回文検出 O(n)）・素朴な回文判定/生成 ※Eertree 📝            |
| [String/StringSearch](./Algorithms/String/StringSearch)                                       | Z-Algorithm ※KMP / BM / RollingHash / AhoCorasick 📝   |

### データ構造（[DataStructures/](./DataStructures)）

| モジュール                                                           | 内容                                                                                                       |
|-----------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| [ArrayUtils](./DataStructures/ArrayUtils)                       | 高速回転・区間和・LIS・スライディングウィンドウ等を備えた不変長配列（1D / 2D）                                                             |
| [AVLTree](./DataStructures/AVLTree)                             | AVL 木による順序付き Set / Multiset（順位検索・近傍検索対応、int / long 特化版あり）                                                |
| [BinaryIndexedTree](./DataStructures/BinaryIndexedTree)         | BIT（点更新区間和・2D・区間加算区間和、BIT 上の二分探索付き）                                                                      |
| [BinarySearchTree](./DataStructures/BinarySearchTree)           | 基本 BST・Treap（順序統計付き乱択平衡 BST）※赤黒木・B木・vEB木 📝                                                              |
| [CartesianTree](./DataStructures/CartesianTree)                 | 📝 デカルト木（TODO）                                                                                           |
| [EulerTour](./DataStructures/EulerTour)                         | 📝 オイラーツアー（TODO）                                                                                         |
| [FastIO/Java17](./DataStructures/FastIO/Java17)                 | 高速入出力（**Java 17 互換**）・対話問題用 InteractiveScanner                                                           |
| [FastIO/Java24](./DataStructures/FastIO/Java24)                 | 高速入出力（Java 24 最適化、SWAR・`VarHandle` 使用）+ [ベンチマーク環境](./DataStructures/FastIO/Java24/Benchmark)             |
| [HashMap](./DataStructures/HashMap)                             | オープンアドレス法のプリミティブ特化ハッシュマップ（O(1) clear、ペア/トリプルキー対応）                                                        |
| [Int128](./DataStructures/Int128)                               | 128bit 符号付き整数（四則演算・文字列変換・比較）                                                                             |
| [LiChaoTree](./DataStructures/LiChaoTree)                       | 📝 Li Chao Tree（直線群へのCHT、TODO）                                                                           |
| [ModNumbers](./DataStructures/ModNumbers)                       | 剰余演算ラッパー ModInt / ModLong（逆元・冪乗付き）                                                                       |
| [PersistentSegmentTree](./DataStructures/PersistentSegmentTree) | 📝 永続セグメント木（TODO）                                                                                        |
| [PersistentUnionFind](./DataStructures/PersistentUnionFind)     | 📝 永続Union-Find（TODO）                                                                                    |
| [PriorityQueue](./DataStructures/PriorityQueue)                 | 遅延ヒープ構築による高速優先度キュー（汎用 / int / long / インデックス付き）                                                           |
| [RingBuffer](./DataStructures/RingBuffer)                       | 容量を2の冪に正規化した高速リングバッファ（汎用 / int / long）                                                                   |
| [SegmentTree](./DataStructures/SegmentTree)                     | セグメント木・遅延評価セグメント木・区間アフィン変換+二乗和（各 int / long 特化版あり）                                                       |
| [SegmentTree2D](./DataStructures/SegmentTree2D)                 | 📝 2次元セグメント木（TODO）                                                                                       |
| [SkipList](./DataStructures/SkipList)                           | 📝 スキップリスト（TODO）                                                                                         |
| [SparseTable](./DataStructures/SparseTable)                     | 📝 Sparse Table（TODO）                                                                                    |
| [Trie](./DataStructures/Trie)                                   | Trie / SuffixTrie / RadixTrie / PatriciaTrie / 三分探索木 / DoubleArrayTrie / SuffixArray ※SuffixAutomaton 📝 |
| [UnionFind](./DataStructures/UnionFind)                         | 経路圧縮 + rank 併用の素集合データ構造（グループ数・サイズ・辺数の管理付き）                                                               |
| [WaveletTree](./DataStructures/WaveletTree)                     | 📝 Wavelet Tree（TODO）                                                                                    |

## ベンチマーク

FastIO の性能計測・比較用の環境を [DataStructures/FastIO/Java24/Benchmark/](./DataStructures/FastIO/Java24/Benchmark)
に用意しています。
AtCoder 想定の JVM オプションでの繰り返し計測、CSV 出力、JIT / GC / JFR プロファイルの取得に対応しています。
使い方は同フォルダの [README](./DataStructures/FastIO/Java24/Benchmark/README.md) を参照してください。

## リポジトリ構成

```
.
├── Algorithms/          # アルゴリズム（モジュールごとにフォルダ分割）
│   └── <Module>/
│       ├── src/         # 実装（Example.java は動作確認用）
│       ├── docs/        # クラス別の詳細ガイド
│       └── README.md    # モジュール概要
├── DataStructures/      # データ構造（構成は同上）
├── README_TEMPLATE.md   # モジュール README のテンプレート
├── GuideTemplate.md     # docs ガイドのテンプレート
└── qodana.yaml          # 静的解析（Qodana）の設定
```

- `src/` 内の `Example.java` は使用例、`Check*.java` はオンラインジャッジ（[Library Checker](https://judge.yosupo.jp/) 等）への提出による検証用です

## 開発ポリシー

- **競技用途を優先**: 実行速度を最優先とし、バリデーションは最低限に留めています
- **コーディング規約**: インデントはタブ、定数は UPPER_SNAKE_CASE、非圧縮クラスには JavaDoc を記述（詳細は [.github/copilot-instructions.md](./.github/copilot-instructions.md)）
- **静的解析**: [Qodana](https://www.jetbrains.com/qodana/)（qodana-jvm 2025.2 / JDK 24）を使用
- **Issue / PR**: [テンプレート](./.github)を用意しています

## 免責事項

本ライブラリはバグを含む可能性があり、完全な動作は保証しません。あくまでライブラリ作成のヒントとしてご利用ください。
