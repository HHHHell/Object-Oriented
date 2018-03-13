package week_10;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

/*
 * @OVERVIEW: 提供信息的文件输出方法
 * 不变式： pathname != null;
 */
public class InfoLogger {
	String pathname;

	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: \this
	 * 
	 * @ EFFECTS: pathname = path;
	 */
	public InfoLogger(String path) {
		pathname = path;
	}
	
	/*
	 * @ REQUIRES: None
	 * 
	 * @ MODIFIES: None
	 * 
	 * @ EFFECTS: regex != null && size > 0 && reqlist != null && logger.repOK
	 * == true && map.repOK == true ==> \result = true;
	 *
	 */
	public boolean repOK() {
		if(pathname == null)
			return false;
		return true;
	}
	/*
	 * @ REQUIRES: request != null;
	 * 
	 * @ MODIFIES: LogInfo
	 * 
	 * @ EFFECTS: 在文件中输出读入请求
	 */
	public void readrequest(Request request) {
		try {
			File ff = new File(pathname + "\\" + request.number + ".txt");
			if (!ff.exists())
				ff.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String re = request.toString() + System.lineSeparator();
			bWriter.append(re);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Infologger Exception in ReadRequest: " + e);

		}
	}

	/*
	 * @ REQUIRES: request != null, tt != null, mode == 1 || mode == 0;
	 * 
	 * @ MODIFIES: LogInfo
	 * 
	 * @ EFFECTS: 在文件中输出出租车信息。如果mode == 0，为请求发生时，mode == 1，为抢单窗口关闭时
	 */
	public void printtaix(Request request, Vector<Taxi> tt, int mode) {
		try {
			File ff = new File(pathname + "\\" + request.number + ".txt");
			if (!ff.exists())
				ff.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff, true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String re = "";

			if (mode == 0) {
				re = System.currentTimeMillis() + ": " + "请求发生时，在请求范围内的出租车:  " + System.lineSeparator();
			} else if (mode == 1) {
				re = "抢单时间窗口关闭时，所有已抢单的出租车状态: " + System.lineSeparator();
			}
			bWriter.append(re);

			if (tt == null || tt.size() == 0) {
				if (mode == 0) {
					re = "  没有在范围内的出租车！" + System.lineSeparator();
				} else if (mode == 1) {
					re = "  没有抢单的出租车！" + System.lineSeparator();
				}
				bWriter.append(re);
				bWriter.flush();
				bWriter.close();
				return;
			}

			for(int i = 0; i < tt.size(); i++) {
				re = "\t" + tt.get(i).toString() + System.lineSeparator();
				bWriter.append(re);
			}
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Infologger Exception in ReadRequest: " + e);
		}
	}

	/*
	 * @ REQUIRES: request != null；
	 * 
	 * @ MODIFIES: LogInfo
	 * 
	 * @ EFFECTS: 在文件中输出选择的出则车， 若tt == null， 表示无出租车响应本请求
	 */
	public void chrosetaxi(Request request, Taxi tt) {
		try {
			File ff = new File(pathname + "\\" + request.number + ".txt");
			if (!ff.exists())
				ff.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff, true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			if (tt == null) {
				String re = "没有可以分配的出租车！" + System.lineSeparator();
				bWriter.append(re);
				bWriter.flush();
				bWriter.close();
				return;
			}
			String re = "将请求分配给: Taxi " + tt.getnum() + System.lineSeparator();
			bWriter.append(re);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Infologger Exception in ReadRequest: " + e);
		}
	}

	/*
	 * @ REQUIRES: request != null, pp != null, mode == 0 || mode == 1；
	 * 
	 * @ MODIFIES: LogInfo
	 * 
	 * @ EFFECTS: 在文件中输出出则车云顶信息，若mode == 0， 表示在前往乘客请求发出地， 若mode == 1， 表示送乘客前往目的地
	 */
	public void taximove(Request request, Point pp, int mode, int number, boolean islast) {
		try {
			File ff = new File(pathname + "\\" + request.number + ".txt");
			if (!ff.exists())
				ff.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff, true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String re = "";

			// pick customer
			if (mode == 0 && number == 0) {
				re = "前往请求发出地接乘客,行驶路径如下：" + System.lineSeparator();
			}
			// send customer

			if (mode == 1 && number == 0) {
				re = System.lineSeparator() + "送乘客至目的地,行驶路径如下：" + System.lineSeparator();
			}
			bWriter.append(re);

			re = "(" + (pp.x + 1) + ", " + (pp.y + 1) + ")  ";
			bWriter.append(re);
			if (number % 5 == 0 || islast)
				bWriter.append(System.lineSeparator());
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Infologger Exception in ReadRequest: " + e);
		}
	}
}
