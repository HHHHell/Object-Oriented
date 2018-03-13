package week_07;

import java.awt.Point;
import java.util.Vector;

public class TaxiThread extends Thread {
	private Taxi taxi;
	private InfoLogger logger;

	public TaxiThread(Taxi tt, InfoLogger iLogger) {
		taxi = tt;
		logger = iLogger;
	}

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

					logger.taximove(nowreq, path, 0);

					taxi.move(path, time);
					taxi.setstatus(0);
					sleep(1000);

					/* send customer to destination */
					taxi.setstatus(1);
					time = System.currentTimeMillis();
					path = taxi.map.shortestpath(taxi.getposition(), nowreq.getdest());

					logger.taximove(nowreq, path, 1);

					taxi.move(path, time);
					taxi.setstatus(0);
					taxi.addcredit(3);
					sleep(1000);

					/* remove this request from serve request list */
					taxi.setstatus(2);
					taxi.setreq(null);
					begintime = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - begintime >= 20000) {
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
