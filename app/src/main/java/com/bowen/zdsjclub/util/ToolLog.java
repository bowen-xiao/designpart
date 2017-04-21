package com.bowen.zdsjclub.util;

import android.util.Log;

/**
 * 日志工具类
 *
 */
public class ToolLog {
	
	private static final String TAG = "ToolLog";

	/**
	 * 调试信息的是否显示，上线后关闭log
	 */
	public static Boolean DEBUG = true;
	
	public static void d(String tag, String msg) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.d(TAG, tag + " : " + msg);
		}
	}

	public static void d(String tag, String msg, Throwable error) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.d(TAG, tag + " : " + msg, error);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.i(TAG, tag + " : " + msg);
		}
	}

	public static void i(String tag, String msg, Throwable error) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.i(TAG, tag + " : " + msg, error);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.w(TAG, tag + " : " + msg);
		}
	}

	public static void w(String tag, String msg, Throwable error) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.w(TAG, tag + " : " + msg, error);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.e(TAG, tag + " : " + msg);
		}
	}

	public static void e(String tag, String msg, Throwable error) {
		if (DEBUG) {
			tag = Thread.currentThread().getName() + ":" + tag;
			Log.e(TAG, tag + " : " + msg, error);
		}
	}
}
