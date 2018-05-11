package cn.young22.bayes;

import java.util.HashMap;

import org.junit.Test;

import edu.hit.ir.ltp4j.Segmentor;

public class Tests {

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
	
	@Test
	public void test_20_all(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSum\\testing20\\";
		String resultPath = "20_all_2.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	
	@Test
	public void test_20_5P(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_5P\\";
		String resultPath = "20_5P.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_10P(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_10P\\";
		String resultPath = "20_10P.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_15P(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_15P\\";
		String resultPath = "20_15P.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_5S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_5S\\";
		String resultPath = "20_5S.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_10S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_10S\\";
		String resultPath = "20_10S.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_15S(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_15S\\";
		String resultPath = "20_15S.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_50W(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_50W\\";
		String resultPath = "20_50W.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_100W(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_100W\\";
		String resultPath = "20_100W.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	@Test
	public void test_20_150W(){
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String testingDir = "F:\\testSumResult\\20_150W\\";
		String resultPath = "20_150W.txt";
		
		BayesClassifier.nbClassifierTester(testingDir, resultPath);
	}
	
	
}
