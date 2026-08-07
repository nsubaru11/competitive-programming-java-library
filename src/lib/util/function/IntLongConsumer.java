package lib.util.function;

import java.util.*;

/**
 * {@code int} 値と {@code long} 値を受け取る処理を表します。
 */
@FunctionalInterface
public interface IntLongConsumer {
	/**
	 * 指定された値を使って処理を実行します。
	 *
	 * @param a {@code int} 値
	 * @param b {@code long} 値
	 */
	void accept(int a, long b);

	/**
	 * この処理の後に指定された処理を実行する合成処理を返します。
	 *
	 * @param after 後に実行する処理
	 * @return 合成された処理
	 */
	default IntLongConsumer andThen(final IntLongConsumer after) {
		Objects.requireNonNull(after);
		return (a, b) -> {
			accept(a, b);
			after.accept(a, b);
		};
	}

}
