package week_11;

import java.awt.Point;
import java.util.Vector;

/**
 * @OVERVIEW: 根据请求队列和出租车状态，调度分配请求
 * 
 * @RepInvariant: taxis != null && map.repOK() == true && cuReqlist != null &&
 *                logger.repOK== true ==> \result = true;
 */
public class Scheduler extends Thread {
	private Vector<Taxi> taxis;
	private CityMap map;
	private Reqlist cuReqlist;
	private InfoLogger logger;

	public Scheduler(Vector<Taxi> taxilist, CityMap mm, Reqlist reqlist, InfoLogger iLogger) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 创建一个Scheduler对象
		 */
		taxis = taxilist;
		map = mm;
		cuReqlist = reqlist;
		logger = iLogger;
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: taxis != null && map.repOK() == true && cuReqlist != null &&
		 *           logger.repOK== true ==> \result = true;
		 */
		if (taxis == null || cuReqlist == null)
			return false;
		return map.repOK() && logger.repOK();
	}

	public int sendreq(Request request, Vector<Integer> greblist) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: greblist
		 * 
		 * @EFFECTS: 将request按一定规则分配给出租车， 返回其编号
		 */
		while (System.currentTimeMillis() - request.gettime() <= 3000) {
			for(int i = 0; i < taxis.size(); i++) {
				if (System.currentTimeMillis() - request.gettime() > 3000)
					break;
				Taxi taxi = taxis.get(i);
				if (map.isinrange(request.getsrc(), taxi.getposition(), 2) && taxi.getstatus() == 2) {
					taxi.grebdeal(greblist);
				}
			}
		}

		Vector<Taxi> myTaxis = new Vector<>();
		for(int i = 0; i < greblist.size(); i++) {
			Taxi tt = taxis.get(greblist.get(i));
			myTaxis.add(tt);
			if (tt.getstatus() != 2) {
				greblist.remove(i--);
				continue;
			}
		}
		logger.printtaix(request, myTaxis, 1);

		if (greblist.size() == 0) {
			return -1;
		}

		if (greblist.size() == 1)
			return greblist.get(0);

		Vector<Integer> crelist = new Vector<>(0, 1);
		int maxcre = 0;
		for(int i = 0; i < greblist.size(); i++) {
			if (taxis.get(greblist.get(i)).getcredit() > maxcre) {
				for(int j = 0; j < crelist.size(); j++)
					crelist.remove(j--);
				crelist.add(greblist.get(i));
				maxcre = taxis.get(greblist.get(i)).getcredit();
			}
			if (taxis.get(greblist.get(i)).getcredit() == maxcre)
				crelist.add(greblist.get(i));
		}
		if (crelist.size() == 0)
			return -1;
		if (crelist.size() == 1)
			return crelist.get(0);

		Vector<Integer> dislist = new Vector<>(0, 1);
		Vector<Point> points = new Vector<>();
		int mindis = 65536;
		for(int i = 0; i < crelist.size(); i++) {
			Point pp = taxis.get(crelist.get(i)).getposition();
			points.add(pp);
		}
		Vector<Integer> distence = map.shorstdistence(request.getsrc(), points);
		for(int i = 0; i < distence.size(); i++) {
			int num = crelist.get(i);
			if (distence.get(i) < mindis) {
				for(int j = 0; j < dislist.size(); j++) {
					dislist.remove(j--);
				}
				dislist.add((Integer) num);
				mindis = distence.get(i);
			}
			if (distence.get(i) == mindis)
				dislist.add((Integer) num);
		}
		if (dislist.size() == 0)
			return -1;
		return dislist.get(0);
	}

	public void run() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: cuRequest, taxis
		 * 
		 * @EFFECTS: 将cuRequest中的请求分配给出租车
		 */
		try {
			while (true) {
				for(int i = 0; i < cuReqlist.getsize(); i++) {
					Request request = cuReqlist.get(i);
					Vector<Taxi> tt = new Vector<>();
					Vector<Integer> greblist = new Vector<>(0, 1);

					if (request.isfirst = true) {
						for(int j = 0; j < taxis.size(); j++) {
							Taxi myTaxi = taxis.get(j);
							if (map.isinrange(request.getsrc(), myTaxi.getposition(), 2)) {
								tt.add(myTaxi);
								if (myTaxi.getstatus() == 2)
									myTaxi.grebdeal(greblist);
							}
						}
						logger.printtaix(request, tt, 0);
						request.isfirst = false;
					}

					int number = sendreq(request, greblist);
					if (number == -1) {
						logger.chrosetaxi(request, null);
						cuReqlist.remove(i--);
						continue;
					}
					taxis.get(number).setreq(request);
					cuReqlist.remove(i--);
					logger.chrosetaxi(request, taxis.get(number));
				}
			}
		} catch (Exception e) {
			System.out.println("Scheduler Exception: " + e);
			System.exit(0);
		}
	}
}
