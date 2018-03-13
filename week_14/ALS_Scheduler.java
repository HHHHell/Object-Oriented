package week_14;

import week_14.Main.Enumkind;
import week_14.Main.Enumstate;

public class ALS_Scheduler extends Scheduler {
	/**
	 * @OVERVIEW: class to distribute requires with ALS algorithm;
	 * 
	 * @RepInvariant: timer != null && reqs != null;
	 */
	private Reqlist sdreqs; // list of requests been picked

	public ALS_Scheduler() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: create a new object of ALS_Scheduler;
		 */
		// <sdreqs != null && reqs != null && timer != null> with <none>
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
		 * @REQUIRES: request.time < timer.gettime
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == true ==> (request.getkind() == FR && request.getdir() == ele.getstate() &&
		 *           (request.getdir() == Enumstate.UP && request.getfloor() <= ele.getmainreq().getfloor()
		 *           && request.getfloor() >= ele.getpos()) ||
		 *           (request.getdir() == Enumstate.DOWN && request.getfloor() >= ele.getmainreq().getfloor()
		 *           && request.getfloor() <= ele.getpos()));
		 *           otherwise, \result == false;
		 */
		
		/*
		 * <\result == true> with <request.getkind() == FR && request.getdir() == ele.getstate() &&
		 *           (request.getdir() == Enumstate.UP && request.getfloor() <= ele.getmainreq().getfloor()
		 *           && request.getfloor() >= ele.getpos()) ||
		 *           (request.getdir() == Enumstate.DOWN && request.getfloor() >= ele.getmainreq().getfloor()
		 *           && request.getfloor() <= ele.getpos())>
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
		 * @REQUIRES: ele != null
		 * 
		 * @MODIFIES: ele, \this
		 * 
		 * @EFFECTS: set ele.mainreq to proper request
		 */
		
		/*
		 * <none> with <ele.getmainreq() != null>
		 * <ele.getmianreq() == \old(sdreqs[min]) && sdreqs.contains(ele.getmainreq()) == false> with <sdreqs.getsize() != 0>
		 * <ele.getmianreq() == \old(reqs[0]) && reqs.contains(ele.getmainreq()) == false> with <sdreqs.getsize() == 0 && reqs.getsize != 0>
		 * <ele.getmianreq() == null> with <sdreqs.getsize() == 0 && reqs.getsize == 0>
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
		/*
		 * int i, 0 <= i < reqs.size, re = \old(reqs).get(i)
		 * <reqs.contains(re) == false> with <ele.getmainreq().same(re)>
		 * <reqs.contains(re) == false && sdreqs.contains(re) == true> with <re.gettime < timer && pickcheck(ele, re) == true> 
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
			if (main == null) {
				break;
			}

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
						Request tmp = sdreqs.get(i);
						sdreqs.remove(tmp);
						i--;
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
