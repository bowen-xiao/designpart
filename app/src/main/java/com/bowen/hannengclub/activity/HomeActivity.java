package com.bowen.hannengclub.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.fragment.BaseFragment;
import com.bowen.hannengclub.fragment.FragmentFactory;
import com.bowen.hannengclub.fragment.HomePagerAdapter;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.UserUtil;
import com.bowen.hannengclub.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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

		uploadPushId();
		registerBroadcast();
	}


	public final static String LOGIN_OUT = "user_login_out";
	/**
	 * 注册广播,刷新头像等用户信息
	 */
	public void registerBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LOGIN_OUT);
		registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (LOGIN_OUT.equals(intent.getAction())) {
				//默认选中第一个
				mViewPager.setCurrentItem(0);
				mBottomItems.check(R.id.rb_home_bottom_first);
			}
		}};

	//提交pushId
	private void uploadPushId(){

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		HashMap<String, Object> map = new HashMap<>();
		map.put("key","3b4f7bad8821b7354bd697f7a5096b6c7ba2aae7");
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
			DialogBean dialogBean = new DialogBean("你尚未登录您的账号，前往登录！", "", "取消", "前去登录");
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
