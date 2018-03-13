package week_05;

import java.util.Scanner;

public class ReqSimulator extends Thread {
	Reqlist queue;
	long btime;

	public ReqSimulator(Reqlist mlist, long time) {
		queue = mlist;
		btime = time;
	}

	// "\\(FR,\\+?0*([1-9]|1[0-9]|20),(UP|DOWN)\\)|\\(ER,#[1-3],\\+?0*([1-9]|1[0-9]|20)\\)"

	public void run() {
		String regex = "\\(FR,\\+?0*([1-9]|1[0-9]|20),(UP|DOWN)\\)|\\(ER,#\\+?0*[1-3],\\+?0*([1-9]|1[0-9]|20)\\)";
		InputHandler handler = new InputHandler(regex);

		Scanner scanner = new Scanner(System.in);
		long ntime = 0;
		Request request;

		try {
			while (scanner.hasNextLine()) {
				String strline = scanner.nextLine();
				ntime = System.currentTimeMillis();
				long time = ntime - btime;
				String[] strings = strline.split(";");

				int count = 0;
				for(int i = 0; i < strings.length; i++) {
					if (count > 9) {
						String ss = "";
						for(int j = i; j < strings.length; j++) {
							ss += (";" + strings[j]);
						}
						System.out.println(System.currentTimeMillis() + ":" + "INVALID [" + ss + "," + time / 1000 + "."
								+ (time % 1000) / 100 + "]");
						break;
					}
					String fsString = strings[i].replaceAll(" ", "");
					if ((request = handler.parse(fsString, ntime, btime)) != null && request.validitycheck()) {
						queue.addterm(request);
						count++;
						continue;
					}
					System.out.println(System.currentTimeMillis() + ":" + "INVALID [" + strings[i] + "," + time / 1000
							+ "." + (time % 1000) / 100 + "]");
				}
			}
		} catch (Exception e) {
			System.out.println("ReqSimulator:" + e);
		}

		scanner.close();
	}
}
