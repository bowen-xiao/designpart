package com.bowen.hannengclub.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.activity.LoginActivity;
import com.bowen.hannengclub.view.LineItemView;

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
			  R.id.ll_mine_head_info_root,
			  R.id.li_mine_change_password
	})
	public void onClick(View view) {
		String url = "";
		/**
		 * 我的积分	/account/point.aspx
		 */
		switch (view.getId()) {
			case R.id.iv_mine_share:
				//Todo 分享我的名片
				break;
			case R.id.ll_mine_head_info_root:
				//个人信息	/account/view.aspx
				url = "account/view.aspx";
				break;
			case R.id.iv_mine_msg:
				// 消息	/account/message.aspx
				url = "account/message.aspx";
				break;
			case R.id.li_seen_mine_people:
				//谁看过我
				break;
			case R.id.li_mine_fans:
				//我的粉丝

				break;
			case R.id.li_mine_percent:
				//我的积分
				url = "account/point.aspx";
				break;
			case R.id.li_mine_example:
				//我的案例
				break;
			case R.id.li_mine_material:
				//我的素材
				break;
			case R.id.li_mine_collect:
				// 我的收藏	/account/favorite_case.aspx
				url = "account/favorite_case.aspx";
				break;
			case R.id.li_mine_comment_manager:
				//评论管理
				break;
			case R.id.li_mine_change_password:
//				修改密码	/account/password.aspx
				url = "account/password.aspx";
				break;
			case R.id.tv_mine_exit_login:
				//退出登录，登录按钮
				jumpToLogin();
				break;
		}
		//如果URL为空直接返回
		if(TextUtils.isEmpty(url)){return;}
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, SysConfiguration.BASE_URL + url);
		startActivity(intent);
	}

	//跳转到登录页面
	private void jumpToLogin() {
		Intent intent = new Intent(mActivity, LoginActivity.class);
		startActivity(intent);
	}
}
