package week_05;

import week_05.Elevator_sys.Enumkind;
import week_05.Elevator_sys.Enumstate;

public class InputHandler {
	private String regex;

	public InputHandler(String str) {
		regex = str;
	}

	public Request parse(String str, long now, long begin) {
		if (!str.matches(regex))
			return null;
		str = str.replaceAll("\\(|\\)", "");
		String[] strings = str.split(",");

		Enumkind flag;
		Enumstate dir = Enumstate.NULL;
		long time = now - begin;
		int floor;
		int ele = 0;

		if (strings[0].equals("FR")) {
			flag = Enumkind.FR;
			floor = Integer.valueOf(strings[1]);
			if (strings[2].equals("UP"))
				dir = Enumstate.UP;
			else dir = Enumstate.DOWN;
		} else {
			flag = Enumkind.ER;
			ele = Integer.valueOf(strings[1].replaceAll("#", ""));
			floor = Integer.valueOf(strings[2]);
		}

		Request request = new Request(flag, floor, dir, time, ele);
		return request;
	}
}
