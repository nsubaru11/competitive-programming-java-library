# プロジェクトガイドライン（Junie 用）

このリポジトリは、競技プログラミング向けの Java ライブラリ集です。アルゴリズムとデータ構造を、問題でそのままコピペして使えるように単一クラス単位で配置しています。ビルドツールに依存せず、標準の `javac/java` だけで動作します。

— 目的：素早く正確に実装を再利用できること
— 対象：AtCoder、Codeforces 等のコンテスト環境

## 1. ディレクトリ構成（概要）
- Algorithms
  - Basic, BinarySearch, DP, Factorial, Levenshtein, MathFunctions,
    MinimumSpaningTree, Palindrome, Permutation, PrimeNumber,
    ShortestPath, Sort, StringSearch, UnimodalUtils
- Data Structures
  - ArrayUtils, BinaryIndexedTree, BinarySearchTree, FastIO,
    ModNumbers, PriorityQueue, RingBuffer, SegmentTree,
    SparseTable, Trie, UnionFind
- .junie/guidelines.md（本ファイル）
- README.md / README_TEMPLATE.md（概要・テンプレート）
- GuideTemplate.md（ドキュメントテンプレート）
- out/（コンパイル済み出力先、任意）
- logs/（JFR などのログ、任意）

各サブフォルダの `src/` 直下にクラスがあり、基本的に「パッケージ宣言なし（デフォルトパッケージ）」で配置されています。必要なファイルだけをコピーして使うか、単発でコンパイルして利用します。

例：
- FastIO 系（Data Structures/FastIO/src）
  - FastScanner, ContestScanner, CompressedFastScanner
  - FastPrinter, ContestPrinter, CompressedFastPrinter
- 平衡二分探索木（BinarySearchTree/src）
  - AVLMultiSet, AVLSet, IntAVLMultiSet など

## 2. ビルド・実行（標準 Java のみ）
このリポジトリはビルドツール非依存です。必要なクラスだけをコンパイルします。

- 最小例（Windows/PowerShell）：
  1) コンパイル
     javac -encoding UTF-8 -d out "Data Structures\FastIO\src\FastScanner.java" "Data Structures\FastIO\src\FastPrinter.java"
  2) 利用（Main を自作した場合）
     javac -encoding UTF-8 -cp out -d out Main.java
     java -cp out Main

- 注意
  - パッケージ宣言が無いので、クラスパス上で同一ディレクトリに置かれる前提です。
  - Java 17。
  - エンコーディングは UTF-8 を推奨。

## 3. テスト方針
現状、テストフレームワーク（JUnit 等）は導入していません。検証は以下の方針とします。
- スニペットテスト：任意の `Main.java` を作成して対象クラスを直接呼び出し、入出力や計算結果を確認する。
- 競技環境での実地テスト：提出前にサンプルケース・自作ケースを通す。
- パフォーマンステスト：FastIO のベンチマークメモ（Data Structures/FastIO/Benchmark/）を参照し、必要に応じて入力規模を変えて確認する。

Junie が修正を行う場合は、最低限の再現用 `Main.java` を一時的に作成し、`out` に出力して動作確認してからコミットします（恒久ファイルにしないこと）。

## 4. コードスタイル / 設計の指針
- 競技向け最適化を優先：例外や境界チェックは必要十分に限定。
- 依存は最小限：標準ライブラリのみを基本とし、外部依存は持たない。
- パッケージなし：コンテストでのコピペ容易性を優先。
- ネーミング：アルゴリズム名・データ構造名をそのままクラス名に。メソッドは短く直感的に。
- 文字コード：UTF-8。
- 性能クリティカルな箇所（例：FastScanner#nextLong など）は、コメントに計算量/注意点を残す。

## 5. 既知の主要コンポーネント
- FastIO 系
  - 高速入力: FastScanner, ContestScanner, CompressedFastScanner
  - 高速出力: FastPrinter, ContestPrinter, CompressedFastPrinter
- 平衡 BST 系
  - AVLMultiSet／AVLSet／IntAVLMultiSet 等（順序統計・重複管理・rank/選択が可能）
- その他データ構造
  - Fenwick Tree, Segment Tree, Sparse Table, Union-Find, Trie など
- アルゴリズム集
  - 二分探索、ソート、最短路、最小全域木、素数/数論、DP、文字列探索 等

## 6. 変更・レビュー手順（Junie 向け）
1) 課題の把握：対象ファイルと API を特定、関連する周辺コードも確認。
2) 最小変更：既存 API 互換（メソッド名/引数/動作）を可能な限り維持。
3) 動作確認：必要に応じ `Main.java` を作成して再現・確認。ベンチが関わる場合は入力規模を変えて確認。
4) ドキュメント更新：使用方法や注意をコメントまたは本ガイドに反映。
5) 提出：変更点の概要と確認結果を添える。

## 7. 典型的な利用例（FastIO）
```
try (FastScanner fs = new FastScanner(System.in); FastPrinter out = new FastPrinter(System.out)) {
    int n = fs.nextInt();
    long sum = 0;
    for (int i = 0; i < n; i++) sum += fs.nextLong();
    out.println(sum);
}
```

## 8. 注意点 / よくある落とし穴
- デフォルトパッケージのため、IDE のパッケージ規約と衝突することがあります（競技用途の設計）。
- 一部クラスは高速化のためにエラーハンドリングを最小化しています。入力不正時の挙動は未定義の場合があります。
- Windows のパス区切りは `\`。PowerShell コマンドは Bash と異なります。

## 9. ライセンス / クレジット
リポジトリの README を参照してください。外部依存は原則ありませんが、引用元がある場合は該当ファイルにクレジットを併記してください。

## 10. このファイルの保守
- プロジェクト構成に大きな変更があれば本ガイドを更新。
- 新規コンポーネントを追加した際は、概要/使い方/注意点を追記。
