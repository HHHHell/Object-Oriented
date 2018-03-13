package week_11;

/**
 * @OVERVIEW:出租车运动线程，控制出租车运动
 * 
 * @RepInvariant: taxi != null && logger.repOK() == true ==> \result = true;
 */
public class TaxiThread extends Thread {
	private Taxi taxi;
	private InfoLogger logger;

	public TaxiThread(Taxi tt, InfoLogger iLogger) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据传入的参数创建TaxiThread对象
		 */
		taxi = tt;
		logger = iLogger;
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: taxis
		 * 
		 * @EFFECTS: 根据出租车的指令情况，控制出租车运动。
		 */
		if (taxi == null)
			return false;
		return logger.repOK();
	}

	public void run() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: taxis
		 * 
		 * @EFFECTS: 根据出租车的指令情况，控制出租车运动。
		 */
		try {
			long begintime = System.currentTimeMillis();
			while (true) {
				if (taxi.getreq() != null) {
					/* pick customers */
					taxi.setstatus(3);
					long time = System.currentTimeMillis();
					Request nowreq = taxi.getreq();

					taxi.move(nowreq.getsrc(), time, logger, 0);
					taxi.setstatus(0);
					sleep(1000);

					/* send customer to destination */
					taxi.setstatus(1);
					time = System.currentTimeMillis();

					taxi.move(nowreq.getdest(), time, logger, 1);
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
