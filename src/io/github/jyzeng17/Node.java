package io.github.jyzeng17;

public class Node implements Comparable<Node> {
	private Path path;
	private State state;
	private State endingState;

	public Node(Path p, State s, State eS) {
		path = p;
		state = s;
		endingState = eS;
	}

	// Comparable interface method
	public int compareTo(Node n) {
		// Lower cost has higher priority
		// But the PriorityQueue sorts in increasing order
		// So here higher cost has higher PriorityQueue priority
		return getCost() - n.getCost();
	}

	// Implement for queue's contain() method
	public boolean equals(Object o) {
		Node n = (Node)o;

		if (n == null)
			return false;

		//return ((path.equals(n.getPath())) && (state.equals(n.getState())));
		return state.equals(n.getState());
	}

	// Implement for HashSet's contain() method
	public int hashCode() {
		return state.hashCode();
	}

	public Path getPath() {
		return path;
	}

	public State getState() {
		return state;
	}

	public Node nextBestChild(int currentChild) {
		return state.nextBestChild(path, currentChild, endingState);
	}

	public int getCost() {
		// Path length + Manhatton distance
		int cost = path.getSize();

		// Initializing ending state's digits' positions' indexes
		int[] endingIndex = new int[Puzzle.SIZE];
		for (int i = 0; i < endingIndex.length; ++i) {
			endingIndex[endingState.get(i)] = i;
		}

		// Initializing current state's digits' positions' indexes
		int[] currentIndex = new int[Puzzle.SIZE];
		for (int i = 0; i < currentIndex.length; ++i) {
			currentIndex[state.get(i)] = i;
		}

		for (int i = 1; i < Puzzle.SIZE; ++i) {
			cost += distanceBetween(currentIndex[i], endingIndex[i]);
		}

		return cost;
	}

	private int indexToX(int index) {
		// Left top is (0, 0)
		return index % Puzzle.EDGE_SIZE;
	}

	private int indexToY(int index) {
		// Left top is (0, 0)
		return (index - (index % Puzzle.EDGE_SIZE)) / Puzzle.EDGE_SIZE;
	}

	private int distanceBetween(int index1, int index2) {
		int x1 = indexToX(index1);
		int y1 = indexToY(index1);
		int x2 = indexToX(index2);
		int y2 = indexToY(index2);

		return (((x1 > x2)? (x1 - x2) : (x2 - x1)) + ((y1 > y2)? (y1 - y2) : (y2 - y1)));
	}

}
