# MergeSort

`mergeSort(int[] data, int n)`は、`data`の先頭`n`要素を分割統治で昇順に並べ替え、同じ配列を返します。マージ時に等値要素を左側から選ぶため安定です。時間計算量は`O(N log N)`、再帰と部分配列のため追加領域は`O(N)`です。

`n`には`0 <= n <= data.length`を指定します。末尾を含む全体をソートする場合は`mergeSort(data, data.length)`を呼び出してください。
