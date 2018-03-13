package week_03;

import week_03.Main.Enumstate;

interface eleinter {
	public void move(Request re);

	public void moveup();

	public void movedown();

	public String toString();
}

public class Elevator implements eleinter {
	private int pos;
	private Enumstate state;
	private Request mainreq;

	Elevator() {
		pos = 1;
		state = Enumstate.STILL;
		mainreq = null;
	}

	Enumstate getstate() {
		return state;
	}

	Request getmainreq() {
		return mainreq;
	}

	int getpos() {
		return pos;
	}

	void changemain(Request re) {
		mainreq = re;
		if (mainreq.getfloor() > pos)
			state = Enumstate.UP;
		else if (mainreq.getfloor() < pos)
			state = Enumstate.DOWN;
		else state = Enumstate.STILL;
	}

	void resetstate() {
		state = Enumstate.STILL;
	}

	void resetmain() {
		mainreq = null;
	}

	public void moveup() {
		pos++;
		state = Enumstate.UP;
	}

	public void movedown() {
		pos--;
		state = Enumstate.DOWN;
	}

	public void move(Request re) {
		if (re.getfloor() > pos) {
			state = Enumstate.UP;
		} else if (re.getfloor() < pos) {
			state = Enumstate.DOWN;
		} else state = Enumstate.STILL;

		pos = re.getfloor();
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
