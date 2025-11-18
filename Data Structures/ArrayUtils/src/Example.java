public final class Example {

	public static void main(final String[] args) {
		// IntArray1D: 循環シフト + 区間和
		IntArray1D ia = new IntArray1D(5, i -> i + 1); // [1,2,3,4,5]
		ia.lShift(); // 論理的に [2,3,4,5,1]
		System.out.println("IntArray1D sum[0,2] = " + ia.sum(0, 2)); // 2+3+4=9

		// LongArray1D: 部分和（任意個）
		LongArray1D la = new LongArray1D(4, i -> (i * 2L + 1)); // [1,3,5,7]
		System.out.println("LongArray1D subset(target=8) = " + la.subset(8)); // 1+7=8

		// IntArray2D: 回転してアクセス
		IntArray2D gI = new IntArray2D(2, 3, (i, j) -> i * 10 + j); // [[0,1,2],[10,11,12]]
		gI.lRotate(); // 左回転
		System.out.println("IntArray2D get(0,0) after lRotate = " + gI.get(0, 0));

		// LongArray2D: 値の設定と取得
		LongArray2D gL = new LongArray2D(2, 2, (_, _) -> 0L);
		gL.set(1, 1, 42L);
		System.out.println("LongArray2D get(1,1) = " + gL.get(1, 1));
	}
}
