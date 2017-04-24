package com.bowen.zdsjclub.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.activity.LoginActivity;
import com.bowen.zdsjclub.view.LineItemView;

import de.hdodenhof.circleimageview.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class MineFragment extends BaseFragment {

	//	如果不认证就不显示
	@BindView(R.id.fragment_iv_header)
	CircleImageView mHeadImage;
	@BindView(R.id.iv_mine_sex_type)
	ImageView       mSexType;
	@BindView(R.id.tv_mine_id_number)
	TextView        mIdNumber;
	@BindView(R.id.li_mine_fans)
	LineItemView    mMyFans;
	@BindView(R.id.li_mine_example)
	LineItemView    mExmaple;
	@BindView(R.id.li_mine_material)
	LineItemView    mMaterial;
	@BindView(R.id.ll_mine_note_root)
	LinearLayout    mAuthorNote;

	//认证等级
	@BindView(R.id.tv_mine_lv)
	TextView mAuthorLv;
	//认证状态
	@BindView(R.id.tv_mine_author_status)
	TextView mAuthorStatus;


	@Override
	protected View initView() {
		View inflate = View.inflate(mActivity, R.layout.fragment_home_mine, null);
		return inflate;
	}

	@Override
	public void loadDataOnce() {
		//更新显示的状态,数据加载完成需要重新更新状态
		upViewState();
	}

	boolean isAuthor;

	//是否通过验证的显示状态
	private void upViewState() {
		int visible = isAuthor ? View.VISIBLE : View.GONE;
		mMyFans.setVisibility(visible);
		mExmaple.setVisibility(visible);
		mMaterial.setVisibility(visible);
		mAuthorLv.setVisibility(visible);
		mAuthorStatus.setVisibility(visible);
		mAuthorNote.setVisibility(isAuthor ? View.GONE : View.VISIBLE);
	}


	@OnClick({R.id.iv_mine_share,
			  R.id.iv_mine_msg,
			  R.id.li_seen_mine_people,
			  R.id.li_mine_fans,
			  R.id.li_mine_percent,
			  R.id.li_mine_example,
			  R.id.li_mine_material,
			  R.id.li_mine_collect,
			  R.id.li_mine_comment_manager,
			  R.id.tv_mine_exit_login,
			  R.id.li_mine_change_password
	})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_mine_share:

				break;
			case R.id.iv_mine_msg:
				break;
			case R.id.li_seen_mine_people:
				break;
			case R.id.li_mine_fans:
				break;
			case R.id.li_mine_percent:
				break;
			case R.id.li_mine_example:
				break;
			case R.id.li_mine_material:
				break;
			case R.id.li_mine_collect:
				break;
			case R.id.li_mine_comment_manager:
				break;
			case R.id.li_mine_change_password:
				break;
			case R.id.tv_mine_exit_login:
				//退出登录，登录按钮
				jumpToLogin();
				break;
		}
	}

	//跳转到登录页面
	private void jumpToLogin() {
		Intent intent = new Intent(mActivity, LoginActivity.class);
		startActivity(intent);
	}
}
