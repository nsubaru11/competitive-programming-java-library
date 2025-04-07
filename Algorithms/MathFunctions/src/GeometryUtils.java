import static java.lang.Math.*;

/**
 * 幾何学関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public class GeometryUtils {

	/**
	 * 2点間の距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @return 2点間の距離
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	/**
	 * 3次元空間での2点間の距離を計算します。
	 *
	 * @param x1 1つ目の点のx座標
	 * @param y1 1つ目の点のy座標
	 * @param z1 1つ目の点のz座標
	 * @param x2 2つ目の点のx座標
	 * @param y2 2つ目の点のy座標
	 * @param z2 2つ目の点のz座標
	 * @return 2点間の距離
	 */
	public static double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
	}

	/**
	 * 2つのベクトル間の角度を計算します。
	 *
	 * @param x1 1つ目のベクトルのx成分
	 * @param y1 1つ目のベクトルのy成分
	 * @param x2 2つ目のベクトルのx成分
	 * @param y2 2つ目のベクトルのy成分
	 * @return 角度（ラジアン）
	 */
	public static double angle(double x1, double y1, double x2, double y2) {
		return acos((x1 * x2 + y1 * y2) / (sqrt(x1 * x1 + y1 * y1) * sqrt(x2 * x2 + y2 * y2)));
	}

	/**
	 * 三角形の面積を計算します。
	 *
	 * @param x1 1つ目の頂点のx座標
	 * @param y1 1つ目の頂点のy座標
	 * @param x2 2つ目の頂点のx座標
	 * @param y2 2つ目の頂点のy座標
	 * @param x3 3つ目の頂点のx座標
	 * @param y3 3つ目の頂点のy座標
	 * @return 三角形の面積
	 */
	public static double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
		return abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2);
	}

	/**
	 * 円の面積を計算します。
	 *
	 * @param radius 半径
	 * @return 円の面積
	 */
	public static double circleArea(double radius) {
		return PI * radius * radius;
	}

	/**
	 * 円の円周を計算します。
	 *
	 * @param radius 半径
	 * @return 円の円周
	 */
	public static double circleCircumference(double radius) {
		return 2 * PI * radius;
	}

	/**
	 * 球の体積を計算します。
	 *
	 * @param radius 半径
	 * @return 球の体積
	 */
	public static double sphereVolume(double radius) {
		return (4.0 / 3.0) * PI * radius * radius * radius;
	}

	/**
	 * 球の表面積を計算します。
	 *
	 * @param radius 半径
	 * @return 球の表面積
	 */
	public static double sphereSurfaceArea(double radius) {
		return 4 * PI * radius * radius;
	}

	/**
	 * 円錐の体積を計算します。
	 *
	 * @param radius 底面の半径
	 * @param height 高さ
	 * @return 円錐の体積
	 */
	public static double coneVolume(double radius, double height) {
		return (1.0 / 3.0) * PI * radius * radius * height;
	}

	/**
	 * 円錐の表面積を計算します。
	 *
	 * @param radius 底面の半径
	 * @param height 高さ
	 * @return 円錐の表面積
	 */
	public static double coneSurfaceArea(double radius, double height) {
		double slantHeight = sqrt(radius * radius + height * height);
		return PI * radius * (radius + slantHeight);
	}

	/**
	 * 直方体の体積を計算します。
	 *
	 * @param length 長さ
	 * @param width  幅
	 * @param height 高さ
	 * @return 直方体の体積
	 */
	public static double cuboidVolume(double length, double width, double height) {
		return length * width * height;
	}

	/**
	 * 直方体の表面積を計算します。
	 *
	 * @param length 長さ
	 * @param width  幅
	 * @param height 高さ
	 * @return 直方体の表面積
	 */
	public static double cuboidSurfaceArea(double length, double width, double height) {
		return 2 * (length * width + width * height + height * length);
	}
} 