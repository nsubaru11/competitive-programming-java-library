package patterns.math;

import java.math.*;
import java.util.*;

/**
 * 分数を表すクラス。 四則演算、比較、変換などの操作を提供します。
 */
public class BigFraction extends Number implements Comparable<BigFraction> {
	public static final BigFraction ZERO = new BigFraction(0);
	public static final BigFraction ONE = new BigFraction(1);
	public static final BigFraction TWO = new BigFraction(2);
	public static final BigFraction THREE = new BigFraction(3);
	public static final BigFraction ONEHALF = new BigFraction(1, 2);
	public static final BigFraction ONETHIRD = new BigFraction(1, 3);
	private static final long serialVersionUID = -7805459192748824132L;
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	private BigInteger numer;
	private BigInteger denom;

	// 1. コンストラクタ, valueOf

	/**
	 * 分子と分母を指定して分数を作成します。 分母に0を指定すると例外が発生します。
	 *
	 * @param n 分子
	 * @param d 分母 (0不可)
	 * @throws ArithmeticException 分母が0の場合
	 */
	public BigFraction(long n, long d) {
		this(BigInteger.valueOf(n), BigInteger.valueOf(d));
	}

	/**
	 * 整数を分子とし、分母を1とする分数を作成します。
	 *
	 * @param l 整数
	 */
	public BigFraction(long l) {
		this(BigInteger.valueOf(l));
	}

	/**
	 * 実数の分子と分母から分数を作成します。
	 *
	 * @param n 分子（double）
	 * @param d 分母（double）
	 */
	public BigFraction(double n, double d) {
		this(BigDecimal.valueOf(n), BigDecimal.valueOf(d));
	}

	/**
	 * 実数を最も近い分数に変換して作成します。
	 *
	 * @param d 実数
	 */
	public BigFraction(double d) {
		this(BigDecimal.valueOf(d));
	}

	/**
	 * 分子と分母を指定して分数を作成します。 分母に0を指定すると例外が発生します。
	 *
	 * @param n 分子
	 * @param d 分母 (0不可)
	 * @throws ArithmeticException 分母が0の場合
	 */
	public BigFraction(BigInteger n, BigInteger d) {
		if (d.equals(BigInteger.ZERO))
			throw new ArithmeticException("/ by zero");
		numer = n;
		denom = d;
		reduceFraction();
	}

	/**
	 * 整数を分子とし、分母を1とする分数を作成します。
	 *
	 * @param bi 整数(BigInteger)
	 */
	public BigFraction(BigInteger bi) {
		this(bi, BigInteger.ONE);
	}

	/**
	 * 実数の分子と分母から分数を作成します。
	 *
	 * @param n 分子（BigDecimal）
	 * @param d 分母（BigDecimal）
	 */
	public BigFraction(BigDecimal n, BigDecimal d) {
		if (d.equals(BigDecimal.ZERO))
			throw new ArithmeticException("/ by zero");
		int scale = Math.max(n.scale(), d.scale());
		numer = n.movePointRight(scale).toBigInteger();
		denom = d.movePointRight(scale).toBigInteger();
		reduceFraction();
	}

	/**
	 * 実数を最も近い分数に変換して作成します。
	 *
	 * @param bd 実数
	 */
	public BigFraction(BigDecimal bd) {
		int scale = bd.scale();
		numer = bd.movePointRight(scale).toBigInteger();
		denom = BigInteger.TEN.pow(scale);
		reduceFraction();
	}

	/**
	 * 既存の分数オブジェクトをコピーして新しい分数を作成します。
	 *
	 * @param f コピー元の分数
	 */
	public BigFraction(BigFraction f) {
		this(f.numer, f.denom);
	}

