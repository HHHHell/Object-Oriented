package week_11;

import java.awt.Point;

/**
 * @OVERVIEW： 存储请求发出地、目的地、发出时间、编号等信息 不变式;
 * 
 * @Repinvariant: src != null && dest != null && time > 0 && number >= 0 ==> \result == true;
 */
public class Request {
	private Point src;
	private Point dest;
	private long time;
	int number;
	boolean visit;
	boolean isfirst;

	static int id = 0;

	public Request(Point aPoint, Point bPoint, long tt) {
		/**
		 * @REQUIRES: aPoint != null, bPoint != null;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据参数创建Request对象
		 */
		src = aPoint;
		dest = bPoint;
		time = (long) Math.ceil(tt / 100.0) * 100;
		visit = false;
		isfirst = true;
		number = -1;
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: src != null && dest != null && time > 0 && number >= 0 ==> \result = true
		 */
		if (src == null || dest == null || time <= 0 || number < 0)
			return false;
		return true;
	}

	public Request(Point aPoint, Point bPoint, long tt, int num) {
		/**
		 * @REQUIRES: aPoint != null, bPoint != null；
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据参数创建Request对象
		 */
		src = aPoint;
		dest = bPoint;
		time = (long) Math.ceil(tt / 100.0) * 100;
		visit = false;
		isfirst = true;
		number = num;
	}

	public Point getsrc() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 返回srcPoint
		 */
		return src;
	}

	public Point getdest() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 返回destPoint
		 */
		return dest;
	}

	public long gettime() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 返回time
		 */
		return time;
	}

	public boolean equals(Request re) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: return \this.equals(re)
		 */
		return this.src.equals(re.src) && this.dest.equals(re.dest) && this.time == re.time;
	}

	public static int mkid() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: id
		 * 
		 * @EFFECTS: 为每个Request对象单独编号
		 */
		return id++;
	}

	public String toString() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: 返回表示该请求信息的字符串
		 */
		String from = "(" + (src.x + 1) + "," + (src.y + 1) + ")";
		String to = "(" + (dest.x + 1) + "," + (dest.y + 1) + ")";

		return time + " Customer Request: form " + from + " to " + to;
	}
}