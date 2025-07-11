public class Example {
	public static void main(String[] args) {
		int[] arr = new int[10];
		System.out.println(arr[-1]);
		Array2D array2D = new Array2D(2, 3);
		int v = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				array2D.set(i, j, v++);
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(array2D.get(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
		array2D.rRotate();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.print(array2D.get(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
		array2D.rRotate();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(array2D.get(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
		array2D.rRotate();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.print(array2D.get(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}