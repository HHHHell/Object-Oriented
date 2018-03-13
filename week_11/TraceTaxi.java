package week_11;

import java.awt.Point;
import java.util.Vector;

/**
 * @OVERVIEW: traceable taxi class,
 * 
 * @RepInvariant: super.repOK() == false || infos == null || myMap == null ==> \result == false;
 */
public class TraceTaxi extends Taxi {
	private InfoList infos;
	private CityMap myMap;

	public TraceTaxi(int num, int size, CityMap mp) {
		/**
		 * @REQUIRES: mp != null && ff != null;
		 * 
		 * @MODIFIES: position
		 * 
		 * @EFFECTS: 根据path的路径前进，如果changeflag.getflag() == true，重新规划路径，直到抵达目的地。
		 * 
		 */
		super(num, size, mp);
		infos = new InfoList();
		myMap = new CityMap(mp);
	}

	public boolean repOK() {
		/**
		 * @REQUIRES: mp != null && ff != null;
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: super.repOK() == false || infos == null || myMap == null ==> \result == false
		 * 
		 */
		if (super.repOK() == false || infos == null || myMap == null)
			return false;
		return false;

	}

	@Override
	public void setreq(Request re) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: seReq
		 * 
		 * @EFFECTS: seReq = re
		 * 
		 * @THREAD_REQUIRES:
		 * 
		 * @THREAD_EFFECTS: \locked()
		 */
		lock.writeLock().lock();
		try {
			if (re == null) {
				seReq = null;
				return;
			}
			Point sPoint = new Point(re.getsrc().x, re.getsrc().y);
			Point dPoint = new Point(re.getdest().x, re.getdest().y);

			seReq = new Request(sPoint, dPoint, re.gettime(), re.number);

			TaxiInfo newInfo = new TaxiInfo(seReq, position);
			infos.add(newInfo);
			return;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void move(Point dest, long tt, InfoLogger logger, int mode) {
		/**
		 * @REQUIRES: None
		 * 
		 * @MODIFIES: position
		 * 
		 * @EFFECTS: 根据path的路径前进，如果changeflag.getflag() == true，重新规划路径，直到抵达目的地。
		 * 
		 */
		int count = 0;
		TaxiInfo info = infos.get(infos.size() - 1);

		Vector<Point> path = myMap.shortestpath(position, dest);
		for(int i = 0; i < path.size(); i++) {
			if (changeflag.getflag() == true) {
				path = myMap.shortestpath(position, dest);
				changeflag.setflag(false);
				i = -1;
				continue;
			}
			myMap.addflow(position, path.get(i));
			myMap.addflow(path.get(i), position);
			if (path.get(i).equals(position))
				continue;

			Point nextp = path.get(i);
			try {
				if (System.currentTimeMillis() - tt < 200)
					Thread.sleep(200 - (System.currentTimeMillis() - tt));
				boolean re = myMap.getlight(lastpos, position, nextp);
				if (!re) {
					Thread.sleep(myMap.lasttime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tt = System.currentTimeMillis();
			myMap.subflow(position, path.get(i));
			myMap.subflow(path.get(i), position);
			lastpos = position;
			position = path.get(i);
			if (mode == 0) {
				info.addPiackPath(position);
			} else {
				info.addServePath(position);
			}
			logger.taximove(seReq, position, mode, count++, position.equals(dest));
		}
	}

	public InfoList terms() {
		/**
		 * @REQUIRES: infos != null
		 * 
		 * @MODIFIES: None
		 * 
		 * @EFFECTS: return the list of history served requests
		 * 
		 */
		return infos;
	}
}
