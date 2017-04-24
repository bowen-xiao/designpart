package com.bowen.zdsjclub.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public abstract class BaseActivity extends FragmentActivity {

	Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//		锁定屏幕的方向
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 设置键盘不要破坏布局
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(getContextViewId());
		ButterKnife.bind(this);
		initData();
	}

	public abstract int getContextViewId();

	public abstract void initData();



}
