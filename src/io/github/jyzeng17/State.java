package io.github.jyzeng17;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class State {
	private byte[] cell;

	public State(byte[] buffer) {
		cell = buffer.clone();
	}

	public byte get(int index) {
		return cell[index];
	}

	public void print() {
		for (int i = 0; i < cell.length; ++i) {
			System.out.printf("%2d" + (((i + 1) % Puzzle.EDGE_SIZE == 0)? "\n" : " "), cell[i]);
		}
		System.out.println("");
	}

	// Implement for queue's contain() method
	public boolean equals(Object o) {
		State s = (State)o;

		if (s == null)
			return false;

		return Arrays.equals(cell, s.getCell());
	}

	// Implement for HashSet's contain() method
	public int hashCode() {
		int result = 0;

		for (int i = 0; i < cell.length; ++i) {
			result += cell[i] * Math.pow(2, i);
		}

		return result;
	}

	public byte[] getCell() {
		return cell;
	}

	public Node nextBestChild(Path path, int lastChild, State endingState) {
		int zeroPosition = getZeroPosition();

		int numberOfChildren = 0;

		boolean[] hasChild = new boolean[Ply.values().length];
		for (int i = 0; i < hasChild.length; ++i) {
			if (hasChild[i] = hasChildWithPly(i, zeroPosition)) {
				++numberOfChildren;
			}
		}

		// Check if next best child exists
		if ((lastChild + 1) < numberOfChildren) {
			// Init a PriorityQueue
			PriorityQueue<Node> childrenPQ = new PriorityQueue<Node>();

			// Offer all children to PQ
			for (int plyIndex = 0; plyIndex < hasChild.length; ++plyIndex) {
				if (hasChild[plyIndex]) {
					childrenPQ.offer(getChildWithPly(plyIndex, zeroPosition, path, endingState));
				}
			}

			// Poll the element at corresponding index
			for (int i = 0; i < lastChild + 1; ++i) {
				childrenPQ.poll();
			}

			return childrenPQ.poll();
		}
		return null;
	}

	private int getZeroPosition() {
		for (int i = 0; i < cell.length; ++i) {
			if (cell[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	private boolean hasChildWithPly(int plyIndex, int zeroPosition) {
		switch (Ply.values()[plyIndex]) {
			case UP:
				// If 0's position not in first row, return true
				if (zeroPosition < Puzzle.EDGE_SIZE) {
					return false;
				}
				break;
			case DOWN:
				// If 0's position not in last row, return true
				if (zeroPosition > (Puzzle.SIZE - 1) - Puzzle.EDGE_SIZE) {
					return false;
				}
				break;
			case LEFT:
				// If 0's position not in first column , return true
				if (zeroPosition % Puzzle.EDGE_SIZE == 0) {
					return false;
				}
				break;
			case RIGHT:
				// If 0's position not in last column , return true
				if ((zeroPosition + 1) % Puzzle.EDGE_SIZE == 0) {
					return false;
				}
				break;
			default:
				break;
		}
		return true;
	}

	private Node getChildWithPly(int plyIndex, int zeroPosition, Path path, State endingState) {
		byte[] buffer = cell.clone();
		switch (Ply.values()[plyIndex]) {
			case UP:
				// Return a Node with 0 and 0's up cell swapping
				buffer[zeroPosition] = buffer[zeroPosition - Puzzle.EDGE_SIZE];
				buffer[zeroPosition - Puzzle.EDGE_SIZE] = 0;
				return new Node(new Path(path).add(Ply.UP), new State(buffer), endingState);
			case DOWN:
				// Return a Node with 0 and 0's down cell swapping
				buffer[zeroPosition] = buffer[zeroPosition + Puzzle.EDGE_SIZE];
				buffer[zeroPosition + Puzzle.EDGE_SIZE] = 0;
				return new Node(new Path(path).add(Ply.DOWN), new State(buffer), endingState);
			case LEFT:
				// Return a Node with 0 and 0's left cell swapping
				buffer[zeroPosition] = buffer[zeroPosition - 1];
				buffer[zeroPosition - 1] = 0;
				return new Node(new Path(path).add(Ply.LEFT), new State(buffer), endingState);
			case RIGHT:
				// Return a Node with 0 and 0's right cell swapping
				buffer[zeroPosition] = buffer[zeroPosition + 1];
				buffer[zeroPosition + 1] = 0;
				return new Node(new Path(path).add(Ply.RIGHT), new State(buffer), endingState);
			default:
				return null;
		}
	}
}
