package week_09;

import java.awt.Point;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;

public class CityMap {
	int[][] map;
	Integer[][] flow;
	int size;
	MyFlag[] flags;
	ReadWriteLock lock;
	TaxiGUI gui;
	
	/*
	 * @ REQUIRES: mm != null;
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 根据传入的参数构建一个CityMap对象
	 * 
	 * @
	 */
	public CityMap(int[][] mm, int s, ReadWriteLock rwlock, MyFlag[] ff, TaxiGUI taxiGUI) {
		map = mm;
		size = s;
		lock = rwlock;
		flow = new Integer[size * size][size * size];
		flags = ff;
		gui = taxiGUI;
		for(int i = 0; i < size * size; i++) {
			for(int j = 0; j < size * size; j++) {
				flow[i][j] = 0;
			}
		}
	}

	/*
	 * @ REQUIRES: pp != null;
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 返回点pp对应的编号
	 * 
	 * @
	 */
	private int getnum(Point pp) {
		return pp.x * size + pp.y;
	}

	/*
	 * @ REQUIRES:num >= 0;
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS:返回编号为num的点
	 * 
	 * @
	 */
	private Point getpoint(int num) {
		return new Point(num / size, num % size);
	}

	/*
	 * @ REQUIRES: sVector != null, dVector != null;
	 * 
	 * @ MODIFIES: dVector
	 * 
	 * @ EFFECTS: \forall pp in sVector;
	 * 
	 * @ dVector.add(pp);
	 * 
	 * @
	 */
	private void copy(Vector<Point> sVector, Vector<Point> dVector) {
		for(int i = 0; i < dVector.size(); i++) {
			dVector.remove(i--);
		}
		for(int i = 0; i < sVector.size(); i++) {
			Point pp = sVector.get(i);
			Point point = new Point(pp.x, pp.y);
			dVector.addElement(point);
		}
	}

	/*
	 * @ REQUIRES: aPoint != null, bPoint != null;
	 * 
	 * @ aPoint.num <= getnum(Point(79,79)),aPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: \result = road flow of road between aPoint and bpoint
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked(flow[aPoint.num][bPoint.num])
	 * 
	 * @
	 */
	public int getflow(Point aPoint, Point bPoint) {
		int anum = getnum(aPoint);
		int bnum = getnum(bPoint);
		int temp = 0;

		synchronized (flow[anum][bnum]) {
			temp = flow[anum][bnum];
		}
		return temp;
	}

	/*
	 * @ REQUIRES: aPoint != null, bPoint != null;
	 * 
	 * @ aPoint.num <= getnum(Point(79,79)), bPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: flow[aPoint.num][bPoint.num]
	 * 
	 * @ EFFECTS: flow[aPoint.num][bPoint.num]+=1;
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked(flow[aPoint.num][bPoint.num])
	 * 
	 * @
	 */
	public void addflow(Point aPoint, Point bPoint) {
		int anum = getnum(aPoint);
		int bnum = getnum(bPoint);

		synchronized (flow[anum][bnum]) {
			flow[anum][bnum]++;
		}
		return;
	}

	/*
	 * @ REQUIRES: aPoint != null, bPoint != null;
	 * 
	 * @ aPoint.num <= getnum(Point(79,79)), bPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: flow[aPoint.num][bPoint.num]
	 * 
	 * @ EFFECTS: flow[aPoint.num][bPoint.num]-=1;
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked(flow[aPoint.num][bPoint.num])
	 * 
	 * @
	 */
	public void subflow(Point aPoint, Point bPoint) {
		int anum = getnum(aPoint);
		int bnum = getnum(bPoint);

		synchronized (flow[anum][bnum]) {
			flow[anum][bnum]--;
		}
		return;
	}

	/*
	 * @ REQUIRES: sPoint != null, dPoint != null;
	 * 
	 * @ sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: \result = shortest path from sPoint to dPoint
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked()
	 * 
	 * @
	 */
	public Vector<Point> shortestpath(Point sPoint, Point dPoint) {
		lock.readLock().lock();
		try {
			Vector<Point> shortestpath = new Vector<Point>();
			Vector<Vector<Point>> paths = new Vector<Vector<Point>>();
			int[] off = new int[] { -1, 1, -80, 80 };
			boolean[] view = new boolean[6400];
			Vector<Integer> queue = new Vector<>();

			for(int i = 0; i < 6400; i++) {
				Vector<Point> pp = new Vector<Point>();
				if (i == getnum(sPoint))
					pp.add(sPoint);
				paths.add(pp);
			}
			queue.add(getnum(sPoint));

			while (queue.size() != 0) {
				int num = queue.get(0);
				if (num == getnum(dPoint))
					break;
				view[num] = true;
				for(int i = 0; i < 4; i++) {
					int next = num + off[i];
					if (next >= 0 && next < 6400 && isconnect(getpoint(num), getpoint(next)) && view[next] == false) {
						view[next] = true;
						queue.add(next);
						copy(paths.get(num), paths.get(next));
						paths.get(next).add(getpoint(next));
					}
				}
				queue.remove(0);
			}
			shortestpath = paths.get(getnum(dPoint));
			return shortestpath;
		} finally {
			lock.readLock().unlock();
		}
	}

	/*
	 * @ REQUIRES: sPoint != null, dPoint != null;
	 * 
	 * @ sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: \result = shortest distance from sPoint to dPoint
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked()
	 * 
	 * @
	 */
	public Vector<Integer> shorstdistence(Point root, Vector<Point> dPoints) {
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

	/*
	 * @ REQUIRES: sPoint != null, dPoint != null;
	 * 
	 * @ sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: map
	 * 
	 * @ EFFECTS: if close successfully, return true, otherwise return false
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked()
	 * 
	 * @
	 */
	public boolean roadclose(Point aPoint, Point bPoint) {
		lock.writeLock().lock();
		boolean result = false;
		try {

			Point sPoint, dPoint;
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
				System.out.println("Closed in map!");
			}
			lock.writeLock().unlock();
		}
	}

	/*
	 * @ REQUIRES: sPoint != null, dPoint != null;
	 * 
	 * @ sPoint.num <= getnum(Point(79,79)),dPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: map
	 * 
	 * @ EFFECTS: if open successfully, return true, otherwise return false
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked()
	 * 
	 * @
	 */
	public boolean roadopen(Point aPoint, Point bPoint) {
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
				System.out.println("Opend in map!");
			}
			lock.writeLock().unlock();
		}
	}

	/*
	 * @ REQUIRES: cerntre != null, a != null;
	 * 
	 * @ centre.num <= getnum(Point(79,79)),a.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 如果a在以centre为中心的range范围内, 返回 true, 否则饭后false
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked()
	 * 
	 * @
	 */
	public boolean isinrange(Point centre, Point a, int range) {
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

	/*
	 * @ REQUIRES: aPOint != null, bPoint != null;
	 * 
	 * @ aPoint.num <= getnum(Point(79,79)), bPoint.num <= getnum(Point(79,79));
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 如果aPoint与bPoint有边直接相连, 返回 true, 否则饭后false
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: \locked()
	 * 
	 * @
	 */
	public boolean isconnect(Point aPoint, Point bPoint) {
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
}
