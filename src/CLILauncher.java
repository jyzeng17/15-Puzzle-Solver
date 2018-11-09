import io.github.jyzeng17.*;
import java.util.Scanner;

public class CLILauncher {
	public static void main(String[] args) {
		// Read starting state
		Scanner scanner = new Scanner(System.in);
		byte[] buffer = new byte[Puzzle.SIZE];
		System.out.println("\nEnter the starting state:");
		for (int i = 0; i < buffer.length; ++i) {
			if (scanner.hasNextByte()) {
				buffer[i] = scanner.nextByte();
			}
			else {
				System.out.println("Error: you didn't provide enough digits to create a puzzle.");
			}
		}
		Puzzle puzzle = new Puzzle(buffer);

		System.out.println("\nExpected cost: " + new Node(new Path(), puzzle.getStartingState(), puzzle.getEndingState()).getCost());

		System.out.println("\n[Start BIDA*]");

		puzzle.startTiming();

		// Call ida*
		Node[] solution = puzzle.bIDAStar();

		long elapsedTime = puzzle.getElapsedTime();

		System.out.println("[Finished]");

		// Print result
		System.out.println("");
		System.out.println("[Starting State]");
		puzzle.getStartingState().print();
		System.out.println("[Matching State]");
		solution[0].getState().print();
		solution[1].getState().print();
		System.out.println("[Ending State]");
		puzzle.getEndingState().print();
		System.out.println("\nTotal number of plys: " + (solution[0].getPath().getSize() + solution[1].getPath().getSize()));
		solution[0].getPath().print();
		solution[1].getPath().printReversely();

		System.out.println("\nOriginally expected cost: " + new Node(new Path(), puzzle.getStartingState(), puzzle.getEndingState()).getCost());

		// Print process time
		System.out.println("\nSearching time: " + elapsedTime + " ms");

		scanner.close();
	}
}
