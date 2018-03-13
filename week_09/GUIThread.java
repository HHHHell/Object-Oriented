package week_09;

import java.awt.Point;
import java.util.Vector;

public class GUIThread extends Thread {
	private int[][] map;
	Vector<Taxi> taxis;
	Reqlist requests;
	int size;
	TaxiGUI gui;

	public GUIThread(int[][] mm, Vector<Taxi> t, Reqlist r, int s, TaxiGUI gTaxiGUI) {
		map = mm;
		taxis = t;
		requests = r;
		size = s;
		gui = gTaxiGUI;
	}
	
	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: gui
	 * 
	 * @ EFFECTS:  根据出租车和请求状态，更新gui显示信息
	 *
	 * @
	 */
	public void run() {
		try {
			gui.LoadMap(map, size);
			while (true) {
				for(int i = 0; i < taxis.size(); i++) {
					gui.SetTaxiStatus(i, taxis.get(i).getposition(), taxis.get(i).getstatus());
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
			}
		} catch (Exception e) {
			System.out.println("GUIThread Exception: " + e);
			System.exit(0);
		}
	}
}
