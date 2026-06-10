import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GenerateTestInput {
	public static void main(String[] args) {
		String fileName = args.length > 0 ? args[0] : "large_input.txt";
		int numInts = 10_000_000;
		int numLongs = 10_000_000;
		int numStrings = 10_000_000;

		System.out.println("Generating test input file: " + fileName);
		System.out.println("Total entries: " + (numInts + numLongs + numStrings));

		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
			Random random = new Random();

			// Generate integers
			System.out.println("Generating " + numInts + " integers...");
			for (int i = 0; i < numInts; i++) {
				writer.print(random.nextInt());
				writer.print(" ");
			}
			writer.println();

			// Generate longs
			System.out.println("Generating " + numLongs + " longs...");
			for (int i = 0; i < numLongs; i++) {
				writer.print(random.nextLong());
				writer.print(" ");
			}
			writer.println();

			// Generate strings
			System.out.println("Generating " + numStrings + " strings...");
			for (int i = 0; i < numStrings; i++) {
				writer.print(generateRandomString(random, 10));
				writer.print(" ");
			}
			writer.println();

			System.out.println("Test input file generated successfully.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String generateRandomString(Random random, int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
}
