package week_11;

/**
 * @OVREVIEW: iterator interface of historyList
 */
public interface Iterator {
	public boolean hasNext();
	/**
	 * @REQUIRES: None
	 * 
	 * @MODIFIES: None
	 * 
	 * @EFFECTS: has next in list , \result == true, otherwise \result == fasle;
	 */

	public TaxiInfo next();
	/**
	 * @REQUIRES: None
	 * 
	 * @MODIFIES: None
	 * 
	 * @EFFECTS: hasNext == true ==> \result == next item in list, otherwise, \result == null;
	 */

	public boolean hasLast();
	/**
	 * @REQUIRES: None
	 * 
	 * @MODIFIES: None
	 * 
	 * @EFFECTS: has last in list , \result == true, otherwise \result == fasle;
	 */

	public TaxiInfo last();
	/**
	 * @REQUIRES: None
	 * 
	 * @MODIFIES: None
	 * 
	 * @EFFECTS: hasLast == true ==> \result == last item in list, otherwise, \result == null;
	 */

}
