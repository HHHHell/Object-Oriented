package week_14;

import week_14.Main.Enumstate;

public class Elevator {
	/**
	 * @OVERVIEW: elevators
	 * 
	 * @RepInvariant: pos > 0 && pos <= 10 && state != null && mainreq != null ==> \result == true;
	 */
	private int pos;
	private Enumstate state;
	private Request mainreq;

	Elevator() {
		/**
		 * @REQUIRES: None;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: pos == 1 && state == STILL;
		 */

		// <pos == 1 && state == STILL> with <none>

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
		 * @EFFECTS: pos > 0 && pos <= 10 && (state == STILL || state == UP || state == DOWN) ==> \result == true;
		 */
		if (pos < 0 || pos > 10 || state == null)
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
		
		// <\result == state> whith <none>
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
		// <\result == mainreq> whith <none>
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
		// <\result == pos> with <none>
		return pos;
	}

	void changemain(Request re) {
		/**
		 * @REQUIRES: re != null;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: mianreq == re && (re.floor > pos ==> state == UP && re.floor < pos ==> state == DOWN
		 *           && re.floor == pos ==> state == STILL)
		 */
		
		/*
		 * <mianreq == re && state == UP> with <re.floor > pos>
		 * <mianreq == re && state == STILL> with <re.floor == pos>
		 * <mianreq == re && state == DOWN> with <re.floor < pos>
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
		//<state == STILL> with <none>
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
		//<mainreq == null> with <none>
		mainreq = null;
	}

	public void moveup() {
		/**
		 * @REQUIRES: \this.pos != 10;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: pos == \old(pos) + 1 && state == UP;
		 */
		//<pos == \old(pos) + 1 && state == UP> with <none>
		pos++;
		state = Enumstate.UP;
	}

	public void movedown() {
		/**
		 * @REQUIRES: \this.pos != 1;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: pos == \old(pos - 1) && state == DOWN;
		 */
		//<pos == \old(pos - 1) && state == DOWN> with <none>
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
