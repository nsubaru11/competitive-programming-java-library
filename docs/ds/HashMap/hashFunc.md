### 1. 64ビット (`long`) ハッシュ関数の検証結果 (ABC455-E)

2変数をパックした `long` キーに対するミキサー関数の比較。

| 方式名                    | 全体時間 (1回目 / 2回目)  | 平均時間 (1回目 / 2回目)      | 評価・特徴                          |
|:-----------------------|:------------------|:----------------------|:-------------------------------|
| **軽量版 SplitMix64**     | **159ms** / 172ms | **101.3ms** / 105.3ms | **最速・最安定。** 撹拌と計算コストのベストバランス。  |
| **Thomas Wang (乗算なし)** | 166ms / 182ms     | 102.9ms / 108.9ms     | ILP（命令レベルの並列性）が活き、乗算ゼロでも高速。    |
| **SplitMix64 (元実装)**   | 177ms / 175ms     | 112.4ms / 110.0ms     | 安定性は高いが、乗算2回のオーバーヘッドがやや重い。     |
| **MurmurHash3 64-bit** | 198ms / 179ms     | 122.0ms / 110.1ms     | 重厚なミキサー。競技プログラミングのパック済みキーには過剰。 |
| **修正版フィボナッチ**          | 170ms / 217ms     | 100.8ms / 124.8ms     | 最速値は出るが、衝突（クラスタリング）によるブレが発生。   |

#### 実装コード群 (64-bit)

```java
// 軽量版 SplitMix64 (乗算1回)
private int hash(long key) {
    key ^= key >>> 33;
    key *= 0xff51afd7ed558ccdL;
    key ^= key >>> 33;
    return (int) key & mask;
}

// Thomas Wang's 64-bit (乗算排除・シフト/XORのみ)
private int hash(long key) {
    key ^= key >>> 21;
    key ^= key << 37;
    key ^= key >>> 28;
    key ^= key << 15;
    key ^= key >>> 32;
    return (int) key & mask;
}

// SplitMix64 (元実装・乗算2回)
private int hash(long key) {
    key ^= key >>> 30;
    key *= 0xbf58476d1ce4e5b9L;
    key ^= key >>> 27;
    key *= 0x94d049bb133111ebL;
    key ^= key >>> 31;
    return (int) key & mask;
}

// MurmurHash3 64-bit Finalizer
private int hash(long key) {
    key ^= key >>> 33;
    key *= 0xff51afd7ed558ccdL;
    key ^= key >>> 33;
    key *= 0xc4ceb9fe1a85ec53L;
    key ^= key >>> 33;
    return (int) key & mask;
}

// 修正版フィボナッチ (乗算1回・シフト1回)
private int hash(long key) {
    key *= 0x9E3779B97F4A7C15L;
    return (int) (key >>> 32) & mask;
}
```

---

### 2. 32ビット (`int`) ハッシュ関数の検証結果 (ABC455-C)

1変数の `int` キーに対するミキサー関数の比較。

| 方式名                     | 全体時間 (1回目 / 2回目)      | 平均時間 (1回目 / 2回目)          | 評価・特徴                              |
|:------------------------|:----------------------|:--------------------------|:-----------------------------------|
| **SplitMix32 Variant**  | **206ms** / **200ms** | **112.3ms** / **113.7ms** | **安定最速。** ハッシュキラーを防ぎつつ無変換を上回る。     |
| **Identity Hash (無変換)** | 207ms / 207ms         | 114.3ms / 114.0ms         | 理論上の最軽量だが、局所的な密集を散らせていない。          |
| **Golden Ratio (乗算1回)** | 200ms / 207ms         | 115.2ms / 116.9ms         | バランスは良いが SplitMix32 Variant に一歩譲る。 |
| **MurmurHash3 32-bit**  | 213ms / 229ms         | 118.0ms / 122.5ms         | 32bit用としてはややオーバースペックで計算コストが響く。     |
| **Thomas Wang 32-bit**  | 208ms / 221ms         | 118.8ms / 121.7ms         | 乗算ゼロだが、シフトと加減算のチェーンが逆にボトルネックに。     |

#### 実装コード群 (32-bit)

```java
// SplitMix32 Variant (非対称定数)
private int hash(int key) {
    key ^= key >>> 16;
    key *= 0x7feb352d;
    key ^= key >>> 15;
    key *= 0x846ca68b;
    key ^= key >>> 16;
    return key & mask;
}

// Identity Hash (無変換)
private int hash(int key) {
    return key & mask;
}

// Golden Ratio Mixer (軽量版フィボナッチ系)
private int hash(int key) {
    key ^= key >>> 16;
    key *= 0x9E3779B9;
    key ^= key >>> 16;
    return key & mask;
}

// MurmurHash3 32-bit Finalizer
private int hash(int key) {
    key ^= key >>> 16;
    key *= 0x85ebca6b;
    key ^= key >>> 13;
    key *= 0xc2b2ae35;
    key ^= key >>> 16;
    return key & mask;
}

// Thomas Wang's 32-bit Hash (乗算排除型)
private int hash(int key) {
    key = (key ^ 61) ^ (key >>> 16);
    key = key + (key << 3);
    key = key ^ (key >>> 4);
    key = key * 0x27d4eb2d;
    key = key ^ (key >>> 15);
    return key & mask;
}
```
