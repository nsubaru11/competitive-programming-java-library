package unit.ds.set;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.set.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class AVLMultisetTest extends MultisetContract {

	Target create() {
		return Adapter.of(Target.class, new AVLMultiset<Long>());
	}

	Collection<?> collection(final long... values) {
		return Arrays.stream(values).boxed().toList();
	}

	@Test
	void comparator() {
		final AVLMultiset<String> s = new AVLMultiset<String>(Comparator.reverseOrder());
		for (final String w : List.of("a", "c", "b", "c")) s.add(w);
		assertEquals("[c, c, b, a]", s.toString());
		assertEquals(2L, s.count("c"));
	}
}
