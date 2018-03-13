package week_05;

import java.util.Vector;

public class Reqlist {
	private Vector<Request> list;
	private int size;

	public Reqlist() {
		list = new Vector<Request>(10, 1);
		size = list.size();
	}

	synchronized int getsize() {
		return size;
	}

	synchronized Request get(int i) {
		return list.get(i);
	}

	synchronized void addterm(Request re) {
		list.add(re);
		size = list.size();
	}

	synchronized void remove(Request re) {
		list.remove(re);
		size = list.size();
	}

	synchronized void remove(int i) {
		list.remove(i);
		size = list.size();
	}
}