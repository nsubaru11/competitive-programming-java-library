# lib.util.format

数値の表示形式を整えるユーティリティを配置します。

## 実装クラス

### [NumberFormatUtils](../../../src/lib/util/format/NumberFormatUtils.java)

- `formatDouble(double, int)`: 小数点以下の桁数を固定した文字列へ変換
- `toPaddedString(long, int)`: 指定桁数まで先頭を`0`で埋めた文字列へ変換

## 注意事項

- `formatDouble`は現在のロケールに依存しない形式を返します。
- `toPaddedString`は指定桁数より長い値を切り詰めません。
