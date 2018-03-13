package week_14;

import java.util.Vector;

public class Reqlist {
	/**
	 * @OVERVIEW: requests list
	 * 
	 * @RepInvariant: \result == true ==> list != null && size == list.size(), otherwise, \result == false;
	 */
	private Vector<Request> list;
	private int size;

	Reqlist() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: create a new object of Reqlist
		 */
		
		// <list != null && size == list.size> with <none>
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
		//<\result == size> with <none>
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
		//<\result == list.get(i)> with <none>
		return list.get(i);
	}

	void addterm(Request re) {
		/**
		 * @REQUIRES: \old(list).contains(re) == false;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: list.contains(re) == true && size == \old(size) + 1;
		 */
		//<list.contains(re) == true && size == \old(size) + 1> with <none>
		list.add(re);
		size = list.size();
	}

	void remove(Request re) {
		/**
		 * @REQUIRES:  \old(list).contains(re) == true;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: list.contains(re) == false && size == \old(size) - 1;
		 */
		// <list.contains(re) == false && size == \old(size) - 1> with <none>
		list.remove(re);
		size = list.size();
	}


}