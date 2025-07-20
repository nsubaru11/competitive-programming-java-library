import static java.lang.System.out;

public class Example {

	public static void main(String[] args) {
		for (int i = 0; i < 1000000000; i++) {
			NumberTheoryUtils.fastGCD(923490024, 825000390);
		}
		for (int i = 0; i < 1000000000; i++) {
			NumberTheoryUtils.GCD(923490024, 825000390);
		}
		for (int i = 0; i < 1000000000; i++) {
			NumberTheoryUtils.fastGCD(923490024, 825000390);
		}
		for (int i = 0; i < 1000000000; i++) {
			NumberTheoryUtils.GCD(923490024, 825000390);
		}
	}

}