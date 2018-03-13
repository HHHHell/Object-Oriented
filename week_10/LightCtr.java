package week_10;

import java.awt.Point;

/*
 * @OVERVIEW: 控制红绿灯变化线程
 * 不变式： map.repOK == true && gui != null && lasttime >= 50 ==> \result = true;
 */
public class LightCtr extends Thread {
	CityMap map;
	TaxiGUI gui;
	int lasttime;

	/*
	 * @ REQUIRES: mm != null && gg != null
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: 根据传入数据创建一个LightCtr对象
	 */
	public LightCtr(CityMap mm, TaxiGUI gg) {
		map = mm;
		gui = gg;
		lasttime = map.lasttime;
	}
	
	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: map.repOK == true && gui != null && lasttime >= 50 ==> \result
	 * = true;
	 */
	public boolean repOK() {
		if(gui == null && lasttime >= 0)
			return false;
		return map.repOK();
	}

	public void run() {
		try {
			while (true) {
				for(int i = 0; i < 80; i++) {
					for(int j = 0; j < 80; j++) {
						int temp = map.light[i][j];
						Point pp = new Point(i, j);
						gui.SetLightStatus(pp, temp);
					}
				}
				Thread.sleep(lasttime);
				map.turnlight();
			}
		} catch (InterruptedException e) {
			System.out.println("LightCtr Exception: " + e);
			System.exit(0);
		}
	}
}
