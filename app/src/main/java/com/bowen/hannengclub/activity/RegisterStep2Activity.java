package com.bowen.hannengclub.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.util.InputCheck;

import butterknife.BindView;
import butterknife.OnTextChanged;

public class RegisterStep2Activity extends BaseActivity {


	@BindView(R.id.et_confirm_nick_name)
	EditText  mEtNickName;
	@BindView(R.id.iv_input_status_nick_name)
	ImageView mIvNickNameStatus;
	@BindView(R.id.et_confirm_password_phone)
	EditText  mEtPasswordOne;
	@BindView(R.id.iv_input_status)
	ImageView mIvPasswordOneStatus;
	@BindView(R.id.et_confirm_password_two)
	EditText  mEtPasswordTwo;
	@BindView(R.id.iv_input_status_two)
	ImageView mIvPassWordTwoStatus;
	@BindView(R.id.et_register_two_introduce_phone)
	EditText  mEtTwoIntroducePhone;
	@BindView(R.id.iv_input_status_introduce)
	ImageView mIvIntroduceStatus;
	@BindView(R.id.btn_sure_to_confirm_register)
	Button    mBtnSureRegister;
	@BindView(R.id.tv_back_login)
	TextView  mTvBackLogin;
	private String mRegisterPhone;

	@Override
	public int getContextViewId() {
		return R.layout.activity_register_step2;
	}

	public final static String PARAM_PHONE_NUMBER = "param_phone_number";
	@Override
	public void initData() {
		//参数信息，必须项
		mRegisterPhone = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
	}

	@OnTextChanged(
		value = R.id.et_confirm_nick_name
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onTextChange(CharSequence s, int start, int count, int after){
		String inputOne = mEtNickName.getText().toString().trim();
		boolean isRight = InputCheck.isNickName(inputOne);
		mIvNickNameStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

	@OnTextChanged(
		value = R.id.et_confirm_password_phone
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onOnePasswordChange(CharSequence s, int start, int count, int after){
		String inputOne = mEtPasswordOne.getText().toString().trim();
		boolean isRight = InputCheck.isPassword(inputOne);
		mIvPasswordOneStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}


	@OnTextChanged(
		value = R.id.et_confirm_password_two
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onTwoPasswordChange(CharSequence s, int start, int count, int after){
		String inputOne = mEtPasswordOne.getText().toString().trim();
		String inputTwo = mEtPasswordTwo.getText().toString().trim();
		boolean isRight = inputOne.equals(inputTwo) && InputCheck.isPassword(inputOne);
		mIvPassWordTwoStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

	@OnTextChanged(
		value = R.id.et_register_two_introduce_phone
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onIntroduceChange(CharSequence s, int start, int count, int after){
		String inputOne = mEtTwoIntroducePhone.getText().toString().trim();
		boolean isRight = InputCheck.isNickName(inputOne);
		mIvIntroduceStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}


}
