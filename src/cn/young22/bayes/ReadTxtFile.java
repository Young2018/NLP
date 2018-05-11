package cn.young22.bayes;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtFile {
	public static List<String> getFileList(File file) {

		List<String> result = new ArrayList<String>();

		if (!file.isDirectory()) {
			System.out.println(file.getAbsolutePath());
			result.add(file.getAbsolutePath());
		} else {
			File[] directoryList = file.listFiles(new FileFilter() {
				public boolean accept(File file) {
					if (file.isFile() && file.getName().indexOf("txt") > -1) {
						return true;
					} else {
						return false;
					}
				}
			});
			for (int i = 0; i < directoryList.length; i++) {
				result.add(directoryList[i].getPath());
			}
		}
		return result;
	}

	public static List<String> readDirToStrings(String dir) {
		File f = new File(dir);
		List<String> list = new ArrayList<String>();
		list = getFileList(f);

		System.out.println(list.size());

		List<String> result = new ArrayList<>();
		
		for (String l : list) {
			result.add(Util.readFileToString(l));		
		}
		return result;
	}

}
