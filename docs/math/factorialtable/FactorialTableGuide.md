# FactorialTable 利用ガイド

> `FactorialTable`は、素数mod上の逆元・階乗・階乗逆元を動的に拡張し、組み合わせ数を定数時間で返すテーブルである。

[← README: FactorialTable](./README.md) · ソース: [FactorialTable.java](../../../src/lib/math/FactorialTable.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

`FactorialTable`は、素数mod上の逆元・階乗・階乗逆元を動的に拡張し、組み合わせ数を定数時間で返すテーブルである。

## 特徴

- 必要な最大添字まで自動拡張
- 既定法`998244353`
- 逆元列挙式`inv[i] = mod - (mod / i) * inv[mod % i] mod mod`を使用
- Lucasの定理を使わない単純な階乗テーブル

## 依存関係

- `java.lang.Math`
- `java.util.Arrays`

## API

| メソッド                         | 戻り値 | 説明                            |
|----------------------------------|--------|---------------------------------|
| `FactorialTable()`               | -      | 最大添字1024、法998244353で作成 |
| `FactorialTable(int n)`          | -      | 最大添字`n`、法998244353で作成  |
| `FactorialTable(int n, int mod)` | -      | 最大添字と素数modを指定         |
| `inv(int n)`                     | `int`  | `n`の乗法逆元                   |
| `fact(int n)`                    | `int`  | `n! mod mod`                    |
| `invFact(int n)`                 | `int`  | `(n!)^-1 mod mod`               |
| `nCr(int n, int r)`              | `int`  | 二項係数                        |
| `nPr(int n, int r)`              | `int`  | 順列数                          |
| `nHr(int n, int r)`              | `int`  | 重複組み合わせ                  |
| `catalan(int n)`                 | `int`  | Catalan数                       |
| `lah(int n, int k)`              | `int`  | Lah数                           |
| `narayana(int n, int k)`         | `int`  | Narayana数                      |
| `ballotTheorem(int n, int k)`    | `int`  | Ballot数／Catalan三角形         |

## 利用例

準備中

## 制約・注意事項

- `mod`は素数を前提とする。
- `inv`は`1 <= n < mod`、`invFact`は`0 <= n < mod`を前提とする。
- `nCr`など逆元を使うメソッドは、参照する最大添字が`mod`未満である必要がある。
- `fact(n)`だけは`n >= mod`で0を返す。
- Lucasの定理は提供しない。
- `catalan(n)`は`2n < mod`を前提とする。

## 性能特性

- 初回拡張: 追加された要素数をmとして$\mathcal{O}(m)$
- 構築済み範囲の各クエリ: $\mathcal{O}(1)$
- 空間: $\mathcal{O}(N)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                              |
|--------------------|------------|---------------------------------------------------|
| **バージョン 1.0** | 2026-08-01 | 動的な逆元・階乗テーブルと組み合わせ数列APIを実装 |
