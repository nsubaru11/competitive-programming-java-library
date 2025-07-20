import java.util.Arrays;
import java.util.StringJoiner;

@SuppressWarnings("unused")
public final class Master {

	/* -------------- toInt -------------- */

	/**
	 * char[] -> int 変換
	 */
	public static int toInt(char[] arr) {
		int intVal = 0;
		for (char c : arr)
			intVal = intVal * 10 + c - '0';
		return intVal;
	}

	/**
	 * String -> int 変換
	 */
	public static int toInt(String s) {
		int intVal = 0;
		for (int i = 0, len = s.length(); i < len; i++)
			intVal = intVal * 10 + s.charAt(i) - '0';
		return intVal;
	}

	/* -------------- toLong -------------- */

	/**
	 * char[] -> long 変換
	 */
	public static long toLong(char[] arr) {
		long longVal = 0;
		for (char c : arr)
			longVal = longVal * 10 + c - '0';
		return longVal;
	}

	/**
	 * String -> long 変換
	 */
	public static long toLong(String s) {
		long longVal = 0;
		for (int i = 0, len = s.length(); i < len; i++)
			longVal = longVal * 10 + s.charAt(i) - '0';
		return longVal;
	}

	/* -------------- toCharArray -------------- */

	/**
	 * int -> char[] 変換
	 */
	public static char[] toCharArray(int n) {
		return String.valueOf(n).toCharArray();
	}

	/**
	 * int -> char[] 変換（桁数指定）
	 * 指定した桁数に満たない場合0で埋める
	 * (ビッグエンディアン)
	 */
	public static char[] toCharArray(int n, int l) {
		char[] c = new char[l];
		for (int i = l - 1; i >= 0; i--) {
			c[i] = (char) (n % 10 + '0');
			n /= 10;
		}
		return c;
	}

	/**
	 * long -> char[] 変換
	 */
	public static char[] toCharArray(long n) {
		return String.valueOf(n).toCharArray();
	}

	/**
	 * long -> char[] 変換（桁数指定）
	 * 指定した桁数に満たない場合0で埋める
	 * (ビッグエンディアン)
	 */
	public static char[] toCharArray(long n, int l) {
		char[] c = new char[l];
		for (int i = l - 1; i >= 0; i--) {
			c[i] = (char) (n % 10 + '0');
			n /= 10;
		}
		return c;
	}

	/**
	 * int[] -> char[] 変換
	 */
	public static char[] toCharArray(int[] arr) {
		int len = arr.length;
		char[] res = new char[len];
		for (int i = 0; i < len; i++) {
			res[i] = (char) (arr[i] + '0');
		}
		return res;
	}

	/* -------------- toString -------------- */

	/**
	 * int -> String 変換
	 */
	public static String toString(int n) {
		return Integer.toString(n);
	}

	/**
	 * long -> String 変換
	 */
	public static String toString(long n) {
		return Long.toString(n);
	}

	/**
	 * char[] -> String 変換
	 */
	public static String toString(char[] c) {
		return String.valueOf(c);
	}

	/**
	 * int[] -> String 変換（半角スペース区切り）
	 */
	public static String toString(int[] arr) {
		StringJoiner sj = new StringJoiner(" ");
		for (int a : arr) sj.add(Integer.toString(a));
		return sj.toString();
	}

	/**
	 * long[] -> String 変換（半角スペース区切り）
	 */
	public static String toString(long[] arr) {
		StringJoiner sj = new StringJoiner(" ");
		for (long a : arr) sj.add(Long.toString(a));
		return sj.toString();
	}

