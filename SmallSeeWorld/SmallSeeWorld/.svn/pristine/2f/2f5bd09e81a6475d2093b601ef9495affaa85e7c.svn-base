package com.wx.seeworld.Utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DisPlayUtils {

	private static DisplayMetrics metrics;
	
	public DisPlayUtils(Activity activity) {
		metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	}

	public static int getWindowsHight(){
		return metrics.heightPixels;
	}
	
	public static int getWindowsWidth(){
		return metrics.widthPixels;
	}
	
}
