package indi.pings.android.util.version;

import indi.pings.android.util.entity.VersionInfoEntity;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Xml;

/**
 *********************************************************
 ** @desc  ：版本信息工具类                                             
 ** @author  Pings                                      
 ** @date    2016年5月20日  
 ** @version v1.0                                                                                  
 * *******************************************************
 */
public class VersionInfoUtil {
	
	/**
	 *********************************************************
	 ** @desc ：根据版本信息.xml获取更新的版本信息                                              
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                      
	 ** @param is
	 ** @return
	 ** @throws Exception                                              
	 * *******************************************************
	 */
	public static VersionInfoEntity getUpdateVersionInfo(InputStream is) throws Exception{
		VersionInfoEntity info = new VersionInfoEntity(); 
		XmlPullParser parser = Xml.newPullParser(); 
		
		//**设置解析的数据源 
	    parser.setInput(is, "utf-8");  
	    int type = parser.getEventType();  
	    
	    while(type != XmlPullParser.END_DOCUMENT ){  
	        switch (type) {  
	        case XmlPullParser.START_TAG:         	
	            if("versionCode".equals(parser.getName())){  
	                info.setVersionCode(Integer.parseInt(parser.nextText()));
	            }if("name".equals(parser.getName())){  
	                info.setName(parser.nextText());
	            }else if ("versionName".equals(parser.getName())){  
	                info.setVersionName(parser.nextText());
	            }else if ("url".equals(parser.getName())){  
	                info.setUrl(parser.nextText());
	            }else if ("updateInfo".equals(parser.getName())){  
	                info.setUpdateInfo(parser.nextText());
	            }else if ("desc".equals(parser.getName())){  
	                info.setDesc(parser.nextText());
	            }  
	            break;  
	        }  
	        type = parser.next();  
	    }
	    
	    return info;  
	}
	
	/** 
	 *********************************************************
	 ** @desc ：获取当前程序的版本信息                                        
	 ** @author Pings                                      
	 ** @date   2016年5月20日                                      
	 ** @return
	 ** @throws Exception                                              
	 * *******************************************************
	 */
	public static VersionInfoEntity getCurrentVersionInfo(Context context) throws Exception{  
		VersionInfoEntity ver = new VersionInfoEntity();
	    PackageManager packageManager = context.getPackageManager(); 
	    PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0); 
	    
	    ver.setVersionCode(packInfo.versionCode);
	    ver.setVersionName(packInfo.versionName);
	    
	    return ver;   
	}

}