	/**
	 * 分子と分母を指定して新しいFractionを作成します。
	 *
	 * @param n 分子
	 * @param d 分母（0不可）
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(long n, long d) {
		return new BigFraction(n, d);
	}

	/**
	 * 整数を分子とし、分母を1とするFractionを作成します。
	 *
	 * @param l 整数
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(long l) {
		return new BigFraction(l);
	}

	/**
	 * 実数の分子と分母から分数を作成します。
	 *
	 * @param n 分子（double）
	 * @param d 分母（double）
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(double n, double d) {
		return new BigFraction(n, d);
	}

	/**
	 * 実数を最も近いFractionに変換します。
	 *
	 * @param d 実数
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(double d) {
		return new BigFraction(d);
	}

	/**
	 * 分子と分母を指定して新しいFractionを作成します。
	 *
	 * @param n 分子
	 * @param d 分母（0不可）
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(BigInteger n, BigInteger d) {
		return new BigFraction(n, d);
	}

	/**
	 * 整数を分子とし、分母を1とするFractionを作成します。
	 *
	 * @param bi 整数
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(BigInteger bi) {
		return new BigFraction(bi);
	}

	/**
	 * 実数の分子と分母から分数を作成します。
	 *
	 * @param n 分子（BigDecimal）
	 * @param d 分母（BigDecimal）
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(BigDecimal n, BigDecimal d) {
		return new BigFraction(n, d);
	}

	/**
	 * 実数を最も近いFractionに変換します。
	 *
	 * @param bd 実数
	 * @return 作成されたFraction
	 */
	public static BigFraction valueOf(BigDecimal bd) {
		return new BigFraction(bd);
	}

	// 2. 値の取得

	/**
	 * 指定した文字列を分数に変換します。 文字列は"実数 / 実数"で表せることを望みます。
	 *
	 * @param s パースする文字列
	 * @return パース後のFraction
	 */
	public static BigFraction parseFraction(String s) {
		s = s.replaceAll(" ", "");
		if (s.isEmpty() || s.equals("/") || s.startsWith("/") || s.endsWith("/"))
			throw new IllegalArgumentException("無効な分数表記: '" + s + "' (例: '3/4' の形式で入力してください)");
		int pos = s.indexOf("/");
		if (pos == -1) {
			return new BigFraction(new BigDecimal(s));
		}
		BigDecimal numer = new BigDecimal(s.substring(0, pos));
		BigDecimal denom = new BigDecimal(s.substring(pos + 1));
		return new BigFraction(numer, denom);
	}

	/**
	 * 分子を取得します。
	 *
	 * @return 分子
	 */
	public BigInteger numerator() {
		return numer;
	}

	// 3. 四則演算, 累乗

	/**
	 * 分母を取得します。
	 *
	 * @return 分母
	 */
	public BigInteger denominator() {
		return denom;
	}

	/**
	 * 整数を加算した新しいFractionを返します。
	 *
	 * @param l 加算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction add(long l) {
		return add(BigFraction.valueOf(l));
	}

	/**
	 * 実数を加算した新しいFractionを返します。
	 *
	 * @param d 加算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction add(double d) {
		return add(BigFraction.valueOf(d));
	}

	/**
	 * 整数を加算した新しいFractionを返します。
	 *
	 * @param bi 加算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction add(BigInteger bi) {
		return add(BigFraction.valueOf(bi));
	}

	/**
	 * 実数を加算した新しいFractionを返します。
	 *
	 * @param bd 加算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction add(BigDecimal bd) {
		return add(BigFraction.valueOf(bd));
	}

	/**
	 * 分数を加算した新しいFractionを返します。
	 *
	 * @param f 加算する分数
	 * @return 計算後のFraction
	 */
	public BigFraction add(BigFraction f) {
		BigInteger n = numer.multiply(f.denom).add(f.numer.multiply(denom));
		BigInteger d = denom.multiply(f.denom);
		return new BigFraction(n, d);
	}

	/**
	 * 整数を減算した新しいFractionを返します。
	 *
	 * @param l 減算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction sub(long l) {
		return sub(BigFraction.valueOf(l));
	}

	/**
	 * 実数を減算した新しいFractionを返します。
	 *
	 * @param d 減算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction sub(double d) {
		return sub(BigFraction.valueOf(d));
	}

	/**
	 * 整数を減算した新しいFractionを返します。
	 *
	 * @param bi 減算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction sub(BigInteger bi) {
		return sub(BigFraction.valueOf(bi));
	}

	/**
	 * 実数を減算した新しいFractionを返します。
	 *
	 * @param bd 減算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction sub(BigDecimal bd) {
		return sub(BigFraction.valueOf(bd));
	}

	/**
	 * 分数を減算した新しいFractionを返します。
	 *
	 * @param f 減算する分数
	 * @return 計算後のFraction
	 */
	public BigFraction sub(BigFraction f) {
		BigInteger n = numer.multiply(f.denom).subtract(f.numer.multiply(denom));
		BigInteger d = denom.multiply(f.denom);
		return new BigFraction(n, d);
	}

