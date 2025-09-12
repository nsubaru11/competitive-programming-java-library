import java.util.Iterator;

public class Example {

	public static void main(String[] args) {
		int n = 10;
		AVLList<Integer> list = new AVLList<>();
		AVLSet<Integer> set = new AVLSet<>();
		for (int i = 0; i < n; i++) {
			int random = (int) (Math.random() * n);
			list.add(random);
			set.add(random);
			System.out.print("a list:");
			for (int j : list) {
				System.out.print(" " + j);
			}

			System.out.print("\nb list:");
			for (Iterator<Integer> it = list.distinctIterator(); it.hasNext(); ) {
				int j = it.next();
				System.out.print(" " + j);
			}

			System.out.print("\na  set:");
			for (int j : set) {
				System.out.print(" " + j);
			}
			System.out.println('\n');
		}
	}

}