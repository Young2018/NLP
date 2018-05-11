package cn.young22.bayes;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtFile {
	/**
	 * 读取文件夹中后缀为txt的文件，并返回文件的路径集合
	 * @param file 目录的路径
	 * @return txt文件的路径集合
	 */
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
	
	/**
	 * 读取文件目录，将其中的txt文件集转为String类型的列表
	 * @param dir txt文件集的目录地址
	 * @return txt文件集转为String类型的列表
	 */
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
