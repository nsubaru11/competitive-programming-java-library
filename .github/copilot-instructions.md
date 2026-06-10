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
│ ├───Basic
│ │ └───src
│ ├───BinarySearch
│ │ ├───docs
│ │ └───src
│ ├───Convolution
│ │ └───src
│ ├───DP
│ │ └───src
│ ├───Factorial
│ │ └───src
│ ├───Graph
│ │ ├───ShortestPath
│ │ │ ├───docs
│ │ │ └───src
│ │ └───SimpleGraph
│ │ └───src
│ ├───Levenshtein
│ │ ├───docs
│ │ └───src
│ ├───MathFunctions
│ │ ├───docs
│ │ └───src
│ ├───Matrix
│ │ └───src
│ ├───MinimumSpanningTree
│ │ ├───docs
│ │ └───src
│ ├───Palindrome
│ │ ├───docs
│ │ └───src
│ ├───Permutation
│ │ ├───docs
│ │ └───src
│ ├───PrimeNumber
│ │ ├───docs
│ │ └───src
│ ├───Randomized
│ │ └───src
│ ├───Sort
│ │ ├───docs
│ │ └───src
│ ├───StringSearch
│ │ ├───docs
│ │ └───src
│ └───UnimodalUtils
│ └───src
├───Data Structures
│ ├───ArrayUtils
│ │ ├───docs
│ │ └───src
│ ├───AVLTree
│ │ ├───docs
│ │ └───src
│ ├───BinaryIndexedTree
│ │ ├───docs
│ │ └───src
│ ├───BinarySearchTree
│ │ ├───docs
│ │ └───src
│ ├───BitSet
│ │ └───src
│ ├───CartesianTree
│ │ └───src
│ ├───FastIO17
│ │ ├───Benchmark
│ │ │ ├───BatchFiles
│ │ │ ├───Output
│ │ │ └───TestCases
│ │ ├───docs
│ │ ├───out
│ │ └───src
│ ├───FastIO24
│ │ ├───Benchmark
│ │ ├───docs
│ │ ├───out
│ │ └───src
│ ├───HashMap
│ │ ├───docs
│ │ └───src
│ ├───Int128
│ │ └───src
│ ├───ModNumbers
│ │ ├───docs
│ │ └───src
│ ├───PriorityQueue
│ │ ├───docs
│ │ └───src
│ ├───RingBuffer
│ │ ├───docs
│ │ └───src
│ ├───SegmentTree
│ │ ├───docs
│ │ └───src
│ ├───SparseTable
│ │ ├───docs
│ │ └───src
│ ├───Trie
│ │ ├───docs
│ │ └───src
│ └───UnionFind
│ ├───docs
│ └───src

## JDKバージョン

このプロジェクトは **JDK 17** を使用して開発を行います。

- JDK 17の最新機能を活用し、コードの最適化や安全性の向上を図ること。
- IntelliJ IDEAの設定やビルドツール（例: Maven, Gradleなど）がJDK 17に対応していることを確認すること。
- 環境変数およびIDEの構成でJDK 17が優先されるように設定してください。

<!-- jdk1hsjava17 のラベルが付与されています。プロジェクトの全コードはJDK 17の基準に従って作成すること。 -->