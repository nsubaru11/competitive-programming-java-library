package unit.math.linearalgebra;

import java.util.*;

import lib.math.linearalgebra.*;

final class LongMatrixUtilsTest extends MatrixUtilsContract {

	Class<?> utils() {
		return LongMatrixUtils.class;
	}

	// 定数の行列を書き換えないよう、深いコピーを渡す
	Object matrix(final long[][] a) {
		return Arrays.stream(a).map(long[]::clone).toArray(long[][]::new);
	}

	Object vector(final long[] v) {
		return v.clone();
	}
}
