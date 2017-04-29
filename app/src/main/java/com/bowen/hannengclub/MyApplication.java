package com.bowen.hannengclub;

import android.app.Application;
import android.util.Log;

import com.bowen.hannengclub.util.ToolLog;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

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
		initShareConfig();
	}

	//初始化友盟分享
	private void initShareConfig() {
		//Todo 分享的ID
		PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
		PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
		UMShareAPI.get(this);

		UMShareConfig config = new UMShareConfig();
		//使用授权页
		config.isNeedAuthOnGetUserInfo(true);
		//分享内容不可以被编辑
		config.isOpenShareEditActivity(false);

		config.setSinaAuthType(UMShareConfig.AUTH_TYPE_WEBVIEW);;
		config.setFacebookAuthType(UMShareConfig.AUTH_TYPE_SSO);
		config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);

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
