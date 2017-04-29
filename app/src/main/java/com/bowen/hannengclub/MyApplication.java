package com.bowen.hannengclub;

import android.app.Application;
import android.util.Log;

import com.bowen.hannengclub.util.ToolLog;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
/**
 * Created by 肖稳华 on 2017/4/18.
 *
 * http://dev.umeng.com/social/android/login-page
 */

public class MyApplication extends Application {

	public static Application context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		Log.e("main",this.getClass().getSimpleName() + " is start 。");
		PgyCrashManager.register(this);
		initTbs();
	}

	//使用QQ浏览器内核,提高网页的兼容性
	private void initTbs() {
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				com.bowen.hannengclub.util.ToolLog.i("View是否初始化完成:" + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				ToolLog.i("X5内核初始化完成");
			}
		};

		QbSdk.setTbsListener(new TbsListener() {
			@Override
			public void onDownloadFinish(int i) {
				com.bowen.hannengclub.util.ToolLog.i("腾讯X5内核 下载结束");
			}

			@Override
			public void onInstallFinish(int i) {
				com.bowen.hannengclub.util.ToolLog.i("腾讯X5内核 安装完成");
			}

			@Override
			public void onDownloadProgress(int i) {
				com.bowen.hannengclub.util.ToolLog.i("腾讯X5内核 下载进度:%" + i);
			}
		});

		QbSdk.initX5Environment(getApplicationContext(), cb);
	}
}
