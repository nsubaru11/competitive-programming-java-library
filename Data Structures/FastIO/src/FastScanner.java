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
	 * @throws IOException {@code InputStream} のクローズ中にエラーが発生した場合
	 */
	@Override
	public void close() throws IOException {
		if (in != System.in) in.close();
	}

	/**
	 * 内部バッファから 1 バイトを読み込みます。
	 * バッファが空の場合は新たにデータを読み込みます。
	 *
	 * @return 読み込んだバイト
	 * @throws RuntimeException 入力終了または I/O エラー時
	 */
	private int read() {
		int p = pos;
		int len = bufferLength;
		if (p < len) {
			pos = p + 1;
			return buffer[p] & 0xFF;
		}
		pos = 0;
		try {
			len = in.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (len <= 0) throw new RuntimeException(new EOFException());
		bufferLength = len;
		return buffer[pos++] & 0xFF;
	}

	/* ------------------------ 基本入力メソッド ------------------------ */

	/**
	 * 次の int 値を読み込みます。
	 *
	 * @return 読み込んだ int 値
	 */
	public final int nextInt() {
		int b = skipSpaces();
		boolean negative = false;
		if (b != 45) {
		} else {
			negative = true;
			b = read();
		}
		int result = 0;
		while (48 <= b && b <= 57) {
			result = (result << 3) + (result << 1) + (b & 15);
			b = read();
		}
		return negative ? -result : result;
	}

	/**
	 * 次の long 値を読み込みます。
	 *
	 * @return 読み込んだ long 値
	 */
	public final long nextLong() {
		int b = skipSpaces();
		boolean negative = false;
		if (b != 45) {
		} else {
			negative = true;
			b = read();
		}
		long result = 0;
		while (48 <= b && b <= 57) {
			result = (result << 3) + (result << 1) + (b & 15);
			b = read();
		}
		return negative ? -result : result;
	}

	/**
	 * 次の double 値を読み込みます。
	 *
	 * @return 読み込んだ double 値
	 */
	public final double nextDouble() {
		int b = skipSpaces();
		boolean negative = false;
		if (b != 45) {
		} else {
			negative = true;
			b = read();
		}
		double result = 0;
		while (48 <= b && b <= 57) {
			result = result * 10 + (b & 15);
			b = read();
		}
		if (b == 46) {
			b = read();
			double scale = 0.1;
			while (48 <= b && b <= 57) {
				result += (b & 15) * scale;
				scale *= 0.1;
				b = read();
			}
		}
		return negative ? -result : result;
	}

	/**
	 * 次の char 値（非空白文字）を読み込みます。
	 *
	 * @return 読み込んだ char 値
	 */
	public final char nextChar() {
		int b = skipSpaces();
		return (char) b;
	}

	/**
	 * 次の String（空白で区切られた文字列）を読み込みます。
	 *
	 * @return 読み込んだ String
	 */
	public final String next() {
		return nextStringBuilder().toString();
	}

	/**
	 * 次の {@code StringBuilder}（空白で区切られた文字列）を読み込みます。
	 *
	 * @return 読み込んだ {@code StringBuilder}
	 */
	public final StringBuilder nextStringBuilder() {
		final StringBuilder sb = new StringBuilder();
		int b = skipSpaces();
		while (b > 32) {
			sb.append((char) b);
			b = read();
		}
		return sb;
	}

	/**
	 * 次の1行を読み込みます。（改行文字は読み飛ばされます）
	 *
	 * @return 読み込んだ 1 行の String
	 */
	public final String nextLine() {
		final StringBuilder sb = new StringBuilder();
		int b = read();
		while (b != 0 && b != 10 && b != 13) {
			sb.append((char) b);
			b = read();
		}
		// 改行文字を読み飛ばす
		if (b == 13) {
			int c = read();
			if (c != 10) pos--;
		}
		return sb.toString();
	}

	/**
	 * 次のトークンを {@code BigInteger} として読み込みます。
	 *
	 * @return 読み込んだ {@code BigInteger}
	 */
	public final BigInteger nextBigInteger() {
		return new BigInteger(next());
	}

	/**
	 * 次のトークンを {@code BigDecimal} として読み込みます。
	 *
	 * @return 読み込んだ {@code BigDecimal}
	 */
	public final BigDecimal nextBigDecimal() {
		return new BigDecimal(next());
	}

}
