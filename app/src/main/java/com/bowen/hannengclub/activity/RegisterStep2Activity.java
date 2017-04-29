package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.bean.BaseReqResult;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.InputCheck;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
	EditText  mEtIntroducePhone;
	@BindView(R.id.iv_input_status_introduce)
	ImageView mIvIntroduceStatus;
	@BindView(R.id.btn_sure_to_confirm_register)
	Button    mBtnSureRegister;
	@BindView(R.id.tv_back_login)
	TextView  mTvBackLogin;
	private String mRegisterPhone;
	private String mRegisterCode;

	@Override
	public int getContextViewId() {
		return R.layout.activity_register_step2;
	}

	public final static String PARAM_PHONE_NUMBER = "param_phone_number";
	public final static String PARAM_PHONE_CODE = "param_phone_code";
	@Override
	public void initData() {
		//参数信息，必须项
		mRegisterPhone = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mRegisterCode = getIntent().getStringExtra(PARAM_PHONE_CODE);
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
		String inputOne = mEtIntroducePhone.getText().toString().trim();
		boolean isRight = InputCheck.isPhoneNumber(inputOne);
		mIvIntroduceStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

	@OnClick(
		value = {
			R.id.btn_sure_to_confirm_register,
			R.id.tv_back_login,
		}
	)
	public void btnClick(View view){
		switch (view.getId()){
			case R.id.btn_sure_to_confirm_register:
				//去保存注册信息
				saveRegisterInfo();
				break;
			case R.id.tv_back_login:
				//去保存注册信息
				finishRegister();
				break;
		}
	}

	//去保存注册信息
	private void saveRegisterInfo(){
		// 1) 检查输入是否合法
		String nickName = mEtNickName.getText().toString().trim();
		if(!InputCheck.isNickName(nickName)){
			DialogBean bean = new DialogBean("格式错误，请重新输入", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}
		String passwordOne = mEtPasswordOne.getText().toString().trim();
		if(!InputCheck.isPassword(passwordOne)){
			DialogBean bean = new DialogBean("密码格式错误，请重新输入", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}

		String passwordTwo = mEtPasswordTwo.getText().toString().trim();
		if(!passwordOne.equals(passwordTwo)){
			DialogBean bean = new DialogBean("您两次输入的密码不一致，请重新输入", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}

		String introducePhone = mEtIntroducePhone.getText().toString().trim();
		if(!TextUtils.isEmpty(introducePhone) ){
			if(!InputCheck.isPhoneNumber(introducePhone)){
				DialogBean bean = new DialogBean("您输入的手机号码有误，请重新输入", "", "", "");
				CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
				msgDialog.showDialog();
				return;
			}
		}

		// 2) 请求网络保存
		mBtnSureRegister.setEnabled(false);
		saveRegister(nickName,passwordOne,introducePhone);
	}


	private void saveRegister(String nickname,String password,String sales_phone){
		/**
		 * 			必选	类型及范围	说明
				 phone_number	是	string	手机号
				 nickname	是	string	昵称
				 password	是	string	密码
				 code	是	string	验证码
				 sales_phone	否	string	推荐人手机号
		 */
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		final HashMap<String, Object> map = new HashMap<>();
		map.put("phone_number",mRegisterPhone);
		map.put("code",mRegisterCode);
		map.put("nickname",nickname);
		map.put("password",password);
		map.put("sales_phone",sales_phone);
		/*LoginResult
		phone_number	是	string	用户号码
		type	是	int	类型：0账号登录，1手机登录
		password	是	string	用户密码或验证码*/
		service.register(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BaseReqResult>() {
				   @Override
				   public void onCompleted() {
					   mBtnSureRegister.setEnabled(true);
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("saveRegister ", e.getMessage() + "--onError !");
					   mBtnSureRegister.setEnabled(true);
				   }

				   @Override
				   public void onNext(BaseReqResult model) {
					   ToolLog.e("saveRegister ",model + "--请求完成 !");
					   if(model != null){
						   if(model.getStatus() == 0){
							   //ToastUtil.showToast(mActivity, model.getErrmsg());
							   DialogBean bean = new DialogBean(model.getErrmsg(),"","","");
							   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							   msgDialog.showDialog();
						   }else{
							   ToastUtil.showToast(mActivity, "注册成功");
							   //关闭2个注册页面
							   finishRegister();
						   }
					   }
				   }
			   });
	}

	private void finishRegister(){
		Intent intent = new Intent(RegisterActivity.FINISH_ACTIVITY);
		sendBroadcast(intent);
		finish();
	}

}
