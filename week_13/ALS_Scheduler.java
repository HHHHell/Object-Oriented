package week_13;

import week_13.Main.Enumkind;
import week_13.Main.Enumstate;

public class ALS_Scheduler extends Scheduler {
	/**
	 * @OVERVIEW: class to distribute requires with ALS algorithm;
	 * 
	 * @RepInvariant: timer != null && reqs != null;
	 */
	public Reqlist sdreqs; // list of requests been picked

	public ALS_Scheduler() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: create a new object of ALS_Scheduler;
		 */
		super();
		sdreqs = new Reqlist();
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODDIFIES: None
		 * 
		 * @EFFECTS: \result == false ==> sdreqs == null || timer == null || reqs == null, otherwise, \result == true;
		 */
		if (sdreqs == null || timer == null || reqs == null)
			return false;
		return true;
	}

	public boolean pickcheck(Elevator ele, Request request) {
		/**
		 * @REQUIRES: ele != null && re != null;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: if re can be picked up by ele, \result == true, otherwise, \result == false;
		 */
		boolean result = false;
		if (request.getkind() == Enumkind.FR) {
			if (request.getdir() == ele.getstate()) {
				if (request.getdir() == Enumstate.UP && request.getfloor() <= ele.getmainreq().getfloor()
						&& request.getfloor() >= ele.getpos())
					result = true;
				else if (request.getdir() == Enumstate.DOWN && request.getfloor() >= ele.getmainreq().getfloor()
						&& request.getfloor() <= ele.getpos())
					result = true;
			}
		} else {
			if (ele.getstate() == Enumstate.UP && request.getfloor() >= ele.getpos())
				result = true;
			else if (ele.getstate() == Enumstate.DOWN && request.getfloor() <= ele.getpos())
				result = true;
		}
		return result;
	}

	void setmain(Elevator ele) { // set main request
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: ele
		 * 
		 * @EFFECTS: set ele.mainreq to proper request
		 */
		Request main;
		if (ele.getmainreq() == null) {
			if (sdreqs.getsize() != 0) {
				main = sdreqs.get(0);
				for(int i = 0; i < sdreqs.getsize(); i++) {
					if (sdreqs.get(i).gettime() < main.gettime())
						main = sdreqs.get(i);
				}
				sdreqs.remove(main);
			} else if (reqs.getsize() != 0) {
				main = reqs.get(0);
				reqs.remove(main);
			} else {
				ele.resetmain();
				return;
			}
			ele.changemain(main);
			if (main.gettime() > timer.gettime())
				timer.goes(main.gettime() - timer.gettime());
		}
	}

	public void schedule(Elevator ele) { // find the requests to be picked up and return request to be done
		/**
		 * @REQUIRES: ele != null
		 * 
		 * @MODIFIES: this
		 * 
		 * @EFFECTS: chose a request to handle accordding to ALS algorithm
		 */
		for(int i = 0; i < reqs.getsize(); i++) {
			Request re = reqs.get(i);
			if (re.gettime() <= timer.gettime() + ele.getmainreq().getcost(ele) && ele.getmainreq().same(re)) {
				errhandler(1, re.toString());
				reqs.remove(re);
				i--;
				continue;
			}
			if (re.gettime() < timer.gettime() && pickcheck(ele, re)) {
				sdreqs.addterm(re);
				reqs.remove(re);
				i--;
			}
		}
	}

	public void command(Elevator ele) {
		/**
		 * @REQUIRES: ele != null
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: schedule all requests in reqs according to ALS algorithm
		 */
		while (reqs.getsize() > 0 || ele.getmainreq() != null) {
			setmain(ele);
			Request main = ele.getmainreq();
/*			if (main == null) {
				break;
			}
*/
			schedule(ele);
			boolean flag = false;
			while (ele.getpos() != main.getfloor()) {
				flag = false;
				if (main.getfloor() > ele.getpos()) {
					ele.moveup();
				} else {
					ele.movedown();
				}
				timer.goes(0.5);
				schedule(ele);

				for(int i = 0; i < sdreqs.getsize(); i++) {
					if (ele.getpos() == sdreqs.get(i).getfloor()) {
						System.out.println(ele.toString(sdreqs.get(i), timer.gettime()));
						sdreqs.remove(i--);
						flag = true;
					}
				}
				if (flag) {
					timer.goes(1);
				}
			}
			if (flag) {
				System.out.println(ele.toString(ele.getmainreq(), timer.gettime() - 1));
			} else {
				System.out.println(ele.toString(ele.getmainreq(), timer.gettime()));
				timer.goes(1);
			}
			ele.resetmain();
			ele.resetstate();
		}
	}
}
