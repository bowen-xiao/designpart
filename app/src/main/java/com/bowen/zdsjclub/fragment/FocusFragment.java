package com.bowen.zdsjclub.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.util.ToolImage;
import com.bowen.zdsjclub.util.ToolLog;
import com.bowen.zdsjclub.view.LineItemView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class FocusFragment extends BaseFragment {

	@BindView(R.id.li_design_circle)
	LineItemView mDesignCircle;

	@BindView(R.id.ll_focus_root)
	LinearLayout mFocusRoot;

	@BindView(R.id.ll_loading_view_root)
	LinearLayout mLoadRoot;
	@BindView(R.id.iv_loading_view)
	ImageView    mIvLoad;

	@Override
	protected View initView() {
		View inflate = View.inflate(mActivity, R.layout.fragment_home_focus, null);
		return inflate;
	}

	@Override
	public void loadDataOnce() {
		ToolImage.loading(mActivity, mIvLoad);
		//第一次可见，可以去加载数据
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mFocusRoot.setVisibility(View.VISIBLE);
				mLoadRoot.setVisibility(View.GONE);
				//mLoadViewRoot.setVisibility(View.GONE);
			}
		}, 5000);
	}

	@OnClick({R.id.li_design_circle,
			  R.id.li_design_club_dynamic,
			  R.id.li_design_same_city,
			  R.id.li_design_myfocus,
			  R.id.li_design_focus_activity,
			  R.id.li_design_focus_subject})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.li_design_circle:
				ToolLog.e("onclick", view.getId() + " : id");
				break;
			case R.id.li_design_club_dynamic:
				break;
			case R.id.li_design_same_city:
				break;
			case R.id.li_design_myfocus:
				break;
			case R.id.li_design_focus_activity:
				break;
			case R.id.li_design_focus_subject:
				break;
		}
	}

}
