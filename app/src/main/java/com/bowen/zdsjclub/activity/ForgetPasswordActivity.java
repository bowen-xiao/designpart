package com.bowen.zdsjclub.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.dialog.CommonMsgDialog;
import com.bowen.zdsjclub.dialog.DialogBean;

import butterknife.BindView;
import butterknife.OnClick;


public class ForgetPasswordActivity extends BaseActivity {

	@BindView(R.id.et_forget_password_phone)
	EditText mInputPhone;

	@BindView(R.id.et_forget_password_msg_recevie)
	EditText mMsgNumber;

	@BindView(R.id.btn_get_phone_msg_number)
	Button mBtnMsgTime;
	private CommonMsgDialog mMsgDialog;

	@Override
	protected String initTitle() {
		return "忘记密码";
	}

	@Override
	public int getContextViewId() {
		return R.layout.activity_forget_password;
	}

	@Override
	public void initData() {
		//初始化数据
		myHandler = new Handler();
	}

	@OnClick({
		R.id.tv_back_login
		,R.id.btn_sure_to_reset_password
		,R.id.btn_get_phone_msg_number
	})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.tv_back_login:
				finish();
				break;
			case R.id.btn_sure_to_reset_password:
				// // TODO: 2017/4/25  需要去访问网络接口获取检验数据
				checkInput();
				break;
			case R.id.btn_get_phone_msg_number:

				getMsgNumber();
				break;
		}
	}

	int seconds = 60;
	//需要去访问接口
	// // TODO: 2017/4/25  需要去短信验证码
	private void getMsgNumber(){
		seconds = 60;
		mBtnMsgTime.setText("60s");
		startTimer();
	}

	private Handler myHandler ;
	private void startTimer(){
		mBtnMsgTime.setEnabled(false);
		myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				seconds--;
				mBtnMsgTime.setText( seconds + "s");
				if(seconds == 0){
					mBtnMsgTime.setText("获取验证码");
					mBtnMsgTime.setEnabled(true);
					return;
				}
				myHandler.postDelayed(this,1000L);
			}
		},1000L);
	}

	//检查输入项
	private void checkInput(){
		String msgNumber = mMsgNumber.getText().toString();
		if(TextUtils.isEmpty(msgNumber)){
			DialogBean bean = new DialogBean("错误信息", "", "", "");
			//显示错误信息
			mMsgDialog = new CommonMsgDialog(mActivity, bean);
			mMsgDialog.showDialog();
		}else{
			//确认密码页面
			Intent intent = new Intent(mActivity, ConfirmPasswordActivity.class);
			startActivity(intent);
		}
	}

}
