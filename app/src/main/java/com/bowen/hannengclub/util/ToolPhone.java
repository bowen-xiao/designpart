package com.bowen.hannengclub.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 肖稳华 on 2017/4/27.
 */

public class ToolPhone {

	/**
	 * 版本号
	 * http://blog.csdn.net/wh_19910525/article/details/8660416
	 * android:versionName:这个是我们常说明的版本号，由三部分组成<major>.<minor>.<point>,该值是个字符串，可以显示给用户。
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
								   PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}
}
