package example.io.compat17;

import java.io.*;
import java.util.*;

import lib.io.compat17.*;

public final class InteractiveScannerExample {

	public static void main(final String[] args) {
		// 入力（対話型ジャッジの応答の代わり）
		String input = "3\n10 20 30\nYes\nhello world\n";
		InteractiveScanner sc = new InteractiveScanner(new ByteArrayInputStream(input.getBytes()));

		// 整数
		int n = sc.nextInt();
		System.out.println(n);
		System.out.println();

		// 配列
		System.out.println(Arrays.toString(sc.nextInt(n)));
		System.out.println();

		// 文字列
		System.out.println(sc.next());
		System.out.println();

		// 行
		System.out.println(sc.nextLine());
	}
}
