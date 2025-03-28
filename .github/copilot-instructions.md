# 技術スタック
- Python 3.10

# ディレクトリ構成
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
│  │  BinarySearchGuide.md
│  │
│  └─src
│          AbstractBinarySearch.java
│
├─FastIO
│  │  FastIO.iml
│  │  PrinterGuide.md
│  │  ScannerGuide.md
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
│  │              Example$FastPrinter.class
│  │              Example$FastScanner.class
│  │              Example.class
│  │              Manacher.class
│  │              Manacher2.class
│  │              PalindromeUtils.class
│  │
│  └─src
│          Example.java
│          Manacher.java
│          Manacher2.java
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
│  │  RingBufferGuide.md
│  │
│  ├─.idea
│  │      .gitignore
│  │      misc.xml
│  │      modules.xml
│  │      vcs.xml
│  │      workspace.xml
│  │
│  └─src
│          IntegerRingBuffer.java
│          LongRingBuffer.java
│          RingBuffer.java
│
├─ShortestPath
│  │  ShortestPath.iml
│  │  ShortestPathGuide.md
│  │
│  └─src
│          BellmanFord.java
│          Dijkstra.java
│          Example.java
│          Warshallfroyd.java
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

# コーディング規則
- 圧縮版のクラス以外は全てのクラス・メソッドでjavadocを記述する
- 定数はUPPER_SNAKE_CASEで記述する
- インデントにはタブキーを用いる
- 限りなく高速化なコードにするためしばしばbit演算子を用いる
- 汎用なライブラリを作成するため様々なオーバーロードを作成する
- 高速に動作させるためプリミティブ型版の実装と、ジェネリクス型版のをしばしば行う
- 内部的に多次元配列を用いる場合、1次元に圧縮する。
