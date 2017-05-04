package indi.pings.android.util.common;

import org.apache.http.HttpVersion;
import org.apache.http.protocol.HTTP;

/**
 * 定义一些常量
 * @author ping 
 * @date 2015年10月1日
 * @version V1.0
 */
public final class Constant {

	/**
	 * http请求
	 * =================================================================
	 */
	//**从连接池中获取连接的超时时间
	public static final int TIME_OUT = 10000;
	//**连接超时时间
	public static final int CONNECTION_TIME_OUT = 10000;
	//**读取数据超时时间
	public static final int SO_TIME_OUT = 10000;
	//**字符集
	public static final String ENCODING = HTTP.UTF_8;
	//**http协议版本
	public static final HttpVersion HTTP_PROTOCOL_VERSION = HttpVersion.HTTP_1_1;
	//**http协议
	public static final String HTTP_PROTOCOL = "http";
	//**https协议
	public static final String HTTPS_PROTOCOL = "https";
	//**连接池中的最多连接总数
	public static final int DEFAULT_MAX_CONNECTIONS = 10;
	//**每台主机最多连接数
	public static final int DEFAULT_HOST_CONNECTIONS = 10;
	//**上传文件参数名
	public static final String REQ_UPLOAD_FILE_NAME = "uploadFiles";
	/**=================================================================*/
	
}
