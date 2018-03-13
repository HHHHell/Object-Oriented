package week_10;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * @OVERVIEW： 主函数， 创建其他进程对象并启动
 * 不变式： pathname != null;
 */
public class Taxi_sys {
	public static final String pathname = "LogInfo"; // 程序输出结果所在的文件夹

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 创建共享对象，创建线程并启动
	 */
	public boolean repOK() {
		if (pathname == null)
			return false;
		return true;
	}

	
	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 创建共享对象，创建线程并启动
	 */
	public static void main(String[] args) {
		try {
			File logfile = new File(pathname);
			if (!logfile.exists()) {
				logfile.mkdirs();
			} else {
				File[] files = logfile.listFiles();
				for(int i = 0; i < files.length; i++) {
					if (files[i].exists())
						files[i].delete();
				}
			}

			int[][] temp = null;
			Reqlist mainreq = new Reqlist();
			Vector<Taxi> taxilist = new Vector<>(0, 1);
			Vector<TaxiThread> threads = new Vector<>(0, 1);
			int taxinum = 100;

			String reg = "\\[CR,\\(\\+?\\d{1,2},\\+?\\d{1,2}\\),\\(\\+?\\d{1,2},\\+?\\d{1,2}\\)\\]";
			InfoLogger iLogger = new InfoLogger(pathname);
			InputHandler inputHandler = new InputHandler(reg, 80, mainreq, iLogger);

			String filepath = "map.txt";
			File ff = new File(filepath);
			if (!ff.exists()) {
				System.out.println("Map file does not exist!");
				System.exit(0);
			}
			temp = inputHandler.getmap(filepath);
			if (temp == null || !inputHandler.validcheck(temp)) {
				System.out.println("Invalid map file!");
				System.exit(0);
			}

			ReadWriteLock maplock = new ReentrantReadWriteLock();
			MyFlag[] flags = new MyFlag[100];
			for(int i = 0; i < 100; i++) {
				flags[i] = new MyFlag();
			}
			TaxiGUI gui = new TaxiGUI();
			gui.LoadMap(temp, 80);
			CityMap map = new CityMap(temp, 80, maplock, flags, gui);
			boolean re = map.loadlight("light.txt");
			if (re == false) {
				System.out.println("Wrong Light file!");
				System.exit(0);
			}
			LightCtr lightCtr = new LightCtr(map, gui);
			GUIThread guiThread = new GUIThread(temp, taxilist, mainreq, 80, gui);

			for(int i = 0; i < taxinum; i++) {
				Taxi tt = new Taxi(i, 80, map, flags[i]);
				taxilist.add(tt);
				TaxiThread taxiThread = new TaxiThread(tt, iLogger);
				threads.add(taxiThread);
			}

			Scheduler scheduler = new Scheduler(taxilist, map, mainreq, iLogger);
			Test testThread = new Test(mainreq, taxilist);
			lightCtr.start();
			inputHandler.setmap(map);
			inputHandler.start();
			guiThread.start();
			scheduler.start();
			testThread.start();
			for(int i = 0; i < taxinum; i++) {
				threads.get(i).start();
			}
		} catch (Exception e) {
			System.out.println("Taxi_sys Exception: " + e);
			System.exit(0);
		}
	}
}
