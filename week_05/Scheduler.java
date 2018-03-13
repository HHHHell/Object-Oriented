package week_05;

import week_05.Elevator_sys.Enumkind;
import week_05.Elevator_sys.Enumstate;

public class Scheduler extends Thread {
	private Newele[] elelist;
	private Reqlist[] esdlist;
	private Reqlist mainlist;

	public Scheduler(Newele[] eles, Reqlist[] tolist) {
		elelist = eles;
		mainlist = tolist[0];
		esdlist = new Reqlist[3];
		for(int i = 0; i < 3; i++) {
			esdlist[i] = tolist[i + 1];
		}
	}

	private boolean ispick(Newele ele, Reqlist sdlist, Request request) {
		if (ele.getstate() == Enumstate.STILL) {
			return false;
		}
		if (sdlist.getsize() == 0) {
			return false;
		}
		boolean result = false;
		if (request.getkind() == Enumkind.ER) {
			if (ele.getstate() == Enumstate.UP && request.getfloor() > ele.getpos())
				result = true;
			if (ele.getstate() == Enumstate.DOWN && request.getfloor() < ele.getpos())
				result = true;
		} else {
			if (ele.getstate() == Enumstate.UP && request.getdir() == Enumstate.UP && ele.getpos() < request.getfloor()
					&& sdlist.get(0).getfloor() >= request.getfloor())
				result = true;
			if (ele.getstate() == Enumstate.DOWN && request.getdir() == Enumstate.DOWN
					&& ele.getpos() > request.getfloor() && sdlist.get(0).getfloor() <= request.getfloor())
				result = true;
		}
		return result;
	}

	private boolean issame(Request re, Reqlist list, int begin) {
		for(int i = begin; i < list.getsize(); i++) {
			if (re.equales(list.get(i))) {
				return true;
			}
		}
		return false;
	}

	private void schedule() {
		boolean flag1 = false, flag2 = false, fflag = false;
		int min = 0;
		if (mainlist.getsize() > 0) {

			for(int i = 0; i < mainlist.getsize(); i++) {
				/* case 1 : same */
				if (mainlist.get(i).getkind() == Enumkind.FR) {
					for(int j = 0; j < 3; j++) {
						if (issame(mainlist.get(i), esdlist[j], 0)) {
							fflag = true;
							break;
						}
					}
					if (fflag) {
						System.out.println(System.currentTimeMillis() + ":SAME " + mainlist.get(i).toString());
						mainlist.remove(i--);
						continue;
					}

					/* case 2 : be picked */
					int[] ablelist = { 0, 0, 0 };
					for(int j = 0; j < 3; j++) {
						if (ispick(elelist[j], esdlist[j], mainlist.get(i))) {
							ablelist[j] = 1;
							flag1 = true;
						}
					}
					if (flag1) {
						flag1 = false;
						min = -1;
						for(int j = 0; j < 3; j++) {
							if (ablelist[j] == 1) {
								if (min == -1)
									min = j;
								if (elelist[j].getmcount() < elelist[min].getmcount())
									min = j;
							}
						}
						boolean nflag = esdlist[min].getsize() == 0;
						esdlist[min].addterm(mainlist.get(i));
						mainlist.remove(i--);
						if (nflag) {
							if (esdlist[min].get(0).getfloor() > elelist[min].getpos())
								elelist[min].setstate(Enumstate.UP);
							else if (esdlist[min].get(0).getfloor() < elelist[min].getpos()) {
								elelist[min].setstate(Enumstate.DOWN);
							}
						}
						continue;
					}

					/* case 3 : at least one elevator is idle */
					for(int j = 0; j < 3; j++) {
						if (esdlist[j].getsize() == 0) {
							ablelist[j] = 1;
							flag2 = true;
						}
					}
					if (flag2) {
						flag2 = false;
						min = -1;
						for(int j = 0; j < 3; j++) {
							if (ablelist[j] == 1) {
								if (min == -1)
									min = j;
								if (elelist[j].getmcount() < elelist[min].getmcount())
									min = j;
							}
						}

						boolean nflag = esdlist[min].getsize() == 0;
						esdlist[min].addterm(mainlist.get(i));
						mainlist.remove(i--);
						if (nflag) {
							if (esdlist[min].get(0).getfloor() > elelist[min].getpos())
								elelist[min].setstate(Enumstate.UP);
							else if (esdlist[min].get(0).getfloor() < elelist[min].getpos()) {
								elelist[min].setstate(Enumstate.DOWN);
							}
						}
						continue;
					}
					continue;
				}

				if (mainlist.get(i).getkind() == Enumkind.ER) {
					int num = mainlist.get(i).getele() - 1;
					if (issame(mainlist.get(i), esdlist[num], 0)) {
						System.out.println(System.currentTimeMillis() + ":SAME " + mainlist.get(i).toString());
						mainlist.remove(i--);
						continue;
					}
					boolean nflag = esdlist[num].getsize() == 0;
					esdlist[num].addterm(mainlist.get(i));
					mainlist.remove(i--);
					if (nflag) {
						if (esdlist[num].get(0).getfloor() > elelist[num].getpos()) {
							elelist[num].setstate(Enumstate.UP);
						} else if (esdlist[num].get(0).getfloor() < elelist[num].getpos()) {
							elelist[num].setstate(Enumstate.DOWN);
						}
					}
				}
			}

		}
	}

	public void run() {
		try {
			while (true) {
				schedule();
			}
		} catch (Exception e) {
			System.out.println("Scheduler:" + e);
		}
	}
}
