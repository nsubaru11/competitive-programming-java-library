package verify.math.primetable;

import java.util.*;
import java.util.function.*;

import lib.math.*;

public final class FactorizationTest {
	public static void main(String[] args) {
		PrimeTable small = new PrimeTable(40);
		if (small.countPrimesUpTo(40) != 12 || small.get(11) != 37 || !small.isPrime(37)) {
			throw new AssertionError();
		}
		PrimeTable table = new PrimeTable(1_000_000);
		if (table.countPrimesUpTo(1_000_000) != 78_498 || !table.isPrime(999_983)) {
			throw new AssertionError();
		}
		if (table.uniquePrimeFactorCount(360) != 3) throw new AssertionError();
		if (table.primeFactorCount(360) != 6) throw new AssertionError();
		if (!Arrays.equals(table.primeFactors(360), new int[]{2, 2, 2, 3, 3, 5})) {
			throw new AssertionError();
		}
		if (!Arrays.equals(table.primeFactors(360L), new long[]{2, 2, 2, 3, 3, 5})) {
			throw new AssertionError();
		}
		if (!Arrays.deepEquals(table.primeFactors2D(360), new int[][]{{2, 3, 5}, {3, 2, 1}})) {
			throw new AssertionError();
		}
		if (!Arrays.deepEquals(table.primeFactors2D(360L), new long[][]{{2, 3, 5}, {3, 2, 1}})) {
			throw new AssertionError();
		}
		if (!table.<LinkedHashMap<Integer, Integer>>primeFactorsMap(360, LinkedHashMap::new).equals(Map.of(2, 3, 3, 2, 5, 1))) {
			throw new AssertionError();
		}
		if (!table.primeFactorsMap(360L, LinkedHashMap::new).equals(Map.of(2L, 3, 3L, 2, 5L, 1))) {
			throw new AssertionError();
		}
		if (!table.primeFactorsMap(360, (Supplier<HashMap<Integer, Integer>>) HashMap::new).equals(Map.of(2, 3, 3, 2, 5, 1))) {
			throw new AssertionError();
		}
		if (!table.<ArrayList<Integer>>primeFactors(360, ArrayList::new).equals(List.of(2, 2, 2, 3, 3, 5))) {
			throw new AssertionError();
		}
		if (!table.primeFactors(360L, ArrayList::new).equals(List.of(2L, 2L, 2L, 3L, 3L, 5L))) {
			throw new AssertionError();
		}
		if (!Arrays.equals(table.primeFactors(600_851_475_143L), new long[]{71, 839, 1471, 6857})) {
			throw new AssertionError();
		}
		if (table.primeFactors2D(1).length != 2 || table.primeFactors2D(1)[0].length != 0) {
			throw new AssertionError();
		}
		if (table.primeFactors(-1).length != 0 || !table.<HashMap<Integer, Integer>>primeFactorsMap(-1, HashMap::new).isEmpty()) {
			throw new AssertionError();
		}
	}
}
