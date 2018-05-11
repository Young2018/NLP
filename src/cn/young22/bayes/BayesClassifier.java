package cn.young22.bayes;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hit.ir.ltp4j.Segmentor;

/**
* NaiveBayes分类器
*/
public class BayesClassifier {
	
	private TrainingDataManager tdm; // 训练数据集管理器
	private static double zoomFactor = 20.0f;
	
	/**
	 * 类别数目
	 */
	private int nClasses;
	/**
	 * 类别标签
	 */
	public ArrayList<Integer> labelIndex = new ArrayList<Integer>();
	/**
	 * 训练集的cache文件,存放在磁盘上
	 */
	public File tsCacheFile;
	/**
	 * 训练集的cache输出流
	 */
	public DataOutputStream tsCache = null;
	
	
	public void init (int nClasses){
		this.nClasses = nClasses;
	}
	
	/**
	 * 默认的构造器,初始化训练集
	 */
	public BayesClassifier(){
		tdm = new TrainingDataManager();
	}
	
	/**
	 * 计算给定的文本属性向量X在给定的分类Cj中的条件概率
	 * <code>ClassConditionalProbability</code>连乘值
	 * @param X 给定的文本属性向量
	 * @param Cj 给定的类别
	 * @return 分类条件概率连乘值,即
	 */
	double calcProd(String[] X, String Cj){
		double probProd = 1.0f;
		
		// 类条件概率连乘
		for(int i = 0; i < X.length; i++){
			String Xi = X[i];
			probProd *= ClassConditionalProbability.calculatePxc(Xi, Cj) * zoomFactor;
		}
		
		// 再乘以先验概率
		probProd *= PriorProbability.calculatePc(Cj);
		return probProd;
	}
	
	/**
	 * 加入一篇训练文档、要求label是相遇总类别数的整数,从0开始
	 * @param text  训练文本
	 * @param label 类别编号
	 * @return 加入是否成功。不成功可能是由于不能在磁盘上创建临时文件
	 */
	public boolean addTrainingText(String text, int label){
		if(label >= nClasses || label < 0){
			return false;
		}
		if(tsCache == null){
			try{
				tsCacheFile = File.createTempFile("tctscache", "data");
				tsCache = new DataOutputStream(
							new BufferedOutputStream(
							new FileOutputStream(tsCacheFile)));
			}catch(IOException e){
				return false;
			}
		}
		if(!labelIndex.contains(label)){
			labelIndex.add(label);
		}
		return true;
	}
	
	
	public String[] DropStopWords(String[] oldWords){
		Vector<String> v1 = new Vector<>();
		for(int i = 0; i < oldWords.length; i++){
			// 不是停用词，加入到新的向量中
			if(RecognizeStopWords.isStopWord(oldWords[i]) == false){
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		return newWords;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ClassifyResult classifyText(String text, String category){
		String[] splitWords = textPreparation(text);
		String[] Classes = tdm.getTrainingClassifications(); // 获得分类
		double probability = 0.0f;
		List<ClassifyResult> crs = new ArrayList<>(); // 分类结果
		for(int i = 0; i < Classes.length; i++){
			String Ci = Classes[i];	//第i个分类
			probability = calcProd(splitWords, Ci);//计算给定的文本属性向量splitWords在给定分类Ci中的分类条件概率
			// 保存分类结果
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;	// 分类
			cr.origionClass = category;
			cr.label = i;
			cr.probability = probability; // 关键字在分类中的条件概率
//			System.out.println(Ci + ":" + probability);
			crs.add(cr);
		}
		
		// 对最后概率的结果进行排序
		Collections.sort(crs, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				ClassifyResult m1 = (ClassifyResult) o1;
				ClassifyResult m2 = (ClassifyResult) o2;
				double result = m1.probability - m2.probability;
				if(result < 0){
					return 1;
				}else{
					return -1;
				}
			}
		});
		
		return crs.get(0);
	}
	
	/**
	 * 文本预处理
	 * @param text
	 * @return
	 */
	private String[] textPreparation(String text) {
		List<String> sentences = new ArrayList<String>();
		List<List<String>> resultStr = new ArrayList<>();
		
		Pattern pattern = Pattern.compile(".*?[,，。？！]");
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()){
			String sen = matcher.group();
			sentences.add(sen);
			ArrayList<String> tmpSen = new ArrayList<>();
			Segmentor.segment(sen, tmpSen);
			resultStr.add(tmpSen);
		}
		
		List<String> tmpText = new ArrayList<>();
		for(List<String> sentence : resultStr){
			tmpText.addAll(sentence);
		}
//		System.out.println(tmpText);
		String[] splitWords = (String[]) tmpText.toArray(new String[tmpText.size()]);
		
		
//		System.out.println(splitWords);
		// 去停用词
		splitWords = DropStopWords(splitWords);
		return splitWords;
	}
	
