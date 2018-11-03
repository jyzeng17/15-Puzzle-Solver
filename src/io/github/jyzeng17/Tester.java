package io.github.jyzeng17;

public class Tester {
	public static void main(String[] args) {
		byte[] b = {1, 2, 3, 4, 0, 6, 7, 8, 5, 9, 10, 12, 13, 14, 11, 15};
		byte[] buffer = new byte[16];
		for (int i = 0; i < 15; ++i) {
			buffer[i] = (byte)(i + 1);
		}
		buffer[15] = 0;
		State start = new State(b);
		State end = new State(buffer);
		Node n = new Node(new Path(), start, end);
		start.print();
		end.print();
		System.out.println(n.getCost());
	}
}
