package example.io.compat17;

import java.util.*;

import lib.io.compat17.*;

public final class FastPrinterExample {

	public static void main(final String[] args) {
		try (FastPrinter out = new FastPrinter()) {
			// 単一の値
			out.println(42);
			out.println("abc");
			out.println();

			// 複数の値
			out.println(1, "two", 3.0);
			out.println();

			// 配列
			out.println(new int[]{1, 2, 3});
			out.println(new long[]{4, 5, 6}, ',');
			out.println();

			// リスト
			List<Integer> list = List.of(1, 2, 3);
			out.println(list);
			out.println(list, i -> i * i);
		}
	}
}
