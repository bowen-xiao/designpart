package com.bowen.hannengclub.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.adapter.NewHomeFragmentFactory;
import com.bowen.hannengclub.adapter.NewHomePageAdapter;
import com.bowen.hannengclub.bean.UserInfo;
import com.bowen.hannengclub.util.UserUtil;
import com.bowen.hannengclub.view.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by 肖稳华 on 2017/4/20.
 * Web X5 //http://x5.tencent.com/tbs/guide.html
 */
public class HomeFragment extends BaseFragment {

	@BindView(R.id.home_page_index)
	PagerSlidingTabStrip mPagerIndex;

	@BindView(R.id.iv_home_sign)
	ImageView mIvSign;

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
	}

	@Override
	public void onResume() {
		super.onResume();
		//默认值
		mIvSign.setImageResource(R.mipmap.remarked);
		UserInfo info = UserUtil.getUserInfo(mActivity);
		if(info != null){
			//是否已经签到的标识
			mIvSign.setImageResource(info.getSign_status() == 0 ? R.mipmap.unremark : R.mipmap.remarked);
		}
	}


	@OnClick(R.id.ll_home_sign_root)
	public void signClick(View view){
		// 签到的点击事件
		switch (view.getId()){
			case R.id.ll_home_sign_root:
				//签到按钮
				jumpTosign();
				break;
		}
	}

	//去签到
	private void jumpTosign() {
		UserInfo info = UserUtil.getUserInfo(mActivity);
		if(info == null || info.getSign_status() == 1){
			return;
		}
		///account/sign.aspx
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, "account/sign.aspx");
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		//必须清除
		NewHomeFragmentFactory.clearCaches();
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		int currentItem = mViewPager.getCurrentItem();
		BaseFragment item = mAdapter.getItem(currentItem);
		if(item != null){
			item.onActivityResult(requestCode,resultCode,data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
