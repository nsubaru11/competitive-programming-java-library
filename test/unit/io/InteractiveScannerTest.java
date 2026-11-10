package unit.io;

import java.io.*;
import java.nio.charset.*;
import java.util.function.*;

import lib.io.*;

final class InteractiveScannerTest extends ScannerContract {

	protected Object create(final String input) {
		final InputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
		return new InteractiveScanner(in, 7);
	}

	protected Object charPredicate(final Predicate<Character> p) {
		return (InteractiveScanner.CharPredicate) p::test;
	}
}
