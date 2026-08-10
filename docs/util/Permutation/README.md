# Permutation

[`Permutation`](../../../src/lib/util/Permutation.java) は、配列の辞書順位置、次の順列、前の順列を扱う配列ユーティリティです。
数学パッケージではなく`lib.util`に配置されています。

- `int`、`long`、`char`の1次元・2次元配列に対応
- `[fromIdx, toIdx)`の範囲指定版を提供
- 重複値を区別しない辞書順index
- `next` / `prev`は配列を直接変更

詳細は[利用ガイド](./PermutationGuide.md)を参照してください。
