package week_11;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @OVERVIEW: 存储地图信息、道路流量信息、道路红绿灯、红绿灯持续时间等信息，提供寻找最短路劲、查询红绿灯、流量增减、道路开闭等功能函数。
 * 
 * @RepInvariant:(\all 0 <= i, j < size, map[i][j] == 0 || map[i][j] == 1 || map[i][j] == 2 || map[i][j] == 3) &&
 *                     (\all 0 <= i, j < size*size, flow[i][j] >= 0) &&
 *                     (\all 0 <= i, j < size, light[i][j] >= 0) && (size > 0) && (flags != null) &&
 *                     (\all 0 <= i < 100, flags[i] != null) && (gui != null) && (lock != null) ==>\result == true;
 */
public class CityMap {
	int[][] map;
	Integer[][] flow;
	Integer[][] light;
	int size;
	int lasttime;
	MyFlag[] flags;
	ReadWriteLock lock;
	TaxiGUI gui;

	public CityMap(int[][] mm, int s, MyFlag[] ff, TaxiGUI taxiGUI) {
		/**
		 * @REQUIRES: mm != null;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据传入的参数构建一个CityMap对象
		 */

		Random random = new Random();
		lasttime = random.nextInt(300) + 200;
		map = mm;
		size = s;
		lock = new ReentrantReadWriteLock();
		flow = new Integer[size * size][size * size];
		light = new Integer[size][size];
		flags = ff;
		gui = taxiGUI;
		for(int i = 0; i < size * size; i++) {
			for(int j = 0; j < size * size; j++) {
				flow[i][j] = 0;
			}
		}
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				light[i][j] = 0;
			}
		}
	}

	public CityMap(CityMap mod) {
		/**
		 * @REQUIRES: mm != null;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: Copy a map from mod
		 */
		
		lasttime = mod.lasttime;
		size = mod.size;
		map = new int[size][size];
		flow = mod.flow;
		light = mod.light;
		flags = mod.flags;
		lock = mod.lock;
		gui = mod.gui;

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				map[i][j] = mod.map[i][j];
			}
		}
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: (\all 0 <= i, j < size, map[i][j] == 0 || map[i][j] == 1 || map[i][j] == 2 || map[i][j] == 3) &&
		 *           (\all 0 <= i, j < size*size, flow[i][j] >= 0) && (\all 0 <= i, j < size, light[i][j] >= 0) &&
		 *           (size > 0) && (flags != null) && (\all 0 <= i < 100, flags[i] != null) && (gui != null) &&
		 *           (lock != null) ==> \result == true;
		 */
		if (map == null || flow == null || light == null || flags == null || lock == null || gui == null)
			return false;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				int m = map[i][j];
				int f = flow[i][j];
				if (m != 0 && m != 1 && m != 2 && m != 3)
					return false;
				if (f < 0)
					return false;
			}
		}
		for(int i = 0; i < 100; i++) {
			if (flags[i] == null)
				return false;
		}
		if (size <= 0 || lasttime < 50)
			return false;
		return true;
	}

	private int getnum(Point pp) {
		/**
		 * @REQUIRES: pp != null;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 返回点pp对应的编号
		 * 
		 */
		return pp.x * size + pp.y;
	}

	private Point getpoint(int num) {
		/**
		 * @REQUIRES:num >= 0;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS:返回编号为num的点
		 * 
		 */
		Point pp = new Point(num / size, num % size);
		return pp;
	}

	public int getflow(Point aPoint, Point bPoint) {
		/**
		 * @REQUIRES: aPoint != null, bPoint != null;
		 * 
		 *            aPoint.num <= getnum(Point(79,79)),aPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == road flow of road between aPoint and bpoint
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked(flow[aPoint.num][bPoint.num])
		 * 
		 */
		int anum = getnum(aPoint);
		int bnum = getnum(bPoint);
		int temp = 0;

		synchronized (flow[anum][bnum]) {
			temp = flow[anum][bnum];
		}
		return temp;
	}

	public boolean loadlight(String filepath) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: light
		 * 
		 * @EFFECTS: load successfully ==> \result == true, otherwise \result == false;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked(light)
		 * 
		 */
		if (filepath == null)
			return false;
		File file = new File(filepath);
		if (file.exists() == false)
			return false;
		try {
			synchronized (light) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				int line = 0;
				while ((tempString = reader.readLine()) != null) {
					if (line > size) {
						reader.close();
						return false;
					}

					tempString = tempString.replaceAll(" |\t", "");
					String rre = "[01]{" + size + "}";
					if (!tempString.matches(rre)) {
						reader.close();
						return false;
					}
					char[] chlist = tempString.toCharArray();
					for(int j = 0; j < chlist.length; j++) {
						int num = line * size + j;
						Point pp = getpoint(num);
						int nn = Character.getNumericValue(chlist[j]);
						if (iscross(pp) == false && nn == 1) {
							System.out.println("Wrong light control in (" + line + "," + j + ")");
							nn = 0;
						}
						light[line][j] = nn;
					}
					line++;
				}
				reader.close();
			}
		} catch (IOException e) {
			System.out.println(e);
			System.exit(0);
		}
		return true;
	}

	public boolean getlight(Point fPoint, Point sPoint, Point dPoint) {
		/**
		 * @REQUIRES: sPoint != null, isconnect(sPoint, dPoint) == true;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == light[sPoint.x][sPoint.y];
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.readLock().lock();
		try {
			int slight = light[sPoint.x][sPoint.y];

			int fnum = getnum(fPoint);
			int snum = getnum(sPoint);
			int dnum = getnum(dPoint);

			int flag = fnum - snum;
			int aflag = dnum - snum;

			if (fnum == snum)
				return true;
			if (slight == 0)
				return true;
			if (flag == aflag)
				return true;
			if ((flag == -80 && aflag == -1) || (flag == -1 && aflag == 80) || (flag == 80 && aflag == 1)
					|| (flag == 1 && aflag == -80))
				return true;
			if (slight == 1) {
				if ((flag == -1 && aflag == 1) || (flag == 1 && aflag == -1))
					return true;
				if ((flag == 80 && aflag == -1) || (flag == -80 && aflag == 1))
					return true;
			}
			if (slight == 2) {
				if ((flag == 80 && aflag == -80) || (flag == -80 && aflag == 80))
					return true;
				if ((flag == -1 && aflag == -80) || (flag == 1 && aflag == 80))
					return true;
			}
			return false;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void turnlight() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: light
		 * 
		 * @EFFECTS: \all 0 <= i, j < size, light[i][j] == 1 ==> light[i][j] == 2,
		 *           light[i][j] == 2 ==> light[i][j] == 1;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.writeLock().lock();
		try {
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if (light[i][j] == 1) {
						light[i][j] = 2;
					} else if (light[i][j] == 2) {
						light[i][j] = 1;
					}
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void addflow(Point aPoint, Point bPoint) {
		/**
		 * @REQUIRES: aPoint != null, bPoint != null;
		 * 
		 *            aPoint.num <= getnum(Point(79,79)), bPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: flow[aPoint.num][bPoint.num]
		 * 
		 * @EFFECTS: flow[aPoint.num][bPoint.num] += 1;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked(flow[aPoint.num][bPoint.num])
		 * 
		 */
		int anum = getnum(aPoint);
		int bnum = getnum(bPoint);

		synchronized (flow[anum][bnum]) {
			flow[anum][bnum]++;
		}
		return;
	}

	public void subflow(Point aPoint, Point bPoint) {
		/**
		 * @REQUIRES: aPoint != null, bPoint != null;
		 * 
		 *            aPoint.num <= getnum(Point(79,79)), bPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: flow[aPoint.num][bPoint.num]
		 * 
		 * @EFFECTS: flow[aPoint.num][bPoint.num] == \old(flow[aPoint.num][bPoint.num]) - 1;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked(flow[aPoint.num][bPoint.num])
		 * 
		 */
		int anum = getnum(aPoint);
		int bnum = getnum(bPoint);

		synchronized (flow[anum][bnum]) {
			flow[anum][bnum]--;
		}
		return;
	}

	public Vector<Point> shortestpath(Point sPoint, Point dPoint) {
		/**
		 * @REQUIRES: sPoint != null, dPoint != null;
		 * 
		 *            sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == shortest path from sPoint to dPoint
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.readLock().lock();
		try {
			Vector<Point> shortpath = new Vector<>();
			int[] weight = new int[6405];
			int[] dep = new int[6405];

			Vector<Vector<Point>> paths = new Vector<Vector<Point>>();
			int[] off = new int[] { -1, 1, -80, 80 };
			boolean[] view = new boolean[6405];
			Vector<Integer> queue = new Vector<>();

			for(int i = 0; i < 6405; i++) {
				weight[i] = 65536;
				view[i] = false;
				dep[i] = -2333;
				Vector<Point> pp = new Vector<Point>();
				if (i == getnum(sPoint)) {
					pp.add(sPoint);
					weight[i] = 0;
					dep[i] = 0;
				}
				paths.add(pp);
			}
			queue.add(getnum(sPoint));

			int depth = 0;
			while (queue.size() > 0) {
				int now = queue.get(0);
				Point nowp = getpoint(now);
				if (dep[now] == dep[getnum(dPoint)] + 1)
					break;
				view[now] = true;
				depth = dep[now] + 1;
				Vector<Point> nexttemp = paths.get(depth);
				for(int i = 0; i < 4; i++) {
					int next = now + off[i];
					Point nextp = getpoint(next);
					if (next >= 0 && next < 6400 && isconnect(nowp, nextp) == true) {
						if (view[next] == false) {
							view[next] = true;
							dep[next] = depth;
							nexttemp.add(nextp);
							weight[next] = weight[now] + flow[next][now];
							queue.add(next);
						} else if (flow[next][now] + weight[now] < weight[next]) {
							weight[next] = flow[next][now] + weight[now];
						}
					}
				}
				paths.add(nexttemp);
				queue.remove(0);
			}

			shortpath.add(dPoint);
			Point lastp = dPoint;
			for(int i = dep[getnum(dPoint)] - 1; i > 0; i--) {
				Vector<Point> nexttemp = paths.get(i);
				int min = 65536;
				Point pp = nexttemp.get(0);
				for(int j = 0; j < nexttemp.size(); j++) {
					Point nowp = nexttemp.get(j);
					int now = getnum(nowp);
					if (weight[now] < min && isconnect(lastp, nowp)) {
						pp = nowp;
						min = weight[now];
					}
				}
				shortpath.add(pp);
				lastp = pp;
			}
			shortpath.addElement(sPoint);
			Vector<Point> tmp = new Vector<>();
			for(int i = shortpath.size() - 1; i >= 0; i--) {
				Point point = shortpath.get(i);
				tmp.add(point);
			}
			shortpath = tmp;
			return tmp;
		} finally {
			lock.readLock().unlock();
		}
	}

	public Vector<Integer> shorstdistence(Point root, Vector<Point> dPoints) {
		/**
		 * @REQUIRES: sPoint != null, dPoint != null;
		 * 
		 *            sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == shortest distance from sPoint to dPoint
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.readLock().lock();
		try {
			Vector<Integer> pointdis = new Vector<Integer>();
			int[] off = new int[] { -1, 1, -80, 80 };
			boolean[] view = new boolean[6400];
			Vector<Integer> queue = new Vector<>();

			int[] distence = new int[6400];
			for(int i = 0; i < 6400; i++) {
				if (i == getnum(root)) {
					distence[i] = 0;
					continue;
				} else distence[i] = 65536;
			}
			queue.add(getnum(root));
			while (queue.size() != 0) {
				int num = queue.get(0);
				int depth = distence[num];
				view[num] = true;
				for(int i = 0; i < 4; i++) {
					int next = num + off[i];
					if (next >= 0 && next < 6400 && isconnect(getpoint(num), getpoint(next)) && view[next] == false) {
						view[next] = true;
						queue.add(next);
						distence[next] = depth + 1;
					}
				}
				queue.remove(0);
			}
			for(int i = 0; i < dPoints.size(); i++) {
				int num = getnum(dPoints.get(i));
				Integer pInteger = distence[num];
				pointdis.add(pInteger);
			}
			return pointdis;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean roadclose(Point aPoint, Point bPoint) {
		/**
		 * @REQUIRES: sPoint != null, dPoint != null;
		 * 
		 *            sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: map
		 * 
		 * @EFFECTS: \result == true ==> \all i, 0 <= i < 100, changeflag[i] == true && isconnect(aPoint, bPoint) == false;
		 * 			 \result == false ==> isconnect(aPoint, bPoint) == true;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.writeLock().lock();
		boolean result = false;
		Point sPoint = aPoint, dPoint = bPoint;
		try {

			if (aPoint.equals(bPoint) || !isconnect(aPoint, bPoint))
				return false;
			if (aPoint.x > bPoint.x || aPoint.y > bPoint.y) {
				sPoint = bPoint;
				dPoint = aPoint;
			} else {
				sPoint = aPoint;
				dPoint = bPoint;
			}
			int temp = map[sPoint.x][sPoint.y];

			if (sPoint.x < dPoint.x) {
				map[sPoint.x][sPoint.y] -= 2;
			} else map[sPoint.x][sPoint.y] -= 1;
			if (map[sPoint.x][sPoint.y] >= 0) {
				result = true;
				return result;
			} else {
				map[sPoint.x][sPoint.y] = temp;
				return false;
			}
		} finally {
			if (result) {
				for(int i = 0; i < flags.length; i++) {
					flags[i].setflag(true);
				}
				gui.SetRoadStatus(aPoint, bPoint, 0);

				if (!iscross(sPoint)) {
					light[sPoint.x][sPoint.y] = 0;
				}
				if (!iscross(dPoint)) {
					light[dPoint.x][dPoint.y] = 0;
				}
			}
			lock.writeLock().unlock();
		}
	}

	public boolean roadopen(Point aPoint, Point bPoint) {
		/**
		 * @REQUIRES: sPoint != null, dPoint != null;
		 * 
		 *            sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: map
		 * 
		 * @EFFECTS: if open successfully, return true, otherwise return false
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.writeLock().lock();
		boolean result = false;
		try {
			Point sPoint, dPoint;
			if (aPoint.equals(bPoint))
				return false;
			if (isconnect(aPoint, bPoint) || Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y) != 1)
				return false;
			if (aPoint.x > bPoint.x || aPoint.y > bPoint.y) {
				sPoint = bPoint;
				dPoint = aPoint;
			} else {
				sPoint = aPoint;
				dPoint = bPoint;
			}
			int temp = map[sPoint.x][sPoint.y];
			if (sPoint.x < dPoint.x) {
				map[sPoint.x][sPoint.y] += 2;
			} else map[sPoint.x][sPoint.y] += 1;
			if (map[sPoint.x][sPoint.y] <= 3) {
				result = true;
				return result;
			} else {
				map[sPoint.x][sPoint.y] = temp;
				return false;
			}
		} finally {
			if (result) {
				for(int i = 0; i < flags.length; i++) {
					flags[i].setflag(true);
				}
				gui.SetRoadStatus(aPoint, bPoint, 1);
			}
			lock.writeLock().unlock();
		}
	}

	public boolean isinrange(Point centre, Point a, int range) {
		/**
		 * @REQUIRES: cerntre != null, a != null;
		 *            centre.num <= getnum(Point(79,79)),a.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 如果a在以centre为中心的range范围内, 返回 true, 否则饭后false
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.readLock().lock();
		try {
			if (centre.equals(a))
				return false;
			if (Math.abs(centre.x - a.x) <= range && Math.abs(centre.y - a.y) <= range)
				return true;
			return false;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isconnect(Point aPoint, Point bPoint) {
		/**
		 * @REQUIRES: aPOint != null, bPoint != null;
		 * 
		 *            aPoint.num <= getnum(Point(79,79)), bPoint.num <= getnum(Point(79,79));
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 如果aPoint与bPoint有边直接相连, 返回 true, 否则饭后false
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.readLock().lock();
		try {
			int avalue = map[aPoint.x][aPoint.y];
			int bvalue = map[bPoint.x][bPoint.y];

			if (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y) != 1) {
				return false;
			}

			if (aPoint.x < bPoint.x && (avalue == 2 || avalue == 3)) {
				return true;
			}
			if (aPoint.x > bPoint.x && (bvalue == 2 || bvalue == 3)) {
				return true;
			}
			if (aPoint.y < bPoint.y && (avalue == 1 || avalue == 3)) {
				return true;
			}
			if (aPoint.y > bPoint.y && (bvalue == 1 || bvalue == 3)) {
				return true;
			}

			return false;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean iscross(Point sPoint) {
		/**
		 * @REQUIRES: aPOint != null;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: sPoint 是十字路口或丁字路口，返回true，否则返回false
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 * 
		 */
		lock.readLock().lock();
		try {
			int count = 0;
			int nowp = getnum(sPoint);
			int[] off = { -1, 1, 80, -80 };

			for(int i = 0; i < 4; i++) {
				int next = nowp + off[i];
				if (next >= 0 && next < size * size) {
					Point bPoint = getpoint(next);
					if (isconnect(sPoint, bPoint))
						count++;
					if (count >= 3)
						return true;
				}
			}
			return false;
		} finally {
			lock.readLock().unlock();
		}
	}
}