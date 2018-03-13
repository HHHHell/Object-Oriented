package week_05;

import week_05.Elevator_sys.Enumstate;

public class EleThread extends Thread {
	private Newele eNewele;
	private Reqlist sdlist;
	private long btime;

	public EleThread(Newele ele, Reqlist slist, Reqlist[] tolist, long time) {
		eNewele = ele;
		sdlist = slist;
		btime = time;
	}

	public void run() {
		try {
			while (true) {
				long stime = System.currentTimeMillis();
				boolean flag = false, anflag = false;
				Enumstate stt = Enumstate.STILL;
				while (sdlist.getsize() > 0) {
					if (sdlist.get(0).getfloor() != eNewele.getpos()) {
						while (System.currentTimeMillis() - stime < 3000) {
						}
						stime += 3000;
						if (sdlist.get(0).getfloor() > eNewele.getpos()) {
							eNewele.moveup();
						} else {
							eNewele.movedown();
						}
					}

					if (eNewele.getpos() == sdlist.get(0).getfloor()) {
						flag = true;
						System.out.println(System.currentTimeMillis() + ":" + sdlist.get(0).toString() + "/"
								+ eNewele.toString((stime - btime), eNewele.getstate()));
						stt = eNewele.getstate();
						eNewele.resetstate();

					}
					for(int i = 1; i < sdlist.getsize(); i++) {
						if (sdlist.get(i).getfloor() == eNewele.getpos()) {
							anflag = true;
							if (!flag) {
								System.out.println(System.currentTimeMillis() + ":" + sdlist.get(i).toString() + "/"
										+ eNewele.toString((stime - btime), eNewele.getstate()));
							} else {
								System.out.println(System.currentTimeMillis() + ":" + sdlist.get(i).toString() + "/"
										+ eNewele.toString((stime - btime), stt));
							}
							sdlist.remove(i--);
						}
					}
					if (flag || anflag) {
						anflag = false;
						while (System.currentTimeMillis() - stime < 6000) {
						}
						stime += 6000;
						if (flag) {
							sdlist.remove(0);
							flag = false;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("EleThread: " + eNewele.toString() + e);
		}
	}
}
