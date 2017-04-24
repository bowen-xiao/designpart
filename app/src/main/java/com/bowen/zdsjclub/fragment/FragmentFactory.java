package com.bowen.zdsjclub.fragment;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;

/**
 * @author bowen
 */
public class FragmentFactory
{

	 // 散列的
	private static SparseArrayCompat<BaseFragment> mCaches = new SparseArrayCompat<BaseFragment>();

	public static BaseFragment getFragment(int position)
	{
		// 获得缓存
		BaseFragment fragment = mCaches.get(position);
		if (fragment != null)
		{
			return fragment;
		}

		switch (position)
		{
			case 0:
				// 首页
				fragment = new HomeFragment();
				break;
			case 1:
				// 案例
				fragment = new HomeFragment();
				break;
			case 2:
				// 关注
				fragment = new FocusFragment();
				break;
			case 3:
				// 设计师
				fragment = new HomeFragment();
				break;
			case 4:
				// 我的
				fragment = new MineFragment();
				break;
		}
		//传递的参数信息
		Bundle args = new Bundle();
		fragment.setArguments(args);
		args.putInt("index",position);
		// 存储到缓存
		mCaches.put(position, fragment);
		return fragment;
	}
	
//	将数据进行清理,退出的时候
	public static void clearCaches(){
		mCaches.clear();
	}
}
