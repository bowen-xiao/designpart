package com.bowen.zdsjclub.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.fragment.FragmentFactory;
import com.bowen.zdsjclub.fragment.HomePagerAdapter;
import com.bowen.zdsjclub.network.DataEngine2;
import com.bowen.zdsjclub.network.RxNetWorkService;
import com.bowen.zdsjclub.util.ToastUtil;
import com.bowen.zdsjclub.util.ToolLog;
import com.bowen.zdsjclub.view.NoScrollViewPager;

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
		//初始化数据
		initBottomMenus();
		//设置默认选中项
		mBottomItems.check(R.id.rb_home_bottom_first);

		mAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(ids.size());

		uploadPushId();
	}

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
					   ToolLog.e("main", "加载完成 !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main",e.getMessage() + "加载完成 !");
				   }

				   @Override
				   public void onNext(String model) {
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
		mBottomItems.check(view.getId());
		mViewPager.setCurrentItem(ids.indexOf(view.getId()));
	}

	@Override
	protected void onDestroy() {
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
}
