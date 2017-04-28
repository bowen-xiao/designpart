package com.bowen.hannengclub.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.fragment.CommonFragment;

import butterknife.BindView;

public class CommonActivity extends BaseActivity {

	private String TAG;

	@BindView(R.id.activity_common_content_root)
	FrameLayout mContentRoot;
	private CommonFragment mFragment;

	@Override
	public int getContextViewId() {
		return R.layout.activity_common;
	}

	@Override
	public void initData() {
		//标识
		TAG = this.getClass().getSimpleName();
		mFragment = new CommonFragment();
		Bundle args = new Bundle();
		args.putString(CommonFragment.COMMON_URL,getIntent().getStringExtra(CommonFragment.COMMON_URL));
		//添加这个标签就可以自动加载,不需要等到看见了再加载
		args.putInt("index",0);
		mFragment.setArguments(args);
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.activity_common_content_root, mFragment, TAG);
		ft.commit();
		mTvRight.setText("关闭");
		mTvRight.setTextColor(Color.BLUE);
		mRightRoot.setVisibility(View.VISIBLE);
	}

	@Override
	protected void leftClick() {
		//可以返回到上一页面,否则就返回
		if(mFragment != null && mFragment.canBack()){
			return;
		}
		super.leftClick();
	}
}
