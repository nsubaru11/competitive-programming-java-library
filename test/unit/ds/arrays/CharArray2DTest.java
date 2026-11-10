package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class CharArray2DTest extends Array2DContract {

	Target create(final int h, final int w) {
		return Adapter.of(Target.class, new CharArray2D(h, w));
	}

	Target create(final int h, final int w, final long v) {
		return Adapter.of(Target.class, new CharArray2D(h, w, (char) v));
	}

	Target create(final int h, final int w, final long[] a) {
		return Adapter.of(Target.class, new CharArray2D(h, w, (char[]) Adapter.convert(a, char[].class)));
	}

	Target create(final long[][] a) {
		return Adapter.of(Target.class, new CharArray2D((char[][]) Adapter.convert(a, char[][].class)));
	}

	@Test
	void fromStrings() {
		// 各文字列の先頭 w 文字を使う
		final CharArray2D a = new CharArray2D(new String[]{"abc", "defg"});
		assertEquals(2, a.h());
		assertEquals(3, a.w());
		assertEquals('f', a.get(1, 2));
	}

	@Test
	void applyAndSetAllConvertToChar() {
		final CharArray2D a = new CharArray2D(new String[]{"ab"});
		assertEquals('c', a.apply(0, 0, (char) 2, (x, d) -> x + d));
		a.setAll((i, j) -> 'x' + j);
		assertEquals('y', a.get(0, 1));
	}

	@Test
	void toStringConcatenatesCharacters() {
		final CharArray2D a = new CharArray2D(new String[]{"ab", "cd"});
		assertEquals("ab\ncd", a.toString());
		a.rotateRight();
		assertEquals("ca\ndb", a.toString());
		assertEquals("", new CharArray2D(0, 0).toString());
	}
}
