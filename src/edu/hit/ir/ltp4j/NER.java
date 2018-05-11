package edu.hit.ir.ltp4j;
import java.util.List;

public class NER {
	static
	{
	   if ((System.getProperty("os.name").toLowerCase().startsWith("win")) && 
	       (System.getProperty("os.arch").toLowerCase().endsWith("64"))) {
	       //System.loadLibrary("msvcr120");
	       //System.loadLibrary("msvcp120");
	   System.loadLibrary("vcruntime140");
       System.loadLibrary("msvcp140");	     }
	    else if ((System.getProperty("os.name").toLowerCase().startsWith("win")) && 
	       (System.getProperty("os.arch").toLowerCase().endsWith("86"))) {
	       System.out.println("由于32位系统内存不足，本系统不支持在win7-32bit系统运行！");
	       System.exit(1);
	    }
	     System.loadLibrary("ner");
	     System.loadLibrary("ner_jni");
	   } 
  public static native int create(String modelPath);

  public static native int recognize(List<String> words,
      List<String> postags, List<String> ners);

  public static native void release();

}

