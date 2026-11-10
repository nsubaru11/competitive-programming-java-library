package unit.support;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 愚直解との照合で使う、重み付きのランダムな操作列の実行器。
 * 操作を名前と重みで登録し、run で指定回数だけ選んで実行する。失敗メッセージにはシード・操作番号・操作名を含める。
 * 実行後、一度も選ばれなかった操作があれば失敗させ、照合されていない操作を見逃さないようにする。
 */
public final class RandomOperations {

	private final List<String> names = new ArrayList<>();
	private final List<Operation> operations = new ArrayList<>();
	private final List<Integer> weights = new ArrayList<>();
	private int totalWeight;

	public RandomOperations add(final String name, final Operation operation) {
		return add(name, 1, operation);
	}

	public RandomOperations add(final String name, final int weight, final Operation operation) {
		names.add(name);
		operations.add(operation);
		weights.add(weight);
		totalWeight += weight;
		return this;
	}

	public void run(final long seed, final Random random, final int count) {
		final int[] executed = new int[operations.size()];
		for (int op = 0; op < count; op++) {
			int r = random.nextInt(totalWeight), k = 0;
			while (r >= weights.get(k)) r -= weights.get(k++);
			executed[k]++;
			operations.get(k).run(random, "seed=" + seed + ", op=" + op + ", " + names.get(k));
		}
		for (int k = 0; k < executed.length; k++) {
			assertNotEquals(0, executed[k], "seed=" + seed + ": 操作 " + names.get(k) + " が一度も選ばれなかった");
		}
	}

	/**
	 * 1回の操作。at は失敗メッセージに含める位置情報である。
	 */
	@FunctionalInterface
	public interface Operation {
		void run(Random random, String at);
	}
}
