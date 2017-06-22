package com.wx.seeworld.Tools;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import com.wx.seeworld.Utils.DisPlayUtils;
import com.wx.seeworld.Utils.NaviStaticBarHightUtils;

public class Screenshot {

	public static void GetandSaveCurrentImage(Activity activity, String imgName) {
		
		// 2.获取屏幕
		View decorview = activity.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		
		// 1.构建Bitmap
		int staticBarHight = NaviStaticBarHightUtils.getStaticBarHight(activity);
		int windowsHight = DisPlayUtils.getWindowsHight();
		int windowsWidth = DisPlayUtils.getWindowsWidth();
		Bitmap Bmp = Bitmap.createBitmap(decorview.getDrawingCache(), 0,staticBarHight, windowsWidth, windowsHight - staticBarHight);
		decorview.setDrawingCacheEnabled(false);		//以清空画图缓冲区
		String SavePath = getSDCardPath() + "/seewolrdmode/ScreenImage";

		// 3.保存Bitmap
		try {
			File path = new File(SavePath);
			// 文件
			String filepath = SavePath + "/" + imgName + ".png";
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	private static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	public static String dayImgPath() {
		String filepath = getSDCardPath()
				+ "/seewolrdmode/ScreenImage/day_mode.png";
		File file = new File(filepath);
		if (file.exists()) {
			return filepath;
		}
		return null;
	}

	public static String nightImgPath() {
		String filepath = getSDCardPath()
				+ "/seewolrdmode/ScreenImage/night_mode.png";
		File file = new File(filepath);
		if (file.exists()) {
			return filepath;
		}
		return null;
	}

}
