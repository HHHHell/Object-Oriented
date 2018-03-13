package week_07;

import java.awt.Point;
import java.util.Vector;

public class Request {
	private Point src;
	private Point dest;
	private long time;
	int number;
	boolean visit;
	boolean isfirst;

	public Request(Point aPoint, Point bPoint, long tt) {
		src = aPoint;
		dest = bPoint;
		time = (long) Math.ceil(tt / 100.0) * 100;
		visit = false;
		isfirst = true;
		number = -1;
	}

	public Request(Point aPoint, Point bPoint, long tt, int num) {
		src = aPoint;
		dest = bPoint;
		time = (long) Math.ceil(tt / 100.0) * 100;
		visit = false;
		isfirst = true;
		number = num;
	}

	public Point getsrc() {
		return src;
	}

	public Point getdest() {
		return dest;
	}

	public long gettime() {
		return time;
	}

	public boolean equals(Request re) {
		return this.src.equals(re.src) && this.dest.equals(re.dest) && this.time == re.time;
	}

	public String toString() {
		String from = "(" + (src.x + 1) + "," + (src.y + 1) + ")";
		String to = "(" + (dest.x + 1) + "," + (dest.y + 1) + ")";

		return time + ": form " + from + " to " + to;
	}
}

class Reqlist {
	private Vector<Request> requests;
	private int size;

	public Reqlist() {
		requests = new Vector<>(0, 1);
		size = 0;
	}

	synchronized public Request get(int i) {
		return requests.get(i);
	}

	synchronized public void add(Request re) {
		requests.add(re);
		size = requests.size();
	}

	synchronized public void remove(int i) {
		requests.remove(i);
		size = requests.size();
	}

	synchronized public void remove(Request re) {
		requests.remove(re);
		size = requests.size();
	}

	synchronized public int getsize() {
		return size;
	}
}
