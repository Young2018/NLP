package cn.young22.nlp.text.summarization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Document {

	// 文本的基本信息
	public ArrayList<ArrayList<String>> sen = new ArrayList<>();	// 预处理后的句子
	public ArrayList<ArrayList<String>> stemmerSen = new ArrayList<>();
	public ArrayList<String> origionalSen = new ArrayList<>(); // 原文的句子的线性表
	public ArrayList<Integer> senLen = new ArrayList<>(); // 原文每条句子的长度的线性表
	public ArrayList<Integer> wordLen = new ArrayList<>(); // 句子的长度
	public ArrayList<TreeSet<Integer>> sVector = new ArrayList<>();
	public ArrayList<ArrayList<Integer>> sTf = new ArrayList<>(); // 句子的 tf-vector长度，sVector用来存储下标
	public ArrayList<Integer> dTf; // 文档的 tf-vector， dVector用来存储下标
	public TreeSet<Integer> dVector;
	public int totalLen; // 总长度
	public int fNum, sNum = 0, wNum; // fnum-document num; wnum-word num; snum-sentence num
	public int[] tf; // tf of words
	public int[] df; // df of words
	public double[] idf; //idf of words
	public double[][] sim, normalSim;
	public int extractScale; // the maxlen of the summary
	
	ArrayList<Integer> summaryId = new ArrayList<>(); // index of the sentence picked
	HashMap<String, Integer> dic = new HashMap<>(); //map words into numbers
	HashMap<Integer, String> dd = new HashMap<>(); //map numbers into words
	
	/**
	 * 读取文件
	 * @param rFiles	
	 * @param filePath
	 * @param stopWordsPath
	 * @throws IOException
	 */
	public void readFile(String[] rFiles, String filePath, String stopWordsPath) throws IOException{
		int i = 0;
		fNum = 0;
		totalLen = sNum;
		for(String inFile : rFiles){
			fNum++;
			String path;
			if(!filePath.equals(" ")){
				path = filePath + System.getProperty("file.separator") + inFile;
			}else {
				path = inFile;
			}
			
			Tokenizer myToken = new Tokenizer();
			List<String> tmp = new ArrayList<>();
			tmp = myToken.tokenize(path, stopWordsPath);
			
			int len = tmp.size();
			totalLen += len;
			i++;
			sen.addAll(myToken.word);
			senLen.addAll(myToken.senLen);
			origionalSen.addAll(tmp);
		}
		sNum = origionalSen.size();
	}
	
	void calcTfIdf(){
		int i = 0, wLen = 0;
		wNum = 0;
		dic = new HashMap<String, Integer>();
		dTf = new ArrayList<>();
		dVector = new TreeSet<>();
		int[] allTf = new int[100000];
		Arrays.fill(allTf, 0);
		wordLen = new ArrayList<>();
		tf = new int[100000];
		df = new int[100000];
		boolean[] occur = new boolean[100000];
		
		ArrayList<ArrayList<String>> calTfIdfVec = new ArrayList<>();
		calTfIdfVec = sen;
		
		for(ArrayList<String> tmpSen : calTfIdfVec){
			wLen = 0;
			TreeSet<Integer> tmpSet = new TreeSet<>();
			Arrays.fill(tf, 0);
			Arrays.fill(occur, false);
			
			for(String tmpWord : tmpSen){
				wLen++;
				if(dic.get(tmpWord) != null){
					int k = dic.get(tmpWord);
					tmpSet.add(k);
					tf[k]++;
					allTf[k]++;
					if(!occur[k]){
						occur[k] = true;
						df[k]++;
					}
				} else {
					dic.put(tmpWord, wNum);
					dd.put(wNum, tmpWord);
					tf[wNum]++;
					allTf[wNum]++;
					df[wNum]++;
					tmpSet.add(wNum);
					occur[wNum] = true;
					wNum++;
				}
			}
			wordLen.add(wLen);
			ArrayList<Integer> tmpTf = new ArrayList<>();
			for(int j : tmpSet){
				tmpTf.add(tf[j]);
			}
			sTf.add(tmpTf);
			sVector.add(tmpSet);
			i++;
		}
		idf = new double[wNum];
		
		for(i = 0; i < wNum; i++){
			idf[i] = Math.log((1 + sNum) / df[i]);
		}
		for(i = 0; i < wNum; i++){
			if(allTf[i] != 0){
				dVector.add(i);
				dTf.add(allTf[i]);
			}
		}
	}
	
	// 计算两个句子的余弦相似度
	double calcCos(TreeSet<Integer> a1, List<Integer> a2, int lenA, TreeSet<Integer>b1, List<Integer> b2, int lenB){
		int x1 = 0, x2 = 0;
		double l1 = 0, l2 = 0;
		int idA = 0, idB = 0;
		double cos = 0;
		TreeSet<Integer> a = new TreeSet<>();
		TreeSet<Integer> b = new TreeSet<>();
		a.addAll(a1);
		b.addAll(b1);
		
		while(a.size() > 0 && b.size() > 0){
			x1 = a.first();
			x2 = b.first();
			if (x1 == x2){
				l1 += Math.pow((double)a2.get(idA)/(double)lenA*idf[x1], 2);
				l2 += Math.pow((double)b2.get(idB)/(double)lenB*idf[x2], 2);
				cos += Math.pow(idf[x1], 2) * (double)a2.get(idA) / (double)lenA * (double)b2.get(idB) / (double)lenB;
				a.pollFirst();
				idA++;
				b.pollFirst();
				idB++;
			}else if (x1 < x2){
				l1 += Math.pow((double)a2.get(idA)/(double)lenA*idf[x1], 2);
				a.pollFirst();
				idA++;
			}else if (x1 > x2){
				l2 += Math.pow((double)b2.get(idB) / (double)lenB*idf[x2], 2);
				b.pollFirst();
				idB++;
			}
		}
		while(a.size() > 0){
			x1 = a.first();
			l1 += Math.pow((double)a2.get(idA) / (double)lenA * idf[x1], 2);
			a.pollFirst();
			idA++;
		}
		while(b.size() > 0){
			x2 = b.first();
			l2 += Math.pow((double)b2.get(idB) / (double)lenB * idf[x2], 2);
			b.pollFirst();
			idB++;
		}
		if(l1 == 0 || l2 == 0) return 0;
		return cos / Math.pow(l1 * l2, 0.5);
	}

	// 计算两个句子的相似度
	void calcSim(){
		sim = new double[sNum][sNum];
		normalSim = new double[sNum][sNum];
		for(int i = 0; i < sNum; i++){
			double sumISim = 0.0;
			for(int j = 0; j < sNum; j++){
				if(i == j){
					sim[i][j] = 1;
				}
				else if(i > j){
					sim[i][j] = sim[j][i];
				}
				else{
					sim[i][j] = calcCos(sVector.get(i), sTf.get(i), wordLen.get(i), sVector.get(j), sTf.get(j), wordLen.get(j));
				}
				sumISim += sim[i][j];
			}
			for(int j = 0; j < sNum; ++j){
				if(sumISim != 0.0){
					normalSim[i][j] = sim[i][j] / sumISim;
				}else{
					normalSim[i][j] = 0.0;
				}
			}
		}
	}
	
	/**
	 * 生成摘要的句子序号，未用到去除冗余的功能
	 * @param score
	 * @return
	 */
	List<Integer> pickSentences(double[] score){
		summaryId = new ArrayList<>();
		int len = 0;
		boolean[] chosen = new boolean[sNum];
		for(int i = 0; i < sNum; i++){
			chosen[i] = false;
		}
		double alpha = 0;
		
		// 按照文摘的字数长度来压缩
		while(len < extractScale){
			double maxScore = 0;
			int pick = -1;
			for(int i = 0; i < sNum; i++){
				double tmpScore = score[i];
				if(tmpScore / Math.pow(senLen.get(i), alpha) > maxScore && !chosen[i] && len + senLen.get(i) < extractScale && senLen.get(i) >= 5){
					maxScore = tmpScore/Math.pow(senLen.get(i), alpha);
					pick = i;
				}
			}
			if(pick == -1){
				break;
			}
			chosen[pick] = true;
			len += senLen.get(pick);
			summaryId.add(pick);
			if(len > extractScale - 20){
				break;
			}
		}
		return summaryId;
	}
	
	List<Integer> pickSentencesWithRatio(double[] score, double factor){
		summaryId = new ArrayList<>();
		int len = 0;
		int senNum = (int) Math.ceil(origionalSen.size() * extractScale / 100.0);
		boolean[] chosen = new boolean[sNum];
		for(int i = 0; i < sNum; i++){
			chosen[i] = false;
		}
		// senNum为句子数目
		while(len < senNum){
			double maxScore = 0;
			int pick = -1;
			for(int i = 0; i < sNum; i++){
				double tmpScore = score[i];
				for(int j : summaryId){
					if(sim[i][j] > factor){
						tmpScore = 0;
					}
				}
				if(tmpScore > maxScore && !chosen[i] && senLen.get(i) >= 5){
					maxScore = tmpScore;
					pick = i;
				}
			}
			if(pick == -1){
				break;
			}
			chosen[pick] = true;
			len++;
			summaryId.add(pick);
		}
		
		return summaryId;
	}
	
	/**
	 * 按照句子长度来获取摘要
	 * @param score
	 * @param factor
	 * @return
	 */
	List<Integer> pickSentencesWithSentencesLimit(double[] score, double factor){
		summaryId = new ArrayList<>();
		int len = 0;
		boolean[] chosen = new boolean[sNum];
		for(int i = 0; i < sNum; i++){
			chosen[i] = false;
		}
		// 此时的extractScale为句子的数目
		while(len < extractScale){
			double maxScore = 0;
			int pick = -1;
			for(int i = 0; i < sNum; i++){
				double tmpScore = score[i];
				for(int j : summaryId){
					if(sim[i][j] > factor){
						tmpScore = 0;
					}
				}
				if(tmpScore > maxScore && !chosen[i] && senLen.get(i) >= 5){
					maxScore = tmpScore;
					pick = i;
				}
			}
			if(pick == -1){
				break;
			}
			chosen[pick] = true;
			len++;
			summaryId.add(pick);
		}
		
		return summaryId;
	}
	
	/**
	 *  获取最后的结果，添加控制因子來減少冗余
	 * @param score		句子节点的TextRank得分
	 * @param factor	控制因子，用来减少
	 * @param alpha
	 * @return	返回选择句子的序号
	 */
	List<Integer> pickSentencesWithWordsLimit(double[] score, double factor){
		summaryId = new ArrayList<>();
		int len = 0;
		boolean[] chosen = new boolean[sNum];
		for(int i = 0; i < sNum; i++){
			chosen[i] = false;
		}
		while(len < extractScale){
			double maxScore = 0;
			int pick = -1;
			for(int i = 0; i < sNum; i++){
				double tmpScore = score[i];
				// 这里将两个句子相似度大于控制因子的句子去除
				for(int j : summaryId){
					if(sim[i][j] > factor){
						tmpScore = 0;
					}
				}
				if(tmpScore > maxScore && !chosen[i] && len + senLen.get(i) < extractScale && senLen.get(i) >= 5){
					maxScore = tmpScore;
					pick = i;
				}
			}
			if(pick == -1){
				break;
			}
			chosen[pick] = true;
			len += senLen.get(pick);
			summaryId.add(pick);
			if(len > extractScale - 20){
				break;
			}
		}
		return summaryId;
	}
	
}
