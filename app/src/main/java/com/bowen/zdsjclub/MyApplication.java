package com.bowen.zdsjclub;

import android.app.Application;
import android.util.Log;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by 肖稳华 on 2017/4/18.
 */

public class MyApplication extends Application {

	public static Application context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		Log.e("main",this.getClass().getSimpleName() + " is start 。");
		PgyCrashManager.register(this);
	}
}
