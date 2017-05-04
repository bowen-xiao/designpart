package com.bowen.hannengclub.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.bean.LoginResult;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.dialog.LoginErrDialog;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.CacheUtils;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

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
		,R.id.iv_wechat_login
		,R.id.iv_qq_login
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
			case R.id.iv_wechat_login:
				thirdLogin(SHARE_MEDIA.WEIXIN);
				break;
			case R.id.iv_qq_login:
				thirdLogin(SHARE_MEDIA.QQ);
				break;
		}
	}

	//微信登录  http://dev.umeng.com/social/android/login-page#1
	private void thirdLogin(SHARE_MEDIA loginType){
		UMShareAPI.get(this).getPlatformInfo(this, loginType, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA share_media) {
				mLoaddingRoot.setVisibility(View.VISIBLE);
				mLoaddingText.setText("获取授权中...");
				ToolLog.e("thirdlogin","start_login");
			}

			@Override
			public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

				/**
					 *
					 UShare封装后字段名	QQ原始字段名	微信原始字段名	新浪原始字段名	字段含义	备注
					 uid	openid	unionid	id	用户唯一标识	如果需要做跨APP用户打通，QQ需要使用unionID实现
					 name	screen_name	screen_name	screen_name	用户昵称
					 gender	gender	gender	gender	用户性别	该字段会直接返回男女
					 iconurl	profile_image_url	profile_image_url	profile_image_url	用户头像
				 */
				if(map != null){
					for (Map.Entry<String, String> entry : map.entrySet()) {
						System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
					}
					int type = 1;
					if(share_media.equals(SHARE_MEDIA.QQ)){
						type = 2;
					}
					//第三登录返回的信息
					String uid = map.get("uid");
					String name = map.get("name");
					String gender = map.get("gender");
					String iconurl = map.get("iconurl");
					String openid = map.get("openid");
					/**
					 * 	必选	类型及范围	说明
					 type	是	int	类型：1微信，2 QQ
					 third_unionid	是	string	微信或QQ的unionid
					 third_openid	是	string	微信或QQ的openid
					 third_nickname	是	string	微信或QQ的昵称
					 */
					HashMap<String, Object> reqParam = new HashMap<>();
					reqParam.put("type",type);
					reqParam.put("third_unionid",uid);
					reqParam.put("third_openid",openid);
					reqParam.put("third_nickname",name);
					reqParam.put("third_nickname",name);
					//参数名称
					reqParam.put("unionid",uid);
					reqParam.put("openid",openid);

					thirdLogin(reqParam);
				}else{
					mLoaddingRoot.setVisibility(View.GONE);
				}
			}

			@Override
			public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
				ToolLog.e("thirdlogin",throwable.getMessage());
			}

			@Override
			public void onCancel(SHARE_MEDIA share_media, int i) {
				ToastUtil.showToast(mActivity,"用户取消授权");
			}
		});

	}

	//检查第三方的信息
	private void thirdLogin(Map<String, Object> map) {
		//打印参数名称
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			ToolLog.e("login","Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.thirdLogin(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<String>() {
				   @Override
				   public void onCompleted() {
					   mLoaddingRoot.setVisibility(View.GONE);
					   ToolLog.e("main thirdLogin", "请求完成 !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main thirdLogin",e.getMessage() + "请求完成 !");
				   }

				   @Override
				   public void onNext(String model) {
					   ToolLog.e("main thirdLogin", "请求完成 !" + model);
				   }
			   });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
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
		//这里应该是为 1
		map.put("type",0);
		map.put("password",inputPassword);
		/*LoginResult
		phone_number	是	string	用户号码
		type	是	int	类型：0账号登录，1手机登录
		password	是	string	用户密码或验证码*/
		service.login(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<LoginResult>() {
				   @Override
				   public void onCompleted() {
					   mBtnLogin.setEnabled(true);
					   //ToolLog.e("login", "请求完成 !");
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
				   public void onNext(LoginResult model) {
					   ToolLog.e("login reslut ",model + "--请求完成 !");
					   if(model != null){
						   ToolLog.e("login reslut ",model.toString() + "--请求完成 !");
						   if(model.getStatus() == 0){
							   //ToastUtil.showToast(mActivity, model.getErrmsg());
							   DialogBean bean = new DialogBean(model.getErrmsg(),"","","");
							   CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							   msgDialog.showDialog();
						   }else{
//							   ToolLog.e("login", "model : " + model);
							   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(model.getItem()));
							   ToastUtil.showToast(mActivity,"登录成功");
							   finish();
						   }
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
