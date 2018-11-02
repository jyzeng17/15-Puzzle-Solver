package io.github.jyzeng17;

import java.util.Scanner;
import java.util.PriorityQueue;
import java.time.Instant;
import java.time.Duration;
import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.ArrayList;
import java.util.Random;

public class Puzzle {
	public static final byte SIZE = 16;
	public static final byte EDGE_SIZE = 4;

	private State startingState;
	private State endingState;

	// bIDA* variables buffer
	private PriorityQueue<QueueElement> forwardLastPQ;
	private PriorityQueue<QueueElement> backwardLastPQ;
	private HashSet<State> forwardClosedStates;
	private HashSet<State> backwardClosedStates;

	// Timing
	private Instant startInstant;
	private Instant stopInstant;

	public Puzzle() {
		byte[] buffer = new byte[SIZE];
		for (int i = 0; i < buffer.length - 1; ++i) {
			buffer[i] = (byte)(i + 1);
		}
		buffer[buffer.length - 1] = 0;
		startingState = new State(buffer);
		endingState = startingState;
	}

	public Puzzle(byte[] buffer) {
		startingState = new State(buffer);

		// Calculate ending state (according to starting state's parity)
		for (int i = 0; i < buffer.length - 1; ++i) {
			buffer[i] = (byte)(i + 1);
		}
		buffer[buffer.length - 1] = 0;
		endingState = new State(buffer);
	}

	// Pass threshold as array to pass by reference
	public Node bDFS(PriorityQueue<QueueElement> lastPQ, int[] threshold, HashSet<State> closedStates, HashSet<QueueElement> goalHS) {
		QueueElement currentQE;
		Node nextChild;
		int nextChildCost;
		int leastCutOffCost = -1;

		// Print process information
		long counter = 0;

		// Initialize priority queue and reset last priority queue
		PriorityQueue<QueueElement> queue = new PriorityQueue<QueueElement>(lastPQ);
		lastPQ.clear();

		// While queue is not empty do
		while (!queue.isEmpty()) {
			// (current, N) = queue poll
			currentQE = queue.poll();

			// R = next best child of N (pick best move)
			nextChild = currentQE.nextBestChild();

			// If R == null => continue (all children of current are searched)
			if (nextChild == null) {
				continue;
			}

			nextChildCost = nextChild.getCost();
			// If cost of currentNode's child > threshold
			if (nextChildCost > threshold[0]) {
				// Record the least cost being cut off
				if (leastCutOffCost == -1) {
					leastCutOffCost = nextChildCost;
				}
				else {
					if (nextChildCost < leastCutOffCost) {
						leastCutOffCost = nextChildCost;
					}
				}

				lastPQ.offer(currentQE);
				continue;
			}

			// Offer (R, N) to queue (means R already searched)
			queue.offer(new QueueElement(currentQE.getCurrentChild() + 1, currentQE.getParent()));

			// If R already in closed states => continue (pruning)
			if (closedStates.contains(nextChild.getState())) {
				continue;
			}
			closedStates.add(nextChild.getState());

			++counter;

			// If R is ending state
			if (goalHS.contains(new QueueElement(-1, nextChild))) {
				System.out.println("ok, " + counter + " state(s) searched. Elapsed time: " + getElapsedTime() + " ms. Size of closedStates: " + closedStates.size());
				return nextChild;
			}

			// Offer (null, R) to queue (search deeper)
			queue.offer(new QueueElement(-1, nextChild));
		}
		// Update threshold value
		threshold[0] = leastCutOffCost;

		// Print process information
		System.out.println("ok, " + counter + " state(s) searched. Elapsed time: " + getElapsedTime() + " ms. Size of closedStates: " + closedStates.size());
		return null;
	}

	public Node[] bIDAStar() {
		byte counter = 0;

		// Check if starting state is ending state
		if (startingState.equals(endingState)) {
			// Success
			Node[] solution = new Node[2];
			solution[0] = new Node(new Path(), startingState, endingState);
			solution[1] = new Node(new Path(), startingState, endingState);
			return solution;
		}

		// Initialize forward closed states
		forwardClosedStates = new HashSet<State>();
		forwardClosedStates.add(startingState);

		// Initialize backward closed states
		backwardClosedStates = new HashSet<State>();
		backwardClosedStates.add(endingState);

		// Initialize forward last priority queue
		forwardLastPQ = new PriorityQueue<QueueElement>();
		forwardLastPQ.offer(new QueueElement(-1, new Node(new Path(), startingState, endingState)));

		// Initialize backward last priority queue
		backwardLastPQ = new PriorityQueue<QueueElement>();
		backwardLastPQ.offer(new QueueElement(-1, new Node(new Path(), endingState, startingState)));

		// Initialize threshold value (state cost)
		int[] forwardThreshold = {0};
		int[] backwardThreshold = {0};

		Node matchNode;
		Node[] solution = new Node[2];
		// While threshold is reasonable do (set true for testing)
		while (true) {
			// Print process information
			System.out.print("[" + ++counter + "] Forward searching threshold: " + forwardThreshold[0] + "... ");

			// Run forward DFS
			matchNode = bDFS(forwardLastPQ, forwardThreshold, forwardClosedStates, new HashSet<QueueElement>(backwardLastPQ));

			// If solution found
			if (matchNode != null) {
				// Success
				solution[0] = matchNode;
				// I'm not sure if this works
				while (!((solution[1] = backwardLastPQ.poll().getParent()).equals(matchNode)));
				return solution;
			}

			System.out.print("[" + ++counter + "] Backward searching threshold: " + backwardThreshold[0] + "... ");

			// Run backward DFS
			matchNode = bDFS(backwardLastPQ, backwardThreshold, backwardClosedStates, new HashSet<QueueElement>(forwardLastPQ));

			// If solution found
			if (matchNode != null) {
				// Success
				solution[1] = matchNode;
				// I'm not sure if this works
				while (!((solution[0] = forwardLastPQ.poll().getParent()).equals(matchNode)));
				return solution;
			}
			// Update threshold value (do in BDFS)
		}
	}

	public void startTiming() {
		startInstant = Instant.now();
	}

	public long getElapsedTime() {
		return Duration.between(startInstant, Instant.now()).toMillis();
	}

	public static void pauseSystem() {
		System.out.print("\nPress Any Key To Continue...");
		new Scanner(System.in).nextLine();
	}

	public State getStartingState() {
		return startingState;
	}

	public State getEndingState() {
		return endingState;
	}

	public void initialize() {
		ArrayList<Byte> digits = new ArrayList<Byte>();
		Random random = new Random();
		int randomInt;
		byte[] buffer;

		do {
			for (Byte b = 0; b < SIZE; ++b) {
				digits.add(b);
			}
			buffer = new byte[SIZE];
			for (int i = SIZE; i > 0; --i) {
				randomInt = random.nextInt(i);
				buffer[i - 1] = digits.remove(randomInt).byteValue();
			}
		}
		while (!isEvenParity(buffer));

		startingState = new State(buffer);
	}

	private boolean isEvenParity(byte[] buffer) {
		int f1 = 0;
		int f2 = 0;

		for (int i = 0; i < buffer.length; ++i) {
			if (buffer[i] == 0) {
				f2 = Math.floorDiv(i, EDGE_SIZE) + 1;
				continue;
			}
			for (int j = i + 1; j < buffer.length; ++j) {
				if (buffer[j] == 0) {
					continue;
				}
				if (buffer[i] > buffer[j]) {
					++f1;
				}
			}
		}

		if ((f1 + f2) % 2 == 1) {
			return false;
		}
		else {
			return true;
		}
	}
}
