package week_07;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

public class Taxi {
	private int credit;
	private int status;
	private int number;
	public CityMap map;
	private Point position;
	private Request seReq;
	/*
	 * stop:status = 0; serving:status = 1; waiting:status = 2; pick:status = 3;
	 */

	public Taxi(int num, int size, CityMap mp) {
		Random random = new Random();
		number = num;
		credit = 0;
		status = 2;
		map = mp;
		seReq = null;
		position = new Point(random.nextInt(size), random.nextInt(size));
	}

	int getcredit() {
		return credit;
	}

	Point getposition() {
		return position;
	}

	int getstatus() {
		return status;
	}

	Request getreq() {
		return seReq;
	}

	int getnum() {
		return number;
	}

	public void setstatus(int a) {
		status = a;
	}

	public void setreq(Request re) {
		if (re == null) {
			seReq = null;
			return;
		}
		Point sPoint = new Point(re.getsrc().x, re.getsrc().y);
		Point dPoint = new Point(re.getdest().x, re.getdest().y);

		seReq = new Request(sPoint, dPoint, re.gettime(), re.number);
		return;
	}

	public void addcredit(int a) {
		credit += a;
	}

	public void randommove() {
		Vector<Point> pp = new Vector<>(0, 1);
		pp.add(new Point(position.x - 1, position.y));
		pp.add(new Point(position.x + 1, position.y));
		pp.add(new Point(position.x, position.y - 1));
		pp.add(new Point(position.x, position.y + 1));

		long tt = System.currentTimeMillis();
		for(int i = 0; i < pp.size(); i++) {
			Point point = pp.get(i);
			if (point.x >= 0 && point.y >= 0 && point.x < map.size && point.y < map.size
					&& map.isconnect(point, position))
				continue;
			pp.remove(i--);
		}
		int bound = pp.size();
		Random random = new Random();
		int ch = random.nextInt(bound);

		try {
			if (System.currentTimeMillis() - tt < 200)
				Thread.sleep(200 - (System.currentTimeMillis() - tt));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tt = System.currentTimeMillis();
		position = pp.get(ch);
	}

	public void move(Vector<Point> path, long tt) {
		for(int i = 0; i < path.size(); i++) {
			if (path.get(i).equals(position))
				continue;
			try {
				if (System.currentTimeMillis() - tt < 200)
					Thread.sleep(200 - (System.currentTimeMillis() - tt));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tt = System.currentTimeMillis();
			position = path.get(i);
		}
	}

	public String toString() {
		String sta = null;
		switch (status) {
		case 0:
			sta = "Stop";
			break;
		case 1:
			sta = "Serving";
			break;
		case 2:
			sta = "Waiting";
			break;
		case 3:
			sta = "Picking";
			break;

		default:
			break;
		}
		String num = "";
		if (number < 10) {
			num = "0" + number;
		} else num = Integer.toString(number);
		String ss = "Taxi: " + num + "   status: " + sta + "   credit: " + credit + "   position:" + "("
				+ (position.x + 1) + " ," + (position.y + 1) + ")";
		return ss;
	}

	public boolean grebdeal(Vector<Integer> list) {
		for(int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(number))
				return true;
		}
		credit += 1;
		list.addElement(number); // put itself into waiting list
		return true;
	}
}
