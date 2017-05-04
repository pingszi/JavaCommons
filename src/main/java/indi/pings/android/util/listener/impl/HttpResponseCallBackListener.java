package indi.pings.android.util.listener.impl;

import indi.pings.android.util.listener.HttpCallbackListener;

import org.apache.http.HttpResponse;

public abstract class HttpResponseCallBackListener implements HttpCallbackListener<HttpResponse, Exception> {
	
	@Override
	public HttpResponse parseResponse(HttpResponse response) throws Exception{
		return response;
	}

	@Override
	public abstract void onFinish(HttpResponse response);
	
	@Override
	public void onError(Exception e) {
		
	}

}