	/**
	 * 整数を乗算した新しいFractionを返します。
	 *
	 * @param l 乗算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction mul(long l) {
		return mul(BigFraction.valueOf(l));
	}

	/**
	 * 実数を乗算した新しいFractionを返します。
	 *
	 * @param d 乗算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction mul(double d) {
		return mul(BigFraction.valueOf(d));
	}

	/**
	 * 整数を乗算した新しいFractionを返します。
	 *
	 * @param bi 乗算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction mul(BigInteger bi) {
		return mul(BigFraction.valueOf(bi));
	}

	/**
	 * 実数を乗算した新しいFractionを返します。
	 *
	 * @param bd 乗算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction mul(BigDecimal bd) {
		return mul(BigFraction.valueOf(bd));
	}

	/**
	 * 分数を乗算した新しいFractionを返します。
	 *
	 * @param f 乗算する分数
	 * @return 計算後のFraction
	 */
	public BigFraction mul(BigFraction f) {
		BigInteger n = numer.multiply(f.numer);
		BigInteger d = denom.multiply(f.denom);
		return new BigFraction(n, d);
	}

	/**
	 * 整数を除算した新しいFractionを返します。
	 *
	 * @param l 除算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction div(long l) {
		return div(BigFraction.valueOf(l));
	}

	/**
	 * 実数を除算した新しいFractionを返します。
	 *
	 * @param d 除算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction div(double d) {
		return div(BigFraction.valueOf(d));
	}

	/**
	 * 整数を除算した新しいFractionを返します。
	 *
	 * @param bi 除算する整数
	 * @return 計算後のFraction
	 */
	public BigFraction div(BigInteger bi) {
		return div(BigFraction.valueOf(bi));
	}

	/**
	 * 実数を除算した新しいFractionを返します。
	 *
	 * @param bd 除算する実数
	 * @return 計算後のFraction
	 */
	public BigFraction div(BigDecimal bd) {
		return div(BigFraction.valueOf(bd));
	}

	/**
	 * 分数を除算した新しいFractionを返します。
	 *
	 * @param f 除算する分数
	 * @return 計算後のFraction
	 */
	public BigFraction div(BigFraction f) {
		BigInteger n = numer.multiply(f.denom);
		BigInteger d = denom.multiply(f.numer);
		return new BigFraction(n, d);
	}

	// 4. 比較, 判定

	/**
	 * 分数をi乗した値を返します。
	 *
	 * @param i 指数部分
	 * @return 分数
	 */
	public BigFraction pow(int i) {
		BigInteger n = numer.pow(i);
		BigInteger d = denom.pow(i);
		return new BigFraction(n, d);
	}

