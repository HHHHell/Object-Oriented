package week_11;

import java.awt.Point;

/**
 * @OVERVIEW: road open or close road request.
 * 
 * @RepInvariant: Request.repOK() == true && act != null ==> \result == true
 */
class RoadRequest extends Request {
	String act;

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: Request.repOK() == true && act != null ==> \result == true
		 */
		boolean s = super.repOK();
		return s && act == null;
	}

	public RoadRequest(Point aPoint, Point bPoint, long tt, String ss) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 创建一个RoadRequest对象
		 */
		super(aPoint, bPoint, tt);
		act = ss;
	}

	public String getact() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = act
		 */
		return act;
	}
}