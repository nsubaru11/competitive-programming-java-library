
public abstract class AbstractRingBuffer {
	protected int head, size, capacity;

	public AbstractRingBuffer(int capacity) {
		if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
		this.capacity = capacity;
		this.size = this.head = 0;
	}

	public AbstractRingBuffer() {
		this(1024);
	}

	public final int size() {
		return size;
	}

	public final int capacity() {
		return capacity;
	}

	public final int remainingCapacity() {
		return capacity - size;
	}

	public final boolean isEmpty() {
		return size == 0;
	}

	public final boolean isFull() {
		return size == capacity;
	}

	public final int physicalIndex(int logicalIndex) {
		return (head + logicalIndex) % capacity;
	}

	public void clear() {
		head = size = 0;
	}
}