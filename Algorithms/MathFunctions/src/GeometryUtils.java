import static java.lang.Math.*;

/**
 * 幾何学関連のユーティリティクラス（Geometry Utilities）
 * <p>
 * このクラスは2次元、3次元、およびN次元空間での幾何学的計算を提供します。
 * 直線の交差判定、長方形（直方体）の交差判定、点の包含判定、および
 * 様々な距離計算（ユークリッド距離、マンハッタン距離、チェビシェフ距離）を含みます。
 * <p>
 * すべてのメソッドは静的であり、数値誤差を考慮して実装されています（浮動小数点の比較には1e-10の許容誤差を使用）。
 */
@SuppressWarnings("unused")
public final class GeometryUtils {

	private static final double EPSILON = 1e-10;

	/**
	 * 2次元空間での2つの直線（線分）の交差判定を行います。
	 *
	 * @param x11 1つ目の直線の始点のx座標
	 * @param y11 1つ目の直線の始点のy座標
	 * @param x12 1つ目の直線の終点のx座標
	 * @param y12 1つ目の直線の終点のy座標
	 * @param x21 2つ目の直線の始点のx座標
	 * @param y21 2つ目の直線の始点のy座標
	 * @param x22 2つ目の直線の終点のx座標
	 * @param y22 2つ目の直線の終点のy座標
	 * @return 交差状態を表す整数値：
	 * <ul>
	 * <li>2：垂直に交差している場合</li>
	 * <li>1：交差している場合（垂直でない）</li>
	 * <li>0：直線が完全に等しい場合（平行）</li>
	 * <li>-1：直線の一部分を共有する（平行）</li>
	 * <li>-2：一方の直線の端点ともう一方の直線の端点が接する場合（平行）</li>
	 * <li>-3：一方の直線の端点がもう一方の直線に含まれる場合（垂直）</li>
	 * <li>-4：一方の直線の端点がもう一方の直線に含まれる場合（非平行・非垂直）</li>
	 * <li>-5：交差していない場合（平行）</li>
	 * <li>-6：交差していない場合（垂直）</li>
	 * <li>-7：交差していない場合（非平行・非垂直）</li>
	 * </ul>
	 */
	public static int crossLine(double x11, double y11, double x12, double y12, double x21, double y21, double x22, double y22) {
		double dx1 = x12 - x11, dy1 = y12 - y11;
		double dx2 = x22 - x21, dy2 = y22 - y21;

		double cross = dx1 * dy2 - dy1 * dx2; // 外積
		double inner = dx1 * dx2 + dy1 * dy2; // 内積
		double len1 = sqrt(dx1 * dx1 + dy1 * dy1);
		double len2 = sqrt(dx2 * dx2 + dy2 * dy2);
		if (len1 < EPSILON || len2 < EPSILON) {
			return -7;
		}
		double cos = inner / (len1 * len2);

		boolean isVertical = abs(cos) < EPSILON;
		boolean onLine11 = isPointOnLine(x11, y11, x21, y21, x22, y22);
		boolean onLine12 = isPointOnLine(x12, y12, x21, y21, x22, y22);
		boolean onLine21 = isPointOnLine(x21, y21, x11, y11, x12, y12);
		boolean onLine22 = isPointOnLine(x22, y22, x11, y11, x12, y12);

		if (abs(cross) < EPSILON) { // 平行のとき
			if (onLine21 && onLine22 && onLine11 && onLine12) {
				return 0;
			}
			boolean eqP11P21 = abs(x11 - x21) < EPSILON && abs(y11 - y21) < EPSILON;
			boolean eqP11P22 = abs(x11 - x22) < EPSILON && abs(y11 - y22) < EPSILON;
			boolean eqP12P21 = abs(x12 - x21) < EPSILON && abs(y12 - y21) < EPSILON;
			boolean eqP12P22 = abs(x12 - x22) < EPSILON && abs(y12 - y22) < EPSILON;
			if ((eqP11P21 && !onLine12 && !onLine22) || (eqP11P22 && !onLine12 && !onLine21) || (eqP12P21 && !onLine11 && !onLine22) || (eqP12P22 && !onLine11 && !onLine21)) {
				return -2;
			}
			if (onLine21 || onLine22 || onLine11 || onLine12) {
				return -1;
			}
			return -5;
		}

		double t1 = ((x21 - x11) * dy2 - (y21 - y11) * dx2) / cross;
		double t2 = ((x11 - x21) * dy1 - (y11 - y21) * dx1) / -cross;
		boolean inSegment1 = t1 >= -EPSILON && t1 <= 1 + EPSILON;
		boolean inSegment2 = t2 >= -EPSILON && t2 <= 1 + EPSILON;

		if (inSegment1 && inSegment2) {
			if (onLine11 || onLine12 || onLine21 || onLine22) {
				return isVertical ? -3 : -4;
			} else {
				return isVertical ? 2 : 1;
			}
		}

		return isVertical ? -6 : -7;
	}

