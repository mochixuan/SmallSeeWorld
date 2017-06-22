package com.wx.seeworld.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class JudgmentIsNetUtils {
	
	public static final int NOCONNECT=1;
	public static final int MOBILE=2;
	public static final int WIFI=3;

	@SuppressWarnings({ "deprecation" })
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo info = manager.getActiveNetworkInfo();  
		if(info != null && info.isAvailable()){
			
			State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			
			if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
				return MOBILE;
			}
			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				return WIFI;
			}
			
		}
		
		return NOCONNECT;

	}

}
