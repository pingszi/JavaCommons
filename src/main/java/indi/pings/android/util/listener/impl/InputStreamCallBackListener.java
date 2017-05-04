package indi.pings.android.util.listener.impl;

import indi.pings.android.util.listener.HttpCallbackListener;

import java.io.InputStream;

import org.apache.http.HttpResponse;

public abstract class InputStreamCallBackListener implements HttpCallbackListener<InputStream, Exception> {
	
	@Override
	public InputStream parseResponse(HttpResponse response) throws Exception{
		return response.getEntity().getContent();
	}

	@Override
	public abstract void onFinish(InputStream response);
	
	@Override
	public void onError(Exception e) {
		
	}

}
