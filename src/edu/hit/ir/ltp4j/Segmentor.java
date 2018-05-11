package edu.hit.ir.ltp4j;
import java.util.List;


public class Segmentor {
  static {
	   if ((System.getProperty("os.name").toLowerCase().startsWith("win")) && 
		       (System.getProperty("os.arch").toLowerCase().endsWith("64"))) {
		       //System.loadLibrary("msvcr120");
		       //System.loadLibrary("msvcp120");
		   System.loadLibrary("vcruntime140");
	       System.loadLibrary("msvcp140");
		     }
		    else if ((System.getProperty("os.name").toLowerCase().startsWith("win")) && 
		       (System.getProperty("os.arch").toLowerCase().endsWith("86"))) {
		       System.out.println("由于32位系统内存不足，本系统不支持在win7-32bit系统运行！");
		       System.exit(1);
		    }
	System.loadLibrary("segmentor");  
    System.loadLibrary("segmentor_jni");
  }
  public static native int create(String modelPath);
  public static native int create(String modelPath,String lexiconPath);
  public static native int segment(String sent,List<String> words);
  public static native void release();
}

