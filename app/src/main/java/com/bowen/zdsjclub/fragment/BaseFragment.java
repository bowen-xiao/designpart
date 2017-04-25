package com.bowen.zdsjclub.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 一级子孩子的基类
 * @author bowen
 */
public abstract class BaseFragment extends Fragment {

//	每个fragment都需要一个依附的对象
	protected Activity mActivity;

	protected String TAG ;
	private View mRootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		TAG = this.getClass().getSimpleName();
		mActivity = getActivity();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		//创建一个自己的视图，每个子类的都不一样，所以需要抽象出来，需要子类自己去实现
		mRootView = initView();
		ButterKnife.bind(this, mRootView);
		return mRootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//			并不是每个子类对象都有自己的数据，所以这里不做强制要求实现
		initData();
	}
	
	/**
	 * 如果子类有自己的数据就只需要写这个方法就可以了
	 */
	public void initData(){
		
	}

//	只要有抽象方法的类，都是抽象方法
	protected  abstract View initView();

	/**
	 * 每次切换都会调用判断当前Fragment是否展现在屏幕上,在onAttach、onCreateView之前分别调用一次
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		Log.d(TAG, "BaseFragment-->setUserVisibleHint()-->" + isVisibleToUser);
		//当Fragment在屏幕上可见并且没有加载过数据时调用
		if(isVisibleToUser && mRootView != null){
			//当Fragment在屏幕可见时加载数据
			loadDataOnce();
		}
	}

	//用户可见的时候
	public void loadDataOnce(){

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

