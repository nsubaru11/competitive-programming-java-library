import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 競技プログラミング向けの高速入力クラスです。
 * 内部バッファを利用して InputStream からの入力を効率的に処理します。
 * ※ ASCII 範囲外の文字は正しく処理できません。入力は半角スペースまたは改行で区切られていることを前提とします.
 */
@SuppressWarnings("unused")
public class FastScanner implements AutoCloseable {

	/* ------------------------ 定数 ------------------------ */

	/**
	 * 入力用内部バッファのデフォルトサイズ（バイト単位）
	 */
	private static final int DEFAULT_BUFFER_SIZE = 65536;

	/* ------------------------ インスタンス変数 ------------------------ */

	/**
	 * 入力元の {@code InputStream}（通常は {@code System.in}）
	 */
	private final InputStream in;

	/**
	 * 入力データを一時的に格納する内部バッファです。
	 * 読み込み時に {@link #read()} でデータを取得し、バッファから消費します。
	 */
	private final byte[] buffer;

	/**
	 * バッファ内で次に読み込む位置
	 */
	private int pos = 0;

	/**
	 * バッファに読み込まれている有効なバイト数
	 */
	private int bufferLength = 0;

	/* ------------------------ コンストラクタ ------------------------ */

	/**
	 * デフォルトの設定で {@code FastScanner} を初期化します。
	 * <ul>
	 *   <li>バッファ容量: 65536</li>
	 *   <li>{@code InputStream}: {@code System.in}</li>
	 * </ul>
	 */
	public FastScanner() {
		this(System.in, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定された {@code InputStream} を使用して {@code FastScanner} を初期化します。
	 * <ul>
	 *   <li>バッファ容量: 65536</li>
	 * </ul>
	 *
	 * @param in 入力元の {@code InputStream}
	 */
	public FastScanner(final InputStream in) {
		this(in, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定されたバッファ容量を用いて {@code FastScanner} を初期化します。
	 * <ul>
	 *   <li>入力元: {@code System.in}</li>
	 * </ul>
	 *
	 * @param bufferSize 内部バッファの容量（バイト単位）
	 */
	public FastScanner(final int bufferSize) {
		this(System.in, bufferSize);
	}

	/**
	 * 指定された {@code InputStream} とバッファ容量で {@code FastScanner} を初期化します。
	 *
	 * @param in         入力元の {@code InputStream}
	 * @param bufferSize 内部バッファの容量（バイト単位）
	 */
	public FastScanner(final InputStream in, final int bufferSize) {
		this.in = in;
		this.buffer = new byte[bufferSize];
	}

	/* ------------------------ プライベートヘルパーメソッド ------------------------ */

	/**
	 * 空白文字を読み飛ばし次の有効文字を返します。
	 * このメソッドでは、Asciiコード32以下を空白文字として扱います。
	 *
	 * @return 有効文字のint値
	 */
	private int skipSpaces() {
		int b = read();
		while (b <= 32) b = read();
		return b;
	}

	/* ------------------------ オーバーライドメソッド ------------------------ */

	/**
	 * このスキャナが使用する {@code InputStream} を閉じます。
	 * 入力元が {@code System.in} の場合は閉じません。
	 *
	 * @throws RuntimeException {@code InputStream} のクローズ中にエラーが発生した場合
	 */
	@Override
	public final void close() {
		try {
			if (in != System.in) in.close();
			pos = 0;
			bufferLength = 0;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 内部バッファから 1 バイトを読み込みます。
	 * バッファが空の場合は新たにデータを読み込みます。
	 *
	 * @return 読み込んだバイト (0-255)
	 * @throws RuntimeException I/Oエラーが発生した場合、またはストリームの終端に達した場合 (内部で {@code IOException} または {@code EOFException} をラップします)
	 */
	private int read() {
		if (pos >= bufferLength) {
			try {
				bufferLength = in.read(buffer, pos = 0, buffer.length);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (bufferLength <= 0) throw new RuntimeException(new EOFException());
		}
		return buffer[pos++] & 0xFF;
	}

	/**
	 * 内部バッファから 次の空白でない 1 バイトを確認します。
	 * バッファが空の場合は新たにデータを読み込みます。
	 *
	 * @return 次の非空白文字のバイト (0-255)、またはストリームの終端に達した場合は 0
	 */
	public final int peek() {
		try {
			int b = skipSpaces();
			pos--;
			return b;
		} catch (RuntimeException e) {
			return 0;
		}
	}

	/**
	 * バッファにデータが残っているかどうかを確認します。
	 *
	 * @return true: EOFではない, false: EOF
	 */
	public final boolean hasNext() {
		return peek() != 0;
	}

	/* ------------------------ 基本入力メソッド ------------------------ */

	/**
	 * 次のトークンを {@code int} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code int} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final int nextInt() {
		int b = skipSpaces();
		boolean negative = false;
		if (b == '-') {
			negative = true;
			b = read();
		}
		int result = 0;
		do {
			result = (result << 3) + (result << 1) + (b & 15);
			b = read();
		} while (b >= '0' && b <= '9');
		return negative ? -result : result;
	}

	/**
	 * 次のトークンを {@code long} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code long} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final long nextLong() {
		int b = skipSpaces();
		boolean negative = false;
		if (b == '-') {
			negative = true;
			b = read();
		}
		long result = 0;
		do {
			result = (result << 3) + (result << 1) + (b & 15);
			b = read();
		} while (b >= '0' && b <= '9');
		return negative ? -result : result;
	}

	/**
	 * 次のトークンを {@code double} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code double} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final double nextDouble() {
		int b = skipSpaces();
		boolean negative = false;
		if (b == '-') {
			negative = true;
			b = read();
		}
		long intPart = 0;
		do {
			intPart = (intPart << 3) + (intPart << 1) + (b & 15);
			b = read();
		} while (b >= '0' && b <= '9');
		double result = intPart;
		if (b == '.') {
			b = read();
			double scale = 0.1;
			do {
				result += (b & 15) * scale;
				scale *= 0.1;
				b = read();
			} while (b >= '0' && b <= '9');
		}
		return negative ? -result : result;
	}

	/**
	 * 次の非空白文字を {@code char} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code char} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final char nextChar() {
		int b = skipSpaces();
		return (char) b;
	}

	/**
	 * 次のトークンを {@code String} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code String} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final String next() {
		return nextStringBuilder().toString();
	}

	/**
	 * 次のトークンを {@code StringBuilder} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code StringBuilder} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final StringBuilder nextStringBuilder() {
		final StringBuilder sb = new StringBuilder();
		int b = skipSpaces();
		do {
			sb.append((char) b);
			b = read();
		} while (b > 32);
		return sb;
	}

	/**
	 * 次の改行コード ('\r', '\n') または、ストリームの終端に達するまでを {@code String} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code String} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final String nextLine() {
		final StringBuilder sb = new StringBuilder();
		int b = read();
		while (b != 0 && b != '\n' && b != '\r') {
			sb.append((char) b);
			b = read();
		}
		if (b == '\r') {
			int c = read();
			if (c != '\n') pos--;
		}
		return sb.toString();
	}

	/**
	 * 次のトークンを {@code BigInteger} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code BigInteger} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final BigInteger nextBigInteger() {
		return new BigInteger(next());
	}

	/**
	 * 次のトークンを {@code BigDecimal} として解釈し、その値を返します。
	 *
	 * @return 読み込んだ {@code BigDecimal} 値
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public final BigDecimal nextBigDecimal() {
		return new BigDecimal(next());
	}

}
