package indi.pings.android.util.listener.impl;

import indi.pings.android.util.common.Constant;
import indi.pings.android.util.listener.HttpCallbackListener;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public abstract class StringCallBackListener implements HttpCallbackListener<String, Exception> {
	
	@Override
	public String parseResponse(HttpResponse response) throws Exception{
		return EntityUtils.toString(response.getEntity(), Constant.ENCODING);
	}

	@Override
	public abstract void onFinish(String response);
	
	@Override
	public void onError(Exception e) {
		
	}

}
