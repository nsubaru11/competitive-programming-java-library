import java.util.Arrays;

public class Example {

	public static void main(String[] args) {
		int[] arr = {1, 2, 2, 3, 4, 5, 7, 9, 11, 12, 12, 15};
		System.out.println(Arrays.toString(arr));
		BinarySearch bs = new BinarySearch(arr);
		for (int i = 0; i < arr.length; i++) {
			int t = (int) (Math.random() * 15);
			AbstractBinarySearch search = new AbstractBinarySearch() {
				@Override
				public int comparator(long m) {
					int i = (int) m;
					return Long.compare(arr[i], t);
				}
			};
			System.out.println(t);
			System.out.print("normal search: " + search.normalSearch(0, arr.length) + " " + bs.normalSearch(t));
			System.out.print(", lower search: " + search.lowerBoundSearch(0, arr.length) + " " + bs.lowerBoundSearch(t));
			System.out.println(", upper search: " + search.upperBoundSearch(0, arr.length) + " " + bs.upperBoundSearch(t));
		}
	}

	private static class BinarySearch extends AbstractBinarySearch {
		private final int[] arr;
		private int target;

		public BinarySearch(int[] arr) {
			this.arr = arr;
		}

		public int normalSearch(int target) {
			this.target = target;
			return normalSearch(0, arr.length);
		}

		public int lowerBoundSearch(int target) {
			this.target = target;
			return lowerBoundSearch(0, arr.length);
		}

		public int upperBoundSearch(int target) {
			this.target = target;
			return upperBoundSearch(0, arr.length);
		}

		@Override
		public int comparator(long m) {
			int i = (int) m;
			return Integer.compare(arr[i], target);
		}
	}
}