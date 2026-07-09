public final class Dice {
	private static final int U = 0, D = 1, F = 2, B = 3, L = 4, R = 5;
	public final int[] dice;

	public Dice() {
		this(1, 6, 3, 4, 2, 5);
	}

	public Dice(final int u, final int d, final int f, final int b, final int l, int r) {
		dice = new int[6];
		dice[U] = u;
		dice[D] = d;
		dice[F] = f;
		dice[B] = b;
		dice[L] = l;
		dice[R] = r;
	}

	public int u() {
		return dice[U];
	}

	public int d() {
		return dice[D];
	}

	public int f() {
		return dice[F];
	}

	public int b() {
		return dice[B];
	}

	public int l() {
		return dice[L];
	}

	public int r() {
		return dice[R];
	}

	public void rotateUR() {
		int right = dice[R];
		dice[R] = dice[U];
		dice[U] = dice[L];
		dice[L] = dice[D];
		dice[D] = right;
	}

	public void rotateFR() {
		int right = dice[R];
		dice[R] = dice[F];
		dice[F] = dice[L];
		dice[L] = dice[B];
		dice[B] = right;
	}

	public void rotateUF() {
		int front = dice[F];
		dice[F] = dice[U];
		dice[U] = dice[B];
		dice[B] = dice[D];
		dice[D] = front;
	}
}
