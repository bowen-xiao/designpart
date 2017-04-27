package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.dialog.LoginErrDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

	@BindView(R.id.login_margin)
	View mMarginTop;
	private LoginErrDialog mLoginErrDialog;

	@Override
	protected String initTitle() {
		return "登录";
	}

	@Override
	public int getContextViewId() {
		return R.layout.activity_login;
	}

	@Override
	public void initData() {
		//1)设置距离顶部的高度
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mMarginTop.getLayoutParams();
		int screenWith = mActivity.getResources().getDisplayMetrics().widthPixels;
		layoutParams.width = screenWith;
		layoutParams.height = (int) (screenWith * 0.2f + 0.5f);
		mMarginTop.setLayoutParams(layoutParams);
	}

	@OnClick({
		R.id.btn_login
		,R.id.tv_forget_password
		,R.id.tv_user_register
	})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.btn_login:
				showLoginErr();
				break;
			case R.id.tv_forget_password:
				jumpToForgetPage();
				break;
			case R.id.tv_user_register:
				jumpToRegister();
				break;
		}
	}

	private void jumpToRegister(){
//		RegisterActivity
		Intent intent = new Intent(mActivity, RegisterActivity.class);
		startActivity(intent);
	}

	//显示登录错误的信息
	private void showLoginErr() {
		if(mLoginErrDialog == null){
			mLoginErrDialog = new LoginErrDialog(mActivity);
		}
		mLoginErrDialog.showDialog();
	}

	private void jumpToForgetPage(){
		Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		if(mLoginErrDialog != null){
			mLoginErrDialog.dismiss();
		}
		super.onDestroy();
	}
}
