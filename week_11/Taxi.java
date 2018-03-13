package week_11;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @OVERVIEW: 存储出租车的状态信息， 提供运动方法
 * 
 * @RepInvariant: credit > =0 && status >=0 && number >= 0 && map.repOK() == true &&
 *                position != null && lastpos != null && changeflag != null
 *                && lock != null ==> \result = true;
 */
public class Taxi {
	protected int credit;
	protected int status;
	protected int number;
	protected CityMap map;
	protected Point position;
	protected Request seReq;
	protected Point lastpos;
	protected ReadWriteLock lock;
	MyFlag changeflag;

	public Taxi(int num, int size, CityMap mp) {
		/**
		 * @REQUIRES: mp != null, ff != null;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据传入的参数创建Taxi对象
		 */
		Random random = new Random();
		number = num;
		credit = 0;
		status = 2;
		map = mp;
		seReq = null;
		position = new Point(random.nextInt(size), random.nextInt(size));
		lastpos = position;
		lock = new ReentrantReadWriteLock();
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: credit > =0 && status >=0 && number >= 0 && map.repOK() == true && position != null &&
		 *           lastpos != null && changeflag != null && lock != null ==> \result = true;
		 */
		if (credit < 0 || status < 0 || number < 0 || position == null || lastpos == null || changeflag == null
				|| lock == null)
			return false;
		return map.repOK();
	}

	public void setflag(MyFlag flag) {
		changeflag = flag;
	}

	public int getcredit() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = credit
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.readLock().lock();
		try {
			return credit;
		} finally {
			lock.readLock().unlock();
		}
	}

	public Point getposition() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = position
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.readLock().lock();
		try {
			return position;
		} finally {
			lock.readLock().unlock();
		}
	}

	public int getstatus() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = status
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.readLock().lock();
		try {
			return status;
		} finally {
			lock.readLock().unlock();
		}
	}

	public Request getreq() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = seReq
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.readLock().lock();
		try {
			return seReq;
		} finally {
			lock.readLock().unlock();
		}
	}

	public int getnum() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = number
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.readLock().lock();
		try {
			return number;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void setstatus(int a) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: status
		 * 
		 * @EFFECTS: status = a;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.writeLock().lock();
		try {
			status = a;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void setreq(Request re) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: seReq
		 * 
		 * @EFFECTS: seReq == re
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.writeLock().lock();
		try {
			if (re == null) {
				seReq = null;
				return;
			}
			Point sPoint = new Point(re.getsrc().x, re.getsrc().y);
			Point dPoint = new Point(re.getdest().x, re.getdest().y);

			seReq = new Request(sPoint, dPoint, re.gettime(), re.number);
			return;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void addcredit(int a) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: credit
		 * 
		 * @EFFECTS: credit += a;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.writeLock().lock();
		try {
			credit += a;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void randommove() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: position
		 * 
		 * @EFFECTS: 在流量最小的边中随机选择一条走
		 * 
		 */

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
		int min = 0;
		for(int i = 0; i < pp.size(); i++) {
			if (map.getflow(position, pp.get(i)) > map.getflow(position, pp.get(min))) {
				pp.remove(i--);
			}
			if (map.getflow(position, pp.get(i)) < map.getflow(position, pp.get(min))) {
				pp.remove(min);
				min = i--;
			}
		}
		Random random = new Random();
		int r = random.nextInt(pp.size());
		Point nextp = pp.get(r);
		map.addflow(position, nextp);
		map.addflow(nextp, position);
		try {
			if (System.currentTimeMillis() - tt < 200)
				Thread.sleep(200 - (System.currentTimeMillis() - tt));
			boolean re = map.getlight(lastpos, position, nextp);
			if (!re) {
				Thread.sleep(map.lasttime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tt = System.currentTimeMillis();
		map.subflow(position, nextp);
		map.subflow(nextp, position);
		lastpos = position;
		position = nextp;

	}

	public void move(Point dest, long tt, InfoLogger logger, int mode) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: position
		 * 
		 * @EFFECTS: 根据path的路径前进，如果changeflag.getflag() == true，重新规划路径，直到抵达目的地。
		 * 
		 */
		int count = 0;
		Vector<Point> path = map.shortestpath(position, dest);
		for(int i = 0; i < path.size(); i++) {
			if (changeflag.getflag() == true) {
				path = map.shortestpath(position, dest);
				changeflag.setflag(false);
				i = -1;
				continue;
			}
			map.addflow(position, path.get(i));
			map.addflow(path.get(i), position);
			if (path.get(i).equals(position))
				continue;

			Point nextp = path.get(i);
			try {
				if (System.currentTimeMillis() - tt < 200)
					Thread.sleep(200 - (System.currentTimeMillis() - tt));
				boolean re = map.getlight(lastpos, position, nextp);
				if (!re) {
					Thread.sleep(map.lasttime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tt = System.currentTimeMillis();
			map.subflow(position, path.get(i));
			map.subflow(path.get(i), position);
			lastpos = position;
			position = path.get(i);
			logger.taximove(seReq, position, mode, count++, position.equals(dest));
		}
	}

	public String toString() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 返回表示出租车信息的字符串
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.readLock().lock();
		try {
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
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean grebdeal(Vector<Integer> list) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: credit
		 * 
		 * @EFFECTS: 抢单并增加信用度
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.writeLock().lock();
		try {
			for(int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(number))
					return true;
			}
			credit += 1;
			list.addElement(number); // put itself into waiting list
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}
}

/**
 * @OVERVIEW: flag class
 * 
 * @RepInvariant: \result == true
 *
 */
class MyFlag {
	private boolean flag;

	public MyFlag() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: position
		 * 
		 * @EFFECTS: 根据传入的参数构造MyFlag对象
		 *
		 */
		flag = false;
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == true;
		 *
		 */
		return true;
	}

	synchronized public boolean getflag() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: /result = flag
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		return flag;
	}

	synchronized public void setflag(boolean ss) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: flag = ss;
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		flag = ss;
	}
}
