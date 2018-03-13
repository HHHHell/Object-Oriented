package week_06;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import week_06.Main.EnumTask;
import week_06.Main.EnumTrigger;

public class Trigger extends Thread {
	private WorkSpace wSpace;
	private MyFile moFile;
	private EnumTrigger trigger;
	private Vector<Summary> sumlist;
	Vector<EnumTask> tasks;

	public static final String detail_path = "Detail.txt";

	public Trigger(WorkSpace wp, MyFile mFile, EnumTrigger tg, Vector<EnumTask> tks, Vector<Summary> slist) {
		wSpace = wp;
		moFile = mFile;
		trigger = tg;
		tasks = tks;
		sumlist = slist;
	}

	private boolean renamed() {
		MyFile f1 = null, f2 = null;
		boolean result = false;
		for(int i = 0; i < wSpace.lshot.size(); i++) {
			f1 = wSpace.lshot.get(i);
			System.out.println("In renamed old:" + f1.toString());
			if (!f1.isFile())
				continue;
			for(int j = 0; j < wSpace.nshot.size(); j++) {
				f2 = wSpace.nshot.get(j);
				System.out.println("In renamed old:" + f2.toString());
				if (!f2.isFile())
					continue;
				if (f1.getlast() == f2.getlast() && f1.getparent().equals(f2.getparent())
						&& f1.getsize() == f2.getsize() && !f1.getname().equals(f2.getname())) {
					result = true;
					if (moFile.isFile() && f2.isFile()) {
						MyFile newFile = new MyFile(f2.getpath(), f2.getname(), f2.getparent(), f2.getlast(),
								f2.getsize(), f2.isFile());
						moFile = newFile;
					}
					operate(f1, f2, detail_path);
					wSpace.lshot.remove(i--);
					wSpace.nshot.remove(j--);
				}
			}
		}
		return result;
	}

	private boolean pathch() {
		MyFile f1 = null, f2 = null;
		boolean result = false;
		for(int i = 0; i < wSpace.lshot.size(); i++) {
			f1 = wSpace.lshot.get(i);
			if (!f1.isFile())
				continue;
			for(int j = 0; j < wSpace.nshot.size(); j++) {
				f2 = wSpace.nshot.get(j);
				if (!f2.isFile())
					continue;
				if (f1.getlast() == f2.getlast() && f1.getname().equals(f2.getname()) && f1.getsize() == f2.getsize()
						&& !f1.getparent().equals(f2.getparent())) {
					result = true;
					if (moFile.isFile() && f2.isFile()) {
						MyFile newFile = new MyFile(f2.getpath(), f2.getname(), f2.getparent(), f2.getlast(),
								f2.getsize(), f2.isFile());
						moFile = newFile;
					}
					operate(f1, f2, detail_path);
					wSpace.lshot.remove(i--);
					wSpace.nshot.remove(j--);
				}
			}
		}
		return result;
	}

	private boolean sizech() {
		MyFile f1 = null, f2 = null;
		boolean result = false;
		for(int i = 0; i < wSpace.lshot.size(); i++) {
			f1 = wSpace.lshot.get(i);
			for(int j = 0; j < wSpace.nshot.size(); j++) {
				f2 = wSpace.nshot.get(j);
				if (!f2.isFile())
					continue;
				if (f1.getname().equals(f2.getname()) && f1.getparent().equals(f2.getparent())
						&& f1.getsize() != f2.getsize()) {
					result = true;
					if (moFile.isFile() && f2.isFile()) {
						MyFile newFile = new MyFile(f2.getpath(), f2.getname(), f2.getparent(), f2.getlast(),
								f2.getsize(), f2.isFile());
						moFile = newFile;
					}
					operate(f1, f2, detail_path);
				}
			}
		}
		return result;
	}

	private boolean modified() {
		MyFile f1 = null, f2 = null;
		boolean result = false;
		for(int i = 0; i < wSpace.lshot.size(); i++) {
			f1 = wSpace.lshot.get(i);
			if (!f1.isFile())
				continue;
			for(int j = 0; j < wSpace.nshot.size(); j++) {
				f2 = wSpace.nshot.get(j);
				if (!f2.isFile())
					continue;
				if (f1.getname().equals(f2.getname()) && f1.getparent().equals(f2.getparent())
						&& f1.getlast() != f2.getlast()) {
					result = true;
					if (moFile.isFile() && f2.isFile()) {
						MyFile newFile = new MyFile(f2.getpath(), f2.getname(), f2.getparent(), f2.getlast(),
								f2.getsize(), f2.isFile());
						moFile = newFile;
					}
					operate(f1, f2, detail_path);
				}
			}
		}
		return result;
	}

	private void summary() {
		Summary ss = new Summary(trigger, moFile);
		boolean flag = false;
		for(int i = 0; i < sumlist.size(); i++) {
			if (ss.equals(sumlist.get(i))) {
				flag = true;
				sumlist.get(i).record();
			}
		}
		if (!flag) {
			sumlist.add(ss);
		}
	}

	private void detail(MyFile oldfile, MyFile newfile, String Detpath) {
		try {
			File wFile = new File(Detpath);
			if (!wFile.exists()) {
				wFile.createNewFile();
			}
			FileWriter fWriter = new FileWriter(wFile, true);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			String oldstring = "Old File :name = " + oldfile.getname() + " path = " + oldfile.getparent()
					+ " last_modified = " + oldfile.getlast() + " size = " + oldfile.getsize();
			String newstring = "New File :name = " + newfile.getname() + " path = " + newfile.getparent()
					+ " last_modified = " + newfile.getlast() + " size = " + newfile.getsize() + System.lineSeparator();
			bWriter.append(oldstring + System.lineSeparator() + newstring + System.lineSeparator());
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			System.out.println("Detail exception");
		}
	}

	private void recover(MyFile sFile) {
		SafeFile chFile = new SafeFile(sFile.getpath());
		if (trigger.equals(Main.EnumTrigger.Renamed)) {
			chFile.renameto(chFile.getparent() + moFile.getname());
		} else if (trigger.equals(Main.EnumTrigger.Path_changed)) {
			chFile.renameto(moFile.getparent() + chFile.getname());
		}
	}

	private void operate(MyFile oldfile, MyFile newfile, String Detpath) {
		for(int i = 0; i < tasks.size(); i++) {
			switch (tasks.get(i)) {
			case Record_datail:
				detail(oldfile, newfile, Detpath);
				break;
			case Record_summary:
				summary();
				break;
			case Recover:
				recover(newfile);
				break;
			}
		}
	}

	public boolean equals(Trigger tt) {
		if (this.moFile.equals(tt.moFile) && this.trigger.equals(tt.trigger))
			return true;
		return false;
	}

	public void run() {
		while (true) {
			try {
				wSpace.scan();
				// System.out.println("aaa");
				switch (trigger) {
				case Renamed:
					// System.out.println(1);
					if (renamed())
						System.out.println("Renamed");
					break;
				case Modified:
					// System.out.println(2);
					if (modified())
						System.out.println("Modified");
					break;
				case Path_changed:
					// System.out.println(3);
					if (pathch())
						System.out.println("Path_changed");
					break;
				case Size_changed:
					// System.out.println(4);
					if (sizech())
						System.out.println("Size_changed");
					break;
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			catch (Exception e) {
				System.out.println("Trigger exception");
			}
		}
	}
}
