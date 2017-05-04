package indi.pings.android.activityManage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * @Description: BaseActivity
 * @author ping 
 * @date 2015年7月20日
 * @version V1.0
 */
public class BaseActivity extends Activity{
	
	protected int START = 0;
	protected int LIMIT = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", this.getClass().getSimpleName());
		ActivityCollector.add(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.remove(this);
	}

}
