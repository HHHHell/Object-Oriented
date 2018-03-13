package week_13;

import java.util.Vector;

public class Reqlist {
	/**
	 * @OVERVIEW: elevators
	 * 
	 * @RepInvariant: pos > 0 && state != null && mainreq != null ==> \result == true;
	 */
	public Vector<Request> list;
	public int size;

	Reqlist() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: create a new object of Reqlist
		 */
		list = new Vector<Request>(10, 1);
		size = list.size();
	}
	
	boolean repOK(){
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == true ==> list != null && size == list.size(), otherwise, \result == false;
		 */
		if(list == null  || size != list.size())
			return false;
		return true;
	}

	int getsize() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == size
		 */
		return size;
	}

	Request get(int i) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == list.get(i)
		 */
		return list.get(i);
	}

	void addterm(Request re) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: list.contains(re) == true && size == \old(size) + 1;
		 */
		list.add(re);
		size = list.size();
	}

	void remove(Request re) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: list.contains(re) == false && size == \old(size) - 1;
		 */
		list.remove(re);
		size = list.size();
	}

	void remove(int i) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: list.contains(\old(list).get(i)) == false && size == \old(size) - 1;
		 */
		list.remove(i);
		size = list.size();
	}
}