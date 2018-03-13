package week_10;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
 * @OVERVIEW: 提供输入处理方法
 * 不变式： regex != null && size > 0 && reqlist != null && logger.repOK == true && map.repOK == true ==> \result = true;
 */
public class InputHandler extends Thread {
	private String regex;
	private int size;
	private Reqlist reqlist;
	private InfoLogger logger;
	private CityMap map;

	/*
	 * @ REQUIRES: re != null， iLogger != null；
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 创建InputHandler对象
	 */
	public InputHandler(String reg, int s, Reqlist re, InfoLogger iLogger) {
		regex = reg;
		size = s;
		reqlist = re;
		logger = iLogger;
	}
	
	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: regex != null && size > 0 && reqlist != null && logger.repOK
	 * == true && map.repOK == true ==> \result = true;
	 *
	 */
	public boolean repOK() {
		if (regex == null || size <= 0 || reqlist == null)
			return false;
		if (logger.repOK() == false || map.repOK() == false)
			return false;
		return true;
	}

	/*
	 * @ REQUIRES: mCityMap != null；
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: map = mCityMap
	 */
	public void setmap(CityMap mCityMap) {
		map = mCityMap;
	}

	/*
	 * @ REQUIRES: regex != null, string != null；
	 * 
	 * @ MODIFIES: string
	 * 
	 * @ EFFECTS: 从string中获得乘客请求并返回，如果输入不合法，返回null
	 */
	public static Request getreq(String string, String regex, int size) {
		string = string.replaceAll(" ", "");
		if (!string.matches(regex))
			return null;
		string.replaceAll("\\[|\\]", "");
		String[] strings = string.split("\\(|\\)");

		int x = 0, y = 0;
		String[] pa = strings[1].split(",");
		x = Integer.parseInt(pa[0]);
		y = Integer.parseInt(pa[1]);
		if (x > size || y > size || x < 1 || y < 1) {
			return null;
		}
		Point aPoint = new Point(x - 1, y - 1);

		x = 0;
		y = 0;
		String[] pb = strings[3].split(",");
		x = Integer.parseInt(pb[0]);
		y = Integer.parseInt(pb[1]);
		if (x > size || y > size || x < 1 || y < 1) {
			return null;
		}
		Point bPoint = new Point(x - 1, y - 1);
		if (aPoint.equals(bPoint)) {
			return null;
		}
		Request request = new Request(aPoint, bPoint, System.currentTimeMillis());
		return request;
	}

	/*
	 * @ REQUIRES: subregex != null, strline != null；
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 从string中获得道路请求并返回，如果输入不合法，返回null
	 */
	public RoadRequest getroadreq(String strline, String subregex) {
		String ss = strline.replaceAll(" ", "");
		if (ss.matches(subregex)) {
			ss.replaceAll("\\[|\\]", "");
			String[] subs = ss.split("\\(|\\)");

			int x = 0, y = 0;
			String[] pa = subs[1].split(",");
			x = Integer.parseInt(pa[0]);
			y = Integer.parseInt(pa[1]);
			if (x > size || y > size || x < 1 || y < 1) {
				return null;
			}
			Point aPoint = new Point(x - 1, y - 1);

			x = 0;
			y = 0;
			String[] pb = subs[3].split(",");
			x = Integer.parseInt(pb[0]);
			y = Integer.parseInt(pb[1]);
			if (x > size || y > size || x < 1 || y < 1) {
				return null;
			}
			Point bPoint = new Point(x - 1, y - 1);
			if (aPoint.equals(bPoint)) {
				return null;
			}
			String flag = "";
			if (subs[0].replaceAll(",", "").equals("Open"))
				flag = "Open";
			else flag = "Close";
			RoadRequest roadRequest = new RoadRequest(aPoint, bPoint, System.currentTimeMillis(), flag);
			return roadRequest;
		}
		return null;
	}

	/*
	 * @ REQUIRES: filepath != null && file exist;
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 从文件中获得地图信息并返回，如果输入不合法，返回null
	 */
	public int[][] getmap(String filepath) {
		int[][] map = new int[size][size];
		File file = new File(filepath);
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 0;
			while ((tempString = bReader.readLine()) != null) {
				if (line > size) {
					bReader.close();
					return null;
				}
				tempString = tempString.replaceAll(" |\t", "");
				String rre = "[0123]{" + size + "}";
				if (!tempString.matches(rre)) {
					bReader.close();
					return null;
				}
				char[] chlist = tempString.toCharArray();
				for(int j = 0; j < chlist.length; j++) {
					map[line][j] = Character.getNumericValue(chlist[j]);
				}
				line++;
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/*
	 * @ REQUIRES: map != null;
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 初步检测是否有孤立点，没有返回true，否则返回false
	 */
	public boolean validcheck(int[][] map) {
		if (map.length != size) {
			return false;
		}

		for(int i = 0; i < size; i++) {
			if (map[i].length != size) {
				return false;
			}
			for(int j = 0; j < size; j++) {
				if (map[i][j] == 0) {
					if (i == 0 && j == 0) {
						return false;
					}
					if (i == 0 && map[i][j - 1] != 1 && map[i][j - 1] != 3) {
						return false;
					}
					if (j == 0 && map[i - 1][j] != 2 && map[i - 1][j] != 3) {
						return false;
					}
					int ii = map[i - 1][j];
					int jj = map[i][j - 1];
					if (ii != 3 && ii != 2 && jj != 3 && jj != 1) {
						return false;
					}
				}
				if (i == size) {
					if (map[i][j] == 3 || map[i][j] == 2) {
						return false;
					}
				}
				if (j == size) {
					if (map[i][j] == 3 || map[i][j] == 1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: mainlist, map
	 * 
	 * @ EFFECTS: 获取输入的请求，并将其放入相应的请求队列中
	 */
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String subregex = "\\[(Close|Open),\\(\\+?\\d{1,2},\\+?\\d{1,2}\\),\\(\\+?\\d{1,2},\\+?\\d{1,2}\\)\\]";
		try {
			while (scanner.hasNextLine()) {
				String strline = scanner.nextLine();
				Request request = getreq(strline, regex, size);

				if (request == null) {
					RoadRequest roadRequest = getroadreq(strline, subregex);
					if (roadRequest == null) {
						System.out.println("Wrong Request!");
						continue;
					}
					if (roadRequest.getact().equals("Open")) {
						boolean t1 = map.roadopen(roadRequest.getsrc(), roadRequest.getdest());
						if (t1 == false)
							System.out.println("Wrong Road Requeset!");
						else System.out.println("Road Opend!");
						continue;
					}
					boolean t2 = map.roadclose(roadRequest.getsrc(), roadRequest.getdest());
					if (t2 == false)
						System.out.println("Wrong Road Requeset!");
					else System.out.println("Road Closed!");
					continue;
				}

				Request aRequest = null;
				synchronized (reqlist) {
					int tt = 0;
					if ((tt = reqlist.getsize()) != 0) {
						aRequest = reqlist.get(tt - 1);
					}
					if (aRequest != null && request.equals(aRequest)) {
						System.out.println("Same Requset!");
						continue;
					}
				}

				request.number = Request.mkid();
				reqlist.add(request);

				logger.readrequest(request);
			}
		} catch (Exception e) {
			System.out.println("InputHandler Exception:" + e);
			System.exit(0);
		}
		scanner.close();
	}
}
