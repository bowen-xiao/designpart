package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.bean.BaseReqResult;
import com.bowen.hannengclub.bean.LoginResult;
import com.bowen.hannengclub.bean.ThirdLoginParam;
import com.bowen.hannengclub.bean.UserInfo;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.CacheUtils;
import com.bowen.hannengclub.util.InputCheck;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 第三方登录绑定信息
 */
public class ThirdLoginBindActivity extends BaseActivity {

	@BindView(R.id.iv_input_phone_status)
	ImageView mInputStatus;

	@BindView(R.id.et_forget_password_phone)
	EditText mInputPhone;

	//et_forget_password_msg_recevie
	@BindView(R.id.et_forget_password_msg_recevie)
	EditText mPhoneCode;

	@BindView(R.id.et_third_login_password)
	EditText mPassword;

	//获取验证码
	@BindView(R.id.btn_get_phone_msg_number)
	Button mBtnPhoneCode;
	//获取验证码
	@BindView(R.id.rl_third_login_bind)
	View mPasswordLine;

	//绑定的按钮
	@BindView(R.id.btn_sure_to_reset_password)
	Button mBtnSure;

	//绑定的按钮
	@BindView(R.id.tv_back_login)
	View mBackLogin;

	//倒计时
	private int seconds;
	private String mPhoneNumber = "";
	private ThirdLoginParam param;

	@Override
	public int getContextViewId() {
		//rl_third_login_bind
		return R.layout.activity_forget_password;
	}

	@Override
	public void initData() {
		param = (ThirdLoginParam) getIntent().getSerializableExtra("param");
		myHandler = new Handler();
		//设置其显示项
		mInputStatus.setVisibility(View.GONE);
		mBackLogin.setVisibility(View.GONE);
		mPasswordLine.setVisibility(View.VISIBLE);
		mBtnSure.setText("绑定账号");
	}

	@OnClick(
		{
			R.id.btn_sure_to_reset_password
			,R.id.btn_get_phone_msg_number
		}
	)
	public void btnClick(View view){
		switch (view.getId()){
			case R.id.btn_sure_to_reset_password:
				//点击事件处理
				postFrom();
				break;
			case R.id.btn_get_phone_msg_number:
				//获取验证码
				getPhoneCode();
				break;
		}
	}

	//获取验证码
	private void getPhoneCode(){
		if(!mBtnPhoneCode.isEnabled()){return;}
		mPhoneNumber = mInputPhone.getText().toString().trim();
		//手机号码不正确
		boolean isPhoneNumber = InputCheck.isPhoneNumber(mPhoneNumber);
		if(!isPhoneNumber){
			//请输入正确的手机号码
			DialogBean bean = new DialogBean("请输入正确的手机号码", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}
		//开始获取验证码
		seconds = 180;
		startTimer();
		//从网络获取
		getPhoneCodeFrom();
	}

	private void getPhoneCodeFrom(){
		/**
		 * 	必选	类型及范围	说明
		 type	是	int	类型：0注册，1找回密码，2手机登录，3第三方绑定
		 */
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		final HashMap<String, Object> map = new HashMap<>();
		map.put("phone_number",mPhoneNumber);
		map.put("type",3);
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
					   ToolLog.e("login getPhoneCode ", e.getMessage() + "--onError !");
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
		mBtnPhoneCode.setText("获取验证码("+seconds+"s)");
		mBtnPhoneCode.setEnabled(false);
		myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				seconds--;
				mBtnPhoneCode.setText( "获取验证码(" + seconds + "s)");
				if(seconds <= 0){
					mBtnPhoneCode.setText("获取验证码");
					mBtnPhoneCode.setEnabled(true);
					return;
				}else{
					myHandler.postDelayed(this,1000L);
				}
			}
		},1000L);
	}

	//提交数据
	private void postFrom(){
		// 1)检查输入项
		boolean isPhoneNumber = !TextUtils.isEmpty(mPhoneNumber) && InputCheck.isPhoneNumber(mPhoneNumber);
		if(!isPhoneNumber){
			//请输入正确的手机号码
			DialogBean bean = new DialogBean("请输入正确的手机号码", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}
		String phoneCode = mPhoneCode.getText().toString().trim();
		if(TextUtils.isEmpty(phoneCode)){
			DialogBean bean = new DialogBean("请输入手机验证码", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}
		String password = mPassword.getText().toString().trim();
		if(!InputCheck.isPassword(password)){
			DialogBean bean = new DialogBean("密码格式不正确", "", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
			return;
		}

		//2) 提交到后台
		bindPhone(password,phoneCode);
	}

	private void bindPhone(String password,String phoneCode){
		if(param != null){
			/**
			 * 	必选	类型及范围	说明
			 type	是	int	类型：1微信，2 QQ
			 third_unionid	是	string	微信或QQ的unionid
			 third_openid	是	string	微信或QQ的openid
			 third_nickname	是	string	微信或QQ的昵称
			 phone_number	是	string	手机号
			 password	是	string	密码
			 code	是	string	手机验证码
			 */
			Map<String, Object> params = param.getParams();
			params.put("phone_number",mPhoneNumber);
			params.put("password",password);
			//手机验证码
			params.put("code",phoneCode);
			mLoaddingRoot.setVisibility(View.VISIBLE);
			mLoaddingText.setText("数据请求中...");
			mBtnSure.setEnabled(false);

			RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
			service.thirdbind(params)
				   .subscribeOn(Schedulers.io())
				   .observeOn(AndroidSchedulers.mainThread())
				   .subscribe(new Subscriber<String>() {
					   @Override
					   public void onCompleted() {
						   //关闭弹出的界面，释放按钮
						   mLoaddingRoot.setVisibility(View.GONE);
						   mBtnSure.setEnabled(true);
					   }

					   @Override
					   public void onError(Throwable e) {

					   }

					   @Override
					   public void onNext(String model) {

//						   LoginResult
						   ToolLog.e("thirdLogin","result : " + model);
						   if (!TextUtils.isEmpty(model)) {
							   LoginResult result = JSON.parseObject(model, LoginResult.class);
							   if(result.getStatus() == 1){
								   //登录成功,返回
								   UserInfo item = result.getItem();
								   if(item != null){
									   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(item));
									   ToastUtil.showToast(mActivity, "登录成功");
									   loginSuccess();
								   }
							   }else{
								   //显示错误信息
								   DialogBean bean = new DialogBean(result.getErrmsg(),"","","");
								   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
								   msgDialog.showDialog();
							   }
					   		}

					   }
				   });
		}
	}

	//绑定成功需要返回首页,关闭登录页面
	private void loginSuccess(){
		Intent intent = new Intent(LoginActivity.FINISH_LOGIN_ACTIVITY);
		sendBroadcast(intent);
		finish();
	}

}
