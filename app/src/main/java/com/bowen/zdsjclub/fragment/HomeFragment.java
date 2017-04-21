package com.bowen.zdsjclub.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.util.ToolImage;

import butterknife.BindView;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class HomeFragment extends BaseFragment {


	@BindView(R.id.ll_loading_view_root)
	LinearLayout mLoadRoot;
	@BindView(R.id.iv_loading_view)
	ImageView    mIvLoad;

	@Override
	protected View initView() {
		View inflate = View.inflate(mActivity, R.layout.fragment_home_first, null);
		return inflate;
	}

	@Override
	public void initData() {
		ToolImage.loading(mActivity, mIvLoad);
	}

	@Override
	public void loadDataOnce() {
		//可以开始加载数据

	}
}