	/**
	 * 2次元空間で点が線分上にあるかを判定します。
	 *
	 * @param px 判定する点のx座標
	 * @param py 判定する点のy座標
	 * @param x1 線分の始点のx座標
	 * @param y1 線分の始点のy座標
	 * @param x2 線分の終点のx座標
	 * @param y2 線分の終点のy座標
	 * @return 点が線分上にある場合はtrue、そうでない場合はfalse
	 */
	private static boolean isPointOnLine(double px, double py, double x1, double y1, double x2, double y2) {
		if (x1 == x2 && y1 == y2) {
			return px == x1 && py == y1;
		}
		double dx = x2 - x1;
		double dy = y2 - y1;
		double cross = (px - x1) * dy - (py - y1) * dx; // 外積
		if (abs(cross) > EPSILON) {
			return false;
		}
		if (abs(dx) >= abs(dy)) {
			return dx > 0 ? x1 <= px && px <= x2 : x2 <= px && px <= x1;
		} else {
			return dy > 0 ? y1 <= py && py <= y2 : y2 <= py && py <= y1;
		}
	}

	/**
	 * 3次元空間での2つの直線（線分）の交差判定を行います。
	 *
	 * @param x11 1つ目の直線の始点のx座標
	 * @param y11 1つ目の直線の始点のy座標
	 * @param z11 1つ目の直線の始点のz座標
	 * @param x12 1つ目の直線の終点のx座標
	 * @param y12 1つ目の直線の終点のy座標
	 * @param z12 1つ目の直線の終点のz座標
	 * @param x21 2つ目の直線の始点のx座標
	 * @param y21 2つ目の直線の始点のy座標
	 * @param z21 2つ目の直線の始点のz座標
	 * @param x22 2つ目の直線の終点のx座標
	 * @param y22 2つ目の直線の終点のy座標
	 * @param z22 2つ目の直線の終点のz座標
	 * @return 交差状態を表す整数値：
	 * <ul>
	 * <li>2：垂直に交差している場合</li>
	 * <li>1：交差している場合（垂直でない）</li>
	 * <li>0：直線が完全に等しい場合（平行）</li>
	 * <li>-1：直線の一部分を共有する（平行）</li>
	 * <li>-2：一方の直線の端点ともう一方の直線の端点が接する場合（平行）</li>
	 * <li>-3：一方の直線の端点がもう一方の直線に含まれる場合（垂直）</li>
	 * <li>-4：一方の直線の端点がもう一方の直線に含まれる場合（非平行・非垂直）</li>
	 * <li>-5：交差していない場合（平行）</li>
	 * <li>-6：交差していない場合（垂直、ねじれの位置にある場合を含む）</li>
	 * <li>-7：交差していない場合（非平行・非垂直、ねじれの位置にある場合を含む）</li>
	 * </ul>
	 */
	public static int crossLine3D(double x11, double y11, double z11, double x12, double y12, double z12,
								  double x21, double y21, double z21, double x22, double y22, double z22) {

		double dx1 = x12 - x11, dy1 = y12 - y11, dz1 = z12 - z11;
		double dx2 = x22 - x21, dy2 = y22 - y21, dz2 = z22 - z21;
		double len1 = sqrt(dx1 * dx1 + dy1 * dy1 + dz1 * dz1);
		double len2 = sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2);
		if (len1 < EPSILON || len2 < EPSILON) return -7;

		double inner = dx1 * dx2 + dy1 * dy2 + dz1 * dz2;
		boolean isVertical = abs(inner / (len1 * len2)) < EPSILON;

		double cx = dy1 * dz2 - dz1 * dy2;
		double cy = dz1 * dx2 - dx1 * dz2;
		double cz = dx1 * dy2 - dy1 * dx2;
		double cross = cx * cx + cy * cy + cz * cz;

		if (cross < EPSILON * EPSILON) {
			// 線分1の始点と線分2の始点を結ぶベクトルと、線分1の方向ベクトルの外積を計算し、
			// 2つの線分が同一直線上にあるか (collinear) を判定
			double dx1_21 = x11 - x21, dy1_21 = y11 - y21, dz1_21 = z11 - z21;
			double collinearCheck = (dy1 * dz1_21 - dz1 * dy1_21) * (dy1 * dz1_21 - dz1 * dy1_21) +
					(dz1 * dx1_21 - dx1 * dz1_21) * (dz1 * dx1_21 - dx1 * dx1_21) +
					(dx1 * dy1_21 - dy1 * dx1_21) * (dx1 * dy1_21 - dy1 * dx1_21);

			// 同一直線上にない場合（平行で離れている）
			if (collinearCheck > EPSILON * EPSILON) {
				return -5;
			}

			// 同一直線上にある場合の判定
			boolean onLine11 = isPointOnLine3D(x11, y11, z11, x21, y21, z21, x22, y22, z22);
			boolean onLine12 = isPointOnLine3D(x12, y12, z12, x21, y21, z21, x22, y22, z22);
			boolean onLine21 = isPointOnLine3D(x21, y21, z21, x11, y11, z11, x12, y12, z12);
			boolean onLine22 = isPointOnLine3D(x22, y22, z22, x11, y11, z11, x12, y12, z12);
			boolean eqP11P21 = abs(x11 - x21) < EPSILON && abs(y11 - y21) < EPSILON && abs(z11 - z21) < EPSILON;
			boolean eqP11P22 = abs(x11 - x22) < EPSILON && abs(y11 - y22) < EPSILON && abs(z11 - z22) < EPSILON;
			boolean eqP12P21 = abs(x12 - x21) < EPSILON && abs(y12 - y21) < EPSILON && abs(z12 - z21) < EPSILON;
			boolean eqP12P22 = abs(x12 - x22) < EPSILON && abs(y12 - y22) < EPSILON && abs(z12 - z22) < EPSILON;

			if ((eqP11P21 && eqP12P22) || (eqP11P22 && eqP12P21)) return 0; // 完全に一致
			if ((eqP11P21 && !onLine12 && !onLine22) || (eqP11P22 && !onLine12 && !onLine21) ||
					(eqP12P21 && !onLine11 && !onLine22) || (eqP12P22 && !onLine11 && !onLine21)) return -2; // 端点で接する
			if (onLine11 || onLine12 || onLine21 || onLine22) return -1; // 一部分を共有
			return -5; // 同一直線上だが離れている
		}

