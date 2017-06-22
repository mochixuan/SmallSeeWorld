package com.wx.seeworld.CacheResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.wx.seeworld.Utils.EncryptionUtils;
import com.wx.seeworld.Utils.ToastUtils;

public class LocalCacheUtils {

	private static final String CACHE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/greatworld";
	private static final String FILE_NAME = EncryptionUtils
			.setEncryptionMD5("seeworlddata.txt");

	private static final int LOADINGHEADER = 0;
	private static final int LOADINGFOOTER = 1;

	@SuppressWarnings("deprecation")
	public static boolean IsStorageState() {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File storageDirectory = Environment.getExternalStorageDirectory();
			StatFs statFs = new StatFs(storageDirectory.getPath());
			long blockSize = statFs.getBlockSize(); // 一块多大
			long freeBlocks = statFs.getAvailableBlocks(); // 多少块
			long sdSizeMb = (freeBlocks * blockSize) / 1024 / 1024;
			if (sdSizeMb > 50) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getCacheFromLocal(Context context) {
		List<String> lists = new ArrayList<String>();
		File file = new File(CACHE_PATH, FILE_NAME);
		if (file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(file),
								"UTF-8"));
				String readLine = reader.readLine();
				String[] imgCaches = readLine.split("##");
				reader.close();
				for (String string : imgCaches) {
					lists.add(string);
				}
				Jugdement(lists, context);
				return lists;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void Jugdement(final List<String> lists,
			final Context context) {

		if (lists.size() > 15) {

			new Thread(new Runnable() {
				public void run() {
					try {
						File file = new File(CACHE_PATH, FILE_NAME);
						FileOutputStream fos = new FileOutputStream(file);
						fos.write("".getBytes());
						fos.close();
						for (int i = lists.size() - 15; i < lists.size(); i++) {
							setCacheToLocal(lists.get(i), context,
									LOADINGHEADER);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}).start();

		} else {
			return;
		}
	}

	public static void setCacheToLocal(String url, Context context,
			int loadingMore) {
		
		if(loadingMore==LOADINGFOOTER)
			return;

		url = url + "##";
		boolean state = IsStorageState();
		if (!state) {
			ToastUtils.makeText(context, "无内存卡或内存不足");
			return;
		}

		File file = new File(CACHE_PATH, FILE_NAME);

		File parentFile = file.getParentFile(); // 文件夹不存在
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		try {

			RandomAccessFile raf = new RandomAccessFile(file, "rwd");

			int read = 0;

			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				read = fis.available();
				fis.close();
			}

			raf.seek(read);
			raf.write(url.getBytes("UTF-8"));
			raf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
