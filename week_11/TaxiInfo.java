package week_11;

import java.awt.Point;
import java.util.Vector;

/**
 * @OVERVIEW: taxi infomation for each served request
 *
 * @RepInvariant: request != null && position != null && pickPath != null &&
 *                servePath != null ==> \result == true;
 */
public class TaxiInfo {
	protected Request request;
	protected Point position;
	protected Vector<Point> pickPath;
	protected Vector<Point> servePath;

	public TaxiInfo(Request re, Point pos) {
		/**
		 * @REQUIRES: re != null && pos != null;
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: create a object of TaxiInfo
		 */
		request = re;
		position = pos;
		pickPath = new Vector<>();
		servePath = new Vector<>();
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: request != null && position != null && pickPath != null && servePath != null ==> \result == true;
		 */
		if (request == null || position == null || pickPath == null || servePath == null)
			return false;
		return true;
	}

	public void addPiackPath(Point pp) {
		/**
		 * @REQUIRES: pp != null;
		 * 
		 * @MODIFIES: \this.pickPath
		 * 
		 * @EFFECTS: add pp into pickPath
		 */
		pickPath.add(pp);
	}

	public void addServePath(Point pp) {
		/**
		 * @REQUIRES: pp != null;
		 * 
		 * @MODIFIES: \this.pickPath
		 * 
		 * @EFFECTS: add pp into servePath
		 */
		servePath.add(pp);
	}

	public String toString() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: return a description string of this object
		 */
		String req = "#REQUEST: " + request.toString() + System.lineSeparator();
		String poString = "Position of taxi while greb deal: " + "(" + position.x + ", " + position.y + ")"
				+ System.lineSeparator();

		String ppath = "Path to fetch customer: " + System.lineSeparator();
		for(int i = 0; i < pickPath.size(); i++) {
			Point point = pickPath.get(i);
			String pString = "(" + (point.x + 1) + ", " + (point.y + 1) + ")";
			ppath += pString;
			if (i != pickPath.size()) {
				ppath += ", ";
			}
			if (i % 5 == 0 && i > 0) {
				ppath += System.lineSeparator();
			}
		}
		ppath += System.lineSeparator();

		String spath = "Path to serve customer: " + System.lineSeparator();
		for(int i = 0; i < servePath.size(); i++) {
			Point point = servePath.get(i);
			String pString = "(" + (point.x + 1) + ", " + (point.y + 1) + ")";
			spath += pString;
			if (i != servePath.size()) {
				spath += ", ";
			}
			if (i % 5 == 0 && i > 0) {
				spath += System.lineSeparator();
			}
		}
		spath += System.lineSeparator();

		return req + poString + ppath + spath;
	}
}

/**
 * @OVERVIEW: list of TaxiInfo
 * 
 * @RepInvariant: infos != null && pointer != null ==> \result == true
 */
class InfoList implements Iterator {
	private Vector<TaxiInfo> infos;
	private Integer pointer;

	public InfoList() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: create a new object of Infolist
		 */
		infos = new Vector<>();
		pointer = 0;
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: infos != null && pointer != null ==> \result == true
		 */

		if (infos == null || pointer == null)
			return false;
		return true;
	}

	public void add(TaxiInfo e) {
		/**
		 * @REQUIRES: e != null
		 * 
		 * @MODIFIES: infos
		 * 
		 * @EFFECTS: infos[infos.size - 1] == e;
		 */
		infos.add(e);
	}

	public TaxiInfo get(int i) {
		/**
		 * @REQUIRES: i >= 0 && i < infos.size()
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == infos[i];
		 */
		return infos.get(i);
	}

	public int size() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == infos.size();
		 */
		return infos.size();
	}

	@Override
	public boolean hasNext() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result == infos[pointer + 1].exist
		 */
		if (pointer + 1 < infos.size())
			return true;
		return false;
	}

	@Override
	public TaxiInfo next() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: hasNext() == true ==> \result == infos[pointer + 1], hasNext() == false ==> \result == null;
		 */
		if (hasNext())
			return infos.get(++pointer);
		return null;
	}

	@Override
	public boolean hasLast() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: \result = infos[pointer-1].exist;
		 */
		if (pointer - 1 > 0)
			return true;
		return false;
	}

	@Override
	public TaxiInfo last() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: hasLast() == true ==> \result == infos[pointer -1], hasLast() == false ==> \result == null;
		 */
		if (hasLast())
			return infos.get(--pointer);
		return null;
	}
}