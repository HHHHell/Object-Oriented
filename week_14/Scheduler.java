package week_14;

public class Scheduler {
	/**
	 * @OVERVIEW: class to distribute requires;
	 * 
	 * @RepInvariant: \result == true ==> timer != null && reqs != null;
	 */
	static String ERR0 = new String("INVALID ");
	static String ERR1 = new String("SAME ");

	protected Timer timer;
	protected Reqlist reqs;

	Scheduler() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: create a object of scheduler;
		 */
		
		// <timer != null && reqs != null> with <none>
		timer = new Timer();
		reqs = new Reqlist();
	}

	boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == true ==> timer != null && reqs != null;
		 */
		if (timer == null || reqs == null)
			return false;
		return true;
	}

	void errhandler(int code, String str) {
		/**
		 * @REQUIRES: (code == 0 || code == 1) && str != null;
		 * 
		 * @MODIFIES: None;
		 * 
		 * @EFFECTS: handle errors according of code
		 */
		// <print(REE0 + str)> with <code == 0, str>;
		// <print(ERR1 + str)> with <code == 1, str>;
		if (code == 0)
			System.out.println(ERR0 + "[" + str + "]");
		else if (code == 1)
			System.out.println(ERR1 + str);
	}

	Reqlist getreqs() {
		/**
		 * @REQUIRES: None
		 * 
		 * @NODIFIES: None
		 * 
		 * @EFFECTS: \result = \this.reqs;
		 */
		//<\result == reqs> with <none>
		return reqs;
	}
}
