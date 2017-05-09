package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.bean.BaseReqResult;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.InputCheck;
import com.bowen.hannengclub.util.ToolLog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.bowen.hannengclub.activity.RegisterStep2Activity.PARAM_PHONE_CODE;
import static com.bowen.hannengclub.activity.RegisterStep2Activity.PARAM_PHONE_NUMBER;

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

	@BindView(R.id.btn_sure_to_confirm_password)
	Button mBtnSureRest;

//	@Override
//	protected String initTitle() {
//		return "确认密码";
//	}

	//activity_confirm_password
	@Override
	public int getContextViewId() {
		return R.layout.activity_confirm_password;
	}

	private String mRegisterPhone;
	private String mRegisterCode;

	@Override
	public void initData() {
		mRegisterPhone = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mRegisterCode = getIntent().getStringExtra(PARAM_PHONE_CODE);
		////校验登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串
//		/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$/
		//[a-zA-Z0-9]{6,16}
	}

	@OnClick(R.id.btn_sure_to_confirm_password)
	public void clickSubmit(View view){
		String inputOne = mEtInputOne.getText().toString().trim();
		String inputTwo = mEtInputTwo.getText().toString().trim();
		//boolean isRight = inputOne.equals(inputTwo) && isPassword(inputOne);

		if(!InputCheck.isPassword(inputOne)){
			DialogBean bean = new DialogBean("密码格式错误，请重新输入", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}

		if(!inputOne.equals(inputTwo)){
			DialogBean bean = new DialogBean("您两次输入的密码不一致，请重新输入", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}

		//2)网络请求
		mBtnSureRest.setEnabled(false);
		reSetPassword(inputOne);
	}


	private void reSetPassword(String password){
		/**
		 * 				必选	类型及范围	说明
		 phone_number	是	string	手机号
		 newpassword	是	string	新密码
		 code	是	string	验证码

		 */
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		final HashMap<String, Object> map = new HashMap<>();
		map.put("phone_number",mRegisterPhone);
		map.put("code",mRegisterCode);
		map.put("newpassword",password);
		service.resetPasword(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BaseReqResult>() {
				   @Override
				   public void onCompleted() {
					   mBtnSureRest.setEnabled(true);
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("saveRegister ", e.getMessage() + "--onError !");
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
							  //恭喜您，密码重置成功
							   DialogBean bean = new DialogBean("恭喜您，密码重置成功","","","");
							   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							   msgDialog.setLeftClick(new View.OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   finishRegister();
								   }
							   });
							   msgDialog.showDialog();
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

	@OnTextChanged(

			value = R.id.et_confirm_password_phone

		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onTextChange(CharSequence s, int start, int count, int after){
		String inputOne = mEtInputOne.getText().toString().trim();
		boolean isRight = InputCheck.isPassword(inputOne);
		mOneStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
		checkTwoStatus();
	}

	@OnTextChanged(
		value =
			R.id.et_forget_password_phone_two
		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onTextChangeTwo(CharSequence s, int start, int count, int after){
		checkTwoStatus();
	}

	private void checkTwoStatus(){
		String inputOne = mEtInputOne.getText().toString().trim();
		String inputTwo = mEtInputTwo.getText().toString().trim();
		boolean isRight = inputOne.equals(inputTwo) && InputCheck.isPassword(inputOne);
		mTwoStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

}
