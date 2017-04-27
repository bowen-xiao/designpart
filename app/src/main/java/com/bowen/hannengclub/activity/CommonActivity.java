package com.bowen.hannengclub.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

	@Override
	public int getContextViewId() {
		return R.layout.activity_common;
	}

	@Override
	public void initData() {
		TAG = this.getClass().getSimpleName();
		final CommonFragment fragment = new CommonFragment();
		Bundle args = new Bundle();
		args.putString(CommonFragment.COMMON_URL,getIntent().getStringExtra(CommonFragment.COMMON_URL));
		fragment.setArguments(args);
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.activity_common_content_root, fragment, TAG);
		ft.commit();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				fragment.loadDataOnce();
			}
		},1000L);
		mTvRight.setText("关闭");
		mTvRight.setTextColor(Color.BLUE);
		mRightRoot.setVisibility(View.VISIBLE);
	}

}
