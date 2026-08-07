package lib.util.function;

import java.util.*;

/**
 * 2つの {@code int} 値を受け取る処理を表します。
 */
@FunctionalInterface
public interface IntBinaryConsumer {
	/**
	 * 指定された値を使って処理を実行します。
	 *
	 * @param a 1つ目の値
	 * @param b 2つ目の値
	 */
	void accept(int a, int b);

	/**
	 * この処理の後に指定された処理を実行する合成処理を返します。
	 *
	 * @param after 後に実行する処理
	 * @return 合成された処理
	 */
	default IntBinaryConsumer andThen(final IntBinaryConsumer after) {
		Objects.requireNonNull(after);
		return (a, b) -> {
			accept(a, b);
			after.accept(a, b);
		};
	}
}