	public static void main(String[] args) {
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		// 从文本文件中读取文章
//		String document = Util.readFileToString("testDocument.txt");
		
		String testingDir = "F:\\testSum\\rawDataTmp5P\\";
		String resultPath = "5PTest.txt";
		nbClassifierTester(testingDir, resultPath);
	}

	public static void nbClassifierTester(String testingDir, String resultPath) {
		double startTime = System.currentTimeMillis();
		
		HashMap<String, Integer> categoryToID = new HashMap<>();
		HashMap<Integer, String> idToCategory = new HashMap<>();
		String[] categories = {"财经", "家居", "教育", "科技", "时尚", "时政", "体育", "娱乐"};
		
		int index = 0;
		for(String c : categories){
			int tmp = index++;
			categoryToID.put(c, tmp);
			idToCategory.put(tmp, c);
		}
		
		BayesClassifier classifier = new BayesClassifier();//构造Bayes分类器
		
		List<List<ClassifyResult>> resultLists = new ArrayList<>();
		
		for(String c : categories){
			List<ClassifyResult> tmpResultList = new ArrayList<ClassifyResult>();
			String tmpDir = testingDir + c;
			List<String> texts = ReadTxtFile.readDirToStrings(tmpDir);
			int i = 0;
			for(String txt : texts){
				tmpResultList.add(classifier.classifyText(txt, c));
				System.out.println(i++);
			}
			resultLists.add(tmpResultList);
		}
		
//		List<String> tmpTexts = ReadTxtFile.readDirToStrings("F:\\testSum\\rawData\\");
		// print resultLists
		/*for(List<ClassifyResult> tmpResultList : resultLists){
			for(ClassifyResult tmpResult : tmpResultList){
				System.out.print(" " + tmpResult);
			}
			System.out.println();
		}*/
		
		double[][] confusionMatrix = new double[categories.length][categories.length];
		int row = 0;
		for(List<ClassifyResult> tmpRL : resultLists){
			for(ClassifyResult r : tmpRL){
				confusionMatrix[row][categoryToID.get(r.classification)]++;
			}
			row++;
		}
		
		//print confusionMatrix
/*		for(double[] items : confusionMatrix){
			for(double item : items){
				System.out.print(" " + item + " ");
			}
			System.out.println();
		}*/
		
		double[] precision = new double[categories.length];
		double[] recall = new double[categories.length];
		double[] f = new double[categories.length];
		
		double[] rowSumOfCM = new double[categories.length];
		double[] columnSumOfCM = new double[categories.length];
		for(int i = 0; i < confusionMatrix.length; i++){
			for(int j = 0; j < confusionMatrix.length; j++){
				rowSumOfCM[i] += confusionMatrix[i][j];
				columnSumOfCM[i] += confusionMatrix[j][i];
			}
		}
		
		for(int i = 0; i < confusionMatrix.length; i++){
			precision[i] = confusionMatrix[i][i] / rowSumOfCM[i];
			recall[i] = confusionMatrix[i][i] / columnSumOfCM[i];
			f[i] = 2 * precision[i] * recall[i] / (precision[i] + recall[i]);
		}
		
		String resultString = "\t precision" + "\t" + "recall\t\t" + "F-Measure\t\n";
		
		
//		System.out.print(resultString);
		for(int i = 0; i < confusionMatrix.length; i++){
			String tmpString = idToCategory.get(i) + " :" + precision[i] + "\t\t" + recall[i] + "\t\t" +f[i] + "\n";
//			System.out.print(tmpString);
			resultString += tmpString;
		}
		
//		System.out.println("below is resultString");
//		System.out.println(resultString);
		
		resultString += "本次测试用时:" + (System.currentTimeMillis() - startTime) / 1000.0 + "s";
		
		/**
		 * 将结果写入到txt文件中
		 */
		try {
			File outFile = new File(resultPath);
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
			BufferedWriter writer = new BufferedWriter(write);
			write.write(resultString);
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		File resultFile = new File(r);
		
	}
}
