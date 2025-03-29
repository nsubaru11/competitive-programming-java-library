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
  │  .gitignore
  │  README.md
  │
  ├─.github
  │      copilot-instructions.md
  │
  ├─.idea
  │      competitive-programming-java-library.iml
  │      misc.xml
  │      modules.xml
  │      vcs.xml
  │      workspace.xml
  │
  ├─BinarySearch
  │  │  BinarySearch.iml
  │  │
  │  ├─docs
  │  │      BinarySearchGuide.md
  │  │
  │  └─src
  │          AbstractBinarySearch.java
  │          ArrayBinarySearch.java
  │          Example.java
  │
  ├─FastIO
  │  │  FastIO.iml
  │  │
  │  ├─docs
  │  │      PrinterGuide.md
  │  │      ScannerGuide.md
  │  │
  │  └─src
  │          CompressedFastPrinter.java
  │          CompressedFastScanner.java
  │          ContestPrinter.java
  │          ContestScanner.java
  │          Example.java
  │          FastPrinter.java
  │          FastScanner.java
  │
  ├─MathFunctions
  │  │  .gitignore
  │  │  MathFunctions.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  └─src
  │          MathFn.java
  │
  ├─out
  │  └─production
  │      ├─BinarySearch
  │      │      AbstractBinarySearch$1.class
  │      │      AbstractBinarySearch$SearchType.class
  │      │      AbstractBinarySearch.class
  │      │      ArrayBinarySearch.class
  │      │      Example$1.class
  │      │      Example$BinarySearch.class
  │      │      Example.class
  │      │
  │      ├─FastIO
  │      │      CompressedFastPrinter$FastPrinter.class
  │      │      CompressedFastPrinter.class
  │      │      CompressedFastScanner$FastScanner.class
  │      │      CompressedFastScanner.class
  │      │      ContestPrinter.class
  │      │      ContestScanner.class
  │      │      Example.class
  │      │      FastPrinter.class
  │      │      FastScanner.class
  │      │
  │      ├─Palindrome
  │      │      Example.class
  │      │      Manacher.class
  │      │      PalindromeUtils.class
  │      │
  │      └─ShortestPath
  │              BellmanFord$Edge.class
  │              BellmanFord.class
  │              Dijkstra$Edge.class
  │              Dijkstra$Vertex.class
  │              Dijkstra.class
  │              Example.class
  │              Warshallfroyd.class
  │
  ├─Palindrome
  │  │  .gitignore
  │  │  Palindrome.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  ├─out
  │  │  └─production
  │  │      └─Palindrome
  │  │              Example.class
  │  │              Manacher.class
  │  │              PalindromeUtils.class
  │  │
  │  └─src
  │          Example.java
  │          Manacher.java
  │          PalindromeUtils.java
  │
  ├─Permutation
  │  │  .gitignore
  │  │  Permutation.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  └─src
  │          Permutation.java
  │
  ├─PrimeNumber
  │  │  .gitignore
  │  │  PrimeNumber.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  ├─out
  │  │  └─production
  │  │      └─PrimeNumber
  │  │              Example.class
  │  │              PrecomputedPrimes$1.class
  │  │              PrecomputedPrimes.class
  │  │              PrimeUtils.class
  │  │
  │  └─src
  │          Example.java
  │          PrecomputedPrimes.java
  │          PrimeUtils.java
  │
  ├─RingBuffer
  │  │  .gitignore
  │  │  RingBuffer.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  ├─docs
  │  │      RingBufferGuide.md
  │  │
  │  └─src
  │          IntegerRingBuffer.java
  │          LongRingBuffer.java
  │          RingBuffer.java
  │
  ├─ShortestPath
  │  │  ShortestPath.iml
  │  │
  │  ├─docs
  │  │      ShortestPathGuide.md
  │  │
  │  └─src
  │          BellmanFord.java
  │          Dijkstra.java
  │          Example.java
  │          Warshallfroyd.java
  │
  ├─TernarySearch
  │  │  .gitignore
  │  │  TernarySearch.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  └─src
  │          Example.java
  │          TernarySearch.java
  │
  ├─Trie
  │  │  .gitignore
  │  │  Trie.iml
  │  │
  │  ├─.idea
  │  │      .gitignore
  │  │      misc.xml
  │  │      modules.xml
  │  │      vcs.xml
  │  │      workspace.xml
  │  │
  │  ├─docs
  │  │      guide.md
  │  │
  │  ├─out
  │  │  └─production
  │  │      └─Trie
  │  │              CompactTrie.class
  │  │              DoubleArrayTrie.class
  │  │              Example.class
  │  │              PatriciaTree.class
  │  │              SuffixArray.class
  │  │              SuffixTree.class
  │  │              TernarySearchTree.class
  │  │              Trie$TrieNode.class
  │  │              Trie.class
  │  │
  │  └─src
  │          CompactTrie.java
  │          DoubleArrayTrie.java
  │          Example.java
  │          PatriciaTrie.java
  │          SuffixArray.java
  │          SuffixTree.java
  │          TernarySearchTree.java
  │          Trie.java
  │
  └─UnionFind
  │  .gitignore
  │  UnionFind.iml
  │
  ├─.idea
  │      .gitignore
  │      misc.xml
  │      modules.xml
  │      vcs.xml
  │      workspace.xml
  │
  └─src
  UnionFind.java