		// --- 非平行の場合（交差またはねじれ） ---
		// スカラー三重積を計算して、同一平面上にあるか (coplanar) を判定
		double dx_s = x21 - x11, dy_s = y21 - y11, dz_s = z21 - z11;
		double tripleProduct = dx_s * cx + dy_s * cy + dz_s * cz;

		// 同一平面上にない（ねじれの位置）
		if (abs(tripleProduct) > EPSILON) {
			return isVertical ? -6 : -7;
		}

		// 同一平面上で交差する場合のパラメータを計算
		double t1_num = (dx_s * dy2 - dy_s * dx2) * dz1 - (dx_s * dz2 - dz_s * dx2) * dy1;
		double t1_den = dx1 * (dy2 * dz1 - dz2 * dy1) - dy1 * (dx2 * dz1 - dz2 * dx1) + dz1 * (dx2 * dy1 - dy2 * dx1);
		double t1 = t1_num / t1_den;

		double t2_num = (x11 + dx1 * t1 - x21) * dx2 + (y11 + dy1 * t1 - y21) * dy2 + (z11 + dz1 * t1 - z21) * dz2;
		double t2_den = dx2 * dx2 + dy2 * dy2 + dz2 * dz2;
		double t2 = t2_num / t2_den;

		boolean inSegment1 = t1 >= -EPSILON && t1 <= 1 + EPSILON;
		boolean inSegment2 = t2 >= -EPSILON && t2 <= 1 + EPSILON;

		if (inSegment1 && inSegment2) {
			boolean onLine11 = isPointOnLine3D(x11, y11, z11, x21, y21, z21, x22, y22, z22);
			boolean onLine12 = isPointOnLine3D(x12, y12, z12, x21, y21, z21, x22, y22, z22);
			boolean onLine21 = isPointOnLine3D(x21, y21, z21, x11, y11, z11, x12, y12, z12);
			boolean onLine22 = isPointOnLine3D(x22, y22, z22, x11, y11, z11, x12, y12, z12);

			if (onLine11 || onLine12 || onLine21 || onLine22) {
				return isVertical ? -3 : -4; // T字路のように端点が他方の線分上にある
			} else {
				return isVertical ? 2 : 1; // 線分の内部で交差
			}
		}

