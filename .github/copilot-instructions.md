# Copilot Instructions

## プロジェクト概要

このプロジェクトは競技プログラミング用のJavaライブラリを提供します。

## コーディング規則

- **JavaDoc コメント**: 圧縮版のクラス以外の全てのクラス・メソッドでJavaDocコメントを記述すること。
- **定数の命名規則**: 定数はUPPER_SNAKE_CASEで記述すること。
- **インデント**: インデントにはタブキーを使用すること。
- **ビット演算子の使用**: 限りなく高速化なコードにするため、可能ならビット演算子を使用すること。
- **オーバーロードの作成**: 汎用ライブラリを作成するため、様々なオーバーロードを作成すること。
- **プリミティブ型版とジェネリクス型版の実装**: 高速に動作させるため、プリミティブ型版とジェネリクス型版の両方を実装すること。
- **多次元配列の圧縮**: 内部的に多次元配列を用いる場合、1次元に圧縮すること。

## ディレクトリ構成

- `src`: ソースコードを含む。
- `docs`: ドキュメントを含む。

C:.
├───.claude
├───.github
│ ├───ISSUE_TEMPLATE
│ └───PULL_REQUEST_TEMPLATE
├───.junie
├───Algorithms
│ ├───Conversion
│ │ └───src
│ ├───DP
│ │ └───src
│ ├───DivideAndConquer
│ │ ├───CentroidDecomposition
│ │ │ └───src
│ │ └───MoAlgorithm
│ │ └───src
│ ├───Graph
│ │ ├───Connectivity
│ │ │ └───src
│ │ ├───Core
│ │ │ └───src
│ │ ├───Flow
│ │ │ ├───MaxFlow
│ │ │ │ └───src
│ │ │ └───MinCostFlow
│ │ │ └───src
│ │ ├───MinimumSpanningTree
│ │ │ ├───docs
│ │ │ └───src
│ │ ├───ShortestPath
│ │ │ ├───docs
│ │ │ └───src
│ │ └───TwoSat
│ │ └───src
│ ├───Math
│ │ ├───Combinatorics
│ │ │ ├───docs
│ │ │ └───src
│ │ ├───Convolution
│ │ │ └───src
│ │ ├───Factorial
│ │ │ └───src
│ │ ├───Geometry
│ │ │ ├───docs
│ │ │ └───src
│ │ ├───LinearAlgebra
│ │ │ └───src
│ │ ├───Matrix
│ │ │ └───src
│ │ ├───NumberTheory
│ │ │ └───src
│ │ ├───NumberUtils
│ │ │ ├───docs
│ │ │ └───src
│ │ ├───Permutation
│ │ │ ├───docs
│ │ │ └───src
│ │ ├───Polynomial
│ │ │ └───src
│ │ └───PrimeNumber
│ │ ├───docs
│ │ └───src
│ ├───Randomized
│ │ └───src
│ ├───Search
│ │ ├───BinarySearch
│ │ │ ├───docs
│ │ │ └───src
│ │ └───UnimodalUtils
│ │ └───src
│ ├───Sort
│ │ ├───docs
│ │ └───src
│ └───String
│ ├───Levenshtein
│ │ ├───docs
│ │ └───src
│ ├───Palindrome
│ │ ├───docs
│ │ └───src
│ └───StringSearch
│ ├───docs
│ └───src
└───DataStructures
├───AVLTree
│ ├───docs
│ └───src
├───ArrayUtils
│ ├───docs
│ └───src
├───BinaryIndexedTree
│ ├───docs
│ └───src
├───BinarySearchTree
│ ├───docs
│ └───src
├───BitSet
│ └───src
├───CartesianTree
│ └───src
├───EulerTour
│ └───src
├───FastIO
│ ├───Java17
│ │ ├───Benchmark
│ │ │ ├───BatchFiles
│ │ │ └───Output
│ │ ├───docs
│ │ └───src
│ └───Java24
│ ├───Benchmark
│ ├───docs
│ └───src
├───HashMap
│ ├───docs
│ └───src
├───Int128
│ └───src
├───LiChaoTree
│ └───src
├───ModNumbers
│ ├───docs
│ └───src
├───PersistentSegmentTree
│ └───src
├───PersistentUnionFind
│ └───src
├───PriorityQueue
│ ├───docs
│ └───src
├───RingBuffer
│ ├───docs
│ └───src
├───SegmentTree
│ ├───docs
│ └───src
├───SegmentTree2D
│ └───src
├───SkipList
│ └───src
├───SparseTable
│ ├───docs
│ └───src
├───Trie
│ ├───docs
│ └───src
├───UnionFind
│ ├───docs
│ └───src
└───WaveletTree
└───src

## JDKバージョン

このプロジェクトは **JDK 24（24.0.2）** を使用して開発を行います。

- JDK 24の最新機能を活用し、コードの最適化や安全性の向上を図ること。
- IntelliJ IDEAの設定やビルドツール（例: Maven, Gradleなど）がJDK 24に対応していることを確認すること。
- 環境変数およびIDEの構成でJDK 24が優先されるように設定してください。
- 例外として `DataStructures/FastIO/Java17` 配下のみ JDK 17 互換を維持すること（Java 17 環境向けの実装のため）。