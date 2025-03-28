import java.io.PrintStream;
import java.util.Arrays;

public class Example {

	public static void main(String[] args) {
		PrintStream out = System.out;
		char[] chars = {'a', 'b', 'c', 'c', 'b', 'a', 'b', 'c', 'c', 'b', 'a', 'a', 'b', 'c', 'd'};
		out.println(chars.length);
		Manacher mc = new Manacher(chars);
		out.println("Manacher");
		out.println(Arrays.toString(mc.getRadii()));
		out.println(mc.getEvenLongestLen());
		out.println(mc.getOddLongestLen());
		out.println(mc.getLongestLen());
		out.println(mc.getEvenPalindromeLengthAt(2));
		out.println(mc.getOddPalindromeLengthAt(5));
	}

}
