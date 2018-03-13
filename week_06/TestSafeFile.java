package week_06;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestSafeFile {

	public static synchronized boolean creatfile(String path) {
		File ff = new File(path);
		try {
			File parentff = new File(ff.getParent());
			if (!parentff.exists())
				parentff.mkdirs();
			return ff.createNewFile();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	public static synchronized boolean delete(String path) {
		boolean flag = false;
		File ff = new File(path);
		if (ff.exists())
			flag = true;
		else if (ff.isFile()) {
			ff.delete();
			flag = true;
		} else {
			File[] files = ff.listFiles();
			for(int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					files[i].delete();
				} else {
					TestSafeFile.delete(files[i].getPath());
				}
			}
			flag = true;
		}
		return flag;
	}

	public static synchronized boolean append(String path, String content) {
		File ff = new File(path);
		try {
			FileWriter fWriter = new FileWriter(ff, true);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			bWriter.append(content);
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static synchronized boolean rename(String path, String nname) {
		File oFile = new File(path);
		if (!oFile.exists())
			return false;
		File nFile = new File(oFile.getParent() + "\\" + nname);
		if (!nFile.exists())
			return false;
		return oFile.renameTo(nFile);
	}

	public static synchronized boolean changepath(String path, String newpath) {
		File oFile = new File(path);
		if (!oFile.exists())
			return false;
		File nFile = new File(newpath + "\\" + oFile.getName());
		if (!nFile.exists())
			return false;
		return oFile.renameTo(nFile);
	}
}
