package week_07;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class InfoLogger {
	String pathname;

	public InfoLogger(String path) {
		pathname = path;
	}

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

	public void printtaix(Request request, Vector<Taxi> tt, int mode) {
		try {
			File ff = new File(pathname + "\\" + request.number + ".txt");
			if (!ff.exists())
				ff.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff, true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String re = "";

			if (mode == 0) {
				re = "请求发生时，在请求范围内的出租车:  " + System.lineSeparator();
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

	public void taximove(Request request, Vector<Point> path, int mode) {
		try {
			File ff = new File(pathname + "\\" + request.number + ".txt");
			if (!ff.exists())
				ff.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ff, true), "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(writer);
			String re = "";

			// pick customer
			if (mode == 1) {
				re = "前往请求发出接乘客,行驶距离为：" + (path.size() - 1) + ",行驶路径如下：" + System.lineSeparator();
			}
			// send customer
			if (mode == 0) {
				re = "送乘客至目的地,行驶距离为：" + (path.size() - 1) + "，行驶路径如下：" + System.lineSeparator();
			}
			bWriter.append(re);

			int count = 0;
			for(int i = 0; i < path.size(); i++) {
				Point point = path.get(i);
				re = "(" + (point.x + 1) + ", " + (point.y + 1) + ")  ";
				bWriter.append(re);
				count++;
				if (count > 5) {
					bWriter.append(System.lineSeparator());
					count = 0;
				}
			}
			bWriter.append(System.lineSeparator());

			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Infologger Exception in ReadRequest: " + e);
		}
	}
}
