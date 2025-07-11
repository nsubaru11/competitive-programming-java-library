import java.util.Iterator;

public class Array1D implements Iterable {
	public int[] array;
	public int size;
	/**
	 * 右方向にシフトした回数を記憶します。（左方向へのシフトはマイナスで表現）
	 */
	public int shiftCount = 0;

	public Array1D(int size) {
		this.size = size;
		this.array = new int[size];
	}

	public Array1D(int[] array) {
		this.array = array;
		this.size = array.length;
	}

	public Array1D(Array1D array) {
		this.array = array.array;
		this.size = array.size;
	}

	public int get(int index) {
		index = ((index + shiftCount) % size + size) % size;
		return array[index];
	}

	public void set(int index, int value) {
		index += shiftCount;
		array[index] = value;
	}

	public void lShift() {
		shiftCount++;
	}

	public void rShift() {
		shiftCount--;
	}

	private int toIndex(int index) {
		return ((index + shiftCount) % size + size) % size;
	}

	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int index = 0;

			public boolean hasNext() {
				return index < size;
			}

			public Integer next() {
				return array[index++];
			}
		};
	}
}