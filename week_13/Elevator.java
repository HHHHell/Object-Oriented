package week_13;

import week_13.Main.Enumstate;

public class Elevator {
	/**
	 * @OVERVIEW: elevators
	 * 
	 * @RepInvariant: pos > 0 && state != null && mainreq != null ==> \result == true;
	 */
	public int pos;
	public Enumstate state;
	public Request mainreq;

	Elevator() {
		/**
		 * @REQUIRES: None;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: pos == 1 && state == STILL;
		 */
		pos = 1;
		state = Enumstate.STILL;
		mainreq = null;
	}

	boolean repOK() {
		/**
		 * @REQUIRES: None;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: pos > 0 && (state == STILL || state == UP || state == DOWN) ==> \result == true;
		 */
		if (pos < 0 || state == null)
			return false;
		return true;
	}

	Enumstate getstate() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == state;
		 */
		return state;
	}

	Request getmainreq() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == mainreq;
		 */
		return mainreq;
	}

	int getpos() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == pos;
		 */
		return pos;
	}

	void changemain(Request re) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: mianreq == re && (re.floor > pos ==> state == UP && re.floor < pos ==> state == DOWN
		 *           && re.floor == pos ==> state == STILL)
		 */
		mainreq = re;
		if (mainreq.getfloor() > pos)
			state = Enumstate.UP;
		else if (mainreq.getfloor() < pos)
			state = Enumstate.DOWN;
		else state = Enumstate.STILL;
	}

	void resetstate() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: state == STILL;
		 */
		state = Enumstate.STILL;
	}

	void resetmain() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: mainreq == null;
		 */
		mainreq = null;
	}

	public void moveup() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: pos == \old(pos) + 1 && state == UP;
		 */
		pos++;
		state = Enumstate.UP;
	}

	public void movedown() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: pos == \old(pos - 1) && state == DOWN;
		 */
		pos--;
		state = Enumstate.DOWN;
	}

	public String toString(Request re, double timer) {
		/**
		 * @REQUIRES: re != null && timer > 0;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: return a string of descriptions of this elevator state
		 */
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
