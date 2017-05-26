package com.bowen.hannengclub.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.activity.HomeActivity;
import com.bowen.hannengclub.activity.SettingActivity;
import com.bowen.hannengclub.bean.MsgResult;
import com.bowen.hannengclub.bean.ShareBean;
import com.bowen.hannengclub.bean.UploadAvator;
import com.bowen.hannengclub.bean.UserInfo;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.network.UpLoadFile;
import com.bowen.hannengclub.popuwindow.SharePopupWindow;
import com.bowen.hannengclub.util.CacheUtils;
import com.bowen.hannengclub.util.GlideImageLoader;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolImage;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.ToolPhone;
import com.bowen.hannengclub.util.UserUtil;
import com.bowen.hannengclub.view.LineItemView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class MineFragment extends BaseFragment {

	//	如果不认证就不显示
	@BindView(R.id.fragment_iv_header)
	CircleImageView mHeadImage;
	@BindView(R.id.iv_mine_sex_type)
	ImageView       mSexType;
	@BindView(R.id.tv_mine_id_number)
	TextView        mIdNumber;
	@BindView(R.id.li_mine_fans)
	LineItemView    mMyFans;
	@BindView(R.id.li_mine_example)
	LineItemView    mExmaple;
	@BindView(R.id.li_mine_material)
	LineItemView    mMaterial;
	@BindView(R.id.ll_mine_note_root)
	LinearLayout    mRedAuthorNote;

	@BindView(R.id.ll_mine_note_text)
	TextView mTvRedAuthorNote;

	//认证等级
	@BindView(R.id.tv_mine_lv)
	TextView mAuthorLv;

	//用户的名字
	@BindView(R.id.tv_user_name)
	TextView mUserName;
	//认证状态
	@BindView(R.id.tv_mine_author_status)
	TextView mAuthorStatus;

	//用户年龄
	@BindView(R.id.tv_user_age)
	TextView mUserAge;

	//用户年龄
	@BindView(R.id.iv_mine_share)
	View mShareView;

	//已经谁的用户才显示的内容
	@BindView(R.id.mine_author_base_root)
	View mAuthorBase;

	//已经谁的用户才显示的内容
	@BindView(R.id.sl_mine_root)
	ScrollView mSlRoot;

	//谁看过我
	@BindView(R.id.li_seen_mine_people)
	View mSeeMe;
	//评论管理
	@BindView(R.id.li_mine_comment_manager)
	View mCommentManager;

	@BindView(R.id.mine_part_two_underline)
	View mTwoPartUnderLine;

	@BindView(R.id.mine_part_one_underline)
	View mOnePartUnderLine;

	//是否有消息的红点
	@BindView(R.id.mine_center_msg_point)
	View mMsgRedCircle;

	//图片选择器 参考 https://github.com/jeasonlzy/ImagePicker
	private ImagePicker imagePicker;
	private UserInfo    mUserInfo;

	protected Handler mHandler;

	@Override
	protected View initView() {
		View inflate = View.inflate(mActivity, R.layout.fragment_home_mine, null);
		return inflate;
	}

	//用户信息更新
	public final static String USER_INFO_UPDATE = "user_info_update";

	public BroadcastReceiver mineReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(USER_INFO_UPDATE)){
				upDateShow();
			}
		}
	};

	/**
	 * 注册广播,刷新头像等用户信息
	 */
	public void registerBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(USER_INFO_UPDATE);
		mActivity.registerReceiver(mineReceiver, filter);
	}

	@Override
	public void loadDataOnce() {
		//更新显示的状态,数据加载完成需要重新更新状态
		upDateShow();
		//用于选择图片
		imagePicker = ImagePicker.getInstance();
		//初始化图片加载器
		imagePicker.setImageLoader(new GlideImageLoader());

		//自动滚动到最顶部
		mSlRoot.smoothScrollTo(0,0);
	}

	@Override
	public void onResume() {
		super.onResume();
		upDateShow();
		getMessage();
	}


	@Override
	public void initData() {
		registerBroadcast();
		//gender	int	性别：-1 保密，0女，1男
		SexTypes.put(-1,R.mipmap.sex_unknow);
		SexTypes.put(0,R.mipmap.sex_woman);
		SexTypes.put(1,R.mipmap.sex_man);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what){
					case  UPDATESHOW:
						if(msg.obj != null &&
						   msg.obj instanceof  UploadAvator){
							handUploadResult((UploadAvator) msg.obj);
						}
						break;
				}
			}
		};
	}

	//更新要显示的内容
	private void handUploadResult(UploadAvator resultBean){
		if(resultBean.getStatus() == 0){
			//显示错误信息
			DialogBean bean = new DialogBean(resultBean.getErrmsg(),"", "", "");
			CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
			msgDialog.showDialog();
		}else{
			//更新头像信息
			final String avatar = resultBean.getItem().getAvatar();
			UserInfo userInfo = UserUtil.getUserInfo(mActivity);
			userInfo.setAvatar(avatar);
			userInfo.setAvatarstate(1);
			mUserInfo = userInfo;
			CacheUtils.setString(mActivity,SysConfiguration.USER_INFO,JSON.toJSONString(userInfo));
			ToolImage.glideDisplayHeaderImage(mActivity, mHeadImage,avatar);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ToolImage.glideDisplayHeaderImage(mActivity, mHeadImage,avatar);
				}
			},3000L);
		}
	}

	//更新显示
	protected final int UPDATESHOW = 1022;

	//	int[] SexTypes = new int[]{R.mipmap.sex_man};
	Map<Integer,Integer> SexTypes = new HashMap<>();
	private void upDateShow(){
		mUserInfo = UserUtil.getUserInfo(mActivity);
		if(mUserInfo != null){
			//1)打印头像
			ToolImage.displayLocalPic(mActivity, mHeadImage, mUserInfo.getAvatar());
			//2)姓名
			mUserName.setText(mUserInfo.getName());
			//3)性别
			mSexType.setImageResource(SexTypes.get(mUserInfo.getGender()));
			//4)id号
			mIdNumber.setText("ID:"+mUserInfo.getId_str());
			//5)认证状态,认证等级
			String author_str = mUserInfo.getAuthor_str();
			if("未认证".equals(author_str)){
				isAuthor = false;
				mTvRedAuthorNote.setVisibility(View.GONE);
				mShareView.setVisibility(View.GONE);
				mAuthorLv.setVisibility(View.GONE);
				mAuthorStatus.setVisibility(View.GONE);
				mAuthorBase.setVisibility(View.GONE);
				mSeeMe.setVisibility(View.GONE);
				mCommentManager.setVisibility(View.GONE);
				mOnePartUnderLine.setVisibility(View.GONE);
				mTwoPartUnderLine.setVisibility(View.GONE);

				mTvRedAuthorNote.setVisibility(View.VISIBLE);
				mRedAuthorNote.setVisibility(View.VISIBLE);
				mTvRedAuthorNote.setText(mUserInfo.getTipsmsg());
			}else{
				isAuthor = true;
				mAuthorStatus.setText(author_str);
				mAuthorStatus.setVisibility(View.VISIBLE);
				//分享的按钮
				mShareView.setVisibility(View.VISIBLE);
				mAuthorLv.setText(mUserInfo.getGrade_str());
				mAuthorBase.setVisibility(View.VISIBLE);
				mSeeMe.setVisibility(View.VISIBLE);
				mCommentManager.setVisibility(View.VISIBLE);
				mOnePartUnderLine.setVisibility(View.VISIBLE);
				mTwoPartUnderLine.setVisibility(View.VISIBLE);

				mTvRedAuthorNote.setVisibility(View.GONE);
				mRedAuthorNote.setVisibility(View.GONE);
			}
			//6)年龄
			mUserAge.setText(mUserInfo.getAge_str()+"岁");
			mUserAge.setVisibility(TextUtils.isEmpty(mUserInfo.getAge_str()) ? View.GONE : View.VISIBLE);
			upViewState();
		}
	}

	boolean isAuthor;

	//是否通过验证的显示状态
	private void upViewState() {
		int visible = isAuthor ? View.VISIBLE : View.GONE;
		mMyFans.setVisibility(visible);
		mExmaple.setVisibility(visible);
		mMaterial.setVisibility(visible);
		mAuthorLv.setVisibility(visible);
		mAuthorStatus.setVisibility(visible);
		mRedAuthorNote.setVisibility(isAuthor ? View.GONE : View.VISIBLE);
	}


	@OnClick({R.id.iv_mine_share,
			  R.id.iv_mine_msg,
			  R.id.li_seen_mine_people,
			  R.id.li_mine_fans,
			  R.id.li_mine_percent,
			  R.id.li_mine_example,
			  R.id.li_mine_material,
			  R.id.li_mine_collect,
			  R.id.li_mine_comment_manager,
			  R.id.tv_mine_exit_login,
			  R.id.ll_mine_head_info_root,
			  R.id.fragment_iv_header,
			  R.id.li_mine_setting,
			  R.id.li_mine_change_password
	})
	public void onClick(View view) {
		String url = "";
		/**
		 * 我的积分	/account/point.aspx
		 */
		switch (view.getId()) {
			case R.id.iv_mine_share:
				//Todo 分享我的名片
				shareMyInfo();
				break;
			case R.id.li_mine_setting:
				//Todo 设置页面
				settingPage();
				break;
			case R.id.fragment_iv_header:
				//Todo 选择上传头像
				getPhoneReq();
				break;
			case R.id.ll_mine_head_info_root:
				//个人信息	/account/view.aspx
				url = "account/view.aspx";
				break;
			case R.id.iv_mine_msg:
				// 消息	/account/message.aspx
				url = "account/message.aspx";
				break;
			case R.id.li_seen_mine_people:
				url = "account/look.aspx";
				//谁看过我
				break;
			case R.id.li_mine_fans:
				//我的粉丝
				url = "account/fans.aspx";
				break;
			case R.id.li_mine_percent:
				//我的积分
				url = "account/point.aspx";
				break;
			case R.id.li_mine_example:
				url = "account/case.aspx";
				//我的案例
				break;
			case R.id.li_mine_material:
				//我的素材
				url = "account/material.aspx";
				break;
			case R.id.li_mine_collect:
				// 我的收藏	/account/favorite_case.aspx
				url = "account/favorite_case.aspx";
				break;
			case R.id.li_mine_comment_manager:
				//评论管理
				url = "account/comment.aspx";
				break;
			case R.id.li_mine_change_password:
//				修改密码	/account/password.aspx
				url = "account/password.aspx";
				break;
			case R.id.tv_mine_exit_login:
				//退出登录，登录按钮
				jumpToLogin();
				break;
		}
		//如果URL为空直接返回
		if(TextUtils.isEmpty(url)){return;}
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, url);
		startActivity(intent);
	}

	//设置页面
	private void settingPage(){
		// TODO: 2017/5/24
		Intent intent = new Intent(mActivity, SettingActivity.class);
		startActivity(intent);
	}

	//分享我的名片
	private void shareMyInfo() {
		ShareBean shareBean = mUserInfo.getShare();
		if(shareBean == null){return;}
		UMShareListener umShareListener = new UMShareListener() {
			@Override
			public void onStart(SHARE_MEDIA share_media) {
				//开始分享
			}

			@Override
			public void onResult(SHARE_MEDIA share_media) {
				WindowManager.LayoutParams
					params = mActivity.getWindow().getAttributes();
				params.alpha=1f;
				mActivity.getWindow().setAttributes(params);
			}

			@Override
			public void onError(SHARE_MEDIA share_media, Throwable throwable) {
				//分享错误
			}

			@Override
			public void onCancel(SHARE_MEDIA share_media) {
				//用户取消分享
				ToastUtil.showToast(mActivity,"用户取消了分享");
			}
		};
		//public SharePopupWindow(final Activity mContext, ShareBean shareBean, UMShareListener itemsOnClick)
		new SharePopupWindow(mActivity,shareBean,umShareListener);
		/**
		 *   UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
		 UMImage image = new UMImage(ShareActivity.this, file);//本地文件
		 UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
		 UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
		 UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
		 */
		/*UMImage image = new UMImage(mActivity,shareBean.getPic());//网络图片
		UMWeb web = new UMWeb(shareBean.getBackurl());
		web.setTitle(shareBean.getTitle());//标题
		web.setThumb(image);  //缩略图
		web.setDescription(shareBean.getContent());//描述

		//SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
		//		SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
		ShareBoardConfig config = new ShareBoardConfig();
		config.setTitleText("分享");
		//不显示指示器
		config.setIndicatorVisibility(false);
		config.setCancelButtonText("取消分享");
		new ShareAction(mActivity).withMedia(web)
								  .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE,
												  SHARE_MEDIA.WEIXIN,
												  SHARE_MEDIA.SINA,
												  SHARE_MEDIA.QQ,
												  SHARE_MEDIA.QZONE
								  )
								  .setCallback(umShareListener).open(config);*/
	}

	//跳转到登录页面
	private void jumpToLogin() {
//		Intent intent = new Intent(mActivity, LoginActivity.class);
//		startActivity(intent);
		DialogBean bean = new DialogBean("您确定退出登录？", "", "确定", "取消");
		CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
		msgDialog.setLeftClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//将数据清理，退出登录
				CacheUtils.setString(mActivity,SysConfiguration.USER_INFO,"");
				//第三方登录需要删除授权
				deleteThirdPlatFromOuthor();
				upDateShow();
				Intent intent = new Intent(HomeActivity.LOGIN_OUT);
				mActivity.sendBroadcast(intent);
			}
		});
		msgDialog.showDialog();
	}

	//删除授权
	private void deleteThirdPlatFromOuthor() {
		UMShareAPI.get(mActivity).deleteOauth(mActivity, SHARE_MEDIA.QQ, authListener);
		UMShareAPI.get(mActivity).deleteOauth(mActivity, SHARE_MEDIA.WEIXIN, authListener);
	}

	UMAuthListener authListener = new UMAuthListener() {
		@Override
		public void onStart(SHARE_MEDIA platform) {

		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			//获取删除授权
			ToolLog.e("main",t.getMessage());
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {

		}
	};

	//解决授权问题
	private void getPhoneReq(){

		//请求授权,权限框架
		new RxPermissions(mActivity)
			.request(
				Manifest.permission.CAMERA,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
//				Manifest.permission.ACCESS_FINE_LOCATION
			)
			.subscribe(new Action1<Boolean>() {
				@Override
				public void call(Boolean granted) {
					if (granted) { // 在android 6.0之前会默认返回true
						// 已经获取权限
						if(mUserInfo.getAvatarstate() == 0){
							selectPic();
						}else{
							// 未获取权限
							DialogBean bean = new DialogBean("头像正在审核中", "", "确定", "");
							CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
							msgDialog.showDialog();
						}
					} else {
						// 未获取权限
						DialogBean bean = new DialogBean("摄像头打开失败，请重试", "", "确定", "");
						CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
						msgDialog.showDialog();
					}
				}
			});
	}


	/**
	 * 去选择图片,设置一些参数信息
	 */
	private void selectPic(){
		int width = (int) (ToolPhone.getScreenWidth(mActivity) / 1.5f + 0.5f);
		//需要考虑苹果手机,输出相素
		int outPutX = 1024;
		imagePicker.setMultiMode(false); //表示单选
		imagePicker.setShowCamera(true);  //显示拍照按钮
		imagePicker.setCrop(true);        //允许裁剪（单选才有效）
		imagePicker.setSaveRectangle(true); //是否按矩形区域保存
		imagePicker.setSelectLimit(1);    //选中数量限制
		imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状(矩形)
		imagePicker.setFocusWidth(width );   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
		imagePicker.setFocusHeight(width);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
		imagePicker.setOutPutX(outPutX);//保存文件的宽度。单位像素
		imagePicker.setOutPutY(outPutX);//保存文件的高度。单位像素

		Intent intent = new Intent(mActivity, ImageGridActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this**/
		// mShareAPI.onActivityResult(requestCode, resultCode, data);
		Log.d("result", "onActivityResult");
		UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {

			case ImagePicker.RESULT_CODE_ITEMS: //表示从相册选择
				if (data != null && requestCode == 100) {
					ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
					if(images != null && images.size() != 0){
						final String path = images.get(0).path;
						ToolLog.e("main",path + " ::: path");
						File file = new File(path);
						if(file != null && file.exists()){
							String uploadUrl = SysConfiguration.BASE_URL + "user/profile/avatar";
							UpLoadFile.upFile(file, uploadUrl, new Callback() {
								@Override
								public void onFailure(Call call, IOException e) {
									ToolLog.i("upload onFailure : " + e.getMessage());
									mActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											ToastUtil.showToast(mActivity,"头像上传失败");
											DialogBean bean = new DialogBean("头像上传失败","", "", "");
											CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
											msgDialog.showDialog();
										}
									});
								}

								@Override
								public void onResponse(Call call, Response response) throws IOException {
									//有返回值
									String result = response.body().string();
									ToolLog.i("upload success : " + result);
									if(!TextUtils.isEmpty(result)){
										UploadAvator resultBean = JSON.parseObject(result, UploadAvator.class);
										if(mHandler != null){
											Message message = mHandler.obtainMessage();
											message.what = UPDATESHOW;
											message.obj = resultBean;
											mHandler.sendMessage(message);
										}
									}
								}
							});
							//ToolImage.displayLocalPic(mActivity,mHeadImage,path);
						}
					}
				}
				break;
		}
	}

	@Override
	public void onDestroy() {
		if(mineReceiver != null){
			mActivity.unregisterReceiver(mineReceiver);
		}
		super.onDestroy();
	}

	//获取消息
	private void getMessage() {
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		Map<String,Object> param = new HashMap<>();
		param.put("msg_type",0);
		service.personalMessage(param)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<String>() {
				   @Override
				   public void onCompleted() {
					   ToolLog.e("main checkForUpdate", "请求完成 !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main personalMessage",e.getMessage() + "请求完成 !");
				   }

				   @Override
				   public void onNext(String model) {
					   ToolLog.e("main checkForUpdate", " onNext model : " + model);
						mMsgRedCircle.setVisibility(View.GONE);
					   if(!TextUtils.isEmpty(model)){
						   MsgResult result = JSON.parseObject(model, MsgResult.class);
						   if(result != null && result.getStatus() == 1){
							   if(result.getItem() != null
								  && result.getItem().getHasread() == 1){
								   mMsgRedCircle.setVisibility(View.VISIBLE);
							   }
						   }
					   }
				   }
			   });
	}
}
