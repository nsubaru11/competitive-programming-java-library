# QuickSort

`quickSort(int[] data, int left, int right)`は、両端を含む区間`[left, right]`を昇順に並べ替えるクイックソートです。入力配列を直接変更し、安定ではありません。平均時間計算量は`O(N log N)`、最悪は`O(N^2)`です。

全体をソートする場合は`quickSort(data, 0, data.length - 1)`を指定します。ピボットは区間中央の値であり、入力分布による性能差があります。
