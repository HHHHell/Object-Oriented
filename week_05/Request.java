package week_05;

import week_05.Elevator_sys.Enumkind;
import week_05.Elevator_sys.Enumstate;

public class Request {

	private Enumstate dir;
	private Enumkind kind;
	private int floor;
	private double time;
	private int elenum;

	Request(Enumkind k, int f, Enumstate d, double t, int n) {
		kind = k;
		time = t;
		floor = f;
		dir = d;
		elenum = n;
	}

	Request() {
		kind = Enumkind.FR;
		time = 0;
		floor = 1;
		dir = Enumstate.NULL;
		elenum = 0;
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

	int getele() {
		return elenum;
	}

	public String toString() {
		String s;
		if (kind == Enumkind.FR)
			s = new String("[" + kind + "," + floor + "," + dir.toString() + "," + (int) (time / 1000) + "."
					+ (int) ((time % 1000) / 100) + "]");
		else s = new String("[" + kind + ",#" + elenum + "," + floor + "," + (int) (time / 1000) + "."
				+ (int) ((time % 1000) / 100) + "]");

		return s;
	}

	public boolean equales(Request request) {
		boolean result = false;
		if (this.kind == Enumkind.ER) {
			if (request.kind == Enumkind.ER && this.elenum == request.elenum && this.floor == request.floor)
				result = true;
		} else {
			if (request.kind == Enumkind.FR && this.floor == request.floor && this.dir == request.dir)
				result = true;
		}
		return result;
	}

	public boolean validitycheck() {
		boolean result = true;

		if (this.kind == Enumkind.FR && this.dir == Enumstate.UP && this.floor == 20)
			result = false;

		if (this.kind == Enumkind.FR && this.dir == Enumstate.DOWN && this.floor == 1)
			result = false;

		return result;
	}
}
