package week_09;

import java.awt.Point;
import java.util.Vector;

public class Request {
	private Point src;
	private Point dest;
	private long time;
	int number;
	boolean visit;
	boolean isfirst;

	static int id = 0;

	/*
	 * @ REQUIRES: aPoint != null, bPoint != null；
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 根据参数创建Request对象
	 */
	public Request(Point aPoint, Point bPoint, long tt) {
		src = aPoint;
		dest = bPoint;
		time = (long) Math.ceil(tt / 100.0) * 100;
		visit = false;
		isfirst = true;
		number = -1;
	}

	/*
	 * @ REQUIRES: aPoint != null, bPoint != null；
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 根据参数创建Request对象
	 */
	public Request(Point aPoint, Point bPoint, long tt, int num) {
		src = aPoint;
		dest = bPoint;
		time = (long) Math.ceil(tt / 100.0) * 100;
		visit = false;
		isfirst = true;
		number = num;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 返回srcPoint
	 */
	public Point getsrc() {
		return src;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 返回destPoint
	 */
	public Point getdest() {
		return dest;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 返回time
	 */
	public long gettime() {
		return time;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: return \this.equals(re)
	 */
	public boolean equals(Request re) {
		return this.src.equals(re.src) && this.dest.equals(re.dest) && this.time == re.time;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: id
	 * 
	 * @ EFFECTS: 为每个Request对象单独编号
	 */
	public static int mkid() {
		return id++;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: 返回表示该请求信息的字符串
	 */
	public String toString() {
		String from = "(" + (src.x + 1) + "," + (src.y + 1) + ")";
		String to = "(" + (dest.x + 1) + "," + (dest.y + 1) + ")";

		return time + " Customer Request: form " + from + " to " + to;
	}
}

class Reqlist {
	private Vector<Request> requests;
	private int size;

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 创建一个Reqlist对象
	 */
	public Reqlist() {
		requests = new Vector<>(0, 1);
		size = 0;
	}

	/*
	 * @ REQUIRES: i > 0 && i < size
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: \result = requests.get(i);
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: locked()
	 * 
	 * @
	 */
	synchronized public Request get(int i) {
		return requests.get(i);
	}

	/*
	 * @ REQUIRES: re != null
	 * 
	 * @ MODIFIES: requests, size
	 * 
	 * @ EFFECTS: add re into requests
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: locked()
	 * 
	 * @
	 */
	synchronized public void add(Request re) {
		requests.add(re);
		size = requests.size();
	}

	/*
	 * @ REQUIRES: i > 0 && i < size
	 * 
	 * @ MODIFIES: requests, size
	 * 
	 * @ EFFECTS: remove requests[i] from requests
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: locked()
	 * 
	 * @
	 */
	synchronized public void remove(int i) {
		requests.remove(i);
		size = requests.size();
	}

	/*
	 * @ REQUIRES: re exists in requests
	 * 
	 * @ MODIFIES: requests, size
	 * 
	 * @ EFFECTS: remove re from requests
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: locked()
	 * 
	 * @
	 */
	synchronized public void remove(Request re) {
		requests.remove(re);
		size = requests.size();
	}

	/*
	 * @ REQUIRES: re exists in requests
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: \result = size
	 * 
	 * @ THREAD_REQUIRES:
	 * 
	 * @ THREAD_EFFECTS: locked()
	 * 
	 * @
	 */
	synchronized public int getsize() {
		return size;
	}
}

class RoadRequest extends Request {
	String act;

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 创建一个RoadRequest对象
	 */
	public RoadRequest(Point aPoint, Point bPoint, long tt, String ss) {
		super(aPoint, bPoint, tt);
		act = ss;
	}

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: \result = act
	 */
	public String getact() {
		return act;
	}
}