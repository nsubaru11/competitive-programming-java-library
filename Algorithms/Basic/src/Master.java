import java.util.Arrays;
import java.util.StringJoiner;

@SuppressWarnings("unused")
public final class Master {

	/* -------------- toInt -------------- */

	/**
	 * char[] -> int 変換
	 */
	public static int toInt(final char[] arr) {
		int intVal = 0;
		for (char c : arr) intVal = intVal * 10 + c - '0';
		return intVal;
	}

	/**
	 * String -> int 変換
	 */
	public static int toInt(final String s) {
		int intVal = 0;
		for (int i = 0, len = s.length(); i < len; i++)
			intVal = intVal * 10 + s.charAt(i) - '0';
		return intVal;
	}

	/* -------------- toLong -------------- */

	/**
	 * char[] -> long 変換
	 */
	public static long toLong(final char[] arr) {
		long longVal = 0;
		for (char c : arr) longVal = longVal * 10 + c - '0';
		return longVal;
	}

	/**
	 * String -> long 変換
	 */
	public static long toLong(final String s) {
		long longVal = 0;
		for (int i = 0, len = s.length(); i < len; i++)
			longVal = longVal * 10 + s.charAt(i) - '0';
		return longVal;
	}

	/* -------------- toCharArray -------------- */

	/**
	 * int -> char[] 変換
	 */
	public static char[] toCharArray(final int n) {
		return String.valueOf(n).toCharArray();
	}

	/**
	 * int -> char[] 変換（桁数指定）
	 * 指定した桁数に満たない場合0で埋める
	 * (ビッグエンディアン)
	 */
	public static char[] toCharArray(int n, final int l) {
		final char[] c = new char[l];
		for (int i = l - 1; i >= 0; i--) {
			c[i] = (char) (n % 10 + '0');
			n /= 10;
		}
		return c;
	}

	/**
	 * long -> char[] 変換
	 */
	public static char[] toCharArray(final long n) {
		return String.valueOf(n).toCharArray();
	}

	/**
	 * long -> char[] 変換（桁数指定）
	 * 指定した桁数に満たない場合0で埋める
	 * (ビッグエンディアン)
	 */
	public static char[] toCharArray(long n, final int l) {
		final char[] c = new char[l];
		for (int i = l - 1; i >= 0; i--) {
			c[i] = (char) (n % 10 + '0');
			n /= 10;
		}
		return c;
	}

	/**
	 * int[] -> char[] 変換
	 */
	public static char[] toCharArray(final int[] arr) {
		final int len = arr.length;
		final char[] res = new char[len];
		for (int i = 0; i < len; i++) {
			res[i] = (char) (arr[i] + '0');
		}
		return res;
	}

	/* -------------- toString -------------- */

	/**
	 * int -> String 変換
	 */
	public static String toString(final int n) {
		return Integer.toString(n);
	}

	/**
	 * long -> String 変換
	 */
	public static String toString(final long n) {
		return Long.toString(n);
	}

	/**
	 * char[] -> String 変換
	 */
	public static String toString(final char[] c) {
		return String.valueOf(c);
	}

	/**
	 * int[] -> String 変換（半角スペース区切り）
	 */
	public static String toString(final int[] arr) {
		final StringJoiner sj = new StringJoiner(" ");
		for (final int a : arr) sj.add(Integer.toString(a));
		return sj.toString();
	}

	/**
	 * long[] -> String 変換（半角スペース区切り）
	 */
	public static String toString(final long[] arr) {
		final StringJoiner sj = new StringJoiner(" ");
		for (final long a : arr) sj.add(Long.toString(a));
		return sj.toString();
	}

	/**
	 * int[][] -> String 変換（半角スペース区切り）
	 */
	public static String toString(final int[][] arr) {
		final StringBuilder sb = new StringBuilder();
		for (final int[] a : arr) {
			sb.append(a[0]);
			for (int i = 1, len = a.length; i < len; i++)
				sb.append(' ').append(a[i]);
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * long[][] -> String 変換（半角スペース区切り）
	 */
	public static String toString(final long[][] arr) {
		final StringBuilder sb = new StringBuilder();
		for (final long[] a : arr) {
			sb.append(a[0]);
			for (int i = 1, len = a.length; i < len; i++)
				sb.append(' ').append(a[i]);
			sb.append('\n');
		}
		return sb.toString();
	}

	/* -------------- toIntArray -------------- */

	/**
	 * char[] -> int[] 変換
	 */
	public static int[] toIntArray(final char[] arr) {
		final int len = arr.length;
		final int[] res = new int[len];
		for (int i = 0; i < len; i++) res[i] = arr[i] - '0';
		return res;
	}

	/**
	 * String -> int[] 変換
	 */
	public static int[] toIntArray(final String s) {
		final int len = s.length();
		final int[] res = new int[len];
		for (int i = 0; i < len; i++) res[i] = s.charAt(i) - '0';
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
	public static void reverse(final int[] arr) {
		for (int i = 0, len = arr.length; i < len / 2; i++) {
			final int tmp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = tmp;
		}
	}

	/**
	 * 配列の反転（char[]）
	 */
	public static void reverse(final char[] arr) {
		for (int i = 0, len = arr.length; i < len / 2; i++) {
			final char tmp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = tmp;
		}
	}

	/**
	 * 配列の反転（long[]）
	 */
	public static void reverse(final long[] arr) {
		for (int i = 0, len = arr.length; i < len / 2; i++) {
			final long tmp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = tmp;
		}
	}

	/* -------------- sort -------------- */

	/**
	 * int値 各桁昇順でソート
	 */
	public static int sort(final int n) {
		final char[] c = toCharArray(n);
		Arrays.sort(c);
		return toInt(c);
	}

	/**
	 * long値 各桁昇順でソート
	 */
	public static long sort(final long n) {
		char[] c = toCharArray(n);
		Arrays.sort(c);
		return toLong(c);
	}

	/**
	 * char[] ソート
	 */
	public static void sort(final char[] arr) {
		Arrays.sort(arr);
	}

	/**
	 * int[] ソート
	 */
	public static void sort(final int[] arr) {
		Arrays.sort(arr);
	}

	/**
	 * long[] ソート
	 */
	public static void sort(final long[] arr) {
		Arrays.sort(arr);
	}

	/**
	 * String ソート（戻り値あり）
	 */
	public static String sort(final String str) {
		final char[] arr = str.toCharArray();
		sort(arr);
		return toString(arr);
	}

	/* -------------- descendingSort -------------- */

	/**
	 * int値 各桁降順でソート
	 */
	public static int descendingSort(final int n) {
		final char[] c = toCharArray(n);
		Arrays.sort(c);
		reverse(c);
		return toInt(c);
	}

	/**
	 * long値 各桁降順でソート
	 */
	public static long descendingSort(final long n) {
		final char[] c = toCharArray(n);
		Arrays.sort(c);
		reverse(c);
		return toLong(c);
	}

	/**
	 * char[] 降順でソート
	 */
	public static void descendingSort(final char[] arr) {
		Arrays.sort(arr);
		reverse(arr);
	}

	/**
	 * int[] 降順ソート
	 */
	public static void descendingSort(final int[] arr) {
		Arrays.sort(arr);
		reverse(arr);
	}

	/**
	 * long[] 降順ソート
	 */
	public static void descendingSort(final long[] arr) {
		Arrays.sort(arr);
		reverse(arr);
	}

	/**
	 * String 降順ソート（戻り値あり）
	 */
	public static String descendingSort(final String str) {
		final char[] arr = str.toCharArray();
		descendingSort(arr);
		return toString(arr);
	}

}
