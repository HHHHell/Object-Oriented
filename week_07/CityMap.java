package week_07;

import java.awt.Point;
import java.util.Vector;

public class CityMap {
	int[][] map;
	int size;

	public CityMap(int[][] mm, int s) {
		map = mm;
		size = s;
	}

	private int getnum(Point pp) {
		return pp.x * size + pp.y;
	}

	private Point getpoint(int num) {
		return new Point(num / size, num % size);
	}

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

	public Vector<Point> shortestpath(Point sPoint, Point dPoint) {
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
	}

	public Vector<Integer> shorstdistence(Point root, Vector<Point> dPoints) {
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
	}

	public boolean isinrange(Point centre, Point a, int range) {
		if (centre.equals(a))
			return false;
		if (Math.abs(centre.x - a.x) <= range && Math.abs(centre.y - a.y) <= range)
			return true;
		return false;
	}

	public boolean isconnect(Point aPoint, Point bPoint) {
		// System.out.println(aPoint + " " + bPoint);
		int avalue = map[aPoint.x][aPoint.y];
		int bvalue = map[bPoint.x][bPoint.y];

		if (Math.abs(aPoint.x - bPoint.x) + Math.abs(aPoint.y - bPoint.y) != 1) {
			// System.out.println(false);
			return false;
		}

		if (aPoint.x < bPoint.x && (avalue == 2 || avalue == 3)) {
			// System.out.println(true);
			return true;
		}
		if (aPoint.x > bPoint.x && (bvalue == 2 || bvalue == 3)) {
			// System.out.println(true);
			return true;
		}
		if (aPoint.y < bPoint.y && (avalue == 1 || avalue == 3)) {
			// System.out.println(true);
			return true;
		}
		if (aPoint.y > bPoint.y && (bvalue == 1 || bvalue == 3)) {
			// System.out.println(true);
			return true;
		}

		// System.out.println(false);
		return false;
	}
}
