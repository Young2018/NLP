package cn.young22.nlp.tests.summarization;

import java.io.IOException;

import org.junit.Test;

import cn.young22.nlp.text.summarization.TextRankClientSingle;
import edu.hit.ir.ltp4j.Segmentor;

public class TestBatch {
	
	private String file = "F:\\testSum\\training900\\";
	private String outFile = "F:\\testSumResult\\";
	
	/**
	 * TextRank批处理程序，按照给定的单词数目输出摘要
	 * @throws IOException
	 */
	@Test
	public void testTextRankBatchWithWordLimit() throws IOException{
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		
		/** 参数
		 * maxWordNum 文摘中词的个数
		 * stopWordsPath 停用词的路径
		 * isRedundancy 是否去冗余 1 表示不去除相识度很高的句子, 2表示去除相似度很高的句子
		 * factor 控制因子，来去除相似度大于该控制因子的两个句子
		 */
		String 	maxWordNum = "150",
				stopWordsPath ="F:\\testSum\\stopWords\\stopwords1893.txt",
				isRedundancy = "2",
				factor = "0.7";

		String type = "100_150W";
		
		String[] categories = { "财经", 
								"教育", 
								"科技", 
								"时尚", 
								"体育", 
								"时政", 
								"娱乐", 
								"家居" };
		
		for(String c : categories){
			String tmpInFile = file + c;
			String tmpOutFile = outFile + type + "\\" + c;
			
			TextRankClientSingle.textRankSingle(maxWordNum, stopWordsPath, isRedundancy, factor,  tmpInFile, tmpOutFile);
		}
		
		System.out.println("finish");
		
	}
	
	/**
	 * TextRank批处理程序，按照给定的句子数目输出摘要
	 * @throws IOException
	 */
	@Test
	public void testTextRankBatchWithSentenceLimit() throws IOException{
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		
		String 	maxSenLen = "5",
				stopWordsPath ="F:\\testSum\\stopWords\\stopwords1893.txt",
				pickMethod = "3",			
				factor = "0.7";
		
		String type = "900_5S";
		
		String[] categories = { "财经", 
								"教育", 
								"科技", 
								"时尚", 
								"体育", 
								"时政", 
								"娱乐", 
								"家居" };
		
		for(String c : categories){
			String tmpInFile = file + c;
			String tmpOutFile = outFile + type + "\\" + c;
			
			TextRankClientSingle.textRankSingle(maxSenLen, stopWordsPath, pickMethod, factor, tmpInFile, tmpOutFile);
		}
		
		System.out.println("finish");
		
	}

	/**
	 * TextRank批处理程序，按照给定的比率输出摘要
	 * @throws IOException
	 */
	@Test
	public void testTextRankBatchWithRatio() throws IOException{
		if (Segmentor.create("./ltp_data/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		
		String 	ratio = "15",
				stopWordsPath ="F:\\testSum\\stopWords\\stopwords1893.txt",
				pickMethod = "4",			
				factor = "0.7";
		
		String type = "100_15P";
		
		String[] categories = { "财经", 
								"教育", 
								"科技", 
								"时尚", 
								"体育", 
								"时政", 
								"娱乐", 
								"家居" };
		
		for(String c : categories){
			String tmpInFile = file + c;
			String tmpOutFile = outFile + type + "\\" + c;
			
			TextRankClientSingle.textRankSingle(ratio, stopWordsPath, pickMethod, factor, tmpInFile, tmpOutFile);
		}
		
		System.out.println("finish");
		
	}
	
	
}
