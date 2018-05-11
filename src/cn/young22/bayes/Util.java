package cn.young22.bayes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;


/**
 * 工具类
 * 
 * @author Young
 *
 */
public class Util {

	/**
	 * 求和函数
	 * 
	 * @param array
	 *            待求和的数组
	 * @return 求和的结果
	 */
	public static double sum(double[] array) {
		double total = 0;
		for (double e : array) {
			total += e;
		}
		return total;
	}

	/**
	 * 快速排序算法
	 * 
	 * @param array
	 *            待排序的数组
	 * @param start
	 *            待排数组开始下标
	 * @param end
	 *            待排数组结尾下标
	 */
	public static void quickSort(int[] array, int start, int end) {
		if (start < end) {
			int baseNum = array[start]; // 选择基准值
			int midNum; // 记录中间值
			int i = start;
			int j = end;
			do {
				while ((array[i] < baseNum) && (i < end)) {
					i++;
				}
				while ((array[j] > baseNum) && (j > start)) {
					j--;
				}
				if (i <= j) {
					midNum = array[i];
					array[i] = array[j];
					array[j] = midNum;
					i++;
					j--;
				}
			} while (i <= j);
			if (start < j) {
				quickSort(array, start, j);
			}
			if (end > i) {
				quickSort(array, i, end);
			}
		}
	}
	
	/**
	 * 读取文件，将其中的内容转为字符串
	 * @param filePath 文件路径
	 * @return 文件的字符串
	 */
	public static String readFileToString(String filePath) {
		StringBuilder sb = new StringBuilder();
		String s = "";

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(filePath));

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = sb.toString();

		return str;
	}

	@Test
	/**
	 * 测试从文本中读取文件成字符串
	 */
	public void testReadFileToString() {
		// 从文本文件中读取文章
		String document = Util.readFileToString("testDocument.txt");
		System.out.println(document);
	}

}
