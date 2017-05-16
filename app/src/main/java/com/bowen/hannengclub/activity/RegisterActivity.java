package com.bowen.hannengclub.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.bean.BaseReqResult;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.fragment.CommonFragment;
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


public class RegisterActivity extends BaseActivity {

	@BindView(R.id.et_forget_password_phone)
	EditText mInputPhone;

	@BindView(R.id.et_forget_password_msg_recevie)
	EditText mMsgNumber;

	@BindView(R.id.btn_get_phone_msg_number)
	Button mBtnMsgTime;
	private CommonMsgDialog mMsgDialog;

	@BindView(R.id.iv_input_phone_status)
	ImageView mPhoneNumberStatus;

	//返回并登录的文字
	@BindView(R.id.tv_back_login)
	TextView mTvBottomBack;

	//注册协议
	@BindView(R.id.ll_register_comment_root)
	LinearLayout mRegisterComment;

	//是否同意的按钮
	@BindView(R.id.cb_register_comment)
	CheckBox mCBAgree;
	private String mPhoneNumber;
//
//	@Override
//	protected String initTitle() {
//		return "注册";
//	}


	public final static String FINISH_ACTIVITY = "finish_activity";
	/**
	 * 注册广播,刷新头像等用户信息
	 */
	public void registerBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(FINISH_ACTIVITY);
		registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (FINISH_ACTIVITY.equals(intent.getAction())) {
				//默认选中第一个
				finish();
			}
		}};

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public int getContextViewId() {
		return R.layout.activity_forget_password;
	}

	@Override
	public void initData() {
		registerBroadcast();
		mRegisterComment.setVisibility(View.VISIBLE);
		mTvBottomBack.setVisibility(View.GONE);
		mInputPhone.setHint("请输入你的手机号");
		//初始化数据
		myHandler = new Handler();

		//数据恢复操作
		if(mIsRestartData != null){
			String inputPhone = mIsRestartData.getString(REGISTER_PHONE);
			if(TextUtils.isEmpty(inputPhone)){return;}
			seconds = mIsRestartData.getInt(REGISTER_CODE,0);
			//如果时间不为零才显示
			if(seconds != 0){
				startTimer();
			}
		}
	}

	@OnClick({
		R.id.btn_sure_to_reset_password
		,R.id.btn_get_phone_msg_number
		,R.id.tv_register_comment
		,R.id.ll_register_comment_root
	})
	public void onClick(View view){
		switch (view.getId()){

			case R.id.btn_sure_to_reset_password:
				// // TODO: 2017/4/25  需要去访问网络接口获取检验数据
				checkInput();
				break;
			case R.id.btn_get_phone_msg_number:
				getMsgNumber();
				break;
			case R.id.tv_register_comment:
				// 注册协议
				jumpToComment();
				break;
			case R.id.ll_register_comment_root:
				// 注册协议
				mCBAgree.setChecked(!mCBAgree.isChecked());
				break;
		}
	}

	@OnTextChanged(

		value = R.id.et_forget_password_phone

		//可以指定执行顺序,不影响原有逻辑
		, callback = OnTextChanged.Callback.TEXT_CHANGED
	)
	public void onTextChange(CharSequence s, int start, int count, int after){
		mPhoneNumberStatus.setVisibility(View.VISIBLE);
		String inputOne = mInputPhone.getText().toString().trim();
		boolean isRight = InputCheck.isPhoneNumber(inputOne);
		mPhoneNumberStatus.setImageResource(isRight ? R.mipmap.input_right : R.mipmap.input_err);
	}

	//跳转到
	private void jumpToComment(){
		//注册协议	/account/regagreement.aspx
		String url = "account/regagreement.aspx";
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, url);
		startActivity(intent);
	}

	int seconds = 180;
	//需要去访问接口
	// // TODO: 2017/4/25  需要去短信验证码
	private void getMsgNumber(){
		if(!mBtnMsgTime.isEnabled()){return;}
		mPhoneNumber = mInputPhone.getText().toString().trim();
		//手机号码不正确
		boolean isPhoneNumber = InputCheck.isPhoneNumber(mPhoneNumber);
		mPhoneNumberStatus.setImageResource(isPhoneNumber ? R.mipmap.input_right : R.mipmap.input_err);
		if(!isPhoneNumber){
			//请输入正确的手机号码
			DialogBean bean = new DialogBean("请输入正确的手机号码","","","");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}
		//开始获取验证码
		seconds = 180;
		startTimer();
		getPhoneCode();
	}

	private void getPhoneCode(){
		/**
		 * 	必选	类型及范围	说明
		 phone_number	是	string	手机号
		 type	是	int	类型：0注册，1找回密码，2手机登录

		 */
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		final HashMap<String, Object> map = new HashMap<>();
		map.put("phone_number",mPhoneNumber);
		map.put("type",0);
		/*LoginResult
		phone_number	是	string	用户号码
		type	是	int	类型：0账号登录，1手机登录
		password	是	string	用户密码或验证码*/
		service.getPhoneCode(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BaseReqResult>() {
				   @Override
				   public void onCompleted() {

				   }

				   @Override
				   public void onError(Throwable e) {
					   //有异常不处理
					   ToolLog.e("login getPhoneCode ",e.getMessage() + "--onError !");
					   //seconds = 1;
				   }

				   @Override
				   public void onNext(BaseReqResult model) {
					   ToolLog.e("login getPhoneCode ",model + "--请求完成 !");
					   if(model != null){
						   if(model.getStatus() == 0){
							   //重新计时
							   seconds = 1;
							   //ToastUtil.showToast(mActivity, model.getErrmsg());
							   DialogBean bean = new DialogBean(model.getErrmsg(),"","","");
							   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							   msgDialog.showDialog();
						   }else{
//							   ToolLog.e("login", "model : " + model);
							  // CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(model));
							   //ToastUtil.showToast(mActivity,"登录成功");
						   }
					   }

				   }
			   });
	}

	private Handler myHandler ;
	private void startTimer(){
		mBtnMsgTime.setText("获取验证码("+seconds+"s)");
		mBtnMsgTime.setEnabled(false);
		myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				seconds--;
				mBtnMsgTime.setText( "获取验证码(" + seconds + "s)");
				if(seconds <= 0){
					mBtnMsgTime.setText("获取验证码");
					mBtnMsgTime.setEnabled(true);
					return;
				}else{
					myHandler.postDelayed(this,1000L);
				}
			}
		},1000L);
	}


	private void verifyPhoneCode(final String msgNumber){
		/**
		 * 		必选	类型及范围	说明
		 phone_number	是	string	手机号
		 type	是	int	类型：0注册，1找回密码，2手机登录
		 code	是	string	验证码
		 */
		mPhoneNumber = mInputPhone.getText().toString().trim();
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		final HashMap<String, Object> map = new HashMap<>();
		map.put("phone_number",mPhoneNumber);
		map.put("type",0);
		map.put("code",msgNumber);
		/*LoginResult
		phone_number	是	string	用户号码
		type	是	int	类型：0账号登录，1手机登录
		password	是	string	用户密码或验证码*/
		service.checkPhoneCode(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BaseReqResult>() {
				   @Override
				   public void onCompleted() {

				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("verifyPhoneCode ",e.getMessage() + "--onError !");
				   }

				   @Override
				   public void onNext(BaseReqResult model) {
					   ToolLog.e("verifyPhoneCode ",model + "--请求完成 !");
					   if(model != null){
						   if(model.getStatus() == 0){
							   //ToastUtil.showToast(mActivity, model.getErrmsg());
							   DialogBean bean = new DialogBean(model.getErrmsg(),"","","");
							   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							   msgDialog.showDialog();
						   }else{
							   //							   ToolLog.e("login", "model : " + model);
							   // CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(model));
							   //ToastUtil.showToast(mActivity,"登录成功");
							   Intent intent = new Intent(mActivity, RegisterStep2Activity.class);
							   intent.putExtra(RegisterStep2Activity.PARAM_PHONE_NUMBER,mPhoneNumber);
							   intent.putExtra(RegisterStep2Activity.PARAM_PHONE_CODE,msgNumber);
							   startActivity(intent);
						   }
					   }
				   }
			   });
	}

	//检查输入项
	private void checkInput(){

		String phoneNumber = mInputPhone.getText().toString().trim();
		//手机号码不正确
		boolean isPhoneNumber = InputCheck.isPhoneNumber(phoneNumber);
		if(!isPhoneNumber){
			//请输入正确的手机号码
			DialogBean bean = new DialogBean("请输入正确的手机号码","","","");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}

		String msgNumber = mMsgNumber.getText().toString();
		//1)是否已经同意协议
		if(!mCBAgree.isChecked()){
			DialogBean bean = new DialogBean("请同意《注册协议》", "", "", "");
			//显示错误信息
			mMsgDialog = new CommonMsgDialog(mActivity, bean);
			mMsgDialog.showDialog();
			return;
		}

		if(TextUtils.isEmpty(msgNumber)){
			DialogBean bean = new DialogBean("请输入验证码", "", "", "");
			//显示错误信息
			mMsgDialog = new CommonMsgDialog(mActivity, bean);
			mMsgDialog.showDialog();
			return;
		}

		// 2)检查验证码
		verifyPhoneCode(msgNumber);

		//去验证验证码
		/*if(TextUtils.isEmpty(msgNumber)){
			DialogBean bean = new DialogBean("错误信息", "", "", "");
			//显示错误信息
			mMsgDialog = new CommonMsgDialog(mActivity, bean);
			mMsgDialog.showDialog();
		}else{
			//确认密码页面
			Intent intent = new Intent(mActivity, RegisterStep2Activity.class);
			startActivity(intent);
		}*/
	}

	//注册的手机号码
	private static final String REGISTER_PHONE = "register_phone";
	//注册码剩余的时间
	private static final String REGISTER_CODE = "register_code";
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(REGISTER_PHONE, mInputPhone.getText().toString().trim());
		outState.putInt(REGISTER_CODE,seconds);
	}

}