		// 無限直線としては交差するが、線分上では交差しない
		return isVertical ? -6 : -7;
	}

	/**
	 * 3次元空間で点が線分上にあるかを判定します。
	 *
	 * @param px 判定する点のx座標
	 * @param py 判定する点のy座標
	 * @param pz 判定する点のz座標
	 * @param x1 線分の始点のx座標
	 * @param y1 線分の始点のy座標
	 * @param z1 線分の始点のz座標
	 * @param x2 線分の終点のx座標
	 * @param y2 線分の終点のy座標
	 * @param z2 線分の終点のz座標
	 * @return 点が線分上にある場合はtrue、そうでない場合はfalse
	 */
	private static boolean isPointOnLine3D(double px, double py, double pz, double x1, double y1, double z1, double x2, double y2, double z2) {
		// 直線の長さが0の場合
		if (x1 == x2 && y1 == y2 && z1 == z2) {
			return px == x1 && py == y1 && pz == z1;
		}

		// 直線の方向ベクトル
		double dx = x2 - x1;
		double dy = y2 - y1;
		double dz = z2 - z1;

		// 点と直線の始点を結ぶベクトル
		double px_x1 = px - x1;
		double py_y1 = py - y1;
		double pz_z1 = pz - z1;

		// 外積を計算して点が直線上にあるかを確認
		double crossX = py_y1 * dz - pz_z1 * dy;
		double crossY = pz_z1 * dx - px_x1 * dz;
		double crossZ = px_x1 * dy - py_y1 * dx;

		// 外積の大きさがほぼ0なら点は直線上にある
		double crossMagnitude = sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ);
		if (crossMagnitude > EPSILON) {
			return false;
		}

		// 点が線分の範囲内にあるかを確認
		// 内積を使用して判定
		double dot = px_x1 * dx + py_y1 * dy + pz_z1 * dz;
		double lineLengthSquared = dx * dx + dy * dy + dz * dz;

		// パラメータtを計算（0≤t≤1なら線分上）
		double t = dot / lineLengthSquared;
		return t >= -EPSILON && t <= 1 + EPSILON;
	}

	/**
	 * 2次元空間での2つの長方形（矩形）の交差判定を行います。
	 *
	 * @param x11 1つ目の長方形の左下点のx座標
	 * @param y11 1つ目の長方形の左下点のy座標
	 * @param x12 1つ目の長方形の右上点のx座標
	 * @param y12 1つ目の長方形の右上点のy座標
	 * @param x21 2つ目の長方形の左下点のx座標
	 * @param y21 2つ目の長方形の左下点のy座標
	 * @param x22 2つ目の長方形の右上点のx座標
	 * @param y22 2つ目の長方形の右上点のy座標
	 * @return 交差状態を表す整数値：
	 * <ul>
	 * <li>2：長方形が辺で交差している場合</li>
	 * <li>1：長方形が交差している場合（一方が他方を含まない）</li>
	 * <li>0：一方の長方形が他方の長方形と完全に等しい場合</li>
	 * <li>-1：一方の長方形が他方の長方形を完全に含む場合</li>
	 * <li>-2：長方形が頂点のみで接している場合</li>
	 * <li>-3：長方形が辺で接している場合</li>
	 * <li>-4：長方形が交差していない場合</li>
	 * </ul>
	 */
	public static int crossRect(double x11, double y11, double x12, double y12, double x21, double y21, double x22, double y22) {
		// 座標が逆転している場合は修正
		if (x11 > x12) {
			double temp = x11;
			x11 = x12;
			x12 = temp;
		}
		if (y11 > y12) {
			double temp = y11;
			y11 = y12;
			y12 = temp;
		}
		if (x21 > x22) {
			double temp = x21;
			x21 = x22;
			x22 = temp;
		}
		if (y21 > y22) {
			double temp = y21;
			y21 = y22;
			y22 = temp;
		}

		// 完全に等しいかを確認
		boolean isEqual = abs(x11 - x21) < EPSILON && abs(x12 - x22) < EPSILON &&
				abs(y11 - y21) < EPSILON && abs(y12 - y22) < EPSILON;
		if (isEqual) {
			return 0;
		}

		// 一方が他方を含むかを確認
		boolean rect1ContainsRect2 = x11 <= x21 + EPSILON && x22 <= x12 + EPSILON &&
				y11 <= y21 + EPSILON && y22 <= y12 + EPSILON;
		boolean rect2ContainsRect1 = x21 <= x11 + EPSILON && x12 <= x22 + EPSILON &&
				y21 <= y11 + EPSILON && y12 <= y22 + EPSILON;

		if (rect1ContainsRect2 || rect2ContainsRect1) {
			return -1;
		}

		// 交差判定
		boolean noIntersect = x12 + EPSILON < x21 || x22 + EPSILON < x11 ||
				y12 + EPSILON < y21 || y22 + EPSILON < y11;

		// 交差していない場合
		if (noIntersect) {
			return -4;
		}

		// 頂点のみで接しているかを確認
		boolean cornerOnly = (abs(x12 - x21) < EPSILON && abs(y12 - y21) < EPSILON) ||
				(abs(x12 - x21) < EPSILON && abs(y11 - y22) < EPSILON) ||
				(abs(x11 - x22) < EPSILON && abs(y12 - y21) < EPSILON) ||
				(abs(x11 - x22) < EPSILON && abs(y11 - y22) < EPSILON);
		if (cornerOnly) {
			return -2;
		}

		// 辺で接しているかを確認
		boolean edgeOnly = abs(x12 - x21) < EPSILON || abs(x11 - x22) < EPSILON ||
				abs(y12 - y21) < EPSILON || abs(y11 - y22) < EPSILON;
		if (edgeOnly) {
			return -3;
		}

		// 辺で交差しているかを確認
		boolean edgeCross = (x11 < x21 && x21 < x12 && x12 < x22) ||
				(x21 < x11 && x11 < x22 && x22 < x12) ||
				(y11 < y21 && y21 < y12 && y12 < y22) ||
				(y21 < y11 && y11 < y22 && y22 < y12);
		if (edgeCross) {
			return 2;
		}

		// その他の交差
		return 1;
	}

	/**
	 * 3次元空間での2つの直方体（箱）の交差判定を行います。
	 *
	 * @param x11 1つ目の直方体の最小点のx座標
	 * @param y11 1つ目の直方体の最小点のy座標
	 * @param z11 1つ目の直方体の最小点のz座標
	 * @param x12 1つ目の直方体の最大点のx座標
	 * @param y12 1つ目の直方体の最大点のy座標
	 * @param z12 1つ目の直方体の最大点のz座標
	 * @param x21 2つ目の直方体の最小点のx座標
	 * @param y21 2つ目の直方体の最小点のy座標
	 * @param z21 2つ目の直方体の最小点のz座標
	 * @param x22 2つ目の直方体の最大点のx座標
	 * @param y22 2つ目の直方体の最大点のy座標
	 * @param z22 2つ目の直方体の最大点のz座標
	 * @return 交差状態を表す整数値：
	 * <ul>
	 * <li>2：直方体が面で交差している場合</li>
	 * <li>1：直方体が交差している場合（一方が他方を含まない）</li>
	 * <li>0：一方の直方体が他方の直方体と完全に等しい場合</li>
	 * <li>-1：一方の直方体が他方の直方体を完全に含む場合</li>
	 * <li>-2：直方体が頂点のみで接している場合</li>
	 * <li>-3：直方体が辺のみで接している場合</li>
	 * <li>-4：直方体が面のみで接している場合</li>
	 * <li>-5：直方体が交差していない場合</li>
	 * </ul>
	 */
	public static int crossRect3D(double x11, double y11, double z11, double x12, double y12, double z12,
								  double x21, double y21, double z21, double x22, double y22, double z22) {
		if (x11 > x12) {
			double temp = x11;
			x11 = x12;
			x12 = temp;
		}
		if (y11 > y12) {
			double temp = y11;
			y11 = y12;
			y12 = temp;
		}
		if (z11 > z12) {
			double temp = z11;
			z11 = z12;
			z12 = temp;
		}
		if (x21 > x22) {
			double temp = x21;
			x21 = x22;
			x22 = temp;
		}
		if (y21 > y22) {
			double temp = y21;
			y21 = y22;
			y22 = temp;
		}
		if (z21 > z22) {
			double temp = z21;
			z21 = z22;
			z22 = temp;
		}

		// 完全に等しいかを確認
		boolean isEqual = abs(x11 - x21) < EPSILON && abs(x12 - x22) < EPSILON &&
				abs(y11 - y21) < EPSILON && abs(y12 - y22) < EPSILON &&
				abs(z11 - z21) < EPSILON && abs(z12 - z22) < EPSILON;
		if (isEqual) {
			return 0;
		}

		// 一方が他方を含むかを確認
		boolean box1ContainsBox2 = x11 <= x21 + EPSILON && x22 <= x12 + EPSILON &&
				y11 <= y21 + EPSILON && y22 <= y12 + EPSILON &&
				z11 <= z21 + EPSILON && z22 <= z12 + EPSILON;

		boolean box2ContainsBox1 = x21 <= x11 + EPSILON && x12 <= x22 + EPSILON &&
				y21 <= y11 + EPSILON && y12 <= y22 + EPSILON &&
				z21 <= z11 + EPSILON && z12 <= z22 + EPSILON;

		if (box1ContainsBox2 || box2ContainsBox1) {
			return -1;
		}

		// 交差判定
		boolean noIntersect = x12 + EPSILON < x21 || x22 + EPSILON < x11 ||
				y12 + EPSILON < y21 || y22 + EPSILON < y11 ||
				z12 + EPSILON < z21 || z22 + EPSILON < z11;

		// 交差していない場合
		if (noIntersect) {
			return -5;
		}

		// 頂点のみで接しているかを確認
		boolean cornerOnly = (abs(x12 - x21) < EPSILON && abs(y12 - y21) < EPSILON && abs(z12 - z21) < EPSILON) ||
				(abs(x12 - x21) < EPSILON && abs(y12 - y21) < EPSILON && abs(z11 - z22) < EPSILON) ||
				(abs(x12 - x21) < EPSILON && abs(y11 - y22) < EPSILON && abs(z12 - z21) < EPSILON) ||
				(abs(x12 - x21) < EPSILON && abs(y11 - y22) < EPSILON && abs(z11 - z22) < EPSILON) ||
				(abs(x11 - x22) < EPSILON && abs(y12 - y21) < EPSILON && abs(z12 - z21) < EPSILON) ||
				(abs(x11 - x22) < EPSILON && abs(y12 - y21) < EPSILON && abs(z11 - z22) < EPSILON) ||
				(abs(x11 - x22) < EPSILON && abs(y11 - y22) < EPSILON && abs(z12 - z21) < EPSILON) ||
				(abs(x11 - x22) < EPSILON && abs(y11 - y22) < EPSILON && abs(z11 - z22) < EPSILON);
		if (cornerOnly) {
			return -2;
		}

		// 辺のみで接しているかを確認
		boolean edgeXOnly = (abs(x12 - x21) < EPSILON || abs(x11 - x22) < EPSILON) &&
				((y11 < y21 && y21 < y12 && y12 < y22) || (y21 < y11 && y11 < y22 && y22 < y12) ||
						(z11 < z21 && z21 < z12 && z12 < z22) || (z21 < z11 && z11 < z22 && z22 < z12));

		boolean edgeYOnly = (abs(y12 - y21) < EPSILON || abs(y11 - y22) < EPSILON) &&
				((x11 < x21 && x21 < x12 && x12 < x22) || (x21 < x11 && x11 < x22 && x22 < x12) ||
						(z11 < z21 && z21 < z12 && z12 < z22) || (z21 < z11 && z11 < z22 && z22 < z12));

		boolean edgeZOnly = (abs(z12 - z21) < EPSILON || abs(z11 - z22) < EPSILON) &&
				((x11 < x21 && x21 < x12 && x12 < x22) || (x21 < x11 && x11 < x22 && x22 < x12) ||
						(y11 < y21 && y21 < y12 && y12 < y22) || (y21 < y11 && y11 < y22 && y22 < y12));

		if (edgeXOnly || edgeYOnly || edgeZOnly) {
			return -3;
		}

		// 面のみで接しているかを確認
		boolean faceXOnly = (abs(x12 - x21) < EPSILON || abs(x11 - x22) < EPSILON) &&
				!((y12 + EPSILON < y21) || (y22 + EPSILON < y11) ||
						(z12 + EPSILON < z21) || (z22 + EPSILON < z11));

		boolean faceYOnly = (abs(y12 - y21) < EPSILON || abs(y11 - y22) < EPSILON) &&
				!((x12 + EPSILON < x21) || (x22 + EPSILON < x11) ||
						(z12 + EPSILON < z21) || (z22 + EPSILON < z11));

		boolean faceZOnly = (abs(z12 - z21) < EPSILON || abs(z11 - z22) < EPSILON) &&
				!((x12 + EPSILON < x21) || (x22 + EPSILON < x11) ||
						(y12 + EPSILON < y21) || (y22 + EPSILON < y11));

		if (faceXOnly || faceYOnly || faceZOnly) {
			return -4;
		}

		// 面で交差しているかを確認
		boolean faceCross = (x11 < x21 && x21 < x12 && x12 < x22) ||
				(x21 < x11 && x11 < x22 && x22 < x12) ||
				(y11 < y21 && y21 < y12 && y12 < y22) ||
				(y21 < y11 && y11 < y22 && y22 < y12) ||
				(z11 < z21 && z21 < z12 && z12 < z22) ||
				(z21 < z11 && z11 < z22 && z22 < z12);
		if (faceCross) {
			return 2;
		}

		// その他の交差
		return 1;
	}

	/**
	 * 3次元空間で点が直方体（箱）の内部に含まれるかを判定します。
	 *
	 * @param px 判定する点のx座標
	 * @param py 判定する点のy座標
	 * @param pz 判定する点のz座標
	 * @param x1 直方体の最小点のx座標
	 * @param y1 直方体の最小点のy座標
	 * @param z1 直方体の最小点のz座標
	 * @param x2 直方体の最大点のx座標
	 * @param y2 直方体の最大点のy座標
	 * @param z2 直方体の最大点のz座標
	 * @return 点の位置を表す整数値：
	 * <ul>
	 * <li>1：点が直方体の内部に含まれる場合</li>
	 * <li>0：点が直方体の境界上（面上）にある場合</li>
	 * <li>-1：点が直方体の辺上にある場合</li>
	 * <li>-2：点が直方体の頂点上にある場合</li>
	 * <li>-3：点が直方体の外部にある場合</li>
	 * </ul>
	 */
	public static int containsPoint(double px, double py, double pz, double x1, double y1, double z1, double x2, double y2, double z2) {
		// 座標が逆転している場合は修正
		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			double temp = y1;
			y1 = y2;
			y2 = temp;
		}
		if (z1 > z2) {
			double temp = z1;
			z1 = z2;
			z2 = temp;
		}

		// 頂点上にある場合
		boolean onVertex = (abs(px - x1) < EPSILON && abs(py - y1) < EPSILON && abs(pz - z1) < EPSILON) ||
				(abs(px - x1) < EPSILON && abs(py - y1) < EPSILON && abs(pz - z2) < EPSILON) ||
				(abs(px - x1) < EPSILON && abs(py - y2) < EPSILON && abs(pz - z1) < EPSILON) ||
				(abs(px - x1) < EPSILON && abs(py - y2) < EPSILON && abs(pz - z2) < EPSILON) ||
				(abs(px - x2) < EPSILON && abs(py - y1) < EPSILON && abs(pz - z1) < EPSILON) ||
				(abs(px - x2) < EPSILON && abs(py - y1) < EPSILON && abs(pz - z2) < EPSILON) ||
				(abs(px - x2) < EPSILON && abs(py - y2) < EPSILON && abs(pz - z1) < EPSILON) ||
				(abs(px - x2) < EPSILON && abs(py - y2) < EPSILON && abs(pz - z2) < EPSILON);
		if (onVertex) {
			return -2;
		}

		// 辺上にある場合
		boolean onEdgeX = (abs(px - x1) < EPSILON || abs(px - x2) < EPSILON) &&
				(abs(py - y1) < EPSILON || abs(py - y2) < EPSILON) &&
				(z1 + EPSILON < pz && pz < z2 - EPSILON);

		boolean onEdgeY = (abs(py - y1) < EPSILON || abs(py - y2) < EPSILON) &&
				(abs(pz - z1) < EPSILON || abs(pz - z2) < EPSILON) &&
				(x1 + EPSILON < px && px < x2 - EPSILON);

		boolean onEdgeZ = (abs(pz - z1) < EPSILON || abs(pz - z2) < EPSILON) &&
				(abs(px - x1) < EPSILON || abs(px - x2) < EPSILON) &&
				(y1 + EPSILON < py && py < y2 - EPSILON);

		if (onEdgeX || onEdgeY || onEdgeZ) {
			return -1;
		}

		// 面上にある場合
		boolean onFaceX = (abs(px - x1) < EPSILON || abs(px - x2) < EPSILON) &&
				(y1 + EPSILON < py && py < y2 - EPSILON) &&
				(z1 + EPSILON < pz && pz < z2 - EPSILON);

		boolean onFaceY = (abs(py - y1) < EPSILON || abs(py - y2) < EPSILON) &&
				(x1 + EPSILON < px && px < x2 - EPSILON) &&
				(z1 + EPSILON < pz && pz < z2 - EPSILON);

		boolean onFaceZ = (abs(pz - z1) < EPSILON || abs(pz - z2) < EPSILON) &&
				(x1 + EPSILON < px && px < x2 - EPSILON) &&
				(y1 + EPSILON < py && py < y2 - EPSILON);

		if (onFaceX || onFaceY || onFaceZ) {
			return 0;
		}

		// 内部に含まれる場合
		if (x1 + EPSILON < px && px < x2 - EPSILON &&
				y1 + EPSILON < py && py < y2 - EPSILON &&
				z1 + EPSILON < pz && pz < z2 - EPSILON) {
			return 1;
		}

		// 外部にある場合
		return -3;
	}

	/**
	 * 3次元空間で点が三角形の内部に含まれるかを判定します。
	 *
	 * @param px 判定する点のx座標
	 * @param py 判定する点のy座標
	 * @param pz 判定する点のz座標
	 * @param x1 三角形の頂点1のx座標
	 * @param y1 三角形の頂点1のy座標
	 * @param z1 三角形の頂点1のz座標
	 * @param x2 三角形の頂点2のx座標
	 * @param y2 三角形の頂点2のy座標
	 * @param z2 三角形の頂点2のz座標
	 * @param x3 三角形の頂点3のx座標
	 * @param y3 三角形の頂点3のy座標
	 * @param z3 三角形の頂点3のz座標
	 * @return 点の位置を表す整数値：
	 * <ul>
	 * <li>1：点が三角形の内部に含まれる場合</li>
	 * <li>0：点が三角形の辺上にある場合</li>
	 * <li>-1：点が三角形の頂点上にある場合</li>
	 * <li>-2：点が三角形の平面上だが三角形の外部にある場合</li>
	 * <li>-3：点が三角形の平面外にある場合</li>
	 * </ul>
	 */
	public static int containsPointTri(double px, double py, double pz, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
		// 点が頂点上にあるかを確認
		boolean onVertex1 = abs(px - x1) < EPSILON && abs(py - y1) < EPSILON && abs(pz - z1) < EPSILON;
		boolean onVertex2 = abs(px - x2) < EPSILON && abs(py - y2) < EPSILON && abs(pz - z2) < EPSILON;
		boolean onVertex3 = abs(px - x3) < EPSILON && abs(py - y3) < EPSILON && abs(pz - z3) < EPSILON;

		if (onVertex1 || onVertex2 || onVertex3) {
			return -1;
		}

		// 三角形の法線ベクトルを計算
		double nx = (y2 - y1) * (z3 - z1) - (z2 - z1) * (y3 - y1);
		double ny = (z2 - z1) * (x3 - x1) - (x2 - x1) * (z3 - z1);
		double nz = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);

		// 法線ベクトルの長さ
		double length = sqrt(nx * nx + ny * ny + nz * nz);

		// 三角形が縮退している場合（面積が0）
		if (length < EPSILON) {
			// 点が線分上にあるか確認
			if (isPointOnLine3D(px, py, pz, x1, y1, z1, x2, y2, z2) ||
					isPointOnLine3D(px, py, pz, x2, y2, z2, x3, y3, z3) ||
					isPointOnLine3D(px, py, pz, x3, y3, z3, x1, y1, z1)) {
				return 0;
			}
			return -3;
		}

		// 法線ベクトルを正規化
		nx /= length;
		ny /= length;
		nz /= length;

		// 点が三角形の平面上にあるかを確認
		double d = -(nx * x1 + ny * y1 + nz * z1);
		double dist = nx * px + ny * py + nz * pz + d;

		// 点が平面上にない場合
		if (abs(dist) > EPSILON) {
			return -3;
		}

		// 点が辺上にあるかを確認
		boolean onEdge1 = isPointOnLine3D(px, py, pz, x1, y1, z1, x2, y2, z2);
		boolean onEdge2 = isPointOnLine3D(px, py, pz, x2, y2, z2, x3, y3, z3);
		boolean onEdge3 = isPointOnLine3D(px, py, pz, x3, y3, z3, x1, y1, z1);

		if (onEdge1 || onEdge2 || onEdge3) {
			return 0;
		}

		// バリセントリック座標を使用して点が三角形内にあるかを確認
		// 三角形の面積を計算
		double area = 0.5 * length;

		// 点と三角形の各頂点を結ぶ小三角形の面積を計算（外積の大きさを利用）
		double cx1 = (y2 - py) * (z3 - pz) - (z2 - pz) * (y3 - py);
		double cy1 = (z2 - pz) * (x3 - px) - (x2 - px) * (z3 - pz);
		double cz1 = (x2 - px) * (y3 - py) - (y2 - py) * (x3 - px);
		double area1 = 0.5 * sqrt(cx1 * cx1 + cy1 * cy1 + cz1 * cz1);

		double cx2 = (y3 - py) * (z1 - pz) - (z3 - pz) * (y1 - py);
		double cy2 = (z3 - pz) * (x1 - px) - (x3 - px) * (z1 - pz);
		double cz2 = (x3 - px) * (y1 - py) - (y3 - py) * (x1 - px);
		double area2 = 0.5 * sqrt(cx2 * cx2 + cy2 * cy2 + cz2 * cz2);

		double cx3 = (y1 - py) * (z2 - pz) - (z1 - pz) * (y2 - py);
		double cy3 = (z1 - pz) * (x2 - px) - (x1 - px) * (z2 - pz);
		double cz3 = (x1 - px) * (y2 - py) - (y1 - py) * (x2 - px);
		double area3 = 0.5 * sqrt(cx3 * cx3 + cy3 * cy3 + cz3 * cz3);

		// バリセントリック座標
		double u = area1 / area;
		double v = area2 / area;
		double w = area3 / area;

		// 数値誤差を考慮
		double sum = u + v + w;
		if (abs(sum - 1.0) > EPSILON) {
			u /= sum;
			v /= sum;
			w /= sum;
		}

		// 内部にある場合（すべての座標が正）
		if (u > EPSILON && v > EPSILON && w > EPSILON) {
			return 1;
		}

		// 平面上だが三角形の外部にある場合
		return -2;
	}

	/**
	 * 2次元空間での2点間のユークリッド距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @return 2点間のユークリッド距離
	 */
	public static double euclidDist(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return sqrt(dx * dx + dy * dy);
	}

	/**
	 * 3次元空間での2点間のユークリッド距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param z1 1つ目の点のz座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @param z2 2つ目の点のz座標
	 * @return 2点間のユークリッド距離
	 */
	public static double euclidDist3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double dz = z2 - z1;
		return sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * N次元空間での2点間のユークリッド距離を計算します。
	 *
	 * @param n  次元数（1以上の整数）
	 * @param p1 1つ目の点の座標配列（長さはn以上）
	 * @param p2 2つ目の点の座標配列（長さはn以上）
	 * @return 2点間のユークリッド距離
	 * @throws IllegalArgumentException 次元数が1未満の場合、または配列の長さが不足している場合
	 */
	public static double euclidDistN(int n, double[] p1, double[] p2) {
		if (n < 1) {
			throw new IllegalArgumentException("次元数は1以上である必要があります");
		}
		if (p1 == null || p2 == null || p1.length < n || p2.length < n) {
			throw new IllegalArgumentException("配列の長さが不足しています");
		}

		double d = 0;
		for (int i = 0; i < n; i++) {
			double diff = p1[i] - p2[i];
			d += diff * diff;
		}
		return sqrt(d);
	}

	/**
	 * 2次元空間での2点間のマンハッタン距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @return 2点間のマンハッタン距離
	 */
	public static double manhattanDist(double x1, double y1, double x2, double y2) {
		double dx = abs(x2 - x1);
		double dy = abs(y2 - y1);
		return dx + dy;
	}

	/**
	 * 3次元空間での2点間のマンハッタン距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param z1 1つ目の点のz座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @param z2 2つ目の点のz座標
	 * @return 2点間のマンハッタン距離
	 */
	public static double manhattanDist3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = abs(x2 - x1);
		double dy = abs(y2 - y1);
		double dz = abs(z2 - z1);
		return dx + dy + dz;
	}

	/**
	 * N次元空間での2点間のマンハッタン距離を計算します。
	 *
	 * @param n  次元数（1以上の整数）
	 * @param p1 1つ目の点の座標配列（長さはn以上）
	 * @param p2 2つ目の点の座標配列（長さはn以上）
	 * @return 2点間のマンハッタン距離
	 * @throws IllegalArgumentException 次元数が1未満の場合、または配列の長さが不足している場合
	 */
	public static double manhattanDistN(int n, double[] p1, double[] p2) {
		if (n < 1) {
			throw new IllegalArgumentException("次元数は1以上である必要があります");
		}
		if (p1 == null || p2 == null || p1.length < n || p2.length < n) {
			throw new IllegalArgumentException("配列の長さが不足しています");
		}

		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += abs(p1[i] - p2[i]);
		}
		return sum;
	}

	/**
	 * 2次元空間での2点間のチェビシェフ距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @return 2点間のチェビシェフ距離
	 */
	public static double chebyshevDist(double x1, double y1, double x2, double y2) {
		double dx = abs(x2 - x1);
		double dy = abs(y2 - y1);
		return max(dx, dy);
	}

	/**
	 * 3次元空間での2点間のチェビシェフ距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param z1 1つ目の点のz座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @param z2 2つ目の点のz座標
	 * @return 2点間のチェビシェフ距離
	 */
	public static double chebyshevDist3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = abs(x2 - x1);
		double dy = abs(y2 - y1);
		double dz = abs(z2 - z1);
		return max(dx, max(dy, dz));
	}

	/**
	 * N次元空間での2点間のチェビシェフ距離を計算します。
	 *
	 * @param n  次元数（1以上の整数）
	 * @param p1 1つ目の点の座標配列（長さはn以上）
	 * @param p2 2つ目の点の座標配列（長さはn以上）
	 * @return 2点間のチェビシェフ距離
	 * @throws IllegalArgumentException 次元数が1未満の場合、または配列の長さが不足している場合
	 */
	public static double chebyshevDistN(int n, double[] p1, double[] p2) {
		if (n < 1) {
			throw new IllegalArgumentException("次元数は1以上である必要があります");
		}
		if (p1 == null || p2 == null || p1.length < n || p2.length < n) {
			throw new IllegalArgumentException("配列の長さが不足しています");
		}

		double maxDiff = 0;
		for (int i = 0; i < n; i++) {
			double diff = abs(p1[i] - p2[i]);
			maxDiff = max(maxDiff, diff);
		}
		return maxDiff;
	}
}
