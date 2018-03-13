package week_05;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Elevator_sys {
	static public enum Enumstate {
		UP, DOWN, STILL, NULL, IDLE
	};

	static public enum Enumkind {
		FR, ER
	};

	public static void main(String[] args) {
		long begintime = System.currentTimeMillis();
		Newele[] elelist = new Newele[3];
		Reqlist[] totallist = new Reqlist[4];

		PrintStream ps = null;
		try {
			ps = new PrintStream("result.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.setOut(ps);

		for(int i = 0; i < 4; i++) {
			if (i < 3)
				elelist[i] = new Newele(i + 1);
			totallist[i] = new Reqlist();
		}

		ReqSimulator rSimulator = new ReqSimulator(totallist[0], begintime);
		EleThread e1 = new EleThread(elelist[0], totallist[1], totallist, begintime);
		EleThread e2 = new EleThread(elelist[1], totallist[2], totallist, begintime);
		EleThread e3 = new EleThread(elelist[2], totallist[3], totallist, begintime);
		Scheduler scheduler = new Scheduler(elelist, totallist);

		e1.start();
		e2.start();
		e3.start();
		rSimulator.start();
		scheduler.start();
	}
}