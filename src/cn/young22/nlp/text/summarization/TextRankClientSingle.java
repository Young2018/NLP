package cn.young22.nlp.text.summarization;

import java.io.File;
import java.io.IOException;

import edu.hit.ir.ltp4j.Segmentor;

public class TextRankClientSingle {

	public static void main(String args[]) throws IOException{
		
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		
		/** 参数
		 * maxWordNum 文摘中词的个数
		 * stopWordsPath 停用词的路径
		 * isRedundancy  是否去冗余
		 * 				 1 表示不去除相识度很高的句子,
		 * 				 2 表示去除相似度很高的句子并按字数去抽取句子
		 * 				 3 表示去除相似度很高的句子并按照句子数目来抽取句子
		 * 				 4 表示去除相似度很高的句子并按照压缩率来抽取句子
		 * factor 控制因子，来去除相似度大于该控制因子的两个句子
		 */
		String 	maxWordNum = "100",
				stopWordsPath ="F:\\testSum\\stopWords\\stopwords1893.txt",
				pickMethod = "2",
				factor = "0.7";
		
		String file = "F:\\testSum\\rawData\\体育";
		String outFile = "F:\\testSumResult";
		
		String[] categories = { "财经", 
								"教育", 
								"科技", 
								"时尚", 
								"体育", 
								"时政", 
								"娱乐", 
								"家居" };
		
//		for(String c : categories){
//			String tmpFile = file + c;
//			System.out.println(tmpFile);
//		}
//		
		textRankSingle(maxWordNum, stopWordsPath, pickMethod, factor, file, outFile);
		
		System.out.println("finish");
	}

	public static void textRankSingle(String maxWordNum, String stopWordsPath, String pickMethod, String factor,
			 String file, String outFile) throws IOException {
		String[] arg;
		File dir = new File(file);
		File[] files = dir.listFiles();
		
		System.out.println("AA" + files.length);
		
		File outDir = new File(outFile);
		// 若给的目录不存在，在创建该目录
		if(!outDir.exists()){
			boolean file_true = outDir.mkdir();
			if(!file_true){
				System.out.println("No valid dir!");
			}
		}
		File outF = new File(outFile);
		for(File f : files){
			TextRank textRank = new TextRank();
			arg = new String[10];
			arg[0] = file + System.getProperty("file.separator") + f.getName();
			// 若给的输出目录不存在，则创建该目录
			if(!outF.exists()){
				outF.mkdirs();
			}
			arg[1] = outFile + System.getProperty("file.separator") + f.getName().replace("UTF-8", "");
			arg[2] = maxWordNum;
			arg[3] = stopWordsPath;
			arg[4] = pickMethod;
			arg[5] = factor;
			
			textRank.Summarize(arg);
		}
	}
	
	
	private static void textRankBatch(String maxWordNum, String stopWordsPath, String pickMethod, String factor,
			String file, String outFile) throws IOException{
		
		textRankSingle(maxWordNum, stopWordsPath, pickMethod, factor, file, outFile);
	}
	
}
