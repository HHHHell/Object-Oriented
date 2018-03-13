package week_13;

import week_13.Main.Enumkind;
import week_13.Main.Enumstate;

public class Request {
	private Enumstate dir;
	private Enumkind kind;
	private int floor;
	private double time;

	Request(Enumkind k, int f, Enumstate d, double t) {
		kind = k;
		time = t;
		floor = f;
		dir = d;
	}

	Request() {
		kind = Enumkind.FR;
		time = 0;
		floor = 1;
		dir = Enumstate.NULL;
	}

	Enumstate getdir() {
		return dir;
	}

	Enumkind getkind() {
		return kind;
	}

	int getfloor() {
		return floor;
	}

	double gettime() {
		return time;
	}

	double getcost(Elevator ele) {
		double cost;
		cost = Math.abs(floor - ele.getpos()) * 0.5 + 1;
		return cost;
	}

	double getcost(Elevator ele, int f) {
		double cost;
		cost = Math.abs(f - ele.getpos()) * 0.5 + 1;

		return cost;
	}

	public String toString() {
		String s;
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		if (kind == Enumkind.FR)
			s = new String("[" + kind + "," + floor + "," + dir.toString() + "," + nf.format(time) + "]");
		else s = new String("[" + kind + "," + floor + "," + nf.format(time) + "]");

		return s;
	}

	public boolean equales(Request request) {
		boolean result = false;
		if (this.kind == request.kind && this.floor == request.floor && this.time == request.time
				&& this.dir == request.dir)
			result = true;
		return result;
	}

	public boolean same(Request temp) {
		boolean result = false;
		if (this.getkind() == Enumkind.ER) {
			if (temp.getkind() == Enumkind.ER && temp.getfloor() == this.getfloor()) {
				result = true;
			}
		} else {
			if (temp.getkind() == Enumkind.FR && temp.getfloor() == this.getfloor() && temp.getdir() == this.getdir()) {
				result = true;
			}
		}
		return result;
	}

	public boolean validitycheck(Boolean isfirst, Request re) {
		boolean result = true;

		if (isfirst) {
			if (!this.toString().equals("[FR,1,UP,0]"))
				result = false;
		}
		if (this.time < re.time)
			result = false;

		if (this.kind == Enumkind.FR && this.dir == Enumstate.UP && this.floor == 10)
			result = false;

		if (this.kind == Enumkind.FR && this.dir == Enumstate.DOWN && this.floor == 1)
			result = false;

		// System.out.println(result);
		return result;
	}
}
