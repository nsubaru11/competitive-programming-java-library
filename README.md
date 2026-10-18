![Java 24](https://img.shields.io/badge/Java-24-ED8B00?style=flat-square)
![Java 17 compatible](https://img.shields.io/badge/Java-17%20compatible-5382A1?style=flat-square)
![Dependencies: none](https://img.shields.io/badge/dependencies-none-4C9A2A?style=flat-square)
[![License: 0BSD](https://img.shields.io/badge/license-0BSD-blue?style=flat-square)](./LICENSE)

# Competitive Programming Java Library

AtCoder向けのJavaアルゴリズム・データ構造ライブラリです。

- [Author: nsubaru (AtCoder)](https://atcoder.jp/users/nsubaru)
- [実装状況](./docs/STATUS.md)
- [ベンチマーク](./docs/io/Java24/Benchmark/README.md)

## 特徴

- `lib.*` パッケージで開発し、AtCoder提出時は依存クラスを `Main.java` にバンドル
- int / long 特化版を含むデータ構造と、FastIOなどの高速化実装
- ビルドツール・外部ライブラリ不要

## 動作環境

Java 24を対象とします。`lib.io.compat17` はJava 17に対応しています。

## クイックスタート

必要なクラスをコンパイルして実行します。

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

> [!IMPORTANT]
> AtCoderにはimport版を提出できません。`run` / `test` / `localtest` / `tomain` / `submit` の前に、ライブラリを単一の `Main.java` へバンドルしてください。

## ライブラリ一覧

全体の実装状況・検証状況は[現状監査レポート](./docs/STATUS.md)を参照してください。

### アルゴリズム（`lib.graph` / `lib.math` / `lib.search` / `lib.sort` / `lib.string` / `lib.util`）

<details>
<summary>アルゴリズム一覧</summary>

| モジュール                                                        | 内容                                                                                                                            |
|-------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| [Conversions](./docs/util/Conversions)                            | 数値・文字配列・数字配列の相互変換                                                                                              |
| [Dice](./docs/util/Dice)                                          | 6面体の向きと3方向の回転操作                                                                                                    |
| [DP](./src/patterns/dp)                                           | 代表的な動的計画法の実装例（Frog・ナップサック・部分和）                                                                        |
| [DivideConquer](./docs/util/DivideConquer)                        | 🚧 転倒数計算（現在は未実装で常に0を返す）                                                                                      |
| [MoAlgorithm](./docs/util/MoAlgorithm)                            | Mo's Algorithm（0-indexed半開区間、4方向callback、ブロック幅指定、ジグザグ走査）                                                |
| [Graph/CentroidDecomposition](./docs/graph/CentroidDecomposition) | 📝 重心分解（TODO）                                                                                                             |
| [Graph/Connectivity](./docs/graph/Connectivity)                   | 🚧 橋・関節点検出、二重連結成分分解（一部未実装）                                                                               |
| [Graph/Core](./docs/graph/Core)                                   | 固定長の有向/無向グラフ、探索・SCC・二部判定                                                                                    |
| [Graph/Tree](./docs/graph/Tree)                                   | 木の直径、根付き木、HLD・LCA・入退場Euler Tour、全方位木DP。重心分解、仮想木、木上MoはTODO                                      |
| [Graph/Flow/MaxFlow](./docs/graph/MaxFlow)                        | 📝 Dinic法による最大流（TODO）                                                                                                  |
| [Graph/Flow/MinCostFlow](./docs/graph/MinCostFlow)                | 📝 最小費用流（TODO）                                                                                                           |
| [Graph/MinimumSpanningTree](./docs/graph/MinimumSpanningTree)     | Kruskal・Prim（最小/最大全域森、採用辺Result・cost-only版）※Edmonds 📝                                                          |
| [Graph/ShortestPath](./docs/graph/ShortestPath)                   | BFS・0-1 BFS・Dijkstra・Bellman-Ford・DAG最短/最長・Warshall-Floyd（負閉路の影響範囲判定）                                      |
| [Graph/TwoSat](./docs/graph/TwoSat)                               | 📝 2-SAT（含意グラフのSCC分解による充足判定、TODO）                                                                             |
| [Math/Convolution](./docs/math/Polynomial)                        | 🚧 NTT、任意mod、AND / OR / XOR / GCD / LCM畳み込み、各種ゼータ変換、FWHTは利用可能。FFTは開発中                                |
| [Math/MathUtils](./docs/math/MathUtils)                           | 整数演算・GCD・階乗・組み合わせ・トーシェント関数などの主要な静的入口                                                           |
| [Math/FactorialTable](./docs/math/FactorialTable)                 | 動的な階乗・逆元テーブル、nCr / nPr / Catalan・Lah・Narayana数                                                                  |
| [Math/FactorUtils](./docs/math/FactorUtils)                       | 素因数分解、素因数・指数配列、素因数個数、約数個数・昇順列挙                                                                    |
| [Math/GeometryUtils](./docs/math/GeometryUtils)                   | 線分・矩形の交差判定、点と図形の位置関係、各種距離計算                                                                          |
| [Math/LinearAlgebra](./docs/math/LinearAlgebra)                   | int / long行列演算。掃き出し法・行列式・ランクは📝                                                                              |
| [Math/Number types](./docs/math/number)                           | Fraction / Int128 / ModInt / ModLong                                                                                            |
| [Math/NumberPredicates](./docs/math/NumberPredicates)             | 完全数・回文数・フィボナッチ数・アームストロング数・ハッピー数の判定                                                            |
| [Util/Permutation](./docs/util/Permutation)                       | 配列の辞書順 index/next/prev（int / long / char / 2次元配列対応）                                                               |
| [Util/FormatUtils](./docs/util/FormatUtils)                       | 数値・配列の文字列化、小数の固定桁表示、整数のゼロ埋め                                                                          |
| [Util/Function](./docs/util/Function)                             | プリミティブ特化の汎用関数型インターフェース                                                                                    |
| [Util/DigitUtils](./docs/util/DigitUtils)                         | 桁数取得、十進表現の反転・並べ替え                                                                                              |
| [Math/Polynomial](./docs/math/Polynomial)                         | 多項式の加減乗・微分・積分・評価などの多項式ユーティリティ                                                                      |
| [Math/PrimeUtils](./docs/math/PrimeUtils)                         | 一回限りの素数判定・素数個数・エラトステネスの篩                                                                                |
| [Math/PrimeTable](./docs/math/PrimeTable)                         | 再利用可能な素数表、近傍検索・素因数分解・素数の添字アクセス                                                                    |
| [Randomized](./src/lib/search/QuickSelect.java)                   | QuickSelect（k 番目の要素を期待 $\mathcal{O}(n)$ で取得）                                                                       |
| [Search/BinarySearch](./docs/search/BinarySearch)                 | 条件関数・ソート済み配列に対する二分探索（lower/upper bound、`-(挿入位置+1)` 形式）                                             |
| [Search/UnimodalUtils](./docs/search/UnimodalUtils)               | 📝 三分探索・黄金分割探索・ニュートン法（TODO）                                                                                 |
| [Sort](./docs/sort)                                               | 実装・比較用の基本ソート11種。将来は連動ソートなどの競技向けユーティリティを追加予定 ※FordJohnson / CountingSort / RadixSort 📝 |
| [String/Levenshtein](./docs/string/Levenshtein)                   | 編集距離（標準 DP・距離制限付き banded DP）※Myers / Wu 📝                                                                       |
| [String/Palindrome](./docs/string/Palindrome)                     | Manacher（全回文検出 $\mathcal{O}(n)$）・素朴な回文判定/生成 ※Eertree 📝                                                        |
| [String/StringSearch](./docs/string/StringSearch)                 | Z-Algorithm ※KMP / BM / RollingHash / AhoCorasick 📝                                                                            |
| [未実装バックログ](./docs/STATUS.md#todoのみのクラス)             | 頻出の未作成データ構造・アルゴリズムをコメントのみのTODOクラスとして管理                                                        |

</details>

### データ構造（`lib.ds`）

<details>
<summary>データ構造一覧</summary>

| モジュール                                               | 内容                                                                                                                 |
|----------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| [Primitive Array Utilities](docs/ds/Arrays)              | プリミティブ配列、集計・探索・反転、1D/2D/3D累積和・剰余累積和、循環配列、deque、2D配列、座標圧縮、LIS・窓・部分集合 |
| [AVLTree](./docs/ds/AVLTree)                             | AVL 木による順序付き Set / Multiset（順位検索・近傍検索対応、int / long 特化版あり）                                 |
| [BinaryIndexedTree](docs/ds/fenwick)                     | BIT（点更新区間和・2D・区間加算区間和、BIT 上の二分探索 `lowerBound` / `upperBound` 付き）                           |
| [BinarySearchTree](./docs/ds/BinarySearchTree)           | 基本 BST・Treap（順序統計付き乱択平衡 BST）※赤黒木・B木・vEB木 📝                                                    |
| [CartesianTree](./src/lib/ds/CartesianTree.java)         | 📝 デカルト木（TODO）                                                                                                |
| [EulerTour](./docs/ds/EulerTour)                         | 根付き木の入退場イベント列（長さ `2N`）                                                                              |
| [FastIO/Java17](./docs/io/Java17)                        | 高速入出力（**Java 17 互換**）・対話問題用 InteractiveScanner                                                        |
| [FastIO/Java24](./docs/io/Java24)                        | 高速入出力（Java 24 最適化、SWAR・`VarHandle` 使用）+ [ベンチマーク環境](./docs/io/Java24/Benchmark)                 |
| [HashMap](./docs/ds/HashMap)                             | オープンアドレス法のプリミティブ特化ハッシュマップ（$\mathcal{O}(1)$ clear、ペア/トリプルキー対応）                  |
| [LiChaoTree](./docs/ds/LiChaoTree)                       | 📝 Li Chao Tree（直線群へのCHT、TODO）                                                                               |
| [PersistentSegmentTree](./docs/ds/PersistentSegmentTree) | 📝 永続セグメント木（TODO）                                                                                          |
| [PersistentUnionFind](./docs/ds/PersistentUnionFind)     | 📝 永続Union-Find（TODO）                                                                                            |
| [PriorityQueue](./docs/ds/PriorityQueue)                 | 遅延ヒープ構築、両端int版、generic / int / longのindex付き更新に対応する優先度キュー                                 |
| [SegmentTree](./docs/ds/SegmentTree)                     | セグメント木・遅延評価セグメント木・区間アフィン変換+二乗和（各 int / long 特化版あり）                              |
| [SegmentTree2D](./docs/ds/SegmentTree2D)                 | 📝 2次元セグメント木（TODO）                                                                                         |
| [SkipList](./docs/ds/SkipList)                           | 📝 スキップリスト（TODO）                                                                                            |
| [SparseTable](./docs/ds/SparseTable)                     | 冪等演算の静的区間クエリ（ジェネリクス / int / long、構築 `O(N log N)`・クエリ `O(1)`）                              |
| [Trie](./docs/ds/Trie)                                   | Trie / SuffixTrie / RadixTrie / PatriciaTrie / 三分探索木 / DoubleArrayTrie / SuffixArray ※SuffixAutomaton 📝        |
| [UnionFind](./docs/ds/UnionFind)                         | 経路圧縮 + rank 併用の素集合データ構造（グループ数・サイズ・辺数の管理付き）                                         |
| [WaveletTree](./docs/ds/WaveletTree)                     | 📝 Wavelet Tree（TODO）                                                                                              |

</details>

## ベンチマーク

FastIO の性能計測・比較用の環境を [docs/io/Java24/Benchmark/](./docs/io/Java24/Benchmark)に用意しています。
AtCoder 想定の JVM オプションでの繰り返し計測、CSV 出力、JIT / GC / JFR プロファイルの取得に対応しています。
使い方は同フォルダの [README](./docs/io/Java24/Benchmark/README.md) を参照してください。

<details>
<summary>リポジトリ構成</summary>

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

- `src/lib` と `src/patterns` はソース、`test` は検証コードです。
- `Example.java` は使用例、`Check*.java` は[Library Checker](https://judge.yosupo.jp/)などでの検証用です。

</details>

## 開発ポリシー

- 実行速度を重視し、入力検証は必要な範囲に絞っています。
- 静的解析には [Qodana Community for JVM](https://www.jetbrains.com/qodana/jvm/) を使用します。
- Issue / PR テンプレートは[こちら](./.github)です。

## ライセンス

本リポジトリの内容は、個別に別条件が明記された第三者の素材を除き、[0BSD（Zero-Clause BSD）](./LICENSE)で提供します。
利用・改変・再配布に、作者表示やライセンス文の同梱を求めません。

## 免責事項

本ライブラリは現状のまま提供され、バグがないこと、正確性、完全性、特定用途への適合性などを保証しません。
競技プログラミング向けの実装であり、各APIの仕様、制約、前提条件を確認したうえで利用してください。サポートや保守も保証しません。
ライセンスの免責・責任制限条項は適用される法令の許す範囲で適用されます。詳細は[LICENSE](./LICENSE)を参照してください。
