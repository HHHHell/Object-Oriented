package week_06;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import week_06.Main.EnumTrigger;

public class Main {
	static public enum EnumTask {
		Record_summary, Record_datail, Recover
	};

	static public enum EnumTrigger {
		Renamed, Modified, Path_changed, Size_changed
	};

	static final String summary_path = "Summary.txt";

	public static void main(String[] args) {
		String regex = "IF \\[.*\\] .* THEN .*";
		File sum = new File(summary_path);
		File det = new File("Detail.txt");
		if (sum.exists())
			sum.delete();
		if (det.exists())
			det.delete();
		Vector<Trigger> triggers = new Vector<Trigger>(0, 1);
		Vector<WorkSpace> wSpaces = new Vector<WorkSpace>(0, 1);
		Vector<Summary> summaries = new Vector<Summary>(0, 1);
		InputHandler iHandler = new InputHandler(regex, wSpaces, triggers, summaries);
		Scanner scanner = new Scanner(System.in);
		iHandler.parse(scanner, wSpaces);
		// System.out.println("out:" + triggers.size());
		File wFile = new File(summary_path);
		long time = System.currentTimeMillis();
		if (!wFile.exists()) {
			try {
				wFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < triggers.size(); i++) {
			triggers.get(i).start();
		}
		for(int i = 0; i < summaries.size(); i++) {
			System.out.println(summaries.get(i).toString());
		}

		while (true) {
			while (System.currentTimeMillis() < time + 1000 * 10) {
			}
			try {
				FileWriter fWriter = new FileWriter(wFile);
				BufferedWriter bWriter = new BufferedWriter(fWriter);
				for(int i = 0; i < summaries.size(); i++) {
					if (i == 0) {
						bWriter.write(summaries.get(i).toString());
					}
					bWriter.append(summaries.get(i).toString());
					bWriter.flush();
				}
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class Summary {
	private EnumTrigger tg;
	private MyFile file;
	private int count;

	public Summary(EnumTrigger tt, MyFile sf) {
		tg = tt;
		file = sf;
		count = 1;
	}

	public void record() {
		count++;
	}

	public boolean equals(Summary ss) {
		return ss.tg.equals(this.tg) && ss.file.equals(this.file);
	}

	public String toString() {
		String string = file.toString() + " " + tg.toString() + ": " + count + System.lineSeparator();
		return string;

	}
}
