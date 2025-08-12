# AVLSet と AVLList

## 🎯 設計の狙い

- **AVLSet**: 重複を許可しない順序付きセット（Set）
- **AVLList**: 重複を許可する順序付きリスト（Multiset）
- 競技プログラミングで使いやすい高速な平衡二分探索木を提供
- Collection インターフェースを実装し、標準的な操作をサポート

## 📊 現在の実装状況

### ✅ 実装済み機能
- **基本操作**: `add()`, `remove()`, `size()`, `isEmpty()`, `clear()`
- **AVL平衡**: 自動的な回転による高さ平衡の維持
- **重複管理**: AVLListでは`cnt`で重複数を管理

### ❌ 未実装機能
- `contains()`, `iterator()`, `toArray()` などのCollectionメソッド
- 順序統計（k番目、rank）
- 境界探索（lowerBound, upperBound）
- 個数取得（count）

## 🔧 高速化の工夫

### 1. **メモリ局所性の最適化**
```java
// ノード構造の最適化
private static final class Node<T extends Comparable<T>> {
    private final T label;        // キー（不変）
    private int cnt, size, height; // 4バイト整数をまとめて配置
    private Node<T> left, right;   // ポインタ
    
    // キャッシュライン境界を考慮したフィールド配置
}
```

### 2. **計算量の最適化**
```java
// バランス因子計算の高速化
private int balanceFactor() {
    int l = leftHeight();
    int r = rightHeight();
    return r - l; // 単純な差分で十分
}

// 高さ計算のキャッシュ
private int leftHeight() {
    return left == null ? 0 : left.height;
}
```

### 3. **回転操作の最適化**
```java
// 回転時の不要な更新を削減
private Node<T> rotateLeft(int balance) {
    if (balance == 2) {
        // 単回転：直接的なポインタ操作
        Node<T> newRoot = this.right;
        this.right = newRoot.left;
        newRoot.left = this;
        updateNode(this);
        updateNode(newRoot);
        return newRoot;
    } else {
        // 二重回転：中間ノードを経由
        // 既存実装を維持
    }
}
```

### 4. **削除操作の改善**
```java
// 削除時の successor/predecessor 選択
public Node<T> remove(T value) {
    // ... 探索部分 ...
    if (cmp == 0) {
        if (cnt > 1) {
            cnt--; // 重複削除のみ
            updateNode(this);
            return this;
        }
        // 物理削除：高さに基づく選択
        if (leftHeight() >= rightHeight()) {
            // predecessor を使用（左部分木の最大値）
            Node<T> pred = findMax(left);
            this.label = pred.label;
            this.cnt = pred.cnt;
            left = left.remove(pred.label);
        } else {
            // successor を使用（右部分木の最小値）
            Node<T> succ = findMin(right);
            this.label = succ.label;
            this.cnt = succ.cnt;
            right = right.remove(succ.label);
        }
    }
    // ... 平衡化部分 ...
}
```

## 🚀 実装すべき高速化機能

### 1. **contains() の実装**
```java
public boolean contains(Object o) {
    if (o == null) return false;
    try {
        @SuppressWarnings("unchecked")
        T value = (T) o;
        return containsRecursive(root, value);
    } catch (ClassCastException e) {
        return false;
    }
}

private boolean containsRecursive(Node<T> node, T value) {
    if (node == null) return false;
    int cmp = value.compareTo(node.label);
    if (cmp < 0) return containsRecursive(node.left, value);
    if (cmp > 0) return containsRecursive(node.right, value);
    return true; // 見つかった
}
```

### 2. **順序統計の実装**
```java
// k番目の要素を取得（1-indexed）
public T kth(int k) {
    if (k < 1 || k > size) {
        throw new IndexOutOfBoundsException("k: " + k);
    }
    return kthRecursive(root, k);
}

private T kthRecursive(Node<T> node, int k) {
    int leftSize = (node.left == null) ? 0 : node.left.size;
    if (k <= leftSize) {
        return kthRecursive(node.left, k);
    }
    if (k <= leftSize + node.cnt) {
        return node.label;
    }
    return kthRecursive(node.right, k - leftSize - node.cnt);
}

// rank: xより小さい要素の個数
public int rank(T x) {
    return rankRecursive(root, x);
}

private int rankRecursive(Node<T> node, T x) {
    if (node == null) return 0;
    int cmp = x.compareTo(node.label);
    if (cmp <= 0) {
        return rankRecursive(node.left, x);
    }
    int leftSize = (node.left == null) ? 0 : node.left.size;
    return leftSize + node.cnt + rankRecursive(node.right, x);
}
```

