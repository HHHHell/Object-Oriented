package week_11;

import java.io.File;
import java.util.Vector;

/**
 * @OVERVIEW： 主函数， 创建其他进程对象并启动
 * 
 * @RepInvariant: pathname != null;
 */
public class Taxi_sys {
	public static final String pathname = "LogInfo"; // 程序输出结果所在的文件夹

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: pathname != null;
		 */
		if (pathname == null)
			return false;
		return true;
	}

	public static void main(String[] args) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 创建共享对象，创建线程并启动
		 */
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

			String mapfile = "map.txt";
			String lightfile = "light.txt";

			File ff = new File(mapfile);
			if (!ff.exists()) {
				System.out.println("Map file does not exist!");
				System.exit(0);
			}

			InfoLogger iLogger = new InfoLogger(pathname);
			InputHandler inputHandler = new InputHandler(reg, 80, mainreq, iLogger);

			temp = inputHandler.getmap(mapfile);
			if (temp == null || !inputHandler.validcheck(temp)) {
				System.out.println("Invalid map file!");
				System.exit(0);
			}

			TaxiGUI gui = new TaxiGUI();
			gui.LoadMap(temp, 80);

			MyFlag[] flags = new MyFlag[100];
			for(int i = 0; i < 100; i++) {
				flags[i] = new MyFlag();
			}
			CityMap map = new CityMap(temp, 80, flags, gui);

			if (map.loadlight(lightfile) == false) {
				System.out.println("Wrong Light file!");
				System.exit(0);
			}

			init_taix(taxilist, map, 80);
			for(int i = 0; i < 100; i++) {
				Taxi tt = taxilist.get(i);
				tt.setflag(flags[i]);
			}

			LightCtr lightCtr = new LightCtr(map, gui);
			GUIThread guiThread = new GUIThread(temp, taxilist, mainreq, 80, gui);

			for(int i = 0; i < taxinum; i++) {
				Taxi tt = taxilist.get(i);
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

	public static void init_taix(Vector<Taxi> taxilist, CityMap map, int size) {
		/**
		 * @REQUIRES: taxilist != null && map != null && size > 0;
		 * 
		 * @MODIFIES: taxilist
		 * 
		 * @EFFECTS: 创建100个Taxi对象， 其中30个为TraceTaxi类型，剩余70个为普通Taxi类型，任何两个出租车的编号不同，且最小为0， 最大为99。
		 *           并将这100个对象存储在taxilist中。
		 */

		// Example:
		for(int i = 0; i < 100; i++) {
			if (i >= 70) {
				Taxi tt = new TraceTaxi(i, size, map);
				taxilist.addElement(tt);
			} else {
				Taxi taxi = new Taxi(i, size, map);
				taxilist.add(taxi);
			}
		}

	}
}
