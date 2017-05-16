package com.bowen.hannengclub.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.bean.LoginResult;
import com.bowen.hannengclub.bean.UserInfo;
import com.bowen.hannengclub.bean.VersionResult;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.fragment.BaseFragment;
import com.bowen.hannengclub.fragment.FragmentFactory;
import com.bowen.hannengclub.fragment.HomePagerAdapter;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.CacheUtils;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.UserUtil;
import com.bowen.hannengclub.view.NoScrollViewPager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;


public class HomeActivity extends BaseActivity {

	@BindView(R.id.home_content_pager)
	NoScrollViewPager mViewPager;
	@BindView(R.id.rb_home_bottom_first)
	RadioButton       mBtnHome;
	@BindView(R.id.rb_home_bottom_example)
	RadioButton       mBtnExample;
	@BindView(R.id.rb_home_bottom_focus)
	RadioButton       mBtnFocus;
	@BindView(R.id.rb_home_bottom_design)
	RadioButton       mBtnDesign;
	@BindView(R.id.rb_home_bottom_mine)
	RadioButton       mBtnMine;
	@BindView(R.id.rg_home_bottom_items)
	RadioGroup        mBottomItems;
	@BindView(R.id.activity_home)
	LinearLayout      mHomeRoot;
	private HomePagerAdapter mAdapter;


	@Override
	public int getContextViewId() {
		return R.layout.activity_home;
	}

	@Override
	public void initData() {
		mTitleRoot.setVisibility(View.GONE);
		//初始化数据
		initBottomMenus();
		//设置默认选中项
		mBottomItems.check(R.id.rb_home_bottom_first);

		mAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(ids.size());
		//默认选中第一个
		mViewPager.setCurrentItem(0);

		registerBroadcast();

		//启用调试
		XGPushConfig.enableDebug(this, SysConfiguration.DEBUG);

		checkForUpdate();
	}

