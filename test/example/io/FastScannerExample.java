package example.io;

import java.io.*;
import java.util.*;

import lib.io.*;

public final class FastScannerExample {

	public static void main(final String[] args) {
		// 入力
		String input = "3\n10 20 30\nabc\n1.5\n";
		FastScanner sc = new FastScanner(new ByteArrayInputStream(input.getBytes()));

		// 整数
		int n = sc.nextInt();
		System.out.println(n);
		System.out.println();

		// 配列
		int[] a = sc.nextInt(n);
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 文字列
		System.out.println(sc.next());
		System.out.println();

		// 小数
		System.out.println(sc.nextDouble());
	}
}
