package com.bowen.hannengclub.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.adapter.NewHomePageAdapter;
import com.bowen.hannengclub.view.PagerSlidingTabStrip;

import butterknife.BindView;


/**
 * Created by 肖稳华 on 2017/4/20.
 * Web X5 //http://x5.tencent.com/tbs/guide.html
 */
public class HomeFragment extends BaseFragment {

	@BindView(R.id.home_page_index)
	PagerSlidingTabStrip mPagerIndex;

	@BindView(R.id.vp_home_pager)
	ViewPager mViewPager;
	private NewHomePageAdapter mAdapter;

	@Override
	protected View initView() {

		View inflate = View.inflate(mActivity, R.layout.fragment_new_home_page, null);
		return inflate;
	}

	@Override
	public void initData() {
		mPagerIndex.setShouldExpand(true);
		//设置分割线颜色为透明
		mPagerIndex.setDividerColor(Color.TRANSPARENT);
		mPagerIndex.setTextColor(getResources().getColor(R.color.tab_text_normal), getResources().getColor(R.color.tab_text_selected));

		mAdapter = new NewHomePageAdapter(getChildFragmentManager(), mActivity);

		//存活数量
		mViewPager.setOffscreenPageLimit(5);

		mViewPager.setAdapter(mAdapter);

		//设置绑定的对象
		mPagerIndex.setViewPager(mViewPager);
		mViewPager.setCurrentItem(0);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mAdapter.getItem(0).loadDataOnce();
			}
		}, 1500L);
	}
}
