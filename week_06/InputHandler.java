package week_06;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import week_06.Main.EnumTask;
import week_06.Main.EnumTrigger;

public class InputHandler {
	private String regex;
	private Vector<WorkSpace> wSpaces;
	private Vector<Trigger> triggers;
	private Vector<Summary> sumlist;

	public InputHandler(String reg, Vector<WorkSpace> wps, Vector<Trigger> tgs, Vector<Summary> slist) {
		regex = reg;
		wSpaces = wps;
		triggers = tgs;
		sumlist = slist;
	}

	/*
	 * IF [C:\Users] renamed THEN recover IF [C:\Users] Renamed THEN Recover
	 * regex = "IF \\[.*\\] .* THEN .*"
	 */
	public void parse(Scanner scanner, Vector<WorkSpace> threads) {
		ErrHandler errHandler = new ErrHandler("InputHandler-parse");
		int wcount = 0;
		String strline = scanner.nextLine();
		while (!strline.equals("end")) {
			if (strline.matches(regex)) {
				String path = getpath(strline);
				EnumTrigger trigger = gettrigger(strline);
				EnumTask task = gettask(strline);
				// System.out.println(path + " " + trigger.toString() + " " +
				// task.toString());
				if (path == null) {
					errHandler.err(0);
					strline = scanner.nextLine();
					continue;
				}

				if (task == null || trigger == null) {
					errHandler.err(0);
					strline = scanner.nextLine();
					continue;
				}
				if (task.equals(EnumTask.Recover)
						&& (trigger.equals(EnumTrigger.Modified) || trigger.equals(EnumTrigger.Size_changed))) {
					errHandler.err(0);
					strline = scanner.nextLine();
					continue;
				}

				SafeFile ff = new SafeFile(path);
				MyFile mFile = new MyFile(path, ff.getname(), ff.getparent(), ff.getlast(), ff.getsize(), ff.isfile());
				Vector<EnumTask> tasks = new Vector<EnumTask>(1, 1);
				tasks.add(task);
				WorkSpace wp = null;
				if (ff.isdirectory()) {
					wp = new WorkSpace(path);
				} else wp = new WorkSpace(ff.getparent());
				Trigger tTrigger = new Trigger(wp, mFile, trigger, tasks, sumlist);
				// System.out.println(tTrigger.toString());
				// System.out.println(wp.toString());
				boolean wflag = false;
				boolean tflag = false;
				for(int i = 0; i < wSpaces.size() || i < triggers.size(); i++) {
					if (i < wSpaces.size()) {
						if (wSpaces.get(i).path.equals(wp.path))
							wflag = true;
					}
					if (i < triggers.size()) {
						if (triggers.get(i).equals(tTrigger)) {
							tflag = true;
							Trigger tem = triggers.get(i);
							boolean temflag = false;
							for(int j = 0; j < tem.tasks.size(); j++) {
								if (tem.tasks.get(j).equals(task)) {
									temflag = true;
									break;
								}
							}
							if (!temflag) {
								tem.tasks.add(task);
							}
						}
					}
				}
				if (!wflag) {
					if (wcount < 8) {
						wSpaces.add(wp);
						wcount++;
					} else {
						errHandler.err(4);
						strline = scanner.nextLine();
						continue;
					}
				}
				if (!tflag) {
					triggers.add(tTrigger);
				}
				strline = scanner.nextLine();
			} else {
				errHandler.err(0);
				strline = scanner.nextLine();
				continue;
			}
		}

	}

	private String getpath(String strline) {
		String[] strings = strline.split("\\[|\\]");
		String path = strings[1];
		File ff = new File(path);
		if (!ff.exists())
			return null;
		return path;
	}

	private EnumTrigger gettrigger(String strline) {
		EnumTrigger trigger = null;
		String[] strs = strline.split("\\[|\\]");
		String[] strings = strs[2].split(" ");
		for(int i = 0; i < strings.length; i++) {
			switch (strings[i]) {
			case "renamed":
			case "Renamed":
				trigger = EnumTrigger.Renamed;
				break;
			case "modified":
			case "Modified":
				trigger = EnumTrigger.Modified;
				break;
			case "path_changed":
			case "Path_changed":
				trigger = EnumTrigger.Path_changed;
				break;
			case "size_changed":
			case "Size_changed":
				trigger = EnumTrigger.Size_changed;
				break;
			default:
				continue;
			}
		}
		return trigger;
	}

	private EnumTask gettask(String strline) {
		EnumTask task = null;
		String[] strs = strline.split("\\[|\\]");
		String[] strings = strs[2].split(" ");
		for(int i = 0; i < strings.length; i++) {
			switch (strings[i]) {
			case "record_summary":
			case "Record_summary":
				task = EnumTask.Record_summary;
				break;
			case "record_detail":
			case "Record_detail":
				task = EnumTask.Record_datail;
				break;
			case "recover":
			case "Recover":
				task = EnumTask.Recover;
				break;
			default:
				continue;
			}
		}
		return task;
	}
}

class ErrHandler {
	public ErrHandler(String sp) {
	}

	public ErrHandler() {
	}

	public void err(int code) {
		switch (code) {
		case 0:
			System.out.println("Invalid input!");
			break;
		case 1:
			System.out.println("File directory is not exist!");
			break;
		case 2:
			System.out.println("Wrong triggers or oprations!");
			break;
		case 3:
			System.out.println("Same instructions!");
			break;
		case 4:
			System.out.println("Too many workspaces!");
		}
	}
}
