package indi.pings.android.util.listener;

import org.apache.http.HttpResponse;

/**
 * @Description: http回调函数
 * @author ping 
 * @date 2016年3月11日
 * @version V1.0
 */
public interface HttpCallbackListener<T, E extends Exception> {
	
	/**
	 * @Description: 解析响应
	 * @author ping
	 * @date 2016年3月18日
	 * @param response
	 */
	public T parseResponse(HttpResponse response) throws Exception;

	/**
	 * @Description: 成功时调用
	 * @author ping
	 * @date 2016年3月18日
	 * @param response
	 */
	public void onFinish(T response);
	
	/**
	 * @Description: 失败时调用
	 * @author ping
	 * @date 2016年3月18日
	 * @param e
	 */
	public void onError(E e);
}