### 3. **境界探索の実装**
```java
// x以上の最小要素
public T lowerBound(T x) {
    Node<T> result = lowerBoundRecursive(root, x);
    return result != null ? result.label : null;
}

private Node<T> lowerBoundRecursive(Node<T> node, T x) {
    if (node == null) return null;
    int cmp = x.compareTo(node.label);
    if (cmp <= 0) {
        Node<T> leftResult = lowerBoundRecursive(node.left, x);
        return leftResult != null ? leftResult : node;
    }
    return lowerBoundRecursive(node.right, x);
}

// xより大きい最小要素
public T upperBound(T x) {
    Node<T> result = upperBoundRecursive(root, x);
    return result != null ? result.label : null;
}

private Node<T> upperBoundRecursive(Node<T> node, T x) {
    if (node == null) return null;
    int cmp = x.compareTo(node.label);
    if (cmp < 0) {
        Node<T> leftResult = upperBoundRecursive(node.left, x);
        return leftResult != null ? leftResult : node;
    }
    return upperBoundRecursive(node.right, x);
}
```

### 4. **Iterator の実装**
```java
public Iterator<T> iterator() {
    return new AVLIterator();
}

private class AVLIterator implements Iterator<T> {
    private final Stack<Node<T>> stack = new Stack<>();
    private Node<T> current;
    private int remaining;
    
    public AVLIterator() {
        pushLeft(root);
        if (!stack.isEmpty()) {
            current = stack.pop();
            remaining = current.cnt;
        }
    }
    
    private void pushLeft(Node<T> node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }
    
    @Override
    public boolean hasNext() {
        return !stack.isEmpty() || (current != null && remaining > 0);
    }
    
    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        
        T result = current.label;
        remaining--;
        
        if (remaining == 0) {
            if (current.right != null) {
                pushLeft(current.right);
            }
            current = stack.isEmpty() ? null : stack.pop();
            if (current != null) {
                remaining = current.cnt;
            }
        }
        
        return result;
    }
}
```

## 📈 パフォーマンス最適化

### 1. **メモリ効率**
- **オブジェクトプール**: 頻繁なノード作成/削除を避ける
- **フィールド配置**: キャッシュライン境界を考慮
- **プリミティブ使用**: 可能な限りint/longを使用

### 2. **計算効率**
- **早期終了**: 不要な計算を避ける
- **キャッシュ活用**: 高さやサイズをキャッシュ
- **分岐予測**: 条件分岐を最適化

### 3. **競技プログラミング向け最適化**
```java
// 高速な比較関数
private static final class FastComparator<T extends Comparable<T>> {
    public static int compare(T a, T b) {
        return a.compareTo(b);
    }
}

// プリミティブ特化版
public static class IntAVLSet {
    private static final class Node {
        private final int label;
        private int cnt, size, height;
        private Node left, right;
        
        // プリミティブ比較で高速化
        private Node add(int value) {
            if (value < label) {
                left = left == null ? new Node(value) : left.add(value);
            } else if (value > label) {
                right = right == null ? new Node(value) : right.add(value);
            } else {
                cnt++;
            }
            // ... 平衡化
        }
    }
}
```

## 🎯 実装優先度

### 高優先度
1. **contains()** - 基本的な検索機能
2. **Iterator** - Collection インターフェースの完全実装
3. **kth() / rank()** - 競技プログラミングで頻出

### 中優先度
1. **lowerBound() / upperBound()** - 境界探索
2. **count()** - 個数取得
3. **toArray()** - 配列変換

### 低優先度
1. **removeAll() / retainAll()** - 集合演算
2. **addAll()** - 一括追加の最適化
3. **逆順Iterator** - 降順走査

## 📝 まとめ

現在の実装は基本的なAVL木の構造は完成しているが、Collection インターフェースの完全実装と競技プログラミングで有用な機能が不足している。上記の高速化手法と実装すべき機能を追加することで、競技プログラミングで実用的な高速な平衡二分探索木ライブラリとなる。