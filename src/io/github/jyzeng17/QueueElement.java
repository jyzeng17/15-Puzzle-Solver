package io.github.jyzeng17;

public class QueueElement implements Comparable<QueueElement> {
	private byte currentChild;
	private Node parent;

	public QueueElement(int c, Node p) {
		currentChild = (byte)c;
		parent = p;
	}

	// Comparable interface method
	public int compareTo(QueueElement qe) {
		// Lower cost has higher priority
		// But the PriorityQueue sorts in increasing order
		// So here higher cost has higher PriorityQueue priority
		return getCost() - qe.getCost();
	}

	public int getCost() {
		return parent.getCost();
	}

	// Inherited method from Collection interface, implement for queue's contains() method
	public boolean equals(Object o) {
		QueueElement qe = (QueueElement)o;

		if (qe == null)
			return false;

		//return ((currentChild == qe.getCurrentChild()) && (parent.equals(qe.getParent())));
		// Ignore currentChild value
		return (parent.equals(qe.getParent()));
	}

	// Implement for HashSet's contain() method
	public int hashCode() {
		return parent.hashCode();
	}

	public byte getCurrentChild() {
		return currentChild;
	}

	public Node getParent() {
		return parent;
	}

	public Node nextBestChild() {
		// pick the next best move child
		return parent.nextBestChild((int)currentChild);
	}
}
