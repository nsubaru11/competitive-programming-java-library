import java.util.Iterator;

public class Array2D implements Iterable {
	public int[] array;
	public int hw, h, w;
	public int rotateCount = 0;

	public Array2D(int h, int w) {
		this.h = h;
		this.w = w;
		this.hw = h * w;
		this.array = new int[hw];
	}

	public Array2D(Array2D array) {
		this.h = array.h;
		this.w = array.w;
		this.hw = array.hw;
		this.array = array.array;
	}

	public int get(int i, int j) {
		return switch (rotateCount & 3) {
			case 0 -> array[i * w + j];
			case 1 -> array[j * w - i + w - 1];
			case 2 -> array[hw - i * w - j - 1];
			case 3 -> array[hw - j * w + i - w];
			default -> throw new IllegalStateException("Unexpected value: " + rotateCount);
		};
	}

	public void set(int i, int j, int value) {
		array[i * w + j] = value;
	}

	public void lRotate() {
		rotateCount++;
	}

	public void rRotate() {
		rotateCount--;
	}


	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int index = 0;

			public boolean hasNext() {
				return index < hw;
			}

			public Integer next() {
				return array[index++];
			}
		};
	}
}