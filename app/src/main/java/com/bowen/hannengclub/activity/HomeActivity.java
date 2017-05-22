package com.bowen.hannengclub.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.bowen.hannengclub.fragment.MineFragment;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.CacheUtils;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.UserUtil;
import com.bowen.hannengclub.view.NoScrollViewPager;
import com.pgyersdk.update.PgyUpdateManager;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
						   //更新的广播
						   sendBroadcast(new Intent(MineFragment.USER_INFO_UPDATE));
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

									   //请求授权,权限框架
									   new RxPermissions(mActivity).request(
										   //					 Manifest.permission.CAMERA,
										   Manifest.permission.WRITE_EXTERNAL_STORAGE,
										   Manifest.permission.READ_EXTERNAL_STORAGE
									   ).subscribe(new Action1<Boolean>() {
										   @Override
										   public void call(Boolean granted) {
											   if (granted) { // 在android 6.0之前会默认返回true
												   // 已经获取权限
												   // 1)开始下载,到服务
												   String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + FILENAME;
												   File file = new File(downloadPath);
												   if(file.exists()){
													   file.delete();
												   }
												   //只是为了适配7.0
												   PgyUpdateManager.register(mActivity,"com.bowen.hannengclub.fileprovider");
													startDownloadTask(mActivity,item.getDown_url());
												   /* 正常的操作
												   if(Build.VERSION.SDK_INT >= 24){
													   //使用管理器下载
													   downloadAPK(item.getDown_url(),FILENAME);
												   }else{
												   		startDownloadTask(mActivity,item.getDown_url());
												   }*/
											   } else {
												   // 未获取权限
												   ToastUtil.showToast(mActivity, "获取授权失败");
											   }
										   }
									   });

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

	private final String FILENAME = "hanneng_new_viersion";
	//下载管理器
	DownloadManager downloadManager ;
	long mTaskId;
	//使用系统下载器下载
	private void downloadAPK(String versionUrl, String versionName) {
		//创建下载任务
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
		request.setAllowedOverRoaming(false);//漫游网络是否可以下载

		//设置文件类型，可以在下载结束后自动打开该文件
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
		request.setMimeType(mimeString);

		//在通知栏中显示，默认就是显示的
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		request.setVisibleInDownloadsUi(true);

		//sdcard的目录下的download文件夹，必须设置
		request.setDestinationInExternalPublicDir("/download/", versionName);
		//request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

		//将下载请求加入下载队列
		downloadManager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
		//加入下载队列后会给该任务返回一个long型的id，
		//通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
		mTaskId = downloadManager.enqueue(request);

		//注册广播接收者，监听下载状态
		mActivity.registerReceiver(down_receiver,
								  new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	//广播接受者，接收下载状态
	private BroadcastReceiver down_receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			checkDownloadStatus();//检查下载状态
		}
	};

	//检查下载状态
	private void checkDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
		Cursor c = downloadManager.query(query);
		if (c.moveToFirst()) {
			int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
			switch (status) {
				case DownloadManager.STATUS_PAUSED:
					ToolLog.i(">>>下载暂停");
				case DownloadManager.STATUS_PENDING:
					ToolLog.i(">>>下载延迟");
				case DownloadManager.STATUS_RUNNING:
					ToolLog.i(">>>正在下载");
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					ToolLog.i(">>>下载完成");
					//下载完成安装APK
					String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + FILENAME;
					installAPK(new File(downloadPath));
					break;
				case DownloadManager.STATUS_FAILED:
					ToolLog.i(">>>下载失败");
					break;
			}
		}
	}

	//http://blog.csdn.net/yulianlin/article/details/52775160
	//下载到本地后执行安装
	protected void installAPK(File file) {
		if (!file.exists()) return;
		/*Uri apkUri =
			FileProvider.getUriForFile(mActivity, "com.bowen.hannengclub.fileprovider", file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// 由于没有在Activity环境下启动Activity,设置下面的标签
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//添加这一句表示对目标应用临时授权该Uri所代表的文件
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		startActivity(intent);*/

		Intent intent = new Intent(Intent.ACTION_VIEW);
		// 由于没有在Activity环境下启动Activity,设置下面的标签
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
			//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
			Uri apkUri =
				FileProvider.getUriForFile(mActivity, "com.bowen.hannengclub.fileprovider", file);
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		}else{
			intent.setDataAndType(Uri.fromFile(file),
								  "application/vnd.android.package-archive");
		}
		startActivity(intent);
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
