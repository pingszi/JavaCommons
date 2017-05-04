package indi.pings.android.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 *********************************************************
 ** @desc  ：网络相关工具                                             
 ** @author  Pings                                      
 ** @date    2016年9月20日  
 ** @version v1.0                                                                                  
 * *******************************************************
 */
public class NetworkUtil {
	
	/**
	 *********************************************************
	 ** @desc ：获取网络信息                                            
	 ** @author Pings                                      
	 ** @date   2016年9月20日                                      
	 ** @param context
	 ** @return                                              
	 * *******************************************************
	 */
	public static NetworkInfo getInfo(Context context) {
		//**获取网络连接信息
	    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return manager.getActiveNetworkInfo();
	}

	/**
	 *********************************************************
	 ** @desc ：检测网络是否连接                                             
	 ** @author Pings                                      
	 ** @date   2016年9月20日                                      
	 ** @param context
	 ** @return                                              
	 * *******************************************************
	 */
	public static boolean isAvailable(Context context) {
		NetworkInfo info = getInfo(context);
	    
	    //**判断网络是否连接
	    if (info != null) {
	        return info.isAvailable();
	    }
	    
	    return false;
	}
	
	/**
	 *********************************************************
	 ** @desc ：获取网络类型                                             
	 ** @author Pings                                      
	 ** @date   2016年9月20日                                      
	 ** @param context
	 ** @return (TYPE_NONE = -1,TYPE_MOBILE = 0,TYPE_WIFI = 1)，参见 ConnectivityManager类定义的网络类型                                      
	 * *******************************************************
	 */
	public static int getType(Context context) {
		NetworkInfo info = getInfo(context);
		
		if (info.isAvailable()) {
			return info.getType();
		}
		
		return -1;
	}
	
	/**
	 *********************************************************
	 ** @desc ：ping百度，测试是否连接外网                                             
	 ** @author Pings                                      
	 ** @date   2016年9月20日                                      
	 ** @return                                              
	 * *******************************************************
	 */
	public static boolean ping(){
		//**ping百度
		return ping("www.baidu.com");
	}
	
	/**
	 *********************************************************
	 ** @desc ：ping指定ip地址，测试是否连接                                             
	 ** @author Pings                                      
	 ** @date   2016年9月20日                                      
	 ** @return                                              
	 * *******************************************************
	 */
	public static boolean ping(String ip){
		int status = -1;
		InputStream input = null;
		BufferedReader in = null;
		InputStreamReader isr = null;
		
		try {
			//**ping网址3次
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
			
			//**读取结果
			input = p.getInputStream();
			isr = new InputStreamReader(input);
			in = new BufferedReader(isr);
			StringBuffer msg = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				msg.append(content);
			}
			Log.d("NetworkUtil ping：" + ip, "content: " + msg.toString());
			
			//**ping的状态
			status = p.waitFor();
			if (status == 0) {
				Log.i("NetworkUtil ping：" + ip, "result: success");
			} else {
				Log.i("NetworkUtil ping：" + ip, "result: failed");
			}
			
		} catch (Exception e) {
			Log.e("NetworkUtil ping：" + ip, "failed", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return (status == 0);
	}
}