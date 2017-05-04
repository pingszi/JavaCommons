package indi.pings.android.activityManage;

import java.util.*;

import android.app.Activity;

/**
 *********************************************************
 ** @desc  ：activity容器                                             
 ** @author  Pings                                      
 ** @date    2016年11月30日  
 ** @version v1.0                                                                                  
 * *******************************************************
 */
public class ActivityCollector {

	private static final Map<String, Activity> activites = new HashMap<String, Activity>();
	
	public static void add(Activity activity){
		activites.put(activity.getClass().getSimpleName(), activity);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Activity> T get(Class<T> cla){
		return (T) activites.get(cla.getSimpleName());
	}
	
	public static void remove(Activity activity){
		activites.remove(activity.getClass().getSimpleName());
	}
	
	public static void finishAll(){
		for(Iterator<Map.Entry<String, Activity>> it = activites.entrySet().iterator(); it.hasNext();){
			Map.Entry<String, Activity> activity = it.next();
			activity.getValue().finish();
			it.remove();
		}
	}
}
