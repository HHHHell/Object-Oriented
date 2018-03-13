package week_11;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Vector;

/**
 * @OVERVIEW: methods for test
 *
 * @RepInvariant: mainlist != null && taxis != null ==> \result = true
 * 
 */
public class Test extends Thread {
	Reqlist mainlist;
	Vector<Taxi> taxis;

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: mainlist != null && taxis != null ==> \result = true
		 */
		if (mainlist == null || taxis == null)
			return false;
		return true;
	}

	public Test(Reqlist reqlist, Vector<Taxi> ts) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据输入的参数，创建Test对象
		 */
		mainlist = reqlist;
		taxis = ts;
	}

	public void sendRequst(Vector<String> strings) {
		/**
		 * @REQUIRES: strings != null;
		 * 
		 * @MODIFIES: mainlist
		 * 
		 * @EFFECTS: 根据输入的内容，获得乘客请求，并将其置入mainlist
		 */
		try {
			String regex = "\\[CR,\\(\\+?\\d{1,2},\\+?\\d{1,2}\\),\\(\\+?\\d{1,2},\\+?\\d{1,2}\\)\\]";
			int size = 80;
			InfoLogger logger = new InfoLogger("LogInfo");
			for(int i = 0; i < strings.size(); i++) {
				// Thread.sleep(10);
				String strline = strings.get(i);
				Request request = InputHandler.getreq(strline, regex, size);
				if (request == null) {
					System.out.println("Wrong Request!");
					continue;
				}

				Request aRequest = null;
				synchronized (mainlist) {
					int tt = 0;
					if ((tt = mainlist.getsize()) != 0) {
						aRequest = mainlist.get(tt - 1);
					}
					if (aRequest != null && request.equals(aRequest)) {
						System.out.println("Same Requset!");
						continue;
					}

				}
				request.number = Request.mkid();
				mainlist.add(request);
				System.out.println(request.toString());
				logger.readrequest(request);
				Thread.sleep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void traceTaxi(String pathname, Taxi taxi, long lasttime, long stoptime) {
		/**
		 * @REQUIRES: pathname != null, file exist;
		 * 
		 * @MODIFIES: LogInfo
		 * 
		 * @EFFECTS: 根据传递的参数，跟踪某辆出租车的状态并输出到文件中
		 */
		try {
			long begin = System.currentTimeMillis();
			File ff = new File(pathname);
			if (!ff.exists()) {
				ff.createNewFile();
			}
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff, true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String lString = taxi.toString() + System.lineSeparator();
			String nString = taxi.toString() + System.lineSeparator();
			bWriter.append(nString);
			while (System.currentTimeMillis() - begin >= lasttime) {
				nString = taxi.toString() + System.lineSeparator();
				if (!nString.equals(lString)) {
					bWriter.append(nString);
					lString = nString;
				}
				sleep(stoptime);
			}
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Tracetaxi Exception: " + e);
		}
	}

	public Vector<Taxi> findTaxi(int sta) {
		/**
		 * @REQUIRES: sta == 0||sta == 1|| sta == 2||sta == 3;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 根据传递的参数，返回处于sta状态下的出租车列表
		 */
		Vector<Taxi> myTaxis = new Vector<>();
		for(int i = 0; i < taxis.size(); i++) {
			Taxi tt = taxis.get(i);
			if (tt.getstatus() == sta) {
				myTaxis.add(tt);
			}
		}
		return myTaxis;
	}

	public void run() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: LogInfo, mainlist
		 * 
		 * @EFFECTS: 测试线程
		 */
		try {
			Vector<String> strings = new Vector<>();
			Random random = new Random();
			for(int i = 0; i < 5; i++) {
				String req = "[CR," + "(" + (random.nextInt(80) + 1) + "," + (random.nextInt(80) + 1) + ")"
						+ ",(80,80)]";
				strings.add(req);
			}
			sendRequst(strings);
		} catch (Exception e) {
			System.out.println("Test Exception: " + e);
		}
	}
}
