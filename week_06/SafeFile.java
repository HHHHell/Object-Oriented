package week_06;

import java.io.File;
import java.util.Vector;

public class SafeFile {
	private File file;

	public SafeFile(String path) {
		file = new File(path);
	}

	public SafeFile(File ff) {
		file = ff;
	}

	synchronized public String getname() {
		return file.getName();
	}

	synchronized public long getlast() {
		return file.lastModified();
	}

	synchronized public String getpath() {
		return file.getAbsolutePath();
	}

	synchronized public long getsize() {
		if (file.isDirectory()) {
			long size = 0;
			File[] cFiles = file.listFiles();
			for(int i = 0; i < cFiles.length; i++) {
				if (cFiles[i].isFile())
					size += cFiles[i].length();
			}
			return size;
		}
		return file.length();
	}

	synchronized public String getparent() {
		String ppString = file.getParent();
		return ppString;
	}

	synchronized public boolean equals(SafeFile sFile) {
		return this.file.equals(sFile.file);
	}

	synchronized public boolean isdirectory() {
		return file.isDirectory();
	}

	synchronized public boolean isfile() {
		return file.isFile();
	}

	synchronized public boolean renameto(String path) {
		File sf = new File(path);
		return file.renameTo(sf);
	}

	synchronized public String toString() {
		return file.getAbsolutePath();
	}

	synchronized public Vector<SafeFile> list() {
		File[] files = file.listFiles();
		Vector<SafeFile> sfiles = new Vector<SafeFile>(0, 1);
		for(int i = 0; i < files.length; i++) {
			sfiles.add(new SafeFile(files[i]));
		}
		return sfiles;
	}

	synchronized public Vector<MyFile> scan(Vector<MyFile> nshot) {
		Vector<SafeFile> sFiles = list();
		for(int i = 0; i < sFiles.size(); i++) {
			// System.out.println(sFiles.get(i).toString());
			MyFile mfile = new MyFile(sFiles.get(i).getpath(), sFiles.get(i).getname(), sFiles.get(i).getparent(),
					sFiles.get(i).getlast(), sFiles.get(i).getsize(), sFiles.get(i).isfile());
			nshot.add(mfile);
			if (sFiles.get(i).isdirectory()) {
				nshot = sFiles.get(i).scan(nshot);
			}
		}
		return nshot;
	}
}
