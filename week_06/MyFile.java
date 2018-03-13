package week_06;

public class MyFile {
	private String name;
	private String path;
	private String papth;
	private long lastmodified;
	private long size;
	private boolean isfile;

	/* path must be exist */
	public MyFile(String pp, String na, String ppath, long last, long ss, boolean isf) {
		path = pp;
		name = na;
		papth = ppath;
		lastmodified = last;
		size = ss;
		isfile = isf;
	}

	public MyFile(MyFile mf) {
		this.name = mf.getname();
		this.path = mf.getpath();
		this.papth = mf.getparent();
		this.lastmodified = mf.lastmodified;
		this.size = mf.size;
		this.isfile = mf.isfile;
	}

	synchronized public String getpath() {
		return path;
	}

	synchronized public String getname() {
		return name;
	}

	synchronized public long getlast() {
		return lastmodified;
	}

	synchronized public long getsize() {
		return size;
	}

	synchronized public String getparent() {
		return papth;
	}

	synchronized public boolean isFile() {
		return isfile;
	}

	synchronized public String toString() {
		String string = path + " " + name;
		return string;
	}

	synchronized public boolean equals(MyFile ff) {
		if (this.name.equals(ff.name) && this.path.equals(ff.path) && this.size == ff.size
				&& this.lastmodified == ff.lastmodified)
			return true;
		return false;
	}
}
