package cn.young22.nlp.text.summarization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hit.ir.ltp4j.Segmentor;

/**
 * 分词类，用来给文本分词
 */
public class Tokenizer {
	public ArrayList<String> passage = new ArrayList<>();
	public ArrayList<Integer> senLen = new ArrayList<>();
	public ArrayList<ArrayList<String>> word = new ArrayList<>();
	HashMap<String, Integer> stopwords = new HashMap<>();
	/**
	 * 读取出停用词，并存储到stopwords中候用
	 * @param stopWordsPath
	 * @throws IOException
	 */
	public void readStopwords(String stopWordsPath) throws IOException {
		File tmpFile = new File(stopWordsPath);
		if(!tmpFile.exists()){
			System.out.println("stopwords file does not exists");
			System.exit(0);
		}
		
		FileReader inFReader = new FileReader(stopWordsPath);
		BufferedReader inBReader = new BufferedReader(inFReader);
		String tmpWord;
		int i = 0;
		while((tmpWord = inBReader.readLine()) != null){
			i++;
			stopwords.put(tmpWord, i);
		}
		inBReader.close();
	}
	
	/**
	 * 判断是否为停用词
	 * @param tmpWord 现在读取的词
	 * @return true 是停用词  false 不是停用词
	 */
	public boolean isStopWords(String tmpWord){
		if(stopwords.get(tmpWord) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 分句及分词
	 * @param inFile	输入的文件
	 * @param stopWordsPath 停用词的路径
	 * @return	以ArrayList<String>的形式返回分词的结果
	 * @throws IOException
	 */
	public ArrayList<String> tokenize(String inFile, String stopWordsPath) throws IOException{
		StringBuffer buffer = new StringBuffer();
		String line;
		if(!stopWordsPath.equals("n") && !stopWordsPath.equals("y")){
			readStopwords(stopWordsPath);
//			System.out.println("hi, it's stopwords");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"));
		line = reader.readLine();
		while (line != null){
			buffer.append(line);
			buffer.append("\n");
			line = reader.readLine();
		}
		reader.close();
		
		// 使用正则表达式，按照,，。？?！!;；来分句
		Pattern pattern = Pattern.compile(".*?[,，。？?！!;；]");
		Matcher matcher = pattern.matcher(buffer);
		while(matcher.find()){
			String sen = matcher.group();
			passage.add(sen);
			senLen.add(sen.length());
			
			ArrayList<String> tmpSentences = new ArrayList<>();
			// 调用LTP的分词包来分词
			Segmentor.segment(sen, tmpSentences);
			word.add(tmpSentences);
		}
		return passage;
	}
}
