package unit.ds.set;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.set.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class AVLSetTest extends SortedSetContract {

	Target create() {
		return Adapter.of(Target.class, new AVLSet<Long>());
	}

	Collection<?> collection(final long... values) {
		return Arrays.stream(values).boxed().toList();
	}

	@Test
	void comparator() {
		final AVLSet<String> s = new AVLSet<>(Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()));
		for (final String w : List.of("bb", "a", "ccc", "aa")) s.add(w);
		assertEquals("[a, bb, aa, ccc]", s.toString());
		assertEquals("bb", s.getByIndex(1));
		assertEquals("aa", s.higher("bb"));
	}
}
