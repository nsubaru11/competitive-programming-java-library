package unit.io.compat17;

import java.io.*;

import lib.io.compat17.*;

import unit.io.*;

final class FastPrinterTest extends PrinterContract {

	protected Object create(final OutputStream out, final int bufferSize, final boolean autoFlush) {
		return new FastPrinter(out, bufferSize, autoFlush);
	}
}
