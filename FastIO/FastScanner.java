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

//  ------------------------ 定数 ------------------------

	/**
	 * 入力用内部バッファのデフォルトサイズ（バイト単位）。
	 */
	private static final int DEFAULT_BUFFER_SIZE = 65536;

//  --------------------- インスタンス変数 ---------------------

	/**
	 * 入力元の InputStream です。デフォルトは {@code System.in} です。
	 */
	private final InputStream in;

	/**
	 * 入力データを一時的に格納する内部バッファです。<br>
	 * 読み込み時に {@link #read()} でデータを取得し、バッファから消費します。
	 */
	private final byte[] buffer;

	/**
	 * 現在のバッファ内で次に読み込む位置
	 */
	private int pos = 0;

	/**
	 * バッファに読み込まれているバイト数
	 */
	private int bufferLength = 0;

//  ---------------------- コンストラクタ ----------------------

	/**
	 * デフォルトの設定でFastScannerを初期化します。<br>
	 * バッファ容量: 65536 <br>
	 * InputStream: System.in <br>
	 */
	public FastScanner() {
		this(System.in, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定されたInputStreamを用いてFastScannerを初期化します。<br>
	 * バッファ容量: 65536 <br>
	 *
	 * @param in 入力元の InputStream
	 */
	public FastScanner(InputStream in) {
		this(in, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定されたバッファ容量でFastScannerを初期化します。<br>
	 * InputStream: System.in <br>
	 *
	 * @param bufferSize 内部バッファの容量（文字単位）
	 */
	public FastScanner(int bufferSize) {
		this(System.in, bufferSize);
	}

	/**
	 * 指定されたバッファ容量とInputStreamでFastScannerを初期化します。<br>
	 *
	 * @param in         入力元の InputStream
	 * @param bufferSize 内部バッファの容量（文字単位）
	 */
	public FastScanner(InputStream in, int bufferSize) {
		this.in = in;
		this.buffer = new byte[bufferSize];
	}

//  -------------------- オーバーライドメソッド --------------------

	/**
	 * このInputStreamを閉じます。
	 * 入力元が {@code System.in} の場合、閉じません。
	 *
	 * @throws IOException {@code close}の際にエラーが発生した場合
	 */
	@Override
	public void close() throws IOException {
		if (in != System.in)
			in.close();
	}

	/**
	 * 内部バッファから 1 バイトを読み込みます。<br>
	 * バッファが空の場合、新たにデータを読み込みます。
	 *
	 * @return 読み込んだバイト
	 * @throws RuntimeException 入力終了または I/O エラー時
	 */
	public byte read() {
		if (pos >= bufferLength) {
			try {
				bufferLength = in.read(buffer, pos = 0, buffer.length);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (bufferLength < 0) {
				throw new RuntimeException(new IOException("End of input reached"));
			}
		}
		return buffer[pos++];
	}

//  -------------------- 基本入力メソッド --------------------

	/**
	 * 次の int 値を読み込みます。
	 *
	 * @return 読み込んだ int 値
	 */
	public int nextInt() {
		int b = read();
		while (isDelimiter(b)) b = read();
		boolean negative = b == '-';
		if (negative) b = read();
		int result = 0;
		while ('0' <= b && b <= '9') {
			result = result * 10 + b - '0';
			b = read();
		}
		return negative ? -result : result;
	}

	/**
	 * 次の long 値を読み込みます。
	 *
	 * @return 読み込んだ long 値
	 */
	public long nextLong() {
		int b = read();
		while (isDelimiter(b)) b = read();
		boolean negative = b == '-';
		if (negative) b = read();
		long result = 0;
		while ('0' <= b && b <= '9') {
			result = result * 10 + b - '0';
			b = read();
		}
		return negative ? -result : result;
	}

	/**
	 * 次の double 値を読み込みます。
	 *
	 * @return 読み込んだ double 値
	 */
	public double nextDouble() {
		int b = read();
		while (isDelimiter(b)) b = read();
		boolean negative = b == '-';
		if (negative) b = read();
		double result = 0;
		while ('0' <= b && b <= '9') {
			result = result * 10 + b - '0';
			b = read();
		}
		if (b == '.') {
			b = read();
			double factor = 10;
			while ('0' <= b && b <= '9') {
				result += (b - '0') / factor;
				factor *= 10;
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
	public char nextChar() {
		byte b = read();
		while (isDelimiter(b)) b = read();
		return (char) b;
	}

	/**
	 * 次の String（空白で区切られた文字列）を読み込みます。
	 *
	 * @return 読み込んだ String
	 */
	public String next() {
		return nextStringBuilder().toString();
	}

	/**
	 * 次の StringBuilder（空白で区切られた文字列）を読み込みます。
	 *
	 * @return 読み込んだ StringBuilder
	 */
	public StringBuilder nextStringBuilder() {
		StringBuilder sb = new StringBuilder();
		byte b = read();
		while (isDelimiter(b)) b = read();
		while (!isDelimiter(b)) {
			sb.appendCodePoint(b);
			b = read();
		}
		return sb;
	}

	/**
	 * 次の1行を読み込みます。（改行文字は読み飛ばされます）
	 *
	 * @return 読み込んだ String
	 */
	public String nextLine() {
		StringBuilder sb = new StringBuilder();
		int b = read();
		while (b != 0 && b != '\r' && b != '\n') {
			sb.appendCodePoint(b);
			b = read();
		}
		return sb.toString();
	}

	/**
	 * 次のトークンを BigInteger として読み込みます。
	 *
	 * @return 読み込んだ BigInteger
	 */
	public BigInteger nextBigInteger() {
		return new BigInteger(next());
	}

	/**
	 * 次のトークンを BigDecimal として読み込みます。
	 *
	 * @return 読み込んだ BigDecimal
	 */
	public BigDecimal nextBigDecimal() {
		return new BigDecimal(next());
	}

	// -------------------- プライベートヘルパーメソッド --------------------

	/**
	 * 指定した文字コードが空白文字（' '、'\n'、'\r'）かどうか判定します。
	 *
	 * @param c 判定対象の文字コード
	 * @return 空白文字の場合 true、それ以外の場合 false
	 */
	private boolean isDelimiter(int c) {
		return ' ' == c || '\n' == c || '\r' == c;
	}
}
