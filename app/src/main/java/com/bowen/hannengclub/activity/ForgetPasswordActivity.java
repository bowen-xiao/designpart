package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.util.InputCheck;
import com.bowen.hannengclub.util.ToastUtil;

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

	@BindView(R.id.iv_input_phone_status)
	ImageView mPhoneNumberStatus;

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
		startTimer();
	}

	private Handler myHandler ;
	private void startTimer(){
		String phoneNumber = mInputPhone.getText().toString().trim();
		//手机号码不正确
		boolean isPhoneNumber = InputCheck.isPhoneNumber(phoneNumber);
		mPhoneNumberStatus.setImageResource(isPhoneNumber ? R.mipmap.input_right : R.mipmap.input_err);
		if(!isPhoneNumber){
			ToastUtil.showToast(mActivity,"请输入正确的手机号码");
			return;
		}
		//开始获取验证码
		seconds = 60;
		mBtnMsgTime.setText("获取验证码(60s)");
		mBtnMsgTime.setEnabled(false);
		myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				seconds--;
				mBtnMsgTime.setText( "获取验证码(" + seconds + "s)");
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
