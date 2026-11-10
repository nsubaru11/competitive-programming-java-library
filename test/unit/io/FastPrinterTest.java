package unit.io;

import java.io.*;

import lib.io.*;

final class FastPrinterTest extends PrinterContract {

	protected Object create(final OutputStream out, final int bufferSize, final boolean autoFlush) {
		return new FastPrinter(out, bufferSize, autoFlush);
	}
}
