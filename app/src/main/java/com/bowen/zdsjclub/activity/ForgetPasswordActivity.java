package com.bowen.zdsjclub.activity;

import android.view.View;

import com.bowen.zdsjclub.R;

import butterknife.OnClick;


public class ForgetPasswordActivity extends BaseActivity {

	@Override
	public int getContextViewId() {
		return R.layout.activity_forget_password;
	}

	@Override
	public void initData() {

	}

	@OnClick({
		R.id.tv_back_login
	})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.tv_back_login:
				finish();
				break;
		}
	}
}
