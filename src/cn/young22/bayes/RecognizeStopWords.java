package cn.young22.bayes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RecognizeStopWords {
	
	/**
	 * 将读取的文件转为ArrayList<String>
	 * @param filePath
	 * @return
	 */
	private static ArrayList<String> readTxtFileToArrayList(String filePath) {
		String s = "";
		ArrayList<String> arrayList = new ArrayList<>();
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			
			while((s = br.readLine()) != null){
				arrayList.add(s);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public static boolean isStopWord(String str) {
		ArrayList<String> al = readTxtFileToArrayList("stopWords/stopwords1893.txt");
		return (al.contains(str));
	}
}
