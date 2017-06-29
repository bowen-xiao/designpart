package com.bowen.hannengclub.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.hannengclub.MyApplication;
import com.bowen.hannengclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public abstract class BaseActivity extends FragmentActivity {

	Activity mActivity;
	@BindView(R.id.ll_common_left)
	LinearLayout mLeftRoot;
	@BindView(R.id.tv_common_title)
	protected TextView     mTvTitle;

	@BindView(R.id.tv_head_title_right_text)
	protected TextView     mTvRight;
	@BindView(R.id.ll_common_right)
	LinearLayout mRightRoot;

	//标题里面的内容
	@BindView(R.id.ll_common_header_root)
	LinearLayout mTitleRoot;

	//加载页面
	@BindView(R.id.ll_page_loadding_root)
	LinearLayout mLoaddingRoot;

	//加载页面
	@BindView(R.id.tv_common_loadding_status)
	TextView mLoaddingText;

	//加载页面
	@BindView(R.id.header_underline)
	View mHeaderLine;

	Bundle mIsRestartData;

	public void onCreateBefore(){}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		onCreateBefore();
		MyApplication.addActivity(this);
		//		锁定屏幕的方向
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 设置键盘不要破坏布局
		//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		getWindow().setFormat(PixelFormat.TRANSLUCENT);  adjustPan
		getWindow().setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		super.onCreate(savedInstanceState);
		mIsRestartData = savedInstanceState;
		mActivity = this;
		View view = View.inflate(mActivity, getContextViewId(), null);
		beforeSetContentView();
		setContentView(R.layout.activity_base);
		FrameLayout mContentView = (FrameLayout) findViewById(R.id.ff_common_content);
		mContentView.addView(view);
		ButterKnife.bind(this);
		initData();
		//设置标题
		setTitle();
	}

	//在启动之前
	public void beforeSetContentView(){}

	private void setTitle(){
		//设置标题
		String title = initTitle();
		/*if(TextUtils.isEmpty(title)){
			mTitleRoot.setVisibility(View.GONE);
		}else{
			mTitleRoot.setVisibility(View.VISIBLE);
			mTvTitle.setText(title);
		}*/
		mTvTitle.setText(title);
	}

	public void setAppTitle(String title){
		mTvTitle.setVisibility(View.GONE);
		mTvTitle.setText(title);
		if(!TextUtils.isEmpty(title)){
			mTvTitle.setVisibility(View.VISIBLE);
		}
	}

	//默认不实现，去设置标题
	protected String initTitle(){
		return  "";
	}

	public abstract int getContextViewId();

	public abstract void initData();

	//左右点击事件处理
	@OnClick({
		 R.id.ll_common_left
		,R.id.ll_common_right
	})
	public void handClick(View view) {
		com.bowen.hannengclub.util.ToolLog.i("click : " + view.getId());
		switch (view.getId()) {
			case R.id.ll_common_left:
				leftClick();
				break;
			case R.id.ll_common_right:
				rightClick();
				break;
		}
	}


	//右上角的点击事件处理
	protected void rightClick(){finish();}

	//默认操作是关闭
	protected void leftClick() {
		finish();
	}

	/** 隐藏已经打开的软键盘 **/
	public void hideSoftInput(){
		try {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus()
											 .getWindowToken(),
										 InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onPause() {
		hideSoftInput();
		super.onPause();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransitionEnter();
	}

	/**
	 * Overrides the pending Activity transition by performing the "Enter" animation.
	 */
	protected void overridePendingTransitionEnter() {
		overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
	}

	/**
	 * Overrides the pending Activity transition by performing the "Exit" animation.
	 */
	protected void overridePendingTransitionExit() {
		overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransitionExit();
	}

}
