# Competitive Programming Java Library

AtCoder などの競技プログラミングで使用することを想定した、Java 製のアルゴリズム・データ構造ライブラリ集です。

- 作者: [nsubaru (AtCoder)](https://atcoder.jp/users/nsubaru)

## 特徴

- **パッケージ化されたAPI**: `lib.*` を単一ソースツリーとして管理し、IDEの補完・定義ジャンプ・安全なリファクタリングをライブラリ全域で利用できます
- **提出時の自動バンドル**: AtCoder側のランナーが `import lib.*` と推移的依存を単一の `Main.java` へ展開します
- **プリミティブ特化版を併設**: 主要なデータ構造はジェネリクス版に加えて int / long 特化版を提供し、オートボクシングのオーバーヘッドを回避します
- **低レベル最適化**: ビット演算・手動バッファリング・SWAR・`VarHandle` などを活用した高速化を行っています（特に FastIO）
- **段階的なドキュメント整備**: 各モジュールの README と主要クラスの詳細ガイドを `docs/` 配下で整備しています。実装・検証状況は[現状監査レポート](./docs/STATUS.md)にまとめています

## 動作環境

| 項目         | 内容                                                            |
|--------------|-----------------------------------------------------------------|
| JDK          | **Java 24（24.0.2 想定）** ※`lib.io.compat17` のみ Java 17 互換 |
| ビルドツール | 不要（`javac` / `java` のみで動作）                             |
| 外部依存     | なし（標準ライブラリのみ）                                      |

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

AtCoderへはこのimport版を直接提出できません。
`run` / `test` / `localtest` / `tomain` / `submit` は、必ずライブラリをバンドルした提出形へ変換してから実行します。
バンドラを使えない場合は、`src/lib/` の依存元ファイルを確認して従来どおり手動展開できます。

## ライブラリ一覧

実装状況: 印なし = 実装済み ／ 🚧 = 開発中・一部未実装 ／ 📝 = 未実装（TODO のみ）

2026-09-02 時点のファイル単位の集計、利用不可API、検証状況、追加TODO一覧は[現状監査レポート](./docs/STATUS.md)を参照してください。

### アルゴリズム（`lib.graph` / `lib.math` / `lib.search` / `lib.sort` / `lib.string` / `lib.util`）

| モジュール                                                        | 内容                                                                                                                            |
|-------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| [Conversions](./docs/util/Conversions)                            | 数値・文字配列・数字配列の相互変換                                                                                              |
| [Dice](./docs/util/Dice)                                          | 6面体の向きと3方向の回転操作                                                                                                    |
| [DP](./src/patterns/dp)                                           | 代表的な動的計画法の実装例（Frog・ナップサック・部分和）                                                                        |
| [DivideConquer](./docs/util/DivideConquer)                        | 🚧 転倒数計算（現在は未実装で常に0を返す）                                                                                      |
| [MoAlgorithm](./docs/util/MoAlgorithm)                            | Mo's Algorithm（0-indexed半開区間、4方向callback、ブロック幅指定、ジグザグ走査）                                                |
| [Graph/CentroidDecomposition](./docs/graph/CentroidDecomposition) | 📝 重心分解（TODO）                                                                                                             |
| [Graph/Connectivity](./docs/graph/Connectivity)                   | 🚧 橋・関節点検出、二重連結成分分解（一部未実装）                                                                               |
| [Graph/Core](./docs/graph/Core)                                   | 固定長の有向/無向グラフ、探索・SCC・二部判定、木の直径、根付き木のLCA・HLD                                                      |
| [Graph/Flow/MaxFlow](./docs/graph/MaxFlow)                        | 📝 Dinic法による最大流（TODO）                                                                                                  |
| [Graph/Flow/MinCostFlow](./docs/graph/MinCostFlow)                | 📝 最小費用流（TODO）                                                                                                           |
| [Graph/MinimumSpanningTree](./docs/graph/MinimumSpanningTree)     | Kruskal・Prim（最小/最大全域森、採用辺Result・cost-only版）※Edmonds 📝                                                          |
| [Graph/ShortestPath](./docs/graph/ShortestPath)                   | BFS・0-1 BFS・Dijkstra・Bellman-Ford・DAG最短/最長・Warshall-Floyd（負閉路の影響範囲判定）                                      |
| [Graph/TwoSat](./docs/graph/TwoSat)                               | 📝 2-SAT（含意グラフのSCC分解による充足判定、TODO）                                                                             |
| [Math/Convolution](./docs/math/Polynomial)                        | 🚧 NTT、AND / OR / XOR / GCD / LCM畳み込み、各種ゼータ変換、FWHTは利用可能。FFT / 任意mod畳み込みは開発中                       |
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

### データ構造（`lib.ds`）

| モジュール                                               | 内容                                                                                                                 |
|----------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| [Primitive Array Utilities](docs/ds/Arrays)              | プリミティブ配列、集計・探索・反転、1D/2D/3D累積和・剰余累積和、循環配列、deque、2D配列、座標圧縮、LIS・窓・部分集合 |
| [AVLTree](./docs/ds/AVLTree)                             | AVL 木による順序付き Set / Multiset（順位検索・近傍検索対応、int / long 特化版あり）                                 |
| [BinaryIndexedTree](docs/ds/fenwick)                     | BIT（点更新区間和・2D・区間加算区間和、BIT 上の二分探索 `lowerBound` / `upperBound` 付き）                           |
| [BinarySearchTree](./docs/ds/BinarySearchTree)           | 基本 BST・Treap（順序統計付き乱択平衡 BST）※赤黒木・B木・vEB木 📝                                                    |
| [CartesianTree](./src/lib/ds/CartesianTree.java)         | 📝 デカルト木（TODO）                                                                                                |
| [EulerTour](./docs/ds/EulerTour)                         | 📝 オイラーツアー（TODO）                                                                                            |
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
| [SparseTable](./docs/ds/SparseTable)                     | 📝 Sparse Table（TODO）                                                                                              |
| [Trie](./docs/ds/Trie)                                   | Trie / SuffixTrie / RadixTrie / PatriciaTrie / 三分探索木 / DoubleArrayTrie / SuffixArray ※SuffixAutomaton 📝        |
| [UnionFind](./docs/ds/UnionFind)                         | 経路圧縮 + rank 併用の素集合データ構造（グループ数・サイズ・辺数の管理付き）                                         |
| [WaveletTree](./docs/ds/WaveletTree)                     | 📝 Wavelet Tree（TODO）                                                                                              |

## ベンチマーク

FastIO の性能計測・比較用の環境を [docs/io/Java24/Benchmark/](./docs/io/Java24/Benchmark)に用意しています。
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
- **コーディング規約**: インデントはタブ、定数は UPPER_SNAKE_CASE、JavaDoc は随時更新します。
- **静的解析**: [Qodana](https://www.jetbrains.com/qodana/)（qodana-jvm 2025.2 / JDK 24）を使用
- **Issue / PR**: [テンプレート](./.github)を用意しています

## 免責事項

本ライブラリはバグを含む可能性があり、完全な動作は保証しません。あくまでライブラリ作成のヒントとしてご利用ください。
