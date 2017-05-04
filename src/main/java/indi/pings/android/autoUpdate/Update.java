package indi.pings.android.autoUpdate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import indi.pings.android.util.entity.VersionInfoEntity;
import indi.pings.android.util.listener.impl.HttpResponseCallBackListener;
import indi.pings.android.util.listener.impl.InputStreamCallBackListener;
import indi.pings.android.util.net.HttpClientUtil;
import indi.pings.android.util.version.VersionInfoUtil;

/**
 *********************************************************
 ** @desc  ：检查更新系统版本                                           
 ** @author  Pings                                      
 ** @date    2016年5月20日  
 ** @version v1.0                                                                                  
 * *******************************************************
 */
public class Update {
	
	private static final String TAG = "Update";
	
	//**窗体上下文
	private Context context;
	//**窗体的handler
	private Handler handler;
	//**服务器版本控制文件url
	private String versionXmlUrl;
	//**客户端最新版本信息
	private VersionInfoEntity newVer;
	//**客户端当前版本信息
	private VersionInfoEntity curVer;

	/** 
	 *********************************************************
	 ** @desc ：弹出对话框通知用户更新程序                                               
	 ** @author Pings                                      
	 ** @date   2016年5月20日 
	 ** @param context       窗体上下文
	 ** @param handler       窗体的handler 
	 ** @param versionXmlUrl 版本控制文件url                                                                                  
	 * *******************************************************
	 */
	public Update(Context context, Handler handler, String versionXmlUrl){
		this.context = context;
		this.handler = handler;
		this.versionXmlUrl = versionXmlUrl; 
	}
	
	/**
	 *********************************************************
	 ** @desc ：启用版本检查更新                                             
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                                                                    
	 * *******************************************************
	 */
	public void start(){
		checkVersionInfo();
	}
	
	/**
	 *********************************************************
	 ** @desc ：检查版本信息                                             
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                                                                    
	 * *******************************************************
	 */
	public void checkVersionInfo(){
		HttpClientUtil.get(versionXmlUrl, new InputStreamCallBackListener() {
			
			@Override
			public void onFinish(InputStream response) {
				//**检查客户端是否是最新版本，不是则提示用户升级
				try {
					newVer = VersionInfoUtil.getUpdateVersionInfo(response);
					curVer = VersionInfoUtil.getCurrentVersionInfo(context);
					if(curVer.equals(newVer)){
						Log.i(TAG, "版本一致，无需升级");
					}else{
						Log.i(TAG, "版本不一致 ，提示用户升级");					
						Message mes = Message.obtain(handler, new Runnable(){

							@Override
							public void run() {	
								showUpdataDialog();
							}
							
						});
						mes.sendToTarget();
					}
				} catch (Exception e) {
					Log.e(TAG, "get version info failure", e);
				} finally {
					try {
						response.close();
					} catch (IOException e) {
						Log.e(TAG, "close inputStream failure", e);
					}
				}
			}
			
			@Override
			public void onError(Exception e) {
				Message mes = Message.obtain(handler, new Runnable(){

					@Override
					public void run() {	
						Toast.makeText(context, "检查更新出错", Toast.LENGTH_SHORT).show();
					}
					
				});
				mes.sendToTarget();
			}
		});
	}
	
	/** 
	 *********************************************************
	 ** @desc ：弹出对话框通知用户更新程序                                               
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                                                                    
	 * *******************************************************
	 */
	public void showUpdataDialog() {  
	    AlertDialog.Builder builer = new Builder(context);   
	    builer.setTitle("版本升级");  
	    builer.setMessage(newVer.getDesc()); 

	    //**确定升级 
	    builer.setPositiveButton("确定", new OnClickListener() { 
	    	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk，更新");  
	            downLoadApk();
			}     
	    });  
	    
	    //**取消升级
	    builer.setNegativeButton("取消", new OnClickListener() {  
	    	
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {  
	    		Log.i(TAG, "不更新");
	        }  
	    });  
	    
	    AlertDialog dialog = builer.create();  
	    dialog.show();  
	} 
	
	/**
	 *********************************************************
	 ** @desc ：下载最新的apk安装包                                             
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                                                                    
	 * *******************************************************
	 */
	public void downLoadApk(){
		//**进度条对话框  
		final ProgressDialog pd = new ProgressDialog(context);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("正在下载更新...");  
	    pd.show();
	    
	    String apkUrl = newVer.getUrl();
	    HttpClientUtil.get(apkUrl, new HttpResponseCallBackListener() {
			
			@Override
			public void onFinish(HttpResponse response) {
				//**sdcard不可用
			    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			    	return;
			    
			    FileOutputStream fos = null;  
		        BufferedInputStream bis = null;
			    try{ 
			    	//**获取安装文件大小   
				    pd.setMax((int)response.getEntity().getContentLength());  
				    File file = new File(Environment.getExternalStorageDirectory(), newVer.getName()); 
				    if(file.exists())
				    	file.delete();
				    file.createNewFile();
			        fos = new FileOutputStream(file);  
			        bis = new BufferedInputStream(response.getEntity().getContent());  
			        byte[] buffer = new byte[1024];  
			        int len, cur = 0;  
			        while((len = bis.read(buffer)) != -1){  
			            fos.write(buffer, 0, len);  
			            cur += len;  
			            //**当前下载进度  
			            pd.setProgress(cur);
			        } 
			        Log.i(TAG, "下载apk成功，安装apk");
			        TimeUnit.SECONDS.sleep(1); 
			        pd.dismiss();
	                installApk(file);
			    } catch (Exception e) {
			    	Log.e(TAG, "download apk failure", e);
				} finally {
					try {
						fos.close();  
				        bis.close(); 
					} catch (IOException e) {
						Log.e(TAG, "close inputStream failure", e);
					}
				}
			}
			
			@Override
			public void onError(Exception e) {
				Message mes = Message.obtain(handler, new Runnable(){

					@Override
					public void run() {	
						pd.dismiss();
						Toast.makeText(context, "下载更新失败", Toast.LENGTH_SHORT).show();
					}
					
				});
				mes.sendToTarget();
			}
		});
	}
	
	/**  
	 *********************************************************
	 ** @desc ：安装apk                                             
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                      
	 ** @param file                                              
	 * *******************************************************
	 */
	public void installApk(File file) {  
	    Intent intent = new Intent();   
	    intent.setAction(Intent.ACTION_VIEW);
	    
	    //**执行的数据类型  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
	    context.startActivity(intent);  
	} 
}
