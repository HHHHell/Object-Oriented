package week_13;

import java.util.Scanner;

public class Main {
	static public enum Enumstate {
		UP, DOWN, STILL, NULL
	};

	static public enum Enumkind {
		FR, ER
	};

	public static void main(String[] args) {
		try {
			Elevator ele = new Elevator();
			ALS_Scheduler als_Scheduler = new ALS_Scheduler();

			getRequests(als_Scheduler);
			als_Scheduler.command(ele);
		} catch (Exception e) {
			System.exit(0);
		}
	}

	public static void getRequests(Scheduler scheduler) {
		int count = 0;
		int f = 0;
		double t = 0;
		Request req, lastreq = null;
		boolean isfirst = true;
		Scanner s = new Scanner(System.in);
		String tem = s.nextLine();
		String str = tem.replaceAll(" ", "");
		String re = new String(
				"\\(FR,\\+?0{0,14}([1-9]|10),(UP|DOWN),\\+?\\d{1,16}\\)|\\(ER,\\+?0{0,14}([1-9]|10),\\+?\\d{1,16}\\)");

		while (!str.matches("run") && count <= 100000) {
			if (str.matches(re)) {
				String[] strs = str.split("[,\\(\\)]");
				try {
					f = Integer.valueOf(strs[2]);
					t = Double.valueOf(strs[strs.length - 1]);

					if (t > 2147483647L) {
						scheduler.errhandler(0, tem);
						tem = s.nextLine();
						str = tem.replaceAll(" ", "");
						continue;
					}
				} catch (NumberFormatException e) {
					scheduler.errhandler(0, tem);
					tem = s.nextLine();
					str = tem.replaceAll(" ", "");
					continue;
				}

				if (strs[1].equals("FR")) {
					Enumstate e;
					if (strs[3].equals("UP"))
						e = Enumstate.UP;
					else e = Enumstate.DOWN;
					req = new Request(Enumkind.FR, f, e, t);
				} else {
					Enumstate e = Enumstate.NULL;
					req = new Request(Enumkind.ER, f, e, t);
				}

				if (isfirst)
					lastreq = req;

				if (req.validitycheck(isfirst, lastreq)) {
					Reqlist reqlist = scheduler.getreqs();
					reqlist.addterm(req);
					lastreq = req;
					isfirst = false;
					count++;
				} else {
					scheduler.errhandler(0, tem);
					tem = s.nextLine();
					str = tem.replaceAll(" ", "");
					continue;
				}
			} else {
				scheduler.errhandler(0, tem);
				tem = s.nextLine();
				str = tem.replaceAll(" ", "");
				continue;
			}
			tem = s.nextLine();
			str = tem.replaceAll(" ", "");
		}
		s.close();
		if (count == 0)
			System.exit(0);
	}
}
