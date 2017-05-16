package com.bowen.hannengclub.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.activity.LoginActivity;
import com.bowen.hannengclub.adapter.NewHomeFragmentFactory;
import com.bowen.hannengclub.adapter.NewHomePageAdapter;
import com.bowen.hannengclub.bean.LoginResult;
import com.bowen.hannengclub.bean.UserInfo;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.CacheUtils;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.UserUtil;
import com.bowen.hannengclub.view.PagerSlidingTabStrip;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
		//getLoginInfo();
		setSignStatus();
		getLoginInfo();
	}

	//设置点击状态
	private void getLoginInfo(){
		//如果token不存在就不同步
		String token = UserUtil.getToken(mActivity);
		if(TextUtils.isEmpty(token)){return;}
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		Map<String,Object> param = new HashMap<>();
		param.put("test","test");
		service.getLogin(param)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<LoginResult>() {
				   @Override
				   public void onCompleted() {
					   setSignStatus();
					   ToolLog.e("main getLogin", "onCompleted !");
				   }

				   @Override
				   public void onError(Throwable e) {
					   ToolLog.e("main getLogin",e.getMessage() + "请求完成 !");
				   }

				   @Override
				   public void onNext(LoginResult model) {
					   ToolLog.e("main getLogin", "onNext !" + model);
					   UserInfo item = model.getItem();
					   if(model.getStatus() == 1 && item != null){
						   //更新登录信息
						   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, JSON.toJSONString(model.getItem()));
					   }else{
						   //清空登录信息
						   CacheUtils.setString(mActivity, SysConfiguration.USER_INFO, "");
					   }
				   }
			   });
	}

	//设置签到状态
	private void setSignStatus(){
		//默认值
		mIvSign.setImageResource(R.mipmap.unremark);
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
		if(info == null){
			//提示用户登录
			DialogBean dialogBean = new DialogBean("您尚未登录您的账号，前往登录！", "", "取消", "前去登录");
			CommonMsgDialog commonMsgDialog = new CommonMsgDialog(mActivity, dialogBean);
			commonMsgDialog.setRightClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//去登录页面
					Intent intent = new Intent(mActivity, LoginActivity.class);
					startActivity(intent);
				}
			});
			commonMsgDialog.showDialog();
			return;
		}
		//已经签到过了
		if(info.getSign_status() == 1){
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
