package week_11;

import java.util.Vector;

/**
 * @OVERVIEW: Thread safe list of requests
 * 
 * @Repinvariant: requests != null ==> \result = true, otherwise, \result = false;
 * 
 */
public class Reqlist {
	private Vector<Request> requests;
	private int size;

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: requests != null ==> \result = true, otherwise, \result = false;
		 * 
		 */
		if (requests == null)
			return false;
		return true;
	}

	public Reqlist() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 创建一个Reqlist对象
		 * 
		 */
		requests = new Vector<>(0, 1);
		size = 0;
	}

	synchronized public Request get(int i) {
		/**
		 * @REQUIRES: i > 0 && i < size
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = requests.get(i);
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: locked()
		 * 
		 */
		return requests.get(i);
	}

	synchronized public void add(Request re) {
		/**
		 * @REQUIRES: re != null
		 * 
		 * @MODIFIES: requests, size
		 * 
		 * @EFFECTS: add re into requests
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: locked()
		 * 
		 */
		requests.add(re);
		size = requests.size();
	}

	synchronized public void remove(int i) {
		/**
		 * @REQUIRES: i > 0 && i < size
		 * 
		 * @MODIFIES: requests, size
		 * 
		 * @EFFECTS: remove requests[i] from requests
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: locked()
		 * 
		 */
		requests.remove(i);
		size = requests.size();
	}

	synchronized public void remove(Request re) {
		/**
		 * @REQUIRES: re exists in requests
		 * 
		 * @MODIFIES: requests, size
		 * 
		 * @EFFECTS: remove re from requests
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: locked()
		 * 
		 */
		requests.remove(re);
		size = requests.size();
	}

	synchronized public int getsize() {
		/**
		 * @REQUIRES: re exists in requests
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = size
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: locked()
		 * 
		 */
		return size;
	}
}