	/**
	 * この分数と指定された分数を比較します。
	 *
	 * @param f 比較対象の分数
	 * @return この分数が f より小さい場合は負、等しい場合は 0、大きい場合は正を返す
	 */
	public int compareTo(BigFraction f) {
		return numer.multiply(f.denom).compareTo(f.numer.multiply(denom));
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof BigFraction))
			return false;
		BigFraction other = (BigFraction) obj;
		return numer.equals(other.numer) && denom.equals(other.denom);
	}

	public int hashCode() {
		return Objects.hash(numer, denom);
	}

	/**
	 * 0かどうか返します。
	 *
	 * @return 0ならtrue、そうでなければfalse
	 */
	public boolean isZero() {
		return equals(ZERO);
	}

	/**
	 * 1かどうか返します。
	 *
	 * @return 1ならtrue、そうでなければfalse
	 */
	public boolean isOne() {
		return equals(ONE);
	}

	/**
	 * 整数かどうか返します。
	 *
	 * @return 整数ならtrue、そうでなければfalse
	 */
	public boolean isInteger() {
		return denom.equals(BigInteger.ONE);
	}

	/**
	 * 真分数をかどうか判定します。
	 *
	 * @return 真分数ならtrue、そうでなければfalse
	 */
	public boolean isProper() {
		return numer.abs().compareTo(denom) < 0;
	}

	// 6. 整数, 小数変換

	/**
	 * 仮分数をかどうか判定します。
	 *
	 * @return 仮分数ならtrue、そうでなければfalse
	 */
	public boolean isImproper() {
		return numer.abs().compareTo(denom) >= 0;
	}

	/**
	 * 指定した分数の逆数を取得します。
	 *
	 * @return 逆数のFraction
	 */
	public BigFraction inverse() {
		return new BigFraction(denom, numer);
	}

	/**
	 * 符号を反転したFractionを返します。
	 *
	 * @return 反転後のFraction
	 */
	public BigFraction negate() {
		return new BigFraction(numer.negate(), denom);
	}

	/**
	 * 分数の小数部分（真分数部分）を取得します。
	 *
	 * @return 真分数部分のFraction
	 */
	public BigFraction fractionPart() {
		return new BigFraction(numer.abs().mod(denom), denom);
	}

	/**
	 * 分数の整数部分を取得します。
	 *
	 * @return 整数部分
	 */
	public BigInteger integerPart() {
		return numer.divide(denom);
	}

	/**
	 * この値以下で最大の整数を取得します。
	 *
	 * @return 整数
	 */
	public BigInteger floor() {
		return numer.signum() < 0 ? numer.subtract(denom).add(BigInteger.ONE).divide(denom) : numer.divide(denom);
	}

	/**
	 * この値以上で最小の整数を取得します。
	 *
	 * @return 整数
	 */
	public BigInteger ceil() {
		return numer.signum() > 0 ? numer.subtract(denom).subtract(BigInteger.ONE).divide(denom) : numer.divide(denom);
	}

	/**
	 * 分数を小数第一位で四捨五入した整数を取得します。
	 *
	 * @return 整数
	 */
	public BigDecimal round(int n) {
		return getDecimal().setScale(n, RoundingMode.HALF_UP);
	}

	/**
	 * 分数を小数にして返します。
	 *
	 * @return 小数
	 */
	public BigDecimal getDecimal() {
		return new BigDecimal(numer).divide(new BigDecimal(denom));
	}

	// 7. 文字列変換
	public String toString() {
		return isInteger() ? numer.toString() : numer + "/" + denom;
	}

	/**
	 * 帯分数表記で返します。
	 *
	 * @return 帯分数の文字列
	 */
	public String toMixedString() {
		return isInteger() ? numer.toString() : integerPart() + " " + fractionPart();
	}

	/**
	 * 分数を小数表記にして返します。
	 *
	 * @param precision 小数点下位桁数
	 * @return 小数表記
	 */
	public String toDecimalString(int precision) {
		return String.format("%." + precision + "f", getDecimal());
	}

	// 8. staticユーティリティ

	/**
	 * パーセンテージ表記で返します。
	 *
	 * @param precision 小数点下位桁数
	 * @return パーセンテージ表記文字列
	 */
	public String toPercent(int precision) {
		return String.format("%." + precision + "f%%", getDecimal().multiply(HUNDRED));
	}

	/**
	 * この分数と指定された分数のうち小さい方を返します。
	 *
	 * @param f 比較対象の分数
	 * @return 小さい方の分数
	 */
	public BigFraction min(BigFraction f) {
		return compareTo(f) <= 0 ? copy() : f.copy();
	}

	/**
	 * この分数と指定された分数のうち大きい方を返します。
	 *
	 * @param f 比較対象の分数
	 * @return 大きい方の分数
	 */
	public BigFraction max(BigFraction f) {
		return compareTo(f) >= 0 ? copy() : f.copy();
	}

	/**
	 * 指定した分数の絶対値を返します。
	 *
	 * @param f 分数
	 * @return 指定した分数の絶対値
	 */
	public BigFraction abs() {
		return new BigFraction(numer.abs(), denom);
	}

	public BigFraction copy() {
		return new BigFraction(this);
	}

	// 9. 内部処理

	/**
	 * 分数を既約分数にします。
	 */
	private void reduceFraction() {
		if (numer.equals(BigInteger.ZERO)) {
			denom = BigInteger.ONE;
			return;
		}
		BigInteger gcd = numer.gcd(denom);
		numer = numer.divide(gcd);
		denom = denom.divide(gcd);
		if (denom.signum() < 0) {
			numer = numer.negate();
			denom = denom.negate();
		}
	}

	@Override
	public int intValue() {
		return numer.divide(denom).intValue();
	}

	@Override
	public long longValue() {
		return numer.divide(denom).longValue();
	}

	@Override
	public float floatValue() {
		return new BigDecimal(numer).divide(new BigDecimal(denom), RoundingMode.HALF_UP).floatValue();
	}

	@Override
	public double doubleValue() {
		return new BigDecimal(numer).divide(new BigDecimal(denom), RoundingMode.HALF_UP).doubleValue();
	}
}
