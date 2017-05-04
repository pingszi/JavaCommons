package indi.pings.android.util.photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 *********************************************************
 ** @desc  ：图片工具类                                            
 ** @author  Pings                                      
 ** @date    2016年9月13日  
 ** @version v1.0                                                                                  
 * *******************************************************
 */
public class PhotoUtil {
	
	//**默认压缩率20%
	public static int DEFAULT_COMPRESS_PERCENT = 20;
	
	/**
	 *********************************************************
	 ** @desc ：将图片压缩至20%后保存到对应的系统文件夹下                                             
	 ** @author Pings                                      
	 ** @date   2016年9月13日                                      
	 ** @param saveDir  保存路径
	 ** @param photo    需要保存的图片路径
	 ** @param fileName 需要保存的名称
	 ** @return                                              
	 * *******************************************************
	 */
	public static File save(String saveDir, String photo, String fileName){
		return save(saveDir, BitmapFactory.decodeFile(photo), fileName, DEFAULT_COMPRESS_PERCENT);
	}
	
	/**
	 *********************************************************
	 *indi.pings.androidUtil
	 *android-util
	 *安卓开发常用工具类（HttpClientUtil、VersionInfoUtil、PhotoUtil等）
	 ** @desc ：将图片压缩至指定比例后保存到对应的系统文件夹下                                             
	 ** @author Pings                                      
	 ** @date   2016年9月13日                                      
	 ** @param saveDir  保存路径
	 ** @param photo    需要保存的图片路径
	 ** @param fileName 需要保存的名称
	 ** @param percent  需要压缩的百分比
	 ** @return                                              
	 * *******************************************************
	 */
	public static File save(String saveDir, String photo, String fileName, int percent){
		return save(saveDir, BitmapFactory.decodeFile(photo), fileName, percent);
	}
	
	/**
	 *********************************************************
	 ** @desc ：将图片压缩至20%后保存到对应的系统文件夹下                                             
	 ** @author Pings                                      
	 ** @date   2016年9月13日                                      
	 ** @param saveDir  保存路径
	 ** @param bit      需要保存的图片
	 ** @param fileName 需要保存的名称
	 ** @return                                              
	 * *******************************************************
	 */
	public static File save(String saveDir, Bitmap bit, String fileName){
		return save(saveDir, bit, fileName, DEFAULT_COMPRESS_PERCENT);
	}

	/**
	 *********************************************************
	 ** @desc ：将图片压缩至指定比例后保存到对应的系统文件夹下                                             
	 ** @author Pings                                      
	 ** @date   2016年9月13日                                      
	 ** @param saveDir  保存路径
	 ** @param bit      需要保存的图片
	 ** @param fileName 需要保存的名称
	 ** @param percent  需要压缩的百分比
	 ** @return                                              
	 * *******************************************************
	 */
	public static File save(String saveDir, Bitmap bit, String fileName, int percent){
		if(!saveDir.endsWith(File.separator))
			saveDir += File.separator;
		
	    File file = new File(saveDir + fileName);
	    FileOutputStream fOut = null;
	    try {
	    	file.createNewFile();
	        fOut = new FileOutputStream(file);  
	        bit.compress(Bitmap.CompressFormat.JPEG, percent, fOut);
	        fOut.flush();  	         
	    } catch (IOException e) {
	        Log.e("PhotoUtil", "error", e);
	        file = null;
	    } finally {
	    	try {
				fOut.close();
			} catch (IOException e) {
				Log.e("PhotoUtil", "error", e);
			} 
	    }
	    
	    return file;
	}
 
}
