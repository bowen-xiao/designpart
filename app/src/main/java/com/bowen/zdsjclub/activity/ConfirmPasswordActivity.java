package com.bowen.zdsjclub.activity;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bowen.zdsjclub.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ConfirmPasswordActivity extends BaseActivity {

	//第二次输入的密码
	@BindView(R.id.et_forget_password_phone_two)
	EditText mEtInputTwo;

	@BindView(R.id.et_confirm_password_phone)
	EditText mEtInputOne;

	//输入的状态
	@BindView(R.id.iv_input_status)
	ImageView mOneStatus;

	//第二次的输入状态
	@BindView(R.id.iv_input_status_two)
	ImageView mTwoStatus;

	@Override
	protected String initTitle() {
		return "确认密码";
	}

	//activity_confirm_password
	@Override
	public int getContextViewId() {
		return R.layout.activity_confirm_password;
	}

	@Override
	public void initData() {
		////校验登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串
//		/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$/
		//[a-zA-Z0-9]{6,16}
	}

	@OnClick(R.id.btn_sure_to_confirm_password)
	public void clickSubmit(View view){
		String inputOne = mEtInputOne.getText().toString().trim();
		String inputTwo = mEtInputTwo.getText().toString().trim();
		boolean isRight = inputOne.equals(inputTwo) && checkInput(inputOne);
		//通过验证的
		if(isRight){
				// // TODO: 2017/4/25 请求接口
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						//去访问数据
					}
				},5000L);
		}
	}

	@OnTextChanged(
		value ={
			R.id.et_forget_password_phone
		}
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED
	)
	public void onTextChange(CharSequence s, int start, int count, int after){
		String inputOne = mEtInputOne.getText().toString().trim();
		boolean isRight = checkInput(inputOne);
		mOneStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

	@OnTextChanged(
		value ={
			R.id.et_forget_password_phone_two
		}
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED
	)
	public void onTextChangeTwo(CharSequence s, int start, int count, int after){
		String inputOne = mEtInputOne.getText().toString().trim();
		String inputTwo = mEtInputTwo.getText().toString().trim();
		boolean isRight = inputOne.equals(inputTwo) && checkInput(inputOne);
		mTwoStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

	//是否是合法的值 6-16位的数字和字母
	private boolean checkInput(String myInput){
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(myInput);
		return (matcher.matches());
	}
}
