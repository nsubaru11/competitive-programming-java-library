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
	├───.github
	│ ├───ISSUE_TEMPLATE
	│ └───PULL_REQUEST_TEMPLATE
	├───.idea
	│ └───inspectionProfiles
	├───Algorithms
	│ ├───.idea
	│ ├───BinarySearch
	│ │ ├───docs
	│ │ └───src
	│ ├───EditDistance
	│ │ ├───.idea
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───LevenshteinDP
	│ │ └───src
	│ ├───Factorial
	│ │ ├───.idea
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Factorial
	│ │ └───src
	│ ├───Master
	│ │ ├───.idea
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Master
	│ │ └───src
	│ ├───MathFunctions
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───MinimumSpaningTree
	│ │ ├───.idea
	│ │ │ └───inspectionProfiles
	│ │ ├───docs
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───MinimumSpaningTree
	│ │ └───src
	│ ├───Palindrome
	│ │ ├───.idea
	│ │ ├───docs
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Palindrome
	│ │ └───src
	│ ├───Permutation
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───PrimeNumber
	│ │ ├───.idea
	│ │ ├───docs
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───PrimeNumber
	│ │ └───src
	│ ├───ShortestPath
	│ │ ├───docs
	│ │ └───src
	│ ├───Sort
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───StringSearch
	│ │ ├───.idea
	│ │ └───src
	│ └───TernarySearch
	│ ├───.idea
	│ └───src
	├───Data Structures
	│ ├───ArrayUtils
	│ │ ├───.idea
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───ArrayUtils
	│ │ └───src
	│ ├───BinaryIndexedTree
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───BinarySearchTree
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───FastIO
	│ │ ├───docs
	│ │ └───src
	│ ├───RingBuffer
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───SegmentTree
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───SparseTable
	│ │ ├───.idea
	│ │ ├───docs
	│ │ └───src
	│ ├───Trie
	│ │ ├───.idea
	│ │ ├───docs
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Trie
	│ │ └───src
	│ └───UnionFind
	│ ├───.idea
	│ ├───docs
	│ └───src
	└───out
	└───production
	├───BinarySearch
	├───BinarySearchTree
	├───EditDistance
	├───FastIO
	├───LevenshteinDP
	├───MathFunctions
	├───Palindrome
	├───PrimeNumber
	├───ShortestPath
	├───Sort
	└───Trie
	PS C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library> tree /F
	Folder PATH listing for volume TIH0771000A
	Volume serial number is 56A7-A524
	C:.
	│ .gitignore
	│ GuideTemplate.md
	│ README.md
	│
	├───.github
	│ │ copilot-instructions.md
	│ │ copilot.yml
	│ │ ISSUE_TEMPLATE.md
	│ │
	│ ├───ISSUE_TEMPLATE
	│ │ bug_report.md
	│ │ config.yml
	│ │ docs_change.md
	│ │ feature_request.md
	│ │ performance_improvement.md
	│ │ refactoring.md
	│ │ style_change.md
	│ │ test_update.md
	│ │
	│ └───PULL_REQUEST_TEMPLATE
	│ bug_report.md
	│ docs_change.md
	│ feature_request.md
	│ performance_improvement.md
	│ refactoring.md
	│ style_change.md
	│ test_update.md
	│
	├───.idea
	│ │ competitive-programming-java-library.iml
	│ │ git_toolbox_prj.xml
	│ │ misc.xml
	│ │ modules.xml
	│ │ vcs.xml
	│ │ workspace.xml
	│ │
	│ └───inspectionProfiles
	│ Project_Default.xml
	│
	├───Algorithms
	│ ├───.idea
	│ │ Algorithms.iml
	│ │ misc.xml
	│ │ modules.xml
	│ │ vcs.xml
	│ │ workspace.xml
	│ │
	│ ├───BinarySearch
	│ │ │ BinarySearch.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───docs
	│ │ │ ArrayBinarySearchGuide.md
	│ │ │ BinarySearchGuide.md
	│ │ │
	│ │ └───src
	│ │ ArrayBinarySearch.java
	│ │ BinarySearch.java
	│ │ Example.java
	│ │
	│ ├───EditDistance
	│ │ │ EditDistance.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───LevenshteinDP
	│ │ │ Example.class
	│ │ │ LevenshteinDP.class
	│ │ │
	│ │ └───src
	│ │ Example.java
	│ │ LevenshteinDP.java
	│ │
	│ ├───Factorial
	│ │ │ Factorial.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Factorial
	│ │ │ Example.class
	│ │ │ FactorialUtils.class
	│ │ │ PreComputedFactorials.class
	│ │ │
	│ │ └───src
	│ │ Example.java
	│ │ FactorialUtils.java
	│ │ PreComputedFactorials.java
	│ │
	│ ├───Master
	│ │ │ Master.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Master
	│ │ │ Example.class
	│ │ │ Master.class
	│ │ │
	│ │ └───src
	│ │ Advance.java
	│ │ Example.java
	│ │ Master.java
	│ │
	│ ├───MathFunctions
	│ │ │ MathFunctions.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ CombinatoricsUtilsGuide.md
	│ │ │ GeometryUtilsGuide.md
	│ │ │
	│ │ └───src
	│ │ CombinatoricsUtils.java
	│ │ DivisionUtils.java
	│ │ Example.java
	│ │ GeometryUtils.java
	│ │ NumberFormatUtils.java
	│ │ NumberPredicates.java
	│ │ NumberTheoryUtils.java
	│ │ PolynomialUtils.java
	│ │ PowerUtils.java
	│ │
	│ ├───MinimumSpaningTree
	│ │ │ MinimumSpaningTree.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ │ .gitignore
	│ │ │ │ misc.xml
	│ │ │ │ modules.xml
	│ │ │ │ vcs.xml
	│ │ │ │ workspace.xml
	│ │ │ │
	│ │ │ └───inspectionProfiles
	│ │ │ Project_Default.xml
	│ │ │
	│ │ ├───docs
	│ │ │ KruskalGuide.md
	│ │ │ PrimGuide.md
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───MinimumSpaningTree
	│ │ │ Example.class
	│ │ │ Kruskal$Edge.class
	│ │ │ Kruskal$UnionFind.class
	│ │ │ Kruskal.class
	│ │ │ Prim$Edge.class
	│ │ │ Prim.class
	│ │ │
	│ │ └───src
	│ │ Edmonds.java
	│ │ Example.java
	│ │ Kruskal.java
	│ │ Prim.java
	│ │
	│ ├───Palindrome
	│ │ │ Palindrome.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ ManacherGuide.md
	│ │ │ PalindromeUtilsGuide.md
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Palindrome
	│ │ │ Example.class
	│ │ │ Manacher.class
	│ │ │ PalindromeUtils.class
	│ │ │
	│ │ └───src
	│ │ Example.java
	│ │ Manacher.java
	│ │ PalindromeUtils.java
	│ │
	│ ├───Permutation
	│ │ │ Permutation.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │       
	│ │ ├───docs
	│ │ │ PermutationGuide.md
	│ │ │
	│ │ └───src
	│ │ Permutation.java
	│ │
	│ ├───PrimeNumber
	│ │ │ PrimeNumber.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ PrecomputedPrimesGuide.md
	│ │ │ PrimeUtilsGuide.md
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───PrimeNumber
	│ │ │ Example.class
	│ │ │ PrecomputedPrimes$1.class
	│ │ │ PrecomputedPrimes.class
	│ │ │ PrimeUtils.class
	│ │ │
	│ │ └───src
	│ │ Example.java
	│ │ FastPrecomputedPrimes.java
	│ │ PrecomputedPrimes.java
	│ │ PrimeUtils.java
	│ │
	│ ├───ShortestPath
	│ │ │ README.md
	│ │ │ ShortestPath.iml
	│ │ │
	│ │ ├───docs
	│ │ │ BellmanFordGuide.md
	│ │ │ DijkstraGuide.md
	│ │ │ WarshallfroydGuide.md
	│ │ │
	│ │ └───src
	│ │ BellmanFord.java
	│ │ Dijkstra.java
	│ │ Example.java
	│ │ Warshallfroyd.java
	│ │
	│ ├───Sort
	│ │ │ README.md
	│ │ │ Sort.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ BubbleSort.md
	│ │ │ CombSort.md
	│ │ │ GnomeSort.md
	│ │ │ InsertionSort.md
	│ │ │ MergeSort.md
	│ │ │ Odd_EvenSort.md
	│ │ │ QuickSort.md
	│ │ │ SelectionSort.md
	│ │ │ ShakerSort.md
	│ │ │ ShellSort.md
	│ │ │ SortAlgorithmsTemplate.md
	│ │ │
	│ │ └───src
	│ │ BubbleSort.java
	│ │ CombSort.java
	│ │ Example.java
	│ │ GnomeSort.java
	│ │ HeapSort.java
	│ │ InsertionSort.java
	│ │ MergeSort.java
	│ │ Odd_EvenSort.java
	│ │ QuickSort.java
	│ │ SelectionSort.java
	│ │ ShakerSort.java
	│ │ ShellSort.java
	│ │
	│ ├───StringSearch
	│ │ │ README.md
	│ │ │ StringSearch.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ └───src
	│ │ BM.java
	│ │ KMP.java
	│ │ RollingHash.java
	│ │ ZAlgorithm.java
	│ │
	│ ├───TernarySearch
	│ │ └───src
	│ └───UnimodalUtils
	│ │ README.md
	│ │ UnimodalUtils.iml
	│ │
	│ ├───.idea
	│ │ misc.xml
	│ │ modules.xml
	│ │ vcs.xml
	│ │ workspace.xml
	│ │
	│ └───src
	│ Example.java
	│ GoldenSectionSearch.java
	│ TernarySearch.java
	│
	├───Data Structures
	│ ├───ArrayUtils
	│ │ │ .gitignore
	│ │ │ ArrayUtils.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───ArrayUtils
	│ │ │ Array1D$1.class
	│ │ │ Array1D.class
	│ │ │ Array2D$1.class
	│ │ │ Array2D.class
	│ │ │ Example.class
	│ │ │
	│ │ └───src
	│ │ Array1D.java
	│ │ Array2D.java
	│ │ Example.java
	│ │
	│ ├───BinaryIndexedTree
	│ │ │ .gitignore
	│ │ │ BinaryIndexedTree.iml
	│ │ │ README.md
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ BITGuide.md
	│ │ │
	│ │ └───src
	│ │ BIT.java
	│ │ Example.java
	│ │
	│ ├───BinarySearchTree
	│ │ │ .gitignore
	│ │ │ BinarySearchTree.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ AVLGuide.md
	│ │ │ BinarySearchTreeGuide.md
	│ │ │ BTreeGuide.md
	│ │ │ RedBlackTreeGuide.md
	│ │ │
	│ │ └───src
	│ │ AVLSet.java
	│ │ BinarySearchTree.java
	│ │ BTree.java
	│ │ Example.java
	│ │ RedBlackTree.java
	│ │
	│ ├───FastIO
	│ │ │ FastIO.iml
	│ │ │
	│ │ ├───docs
	│ │ │ ContestPrinterGuide.md
	│ │ │ ContestScannerGuide.md
	│ │ │ FastPrinterGuide.md
	│ │ │ FastScannerGuide.md
	│ │ │
	│ │ └───src
	│ │ CompressedFastPrinter.java
	│ │ CompressedFastScanner.java
	│ │ ContestPrinter.java
	│ │ ContestScanner.java
	│ │ Example.java
	│ │ FastPrinter.java
	│ │ FastScanner.java
	│ │
	│ ├───RingBuffer
	│ │ │ .gitignore
	│ │ │ RingBuffer.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ IntegerRingBufferGuide.md
	│ │ │ LongRingBufferGuide.md
	│ │ │ RingBufferGuide.md
	│ │ │
	│ │ └───src
	│ │ IntegerRingBuffer.java
	│ │ LongRingBuffer.java
	│ │ RingBuffer.java
	│ │
	│ ├───SegmentTree
	│ │ │ .gitignore
	│ │ │ SegmentTree.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ LazySegmentTreeGuide.md
	│ │ │ SegmentTreeGuide.md
	│ │ │
	│ │ └───src
	│ │ Example.java
	│ │ LazySegmentTree.java
	│ │ SegmentTree.java
	│ │
	│ ├───SparseTable
	│ │ │ .gitignore
	│ │ │ SparseTable.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ SparseTableGuide.md
	│ │ │
	│ │ └───src
	│ │ Example.java
	│ │ SparseTable.java
	│ │
	│ ├───Trie
	│ │ │ .gitignore
	│ │ │ Trie.iml
	│ │ │
	│ │ ├───.idea
	│ │ │ .gitignore
	│ │ │ misc.xml
	│ │ │ modules.xml
	│ │ │ vcs.xml
	│ │ │ workspace.xml
	│ │ │
	│ │ ├───docs
	│ │ │ guide.md
	│ │ │
	│ │ ├───out
	│ │ │ └───production
	│ │ │ └───Trie
	│ │ │ CompactTrie.class
	│ │ │ DoubleArrayTrie.class
	│ │ │ Example.class
	│ │ │ PatriciaTree.class
	│ │ │ SuffixArray.class
	│ │ │ SuffixTree.class
	│ │ │ TernarySearchTree.class
	│ │ │ Trie$TrieNode.class
	│ │ │ Trie.class
	│ │ │
	│ │ └───src
	│ │ CompactTrie.java
	│ │ DoubleArrayTrie.java
	│ │ Example.java
	│ │ PatriciaTrie.java
	│ │ RadixTrie.java
	│ │ SuffixArray.java
	│ │ SuffixTree.java
	│ │ TernarySearchTree.java
	│ │ Trie.java
	│ │
	│ └───UnionFind
	│ │ .gitignore
	│ │ UnionFind.iml
	│ │
	│ ├───.idea
	│ │ .gitignore
	│ │ misc.xml
	│ │ modules.xml
	│ │ vcs.xml
	│ │ workspace.xml
	│ │
	│ ├───docs
	│ │ UnionFindGuide.md
	│ │
	│ └───src
	│ UnionFind.java
	│
	└───out
	└───production
	├───BinarySearch
	│ ArrayBinarySearch$1.class
	│ ArrayBinarySearch$BSException$ErrorType.class
	│ ArrayBinarySearch$BSException.class
	│ ArrayBinarySearch$SearchType.class
	│ ArrayBinarySearch.class
	│ BinarySearch$1.class
	│ BinarySearch$BSException$ErrorType.class
	│ BinarySearch$BSException.class
	│ BinarySearch$CompareFunction.class
	│ BinarySearch$SearchType.class
	│ BinarySearch.class
	│ Example.class
	│
	├───BinarySearchTree
	│ AVLSet$Node.class
	│ AVLSet.class
	│ BinarySearchTree.class
	│ BTree.class
	│ Example.class
	│ RedBlackTree.class
	│
	├───EditDistance
	│ Example.class
	│ LevenshteinDP.class
	│
	├───Factorial
	│ Example.class
	│ FactorialUtils.class
	│ PreComputedFactorials.class
	│
	├───FastIO
	│ CompressedFastPrinter$FastPrinter.class
	│ CompressedFastPrinter.class
	│ CompressedFastScanner$FastScanner.class
	│ CompressedFastScanner.class
	│ ContestPrinter.class
	│ ContestScanner.class
	│ Example.class
	│ FastPrinter.class
	│ FastScanner.class
	│
	├───LevenshteinDP
	│ Example.class
	│ LevenshteinDP.class
	│
	├───MathFunctions
	│ CombinatoricsUtils.class
	│ DivisionUtils.class
	│ Example.class
	│ GeometryUtils.class
	│ NumberFormatUtils.class
	│ NumberPredicates.class
	│ NumberTheoryUtils.class
	│ PolynomialUtils.class
	│ PowerUtils.class
	│
	├───Palindrome
	│ Example.class
	│ Manacher.class
	│ PalindromeUtils.class
	│
	├───PrimeNumber
	│ Example.class
	│ FastPrecomputedPrimes$1.class
	│ FastPrecomputedPrimes.class
	│ PrecomputedPrimes$1.class
	│ PrecomputedPrimes.class
	│ PrimeUtils.class
	│
	├───ShortestPath
	│ BellmanFord$Edge.class
	│ BellmanFord.class
	│ Dijkstra$Edge.class
	│ Dijkstra$Vertex.class
	│ Dijkstra.class
	│ Example.class
	│ Warshallfroyd.class
	│
	├───Sort
	│ BubbleSort.class
	│ CombSort.class
	│ Example.class
	│ GnomeSort.class
	│ HeapSort.class
	│ InsertionSort.class
	│ MergeSort.class
	│ Odd_EvenSort.class
	│ QuickSort.class
	│ SelectionSort.class
	│ ShakerSort.class
	│ ShellSort.class
	│
	└───Trie
	CompactTrie.class
	DoubleArrayTrie.class
	Example.class
	PatriciaTrie$TrieNode.class
	PatriciaTrie.class
	SuffixArray.class
	SuffixTree.class
	TernarySearchTree.class
	Trie.class

## JDKバージョン

このプロジェクトは **JDK 17** を使用して開発を行います。

- JDK 17の最新機能を活用し、コードの最適化や安全性の向上を図ること。
- IntelliJ IDEAの設定やビルドツール（例: Maven, Gradleなど）がJDK 17に対応していることを確認すること。
- 環境変数およびIDEの構成でJDK 17が優先されるように設定してください。

<!-- jdk1hsjava17 のラベルが付与されています。プロジェクトの全コードはJDK 17の基準に従って作成すること。 -->