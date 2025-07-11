# BubbleSort

## 概要

`BubbleSort` クラスは、整数の配列を昇順にソートするためのバブルソートアルゴリズムを提供するクラスです。  
基本的なバブルソートおよび改良版のバブルソート機能を備えており、簡単に呼び出すだけで効率よく配列を並べ替えることができます。

## 特徴

- 単純でわかりやすい実装
- 配列がソートされているかを逐次チェックし、早期終了することで効率性を向上
- ソートの範囲を最小限に制限した最適化バージョンを提供
- 静的メソッドのみで提供されているため、インスタンス生成が不要

## メソッド詳細

### basicBubbleSort(int[] arr)

- 標準のバブルソートで配列を昇順にソートするメソッドです。
- ソート完了が内部で判断された時点で処理を早期終了します。

### bubbleSort(int[] arr)

- 交換が発生した最後の位置を記録し、それ以降の位置を処理対象外にします。
- 段階的に処理対象範囲を狭めることで、基本型バブルソートより高い効率を提供します。

## BubbleSortアルゴリズム詳細

バブルソートは、配列の隣接する2つの要素を順次比較して順序が逆の場合に交換する最もシンプルなソートアルゴリズムです。

- **基本的な流れ**:
	1. 左端から順番に隣り合う要素を比較
	2. 順序が誤っていれば、要素を交換
	3. 配列の最後まで到達すると最大の要素が最後尾になる
	4. ソート内部で交換が1度も発生しなかった場合、ソート完了として終了
	5. 完了するまで繰り返す

- **最適化版アルゴリズムの詳細**:
	- 各パスでの最後の交換位置を記録
	- 次回のパスでは、この位置以降はソート済みと判断でき、以降のチェックをスキップすることで処理範囲を縮小

## 注意事項

- バブルソートは非常にシンプルですが、大きなデータセットでは効率が悪化する傾向があります。
- 数千個以上の要素を扱う場合は、他の効率的なアルゴリズム（例えばクイックソートやマージソートなど）の使用を検討する必要があります。

## パフォーマンス特性

- 時間計算量:
	- 最良の場合: O(n)（すでにソート済みの配列）
	- 平均および最悪の場合: O(n²)
- 空間計算量: O(1)（元の配列を除いて追加の空間をほぼ使用しません）