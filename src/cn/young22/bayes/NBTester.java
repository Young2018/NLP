package cn.young22.bayes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class NBTester {

	/**
	 * 返回可能分类数目
	 */
	protected int resultNum = 1;
	
	/**
	 * 分类器
	 */
	protected BayesClassifier classifier;
	
	/**
	 * 类别列表
	 */
	protected final ArrayList<String> categoryList = new ArrayList<>();
	
	/**
	 * 
	 */
	protected int index = 0;
	
	/**
	 * 训练语料路径
	 */
	protected String trainingFolder = null;
	
	/**
	 * 测试语料路径
	 */
	protected String testingFolder = null;
	
	/**
	 * 训练目录编号
	 */
	protected int trainingPathIndex = -1;
	
	/**
	 * 测试目录编号
	 */
	protected int testingPathIndex = -1;
	
	/**
	 * 文件编码
	 */
	protected String encoding = "UTF-8";
	
	/**
	 * 训练文件使用比例 
	 */
	protected double ratio1 = 1.0;
	
	/**
	 * 测试文档使用比例
	 */
	protected double ratio2 = 0.0;
	/**
	 * 后缀过滤
	 */
	protected String suffix = null;
	
	/**
	 * 是否打印细节
	 */
	protected boolean printDetail = false;
	
	/**
	 * 分类编号索引
	 */
	protected HashMap<String, Integer> categoryToInt = new HashMap<>();
	
	/**
	 * 测试器
	 */
	protected Tester tester = null;
	
	public double getPrecision(){
		if(tester == null){
			return -1;
		}
		return tester.calculate().microAverage;
	}
	
	/**
	 * 测试结果类
	 */
	class TestResult{
		int testSize = 0;
		public double microAverage = 0.0;
		public double macroPrecision = 0.0;
		public double macroRecall = 0.0;
		public double macroFMeasure = 0.0;
		
		public TestResult(){};
		
		public TestResult(TestResult t){
			assign(t);
		}
		
		public void assign(TestResult t){
			if(t != null){
				testSize = t.testSize;
				microAverage = t.microAverage;
				macroPrecision = t.macroPrecision;
				macroRecall = t.macroRecall;
				macroFMeasure = t.macroFMeasure;
			}
		}
		public String toString() {
			return "Test set size: " + testSize + "\n"
				+ "[MacroAverage]: " 
				+ " Precision: " + macroPrecision
				+ " Recall: " + macroRecall
				+ " FMeasure: " + macroFMeasure 
				+ "\n"
				+ "[MicroAverage]: " + 	microAverage;
		}
	}
	
	
	protected class Tester{
		private int size;
		public double predict[];
		public double answer[];
		public double correct[];
		
		public Tester(int s){
			size = s;
			predict = new double[s];
			answer = new double[s];
			correct = new double[s];
		}
		
		public double average(double array[]){
			double sum = 0;
			for(int i = 0; i < array.length; i++){
				sum += array[i];
			}
			return sum / array.length;
		}
		
		public TestResult calculate(){
			double precision[] = new double[predict.length];
			double recall[] = new double[predict.length];
			TestResult result = new TestResult();
			
			for(int i = 0; i < predict.length; i++){
				result.testSize += predict[i];
				if(correct[i] != 0){
					precision[i] = correct[i] / predict[i];
					recall[i] = correct[i] / answer[i];
					result.microAverage += correct[i];
				} else {
					precision[i] = 0;
					recall[i] = 0;
				}
			}
			result.macroPrecision = average(precision);
			result.macroRecall = average(recall);
			result.macroFMeasure = 2 * result.macroPrecision * result.macroRecall
								/ (result.macroPrecision + result.macroRecall);
			result.microAverage /= (double) result.testSize;
			return result;
		}
		
		public TestResult calculate(String[] categoryList){
			if(size != categoryList.length){
				System.out.println("The size of category list (" + categoryList.length
						+ ") does NOT match size of this Tester (" + size + ") !!!");
				return null;
			}
			double precision[] = new double[predict.length];
			double recall[] = new double[predict.length];
			TestResult result = new TestResult();
			
			for(int i = 0; i < predict.length; i++){
				result.testSize += predict[i];
				if(correct[i] != 0){
					precision[i] = correct[i] / predict[i];
					recall[i] = correct[i] / answer[i];
					result.microAverage += correct[i];
				} else {
					precision[i] = 0;
					recall[i] = 0;
				}
				System.out.println(categoryList[i] + ": " + "Precision: " + precision[i]
						+ " Recall: " + recall[i] + " FMeasure: " + 2 * precision[i]
						* recall[i] / (precision[i] + recall[i]));
			}
			result.macroPrecision = average(precision);
			result.macroRecall = average(recall);
			result.macroFMeasure = 2 * result.macroPrecision * result.macroRecall
					/ (result.macroPrecision + result.macroRecall);
			result.microAverage /= (double)result.testSize;
			System.out.println(result.toString());
			return result;
		}
		
	}
	
	public BayesClassifier getClassifier(){
		return classifier;
	}
	
	public void setClassifier(BayesClassifier bc){
		classifier = bc;
	}
	
	public String getCategoryName (int id){
		return categoryList.get(id);
	}
	
	public int getCategorySize(){
		return categoryList.size();
	}
	
	/**
	 * 从文件夹中获取分类列表
	 */
	public boolean loadCategoryListFromFolder(String folder){
		File f;
		if(folder == null || !(f = new File(folder)).exists() || !f.isDirectory()){
			return false;
		}
		
		categoryList.clear();
		
		File listFiles[] = f.listFiles();
		for(int i = 0; i < listFiles.length; ++i){
			if(listFiles[i].isDirectory()){
				categoryList.add(listFiles[i].getName());
			}
		}
		categoryToInt.clear();
		System.out.println("_______________\nCategory List:");
		for(int i = 0; i < categoryList.size(); ++i){
			categoryToInt.put(categoryList.get(i), i);
			System.out.println(i + "\t\t" + categoryList.get(i));
		}
		System.out.println("__________________");
		return true;
	}
	
	/**
	 * 给定类别,添加训练文本
	 */
/*	public boolean addTrainingText(String category, String fileName){
		int label = -1;
		if(fileName == null){
			System.err.println("ERROR : AddTrainingText()  filename is NULL !");
			return false;
		}
		if(category == null || !categoryToInt.containsKey(category) || (label = categoryToInt.get(category)) < 0){
			System.err.println("ERROR : AddTrainingText()  Can't find category: " 
					+ category + " "
					+ categoryToInt.keySet().contains(category) + " " 
					+ categoryToInt.toString() + " " 
					+ categoryToInt.get(category));
			return false;
		}
		String content;
		try{
			content = TextFileReader.readAll(fileName, encoding);
			
		}catch(Exception e){
			System.err.println("ERROR : AddTrainingText()  Can't read content from " + fileName);
			return false;
		}
		
		return true;
		
	}
	
	*/
	
	/**
	 * 自动添加训练文件
	 * @param fileName
	 */
	public void addFiles(String fileName){
		if(fileName == null){
			return;
		}
		File file = new File(fileName);
		if(!file.exists()){
			return;
		}
		
		ArrayList<String> filteredName = new ArrayList<>();
		
		if(file.isDirectory()){
			File[] listFiles = file.listFiles();
			for(int i = 0; i < listFiles.length; ++i){
				if(listFiles[i].isDirectory()){
					if(trainingPathIndex >= 0){
						addFiles(listFiles[i].getAbsolutePath());
					} else {
						trainingPathIndex = categoryToInt.get(listFiles[i].getName());
						if(trainingPathIndex < 0){
							System.err.println("Can't map " + listFiles[i].getName());
						} else {
							addFiles(listFiles[i].getAbsolutePath());
						}
						trainingPathIndex = -1;
					}
				} else if (listFiles[i].isFile() && trainingPathIndex >= 0){
					if(suffix == null ||
							(suffix.startsWith("NOT") && !listFiles[i].getName().endsWith(suffix.replace("NOT", ""))) ||
							(!suffix.startsWith("NOT") && listFiles[i].getName().endsWith(suffix))){
						filteredName.add(listFiles[i].getAbsolutePath());
					}
				}
			} 
		}
		else if (file.isFile() && trainingPathIndex >= 0){
			if(suffix == null ||
					(suffix.startsWith("NOT") && !file.getName().endsWith(suffix.replace("NOT", ""))) ||
					(!suffix.startsWith("NOT") && file.getName().endsWith(suffix))){
				filteredName.add(file.getAbsolutePath());
			}
		}
		
		for(int i = 0; i < filteredName.size(); ++i){
			if((double)i / (double)filteredName.size() > ratio1){
				break;
			}
			
		}
	}
	
	
	/**
	 * 对文件进行自动分类测试
	 */
/*	public void testFiles(String fileName){
		if(fileName == null){
			return;
		}
		File file = new File(fileName);
		if(!file.exists()){
			return;
		}
		ArrayList<String> filteredName = new ArrayList<>();
		
		if(file.isDirectory()){
			File[] listFiles = file.listFiles();
			for(int i = 0; i < listFiles.length; ++i){
				if(listFiles[i].isDirectory()){
					if(testingPathIndex >= 0){
						testFiles(listFiles[i].getAbsolutePath());
					}else{
						testingPathIndex = categoryToInt.get(listFiles[i].getName());
						if(testingPathIndex < 0){
							System.err.println("Can't map " + listFiles[i].getName());
						}else{
							testFiles(listFiles[i].getAbsolutePath());
						}
						testingPathIndex = -1;
					}
				}else if(listFiles[i].isFile() && testingPathIndex >= 0){
					if(suffix == null ||
							(suffix.startsWith("NOT") && !listFiles[i].getName().endsWith(suffix.replace("NOT", ""))) ||
							(!suffix.startsWith("NOT") && listFiles[i].getName().endsWith(suffix))){
						filteredName.add(listFiles[i].getAbsolutePath());
					}
				}
			}
		} else if (file.isFile() && testingPathIndex >= 0){
			if (suffix == null || 
					(suffix.startsWith("NOT") && !file.getName().endsWith(suffix.replace("NOT", ""))) ||
					(!suffix.startsWith("NOT") && file.getName().endsWith(suffix)))
				filteredName.add(file.getAbsolutePath());
		}
		
		for(int i = 0; i < filteredName.size(); ++i){
			if((double)i / (double)filteredName.size() < 1 - ratio2){
				continue;
			}
			
			String content = null;
			try{
				content = TextFileReader.readAll(filteredName.get(i), encoding);
			}catch(IOException e){
				System.err.println("ERROR : testfiles()  Can't read content from " + filteredName.get(i));
			}
			ClassifyResult result = classifier.classifyText(content);
			if(result != null){
				tester.predict[result.label]++;
				tester.answer[testingPathIndex]++;
				if(testingPathIndex == result.label){
					if(printDetail){
						System.out.println("Right!" + filteredName.get(i) + " "
								+ categoryList.get(result.label) + " "
								+ categoryList.get(testingPathIndex));
					}
					tester.correct[result.label]++;
				} else if(printDetail){
					System.out.println("Wrong!" + filteredName.get(i) + " "
							+ categoryList.get(result.label) + " "
							+ categoryList.get(testingPathIndex));
				}
			}
			index++;
		}
	}
	*/
	
	/**
	 * 批量的NBTextClassifier
	 */
/*	public void runAsNBTextClassifier(){
		
		if(testingFolder != null){
			tester = new Tester(categoryList.size());
			Long startTime = System.currentTimeMillis();
			testFiles(testingFolder);
			tester.calculate(categoryList.toArray(new String[categoryList.size()]));
			System.out.println("Testing time consumed: " + (double)(System.currentTimeMillis() - startTime) / 1000.0 + "s"); 
		}
	}
	*/
	@Test
	public void testBatchNBTextClassifier(){
		
	}
	
}
