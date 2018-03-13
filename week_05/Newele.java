package week_05;

import week_05.Elevator_sys.Enumstate;

public class Newele extends Elevator {
	private int pos;
	private int num;
	private Enumstate state;
	public long mcount;

	Newele(int number) {
		pos = 1;
		num = number;
		state = Enumstate.STILL;
		mcount = 0;
	}

	int getnum() {
		return num;
	}

	int getpos() {
		return pos;
	}

	Enumstate getstate() {
		return state;
	}

	long getmcount() {
		return mcount;
	}

	void setstate(Enumstate ss) {
		state = ss;
	}

	public void moveup() {
		pos++;
		mcount++;
		state = Enumstate.UP;
	}

	public void movedown() {
		pos--;
		mcount++;
		state = Enumstate.DOWN;
	}

	public String toString(long timer, Enumstate stt) {
		String str;
		if (state == Enumstate.STILL && stt == Enumstate.STILL) {
			str = new String("(#" + num + "," + pos + ",STILL," + mcount + "," + (int) (timer / 1000 + 6) + "."
					+ (int) ((timer % 1000) / 100) + ")");
		} else {
			str = new String("(#" + num + "," + pos + "," + stt + "," + mcount + "," + (int) (timer / 1000) + "."
					+ (int) ((timer % 1000) / 100) + ")");
		}

		return str;
	}

	public void rmsame(Request re, Reqlist list, int j) {
		for(int i = j; i < list.getsize(); i++) {
			if (re.equales(list.get(i))) {
				System.out.println(System.currentTimeMillis() + ":SAME " + list.get(i).toString());
				list.remove(i);
			}
		}
	}
}
