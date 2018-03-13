package week_13;


public class Scheduler {
	/**
	 * @OVERVIEW: class to distribute requires with FCFS algorithm;
	 * 
	 * @RepInvariant: timer != null && reqs != null;
	 */
	static String ERR0 = new String("INVALID ");
	static String ERR1 = new String("SAME ");

	public Timer timer;
	public Reqlist reqs;

	Scheduler() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: create a object of scheduler;
		 */
		timer = new Timer();
		reqs = new Reqlist();
	}

	void errhandler(int code, String str) {
		/**
		 * @REQUIRES: (code == 0 || code == 1) && str != null;
		 * 
		 * @MODIFIES: None;
		 * 
		 * @EFFECTS: handle errors according of code
		 */
		if (code == 0)
			System.out.println(ERR0 + "[" + str + "]");
		else if (code == 1)
			System.out.println(ERR1 + str);
	}

	Reqlist getreqs(){
		/**
		 * @REQUIRES: None
		 * 
		 * @NODIFIES: None
		 * 
		 * @EFFECTS: \result = \this.reqs;
		 */
		return reqs;
	}
}