	private void getLoginInfo(){
		//如果token不存在就不同步
		String token = UserUtil.getToken(mActivity);
		if(TextUtils.isEmpty(token)){return;}
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		Map<String,Object> param = new HashMap<>();
		param.put("test","test");
		service.getLogin(param)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<LoginResult>() {
				   @Override
				   public void onCompleted() {

					   ToolLog.e("main getLogin", "onCompleted !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main getLogin",e.getMessage() + "请求完成 !");
				   }

				   @Override
				   public void onNext(LoginResult model) {
					   ToolLog.e("main getLogin", "onNext !" + model);
					   UserInfo item = model.getItem();
					   if(model.getStatus() == 1 && item != null){
						   //更新登录信息
						   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(model.getItem()));
					   }else{
						   //清空登录信息
						   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, "");
					   }
				   }
			   });
	}


	//检查更新
	private void checkForUpdate() {
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		Map<String,Object> param = new HashMap<>();
		param.put("test","test");
		service.getVersion(param)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<VersionResult>() {
				   @Override
				   public void onCompleted() {

					   ToolLog.e("main checkForUpdate", "请求完成 !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main checkForUpdate",e.getMessage() + "请求完成 !");
				   }

				   @Override
				   public void onNext(VersionResult model) {

					   ToolLog.e("main checkForUpdate", "checkForUpdate : " + model);
					   if(model != null){
						   if(model.getStatus() == 1){
							   final VersionResult.ItemBean item = model.getItem();
							   DialogBean dialogBean = new DialogBean(item.getMessage(),item.getTitle(),item.getUpdate(),item.getCancel());
							   CommonMsgDialog dialog = new CommonMsgDialog(mActivity, dialogBean);
							   dialog.setLeftClick(new View.OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   //去下载
									   ToolLog.e("main checkForUpdate", "下载地址 :: " + item.getDown_url());
									   // 1)开始下载,到服务
									   startDownloadTask(
										   mActivity,
										   item.getDown_url());
//									   Intent intent1 = new Intent(mActivity, UpLoadService.class);
////									   intent1.putExtra(UpLoadService.DOWNLOAD_RUL ,item.getDown_url());
//									   intent1.putExtra(UpLoadService.DOWNLOAD_RUL ,"http://xg.qq.com/pigeon_v2/resource/sdk/Xg-Push-SDK-Android-3.0.zip");
//									   startService(intent1);
								   }
							   });
							   dialog.showDialog();
						   }
					   }
				   }
			   });
	}

	@Override
	protected void onResume() {
		//更新用户的登录信息
		//同步用户信息
		getLoginInfo();

		super.onResume();
		UserInfo userInfo = UserUtil.getUserInfo(mActivity);
		String userid = "";
		if(userInfo != null && !TextUtils.isEmpty(userInfo.getId())){
			userid = userInfo.getId();
		}
		//根据用户ID绑定
		XGPushManager.registerPush(getApplicationContext(), userid,new XGIOperateCallback() {
			@Override
			public void onSuccess(Object o, int i) {
				uploadPushId();
			}

			@Override
			public void onFail(Object o, int i, String s) {

			}
		});

	}

	public final static String LOGIN_OUT = "user_login_out";
	public final static String UPDATE_ERR = "update_err";
	/**
	 * 注册广播,刷新头像等用户信息
	 */
	public void registerBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LOGIN_OUT);
		filter.addAction(UPDATE_ERR);
		registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (LOGIN_OUT.equals(intent.getAction())) {
				//默认选中第一个
				mViewPager.setCurrentItem(0);
				mBottomItems.check(R.id.rb_home_bottom_first);
			}else if(UPDATE_ERR.equals(intent.getAction())){
				Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
			}
		}};

	//提交pushId
	private void uploadPushId(){
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		HashMap<String, Object> map = new HashMap<>();
		map.put("key", XGPushConfig.getToken(mActivity));
		service.upLoadPushID(map)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<String>() {
				   @Override
				   public void onCompleted() {

					   ToolLog.e("main", "请求完成 !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main",e.getMessage() + "请求完成 !");
				   }

				   @Override
				   public void onNext(String model) {
					   ToolLog.e("main", "model : " + model);
				   }
			   });
	}

	//底部按钮的
	List<Integer> ids;

	private void initBottomMenus(){
		ids = new ArrayList<>();
		//添加数据项
		ids.add(R.id.rb_home_bottom_first);
		ids.add(R.id.rb_home_bottom_example);
		ids.add(R.id.rb_home_bottom_focus);
		ids.add(R.id.rb_home_bottom_design);
		ids.add(R.id.rb_home_bottom_mine);
	}

	@OnClick({R.id.rb_home_bottom_first,
			  R.id.rb_home_bottom_example,
			  R.id.rb_home_bottom_focus,
			  R.id.rb_home_bottom_design,
			  R.id.rb_home_bottom_mine})
	public void onClick(View view) {

		/*if(view.getId() == R.id.rb_home_bottom_example){
			DeleteDialog deleteDialog = new DeleteDialog(mActivity);
			deleteDialog.showDialog();
		}*/
		if(view.getId() == R.id.rb_home_bottom_mine && UserUtil.getUserInfo(mActivity) == null){
			mBottomItems.check(ids.get(mViewPager.getCurrentItem()));
			DialogBean dialogBean = new DialogBean("您尚未登录您的账号，前往登录！", "", "取消", "前去登录");
			CommonMsgDialog commonMsgDialog = new CommonMsgDialog(mActivity, dialogBean);
			commonMsgDialog.setRightClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//去登录页面
					Intent intent = new Intent(mActivity, LoginActivity.class);
					startActivity(intent);
				}
			});
			commonMsgDialog.showDialog();
			return;
		}
		mBottomItems.check(view.getId());
		mViewPager.setCurrentItem(ids.indexOf(view.getId()));
	}

	@Override
	protected void onDestroy() {
		if(receiver != null){
			unregisterReceiver(receiver);
		}
		FragmentFactory.clearCaches();
		super.onDestroy();
	}

	private long exitPress = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
			&& event.getAction() == KeyEvent.ACTION_DOWN) {
			//如果在显示先进行取消
			ToastUtil.cancelToast();
			if ((System.currentTimeMillis() - exitPress) > 2000 && mActivity != null) {
				//需要先关闭其它的toast
				ToastUtil.showToast(mActivity, "再按一次退出应用");
				exitPress = System.currentTimeMillis();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int currentItem = mViewPager.getCurrentItem();
		BaseFragment item = mAdapter.getItem(currentItem);
		if(item != null){
			item.onActivityResult(requestCode,resultCode,data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
