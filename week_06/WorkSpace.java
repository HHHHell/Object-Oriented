package week_06;

import java.util.Vector;

public class WorkSpace {
	String path;
	Vector<MyFile> lshot;
	Vector<MyFile> nshot;

	public WorkSpace(String pp) {
		path = pp;
		lshot = new Vector<MyFile>(0, 1);
		nshot = new Vector<MyFile>(0, 1);
	}

	public void scan() {
		lshot = nshot;
		nshot = new Vector<MyFile>(0, 1);
		SafeFile sFile = new SafeFile(path);

		nshot = sFile.scan(nshot);
		MyFile f1 = null, f2 = null;
		synchronized (this) {
			for(int i = 0; i < lshot.size(); i++) {
				f1 = lshot.get(i);
				System.out.println("old:" + f1.toString());
				for(int j = 0; j < nshot.size(); j++) {
					f2 = nshot.get(j);
					System.out.println("new:" + f2.toString());
					if (f1.equals(f2)) {
						lshot.remove(f1);
						nshot.remove(f2);
						i--;
						j--;
						break;
					}
				}
			}
		}
		return;
	}
}
