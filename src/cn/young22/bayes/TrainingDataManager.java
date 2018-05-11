package cn.young22.bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 训练数据管理器
 * 
 * @author Young
 *
 */
public class TrainingDataManager {

	private String[] trainingFileClassifications;// 训练语料分类
	private File trainingTextDirecotry; // 训练语料分类目录
	private final static String DEFAULT_PATH = "F:\\testSumResult\\900_5S";

	/**
	 * 训练器构造器
	 */
	public TrainingDataManager() {
		trainingTextDirecotry = new File(DEFAULT_PATH);
		if (!trainingTextDirecotry.isDirectory()) {
			throw new IllegalArgumentException("训练数据文件夹不存在![" + DEFAULT_PATH + "]");
		}
		this.trainingFileClassifications = trainingTextDirecotry.list();
	}

	/**
	 * 返回训练文本类别，这个类别就是训练数据集的分类目录名称
	 */
	public String[] getTrainingClassifications() {
		return this.trainingFileClassifications;
	}

	/**
	 * 根据训练文本返回这个类别下的所有训练文本路径(full path)
	 * 
	 * @param classification
	 *            给定的分类
	 * @return 给定分类下所有文件的路径(full path)
	 */
	public String[] getFilePath(String classification) {
		File classDir = new File(trainingTextDirecotry.getPath() + File.separator + classification);
		String[] ret = classDir.list();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = trainingTextDirecotry.getPath() + File.separator + classification + File.separator + ret[i];
		}
		return ret;
	}

	/**
	 * 返回给定路径的文本文件内容
	 * 
	 * @param filePath
	 *            给定的文本文件路径
	 * @return 文本内容
	 * @throws IOException
	 */
	public static String getText(String filePath) throws IOException {
		InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader reader = new BufferedReader(isReader);
		String aline;
		StringBuilder sb = new StringBuilder();

		while ((aline = reader.readLine()) != null) {
			sb.append(aline + " ");
		}
		isReader.close();
		reader.close();
		return sb.toString();
	}

	/**
	 * 返回训练文本集中所有的文本数目
	 * 
	 * @return 训练文本集中所有的文本数目
	 */
	public int getTrainingFileCount() {
		int ret = 0;
		for (int i = 0; i < trainingFileClassifications.length; i++) {
			ret += getTrainingFileCountOfClassification(trainingFileClassifications[i]);
		}
		return ret;
	}

	/**
	 * 返回训练文本集中在给定分类目录下的训练文本数目
	 * 
	 * @param classification 给定的分类
	 * @return 训练文本集中再给定分类下的训练文本数目
	 */
	public int getTrainingFileCountOfClassification(String classification) {
		File classDir = new File(trainingTextDirecotry.getPath() + File.separator + classification);
		return classDir.list().length;
	}

	/**
	 * 返回給定分类中包含 关键字/词的训练文本的数目
	 * @param classifcation 给定的分类
	 * @param key 给定的关键字/词
	 * @return 给定分类中包含关键字/词的训练文本的数目
	 */
	public int getCountContainKeyOfClassification(String classifcation, String key) {
		int ret = 0;
		try {
			String[] filePath = getFilePath(classifcation);
			for (int j = 0; j < filePath.length; j++) {
				String text = getText(filePath[j]);
				if (text.contains(key)) {
					ret++;
				}
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		} catch (IOException e) {
			Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
		return ret;
	}
	
}
