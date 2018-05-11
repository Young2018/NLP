package cn.young22.bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TextFileReader {

	BufferedReader br;
	
	/**
	 * 读取当前文件中的所有内容，返回一个字符串。
	 * 这个函数的性能要比一行一行的readLine好
	 * @return 当前文件中的所有内容,包括回车(\r)和换行(\n)
	 * @throws IOException 
	 * 
	 */
	public String readAll() throws IOException{
		int bufSize = 4096;
		char[] buffer = new char[bufSize];
		int read, fill = 0;
		while(true){
			read = br.read(buffer, fill, buffer.length - fill);
			if(read == -1){
				break;
			}
			fill += read;
			if(fill >= buffer.length){
				char[] newBuffer = new char[bufSize + buffer.length];
				for(int i = 0; i < buffer.length; i++){
					newBuffer[i] = buffer[i];
				}
				buffer = null;
				buffer = newBuffer;
			}
		}
		return new String(buffer, 0, fill);
	}
	
	public static String readAll(String fileName) throws IOException{
		return readAll(fileName, "UFT-8");
	}
	
	public static String readAll(String fileName, String encode) throws IOException{
		TextFileReader reader = new TextFileReader(fileName, encode);
		String result = reader.readAll();
		reader.close();
		return result;
	}
	
	public TextFileReader(String fileName) throws UnsupportedEncodingException, FileNotFoundException{
		this(new File(fileName), "UTF-8");
	}

	public TextFileReader(String fileName, String encode) throws UnsupportedEncodingException, FileNotFoundException {
		this(new File(fileName), encode);
	}
	
	public TextFileReader(File file) throws UnsupportedEncodingException, FileNotFoundException{
		this(file, "UTF-8");
	}
	
	public TextFileReader(File file, String encode) throws UnsupportedEncodingException, FileNotFoundException{
		br = constructorReader(file, encode);
	}
	
	protected BufferedReader constructorReader(File file, String encode) throws UnsupportedEncodingException, FileNotFoundException{
		return new BufferedReader( new InputStreamReader(new FileInputStream(file), encode));
	}
	
	public void close() throws IOException {
		br.close();
	}
	
}
