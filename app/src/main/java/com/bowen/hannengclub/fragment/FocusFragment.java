package com.bowen.hannengclub.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.util.ToolImage;
import com.bowen.hannengclub.view.LineItemView;

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
		/**
		 * 关注
			 设计圈	/focus/dynamic.aspx
			 俱乐部动态	/focus/club.aspx
			 同城设计师	/designer/index.aspx
			 关注的设计师	/focus/designer.aspx
			 关注的活动	/focus/active.aspx
			 关注的专题	/focus/subject.aspx
		 */
		String url = "";
		switch (view.getId()) {
			case R.id.li_design_circle:
				//设计圈
//				ToolLog.e("onclick", view.getId() + " : id");
				url = "focus/club.aspx";
				break;
			case R.id.li_design_club_dynamic:
				//俱乐部动态
				url = "focus/dynamic.aspx";
				break;
			case R.id.li_design_same_city:
				// 同城设计师
				url = "designer/index.aspx";
				break;
			case R.id.li_design_myfocus:
				//关注的设计师
				url = "focus/designer.aspx";
				break;
			case R.id.li_design_focus_activity:
				url = "focus/active.aspx";
				break;
			case R.id.li_design_focus_subject:
				url = "focus/subject.aspx";
				break;
		}
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, SysConfiguration.BASE_URL + url);
		startActivity(intent);
	}

}
