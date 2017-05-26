package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.bowen.hannengclub.R;


/**
 * Created by 肖稳华 on 2017/4/24.
 */

public class StartActivity extends BaseActivity {

	@Override
	protected String initTitle() {
		mTitleRoot.setVisibility(View.GONE);
		return "";
	}

	@Override
	public int getContextViewId() {
		//需要全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		return R.layout.activity_start;
	}

	@Override
	public void initData() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				jumpToHome();
			}
		},3000L);
	}

	//跳转到首页
	private void jumpToHome(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		overridePendingTransition(0,0);
		finish();
	}


	/**
	 * Overrides the pending Activity transition by performing the "Enter" animation.
	 */
	protected void overridePendingTransitionEnter() {
		overridePendingTransition(0, 0);
	}

	/**
	 * Overrides the pending Activity transition by performing the "Exit" animation.
	 */
	protected void overridePendingTransitionExit() {
		overridePendingTransition(0, 0);
	}

}
