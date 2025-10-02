import static java.lang.System.out;

public final class Example {

	public static void main(String[] args) {
		for (int i = 0; i < 1000000000; i++) {
			out.println(NumberTheoryUtils.fastGCD(923490024, 825000390));
		}
		for (int i = 0; i < 1000000000; i++) {
			out.println(NumberTheoryUtils.GCD(923490024, 825000390));
		}
		for (int i = 0; i < 1000000000; i++) {
			out.println(NumberTheoryUtils.fastGCD(923490024, 825000390));
		}
		for (int i = 0; i < 1000000000; i++) {
			out.println(NumberTheoryUtils.GCD(923490024, 825000390));
		}
	}

}
