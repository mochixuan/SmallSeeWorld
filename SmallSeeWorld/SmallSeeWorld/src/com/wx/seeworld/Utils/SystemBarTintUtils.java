package com.wx.seeworld.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SystemBarTintUtils {
	
	private static Context context;
	private static boolean supportTint;

	@SuppressWarnings("static-access")
	public SystemBarTintUtils(Context context){
		this.context=context;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			supportTint=true;
		}
	}
	
	@SuppressWarnings("unused")
	@TargetApi(19)
	public static void setStatusBarTint(int colorId) {
		if(supportTint){
			Window win = ((Activity) context).getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS; // 透明通知栏
			if (true) {
				winParams.flags |= bits; // 去除默认黑色状态栏(有一个一就是一)
			} else {
				winParams.flags &= ~bits;
			}
			win.setAttributes(winParams);
			SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
			tintManager.setStatusBarTintEnabled(true); // 激活状态栏设置
			tintManager.setStatusBarTintColor(colorId); // 通知栏所需颜色
		}
	}
	
	@SuppressWarnings("unused")
	@TargetApi(19)
	public static void setNavigationBarTint(int colorId) {
		if(supportTint){
			Window win = ((Activity) context).getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION; // 透明通知栏
			if (true) {
				winParams.flags |= bits; // 去除默认黑色状态栏(有一个一就是一)
			} else {
				winParams.flags &= ~bits;
			}
			win.setAttributes(winParams);
			SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
			tintManager.setNavigationBarTintEnabled(true); // 激活状态栏设置
			tintManager.setNavigationBarTintColor(colorId); // 通知栏所需颜色
		}
	}

}
