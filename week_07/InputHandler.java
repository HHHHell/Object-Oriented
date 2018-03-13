package week_07;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class InputHandler extends Thread {
	private String regex;
	private int size;
	private Reqlist reqlist;
	private int count;
	private InfoLogger logger;

	public InputHandler(String reg, int s, Reqlist re, InfoLogger iLogger) {
		regex = reg;
		size = s;
		reqlist = re;
		count = 0;
		logger = iLogger;
	}

	// [CR,(15,20),(79,66)]
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

	public void run() {
		Scanner scanner = new Scanner(System.in);
		try {
			while (scanner.hasNextLine()) {
				String strline = scanner.nextLine();
				Request request = getreq(strline, regex, size);
				// System.out.println(request);
				boolean flag = false;
				if (request == null) {
					System.out.println("Wrong Request!");
					continue;
				}
				for(int i = 0; i < reqlist.getsize(); i++) {
					if (request.equals(reqlist.get(i)))
						flag = true;
				}
				if (flag) {
					System.out.println("Same Requset!");
					continue;
				}
				synchronized (reqlist) {
					request.number = count++;
					reqlist.add(request);
				}

				logger.readrequest(request);
			}
		} catch (Exception e) {
			System.out.println("InputHandler Exception:" + e);
			System.exit(0);
		}
		scanner.close();
	}
}
