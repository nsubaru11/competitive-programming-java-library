# Competitive Programming Java Library

AtCoder などの競技プログラミングで使用することを想定した、Java 製のアルゴリズム・データ構造ライブラリ集です。

- 作者: [nsubaru (AtCoder)](https://atcoder.jp/users/nsubaru)

## 特徴

- **コピペ前提の設計**: 各クラスはパッケージ宣言なし・外部依存なしの単一ファイルで完結しており、コンテスト中にそのまま貼り付けて使えます
- **プリミティブ特化版を併設**: 主要なデータ構造はジェネリクス版に加えて int / long 特化版を提供し、オートボクシングのオーバーヘッドを回避します
- **低レベル最適化**: ビット演算・手動バッファリング・SWAR・`VarHandle` などを活用した高速化を行っています（特に FastIO）
- **ドキュメント完備**: 各モジュールに README を、主要クラスには `docs/` 配下の詳細ガイドを用意しています

## 動作環境

| 項目 | 内容 |
|---|---|
| JDK | **Java 24（24.0.2 想定）** ※`Data Structures/FastIO17` のみ Java 17 互換 |
| ビルドツール | 不要（`javac` / `java` のみで動作） |
| 外部依存 | なし（標準ライブラリのみ） |

## クイックスタート

ビルドツールは使わず、必要なクラスだけを `javac` でコンパイルします。

```powershell
# 例: FastIO24 を使う場合（PowerShell）
javac -encoding UTF-8 -d out "Data Structures\FastIO24\src\FastScanner.java" "Data Structures\FastIO24\src\FastPrinter.java"
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

| モジュール | 内容 |
|---|---|
| [Basic](./Algorithms/Basic) | 数値・文字列・配列の相互変換、累乗・GCD/LCM・約数列挙などの基本ユーティリティ |
| [BinarySearch](./Algorithms/BinarySearch) | 条件関数・ソート済み配列に対する二分探索（lower/upper bound、`-(挿入位置+1)` 形式） |
| [Convolution](./Algorithms/Convolution) | 🚧 NTT / FFT / ビット演算系畳み込み（内部変換ロジックが未実装） |
| [DP](./Algorithms/DP) | 代表的な動的計画法の実装例（Frog・ナップサック・部分和） |
| [Factorial](./Algorithms/Factorial) | 階乗・逆元の前計算テーブル、nCr / nPr / カタラン数・ベル数等 |
| [Graph/ShortestPath](./Algorithms/Graph/ShortestPath) | Dijkstra（最短/最長）・Bellman-Ford（負閉路検出）・Warshall-Floyd |
| [Graph/SimpleGraph](./Algorithms/Graph/SimpleGraph) | 有向/無向グラフ（SCC・トポロジカルソート・二部判定）、木（直径）、根付き木（LCA・HLD） |
| [Levenshtein](./Algorithms/Levenshtein) | 編集距離（標準 DP・距離制限付き banded DP）※Myers / Wu 📝 |
| [MathFunctions](./Algorithms/MathFunctions) | 組合せ・数論・幾何・多項式・冪乗・数値判定などの数学ユーティリティ群 |
| [Matrix](./Algorithms/Matrix) | int / long 行列演算（加減乗・累乗・mod 演算、in-place 版あり） |
| [MinimumSpanningTree](./Algorithms/MinimumSpanningTree) | Kruskal・Prim（最小/最大全域木）※Edmonds 📝 |
| [Palindrome](./Algorithms/Palindrome) | Manacher（全回文検出 O(n)）・素朴な回文判定/生成 |
| [Permutation](./Algorithms/Permutation) | 辞書順 next/prev permutation（int / long / char / 2次元配列対応） |
| [PrimeNumber](./Algorithms/PrimeNumber) | 素数判定・エラトステネスの篩（ビット圧縮）・素因数分解・k 番目の素数 |
| [Randomized](./Algorithms/Randomized) | QuickSelect（k 番目の要素を期待 O(n) で取得） |
| [Sort](./Algorithms/Sort) | 学習用ソートアルゴリズム11種 ※FordJohnson 📝 |
| [StringSearch](./Algorithms/StringSearch) | Z-Algorithm ※KMP / BM / RollingHash 📝 |
| [UnimodalUtils](./Algorithms/UnimodalUtils) | 📝 三分探索・黄金分割探索・ニュートン法（TODO） |

### データ構造（[Data Structures/](./Data%20Structures)）

| モジュール | 内容 |
|---|---|
| [ArrayUtils](./Data%20Structures/ArrayUtils) | 高速回転・区間和・LIS・スライディングウィンドウ等を備えた不変長配列（1D / 2D） |
| [AVLTree](./Data%20Structures/AVLTree) | AVL 木による順序付き Set / Multiset（順位検索・近傍検索対応、int / long 特化版あり） |
| [BinaryIndexedTree](./Data%20Structures/BinaryIndexedTree) | BIT（点更新区間和・2D・区間加算区間和、BIT 上の二分探索付き） |
| [BinarySearchTree](./Data%20Structures/BinarySearchTree) | 基本 BST・Treap（順序統計付き乱択平衡 BST）※赤黒木・B木・vEB木 📝 |
| [CartesianTree](./Data%20Structures/CartesianTree) | 📝 デカルト木（TODO） |
| [FastIO17](./Data%20Structures/FastIO17) | 高速入出力（**Java 17 互換**）・対話問題用 InteractiveScanner |
| [FastIO24](./Data%20Structures/FastIO24) | 高速入出力（Java 24 最適化、SWAR・`VarHandle` 使用）+ [ベンチマーク環境](./Data%20Structures/FastIO24/Benchmark) |
| [HashMap](./Data%20Structures/HashMap) | オープンアドレス法のプリミティブ特化ハッシュマップ（O(1) clear、ペア/トリプルキー対応） |
| [Int128](./Data%20Structures/Int128) | 128bit 符号付き整数（四則演算・文字列変換・比較） |
| [ModNumbers](./Data%20Structures/ModNumbers) | 剰余演算ラッパー ModInt / ModLong（逆元・冪乗付き） |
| [PriorityQueue](./Data%20Structures/PriorityQueue) | 遅延ヒープ構築による高速優先度キュー（汎用 / int / long / インデックス付き） |
| [RingBuffer](./Data%20Structures/RingBuffer) | 容量を2の冪に正規化した高速リングバッファ（汎用 / int / long） |
| [SegmentTree](./Data%20Structures/SegmentTree) | セグメント木・遅延評価セグメント木・区間アフィン変換+二乗和（各 int / long 特化版あり） |
| [SparseTable](./Data%20Structures/SparseTable) | 📝 Sparse Table（TODO） |
| [Trie](./Data%20Structures/Trie) | Trie / SuffixTrie / RadixTrie / PatriciaTrie / 三分探索木 / DoubleArrayTrie / SuffixArray |
| [UnionFind](./Data%20Structures/UnionFind) | 経路圧縮 + rank 併用の素集合データ構造（グループ数・サイズ・辺数の管理付き） |

## ベンチマーク

FastIO の性能計測・比較用の環境を [Data Structures/FastIO24/Benchmark/](./Data%20Structures/FastIO24/Benchmark) に用意しています。
AtCoder 想定の JVM オプションでの繰り返し計測、CSV 出力、JIT / GC / JFR プロファイルの取得に対応しています。
使い方は同フォルダの [README](./Data%20Structures/FastIO24/Benchmark/README.md) を参照してください。

## リポジトリ構成

```
.
├── Algorithms/          # アルゴリズム（モジュールごとにフォルダ分割）
│   └── <Module>/
│       ├── src/         # 実装（Example.java は動作確認用）
│       ├── docs/        # クラス別の詳細ガイド
│       └── README.md    # モジュール概要
├── Data Structures/     # データ構造（構成は同上）
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
