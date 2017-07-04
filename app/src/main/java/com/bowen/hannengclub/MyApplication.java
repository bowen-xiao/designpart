package com.bowen.hannengclub;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.bowen.hannengclub.util.ToolLog;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

import java.util.ArrayList;
import java.util.List;

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
		PlatformConfig.setWeixin("wxd394e2ec4060bd80", "920eee8134b2a9901c33a6f587593624");
		PlatformConfig.setQQZone("101394093", "691c49892d5511379551345bf5f369d3");
		/**
		 *   新浪微博分享
		 *   Apple ID：1234480662
			 App Key：53986201
			 App Secret：948f84d491b84fc365124b6732425078 **/
		PlatformConfig.setSinaWeibo("53986201", "948f84d491b84fc365124b6732425078", "http://sns.whalecloud.com");
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

	private static List<Activity> mActivityList = new ArrayList<>();// 记录应用打开的所有activity

	/**
	 * 添加activity
	 *
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		if (!mActivityList.contains(activity)) {
			mActivityList.add(activity);
		}
	}

	/**
	 * 应用关闭时逐个关闭activity
	 */
	public static void exit() {
		try {
			for (Activity activity : mActivityList) {
				if (activity != null) {
					activity.finish();
                   /* //关闭所有Activity
                    removeAll();
                    //退出进程
                    System.exit(0);*/
				}
			}
			mActivityList.clear();
		} catch (Exception e) {
			e.printStackTrace();
			ToolLog.e(" exit err" , e.getMessage());
		} finally {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
