package week_05;

import week_05.Elevator_sys.Enumstate;;

interface eleinter {
	public void moveup();

	public void movedown();

	public String toString();
}

public class Elevator implements eleinter {
	private int pos;
	private Enumstate state;

	Elevator() {
		pos = 1;
		state = Enumstate.STILL;
	}

	Enumstate getstate() {
		return state;
	}

	int getpos() {
		return pos;
	}

	void resetstate() {
		state = Enumstate.STILL;
	}

	public void moveup() {
		pos++;
		state = Enumstate.UP;
	}

	public void movedown() {
		pos--;
		state = Enumstate.DOWN;
	}

	public String toString(Request re, double timer) {
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		String str;
		if (state == Enumstate.STILL) {
			if (timer % 1 == 0)
				str = new String(re.toString() + "/" + "(" + pos + "," + state + "," + nf.format(timer + 1) + ".0)");
			else str = new String(re.toString() + "/" + "(" + pos + "," + state + "," + nf.format(timer + 1) + ")");
		} else {
			if (timer % 1 == 0)
				str = new String(re.toString() + "/" + "(" + pos + "," + state + "," + nf.format(timer) + ".0)");
			else str = new String(re.toString() + "/" + "(" + pos + "," + state + "," + nf.format(timer) + ")");
		}

		return str;
	}
}
