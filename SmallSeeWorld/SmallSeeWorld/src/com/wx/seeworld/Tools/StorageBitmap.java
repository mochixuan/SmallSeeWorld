package com.wx.seeworld.Tools;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

public class StorageBitmap {

	public static void GetandSaveCurrentImage(Activity activity, String imgName,Bitmap bitmap) {
		
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
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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

	public static String getImgPath(String imgName) {
		String filepath = getSDCardPath()
				+ "/seewolrdmode/ScreenImage/"+imgName+".png";
		File file = new File(filepath);
		if (file.exists()) {
			return filepath;
		}
		return null;
	}

}
