package cn.young22.nlp.tests.bayes;

import java.util.HashMap;

import org.junit.Test;

import cn.young22.bayes.BayesClassifier;
import edu.hit.ir.ltp4j.Segmentor;

public class Tests100 {

	@Test
	public void testHashMap(){
		HashMap<String, Integer> cs = new HashMap<>();
		String[] categories = {"财经", "家居", "教育", "科技", "时尚", "时政", "体育", "娱乐"};
		
		int index = 0;
		for(String c : categories){
			cs.put(c, index++);
		}
		
		System.out.println(cs.get("财经"));
		
		System.out.println(cs);
	}
	
	// 跑过
	@Test
	public void test_100_all(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSum\\testing100\\";
		String resultPath = "100_all.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	// 跑过
	@Test
	public void test_100_5P(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_5P\\";
		String resultPath = "100_5P.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	// 跑过
	@Test
	public void test_100_10P(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_10P\\";
		String resultPath = "100_10P_2.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	// 跑过
	@Test
	public void test_100_15P(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_15P\\";
		String resultPath = "100_15P.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	// 训练900，经摘要过的，测试100，经摘要过的
	@Test
	public void test_train_900_test_100_5S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_5S\\";
		String resultPath = "100_5S.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	// 跑过
	@Test
	public void test_100_5S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_5S\\";
		String resultPath = "100_5S.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	// 跑过
	@Test
	public void test_100_10S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_10S\\";
		String resultPath = "100_10S_2.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	// 跑过
	@Test
	public void test_100_15S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_15S\\";
		String resultPath = "100_15S.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	// 已跑
	@Test
	public void test_100_50W(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_50W\\";
		String resultPath = "100_50W.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	// 跑过
	@Test
	public void test_100_100W(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_100W\\";
		String resultPath = "100_100W.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	// 已跑
	@Test
	public void test_100_150W(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\100_150W\\";
		String resultPath = "100_150W.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
}
