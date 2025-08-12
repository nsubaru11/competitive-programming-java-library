import java.util.Iterator;

@SuppressWarnings("unused")
public class DoubleRingBuffer extends AbstractRingBuffer implements Iterable<Double> {
	private final double[] buf;

	public DoubleRingBuffer(int size) {
		super(size);
		buf = new double[size];
	}

	public DoubleRingBuffer() {
		this(1024);
	}

	/**
	 * バッファの末尾に要素を追加します。
	 *
	 * @param e 追加する要素
	 * @return バッファに正常に追加された場合はtrueを返します。 すでに満杯の場合はfalseを返します。
	 */
	public boolean addLast(final double e) {
		if (isFull()) grow();
		buf[physicalIndex(size++)] = e;
		return true;
	}

	/**
	 * バッファの先頭に要素を追加
	 *
	 * @param e 追加する要素
	 * @return バッファに正常に追加された場合はtrueを返します。 すでに満杯の場合はfalseを返します。
	 */
	public boolean addFirst(final double e) {
		if (isFull()) grow();
		head = physicalIndex(capacity - 1);
		buf[head] = e;
		size++;
		return true;
	}

	public double get(int i) {
		return buf[physicalIndex(i)];
	}

	private void grow() {

	}

	@Override
	public Iterator<Double> iterator() {
		return null;
	}
}