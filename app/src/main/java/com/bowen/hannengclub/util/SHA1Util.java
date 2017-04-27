/**
 * SHA1Util.java
 * 2014-8-22
 * 深圳市五月高球信息咨询有限公司
 * 欧阳丰
 */
package com.bowen.hannengclub.util;

import android.annotation.SuppressLint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 欧阳丰
 * 
 */
public class SHA1Util {

	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1"); // 选择SHA-1，也可以选择MD5
			md.update(inStr.getBytes());
			byte[] digest = md.digest(); // 返回的是byet[]，要转化为String存储比较方便
			outStr = bytetoString(digest);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return outStr;
	}

	@SuppressLint("DefaultLocale")
	public static String bytetoString(byte[] messageDigest) {
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < messageDigest.length; i++) {
			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	}
}