	/**
	 * int[][] -> String 変換（半角スペース区切り）
	 */
	public static String toString(int[][] arr) {
		StringBuilder sb = new StringBuilder();
		for (int[] a : arr) {
			sb.append(a[0]);
			for (int i = 1, len = a.length; i < len; i++) {
				sb.append(' ').append(a[i]);
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * long[][] -> String 変換（半角スペース区切り）
	 */
	public static String toString(long[][] arr) {
		StringBuilder sb = new StringBuilder();
		for (long[] a : arr) {
			sb.append(a[0]);
			for (int i = 1, len = a.length; i < len; i++) {
				sb.append(' ').append(a[i]);
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/* -------------- toIntArray -------------- */

	/**
	 * char[] -> int[] 変換
	 */
	public static int[] toIntArray(char[] arr) {
		int len = arr.length;
		int[] res = new int[len];
		for (int i = 0; i < len; i++)
			res[i] = arr[i] - '0';
		return res;
	}

	/**
	 * String -> int[] 変換
	 */
	public static int[] toIntArray(String s) {
		int len = s.length();
		int[] res = new int[len];
		for (int i = 0; i < len; i++)
			res[i] = s.charAt(i) - '0';
		return res;
	}

	/* -------------- reverse -------------- */

	/**
	 * int値の反転
	 */
	public static int reverse(int n) {
		int iReverse = 0;
		while (n > 0) {
			iReverse = iReverse * 10 + n % 10;
			n /= 10;
		}
		return iReverse;
	}

	/**
	 * long値の反転
	 */
	public static long reverse(long n) {
		long lReverse = 0;
		while (n > 0) {
			lReverse = lReverse * 10 + n % 10;
			n /= 10;
		}
		return lReverse;
	}

	/**
	 * 配列の反転（int[]）
	 */
	public static void reverse(int[] arr) {
		for (int i = 0, len = arr.length; i < len / 2; i++) {
			int tmp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = tmp;
		}
	}

	/**
	 * 配列の反転（char[]）
	 */
	public static void reverse(char[] arr) {
		for (int i = 0, len = arr.length; i < len / 2; i++) {
			char tmp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = tmp;
		}
	}

	/**
	 * 配列の反転（long[]）
	 */
	public static void reverse(long[] arr) {
		for (int i = 0, len = arr.length; i < len / 2; i++) {
			long tmp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = tmp;
		}
	}

	/* -------------- sort -------------- */

	/**
	 * int値 各桁昇順でソート
	 */
	public static int sort(int n) {
		char[] c = toCharArray(n);
		Arrays.sort(c);
		return toInt(c);
	}

	/**
	 * long値 各桁昇順でソート
	 */
	public static long sort(long n) {
		char[] c = toCharArray(n);
		Arrays.sort(c);
		return toLong(c);
	}

	/**
	 * char[] ソート
	 */
	public static void sort(char[] arr) {
		Arrays.sort(arr);
	}

	/**
	 * int[] ソート
	 */
	public static void sort(int[] arr) {
		Arrays.sort(arr);
	}

	/**
	 * long[] ソート
	 */
	public static void sort(long[] arr) {
		Arrays.sort(arr);
	}

	/**
	 * String ソート（戻り値あり）
	 */
	public static String sort(String str) {
		char[] arr = str.toCharArray();
		sort(arr);
		return toString(arr);
	}

	/* -------------- descendingSort -------------- */

	/**
	 * int値 各桁降順でソート
	 */
	public static int descendingSort(int n) {
		char[] c = toCharArray(n);
		Arrays.sort(c);
		reverse(c);
		return toInt(c);
	}

	/**
	 * long値 各桁降順でソート
	 */
	public static long descendingSort(long n) {
		char[] c = toCharArray(n);
		Arrays.sort(c);
		reverse(c);
		return toLong(c);
	}

	/**
	 * char[] 降順でソート
	 */
	public static void descendingSort(char[] arr) {
		Arrays.sort(arr);
		reverse(arr);
	}

	/**
	 * int[] 降順ソート
	 */
	public static void descendingSort(int[] arr) {
		Arrays.sort(arr);
		reverse(arr);
	}

	/**
	 * long[] 降順ソート
	 */
	public static void descendingSort(long[] arr) {
		Arrays.sort(arr);
		reverse(arr);
	}

	/**
	 * String 降順ソート（戻り値あり）
	 */
	public static String descendingSort(String str) {
		char[] arr = str.toCharArray();
		descendingSort(arr);
		return toString(arr);
	}

}
