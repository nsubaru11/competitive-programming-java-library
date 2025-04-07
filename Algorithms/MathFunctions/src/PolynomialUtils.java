import static java.lang.Math.pow;

/**
 * 多項式関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public class PolynomialUtils {

	/**
	 * 多項式の加算を行います。
	 *
	 * @param a 1つ目の多項式の係数配列（降べきの順）
	 * @param b 2つ目の多項式の係数配列（降べきの順）
	 * @return 加算結果の多項式の係数配列
	 */
	public static double[] add(double[] a, double[] b) {
		int maxLength = Math.max(a.length, b.length);
		double[] result = new double[maxLength];

		for (int i = 0; i < a.length; i++) {
			result[i] += a[i];
		}

		for (int i = 0; i < b.length; i++) {
			result[i] += b[i];
		}

		return result;
	}

	/**
	 * 多項式の減算を行います。
	 *
	 * @param a 1つ目の多項式の係数配列（降べきの順）
	 * @param b 2つ目の多項式の係数配列（降べきの順）
	 * @return 減算結果の多項式の係数配列
	 */
	public static double[] subtract(double[] a, double[] b) {
		int maxLength = Math.max(a.length, b.length);
		double[] result = new double[maxLength];

		for (int i = 0; i < a.length; i++) {
			result[i] += a[i];
		}

		for (int i = 0; i < b.length; i++) {
			result[i] -= b[i];
		}

		return result;
	}

	/**
	 * 多項式の乗算を行います。
	 *
	 * @param a 1つ目の多項式の係数配列（降べきの順）
	 * @param b 2つ目の多項式の係数配列（降べきの順）
	 * @return 乗算結果の多項式の係数配列
	 */
	public static double[] multiply(double[] a, double[] b) {
		double[] result = new double[a.length + b.length - 1];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				result[i + j] += a[i] * b[j];
			}
		}

		return result;
	}

	/**
	 * 多項式の微分を行います。
	 *
	 * @param a 多項式の係数配列（降べきの順）
	 * @return 微分結果の多項式の係数配列
	 */
	public static double[] differentiate(double[] a) {
		if (a.length <= 1) {
			return new double[0];
		}

		double[] result = new double[a.length - 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = a[i] * (a.length - 1 - i);
		}

		return result;
	}

	/**
	 * 多項式の積分を行います。
	 *
	 * @param a 多項式の係数配列（降べきの順）
	 * @param c 積分定数
	 * @return 積分結果の多項式の係数配列
	 */
	public static double[] integrate(double[] a, double c) {
		double[] result = new double[a.length + 1];
		result[a.length] = c;

		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] / (a.length - i);
		}

		return result;
	}

	/**
	 * 多項式の値を計算します。
	 *
	 * @param a 多項式の係数配列（降べきの順）
	 * @param x 代入する値
	 * @return 多項式の値
	 */
	public static double evaluate(double[] a, double x) {
		double result = 0;
		for (int i = 0; i < a.length; i++) {
			result += a[i] * pow(x, a.length - 1 - i);
		}
		return result;
	}

	/**
	 * 多項式の次数を計算します。
	 *
	 * @param a 多項式の係数配列（降べきの順）
	 * @return 多項式の次数
	 */
	public static int degree(double[] a) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] != 0) {
				return a.length - 1 - i;
			}
		}
		return -1; // 零多項式
	}

	/**
	 * 多項式の最大公約数を計算します。
	 *
	 * @param a 1つ目の多項式の係数配列（降べきの順）
	 * @param b 2つ目の多項式の係数配列（降べきの順）
	 * @return 最大公約数の多項式の係数配列
	 */
	public static double[] gcd(double[] a, double[] b) {
		if (b.length == 0 || (b.length == 1 && b[0] == 0)) {
			return a;
		}

		double[] remainder = new double[a.length];
		System.arraycopy(a, 0, remainder, 0, a.length);

		while (true) {
			int degreeA = degree(remainder);
			int degreeB = degree(b);

			if (degreeB == -1) {
				return remainder;
			}

			if (degreeA < degreeB) {
				double[] temp = remainder;
				remainder = b;
				b = temp;
				continue;
			}

			double factor = remainder[0] / b[0];
			double[] quotient = new double[degreeA - degreeB + 1];
			quotient[0] = factor;

			double[] product = multiply(quotient, b);
			remainder = subtract(remainder, product);
		}
	}
} 