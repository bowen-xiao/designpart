package com.bowen.zdsjclub.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CacheUtils {

	private static final String NAME = "bowen_cache";

	private static SharedPreferences sp;

	/**
	 * 这里只是为了做到sp的重用
	 * 
	 * @param context
	 *            上下文对象
	 * @return
	 */
	private static SharedPreferences getSP(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
			//Activity.MODE_PRIVATE | Context.MODE_MULTI_PROCESS
		}
		return sp;
	}

	/**
	 * 
	 * @param context  上下文
	 * @param key    		变量名 
	 * @return
	 */
	public static boolean getBoolean(Context context, String key) {
		SharedPreferences preferences = getSP(context);
		return preferences.getBoolean(key, false);
	}
	
	/**
	 * 
	 * @param context  上下文
	 * @param key    		变量名 
	 * @param defValue  默认值
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences preferences = getSP(context);
		return preferences.getBoolean(key, defValue);
	}
	

	/**
	 * 
	 * @param context   上下文对象
	 * @param key			存入的名字
	 * @param value		需要存的值
	 */
	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences preferences = getSP(context);
		Editor edit = preferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	
	/**
	 * @param context  上下文
	 * @param key    		变量名 
	 * @return
	 */
	public static String getString(Context context, String key) {
		SharedPreferences preferences = getSP(context);
		return preferences.getString(key, "");
	}
	
	/**
	 * 
	 * @param context  上下文
	 * @param key    		变量名 
	 * @param defValue  默认值
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences preferences = getSP(context);
		return preferences.getString(key, defValue);
	}
	
	/**
	 * 
	 * @param context   上下文对象
	 * @param key			存入的名字
	 * @param value		需要存的值
	 */
	public static void setString(Context context, String key, String value) {
		SharedPreferences preferences = getSP(context);
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	
	/**
	 * 
	 * @param context  上下文
	 * @param key    		变量名 
	 * @return
	 */
	public static int getInt(Context context, String key) {
		SharedPreferences preferences = getSP(context);
		return preferences.getInt(key,0);
	}
	
	/**
	 * 
	 * @param context  上下文
	 * @param key    		变量名 
	 * @param defValue  默认值
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences preferences = getSP(context);
		return preferences.getInt(key, defValue);
	}
	
	/**
	 * 
	 * @param context   上下文对象
	 * @param key			存入的名字
	 * @param value		需要存的值
	 */
	public static void setInt(Context context, String key, int value) {
		SharedPreferences preferences = getSP(context);
		Editor edit = preferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	

}
