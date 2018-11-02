package io.github.jyzeng17;

import java.util.ArrayList;

public class Path {
	private ArrayList<Ply> path;

	public Path() {
		path = new ArrayList<Ply>();
	}

	public Path(Path p) {
		path = new ArrayList<Ply>(p.getPath());
	}

	public int getSize() {
		return path.size();
	}

	// Implement for queue's contain() method
	public boolean equals(Object o) {
		Path p = (Path)o;

		if (p == null)
			return false;

		return path.equals(p.getPath());
	}

	public ArrayList<Ply> getPath() {
		return path;
	}

	public Path add(Ply ply) {
		path.add(ply);
		return this;
	}

	public void print() {
		for (int i = 0; i < path.size(); ++i) {
			System.out.print(path.get(i));
			System.out.print(((i == path.size() - 1)? "\n" : " "));
		}
	}

	public void printReversely() {
		for (int i = path.size() - 1; i >= 0; --i) {
			Ply reversePly;
			switch (path.get(i)) {
				case UP:
					reversePly = Ply.DOWN;
					break;
				case DOWN:
					reversePly = Ply.UP;
					break;
				case LEFT:
					reversePly = Ply.RIGHT;
					break;
				case RIGHT:
					reversePly = Ply.LEFT;
					break;
				default:
					return;
			}
			System.out.print(reversePly);
			System.out.print(((i == 0)? "\n" : " "));
		}
	}

	public ArrayList<Ply> getReversePath() {
		ArrayList<Ply> reversePath = new ArrayList<Ply>();

		for (int i = path.size() - 1; i >= 0; --i) {
			Ply reversePly;
			switch (path.get(i)) {
				case UP:
					reversePly = Ply.DOWN;
					break;
				case DOWN:
					reversePly = Ply.UP;
					break;
				case LEFT:
					reversePly = Ply.RIGHT;
					break;
				case RIGHT:
					reversePly = Ply.LEFT;
					break;
				default:
					return null;
			}
			reversePath.add(reversePly);
		}

		return reversePath;
	}
}
