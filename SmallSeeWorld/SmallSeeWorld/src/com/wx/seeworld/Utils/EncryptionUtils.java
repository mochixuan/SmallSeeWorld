package com.wx.seeworld.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {

	public static String setEncryptionMD5(String content) {
		StringBuffer sbf = null;
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5"); // 获取MD5对象
			// 加密 8个byte一位永远为16个 16位8个一位共32位
			byte[] digest = instance.digest(content.getBytes());
			sbf = new StringBuffer();
			for (byte b : digest) {
				int a = b & 0xff; // 8个0 8个1 有的电脑11111111 01011010削去前面8个1(1位变两位)
				String hexString = Integer.toHexString(a); // 转化为16进制
				if (hexString.length() < 2) {
					hexString = "0" + hexString;
				}
				sbf.append(hexString);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sbf.toString();
	}

}
