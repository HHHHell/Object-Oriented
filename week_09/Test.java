package week_09;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Vector;

public class Test extends Thread {
	Reqlist mainlist;
	Vector<Taxi> taxis;

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 根据输入的参数，创建Test对象
	 */
	public Test(Reqlist reqlist, Vector<Taxi> ts) {
		mainlist = reqlist;
		taxis = ts;
	}

	/*
	 * @ REQUIRES: strings != null;
	 * 
	 * @ MODIFIES: mainlist
	 * 
	 * @ EFFECTS: 根据输入的内容，获得乘客请求，并将其置入mainlist
	 */
	public void sendRequst(Vector<String> strings) {
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
				logger.readrequest(request);
				Thread.sleep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @ REQUIRES: pathname != null, file exist;
	 * 
	 * @ MODIFIES: LogInfo
	 * 
	 * @ EFFECTS: 根据传递的参数，跟踪某辆出租车的状态并输出到文件中
	 */
	public void traceTaxi(String pathname, Taxi taxi, long lasttime, long stoptime) {
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

	/*
	 * @ REQUIRES: sta == 0||sta == 1|| sta == 2||sta == 3;
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 根据传递的参数，返回处于sta状态下的出租车列表
	 */
	public Vector<Taxi> findTaxi(int sta) {
		Vector<Taxi> myTaxis = new Vector<>();
		for(int i = 0; i < taxis.size(); i++) {
			Taxi tt = taxis.get(i);
			if (tt.getstatus() == sta) {
				myTaxis.add(tt);
			}
		}
		return myTaxis;
	}
	
	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: LogInfo, mainlist
	 * 
	 * @ EFFECTS: 测试线程
	 */
	public void run() {
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
