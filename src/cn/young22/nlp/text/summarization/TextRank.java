package cn.young22.nlp.text.summarization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Iterator;

public class TextRank {
	public Document myDoc = new Document();
	public double[][] similarity;
	
	public void Summarize(String args[]) throws IOException{
		/*读取文本文件*/
		String[] singleFile = new String[1];
		singleFile[0] = args[0];
		myDoc.extractScale = Integer.parseInt(args[2]);	// 文摘中文字的最大长度
		myDoc.readFile(singleFile, " ", args[3]);	// 读取待摘要文本文件
		
		/*计算句子的相似矩阵*/
		myDoc.calcTfIdf();
		myDoc.calcSim();
		similarity = new double[myDoc.sNum][myDoc.sNum];
		for(int i = 0; i < myDoc.sNum; ++i){
			double sumISim = 0.0;
			for(int j = 0; j < myDoc.sNum; ++j){
				if(i == j) similarity[i][j] = 0.0;
				else{
					int tmpNum = 0;
					for(Iterator<Integer> iter = myDoc.sVector.get(i).iterator(); iter.hasNext(); ){
						int now = iter.next();
						if(myDoc.sVector.get(j).contains(now)){
							tmpNum++;
						}
					}
					similarity[i][j] = tmpNum / (Math.log(1.0 * myDoc.senLen.get(i)) + Math.log(1.0 * myDoc.senLen.get(j)));
				}
				sumISim += similarity[i][j];
			}
			
			/*按照行归一化句子的相似度*/
			for(int j = 0; j < myDoc.sNum; j++){
				if(sumISim == 0.0){
					similarity[i][j] = 0.0;
				}else{
					similarity[i][j] = similarity[i][j] / sumISim;
				}
			}
		}
		
		// 打印句子之间的相似度
		/*for(double[] ss : similarity){
			for(double s : ss){
				System.out.printf(" %.2f ", s);
			}
			System.out.println();
		}*/

		/*计算句子的TextRank得分*/
		double[] uOld = new double[myDoc.sNum];
		double[] u = new double[myDoc.sNum];
		for(int i = 0; i < myDoc.sNum; i++){
			uOld[i] = 1.0;
			u[i] = 1.0;
		}
		
		double eps = 0.00001, beta = 0.85, minus = 1.0;
		while(minus > eps){
			uOld = u.clone();
			for(int i = 0; i < myDoc.sNum; i++){
				double sumSim = 0.0;
				for(int j = 0; j < myDoc.sNum; j++){
					if(j == i) continue;
					else{
						sumSim = sumSim + similarity[j][i] * uOld[j];
					}
				}
				u[i] = beta * sumSim + (1 - beta);
			}
			minus = 0.0;
			for(int j = 0; j < myDoc.sNum; j++){
				double add = Math.abs(u[j] - uOld[j]);
				minus += add;
			}
		}
		
		/*设置去除相似度很大的句子的控制因子*/
		double factor = 0.9;
		if(Double.parseDouble(args[5]) >= 0){
			factor = Double.parseDouble(args[5]);
		}
		
		/*System.out.println("____________________________");
		for(double tmp : u){
			System.out.println(tmp);
		}
		System.out.println("____________________________");*/
		if(args[4].equals("1")){
			myDoc.pickSentences(u);
		}else if(args[4].equals("2")){
			myDoc.pickSentencesWithWordsLimit(u, factor);
		}else if(args[4].equals("3")){
			myDoc.pickSentencesWithSentencesLimit(u, factor);
		}else if(args[4].equals("4")){
			myDoc.pickSentencesWithRatio(u, factor);
		}
		
		/*System.out.println("----summaryId");
    	for(int index = 0; index < myDoc.summaryId.size(); index++){
    		System.out.println(index + " : " + myDoc.summaryId.get(index) + " " + myDoc.origionalSen.get(index).length());
    	}*/
		
    	/*修改了文摘输出的顺序，让文摘按照原文顺序输出*/
    	Collections.sort(myDoc.summaryId);
    	
    	/* Output the abstract */
    	try{
    		File outfile = new File(args[1]);
    		
    		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(outfile),"utf-8");
    		BufferedWriter writer = new BufferedWriter(write);
    		for (int i : myDoc.summaryId){
                //System.out.println(myDoc.originalSen.get(i));
    			writer.write(myDoc.origionalSen.get(i));
    			writer.write("\n");
            }
    		writer.close();
    	}
    	catch(Exception e){
    		System.out.println("There are errors in the output.");
    		e.printStackTrace();
    	}
	}
}
