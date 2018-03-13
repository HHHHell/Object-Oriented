package week_09;

import java.awt.Point;
import java.util.Vector;

public class TaxiThread extends Thread {
	private Taxi taxi;
	private InfoLogger logger;

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 根据传入的参数创建TaxiThread对象
	 */
	public TaxiThread(Taxi tt, InfoLogger iLogger) {
		taxi = tt;
		logger = iLogger;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: taxis
	 * 
	 * @ EFFECTS: 根据出租车的指令情况，控制出租车运动。
	 */
	public void run() {
		try {
			long begintime = System.currentTimeMillis();
			while (true) {
				while (taxi.getreq() != null) {
					/* pick customers */
					taxi.setstatus(3);
					long time = System.currentTimeMillis();
					Request nowreq = taxi.getreq();
					Vector<Point> path = taxi.map.shortestpath(taxi.getposition(), nowreq.getsrc());

					taxi.move(path, time, logger, 0);
					taxi.setstatus(0);
					sleep(1000);

					/* send customer to destination */
					taxi.setstatus(1);
					time = System.currentTimeMillis();
					path = taxi.map.shortestpath(taxi.getposition(), nowreq.getdest());

					taxi.move(path, time, logger, 1);
					taxi.setstatus(0);
					taxi.addcredit(3);
					sleep(1000);

					/* remove this request from serve request list */
					taxi.setstatus(2);
					taxi.setreq(null);
					begintime = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - begintime >= 20 * 1000) {
					taxi.setstatus(0);
					sleep(1000);
					begintime = System.currentTimeMillis();
					taxi.setstatus(2);
				}
				taxi.randommove();
				// System.out.println(taxi.toString());
			}
		} catch (InterruptedException e) {
			System.out.println("TaxiThread Exception: " + e);
			System.exit(0);
		}
	}
}
