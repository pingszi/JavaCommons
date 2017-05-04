package indi.pings.android.util.net;

import indi.pings.android.util.common.Constant;
import indi.pings.android.util.listener.HttpCallbackListener;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

/**
 * Httpclient工具类
 * @author ping
 * @date 2015年10月1日
 * @version V1.1
 * 
 * @update
 * V1.1 添加getHttpsessionId方法 pings 2016-09-22  
 */
public class HttpClientUtil {
	
	public static HttpClient client;
	public static ExecutorService threadPool = Executors.newFixedThreadPool(3);
	private static String TAG = "HttpClientUtil";
	
	/**
	 * @Description: 返回线程安全的HttpClient
	 * @author ping
	 * @date 2016年3月22日
	 * @return
	 */
	public static synchronized HttpClient getHttpClient(){
		if(client != null)
			return client;
		
        HttpParams params = new BasicHttpParams(); 
        //**设置基本参数  
        HttpProtocolParams.setVersion(params, Constant.HTTP_PROTOCOL_VERSION);  
        HttpProtocolParams.setContentCharset(params, Constant.ENCODING);  
        HttpProtocolParams.setUseExpectContinue(params, true);   
        //**从连接池中取连接的超时时间
        ConnManagerParams.setTimeout(params, Constant.TIME_OUT);  
        //**每台主机最多连接数 
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(Constant.DEFAULT_HOST_CONNECTIONS));    
        //**连接池中的最多连接总数 
        ConnManagerParams.setMaxTotalConnections(params, Constant.DEFAULT_MAX_CONNECTIONS); 
        //**连接超时 
        HttpConnectionParams.setConnectionTimeout(params, Constant.CONNECTION_TIME_OUT);  
        //**请求超时 
        HttpConnectionParams.setSoTimeout(params, Constant.SO_TIME_OUT);  
        //**设置HttpClient支持http/https
        SchemeRegistry schReg = new SchemeRegistry();  
        schReg.register(new Scheme(Constant.HTTP_PROTOCOL, PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme(Constant.HTTPS_PROTOCOL, SSLSocketFactory.getSocketFactory(), 443)); 
        //**使用线程安全的连接管理来创建HttpClient  
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);  
        return new DefaultHttpClient(conMgr, params);  
	}
	
	/**
	 *********************************************************
	 ** @desc ：返回httpsessionId                                             
	 ** @author Pings                                      
	 ** @date   2016年9月22日                                      
	 ** @return                                              
	 * *******************************************************
	 */
	public static String getHttpsessionId(){
		DefaultHttpClient client = (DefaultHttpClient) getHttpClient();
		List<Cookie> cookies = client.getCookieStore().getCookies();
		String httpsessionId = "";
		
		for(Cookie cookie : cookies) {
			if("JSESSIONID".equalsIgnoreCase(cookie.getName())){
				httpsessionId = cookie.getValue();
				break;
			}
		}
		
		Log.d(TAG,  "httpsessionId：" + httpsessionId);
		return httpsessionId;
	}
	
	/**
	 * @Description: 成功调用回调函数
	 * @author ping
	 * @date 2016年3月18日
	 * @param listener
	 * @param response
	 * @throws Exception
	 */
	private static <T> void finishCallBack(final HttpCallbackListener<T, Exception> listener,  HttpResponse response) throws Exception{
		T rsp = listener.parseResponse(response);
		Log.i(TAG, rsp.toString());
		listener.onFinish(rsp);
		
		if(rsp instanceof InputStream)
			((InputStream) rsp).close();
	}
	
	/**
	 * @Description: 失败调用回调函数
	 * @author ping
	 * @date 2016年3月18日
	 * @param listener
	 * @param e
	 */
	private static void errorCallBack(final HttpCallbackListener<?, Exception> listener,  Exception e){
		Log.e(TAG, "error", e);
		listener.onError(e);
	}
	
	/**
	 * get提交请求
	 * @author ping
	 * @date 2015年10月1日
	 * @param url 访问地址
	 * @param listener 回调函数
	 */
	public static void get(final String url, final HttpCallbackListener<?, Exception> listener){
		//**创建HttpGet对象
		final HttpGet get = new HttpGet(url);
		
		threadPool.execute(new Runnable(){

			@Override
			public void run() {
				try{
					//**创建httpClient对象
					client = getHttpClient();
			        //**执行请求
			        HttpResponse response = client.execute(get);
			        //**请求成功，读取返回参数
			        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			        	finishCallBack(listener, response);
			        }
			        else{
			        	get.abort();
			        	Log.d(TAG,  "返回数据不是正常状态：" + url);
			        }
			        
				}catch(Exception e){
					get.abort();
					errorCallBack(listener, e);
				}
			}
			
		});
	}
	
	/**
	 * post提交请求
	 * @author ping
	 * @date 2015年10月1日
	 * @param url 访问地址
	 * @param data 参数
	 * @param listener 回调函数
	 */
	public static void post(final String url, final List<NameValuePair> data, final HttpCallbackListener<?, Exception> listener){
		//**创建httpPost对象
		final HttpPost post = new HttpPost(url);
		
		threadPool.execute(new Runnable(){

			@Override
			public void run() {
				try{
					//**创建httpClient对象
					client = getHttpClient();
					//**设置参数
					HttpEntity entity = new UrlEncodedFormEntity(data, Constant.ENCODING);
			        //**设置参数
			        post.setEntity(entity);
			        //**执行请求
			        HttpResponse response = client.execute(post);       
			        //**请求成功，读取返回参数
			        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			        	finishCallBack(listener, response);
			        }
			        
			        else{
			        	post.abort();
			        	Log.d(TAG,  "返回数据不是正常状态：" + url);
			        }
				}catch(Exception e){
					post.abort();
					errorCallBack(listener, e);
				}
			}
			
		});
	}
	
	/**
	 * post提交请求
	 * @author ping
	 * @date 2015年10月1日
	 * @param url 访问地址
	 * @param data 参数
	 * @param listener 回调函数
	 */
	public static <V> void post(String url, Map<String, V> data, final HttpCallbackListener<?, Exception> listener){
       List<NameValuePair> newData = new ArrayList<NameValuePair>();
       
       //**转换参数类型
       for(Iterator<Map.Entry<String, V>> it = data.entrySet().iterator(); it.hasNext();){
    	   Map.Entry<String, V> entry = it.next();
    	   V value = entry.getValue();
    	   
    	   if(value != null)
    		   newData.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
    	   else
    		   newData.add(new BasicNameValuePair(entry.getKey(), ""));
       }

        post(url, newData, listener);
	}
	
	/**
	 * @Description: post上传
	 * @author ping
	 * @date 2016年3月22日
	 * @param url 访问地址
	 * @param files 上传文件
	 * @param data 参数
	 * @param listener 回调函数
	 */
	public static <V> void post(final String url, final List<File> files, final Map<String, V> data, final HttpCallbackListener<?, Exception> listener){
		//**创建httpPost对象
		final HttpPost post = new HttpPost(url);
		
		threadPool.execute(new Runnable(){

			@Override
			public void run() {
				try{
					//**创建httpClient对象
					client = getHttpClient();
					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					//**设置浏览器兼容模式
					builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

					//**添加文件参数
					for (int i = 0; i < files.size(); i++) {
						builder.addBinaryBody(Constant.REQ_UPLOAD_FILE_NAME, files.get(i));
					}
					
					//**添加其它参数
			       for(Iterator<Map.Entry<String, V>> it = data.entrySet().iterator(); it.hasNext();){
			    	   Map.Entry<String, V> entry = it.next();
			    	   V value = entry.getValue();
			    	   
			    	   if(value != null)
			    		   builder.addTextBody(entry.getKey(), entry.getValue().toString());
			    	   else
			    		   builder.addTextBody(entry.getKey(), "");	    	   
			       }
		
					//**设置请求参数
					post.setEntity(builder.build());
			        //**执行请求
			        HttpResponse response = client.execute(post);		        
			        //**请求成功，读取返回参数
			        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			        	finishCallBack(listener, response);
			        }
			        
			        else{
			        	post.abort();
			        	Log.d(TAG,  "返回数据不是正常状态：" + url);
			        }
				}catch(Exception e){
					post.abort();
					errorCallBack(listener, e);
				}
			}
			
		});
	}

}
