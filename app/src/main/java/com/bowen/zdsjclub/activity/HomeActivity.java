package com.bowen.zdsjclub.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.fragment.FragmentFactory;
import com.bowen.zdsjclub.fragment.HomePagerAdapter;
import com.bowen.zdsjclub.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
	}

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
}
