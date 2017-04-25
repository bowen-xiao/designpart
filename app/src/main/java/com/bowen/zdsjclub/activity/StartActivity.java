package com.bowen.zdsjclub.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.bowen.zdsjclub.R;

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
		finish();
	}
}
