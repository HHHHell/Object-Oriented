package week_07;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class Test extends Thread {
	public static void sendRequst(Vector<String> strings, Reqlist mainlist) {
		String regex = "\\[CR,\\(\\+?\\d{1,2},\\+?\\d{1,2}\\),\\(\\+?\\d{1,2},\\+?\\d{1,2}\\)\\]";
		int size = 80;
		InfoLogger logger = new InfoLogger("LogInfo");
		for(int i = 0; i < strings.size(); i++) {
			String strline = strings.get(i);
			Request request = InputHandler.getreq(strline, regex, size);
			boolean flag = false;
			if (request == null) {
				System.out.println("Wrong Request!");
				continue;
			}
			for(int j = 0; j < mainlist.getsize(); j++) {
				if (request.equals(mainlist.get(j)))
					flag = true;
			}
			if (flag) {
				System.out.println("Same Requset!");
				continue;
			}
			synchronized (mainlist) {
				request.number = mainlist.get(mainlist.getsize() - 1).number + 1;
				mainlist.add(request);
			}
			logger.readrequest(request);
		}
	}

	public static void traceTaxi(String pathname, Taxi taxi, long lasttime, long stoptime) {
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

	public static Vector<Taxi> findTaxi(Vector<Taxi> taxis, int sta) {
		Vector<Taxi> myTaxis = new Vector<>();
		for(int i = 0; i < taxis.size(); i++) {
			Taxi tt = taxis.get(i);
			if (tt.getstatus() == sta) {
				myTaxis.add(tt);
			}
		}
		return myTaxis;
	}

	public static void printTaxi(Taxi tt, String pathname) {
		try {
			File ff = new File(pathname);
			if (!ff.exists()) {
				ff.createNewFile();
			}
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff,true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String myString = tt.toString() + System.lineSeparator();
			bWriter.append(myString);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Tracetaxi Exception: " + e);
		}
	}
}
