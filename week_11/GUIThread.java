package week_11;

import java.awt.Point;
import java.util.Vector;

/**
 * @OVERVIEW: 控制gui的显示
 * 
 * @RepInvariant: (taxis != null) && (\all 0 <= i < 100, taxis[i] != null) &&
 *                (size > 0) && (gui != null) ==> \result == true
 * 
 */
public class GUIThread extends Thread {
	Vector<Taxi> taxis;
	Reqlist requests;
	int size;
	TaxiGUI gui;

	public GUIThread(int[][] mm, Vector<Taxi> t, Reqlist r, int s, TaxiGUI gTaxiGUI) {
		/**
		 * @REQUIRES: mm != null, t != null, r != null, s > 0, gTaxiGUI != null
		 * 
		 * @MODIFIES: \this
		 * 
		 * @EFFECTS: 根据传入信息，构造一个GUIThread对象
		 *
		 */
		taxis = t;
		requests = r;
		size = s;
		gui = gTaxiGUI;
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: (taxis != null) && (\all 0 <= i < 100, taxis[i] != null) &&
		 *           (size > 0) && (gui != null) ==> \result == true
		 *
		 */
		if (taxis == null)
			return false;
		if (size <= 0 || gui == null)
			return false;
		for(int i = 0; i < 100; i++) {
			if (taxis.get(i) == null)
				return false;
		}
		return true;
	}

	public void run() {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: gui
		 * 
		 * @EFFECTS: 根据出租车和请求状态，更新gui显示信息
		 *
		 */
		try {
			while (true) {
				for(int i = 0; i < taxis.size(); i++) {
					gui.SetTaxiStatus(i, taxis.get(i).getposition(), taxis.get(i).getstatus());
					int type = taxis.get(i) instanceof TraceTaxi ? 1 : 0;
					gui.SetTaxiType(taxis.get(i).getnum(), type);
				}
				for(int i = 0; i < requests.getsize(); i++) {
					Request re = requests.get(i);
					if (re.visit == false) {
						Point sPoint = new Point(re.getsrc().x, re.getsrc().y);
						Point dPoint = new Point(re.getdest().x, re.getdest().y);
						gui.RequestTaxi(sPoint, dPoint);
						re.visit = true;
					}
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			System.out.println("GUIThread Exception: " + e);
			System.exit(0);
		}
	}
}
