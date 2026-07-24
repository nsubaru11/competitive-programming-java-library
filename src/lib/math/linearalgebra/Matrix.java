package lib.math.linearalgebra;

import java.util.*;

import lib.math.number.Fraction;

@SuppressWarnings("unused")
public final class Matrix {
	// 行列 -> 分数の2次元配列
	// m * n次行列
	private Fraction[][] matrix;
	private int m, n, rank;
	private Fraction determinant;

	Matrix(int[][] A) {
		m = A.length;
		n = A[m - 1].length;
		matrix = new Fraction[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = new Fraction(A[i][j]);
			}
		}
	}

	Matrix(long[][] A) {
		m = A.length;
		n = A[m - 1].length;
		matrix = new Fraction[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = new Fraction(A[i][j]);
			}
		}
	}

	Matrix(Fraction[][] matrix) {
		m = matrix.length;
		n = matrix[m - 1].length;
		this.matrix = matrix;
	}

	private void add(Matrix mat) {
		// 行列の和
		if (m != mat.m || n != mat.n) {
			throw new ArithmeticException("not defined!");
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = matrix[i][j].add(mat.matrix[i][j]);
			}
		}
	}

	private void subtract(Matrix mat) {
		// 行列の差
		if (m != mat.m || n != mat.n) {
			throw new ArithmeticException("not defined!");
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = matrix[i][j].subtract(mat.matrix[i][j]);
			}
		}
	}

	private Matrix multiply(Matrix mat) {
		// 行列の積
		if (n != mat.m) {
			throw new ArithmeticException("not defined!");
		}
		int p = mat.n;
		Matrix AB = new Matrix(new long[m][p]);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < n; k++) {
					Fraction product = matrix[i][k].multiply(mat.matrix[k][j]);
					AB.matrix[i][j] = AB.matrix[i][j].add(product);
				}
			}
		}
		return AB;
	}

	private Matrix multiply(Fraction[][] mat) {
		// 行列の積
		if (n != mat.length) {
			throw new ArithmeticException("not defined!");
		}
		int p = mat[0].length;
		Matrix AB = new Matrix(new long[m][p]);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < n; k++) {
					Fraction product = matrix[i][k].multiply(mat[k][j]);
					AB.matrix[i][j] = AB.matrix[i][j].add(product);
				}
			}
		}
		return AB;
	}

	private Matrix multiply(Fraction k) {
		// スカラー倍
		Matrix kA = new Matrix(new Fraction[m][n]);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				kA.matrix[i][j] = matrix[i][j].multiply(k);
			}
		}
		return kA;
	}

	private Matrix multiply(long k) {
		// スカラー倍
		Matrix kA = new Matrix(new Fraction[m][n]);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				kA.matrix[i][j] = matrix[i][j].multiply(k);
			}
		}
		return kA;
	}

	private Matrix pow(int k) {
		// 行列の累乗
		Matrix ans = identityMat();
		for (Matrix temp = new Matrix(matrix); k > 0; temp = temp.multiply(temp), k >>= 1) {
			if ((k & 1) == 1) ans = ans.multiply(temp);
		}
		return ans;
	}

	private Matrix transpose() {
		// 転置行列
		Matrix mat = new Matrix(new Fraction[n][m]);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				mat.matrix[i][j] = matrix[j][i];
			}
		}
		return mat;
	}

	private Fraction det2D() {
		// 2次の行列式
		if (m == 2 && n == 2) {
			Fraction det = matrix[0][0].multiply(matrix[1][1]);
			determinant = det.subtract(matrix[0][1].multiply(matrix[1][0]));
			return determinant;
		} else {
			throw new ArithmeticException("not 2D!");
		}
	}

	private Fraction det2D_ex() {
		// 2次の拡大係数行列の行列式
		if (m == 2 && n == 3) {
			Fraction det = matrix[0][0].multiply(matrix[1][1]);
			determinant = det.subtract(matrix[0][1].multiply(matrix[1][0]));
			return determinant;
		} else {
			throw new ArithmeticException("not 2D_ex!");
		}
	}

	private Fraction det3D() {
		// 3次の行列式
		if (m == 3 && n == 3) {
			Fraction det = matrix[0][0].multiply(matrix[1][1]).multiply(matrix[2][2]);
			det = det.add(matrix[0][1].multiply(matrix[1][2]).multiply(matrix[2][0]));
			det = det.add(matrix[0][2].multiply(matrix[1][0]).multiply(matrix[2][1]));
			det = det.subtract(matrix[0][0].multiply(matrix[1][2]).multiply(matrix[2][1]));
			det = det.subtract(matrix[0][1].multiply(matrix[1][0]).multiply(matrix[2][2]));
			determinant = det.subtract(matrix[0][2].multiply(matrix[1][1]).multiply(matrix[2][0]));
			return determinant;
		} else {
			throw new ArithmeticException("not 3D!");
		}
	}

	private Fraction det3D_ex() {
		// 3次の拡大係数行列の行列式
		if (m == 3 && n == 4) {
			Fraction det = matrix[0][0].multiply(matrix[1][1]).multiply(matrix[2][2]);
			det = det.add(matrix[0][1].multiply(matrix[1][2]).multiply(matrix[2][0]));
			det = det.add(matrix[0][2].multiply(matrix[1][0]).multiply(matrix[2][1]));
			det = det.subtract(matrix[0][0].multiply(matrix[1][2]).multiply(matrix[2][1]));
			det = det.subtract(matrix[0][1].multiply(matrix[1][0]).multiply(matrix[2][2]));
			determinant = det.subtract(matrix[0][2].multiply(matrix[1][1]).multiply(matrix[2][0]));
			return determinant;
		} else {
			throw new ArithmeticException("not 3D_ex!");
		}
	}

	private Fraction det() {
		// n次の行列式
		if (m == n) {
			if (m == 2) return det2D();
			if (m == 3) return det3D();
			Fraction det = new Fraction(0);
			for (int i = 0; i < m; i++) {
				if (matrix[i][0].equals(new Fraction(0))) continue;
				Fraction[][] minor = new Fraction[m - 1][m - 1];
				for (int k = 0, s = 0; k < m; k++) {
					if (k == i) continue;
					for (int t = 0; t < m - 1; t++) {
						minor[s][t] = matrix[k][t + 1];
					}
					s++;
				}
				Fraction D = matrix[i][0].multiply(LaplaceExpansion(i, m - 1, minor));
				if (i % 2 != 0) {
					det = det.add(D.multiply(-1));
				} else {
					det = det.add(D);
				}
			}
			determinant = det;
			return det;
		} else {
			throw new ArithmeticException("not defined!");
		}
	}

	private Fraction LaplaceExpansion(int i, int m, Fraction[][] matrix) {
		if (m == 3) {
			Matrix mat = new Matrix(matrix);
			return mat.det3D();
		}
		Fraction det = new Fraction(0);
		for (i = 0; i < m; i++) {
			if (matrix[i][0].equals(new Fraction(0))) continue;
			Fraction[][] minor = new Fraction[m - 1][m - 1];
			for (int k = 0, s = 0; k < m; k++) {
				if (k == i) continue;
				for (int t = 0; t < m - 1; t++) {
					minor[s][t] = matrix[k][t + 1];
				}
				s++;
			}
			Fraction D = matrix[i][0].multiply(LaplaceExpansion(i, m - 1, minor));
			if (i % 2 != 0) {
				det = det.add(D.multiply(-1));
			} else {
				det = det.add(D);
			}
		}
		return det;
	}

	private Matrix identityMat() {
		// min(m, n)次の単位行列の作成
		int k = Math.min(m, n);
		int[][] A = new int[k][k];
		for (int i = 0; i < k; i++) {
			A[i][i] = 1;
		}
		Matrix identity = new Matrix(A);
		return identity;
	}

	private Matrix inverse2D() {
		// 2次の逆行列
		if (m == 2 && n == 2) {
			Fraction det = det2D();
			Matrix inverse = new Matrix(new Fraction[2][2]);
			inverse.matrix[0][0] = matrix[1][1];
			inverse.matrix[0][1] = matrix[0][1].multiply(-1);
			inverse.matrix[1][1] = matrix[0][0];
			inverse.matrix[1][0] = matrix[1][0].multiply(-1);
			return inverse.multiply(det.getInverse());
		} else {
			throw new ArithmeticException("not 2D!");
		}
	}

	private Matrix inverse2D_ex() {
		// 2次の拡大係数行列の逆行列
		if (m == 2 && n == 3) {
			Fraction det = det2D_ex();
			Matrix inverse = new Matrix(new Fraction[2][2]);
			inverse.matrix[0][0] = matrix[1][1];
			inverse.matrix[0][1] = matrix[0][1].multiply(-1);
			inverse.matrix[1][1] = matrix[0][0];
			inverse.matrix[1][0] = matrix[1][0].multiply(-1);
			return inverse.multiply(det.getInverse());
		} else {
			throw new ArithmeticException("not 2D!");
		}
	}

	private Matrix inverse() {
		// n次の逆行列
		Matrix inverse = identityMat();
		rank = 0;
		for (int i = 0; i < n; i++) {
			for (int j = i; j < m; j++) {
				if (matrix[j][i].numerator != 0) {
					rank++;
					if (j != i) {
						swapRow(i, j);
						inverse.swapRow(i, j);
					}
					Fraction mul = matrix[i][i].getInverse();
					multiplyRow(i, mul);
					inverse.multiplyRow(i, mul);
					for (int k = 0; k < m; k++) {
						if (k == i) continue;
						long num = matrix[k][i].numerator;
						long den = matrix[k][i].denominator;
						addMultiplyRow(k, i, -num, den);
						inverse.addMultiplyRow(k, i, -num, den);
					}
					break;
				}
			}
		}
		return inverse;
	}

	private Matrix inverse_ex() {
		// n次の拡大係数行列の連立方程式
		rank = 0;
		Matrix inverse = identityMat();
		for (int i = 0; i < n - 1; i++) {
			for (int j = i; j < m; j++) {
				if (matrix[j][i].numerator != 0) {
					rank++;
					if (j != i) {
						swapRow(i, j);
						inverse.swapRow(i, j);
					}
					Fraction mul = matrix[i][i].getInverse();
					multiplyRow(i, mul);
					inverse.multiplyRow(i, mul);
					for (int k = 0; k < m; k++) {
						if (k == i) continue;
						long num = matrix[k][i].numerator;
						long den = matrix[k][i].denominator;
						addMultiplyRow(k, i, -num, den);
						inverse.addMultiplyRow(k, i, -num, den);
					}
					break;
				}
			}
		}
		return inverse;
	}

	private Fraction[] simultaneousEquation() {
		// 拡大係数行列の連立方程式
		rank = 0;
		Matrix inverse = identityMat();
		for (int i = 0; i < n - 1; i++) {
			for (int j = i; j < m; j++) {
				if (matrix[j][i].numerator != 0) {
					rank++;
					if (j != i) {
						swapRow(i, j);
					}
					Fraction mul = matrix[i][i].getInverse();
					multiplyRow(i, mul);
					for (int k = 0; k < m; k++) {
						if (k == i) continue;
						long num = matrix[k][i].numerator;
						long den = matrix[k][i].denominator;
						addMultiplyRow(k, i, -num, den);
					}
					break;
				}
			}
		}
		Fraction[] x = new Fraction[rank];
		for (int i = 0; i < rank; i++) {
			x[i] = matrix[i][n - 1];
		}
		return x;
	}

	private void swapRow(int i, int j) {
		// 行の入れ替え
		for (int idx = 0; idx < n; idx++) {
			Fraction temp = matrix[i][idx];
			matrix[i][idx] = matrix[j][idx];
			matrix[j][idx] = temp;
		}
	}

	private void addMultiplyRow(int i, int j, long k, long l) {
		// 行ｊ を k / l倍して行i に加える
		Fraction f = new Fraction(k, l);
		for (int idx = 0; idx < n; idx++) {
			matrix[i][idx] = matrix[i][idx].add(matrix[j][idx].multiply(f));
		}
	}

	private void addMultiplyRow(int i, int j, Fraction f) {
		// 行ｊ を f 倍して行i に加える
		for (int idx = 0; idx < n; idx++) {
			matrix[i][idx] = matrix[i][idx].add(matrix[j][idx].multiply(f));
		}
	}

	private void multiplyRow(int i, long k, long l) {
		// 行i を k / l倍
		Fraction f = new Fraction(k, l);
		for (int idx = 0; idx < n; idx++) {
			matrix[i][idx] = matrix[i][idx].multiply(f);
		}
	}

	private void multiplyRow(int i, Fraction f) {
		// 行i を f倍
		for (int idx = 0; idx < n; idx++) {
			matrix[i][idx] = matrix[i][idx].multiply(f);
		}
	}

	public boolean equals(Object mat) {
		// 二つの行列が等しいかどうか
		return toString().equals(mat.toString());
	}

	public String toString() {
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < m; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			for (int j = 0; j < n; j++) {
				sj2.add(matrix[i][j].toString());
			}
			sj.add(sj2.toString());
		}
		return sj.toString();
	}

}
