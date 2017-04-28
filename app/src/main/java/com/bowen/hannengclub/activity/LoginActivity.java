package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.dialog.LoginErrDialog;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.ToolLog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

	@BindView(R.id.login_margin)
	View mMarginTop;

	@BindView(R.id.btn_login)
	Button mBtnLogin;

	@BindView(R.id.et_login_phone_number)
	EditText mEtPhone;

	@BindView(R.id.et_login_password)
	EditText mEtPassword;

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
				//登录按钮
				//showLoginErr();
				login();
				break;
			case R.id.tv_forget_password:
				jumpToForgetPage();
				break;
			case R.id.tv_user_register:
				jumpToRegister();
				break;
		}
	}

	//提交pushId
	private void login(){
		// 1)数据有效性检查
		String inputPhone = mEtPhone.getText().toString().trim();
		String inputPassword = mEtPassword.getText().toString().trim();

		// 2)网络访问
		mLoaddingRoot.setVisibility(View.VISIBLE);
		mLoaddingText.setText("登录中...");
		mBtnLogin.setEnabled(false);
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		final HashMap<String, Object> map = new HashMap<>();
		map.put("phone_number",inputPhone);
		map.put("type",1);
		map.put("password",inputPassword);
		/*LoginResult
		phone_number	是	string	用户号码
		type	是	int	类型：0账号登录，1手机登录
		password	是	string	用户密码或验证码*/
		service.login(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<String>() {
				   @Override
				   public void onCompleted() {
					   mBtnLogin.setEnabled(true);
					   ToolLog.e("login", "请求完成 !");
					   mLoaddingRoot.setVisibility(View.GONE);
				   }

				   @Override
				   public void onError(Throwable e) {
					   mBtnLogin.setEnabled(true);
					   mLoaddingRoot.setVisibility(View.GONE);
					   //ToolLog.e("login",e.getMessage() + "请求完成 !");
					   DialogBean bean = new DialogBean("登录失败","","","");
					   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
					   msgDialog.showDialog();
				   }

				   @Override
				   public void onNext(String model) {
					   ToolLog.e("login reslut ",model + "--请求完成 !");
					   if(model != null){
						  /* if(model.getStatus() == 0){
							   ToastUtil.showToast(mActivity,model.getErrmsg());
							   DialogBean bean = new DialogBean(model.getErrmsg(),"","","");
							   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							   msgDialog.showDialog();
						   }else{
//							   ToolLog.e("login", "model : " + model);
							   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(model));
							   ToastUtil.showToast(mActivity,"登录成功");
						   }*/
					   }

				   }
			   });
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
